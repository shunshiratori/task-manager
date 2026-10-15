# API実行ガイド

## 準備

[README](../../../README.md#セットアップ)の手順でアプリを起動します。curl・Python 3・Bashが必要です。以下のスクリプトはユーザーを2人作成するため、一時的な開発DBで実行してください。Task・Projectは最後に削除しますが、ユーザー削除APIがないためユーザーは残ります。

```bash
BASE_URL=http://localhost:8080 bash scripts/api-example.sh
```

スクリプトは登録 → ログイン → Project作成 → Task作成・一覧・詳細 → 更新 → 削除を実行します。未認証の401、他人のTaskの取得・更新・削除の404も確認し、想定と違うHTTPステータスなら失敗します。

## 手動で試す

ユーザー登録（200・本文なし）:

```bash
curl -i -H 'Content-Type: application/json' \
  -d '{"name":"taro","mail":"taro@example.com","password":"example-password-123"}' \
  http://localhost:8080/users
```

ログイン（200・JWT文字列）:

```bash
TOKEN=$(curl -fsS -H 'Content-Type: application/json' \
  -d '{"mail":"taro@example.com","password":"example-password-123"}' \
  http://localhost:8080/login)
```

Task一覧（200・JSON配列）:

```bash
curl -i -H "Authorization: Bearer $TOKEN" http://localhost:8080/tasks
```

未認証（401）:

```bash
curl -i http://localhost:8080/tasks
```

存在しない、または他人のTask（404。IDは環境に合わせて置き換え）:

```bash
curl -i -H "Authorization: Bearer $TOKEN" http://localhost:8080/tasks/999999
```

作成・更新のJSONとレスポンスは[Task API](TasksAPI.md)・[Project API](ProjectsAPI.md)を参照してください。エラー本文の形式はSpring Bootの設定に依存するため、ここではHTTPステータスを確認します。
