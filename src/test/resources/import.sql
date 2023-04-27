-- insert firm
INSERT INTO firms (id,cin, vatin, city, email, name, phone, post_code, state, street, copy_default_patterns) VALUES (1, '', '', '', '', 'DEMO', '', '', '', '', true);
INSERT INTO firm_settings (id, charge_per_hour, cost_per_km, dph, incision, sale, working_hours) VALUES (1, 0, 10, 21, 0, 0, 8);

-- insert users
INSERT INTO users (id, created_at, email, enabled, first_name, last_name, password, username, firm_id) values (1, now(), 'tester@tester.cz',true, 'tester','tester','tester123','tester123',1);
INSERT INTO users (id, created_at, email, enabled, first_name, last_name, password, username, firm_id) VALUES (99, '2022-07-12 20:36:15.364394', 'st55409@gs.upce.cz', true, 'ivafds', 'dfff', '$2a$10$7XdCyjQniQ32qN6j.XiYWORN/kGzgmMdhuDGR6LVRrbJEn3i07kzi', 'rootroot',1);
INSERT INTO users (id, created_at, email, enabled, first_name, last_name, password, username, firm_id) VALUES (14, '2022-07-15 11:26:59.299554', 'tester@addai.cz', true, 'tester', 'tester', '275822df', 'upce',1);

-- insert patterns
INSERT INTO patterns (id, description, duration, name, price_withoutvat, measure_unit, firm_id) VALUES (5083, 'NULL', 0.101, 'měření přechod.odporu ochran.spojení/pospojování.', 10, null, null);
INSERT INTO patterns (id, description, duration, name, price_withoutvat, measure_unit, firm_id) VALUES (5093, 'NULL', 0.53, 'měřící trafo proudu podpěrné od 40A vč.zapojení.', 30, null, null);
INSERT INTO patterns (id, description, duration, name, price_withoutvat, measure_unit, firm_id) VALUES (5096, 'NULL', 0.76, 'měřící transformátor napětí do 35kV.',  0.00, null, null);
INSERT INTO patterns (id, description, duration, name, price_withoutvat, measure_unit, firm_id) VALUES (5097, 'NULL', 0.4, 'měřící transformátor proudu do 10kV.', 0.00, null, 1);
INSERT INTO patterns (id, description, duration, name, price_withoutvat, measure_unit, firm_id) VALUES (5098, 'NULL', 0.46, 'měřící transformátor proudu do 22kV.', 0.00, null, 1);
INSERT INTO patterns (id, description, duration, name, price_withoutvat, measure_unit, firm_id) VALUES (5099, 'NULL', 0.76, 'měřící transformátor proudu do 35kV.', 0.00, null, 1);
INSERT INTO patterns (id, description, duration, name, price_withoutvat, measure_unit, firm_id) VALUES (5101, 'NULL', 0.111, 'minilišta vkládací pevně uložená do š.20mm.', 30.00, null, 1);
INSERT INTO patterns (id, description, duration, name, price_withoutvat, measure_unit, firm_id) VALUES (5104, 'NULL', 1.2, 'montáž elmotoru na stáv podklad bez zapoj /do 10kW.', 20.00, null, 1);
INSERT INTO patterns (id, description, duration, name, price_withoutvat, measure_unit, firm_id) VALUES (5106, 'NULL', 0.4, 'montáž elmotoru na stáv podklad bez zapoj /do 1kW.', 0.00, null, null);
INSERT INTO patterns (id, description, duration, name, price_withoutvat, measure_unit, firm_id) VALUES (5108, 'NULL', 0.65, 'montáž elmotoru na stáv podklad bez zapoj /do 3kW.', 100.00, null, 1);
INSERT INTO patterns (id, description, duration, name, price_withoutvat, measure_unit, firm_id) VALUES (5109, 'NULL', 0.03, 'montáž háku bez svítidla.', 0.00, null, 1);
INSERT INTO patterns (id, description, duration, name, price_withoutvat, measure_unit, firm_id) VALUES (5196, 'NULL', 0.76, 'měřící transformátor napětí do 3032kV.',  0.00, null, null);


-- insert customers
INSERT INTO customers (id, email, name, phone, firm_id) VALUES (1, 'tester@upce.cz', 'tester', '58439472', 1);
INSERT INTO customers (id, email, name, phone, firm_id) VALUES (5, 'fdsfsf@addai.cz', 'fdfsf', '58439472', 1);


-- insert firm_patterns
INSERT INTO firm_pattern (firm_id, pattern_id) VALUES (1, 5083);
INSERT INTO firm_pattern (firm_id, pattern_id) VALUES (1, 5093);
INSERT INTO firm_pattern (firm_id, pattern_id) VALUES (1, 5096);
INSERT INTO firm_pattern (firm_id, pattern_id) VALUES (1, 5106);


-- insert offers
INSERT INTO offers (id, price_withoutvat, created_date, description, distance, name, note, status, customer_id, firm_id) VALUES (1, 8107.50, '2023-02-13 00:00:00', '', 30, 'test', '', 'IN_PROGRESS', 1, 1);
INSERT INTO offers (id, price_withoutvat, created_date, description, distance, name, note, status, customer_id, firm_id) VALUES (2, 6330.00, '2023-02-13 00:00:00', 'Nabidka pro Pure Storage', 10, 'Pure Storage', '', 'SENT', 5, 1);

INSERT INTO offer_pattern (offer_id, pattern_id, count) values(1,5083,2)
INSERT INTO offer_pattern (offer_id, pattern_id, count) values(1,5093,2)