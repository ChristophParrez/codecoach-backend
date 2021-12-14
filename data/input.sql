--INSERT INTO topics (topic_id, name) values (0, 'Math');
--INSERT INTO topics (topic_id, name) values (1, 'Physics');
--INSERT INTO topics (topic_id, name) values (2, 'Yoga');
--INSERT INTO topics (topic_id, name) values (3, 'English');

--INSERT INTO coach_info (coach_info_id, availability, coach_xp, introduction) values (0, 'Daily', 0, 'Hi!');
--INSERT INTO coach_info (coach_info_id, availability, coach_xp, introduction) values (1, 'On wednesdays', 0, 'Hi!');
--INSERT INTO coach_info (coach_info_id, availability, coach_xp, introduction) values (2, 'In the weekend', 0, 'Hi!');
--INSERT INTO coach_info (coach_info_id, availability, coach_xp, introduction) values (3, 'On mondays and fridays', 0, 'Hi!');

--INSERT INTO coaching_topics (coaching_topic_id, experience, coach_info_id, topic) values (0, 0, 0, 0);
--INSERT INTO coaching_topics (coaching_topic_id, experience, coach_info_id, topic) values (1, 1.5, 0, 1);
--INSERT INTO coaching_topics (coaching_topic_id, experience, coach_info_id, topic) values (2, 2, 1, 2);
--INSERT INTO coaching_topics (coaching_topic_id, experience, coach_info_id, topic) values (3, 2, 1, 3);
--INSERT INTO coaching_topics (coaching_topic_id, experience, coach_info_id, topic) values (4, 2, 2, 2);
--INSERT INTO coaching_topics (coaching_topic_id, experience, coach_info_id, topic) values (5, 3, 2, 3);
--INSERT INTO coaching_topics (coaching_topic_id, experience, coach_info_id, topic) values (6, 4, 3, 0);

--INSERT INTO users (user_id, company_name, email, first_name, last_name, password, picture, coach_info) values (0, 'Switchfully', 'a@b.c', 'A', 'B', 'pass', 'Foto', null);
--INSERT INTO users (user_id, company_name, email, first_name, last_name, password, picture, coach_info) values (1, 'Switchfully', 'a@b.c', 'A', 'B', 'pass', 'Foto', 0);
--INSERT INTO users (user_id, company_name, email, first_name, last_name, password, picture, coach_info) values (2, 'Switchfully', 'a@b.c', 'A', 'B', 'pass', 'Foto', 1);
--INSERT INTO users (user_id, company_name, email, first_name, last_name, password, picture, coach_info) values (3, 'Switchfully', 'a@b.c', 'A', 'B', 'pass', 'Foto', 2);
--INSERT INTO users (user_id, company_name, email, first_name, last_name, password, picture, coach_info) values (4, 'Switchfully', 'a@b.c', 'A', 'B', 'pass', 'Foto', 3);

INSERT INTO roles (role_id, role_name) values (0, 'COACHEE');
INSERT INTO roles (role_id, role_name) values (1, 'COACH');
INSERT INTO roles (role_id, role_name) values (2, 'ADMIN');