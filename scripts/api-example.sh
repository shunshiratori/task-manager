#!/usr/bin/env bash
# 開発用API実行例。ユーザー・プロジェクトを作成するため、一時環境で実行する。
set -euo pipefail
BASE_URL="${BASE_URL:-http://localhost:8080}"
RUN_ID="$(date +%s)-$$"
PASSWORD='example-password-123'
MAIL="demo-${RUN_ID}@example.com"
OTHER_MAIL="other-${RUN_ID}@example.com"
json_field() { python3 -c 'import json,sys; print(json.load(sys.stdin)[sys.argv[1]])' "$1"; }
jwt_user_id() {
  python3 -c 'import base64,json,sys; p=sys.stdin.read().strip().split(".")[1]; print(json.loads(base64.urlsafe_b64decode(p+"="*(-len(p)%4)))["sub"])'
}
expect_status() {
  local expected="$1"; shift
  local actual
  actual=$(curl -sS -o /dev/null -w '%{http_code}' "$@")
  if [ "$actual" != "$expected" ]; then echo "Expected HTTP $expected, got $actual" >&2; exit 1; fi
}
expect_status 200 -H 'Content-Type: application/json' -d "{\"name\":\"demo\",\"mail\":\"$MAIL\",\"password\":\"$PASSWORD\"}" "$BASE_URL/users"
TOKEN=$(curl -fsS -H 'Content-Type: application/json' -d "{\"mail\":\"$MAIL\",\"password\":\"$PASSWORD\"}" "$BASE_URL/login")
# デモ用にJWTのsubを読むだけであり、署名検証はサーバーが行う。
USER_ID=$(printf '%s' "$TOKEN" | jwt_user_id)
PROJECT=$(curl -fsS -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' -d "{\"title\":\"API example\",\"status\":0,\"userId\":$USER_ID}" "$BASE_URL/projects")
PROJECT_ID=$(printf '%s' "$PROJECT" | json_field projectId)
TASK=$(curl -fsS -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' -d "{\"title\":\"First task\",\"content\":\"Example\",\"userId\":$USER_ID,\"projectId\":$PROJECT_ID}" "$BASE_URL/tasks")
TASK_ID=$(printf '%s' "$TASK" | json_field id)
printf '%s' "$TASK" | python3 -c 'import json,sys; d=json.load(sys.stdin); assert "projectId" in d and "project_id" not in d'
curl -fsS -H "Authorization: Bearer $TOKEN" "$BASE_URL/tasks" | python3 -m json.tool
curl -fsS -H "Authorization: Bearer $TOKEN" "$BASE_URL/tasks/$TASK_ID" | python3 -m json.tool
expect_status 401 "$BASE_URL/tasks"
expect_status 200 -H 'Content-Type: application/json' -d "{\"name\":\"other\",\"mail\":\"$OTHER_MAIL\",\"password\":\"$PASSWORD\"}" "$BASE_URL/users"
OTHER_TOKEN=$(curl -fsS -H 'Content-Type: application/json' -d "{\"mail\":\"$OTHER_MAIL\",\"password\":\"$PASSWORD\"}" "$BASE_URL/login")
expect_status 404 -H "Authorization: Bearer $OTHER_TOKEN" "$BASE_URL/tasks/$TASK_ID"
expect_status 404 -X PUT -H "Authorization: Bearer $OTHER_TOKEN" -H 'Content-Type: application/json' -d '{"title":"forbidden","content":"forbidden","status":1}' "$BASE_URL/tasks/$TASK_ID"
expect_status 404 -X DELETE -H "Authorization: Bearer $OTHER_TOKEN" "$BASE_URL/tasks/$TASK_ID"
curl -fsS -X PUT -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' -d '{"title":"Updated task","content":"Example","status":1}' "$BASE_URL/tasks/$TASK_ID" | python3 -m json.tool
expect_status 200 -X DELETE -H "Authorization: Bearer $TOKEN" "$BASE_URL/tasks/$TASK_ID"
expect_status 404 -H "Authorization: Bearer $TOKEN" "$BASE_URL/tasks/$TASK_ID"
expect_status 200 -X DELETE -H "Authorization: Bearer $TOKEN" "$BASE_URL/projects/$PROJECT_ID"
echo 'API examples passed (users remain in the demo database).'
