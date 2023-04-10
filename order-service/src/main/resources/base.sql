CREATE DATABASE order_service;
CREATE USER 'order_server_user'@'localhost' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON order_service.* TO 'order_server_user'@'localhost';
