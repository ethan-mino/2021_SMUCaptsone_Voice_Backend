INSERT INTO emoji_category(name, color) VALUES("매우 좋음", "#13A660");
INSERT INTO emoji_category(name, color) VALUES("좋음", "#13EC95");
INSERT INTO emoji_category(name, color) VALUES("보통", "#04B8F7");
INSERT INTO emoji_category(name, color) VALUES("나쁨", "#FFA802");
INSERT INTO emoji_category(name, color) VALUES("매우 나쁨", "#FF0004");

INSERT INTO emoji(text, category_id) VALUES("행복해요", "1");
INSERT INTO emoji(text, category_id) VALUES("기뻐요", "1");
INSERT INTO emoji(text, category_id) VALUES("끝내줘요", "1");
INSERT INTO emoji(text, category_id) VALUES("최고예요", "1");

INSERT INTO emoji(text, category_id) VALUES("설레요", "2");
INSERT INTO emoji(text, category_id) VALUES("편안해요", "2");
INSERT INTO emoji(text, category_id) VALUES("즐거워요", "2");
INSERT INTO emoji(text, category_id) VALUES("신나요", "2");

INSERT INTO emoji(text, category_id) VALUES("심심해요", "3");
INSERT INTO emoji(text, category_id) VALUES("당황스러워요", "3");
INSERT INTO emoji(text, category_id) VALUES("잘 모르겠어요", "3");
INSERT INTO emoji(text, category_id) VALUES("묘해요", "3");

INSERT INTO emoji(text, category_id) VALUES("언짢아요", "4");
INSERT INTO emoji(text, category_id) VALUES("불편해요", "4");
INSERT INTO emoji(text, category_id) VALUES("우울해요", "4");
INSERT INTO emoji(text, category_id) VALUES("짜증나요", "4");

INSERT INTO emoji(text, category_id) VALUES("끔찍해요", "5");
INSERT INTO emoji(text, category_id) VALUES("역겨워요", "5");
INSERT INTO emoji(text, category_id) VALUES("슬퍼요", "5");
INSERT INTO emoji(text, category_id) VALUES("화나요", "5");



INSERT INTO chat_mode (name) VALUES("formal");
INSERT INTO chat_mode (name) VALUES("informal");

INSERT INTO voice (name, owner) VALUES("voice1", "urvoice");
INSERT INTO voice (name, owner) VALUES("voice2", "urvoice");
INSERT INTO voice (name, owner) VALUES("voice3", "urvoice");
INSERT INTO voice (name, owner) VALUES("voice4", "urvoice");
INSERT INTO voice (name, owner) VALUES("voice5", "urvoice");

INSERT INTO voice (name, owner) VALUES("voice6", "myVoice");
INSERT INTO voice (name, owner) VALUES("voice7", "myVoice");
INSERT INTO voice (name, owner) VALUES("voice8", "myVoice");
INSERT INTO voice (name, owner) VALUES("voice9", "myVoice");

INSERT INTO chat_bot (id, mode_id, voice_id, owner, image_file_id, name) VALUES(1, 1, 1, "urVoice", NULL, "Bot1");
INSERT INTO chat_bot (id, mode_id, voice_id, owner, image_file_id, name) VALUES(2, 2, 2, "urVoice", NULL, "Bot2");
INSERT INTO chat_bot (id, mode_id, voice_id, owner, image_file_id, name) VALUES(3, 1, 3, "urVoice", NULL, "Bot3");
INSERT INTO chat_bot (id, mode_id, voice_id, owner, image_file_id, name) VALUES(4, 1, 3, "urVoice", 1, "Bot4");


INSERT INTO chat_bot (id, mode_id, voice_id, owner, image_file_id, name) VALUES(5, 2, 6, "myVoice", NULL, "Bot4");
INSERT INTO chat_bot (id, mode_id, voice_id, owner, image_file_id, name) VALUES(6, 1, 7, "myVoice", NULL, "Bot5");
INSERT INTO chat_bot (id, mode_id, voice_id, owner, image_file_id, name) VALUES(7, 1, 8, "myVoice", NULL, "Bot6");
INSERT INTO chat_bot (id, mode_id, voice_id, owner, image_file_id, name) VALUES(8, 2, 9, "myVoice", NULL, "Bot7");


INSERT INTO file (file_path, content_type, owner, url) VALUES ("C:/Users/Administrator/AppData/Local/Temp", "image", "urVoice", "/image/profile/profile1.jpg");


INSERT INTO chat (bot_id, text, is_bot, create_date, owner) VALUES(1, "BOT 1, owner, hello!", 0, now(), "urvoice");
INSERT INTO chat (bot_id, text, is_bot, create_date, owner) VALUES(1, "BOT 1, bot, hello!", 1, now(), "urvoice");
