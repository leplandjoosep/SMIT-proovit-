CREATE TABLE vinyl_record (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    artist VARCHAR(255) NOT NULL,
    release_year INT,
    acquired_from VARCHAR(255),
    acquired_date DATE,
    location VARCHAR(255),
    notes TEXT,
    fingerprint VARCHAR(512),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX ux_vinyl_fingerprint ON vinyl_record(fingerprint);


CREATE TABLE bike_part (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    brand VARCHAR(255),
    category VARCHAR(50),
    quantity INT NOT NULL DEFAULT 1,
    location VARCHAR(255),
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE loan (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    bike_part_id UUID NOT NULL,
    borrower_name VARCHAR(255) NOT NULL,
    borrowed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    due_at TIMESTAMP,
    returned_at TIMESTAMP,
    CONSTRAINT fk_loan_part FOREIGN KEY (bike_part_id) REFERENCES bike_part(id) ON DELETE CASCADE
);

CREATE INDEX ix_bike_part_category ON bike_part(category);
CREATE INDEX ix_loan_part_status ON loan(bike_part_id, returned_at);
