# Task API

Base URL: `http://localhost:8080`。全操作で`Authorization: Bearer <token>`が必要です。JSONリクエストには`Content-Type: application/json`を指定します。

| 操作 | エンドポイント | 成功時 |
|---|---|---|
| 作成 | `POST /tasks` | 200・TaskCreateResponse |
| 一覧 | `GET /tasks` | 200・TaskResponseの配列 |
| 詳細 | `GET /tasks/{id}` | 200・TaskResponse |
| 更新 | `PUT /tasks/{id}` | 200・TaskUpdateResponse |
| 削除 | `DELETE /tasks/{id}` | 200・本文なし |

一覧はログインユーザーのTaskのみを返します。詳細・更新・削除はIDとログインユーザーのuserIdで検索し、存在しないTaskと他人のTaskはいずれも404です。トークンなし・不正トークンは401です。

## 作成

```json
{"title":"First task","content":"Example","userId":1,"projectId":1}
```

レスポンス例:

```json
{"id":1,"title":"First task","content":"Example","userId":1,"projectId":1}
```

`userId`・`projectId`は省略可能ですが、作成時の所有者は現状リクエストの`userId`で決まります。自分のTaskとして一覧・取得するにはログインユーザーのIDを指定します。作成時の所有者・Projectへのアクセス制御は今後の対応事項です。

## 一覧・詳細

一覧は次のオブジェクトの配列、詳細は単一オブジェクトを返します。

```json
{"id":1,"title":"First task","content":"Example","status":0,"projectId":1,"userId":1}
```

## 更新

```json
{"title":"Updated task","content":"Example","status":1,"projectId":1}
```

レスポンス例:

```json
{"id":1,"title":"Updated task","content":"Example","status":1,"projectId":1}
```

`status`は整数（0:未実施、1:実行中、2:完了）です。部分更新APIではないため`title`・`content`・`status`をまとめて送信してください。`projectId`を省略した場合は現在の関連付けを維持します。

## JSON名の変更

作成レスポンスの`project_id`を`projectId`に統一しました。以前のキーを参照する利用側は修正が必要です。DBのカラム名は変更していません。

[実行例](EXAMPLES.md)
