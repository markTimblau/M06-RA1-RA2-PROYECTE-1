DROP TABLE IF EXISTS champions;

CREATE TABLE champions (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    title VARCHAR(50) NOT NULL,
    role VARCHAR(50) NOT NULL,
    difficulty VARCHAR(50) NOT NULL,
    release_date DATE NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    bio TEXT NOT NULL
);