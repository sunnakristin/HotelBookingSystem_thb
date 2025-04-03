-- Create the database
CREATE DATABASE IF NOT EXISTS hotel_db;
USE hotel_db;

-- Create hotels table
CREATE TABLE IF NOT EXISTS hotels (
                                      hotel_id INT AUTO_INCREMENT PRIMARY KEY,
                                      name VARCHAR(100) NOT NULL,
    location VARCHAR(200) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

-- Create hotel_rooms table
CREATE TABLE IF NOT EXISTS hotel_rooms (
                                           room_id INT AUTO_INCREMENT PRIMARY KEY,
                                           hotel_id INT NOT NULL,
                                           type VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    number_of_guests INT NOT NULL,
    availability BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (hotel_id) REFERENCES hotels(hotel_id) ON DELETE CASCADE
    );

-- Create room_images table
CREATE TABLE IF NOT EXISTS room_images (
                                           image_id INT AUTO_INCREMENT PRIMARY KEY,
                                           room_id INT NOT NULL,
                                           image_path VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (room_id) REFERENCES hotel_rooms(room_id) ON DELETE CASCADE
    );

-- Insert sample data
INSERT INTO hotels (name, location) VALUES
                                        ('Grand Hotel', 'Reykjavik, Iceland'),
                                        ('Mountain View Hotel', 'Akureyri, Iceland'),
                                        ('Ocean Breeze Resort', 'Vik, Iceland');

-- Insert sample rooms
INSERT INTO hotel_rooms (hotel_id, type, price, number_of_guests, availability) VALUES
-- Grand Hotel rooms
(1, 'Standard', 100.00, 2, TRUE),
(1, 'Deluxe', 200.00, 4, TRUE),
(1, 'Suite', 300.00, 6, TRUE),
-- Mountain View Hotel rooms
(2, 'Standard', 90.00, 2, TRUE),
(2, 'Deluxe', 180.00, 4, TRUE),
(2, 'Suite', 270.00, 6, TRUE),
-- Ocean Breeze Resort rooms
(3, 'Standard', 120.00, 2, TRUE),
(3, 'Deluxe', 240.00, 4, TRUE),
(3, 'Suite', 360.00, 6, TRUE);

-- Insert sample room images
INSERT INTO room_images (room_id, image_path) VALUES
                                                  (1, '/images/rooms/standard1.jpg'),
                                                  (1, '/images/rooms/standard2.jpg'),
                                                  (2, '/images/rooms/deluxe1.jpg'),
                                                  (2, '/images/rooms/deluxe2.jpg'),
                                                  (3, '/images/rooms/suite1.jpg'),
                                                  (3, '/images/rooms/suite2.jpg');

-- Create indexes for better performance
CREATE INDEX idx_hotel_location ON hotels(location);
CREATE INDEX idx_room_type ON hotel_rooms(type);
CREATE INDEX idx_room_availability ON hotel_rooms(availability);
CREATE INDEX idx_room_guests ON hotel_rooms(number_of_guests);