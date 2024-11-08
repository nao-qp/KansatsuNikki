--テストデータ
delete from users;
delete from profiles;
delete from plants;
delete from diaries;

INSERT INTO users (id, account, pass, is_deleted)
VALUES(
1
,'test'
,'test'
,0
);

INSERT INTO profiles (id, users_id, nickname, profile, file_path)
VALUES(
1
,1
,'testのニックネーム'
,'profileテキスト情報'
,'1_1_prof_20240912.jpg'
);

INSERT INTO plants (id, users_id, name, detail, is_deleted)
VALUES(1,1,'パキラ','パキラ植物詳細テキスト情報',0);
INSERT INTO plants (id, users_id, name, detail, is_deleted)
VALUES(2,1,'カポック','カポック植物詳細テキスト情報',0);
INSERT INTO plants (id, users_id, name, detail, is_deleted)
VALUES(3,1,'サボテン','サボテン植物詳細テキスト情報',0);
INSERT INTO plants (id, users_id, name, detail, is_deleted)
VALUES(4,1,'リプサリス','リプサリス植物詳細テキスト情報',0);
INSERT INTO plants (id, users_id, name, detail, is_deleted)
VALUES(5,1,'ウンベラータ','ウンベラータ植物詳細テキスト情報',0);


--diaries
INSERT INTO diaries (id, plants_id, users_id, observation_date, detail, is_deleted)
VALUES(1,1,1,'2024-09-12','観察日記のテキスト情報',0);
INSERT INTO diaries (id, plants_id, users_id, observation_date, detail, is_deleted)
VALUES(2,1,1,'2024-11-07','観察日記のテキスト情報',0);

--plantfiles
INSERT INTO plantfiles (id, plants_id, display_order, file_path)
VALUES(1,1,1,'1_1_20240914.jpg');
INSERT INTO plantfiles (id, plants_id, display_order, file_path)
VALUES(2,2,1,'2_2_20240914.jpg');
INSERT INTO plantfiles (id, plants_id, display_order, file_path)
VALUES(3,3,1,'3_3_20240914.jpg');
INSERT INTO plantfiles (id, plants_id, display_order, file_path)
VALUES(4,4,1,'4_4_20240914.jpg');
INSERT INTO plantfiles (id, plants_id, display_order, file_path)
VALUES(5,5,1,'5_5_20240914.jpg');
--diaryfiles
INSERT INTO diaryfiles (id, diaries_id, display_order, file_path)
VALUES(1,1,1,'1_1_20241107.jpg');
INSERT INTO diaryfiles (id, diaries_id, display_order, file_path)
VALUES(2,1,,'1_1_20241107.jpg');

