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
    title          varchar(255) NOT NULL,
    "version"      int8         NOT NULL,
    priority_level varchar(255) NULL,
    problem_type   varchar(255) NULL,
    status         varchar(255) NULL,
    expert_email   varchar(255) NULL,
    timestamp      timestamp    NOT NULL,
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
VALUES ('4935531461206', 'Apple iPhone 15 256GB Titanium Blu', 'Apple');
INSERT INTO products
    (ean, name, brand)
VALUES ('4935531461207', 'Apple iPhone 15 256GB Titanium Black', 'Apple');
INSERT INTO products
(ean, name, brand)
VALUES ('4935531461208', 'Apple iPhone 15 256GB Titanium Natural', 'Apple');
INSERT INTO products
(ean, name, brand)
VALUES ('4935531461209', 'Apple iPhone 15 512GB Titanium Blu', 'Apple');
INSERT INTO products
(ean, name, brand)
VALUES ('4935531461210', 'Apple iPhone 15 512GB Titanium Black', 'Apple');
INSERT INTO products
(ean, name, brand)
VALUES ('4935531461211', 'Apple iPhone 15 512GB Titanium Natural', 'Apple');
INSERT INTO products
(ean, name, brand)
VALUES ('4935531461212', 'Google Pixel 8 Pro 256GB Green', 'Google');
INSERT INTO products
(ean, name, brand)
VALUES ('4935531461213', 'Google Pixel 8 Pro 256GB Blue', 'Google');
INSERT INTO products
(ean, name, brand)
VALUES ('4935531461214', 'Google Pixel 8 Pro 256GB Black', 'Google');
INSERT INTO products
    (ean, name, brand)
VALUES ('4935531461215', 'Macbook Pro 1TB 16GB M2Pro Space Gray', 'Apple');
INSERT INTO products
    (ean, name, brand)
VALUES ('4935531461216', 'Macbook Pro 1TB 32GB M2Max Space Gray', 'Apple');

CREATE SEQUENCE IF NOT EXISTS attachment_seq INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS message_seq INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS status_changes_seq INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS tickets_seq INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS warranties_seq INCREMENT BY 50;
