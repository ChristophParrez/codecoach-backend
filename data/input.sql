SET SCHEMA 'runtimedb';

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
INSERT INTO users (coach_info, password, last_name, user_id, company_name, email, first_name, picture) values (2, 'Password1', 'Praline', 2, 'Studio 100', 'mevrouw@prali.ne', 'Mevrouw', 'https://pbs.twimg.com/profile_images/1408234790/Rofl_irl_400x400.jpg');
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
INSERT INTO users (user_id, company_name, email, first_name, last_name, password, picture, coach_info, telephone_number) values ('1002', 'Cynalco Medics', 'michel@mail.be', 'Michel', 'Drets', '$2a$10$6nCHRHSI92.guttxstf/XO6S7Xe.SV5n9hpOw1HerNOpwb8/OyFm2', 'https://www.showbizzcontent.be/episodegids/heteiland12.jpg', '2002', '+32472491759');
INSERT INTO users_roles (user_id, role_id) values ('1002', '0');
INSERT INTO users_roles (user_id, role_id) values ('1002', '1');
INSERT INTO users_roles (user_id, role_id) values ('1002', '2');
INSERT INTO coaching_topics (coaching_topic_id, experience, topic, coaching_info_id) values ('3003', 6, 'Java', '2002');

INSERT INTO coach_info (coach_info_id, availability, coach_xp, introduction) values ('2003', 'On weekdays', 20, 'Hi there!');
INSERT INTO users (user_id, company_name, email, first_name, last_name, password, picture, coach_info, telephone_number) values ('1003', 'The Blue Bank', 'ellen@bluebank.com', 'Ellen', 'Gottesdiener', '$2a$10$6nCHRHSI92.guttxstf/XO6S7Xe.SV5n9hpOw1HerNOpwb8/OyFm2', 'https://randomuser.me/api/portraits/women/12.jpg', '2003', null);
INSERT INTO coaching_topics (coaching_topic_id, experience, topic, coaching_info_id) values ('3001', 7, 'Java', '2003');
INSERT INTO coaching_topics (coaching_topic_id, experience, topic, coaching_info_id) values ('3002', 5, 'Angular', '2003');
INSERT INTO users_roles (user_id, role_id) values ('1003', '0');
INSERT INTO feedbacks (feedback_id, comment, score_one, score_two, feedback_giver) values ('9001', 'I learned a lot', 6, 6, '1003');
INSERT INTO sessions (session_id, date, remarks, subject, time, coach, coachee, coachee_feedback, location, status) values ('5001', '2021-12-21', 'I would like to learn about polymorphism', 'Java', '14:30:00', '1002', '1003', '9001', 'Online', 'DONE_WAITING_FOR_FEEDBACK');

-- Mert
INSERT INTO users (user_id, company_name, email, first_name, last_name, password, picture, coach_info, telephone_number) values ('7319', 'FOD ECO', 'mert@mail.be', 'Mert', 'Demirok', '$2a$10$aN7hxcif/Dcgwhzbd3b4X.gg.3j5f6ZYo1rATsUZNh6iyTVbWMySe', 'https://media-exp1.licdn.com/dms/image/C4D03AQHcGIcXI0sS6w/profile-displayphoto-shrink_400_400/0/1586875881687?e=1645660800&v=beta&t=tg-1lVU1yesqHweUqsnGH-w8WZ8YxxVz6uYtZnQWMv8', null, '+32489223344');
INSERT INTO users_roles (user_id, role_id) values ('7319', '0');

--Free
--coachee Free Hoebeke
INSERT INTO users (user_id, first_name, last_name, password,   company_name, email, telephone_number,  picture)
values ('FreeDb_userCoachee_1', 'Free', 'Hoebeke', '$2a$10$G/yX9SSk28WiXznnydU2d.ZtXYieBiZGtluDyW.90eGLRKv0e5vGG',
        'Cegeka', 'freehoebeke@gmail.com', '+32479074042' , 'https://freeh23.github.io/Images/me-and-myCat.jpg');
INSERT INTO users_roles (role_id, user_id) values (0, 'FreeDb_userCoachee_1');
-- 2nd coache for showing an exception in Postman
INSERT INTO users (user_id, first_name, last_name, password,   company_name, email, telephone_number,  picture)
values ('FreeDb_userCoachee_2', 'Samson', 'Van Gert', '$2a$10$G/yX9SSk28WiXznnydU2d.ZtXYieBiZGtluDyW.90eGLRKv0e5vGG',
        'Studio100', 'samson@studio.com', '+32479074042' , 'https://cds.radio1.be/sites/default/files/styles/1200x630_scale_and_crop/public/remote_files/576ec2bea006d_samson_0.jpg?itok=4ue8eiUB');
--coach Simon Brown
INSERT INTO coach_info (introduction, coach_info_id, availability, coach_xp)
values ('Hello! My name is Simon Brown and I would love to help you with Spring or SQL', 'FreeDb_coachInfo_Simon' , 'All days', 50);
INSERT INTO users (coach_info, password, last_name, user_id, company_name, email, telephone_number, first_name, picture)
values ('FreeDb_coachInfo_Simon', '$2a$10$G/yX9SSk28WiXznnydU2d.ZtXYieBiZGtluDyW.90eGLRKv0e5vGG', 'Brown', 'FreeDb_userCoach_1',
        'Big Red Paper Company', 'simon@mail.com', '+32479074042' ,'Simon', 'https://randomuser.me/api/portraits/men/31.jpg');
INSERT INTO users_roles (role_id, user_id) values (0, 'FreeDb_userCoach_1');
INSERT INTO users_roles (role_id, user_id) values (1, 'FreeDb_userCoach_1');
INSERT INTO coaching_topics (coaching_info_id, topic, experience, coaching_topic_id)
values ('FreeDb_coachInfo_Simon', 'SQL', 5, 'FreeDb_coachTopics_S1');
INSERT INTO coaching_topics (coaching_info_id, topic, experience, coaching_topic_id)
values ('FreeDb_coachInfo_Simon', 'Spring', 6, 'FreeDb_coachTopics_S2');
--Dummy sessions
--1 session waiting for accept by coach (REQUESTED)
INSERT INTO sessions (session_id, date, remarks, subject, time, coach, coachee, location, status)
VALUES ('FreeDb_dummySessoin_12', '2022-01-25', 'How to write a good sql file for my Spring project', 'SQL', '19:00:00','FreeDb_userCoach_1', 'FreeDb_userCoachee_1', 'Online', 'REQUESTED');
--2 SQL sessions to be cancelled (REQUESTED)
INSERT INTO sessions (session_id, date, remarks, subject, time, coach, coachee, location, status)
VALUES ('FreeDb_dummySessoin_2', '2022-01-21', 'How to set up a table with SQL?', 'SQL', '18:30:00','FreeDb_userCoach_1', 'FreeDb_userCoachee_1', 'Online', 'REQUESTED');
--INSERT INTO sessions (session_id, date, remarks, subject, time, coach, coachee, location, status)
--VALUES ('FreeDb_dummySessoin_3', '2022-01-24', 'How to write good sql code', 'SQL', '18:30:00','FreeDb_userCoach_1', 'FreeDb_userCoachee_1', 'Online', 'REQUESTED');
--1 session in the future, ACCEPTED by coach
INSERT INTO sessions (session_id, date, remarks, subject, time, coach, coachee, location, status)
VALUES ('FreeDb_dummySessoin_4', '2022-02-23', 'Explanations on Spring data', 'Spring', '19:00:00','FreeDb_userCoach_1', 'FreeDb_userCoachee_1', 'Online', 'ACCEPTED');
--1 session request declined by coach
INSERT INTO sessions (session_id, date, remarks, subject, time, coach, coachee, location, status)
VALUES ('FreeDb_dummySessoin_5', '2022-02-23', 'Explanations on Spring boot', 'Spring', '19:00:00','FreeDb_userCoach_1', 'FreeDb_userCoachee_1', 'Online', 'DECLINED');
--1 accepted session canceled by coach
INSERT INTO sessions (session_id, date, remarks, subject, time, coach, coachee, location, status)
VALUES ('FreeDb_dummySessoin_6', '2022-02-23', 'Explanations on Spring core', 'Spring', '19:00:00','FreeDb_userCoach_1', 'FreeDb_userCoachee_1', 'Online', 'FINISHED_CANCELLED_BY_COACH');
--1 accepted session canceled by coachee
INSERT INTO sessions (session_id, date, remarks, subject, time, coach, coachee, location, status)
VALUES ('FreeDb_dummySessoin_7', '2022-02-23', 'Explanations on Spring', 'Spring', '19:00:00','FreeDb_userCoach_1', 'FreeDb_userCoachee_1', 'Online', 'FINISHED_CANCELLED_BY_COACHEE');
--1 session never accepted but date in the past (REQUESTED -> FINISHED AUTOMATICALLY CLOSED)
INSERT INTO sessions (session_id, date, remarks, subject, time, coach, coachee, location, status)
VALUES ('FreeDb_dummySessoin_8', '2021-12-13', 'Date format in SQL', 'SQL', '19:00:00','FreeDb_userCoach_1', 'FreeDb_userCoachee_1', 'Online', 'REQUESTED');
--1 session in the future, ACCEPTED by coach
INSERT INTO sessions (session_id, date, remarks, subject, time, coach, coachee, location, status)
VALUES ('FreeDb_dummySessoin_9', '2022-02-23', 'Explanations on Spring web', 'Spring', '19:00:00','FreeDb_userCoach_1', 'FreeDb_userCoachee_1', 'Online', 'ACCEPTED');
--1 session done where I can give feedback (ACCEPTED -> DONE WAITING FOR FEEDBACK)
INSERT INTO sessions (session_id, date, remarks, subject, time, coach, coachee, location, status)
VALUES ('FreeDb_dummySessoin_10', '2021-12-14', 'formatting problems in SQL', 'SQL', '19:00:00','FreeDb_userCoach_1', 'FreeDb_userCoachee_1', 'Online', 'ACCEPTED');
--1 session (DONE WAITING FOR FEEDBACK) -> but just waiting for coach
INSERT INTO feedbacks (feedback_id, comment, score_one, score_two, feedback_giver)
VALUES ('FreeDb_userCoachee_1_feedback1', 'It was great!', 6, 6, 'FreeDb_userCoachee_1');
INSERT INTO sessions (session_id, date, remarks, subject, time, coach, coachee, coachee_feedback, location, status)
VALUES ('FreeDb_dummySessoin_11', '2021-12-14', 'Issues with executing SQL on my database', 'SQL', '19:00:00','FreeDb_userCoach_1', 'FreeDb_userCoachee_1', 'FreeDb_userCoachee_1_feedback1', 'Online', 'DONE_WAITING_FOR_FEEDBACK');
--1 session finished and feedback given
INSERT INTO feedbacks (feedback_id, comment, score_one, score_two, feedback_giver)
VALUES ('FreeDb_userCoachee_1_feedback2', 'It was great!', 6, 6, 'FreeDb_userCoachee_1');
INSERT INTO feedbacks (feedback_id, comment, score_one, score_two, feedback_giver)
VALUES ('FreeDb_userCoach_1_feedback1', 'It was great!', 6, 6, 'FreeDb_userCoach_1');
INSERT INTO sessions (session_id, date, remarks, subject, time, coach, coach_feedback, coachee, coachee_feedback, location, status)
VALUES ('FreeDb_dummySessoin_1', '2021-12-14', 'Unions with SQL', 'SQL', '19:00:00','FreeDb_userCoach_1', 'FreeDb_userCoach_1_feedback1' , 'FreeDb_userCoachee_1', 'FreeDb_userCoachee_1_feedback2', 'Online', 'FINISHED_FEEDBACK_GIVEN');


-- Christoph
INSERT INTO users (user_id, password, first_name, last_name, company_name, email) values ('chris69', '$2a$10$lX8Ynwq7hbyFSG0yo7YeIei0ix7i9V/sDcI2apUBX4xnL.eJyawDO', 'Christoph', 'Parrez', 'Switchfully', 'christoph@gmail.com');
INSERT INTO users_roles (user_id, role_id) values ('chris69', 0);
