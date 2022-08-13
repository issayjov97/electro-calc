-- insert firm
INSERT INTO firms (id,cin, vatin, city, email, mobile, name, phone, post_code, state, street) VALUES (1, '', '', '', '', null, 'DEMO', '', '', '', '');

-- insert users
INSERT INTO users (id, created_at, email, enabled, first_name, last_name, password, username, firm_entity_id, otp_required) values (1, now(), 'tester@tester.cz',true, 'tester','tester','tester123','tester123',1, false);
INSERT INTO users (id, created_at, email, enabled, first_name, last_name, password, username, firm_entity_id, otp_required) VALUES (99, '2022-07-12 20:36:15.364394', 'st55409@gs.upce.cz', true, 'ivafds', 'dfff', '$2a$10$7XdCyjQniQ32qN6j.XiYWORN/kGzgmMdhuDGR6LVRrbJEn3i07kzi', 'rootroot',1, false);
INSERT INTO users (id, created_at, email, enabled, first_name, last_name, password, username, firm_entity_id, otp_required) VALUES (14, '2022-07-15 11:26:59.299554', 'tester@addai.cz', true, 'tester', 'tester', '275822df', 'tester',1,false);

-- insert patterns
INSERT INTO patterns (id, description, duration, name,vat, price_withoutvat, measure_unit, firm_entity_id, plu) VALUES (5083, 'NULL', 0.101, 'měření přechod.odporu ochran.spojení/pospojování.', 21, 0.00, null, null, '2251608');
INSERT INTO patterns (id, description, duration, name,vat, price_withoutvat, measure_unit, firm_entity_id, plu) VALUES (5093, 'NULL', 0.53, 'měřící trafo proudu podpěrné od 40A vč.zapojení.', 21, 0.00, null, null, '6396654');
INSERT INTO patterns (id, description, duration, name,vat, price_withoutvat, measure_unit, firm_entity_id, plu) VALUES (5096, 'NULL', 0.76, 'měřící transformátor napětí do 35kV.',  21, 0.00, null, null, '3684466');
INSERT INTO patterns (id, description, duration, name,vat, price_withoutvat, measure_unit, firm_entity_id, plu) VALUES (5097, 'NULL', 0.4, 'měřící transformátor proudu do 10kV.', 21, 0.00, null, 1, '8180783');
INSERT INTO patterns (id, description, duration, name,vat, price_withoutvat, measure_unit, firm_entity_id, plu) VALUES (5098, 'NULL', 0.46, 'měřící transformátor proudu do 22kV.', 21, 0.00, null, 1, '236130');
INSERT INTO patterns (id, description, duration, name,vat, price_withoutvat, measure_unit, firm_entity_id, plu) VALUES (5099, 'NULL', 0.76, 'měřící transformátor proudu do 35kV.', 21, 0.00, null, 1, '4490073');
INSERT INTO patterns (id, description, duration, name,vat, price_withoutvat, measure_unit, firm_entity_id, plu) VALUES (5101, 'NULL', 0.111, 'minilišta vkládací pevně uložená do š.20mm.', 21, 0.00, null, 1, '32951');
INSERT INTO patterns (id, description, duration, name,vat, price_withoutvat, measure_unit, firm_entity_id, plu) VALUES (5104, 'NULL', 1.2, 'montáž elmotoru na stáv podklad bez zapoj /do 10kW.', 21, 0.00, null, 1, '1317940');
INSERT INTO patterns (id, description, duration, name,vat, price_withoutvat, measure_unit, firm_entity_id, plu) VALUES (5106, 'NULL', 0.4, 'montáž elmotoru na stáv podklad bez zapoj /do 1kW.', 21, 0.00, null, null, '6779871');
INSERT INTO patterns (id, description, duration, name,vat, price_withoutvat, measure_unit, firm_entity_id, plu) VALUES (5108, 'NULL', 0.65, 'montáž elmotoru na stáv podklad bez zapoj /do 3kW.', 21, 0.00, null, 1, '841329');
INSERT INTO patterns (id, description, duration, name,vat, price_withoutvat, measure_unit, firm_entity_id, plu) VALUES (5109, 'NULL', 0.03, 'montáž háku bez svítidla.', 21, 0.00, null, 1, '6845927');

INSERT INTO customers (id, email, name, phone, firm_entity_id) VALUES (1, 'tester@upce.cz', 'tester', 'tester', 1);
INSERT INTO customers (id, email, name, phone, firm_entity_id) VALUES (5, 'fdsfsf@addai.cz', 'fdfsf', '58439472', 1);

-- insert firm_patterns
INSERT INTO firm_pattern (firm_id, pattern_id) VALUES (1, 5083);
INSERT INTO firm_pattern (firm_id, pattern_id) VALUES (1, 5093);
INSERT INTO firm_pattern (firm_id, pattern_id) VALUES (1, 5096);

-- insert orders
INSERT INTO orders (id, created_at, materials_cost, transportation_cost, vat, work_hours, customer_entity_id, description, job_order_entity_id, firm_entity_id, price_withoutvat) VALUES (1, '2022-07-24 17:09:40.881276', 3123.00, 3213.00, 10, 0, 1, null, null, 1, null);

-- insert order_patterns
INSERT INTO order_pattern (order_id, pattern_id) VALUES (1, 3700);
INSERT INTO order_pattern (order_id, pattern_id) VALUES (1, 3701);
INSERT INTO order_pattern (order_id, pattern_id) VALUES (1, 5109);


-- INSERT INTO offers (id, vat, price_withoutvat, created_at, description, materials_cost, transportation_cost, work_hours, customer_entity_id, firm_entity_id, job_order_entity_id) VALUES (1, 21, 264.00, '2022-07-29 12:23:48.409361', null, 232.00, 32.00, 3213213, 7667, 1, null);
--
-- INSERT INTO offer_pattern (offer_id, pattern_id) VALUES (1, 5109);
--
-- INSERT INTO public.demands (id, vat, price_withoutvat, created_at, description, materials_cost, transportation_cost, work_hours, customer_entity_id, firm_entity_id, job_order_entity_id) VALUES (1, 21, 0.00, '2022-07-27 11:44:00.511775', null, 234.00, 2442.00, 0, null, 1, null);
--
-- INSERT INTO demand_pattern (demand_id, pattern_id) VALUES (1, 5109);
--
-- INSERT INTO firm_pattern (firm_id, pattern_id) VALUES (1, 5109);
-- INSERT INTO firm_pattern (firm_id, pattern_id) VALUES (1, 5108);
-- INSERT INTO firm_pattern (firm_id, pattern_id) VALUES (1, 5106);
