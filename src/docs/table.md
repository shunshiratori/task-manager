# テーブル設計
## Users
| Field        | Type   | Required | Description | memo |
|--------------|--------|----------|-------------|------|
| user_id      | number | yes      | ユーザーid      |      |
| name         | string | yes      | ユーザー名       |      |
| mail         | string | yes      | メールアドレス     |      |
| password     | string | yes      | パスワード       |      |
| authority_id | number | yes      | 権限id        |      |

## Tasks
| Field        | Type      | Required | Description | memo             |
|--------------|-----------|----------|-------------|------------------|
| id           | number    | yes      | タスクid       |                  |
| title        | string    | yes      | タスク名        |                  |
| content      | string    | no       | タスク詳細       |                  |
| status       | string    | yes      | タスク状況       | 0:未実施、1:実行中、2:完了 |
| created_at   | timestamp | yes      | 作成日時        |                  |
| updated_at   | timestamp | yes      | 更新日時        |                  |
| project_id   | number    | yes      | プロジェクトid    | projectsテーブルと連携  |
| user_id      | number    | yes      | ユーザーid      | Usersテーブルと連携     |
| authority_id | number    | yes      | 権限id        |                  |

## Projects
| Field        | Type      | Required | Description | memo         |
|--------------|-----------|----------|-------------|--------------|
| project_id   | number    | yes      | プロジェクトid    |              |
| title        | string    | yes      | プロジェクト名     |              |
| status       | int       | yes      | プロジェクト状況    |              |
| created_at   | timestamp | yes      | 作成日時        |              |
| updated_at   | timestamp | yes      | 更新日時        |              |
| user_id      | number    | yes      | ユーザーid      | Usersテーブルと連携 |
| authority_id | number    | yes      | 権限id        |              |
