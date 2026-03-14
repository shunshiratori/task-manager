## Base URL

```
http://localhost:8080
```

---

# 1. タスク作成

## Endpoint

POST /tasks

## Description

ユーザーを新規作成する。

### Request

Content-Type: application/json

| Field   | Type   | Required | Description |
|---------|--------|----------|-------------|
| title   | string | yes      | タスク名        |
| content | string | yes      | タスク詳細       |

### Request Example

```json
{
  "title": "タスクタイトル",
  "content": "タスク詳細"
}
```

---

### Response

Status: 200 OK

```json
{
  "id": 1,
  "title": "タスクタイトル",
  "content": "タスク詳細"
}
```

---

# 2. タスク一覧取得

## Endpoint

GET /tasks

## Description

登録されているタスク一覧を取得する。

### Response Example

```json
[
  {
    "id": 1,
    "title": "タスクタイトル",
    "content": "タスク詳細"
  }
]
```

---

# 3. タスク更新

## Endpoint

PUT /tasks/{id}

## Description

指定したタスクを更新する。

### Path Parameter

| Parameter | Type   | Description |
|-----------|--------|-------------|
| id        | number | タスクID       |

### Request Example

```json
{
  "title": "タスクタイトル",
  "content": "タスク詳細",
  "status": "1"
  
}
```

### Request

Content-Type: application/json

| Field   | Type   | Required | Description |
|---------|--------|----------|-------------|
| title   | string | yes      | タスク名        |
| content | string | yes      | タスク詳細       |
| status  | int    | yes      | タスク状況       |

---
### Response Example

```json
{
  "id": 1,
  "name": "taro",
  "email": "taro@example.com"
}
```

---

# 4. タスク削除

## Endpoint

DELETE /tasks/{id}

## Description

指定したタスクを削除する。

### Response

Status: 204 No Content
