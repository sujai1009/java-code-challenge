-- Sample data set of 20 devices
-- This file is automatically loaded by Spring Boot on startup (when using H2 or MySQL with spring.sql.init.mode=always)

-- Note: For MySQL, ensure spring.sql.init.mode=always is set in application-mysql.properties
-- For H2, data.sql is loaded by default

INSERT INTO devices (name, brand, state, creation_time, updated_at, version) VALUES ('iPhone 15', 'Apple', 'AVAILABLE', NOW(), NOW(), 0);
INSERT INTO devices (name, brand, state, creation_time, updated_at, version) VALUES ('iPhone 15 Pro', 'Apple', 'IN_USE', NOW(), NOW(), 0);
INSERT INTO devices (name, brand, state, creation_time, updated_at, version) VALUES ('iPhone 14', 'Apple', 'INACTIVE', NOW(), NOW(), 0);
INSERT INTO devices (name, brand, state, creation_time, updated_at, version) VALUES ('Galaxy S24', 'Samsung', 'AVAILABLE', NOW(), NOW(), 0);
INSERT INTO devices (name, brand, state, creation_time, updated_at, version) VALUES ('Galaxy S24 Ultra', 'Samsung', 'IN_USE', NOW(), NOW(), 0);
INSERT INTO devices (name, brand, state, creation_time, updated_at, version) VALUES ('Galaxy Z Flip 5', 'Samsung', 'AVAILABLE', NOW(), NOW(), 0);
INSERT INTO devices (name, brand, state, creation_time, updated_at, version) VALUES ('Pixel 8', 'Google', 'AVAILABLE', NOW(), NOW(), 0);
INSERT INTO devices (name, brand, state, creation_time, updated_at, version) VALUES ('Pixel 8 Pro', 'Google', 'IN_USE', NOW(), NOW(), 0);
INSERT INTO devices (name, brand, state, creation_time, updated_at, version) VALUES ('Pixel 7a', 'Google', 'INACTIVE', NOW(), NOW(), 0);
INSERT INTO devices (name, brand, state, creation_time, updated_at, version) VALUES ('OnePlus 12', 'OnePlus', 'AVAILABLE', NOW(), NOW(), 0);
INSERT INTO devices (name, brand, state, creation_time, updated_at, version) VALUES ('OnePlus 11', 'OnePlus', 'IN_USE', NOW(), NOW(), 0);
INSERT INTO devices (name, brand, state, creation_time, updated_at, version) VALUES ('Xiaomi 14', 'Xiaomi', 'AVAILABLE', NOW(), NOW(), 0);
INSERT INTO devices (name, brand, state, creation_time, updated_at, version) VALUES ('Xiaomi 13T', 'Xiaomi', 'INACTIVE', NOW(), NOW(), 0);
INSERT INTO devices (name, brand, state, creation_time, updated_at, version) VALUES ('Redmi Note 13', 'Xiaomi', 'AVAILABLE', NOW(), NOW(), 0);
INSERT INTO devices (name, brand, state, creation_time, updated_at, version) VALUES ('Mate 60 Pro', 'Huawei', 'IN_USE', NOW(), NOW(), 0);
INSERT INTO devices (name, brand, state, creation_time, updated_at, version) VALUES ('P60 Pro', 'Huawei', 'AVAILABLE', NOW(), NOW(), 0);
INSERT INTO devices (name, brand, state, creation_time, updated_at, version) VALUES ('Find X7 Ultra', 'Oppo', 'AVAILABLE', NOW(), NOW(), 0);
INSERT INTO devices (name, brand, state, creation_time, updated_at, version) VALUES ('Reno 11', 'Oppo', 'INACTIVE', NOW(), NOW(), 0);
INSERT INTO devices (name, brand, state, creation_time, updated_at, version) VALUES ('Vivo X100 Pro', 'Vivo', 'IN_USE', NOW(), NOW(), 0);
INSERT INTO devices (name, brand, state, creation_time, updated_at, version) VALUES ('X100 Pro', 'Vivo', 'AVAILABLE', NOW(), NOW(), 0);
