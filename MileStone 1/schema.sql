-- Run this once to set up the database

CREATE DATABASE IF NOT EXISTS ecommerce_db;
USE ecommerce_db;

CREATE TABLE IF NOT EXISTS products (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  name        VARCHAR(255)   NOT NULL,
  description TEXT,
  price       DECIMAL(10,2)  NOT NULL,
  image       VARCHAR(500)   DEFAULT '',
  stock       INT            DEFAULT 0
);

CREATE TABLE IF NOT EXISTS users (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  name       VARCHAR(100)  NOT NULL,
  email      VARCHAR(150)  NOT NULL UNIQUE,
  password   VARCHAR(255)  NOT NULL,
  created_at TIMESTAMP     DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cart (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  session_id  VARCHAR(64)    NOT NULL,
  product_id  INT            NOT NULL,
  quantity    INT            DEFAULT 1,
  created_at  TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uq_session_product (session_id, product_id),
  FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS orders (
  id             INT AUTO_INCREMENT PRIMARY KEY,
  user_id        INT           NOT NULL,
  session_id     VARCHAR(64)   NOT NULL,
  total          DECIMAL(10,2) NOT NULL,
  payment_method VARCHAR(50)   NOT NULL,
  status         VARCHAR(30)   DEFAULT 'confirmed',
  created_at     TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS order_items (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  order_id   INT           NOT NULL,
  product_id INT           DEFAULT NULL,
  name       VARCHAR(255)  NOT NULL,
  price      DECIMAL(10,2) NOT NULL,
  quantity   INT           NOT NULL,
  FOREIGN KEY (order_id)   REFERENCES orders(id)   ON DELETE CASCADE,
  FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE SET NULL
);

-- Sample products
INSERT INTO products (name, description, price, image, stock) VALUES
  ('Wireless Headphones', 'Premium noise-cancelling over-ear headphones with 30h battery.', 6599.00,
   'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400', 15),
  ('Mechanical Keyboard', 'Compact TKL keyboard with RGB backlight and tactile switches.', 4999.00,
   'https://images.unsplash.com/photo-1618384887929-16ec33fab9ef?w=400', 8),
  ('USB-C Hub', '7-in-1 hub: HDMI 4K, 3x USB-A, SD card, PD charging.', 2899.00,
   'https://images.unsplash.com/photo-1587202372775-e229f172b9d7?w=400', 25),
  ('Webcam 1080p', 'Full HD webcam with built-in mic and auto light correction.', 4199.00,
   'https://images.unsplash.com/photo-1596742578443-7682ef5251cd?w=400', 12),
  ('Desk Lamp LED', 'Adjustable colour temperature and brightness, USB charging port.', 2499.00,
   'https://images.unsplash.com/photo-1534073828943-f801091bb18c?w=400', 20),
  ('Mouse Pad XL', 'Extended gaming mouse pad 900x400mm, stitched edges.', 1599.00,
   'https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7?w=400', 0);
