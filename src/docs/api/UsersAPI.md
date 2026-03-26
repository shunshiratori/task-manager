# TaskManager API Specification
## Base URL
```
http://localhost:8080
```

# ユーザー作成
## Endpoint
POST /users

## Description
ユーザーを新規作成する。

### Request
Content-Type: application/json

| Field    | Type   | Required | Description |
|----------|--------|----------|-------------|
| name     | string | yes      | ユーザー名       |
| email    | string | yes      | メールアドレス     |
| password | string | yes      | パスワード       |

### Request Example
```json
{
  "name": "taro",
  "email": "taro@example.com",
  "password": "1234"
}
```

### Response
Status: 200 OK
```json
{
  "id": 1,
  "name": "taro",
  "email": "taro@example.com"
}
```

# ユーザー一覧取得
## Endpoint
GET /users

## Description
登録されているユーザー一覧を取得する。

### Response Example
```json
[
  {
    "id": 1,
    "name": "taro",
    "email": "taro@example.com"
  }
]
```

# ユーザー取得
## Endpoint
GET /users/{id}

## Description
指定したユーザーを取得する。

### Path Parameter
| Parameter | Type    | Description |
| --------- | ------- | ----------- |
| id        | integer | ユーザーID      |

### Response Example
```json
{
  "id": 1,
  "name": "taro",
  "email": "taro@example.com"
}
```
