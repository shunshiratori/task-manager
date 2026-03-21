## Base URL

```
http://localhost:8080
```

# プロジェクト作成

## Endpoint
POST /projects

## Description
プロジェクトを新規作成する。

### Request
Content-Type: application/json

| Field  | Type   | Required | Description |
|--------|--------|----------|-------------|
| title  | string | yes      | プロジェクト名     |
| status | string | yes      | プロジェクト状況    |

### Request Example
```json
{
  "title": "タスクタイトル",
  "status": 0
}
```

### Response
Status: 200 OK

```json
{
  "project_id": 1,
  "title": "タスクタイトル",
  "status": 0
}
```

# プロジェクト一覧取得

## Endpoint
GET /projects

## Description
登録されているプロジェクト一覧を取得する。

### Response Example

```json
[
  {
    "id": 1,
    "title": "タスクタイトル",
    "status": "タスク詳細"
  }
]
```

---
# プロジェクト詳細取得

## Endpoint
GET /projects/{id}

## Description
登録されているプロジェクトに紐づいたタスクを取得する。

### Response Example

```json
[
  {
    "id": 1,
    "project_id": 1,
    "title": "タスクタイトル",
    "content": "タスク詳細",
    "tasks": [
      {
        "id": 1,
        "title": "タスク",
        "content": "タスク内容",
        "status": 0
      }
    ]
  }
]
```

# プロジェクト更新

## Endpoint
PUT /projects/{id}

## Description
指定したprojectを更新する。

### Path Parameter

| Parameter | Type   | Description |
|-----------|--------|-------------|
| id        | number | タスクID       |

### Request
Content-Type: application/json

| Field   | Type   | Required | Description |
|---------|--------|----------|-------------|
| title   | string | yes      | プロジェクト名     |
| status  | int    | yes      | プロジェクト状況    |

### Request Example
```json
{
  "title": "タスクタイトル",
  "status": 1
}
```
### Response Example
```json
{
  "id": 1,
  "title": "taro",
  "status": "taro@example.com"
}
```

# 4. タスク削除

## Endpoint
DELETE /projects/{id}

## Description
指定したプロジェクトを削除する。

### Response
Status: 204 No Content
