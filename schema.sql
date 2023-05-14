-- public.products definition

-- Drop table

-- DROP TABLE public.products;

CREATE TABLE IF NOT EXISTS public.products
(
    ean    varchar(255) NOT NULL,
    brand  varchar(255) NULL,
    "name" varchar(255) NULL,
    CONSTRAINT products_pkey PRIMARY KEY (ean)
);


-- public.profiles definition

-- Drop table

-- DROP TABLE public.profiles;

CREATE TABLE IF NOT EXISTS public.profiles
(
    email   varchar(255) NOT NULL,
    "name"  varchar(255) NOT NULL,
    surname varchar(255) NOT NULL,
    CONSTRAINT profiles_pkey PRIMARY KEY (email)
);


-- public.profile_skills definition

-- Drop table

-- DROP TABLE public.profile_skills;

CREATE TABLE IF NOT EXISTS public.profile_skills
(
    profile_email varchar(255) NOT NULL,
    skills        varchar(255) NULL,
    CONSTRAINT fkdvm53pu3nti4dav2vidsj8bv8 FOREIGN KEY (profile_email) REFERENCES profiles (email)
);


-- public.tickets definition

-- Drop table

-- DROP TABLE public.tickets;

CREATE TABLE IF NOT EXISTS public.tickets
(
    id             int8         NOT NULL,
    "version"      int8         NOT NULL,
    priority_level varchar(255) NULL,
    problem_type   varchar(255) NULL,
    status         varchar(255) NULL,
    customer_email varchar(255) NOT NULL,
    expert_email   varchar(255) NULL,
    product_ean    varchar(255) NOT NULL,
    CONSTRAINT tickets_pkey PRIMARY KEY (id),
    CONSTRAINT fk9m28r3wh8pt856v8w9r7we969 FOREIGN KEY (product_ean) REFERENCES products (ean),
    CONSTRAINT fkpxhaghy083e9rjplpcxa3vaoh FOREIGN KEY (customer_email) REFERENCES profiles (email),
    CONSTRAINT fkqu447rw3ykl5bgwxgn4lalgb9 FOREIGN KEY (expert_email) REFERENCES profiles (email)
);


-- public.message definition

-- Drop table

-- DROP TABLE public.message;

CREATE TABLE IF NOT EXISTS public.message
(
    id          int8         NOT NULL,
    "version"   int8         NOT NULL,
    "text"      varchar(255) NOT NULL,
    "timestamp" timestamp    NOT NULL,
    ticket_id   int8         NULL,
    user_email  varchar(255) NULL,
    CONSTRAINT message_pkey PRIMARY KEY (id),
    CONSTRAINT fkhnb59gu2lb6heob6tsdy7hbqr FOREIGN KEY (ticket_id) REFERENCES tickets (id),
    CONSTRAINT fkpae8ihj1ajx2q5iknitan60wt FOREIGN KEY (user_email) REFERENCES profiles (email)
);


-- public.status_changes definition

-- Drop table

-- DROP TABLE public.status_changes;

CREATE TABLE IF NOT EXISTS public.status_changes
(
    id          int8         NOT NULL,
    "version"   int8         NOT NULL,
    status      varchar(255) NULL,
    "timestamp" timestamp    NULL,
    ticket_id   int8         NULL,
    user_email  varchar(255) NOT NULL,
    CONSTRAINT status_changes_pkey PRIMARY KEY (id),
    CONSTRAINT fkbt46hd55iohsv0xk70ofipgpu FOREIGN KEY (user_email) REFERENCES profiles (email),
    CONSTRAINT fkpvlilvfs17xwbdnxcrlq75yjs FOREIGN KEY (ticket_id) REFERENCES tickets (id)
);


-- public.attachment definition

-- Drop table

-- DROP TABLE public.attachment;

CREATE TABLE IF NOT EXISTS public.attachment
(
    id         int8         NOT NULL,
    "version"  int8         NOT NULL,
    "content"  oid          NOT NULL,
    "name"     varchar(255) NOT NULL,
    "type"     varchar(255) NOT NULL,
    message_id int8         NULL,
    user_email varchar(255) NULL,
    CONSTRAINT attachment_pkey PRIMARY KEY (id),
    CONSTRAINT fkoo11928qbsiolkc10dph1p214 FOREIGN KEY (message_id) REFERENCES message (id),
    CONSTRAINT fkqtnm4x6je8s07mhjjvbya346w FOREIGN KEY (user_email) REFERENCES profiles (email)
);

INSERT INTO profiles (email, name, surname)
VALUES ('manager@mail.com', 'Marco', 'Mele');

INSERT INTO profiles (email, name, surname)
VALUES ('client@mail.com', 'Francesco', 'Russo');

INSERT INTO profiles (email, name, surname)
VALUES ('expert@mail.com', 'Federico', 'Rinaudi');

INSERT INTO profiles (email, name, surname)
VALUES ('client1@mail.com', 'Leonardo', 'Volpini');

INSERT INTO profile_skills (profile_email, skills)
VALUES ('expert@mail.com', 'HARDWARE');


INSERT INTO products
    (ean, name, brand)
VALUES ('4935531461206', 'JMT X-ring 530x2 Gold 104 Open Chain With Rivet Link for Kawasaki KH 400 a 1976', 'JMT');
INSERT INTO products
    (ean, name, brand)
VALUES ('3521201753911', '1x Summer Tyre Michelin Pilot Sport 4 255/40zr17 98y El', 'Michelin');
INSERT INTO products
    (ean, name, brand)
VALUES ('5013812835005', 'Kent Bag of Rags 500g 100 Cotton KR500', 'Kent');
INSERT INTO products
    (ean, name, brand)
VALUES ('5051247498761', 'Sealey Tools VS3815 Suspension Arm Lever', 'Sealey');
INSERT INTO products
    (ean, name, brand)
VALUES ('4007817332127', 'Staedtler Lumocolor Medium Tip Water Soluble OHP Black Pen St33192', 'Staedtler');
INSERT INTO products
    (ean, name, brand)
VALUES ('5052746112566', 'BM Fitting Kit FK80303B for Exhaust Catalytic Converter Bm80303h Fits OPEL', 'BM Catalysts');
INSERT INTO products
    (ean, name, brand)
VALUES ('4260558857718', '32gb Mini Button Security Camera WiFi WLAN IP Live App Video Tone Recording A106', 'IBM');
INSERT INTO products
    (ean, name, brand)
VALUES ('5905133212661', 'Pipe Connector Exhaust System FA1 913-962', 'FA1');
INSERT INTO products
    (ean, name, brand)
VALUES ('8424445071292', 'Rear View Mirror Glass Pair LHD Only Alkar 6401453 2pcs P OE Replacement', 'Alkar');
INSERT INTO products
    (ean, name, brand)
VALUES ('6932799230098', 'Superior Quality Club Special Poker Playing Cards No. 2009', 'TJM');