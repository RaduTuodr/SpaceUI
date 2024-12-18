CREATE DATABASE space;

CREATE TABLE planet_type(
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(20)
);

CREATE TABLE planet(
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(20),
                       type_id INT,
                       FOREIGN KEY (type_id) REFERENCES planet_type(id)
);

CREATE TYPE CIV_TYPE AS ENUM ('Type0', 'Type1', 'Type2', 'Type3');

CREATE TABLE species(
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(20),
                        civil_type_id CIV_TYPE,
                        home_planet_id INT,
                        FOREIGN KEY (home_planet_id) REFERENCES planet(id)
);

CREATE TABLE ship_classes(
                             id SERIAL PRIMARY KEY,
                             name VARCHAR(20)
);

CREATE TABLE spaceship(
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(20),
                          class_id INT,
                          FOREIGN KEY (class_id) REFERENCES ship_classes(id)
);

CREATE TABLE resources(
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(40) UNIQUE NOT NULL,
                          type CIV_TYPE,
                          description TEXT NOT NULL,
                          value DECIMAL NOT NULL,
                          unit VARCHAR(20)
);

CREATE TABLE planet_resources(
                                 id SERIAL PRIMARY KEY,
                                 amount DECIMAL,
                                 planet_id INT,
                                 resource_id INT,
                                 FOREIGN KEY (planet_id) REFERENCES planet(id),
                                 FOREIGN KEY (resource_id) REFERENCES resources(id)
);

CREATE TYPE MISSION_STATUS AS ENUM ('In progress', 'Completed', 'Failed');

CREATE TABLE missions(
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(40),
                         description TEXT,
                         start_date DATE,
                         end_date DATE,
                         status MISSION_STATUS,
                         ship_id INT,
                         FOREIGN KEY (ship_id) REFERENCES spaceship(id)
);

CREATE TYPE FLEET_TYPE AS ENUM ('Combat', 'Exploration', 'Trade');

CREATE TABLE fleets(
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(100),
                       fleet_type FLEET_TYPE NOT NULL,
                       commander_name VARCHAR(100),
                       crew_capacity INT,
                       home_planet_id INT,
                       fleet_mission_id INT,
                       FOREIGN KEY (home_planet_id) REFERENCES planet(id),
                       FOREIGN KEY (fleet_mission_id) REFERENCES missions(id)
);

CREATE TYPE EVENT_TYPE AS ENUM ('Conflict', 'Diplomatic', 'Economic');

CREATE TABLE events(
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(40),
                       type EVENT_TYPE,
                       description TEXT NOT NULL,
                       date DATE,
                       resolved BOOLEAN DEFAULT FALSE,
                       planet_id INT,
                       species_id INT,
                       FOREIGN KEY (planet_id) REFERENCES planet(id),
                       FOREIGN KEY (species_id) REFERENCES species(id)
);

--------------------------- SOME UNIQUE CONSTRAINTS --------------------------

ALTER TABLE planet ADD CONSTRAINT unique_planet_name UNIQUE (name);
ALTER TABLE species ADD CONSTRAINT unique_species_name UNIQUE (name);
ALTER TABLE spaceship ADD CONSTRAINT unique_spaceship_name UNIQUE (name);
ALTER TABLE planet_resources ADD CONSTRAINT unique_planet_resource UNIQUE (planet_id, resource_id);
ALTER TABLE missions ADD CONSTRAINT check_end_after_start CHECK (end_date > start_date);
ALTER TABLE fleets ADD CONSTRAINT check_crew_capacity_nonnegative CHECK (crew_capacity >= 0);
ALTER TABLE resources ADD CONSTRAINT check_resource_value_nonnegative CHECK (value >= 0);
ALTER TABLE planet_resources ADD CONSTRAINT check_resource_amount_nonnegative CHECK (amount >= 0);
ALTER TABLE events ADD CONSTRAINT check_event_date CHECK (date <= CURRENT_DATE);

--------------------------- POPULATING THE TABLES ---------------------------

INSERT INTO planet_type (name) VALUES
                                   ('Terrestrial'),
                                   ('Gas Giant'),
                                   ('Ice Giant'),
                                   ('Dwarf');

INSERT INTO planet (name, type_id) VALUES
                                       ('Earth', 1),
                                       ('Mars', 1),
                                       ('Jupiter', 2),
                                       ('Neptune', 3),
                                       ('Pluto', 4);

INSERT INTO species (name, civil_type_id, home_planet_id) VALUES
                                                              ('Humans', 'Type0', 1),
                                                              ('Martians', 'Type1', 2),
                                                              ('Jovians', 'Type2', 3);

INSERT INTO ship_classes (name) VALUES
                                    ('Explorer'),
                                    ('Battleship'),
                                    ('Freighter');

INSERT INTO spaceship (name, class_id) VALUES
                                           ('Voyager', 1),
                                           ('Defender', 2),
                                           ('Galactic Freighter', 3);

INSERT INTO resources (name, type, description, value, unit) VALUES
                                                                 ('Gold', 'Type0', 'Precious metal used in technology.', 5000.0, 'kg'),
                                                                 ('Helium-3', 'Type1', 'Fuel for fusion reactors.', 10000.0, 'kg'),
                                                                 ('Water', 'Type0', 'Essential for life.', 2.0, 'liters');

INSERT INTO planet_resources (amount, planet_id, resource_id) VALUES
                                                                  (1000.0, 1, 1), -- Earth has 1000kg of Gold
                                                                  (5000.0, 3, 2), -- Jupiter has 5000kg of Helium-3
                                                                  (200000.0, 1, 3); -- Earth has 200000 liters of Water

INSERT INTO missions (name, description, start_date, end_date, status, ship_id) VALUES
                                                                                    ('Alpha Exploration', 'First mission to explore Mars.', '2024-01-01', '2024-12-31', 'In progress', 1),
                                                                                    ('Defensive Fleet Mission', 'Defend against a possible alien threat.', '2023-05-01', '2023-06-01', 'Completed', 2);

INSERT INTO fleets (name, fleet_type, commander_name, crew_capacity, home_planet_id, fleet_mission_id) VALUES
                                                                                                           ('Earth Exploration Fleet', 'Exploration', 'Commander Shepard', 100, 1, 1),
                                                                                                           ('Martian Defense Fleet', 'Combat', 'General Zod', 200, 2, 2);
a
INSERT INTO events (name, type, description, date, resolved, planet_id, species_id) VALUES
('Earth-Mars Treaty', 'Diplomatic', 'Establishment of a trade route between Earth and Mars.', '2024-03-15', TRUE, 1, 1),
('Jovian Resource Conflict', 'Conflict', 'Dispute over Helium-3 extraction rights.', '2023-07-21', FALSE, 3, 3);
