CREATE TABLE IF NOT EXISTS parcels (
                                       id BIGSERIAL PRIMARY KEY,
                                       name VARCHAR(255) NOT NULL UNIQUE,
                                       symbol CHAR(1) NOT NULL,
                                       shape TEXT NOT NULL,
                                       width INTEGER NOT NULL,
                                       height INTEGER NOT NULL,
                                       area INTEGER NOT NULL,
                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
