INSERT INTO tb_account (username, password)  VALUES ('caiqi','123');
INSERT INTO tb_account (username, password)  VALUES ('ckey','123');
INSERT INTO tb_account (username, password)  VALUES ('zy','123');
INSERT INTO tb_account (username, password)  VALUES ('hcw','123');
INSERT INTO tb_account (username, password)  VALUES ('hjq','123');
INSERT INTO tb_account (username, password)  VALUES ('zhoulin','123');

INSERT INTO tb_status_category (img_url,title,status_num) VALUES ('www.baidu.com','hao',0);
INSERT INTO tb_status_category (img_url,title,status_num) VALUES ('www.souhu.com','hao',0);
INSERT INTO tb_status_category (img_url,title,status_num) VALUES ('www.tudou.com','hao',0);
INSERT INTO tb_status_category (img_url,title,status_num) VALUES ('www.kuaike.com','hao',0);
INSERT INTO tb_status_category (img_url,title,status_num) VALUES ('www.wangyi.com','hao',0);
INSERT INTO tb_status_category (img_url,title,status_num) VALUES ('www.blibli.com','hao',0);

INSERT INTO tb_status (created_at,text,comment_num,love_num,publisher_id,category_id) VALUES ('2015-9-20 14:22:11','hao',0,0,1,1);
INSERT INTO tb_status (created_at,text,comment_num,love_num,publisher_id,category_id) VALUES ('2014-9-21 14:22:11','hao',0,0,1,2);
INSERT INTO tb_status (created_at,text,comment_num,love_num,publisher_id,category_id) VALUES ('2013-9-22 14:22:11','hao',0,0,1,3);
INSERT INTO tb_status (created_at,text,comment_num,love_num,publisher_id,category_id) VALUES ('2012-9-23 14:22:11','hao',0,0,1,4);
INSERT INTO tb_status (created_at,text,comment_num,love_num,publisher_id,category_id) VALUES ('2011-9-24 14:22:11','hao',0,0,1,5);
INSERT INTO tb_status (created_at,text,comment_num,love_num,publisher_id,category_id) VALUES ('2010-9-25 14:22:11','hao',0,0,2,1);
INSERT INTO tb_status (created_at,text,comment_num,love_num,publisher_id,category_id) VALUES ('2010-9-25 14:22:11','hao',0,0,2,2);

INSERT INTO tb_status_love(account_id,status_id) VALUES (1,1);
INSERT INTO tb_status_love(account_id,status_id) VALUES (1,2);
INSERT INTO tb_status_love(account_id,status_id) VALUES (1,3);
INSERT INTO tb_status_love(account_id,status_id) VALUES (1,4);
INSERT INTO tb_status_love(account_id,status_id) VALUES (1,5);
INSERT INTO tb_status_love(account_id,status_id) VALUES (2,1);
INSERT INTO tb_status_love(account_id,status_id) VALUES (2,2);
