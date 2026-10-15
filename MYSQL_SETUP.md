# MySQL 設定ガイド

## 設定ファイルの役割

| ファイル | 役割 |
|---|---|
| `application.yml` | アプリ名・デフォルトプロファイル・JWTなどの共通設定 |
| `application-local.yml` | ローカルMySQL。デフォルトで有効。スキーマは`update` |
| `application-prod.yml` | 本番MySQL。明示的に有効化。スキーマは`validate` |

`dev`プロファイルは用意していません。ローカル起動はプロファイル指定不要です。

## ローカル開発

DockerでのDB起動と環境変数の読み込みは[README](README.md#セットアップ)を参照してください。

既存のMySQLを使う場合は、先にデータベースを作成します。Dockerの`MYSQL_DATABASE`で作成済みなら不要です。

```sql
CREATE DATABASE IF NOT EXISTS taskmanager;
```

`.env.example`を`.env`にコピーし、接続情報と`JWT_SECRET`を設定します。`.env`はGit管理対象外で、自動読み込みはされません。以下は自分で管理するシェル形式のファイルを読み込む手順です。空白や特殊文字を含む値はクォートしてください。

```bash
set -a
. ./.env
set +a
./mvnw spring-boot:run
```

## 環境変数

| 変数 | localの既定値 | prod |
|---|---|---|
| `DB_HOST` | `localhost` | 必須 |
| `DB_PORT` | `3306` | 必須 |
| `DB_NAME` | `taskmanager` | 必須 |
| `DB_USER` | `root` | 必須 |
| `DB_PASSWORD` | `root` | 必須 |
| `JWT_SECRET` | なし | 両環境で必須。UTF-8で32バイト以上 |

JWT鍵は`openssl rand -hex 32`などで生成します。ローカルと本番で別の鍵を使用してください。

## JARの作成と起動

READMEのテスト手順で確認した後にJARを作成します。以下はローカルDBと環境変数が準備済みの場合です。

```bash
./mvnw package
java -jar target/taskManager-0.0.1-SNAPSHOT.jar
```

## 本番MySQL / RDS

本番環境には上記の全環境変数を設定し、次のように起動します。

```bash
java -jar target/taskManager-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

- 本番設定はTLS接続（`useSSL=true`）を使用します。証明書・接続先検証の要件は配備環境に合わせて確認してください。
- RDSなどのネットワーク設定では、アプリケーションからDBの3306番ポートへ接続できるようにします。
- `ddl-auto: validate`はテーブルを作成・変更しません。起動前にEntityに対応するスキーマを用意してください。現時点ではマイグレーション機構は未導入です。
- ローカルの`update`も変更履歴を管理する仕組みではありません。本番のスキーマ移行には使用しません。

## トラブルシューティング

| 症状 | 確認する内容 |
|---|---|
| Connection refused | MySQLの起動状態、ホスト、ポート |
| Access denied | DBユーザー、パスワード、接続権限 |
| Unknown database | `DB_NAME`に指定したDBの存在 |
| JWT設定エラー | `JWT_SECRET`の設定とUTF-8でのバイト数 |
| Schema validationエラー | 本番DBのスキーマとEntityの一致 |
