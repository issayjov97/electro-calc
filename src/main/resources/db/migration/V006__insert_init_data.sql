-- insert role
INSERT INTO authorities (name) VALUES ('ADMIN');
INSERT INTO authorities (name) VALUES ('USER');
-- insert firm
INSERT INTO firms (cin, vatin, city, copy_default_patterns, email, mobile, name, phone, post_code, state, street) VALUES ('','' , '', false, 'demor@upce.cz', null, 'DEMO', '', '', '', '');
INSERT INTO firm_settings (id, charge_per_hour, cost_per_km, dph, incision, sale, working_hours) VALUES (1,1, 0, 21, 0, 0, 1);
-- insert user
INSERT INTO users (created_at, email, enabled, first_name, last_name, password, username, firm_id) VALUES ('2023-01-01 00:00:00.000000', 'upce@upce.cz', true, 'Admin', 'Admin', '$2a$10$ft7BS8Dik1EkXK5DcqTvquOhwfsgRTEpVE0beAtXeBlkaBs4FNpp2', 'root', 1);
-- insert user role
INSERT INTO user_authority (user_id, authority_id) VALUES (1, 1);