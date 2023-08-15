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


CREATE TABLE IF NOT EXISTS public.warranties
(
    id             int8         NOT NULL,
    start_date     date         NOT NULL,
    end_date       date         NOT NULL,
    product_ean    varchar(255) NOT NULL,
    typology       varchar(255) NOT NULL,
    customer_email varchar(255) NOT NULL,
    "version"      int8         NOT NULL,
    CONSTRAINT warranties_pkey PRIMARY KEY (id),
    FOREIGN KEY (product_ean) REFERENCES products (ean)
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
    expert_email   varchar(255) NULL,
    warranty_id    int8         NOT NULL,
    CONSTRAINT tickets_pkey PRIMARY KEY (id),
    FOREIGN KEY (warranty_id) REFERENCES warranties (id)
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
    CONSTRAINT fkhnb59gu2lb6heob6tsdy7hbqr FOREIGN KEY (ticket_id) REFERENCES tickets (id)
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
    CONSTRAINT fkoo11928qbsiolkc10dph1p214 FOREIGN KEY (message_id) REFERENCES message (id)
);




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

CREATE SEQUENCE IF NOT EXISTS attachment_seq INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS message_seq INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS status_changes_seq INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS tickets_seq INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS warranties_seq INCREMENT BY 50;
