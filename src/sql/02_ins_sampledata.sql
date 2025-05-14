/** サンプルデータ **/

/** 初期化 **/
delete from users;
delete from profiles;
delete from plants;
delete from plantfiles;
delete from diaries;
delete from diaryfiles;

/** users **/
/** アカウントID: testuser パスワード: a **/
INSERT INTO `users` (`id`,`account`,`pass`,`is_deleted`,`create_date_time`,`update_date_time`) VALUES (2,'testuser','$2a$10$IEopuIE4kkqvlqQNzdtfeekLoRqHyHILbSNEm6YJpPwYK9KRuKh9.',0,'2025-05-09 18:22:14','2025-05-09 18:22:14');

/** profiles **/
INSERT INTO `profiles` (`id`,`users_id`,`nickname`,`profile`,`file_path`,`is_deleted`,`create_date_time`,`update_date_time`) VALUES (2,2,'植物花子','植物大好き。観葉植物をたくさん育てています。','2_2_20250514.jpg',0,'2025-05-09 18:22:14','2025-05-14 10:41:26');

/** plants **/
INSERT INTO `plants` (`id`,`users_id`,`name`,`detail`,`is_deleted`,`create_date_time`,`update_date_time`) VALUES (24,2,'ベゴニア','2024年春頃購入。\r\n根っこが細いため、有機質の土を使用。',0,'2025-05-14 10:42:50','2025-05-14 10:42:50');
INSERT INTO `plants` (`id`,`users_id`,`name`,`detail`,`is_deleted`,`create_date_time`,`update_date_time`) VALUES (25,2,'ゲオメトリクス','根腐れ注意\r\n',0,'2025-05-14 10:43:54','2025-05-14 10:43:54');
INSERT INTO `plants` (`id`,`users_id`,`name`,`detail`,`is_deleted`,`create_date_time`,`update_date_time`) VALUES (26,2,'モンステラ','100均で購入\r\n脅威の育成スピード',0,'2025-05-14 10:52:39','2025-05-14 10:52:39');
INSERT INTO `plants` (`id`,`users_id`,`name`,`detail`,`is_deleted`,`create_date_time`,`update_date_time`) VALUES (27,2,'サボテン高砂','年々花の数が増えている。\r\n根腐れ注意。夏冬はしっかり乾かしてから水やり。\r\n',0,'2025-05-14 11:14:05','2025-05-14 11:14:05');

/** plantfiles **/
INSERT INTO `plantfiles` (`id`,`plants_id`,`display_order`,`file_path`,`is_deleted`,`create_date_time`,`update_date_time`) VALUES (51,24,1,'51_24_20250514.jpg',0,'2025-05-14 10:42:50','2025-05-14 10:42:51');
INSERT INTO `plantfiles` (`id`,`plants_id`,`display_order`,`file_path`,`is_deleted`,`create_date_time`,`update_date_time`) VALUES (52,25,1,'52_25_20250514.jpg',0,'2025-05-14 10:43:54','2025-05-14 10:43:55');
INSERT INTO `plantfiles` (`id`,`plants_id`,`display_order`,`file_path`,`is_deleted`,`create_date_time`,`update_date_time`) VALUES (53,26,1,'53_26_20250514.jpg',0,'2025-05-14 10:52:39','2025-05-14 10:52:40');
INSERT INTO `plantfiles` (`id`,`plants_id`,`display_order`,`file_path`,`is_deleted`,`create_date_time`,`update_date_time`) VALUES (54,26,2,'54_26_20250514.jpg',0,'2025-05-14 10:52:40','2025-05-14 10:52:41');
INSERT INTO `plantfiles` (`id`,`plants_id`,`display_order`,`file_path`,`is_deleted`,`create_date_time`,`update_date_time`) VALUES (55,27,1,'55_27_20250514.jpg',0,'2025-05-14 11:14:05','2025-05-14 11:14:06');
INSERT INTO `plantfiles` (`id`,`plants_id`,`display_order`,`file_path`,`is_deleted`,`create_date_time`,`update_date_time`) VALUES (56,27,2,'56_27_20250514.jpg',0,'2025-05-14 11:14:06','2025-05-14 11:14:07');

/** diaries **/
INSERT INTO `diaries` (`id`,`plants_id`,`users_id`,`observation_date`,`detail`,`is_deleted`,`create_date_time`,`update_date_time`) VALUES (19,27,2,'2025-04-16','満開。\r\n今年もたくさん咲きました。',0,'2025-05-14 11:18:20','2025-05-14 11:19:37');
INSERT INTO `diaries` (`id`,`plants_id`,`users_id`,`observation_date`,`detail`,`is_deleted`,`create_date_time`,`update_date_time`) VALUES (20,27,2,'2025-05-14','今日もたくさん咲いています。',0,'2025-05-14 11:18:57','2025-05-14 11:18:57');

/** diaryfiles **/
INSERT INTO `diaryfiles` (`id`,`diaries_id`,`display_order`,`file_path`,`is_deleted`,`create_date_time`,`update_date_time`) VALUES (20,19,1,'20_19_20250514.jpg',0,'2025-05-14 11:18:20','2025-05-14 11:18:21');
INSERT INTO `diaryfiles` (`id`,`diaries_id`,`display_order`,`file_path`,`is_deleted`,`create_date_time`,`update_date_time`) VALUES (21,19,2,'21_19_20250514.jpg',0,'2025-05-14 11:18:21','2025-05-14 11:18:22');
INSERT INTO `diaryfiles` (`id`,`diaries_id`,`display_order`,`file_path`,`is_deleted`,`create_date_time`,`update_date_time`) VALUES (22,20,1,'22_20_20250514.jpg',0,'2025-05-14 11:18:57','2025-05-14 11:18:58');
