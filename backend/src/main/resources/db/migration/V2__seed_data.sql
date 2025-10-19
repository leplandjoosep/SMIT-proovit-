
-- VINYLS (15)
INSERT INTO vinyl_record (title, artist, release_year, acquired_from, acquired_date, location, notes, fingerprint, created_at, updated_at)
VALUES
    ('Thriller',              'Michael Jackson', 1982, 'eBay',        DATE '2024-07-12', 'Living room shelf A1', 'Mint copy',          LOWER('Michael Jackson|Thriller|1982'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Back in Black',         'AC/DC',           1980, 'Flea market', DATE '2024-05-03', 'A1',                  'Slight ring wear',    LOWER('AC/DC|Back in Black|1980'),      CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('The Dark Side of the Moon','Pink Floyd',   1973, 'Discogs',     DATE '2024-04-21', 'A2',                  '1979 reissue',        LOWER('Pink Floyd|The Dark Side of the Moon|1973'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Rumours',               'Fleetwood Mac',   1977, 'Friend',      DATE '2024-03-15', 'A2',                  'Gift',                LOWER('Fleetwood Mac|Rumours|1977'),    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Nevermind',             'Nirvana',         1991, 'Record shop', DATE '2024-02-10', 'B1',                  'VG+ condition',       LOWER('Nirvana|Nevermind|1991'),        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Abbey Road',            'The Beatles',     1969, 'Discogs',     DATE '2024-01-26', 'B1',                  '',                    LOWER('The Beatles|Abbey Road|1969'),   CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('OK Computer',           'Radiohead',       1997, 'Netipoe',     DATE '2024-05-19', 'B2',                  '',                    LOWER('Radiohead|OK Computer|1997'),    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Kind of Blue',          'Miles Davis',     1959, 'Discogs',     DATE '2024-06-01', 'Jazz box',            'Mono',                LOWER('Miles Davis|Kind of Blue|1959'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Master of Puppets',     'Metallica',       1986, 'Record shop', DATE '2024-03-08', 'C1',                  '',                    LOWER('Metallica|Master of Puppets|1986'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Legend',                'Bob Marley',      1984, 'Flea market', DATE '2024-07-02', 'C1',                  'Compilation',         LOWER('Bob Marley|Legend|1984'),        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Like a Prayer',         'Madonna',         1989, 'eBay',        DATE '2024-08-18', 'C2',                  '',                    LOWER('Madonna|Like a Prayer|1989'),    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('A Night at the Opera',  'Queen',           1975, 'Discogs',     DATE '2024-05-28', 'C2',                  'Bohemian Rhapsody!',  LOWER('Queen|A Night at the Opera|1975'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Random Access Memories','Daft Punk',       2013, 'Netipoe',     DATE '2024-09-05', 'D1',                  '180g',                LOWER('Daft Punk|Random Access Memories|2013'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('The Joshua Tree',       'U2',              1987, 'Record shop', DATE '2024-06-22', 'D1',                  '',                    LOWER('U2|The Joshua Tree|1987'),       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('1989',                  'Taylor Swift',    2014, 'Netipoe',     DATE '2024-10-01', 'D2',                  'Deluxe',              LOWER('Taylor Swift|1989|2014'),        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


-- BIKE PARTS (15)
INSERT INTO bike_part (name, brand, category, quantity, location, notes, created_at, updated_at)
VALUES
    ('Aluminum Frame 54cm',  'Specialized', 'FRAME',     1, 'Garage A1', 'Road geometry',          CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Hydraulic Brake Set',  'Shimano',     'BRAKE',     2, 'Garage A2', 'Deore BR-M6100',         CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Disc Rotor 160mm',     'SRAM',        'BRAKE',     2, 'Garage A2', 'Centerline',             CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('All-round Tire 700x28','Continental', 'TIRE',      4, 'Garage B1', 'GP5000',                 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('MTB Tire 29x2.2',      'Maxxis',      'TIRE',      3, 'Garage B1', 'Ardent',                 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Floor Pump',           'Topeak',      'PUMP',      1, 'Garage Shelf', 'Gauge works',         CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Mini Pump',            'Lezyne',      'PUMP',      2, 'Toolbox', 'Compact',                  CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Chain 11-speed',       'KMC',         'CHAIN',     3, 'Garage C1', 'X11',                    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Saddle Comfort',       'Selle Royal', 'SADDLE',    1, 'Garage C2', 'Gel',                    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Handlebar 42cm',       'Zipp',        'HANDLEBAR', 1, 'Garage C2', 'Drop bar',               CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Pedal Set',            'Shimano',     'PEDAL',     2, 'Garage D1', 'SPD',                    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Cassette 11-32',       'SunRace',     'GEAR',      1, 'Garage D1', '',                       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Brake Cable Kit',      'Jagwire',     'CABLE',     5, 'Spare Box', '',                       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Front Light',          'Cateye',      'LIGHT',     1, 'Handlebar bin', 'USB',                CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Rear Rack',            'Topeak',      'RACK',      1, 'Garage D2', 'QuickTrack',             CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


-- LOANS (5)
INSERT INTO loan (bike_part_id, borrower_name, borrowed_at, due_at, returned_at)
VALUES
((SELECT id FROM bike_part WHERE name='Mini Pump' LIMIT 1),           'Kaur',   CURRENT_TIMESTAMP - INTERVAL '2' DAY,  CURRENT_TIMESTAMP + INTERVAL '5' DAY, NULL),
((SELECT id FROM bike_part WHERE name='Hydraulic Brake Set' LIMIT 1), 'Mari',   CURRENT_TIMESTAMP - INTERVAL '7' DAY,  CURRENT_TIMESTAMP + INTERVAL '1' DAY, NULL),
((SELECT id FROM bike_part WHERE name='Front Light' LIMIT 1),         'Siim',   CURRENT_TIMESTAMP - INTERVAL '10' DAY, CURRENT_TIMESTAMP - INTERVAL '1' DAY, NULL),
((SELECT id FROM bike_part WHERE name='Pedal Set' LIMIT 1),           'Laura',  CURRENT_TIMESTAMP - INTERVAL '20' DAY, CURRENT_TIMESTAMP - INTERVAL '10' DAY, CURRENT_TIMESTAMP - INTERVAL '5' DAY),
((SELECT id FROM bike_part WHERE name='MTB Tire 29x2.2' LIMIT 1),     'Arno',   CURRENT_TIMESTAMP - INTERVAL '1' DAY,  CURRENT_TIMESTAMP + INTERVAL '14' DAY, NULL);