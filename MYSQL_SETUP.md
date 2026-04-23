# MySQL 設定ガイド

このプロジェクトはMySQLを使用しています。以下の手順に従って環境を構築してください。

## ローカル開発環境

### 1. MySQLのインストール

**Mac (Homebrew):**
```bash
brew install mysql
brew services start mysql
```

**Windows:**
- [MySQL公式サイト](https://dev.mysql.com/downloads/mysql/)からダウンロード
- インストーラーを実行

**Docker:**
```bash
docker run --name mysql-local \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=taskmanager \
  -p 3306:3306 \
  -d mysql:8.0
```

### 2. データベースの作成

```bash
mysql -u root -p
# パスワード: root

CREATE DATABASE taskmanager;
USE taskmanager;
```

### 3. アプリケーション起動

```bash
mvn spring-boot:run
```

**または**

```bash
mvn package
java -jar target/taskManager-0.0.1-SNAPSHOT.jar
```

## AWS デプロイ (RDS for MySQL)

### 1. AWS RDS インスタンスの作成

1. AWSコンソール → RDS
2. **データベースの作成** をクリック
3. エンジン選択: **MySQL 8.0**
4. テンプレート: **無料利用枠** ✅
5. 以下の情報を設定:
   - **DB インスタンス識別子**: taskmanager-db
   - **マスターユーザー名**: admin
   - **マスターパスワード**: [安全なパスワード]
   - **DBインスタンスクラス**: db.t3.micro (無料枠)
   - **ストレージ**: 20GB

6. **データベース作成**

### 2. セキュリティグループの設定

1. RDS インスタンス選択
2. **VPC セキュリティグループ** を確認
3. インバウンドルール: ポート 3306 を開く
   - ソース: アプリケーションのセキュリティグループ

### 3. 環境変数の設定

アプリケーションをデプロイする環境（EC2, ECS, Beanstalk など）で以下を設定:

```bash
export DB_HOST=taskmanager-db.xxxxx.ap-northeast-1.rds.amazonaws.com
export DB_PORT=3306
export DB_NAME=taskmanager
export DB_USER=admin
export DB_PASSWORD=your_secure_password
```

### 4. アプリケーション起動

```bash
# prod プロファイルで起動
java -jar target/taskManager-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod
```

## 環境ファイル (.env)

ローカル開発環境で環境変数を管理する場合:

**.env ファイル (gitignore に追加):**
```
DB_HOST=localhost
DB_PORT=3306
DB_NAME=taskmanager
DB_USER=root
DB_PASSWORD=root
```

**起動時に読み込む:**
```bash
export $(cat .env | xargs)
java -jar target/taskManager-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=dev
```

## 設定ファイルの説明

- **application.yml**: ローカル開発用（デフォルト）
  - H2互換の設定
  - `ddl-auto: create` → テーブル自動作成

- **application-prod.yml**: 本番用（RDS）
  - 環境変数から接続情報を読み込み
  - `ddl-auto: validate` → スキーマ検証のみ

## トラブルシューティング

### Connection refused
```
原因: MySQLが起動していない
解決: brew services start mysql (Mac)
     または MySQL のサービスを開始 (Windows)
```

### Access denied
```
原因: ユーザー名またはパスワードが違う
確認: mysql -u root -p で接続試行
```

### Unknown database
```
原因: taskmanager データベースが存在しない
解決: CREATE DATABASE taskmanager; を実行
```

## RDS 接続確認

```bash
# ローカルから RDS への接続確認
mysql -h taskmanager-db.xxxxx.ap-northeast-1.rds.amazonaws.com \
      -u admin -p taskmanager
```
