# ユーザー登録・認証API

Base URL: `http://localhost:8080`。JSONリクエストは`Content-Type: application/json`を指定します。

## POST /users

認証不要。ユーザーを作成します。成功時は **200・本文なし**です。

```json
{"name":"taro","mail":"taro@example.com","password":"example-password-123"}
```

メール項目は`mail`です。ユーザー一覧・詳細取得APIは未実装です。

## POST /login

認証不要。成功時は **200・JWT文字列**を返します（JSONオブジェクトではありません）。

```json
{"mail":"taro@example.com","password":"example-password-123"}
```

認証失敗時は401。トークンの有効期間は1時間です。他のAPIでは`Authorization: Bearer <token>`を指定します。

実行例は[API実行ガイド](EXAMPLES.md)を参照してください。
