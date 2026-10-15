# Project API

全操作で`Authorization: Bearer <token>`が必要です。JSONリクエストには`Content-Type: application/json`を指定します。

| 操作 | エンドポイント | 成功時 |
|---|---|---|
| 作成 | `POST /projects` | 200・ProjectCreateResponse |
| 一覧 | `GET /projects?userId=1` | 200・ProjectResponseの配列 |
| 詳細 | `GET /projects/{projectId}` | 200・ProjectFindResponse |
| 更新 | `PUT /projects/{projectId}` | 200・ProjectResponse |
| 削除 | `DELETE /projects/{projectId}` | 200・本文なし |

現状、Projectはログインユーザーによる所有者制限が未実装です。一覧の`userId`は任意の検索条件で、省略すると全件を返します。Taskの所有者制限と同じ保証はありません。

## 作成

```json
{"title":"API example","status":0,"userId":1}
```

レスポンス:

```json
{"projectId":1,"title":"API example","status":0,"userId":1,"authorityId":0}
```

## 一覧

上記と同じフィールドのオブジェクトを配列で返します。

## 詳細

配列ではなく、関連Taskを含む単一オブジェクトです。

```json
{"projectId":1,"title":"API example","status":0,"authorityId":0,"tasks":[{"id":1,"title":"First task","content":"Example","status":0,"projectId":1,"userId":null}]}
```

現実装では詳細レスポンスの`authorityId`はDTOの初期値、内包するTaskの`userId`は未設定です。

## 更新

```json
{"title":"Updated project","status":1,"authorityId":0}
```

レスポンス例:

```json
{"projectId":1,"title":"Updated project","status":1,"authorityId":0,"userId":null}
```

更新レスポンスの`userId`は現実装では未設定です。Projectの存在しないIDへの取得・更新は、現状404への変換がなく500になります。削除前に関連Taskを削除してください。

[実行例](EXAMPLES.md)
