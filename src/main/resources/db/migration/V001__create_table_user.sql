create table users(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    auth0_id VARCHAR(255) UNIQUE,
    username VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    street VARCHAR(255),
    house_number VARCHAR(255),
    city VARCHAR(255),
    postal_code VARCHAR(255),
    country VARCHAR(255),
    created_at DATETIME,
    phone_number VARCHAR(255)
);