DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals(user_id, description, calories) VALUES
    (100000, 'Хавчик', 500),
    (100000,'SuperHAvKA',120),
    (100000,'VASYA!GIIIR',2000),
    (100001, 'Dolma', 300);