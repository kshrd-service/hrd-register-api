INSERT INTO baciis (grade, sort_order)
VALUES ('Grade A', DEFAULT),
       ('Grade B', DEFAULT),
       ('Grade C', DEFAULT),
       ('Grade D', DEFAULT),
       ('Grade E', DEFAULT),
       ('Grade Auto', DEFAULT),
       ('Other', DEFAULT);

INSERT INTO addresses (name, sort_order)
VALUES ('Tuol Kouk', DEFAULT),
       ('Sen Sok', DEFAULT),
       ('Russey Keo', DEFAULT),
       ('Prek Pnov', DEFAULT),
       ('Prampir Makara', DEFAULT),
       ('Pou Senchey', DEFAULT),
       ('Mean Chey', DEFAULT),
       ('Kamboul', DEFAULT),
       ('Daun Penh', DEFAULT),
       ('Dangkao', DEFAULT),
       ('Chroy Changvar', DEFAULT),
       ('Chbar Ampov', DEFAULT),
       ('Chamkar Mon', DEFAULT),
       ('Boeng Keng Kang', DEFAULT),
       ('Prek Kompoeus', DEFAULT),
       ('Other', DEFAULT);

INSERT INTO educations (level_of_education, sort_order)
VALUES ('Year 2 Semester 2', DEFAULT),
       ('Year 3 Semester 1', DEFAULT),
       ('Year 3 Semester 2', DEFAULT),
       ('Year 4 Semester 1', DEFAULT),
       ('Year 4 Semester 2', DEFAULT),
       ('Graduated', DEFAULT);

INSERT INTO provinces (name, sort_order)
VALUES ('Phnom Penh', DEFAULT),
       ('Kampong Cham', DEFAULT),
       ('Siem Reap', DEFAULT),
       ('Banteay Meanchey', DEFAULT),
       ('Battambang', DEFAULT),
       ('Kampong Chhnang', DEFAULT),
       ('Kampong Speu', DEFAULT),
       ('Kampong Thom', DEFAULT),
       ('Kampot', DEFAULT),
       ('Kandal', DEFAULT),
       ('Koh Kong', DEFAULT),
       ('Kep', DEFAULT),
       ('Kratie', DEFAULT),
       ('Mondulkiri', DEFAULT),
       ('Oddar Meanchey', DEFAULT),
       ('Pailin', DEFAULT),
       ('Preah Sihanouk', DEFAULT),
       ('Preah Vihear', DEFAULT),
       ('Pursat', DEFAULT),
       ('Prey Veng', DEFAULT),
       ('Ratanakiri', DEFAULT),
       ('Stung Treng', DEFAULT),
       ('Svay Rieng', DEFAULT),
       ('Takeo', DEFAULT),
       ('Tboung Khmum', DEFAULT);

INSERT INTO universities (abbreviation, sort_order)
VALUES ('RUPP', DEFAULT),
       ('SETEC', DEFAULT),
       ('BBU', DEFAULT),
       ('PUC', DEFAULT),
       ('PPIU', DEFAULT),
       ('NU', DEFAULT),
       ('UBB', DEFAULT),
       ('AEU', DEFAULT),
       ('UME', DEFAULT),
       ('RAC', DEFAULT),
       ('CARDI', DEFAULT),
       ('ITC', DEFAULT),
       ('CSUK', DEFAULT),
       ('NIE', DEFAULT),
       ('NTTI', DEFAULT),
       ('PNCA', DEFAULT),
       ('RUA', DEFAULT),
       ('RUFA', DEFAULT),
       ('RULE', DEFAULT),
       ('SRU', DEFAULT),
       ('UHS', DEFAULT),
       ('IIC', DEFAULT),
       ('UC', DEFAULT),
       ('UP', DEFAULT),
       ('PPIT', DEFAULT),
       ('CamEd', DEFAULT),
       ('SPI', DEFAULT),
       ('BELTEI', DEFAULT),
       ('CMU', DEFAULT),
       ('AUPP', DEFAULT),
       ('CUS', DEFAULT),
       ('NUM', DEFAULT),
       ('PCU', DEFAULT),
       ('PARAGON', DEFAULT),
       ('NIB', DEFAULT),
       ('AIB', DEFAULT),
       ('WU', DEFAULT),
       ('CADT', DEFAULT),
       ('AU', DEFAULT);

INSERT INTO generations (generation, sort_order)
VALUES ('1st Generation', DEFAULT),
       ('2nd Generation', DEFAULT),
       ('3rd Generation', DEFAULT),
       ('4th Generation', DEFAULT),
       ('5th Generation', DEFAULT),
       ('6th Generation', DEFAULT),
       ('7th Generation', DEFAULT),
       ('8th Generation', DEFAULT),
       ('9th Generation', DEFAULT),
       ('10th Generation', DEFAULT),
       ('11th Generation', DEFAULT),
       ('12th Generation', DEFAULT),
       ('13th Generation', DEFAULT),
       ('14th Generation', DEFAULT);


ALTER TABLE addresses
    ALTER COLUMN address_id SET DEFAULT gen_random_uuid();

ALTER TABLE educations
    ALTER COLUMN education_id SET DEFAULT gen_random_uuid();

ALTER TABLE provinces
    ALTER COLUMN province_id SET DEFAULT gen_random_uuid();

ALTER TABLE universities
    ALTER COLUMN university_id SET DEFAULT gen_random_uuid();

ALTER TABLE generations
    ALTER COLUMN generation_id SET DEFAULT gen_random_uuid();

ALTER TABLE baciis
    ALTER COLUMN bacii_id SET DEFAULT gen_random_uuid();

ALTER TABLE baciis
    ADD COLUMN IF NOT EXISTS sort_order INT;

CREATE SEQUENCE IF NOT EXISTS baciis_sort_seq START 1;

ALTER TABLE baciis
    ALTER COLUMN sort_order SET DEFAULT nextval('baciis_sort_seq');

ALTER TABLE addresses
    ADD COLUMN IF NOT EXISTS sort_order INT;

CREATE SEQUENCE IF NOT EXISTS addresses_sort_seq START 1;

ALTER TABLE addresses
    ALTER COLUMN sort_order SET DEFAULT nextval('addresses_sort_seq');

ALTER TABLE educations
    ADD COLUMN IF NOT EXISTS sort_order INT;

CREATE SEQUENCE IF NOT EXISTS educations_sort_seq START 1;

ALTER TABLE educations
    ALTER COLUMN sort_order SET DEFAULT nextval('educations_sort_seq');

ALTER TABLE provinces
    ADD COLUMN IF NOT EXISTS sort_order INT;

CREATE SEQUENCE IF NOT EXISTS provinces_sort_seq START 1;

ALTER TABLE provinces
    ALTER COLUMN sort_order SET DEFAULT nextval('provinces_sort_seq');

ALTER TABLE universities
    ADD COLUMN IF NOT EXISTS sort_order INT;

CREATE SEQUENCE IF NOT EXISTS universities_sort_seq START 1;

ALTER TABLE universities
    ALTER COLUMN sort_order SET DEFAULT nextval('universities_sort_seq');

ALTER TABLE generations
    ADD COLUMN IF NOT EXISTS sort_order INT;

CREATE SEQUENCE IF NOT EXISTS generations_sort_seq START 1;

ALTER TABLE generations
    ALTER COLUMN sort_order SET DEFAULT nextval('generations_sort_seq');

