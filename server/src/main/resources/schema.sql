--db name = products

create table if not exists products
(
    ean   varchar(15) primary key,
    name  varchar(255),
    brand varchar(255)
);

create table if not exists profiles
(
    email   varchar(255) primary key,
    name    varchar(127),
    surname varchar(127)
);

insert into profiles (email, name, surname) values ('marcomele18@gmail.com', 'Marco', 'Mele');

INSERT INTO products
(ean, name, brand)
VALUES('4935531461206', 'JMT X-ring 530x2 Gold 104 Open Chain With Rivet Link for Kawasaki KH 400 a 1976', 'JMT');
INSERT INTO products
(ean, name, brand)
VALUES('3521201753911', '1x Summer Tyre Michelin Pilot Sport 4 255/40zr17 98y El', 'Michelin');
INSERT INTO products
(ean, name, brand)
VALUES('5013812835005', 'Kent Bag of Rags 500g 100 Cotton KR500', 'Kent');
INSERT INTO products
(ean, name, brand)
VALUES('5051247498761', 'Sealey Tools VS3815 Suspension Arm Lever', 'Sealey');
INSERT INTO products
(ean, name, brand)
VALUES('4007817332127', 'Staedtler Lumocolor Medium Tip Water Soluble OHP Black Pen St33192', 'Staedtler');
INSERT INTO products
(ean, name, brand)
VALUES('5052746112566', 'BM Fitting Kit FK80303B for Exhaust Catalytic Converter Bm80303h Fits OPEL', 'BM Catalysts');
INSERT INTO products
(ean, name, brand)
VALUES('4260558857718', '32gb Mini Button Security Camera WiFi WLAN IP Live App Video Tone Recording A106', '');
INSERT INTO products
(ean, name, brand)
VALUES('5905133212661', 'Pipe Connector Exhaust System FA1 913-962', 'FA1');
INSERT INTO products
(ean, name, brand)
VALUES('8424445071292', 'Rear View Mirror Glass Pair LHD Only Alkar 6401453 2pcs P OE Replacement', 'Alkar');
INSERT INTO products
(ean, name, brand)
VALUES('6932799230098', 'Superior Quality Club Special Poker Playing Cards No. 2009', 'TJM');
