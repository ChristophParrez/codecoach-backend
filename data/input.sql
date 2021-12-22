SET SCHEMA 'runtime';

INSERT INTO roles (role_id, role_name) values (0, 'COACHEE');
INSERT INTO roles (role_id, role_name) values (1, 'COACH');
INSERT INTO roles (role_id, role_name) values (2, 'ADMIN');

INSERT INTO topics (topic_id) values ('Java');
INSERT INTO topics (topic_id) values ('HTML');
INSERT INTO topics (topic_id) values ('CSS');
INSERT INTO topics (topic_id) values ('JavaScript');
INSERT INTO topics (topic_id) values ('Spring');
INSERT INTO topics (topic_id) values ('Angular');
INSERT INTO topics (topic_id) values ('SQL');

INSERT INTO locations (name) values ('Online');
INSERT INTO locations (name) values ('Face2Face');

INSERT INTO status (status_name) values ('REQUESTED');
INSERT INTO status (status_name) values ('ACCEPTED');
INSERT INTO status (status_name) values ('DONE_WAITING_FOR_FEEDBACK');
INSERT INTO status (status_name) values ('FINISHED_FEEDBACK_GIVEN');
INSERT INTO status (status_name) values ('FINISHED_AUTOMATICALLY_CLOSED');
INSERT INTO status (status_name) values ('FINISHED_CANCELLED_BY_COACHEE');
INSERT INTO status (status_name) values ('FINISHED_CANCELLED_BY_COACH');
INSERT INTO status (status_name) values ('DECLINED');

INSERT INTO coach_info (introduction, coach_info_id, availability, coach_xp) values ('Hello World', 0, 'Every Monday', 0);
INSERT INTO coach_info (introduction, coach_info_id, availability, coach_xp) values ('Hi there!', 1, 'Every Tuesday', 10);
INSERT INTO coach_info (introduction, coach_info_id, availability, coach_xp) values ('Greetings!', 2, 'Every Wednesday', 20);
INSERT INTO coach_info (introduction, coach_info_id, availability, coach_xp) values ('Hey!', 3, 'Everyday', 50);
INSERT INTO coach_info (introduction, coach_info_id, availability, coach_xp) values ('Hello!', 4, 'In the weekend', 70);

INSERT INTO users (coach_info, password, last_name, user_id, company_name, email, first_name, picture) values (0, 'Password1', 'Burgemeester', 0, 'Studio 100', 'meneer@burgemeester.de', 'Meneer De', 'https://vroegert.nl/wp-content/uploads/2016/05/Meneer-de-burgemeester-e1463667451236.jpg');
INSERT INTO users (coach_info, password, last_name, user_id, company_name, email, first_name, picture) values (1, 'Password1', 'De Bolle', 1, 'Studio 100', 'octaaf@bolle.de', 'Octaaf', 'https://pbs.twimg.com/profile_images/1025276561082265600/iWWvCOXP_400x400.jpg');
INSERT INTO users (coach_info, password, last_name, user_id, company_name, email, first_name, picture) values (2, 'Password1', 'Praline', 2, 'Studio 100', 'mevrouw@prali.ne', 'Mevrouw ', 'https://pbs.twimg.com/profile_images/1408234790/Rofl_irl_400x400.jpg');
INSERT INTO users (coach_info, password, last_name, user_id, company_name, email, first_name, picture) values (3, 'Password1', 'Vande Minister', 3, 'Studio 100', 'afgevaardigdevan@deminist.er', 'De Afgevaardigde', 'https://pbs.twimg.com/media/ERN4MctUYAAaLIs?format=jpg&name=900x900');
INSERT INTO users (coach_info, password, last_name, user_id, company_name, email, first_name, picture) values (4, 'Password1', 'Vanleemhuyzen', 4, 'Studio 100', 'eugene@vanleemhuyz.en', 'Eugène', 'https://cds.mnm.be/sites/default/files/styles/800x600_focus/public/article/2017_12/wp_dgvlhq.jpg?itok=4Zb566Iu');

INSERT INTO users_roles (role_id, user_id) values (0, 0);
INSERT INTO users_roles (role_id, user_id) values (1, 0);
INSERT INTO users_roles (role_id, user_id) values (0, 1);
INSERT INTO users_roles (role_id, user_id) values (1, 1);
INSERT INTO users_roles (role_id, user_id) values (0, 2);
INSERT INTO users_roles (role_id, user_id) values (1, 2);
INSERT INTO users_roles (role_id, user_id) values (0, 3);
INSERT INTO users_roles (role_id, user_id) values (1, 3);
INSERT INTO users_roles (role_id, user_id) values (0, 4);
INSERT INTO users_roles (role_id, user_id) values (1, 4);

INSERT INTO coaching_topics (coaching_info_id, topic, experience, coaching_topic_id) values (0, 'Angular', 4, 0);
INSERT INTO coaching_topics (coaching_info_id, topic, experience, coaching_topic_id) values (0, 'JavaScript', 6, 1);
INSERT INTO coaching_topics (coaching_info_id, topic, experience, coaching_topic_id) values (1, 'HTML', 7, 2);
INSERT INTO coaching_topics (coaching_info_id, topic, experience, coaching_topic_id) values (1, 'CSS', 5, 3);
INSERT INTO coaching_topics (coaching_info_id, topic, experience, coaching_topic_id) values (2, 'JavaScript', 4, 4);
INSERT INTO coaching_topics (coaching_info_id, topic, experience, coaching_topic_id) values (2, 'HTML', 6, 5);
INSERT INTO coaching_topics (coaching_info_id, topic, experience, coaching_topic_id) values (3, 'SQL', 3, 6);
INSERT INTO coaching_topics (coaching_info_id, topic, experience, coaching_topic_id) values (3, 'Java', 2, 7);
INSERT INTO coaching_topics (coaching_info_id, topic, experience, coaching_topic_id) values (4, 'Java', 5, 8);
INSERT INTO coaching_topics (coaching_info_id, topic, experience, coaching_topic_id) values (4, 'Spring', 4, 9);




-- Glenn
INSERT INTO coach_info (introduction, coach_info_id, availability, coach_xp) values (null, '2002', null, 0);
INSERT INTO users (user_id, company_name, email, first_name, last_name, password, picture, coach_info, telephone_number) values ('1002', 'eGov', 'glenn@mail.be', 'Glenn', 'Verhaeghe', '$2a$10$6nCHRHSI92.guttxstf/XO6S7Xe.SV5n9hpOw1HerNOpwb8/OyFm2', null, '2002', '+32472491759');
INSERT INTO users_roles (user_id, role_id) values ('1002', '0');
INSERT INTO users_roles (user_id, role_id) values ('1002', '1');
INSERT INTO users_roles (user_id, role_id) values ('1002', '2');

-- Mert
INSERT INTO users (user_id, company_name, email, first_name, last_name, password, picture, coach_info, telephone_number) values ('7319', 'FOD ECO', 'mert@mail.be', 'Mert', 'Demirok', '$2a$10$aN7hxcif/Dcgwhzbd3b4X.gg.3j5f6ZYo1rATsUZNh6iyTVbWMySe', null, null, '+32489223344');
INSERT INTO users_roles (user_id, role_id) values ('7319', '0');