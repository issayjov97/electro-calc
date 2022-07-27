-- insert firm
INSERT INTO firms (id,cin, vatin, city, email, mobile, name, phone, post_code, state, street) VALUES (1, '', '', '', '', null, 'DEMO', '', '', '', '');

-- insert users
INSERT INTO users (id, created_at, email, enabled, first_name, last_name, password, username, firm_entity_id) values (1, now(), 'tester@tester.cz',true, 'tester','tester','tester123','tester123',1);
INSERT INTO users (id, created_at, email, enabled, first_name, last_name, password, username, firm_entity_id) VALUES (99, '2022-07-12 20:36:15.364394', 'st55409@gs.upce.cz', true, 'ivafds', 'dfff', '$2a$10$7XdCyjQniQ32qN6j.XiYWORN/kGzgmMdhuDGR6LVRrbJEn3i07kzi', 'rootroot',1);
INSERT INTO users (id, created_at, email, enabled, first_name, last_name, password, username, firm_entity_id) VALUES (14, '2022-07-15 11:26:59.299554', 'tester@addai.cz', true, 'tester', 'tester', '275822df', 'tester',1);

-- insert patterns
INSERT INTO patterns (id, vat, description, duration, name, price_withoutvat, user_id) VALUES (3700, 21, 'test', 86, 'testtest', 26.00, 1);
INSERT INTO patterns (id, vat,  description, duration, name, price_withoutvat, user_id) VALUES (3701, 21, 'tetstetst', 0, 'test123456', 0.00, 1);
INSERT INTO customers (id, email, name, phone, firm_entity_id) VALUES (1, 'tester@upce.cz', 'tester', 'tester', 1);
INSERT INTO customers (id, email, name, phone, firm_entity_id) VALUES (5, 'fdsfsf@addai.cz', 'fdfsf', '58439472', 1);

-- insert orders
INSERT INTO orders (id, created_at, materials_cost, transportation_cost, vat, work_hours, customer_entity_id, description, job_order_entity_id, firm_entity_id, price_withoutvat) VALUES (1, '2022-07-24 17:09:40.881276', 3123.00, 3213.00, 10, 0, 1, null, null, 1, null);

-- insert order_patterns
INSERT INTO order_pattern (order_id, pattern_id) VALUES (1, 3700);
INSERT INTO order_pattern (order_id, pattern_id) VALUES (1, 3701);
