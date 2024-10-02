--DB作成
create database kansatsu;
USE kansatsu;

--文字コード、文字照合順序を設定
ALTER DATABASE kansatsu
CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;

--テーブル作成
CREATE TABLE users (
    id INT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    account VARCHAR(30) NOT NULL COMMENT 'アカウントID',
    pass VARCHAR(60) NOT NULL COMMENT 'パスワード',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '削除フラグ',
    create_date_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登録日時',
    update_date_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
    PRIMARY KEY(id),
    UNIQUE(account),
    INDEX IX_users_account (account)
) COMMENT = 'ユーザー情報'
;

CREATE TABLE profiles (
    id INT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    users_id INT NOT NULL COMMENT 'ユーザーID',
    nickname VARCHAR(30) NOT NULL COMMENT 'ニックネーム',
    profile VARCHAR(160) COMMENT 'プロフィール詳細',
    file_path VARCHAR(255) COMMENT '画像ファイルパス',
    create_date_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登録日時',
    update_date_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
    PRIMARY KEY(id),
    INDEX IX_profiles_users_id (users_id)
) COMMENT = 'プロフィール情報'
;

CREATE TABLE plants (
    id INT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    users_id INT NOT NULL COMMENT 'ユーザーID',
    name VARCHAR(30) COMMENT '植物名',
    detail VARCHAR(1000) COMMENT '植物詳細',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '削除フラグ',
    create_date_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登録日時',
    update_date_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
    PRIMARY KEY(id),
    INDEX IX_plants_name (name),
    INDEX IX_plants_users_id (users_id)
) COMMENT = '植物データ'
;

CREATE TABLE plantfiles (
    id INT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    plants_id INT NOT NULL COMMENT '植物データID',
    display_order INT NOT NULL COMMENT '画像表示順',
    file_path VARCHAR(255) NOT NULL COMMENT '画像ファイルパス',
    create_date_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登録日時',
    update_date_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
    PRIMARY KEY(id),
    INDEX IX_plantfiles_plants_id (plants_id)    
) COMMENT = '植物画像データ'
;

CREATE TABLE diaries (
    id INT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    plants_id INT NOT NULL COMMENT '植物データID',
    users_id INT NOT NULL COMMENT 'ユーザーID',
    observation_date DATE COMMENT '観察日',
    detail VARCHAR(1000) COMMENT '記録',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '削除フラグ',
    create_date_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登録日時',
    update_date_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
    PRIMARY KEY(id),
    INDEX IX_diaries_plants_id (plants_id)
) COMMENT = '観察日記データ'
;

CREATE TABLE diaryfiles (
    id INT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    diaries_id INT NOT NULL COMMENT '観察日記データID',
    display_order INT NOT NULL COMMENT '画像表示順',
    file_path VARCHAR(255) NOT NULL COMMENT '画像ファイルパス',
    create_date_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登録日時',
    update_date_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
    PRIMARY KEY(id),
    INDEX IX_diaryfiles_diaries_id (diaries_id)
) COMMENT = '観察日記画像データ'
;

