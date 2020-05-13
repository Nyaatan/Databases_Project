DROP TABLE "WH_Employees" CASCADE CONSTRAINTS;
DROP TABLE "WH_Ordered_products" CASCADE CONSTRAINTS;
DROP TABLE "WH_Orders" CASCADE CONSTRAINTS;
DROP TABLE "WH_Products" CASCADE CONSTRAINTS;
DROP TABLE "WH_Shops" CASCADE CONSTRAINTS;
DROP VIEW WH_OrderView;
DROP TABLE "WH_Logs";
create table "WH_Employees"
(
    "Employee_id"      NUMBER       not null
        constraint WH_EMPLOYEES_PK
            primary key,
    "First_name"       VARCHAR2(32),
    "Last_name"        VARCHAR2(32) not null,
    "Completed_orders" NUMBER default 0,
    "Salary"           NUMBER(6, 2) not null
);

create table "WH_Shops"
(
    "Shop_id" NUMBER       not null
        constraint WH_SHOPS_PK
            primary key,
    "Address" VARCHAR2(64) not null,
    "Name"    VARCHAR2(64) not null
);

create table "WH_Orders"
(
    "Order_id"    NUMBER                         not null
        constraint WH_ORDERS_PK
            primary key,
    "Shop_id"     NUMBER                         not null
        constraint WH_ORDERS_SHOP_ID_FK
            references "WH_Shops"
            on delete set null,
    "Employee_id" NUMBER       default NULL
        constraint WH_ORDERS_EMPLOYEE_ID_FK
            references "WH_Employees"
            on delete set null,
    "Status"      VARCHAR2(11) default ''Waiting'' not null
        check ( "Status" IN (''Waiting'', ''In progress'', ''Completed'', ''Cancelled'') )
);

create table "WH_Products"
(
    "Product_id" NUMBER       not null
        constraint WH_PRODUCTS_PK
            primary key,
    "Name"       VARCHAR2(64) not null,
    "Count"      NUMBER       not null
);

create table "WH_Ordered_products"
(
    "WH_Ordered_product_id" NUMBER not null
        constraint WH_ORDERED_PRODUCTS_PK
            primary key,
    "Order_id"           NUMBER not null
        constraint WH_ORD_PROD_ORDER_ID_FK
            references "WH_Orders"
                on delete cascade,
    "Product_id"         NUMBER not null
        constraint WH_ORD_PROD_PRODUCT_ID_FK
            references "WH_Products"
                on delete cascade,
    "Count"              NUMBER not null
);

create table "WH_Logs"
(
	"Date" date not null,
	"User_name" varchar(64) not null,
	"Table_name" varchar(64) not null,
	"Action" varchar(64) not null,
    "Row_id" NUMBER not null
);

CREATE VIEW WH_OrderView AS
    SELECT s."Shop_id", s."Name" "Shop Name", COUNT( o."Shop_id") "WH_Orders"
FROM "WH_Orders" o JOIN "WH_Shops" s ON o."Shop_id" = s."Shop_id"
GROUP BY s."Shop_id", s."Name";

create or replace trigger employees_log
    after insert or update or delete on "WH_Employees"
    for each row
declare
    action varchar(64);
    row_id number(10);
begin
    if updating then
        action := ''Update'';
        row_id := :OLD."Employee_id";
    elsif inserting then
        action := ''Insert'';
        row_id := :NEW."Employee_id";
    elsif deleting then
        action := ''Delete'';
        row_id := :OLD."Employee_id";
    end if;

    insert into "WH_Logs"
        values (SYSDATE,USER, ''Employees'', action, row_id);
end;

create or replace trigger orders_log
    after insert or update or delete on "WH_Orders"
    for each row
declare
    action varchar(64);
    row_id number(10);
begin
    if updating then
        action := ''Update'';
        row_id := :OLD."Order_id";
    elsif inserting then
        action := ''Insert'';
        row_id := :NEW."Order_id";
    elsif deleting then
        action := ''Delete'';
        row_id := :OLD."Order_id";
    end if;

    insert into "WH_Logs"
        values (SYSDATE,USER, ''Orders'', action, row_id);
end;

create or replace trigger shops_log
    after insert or update or delete on "WH_Shops"
    for each row
declare
    action varchar(64);
    row_id number(10);
begin
    if updating then
        action := ''Update'';
        row_id := :OLD."Shop_id";
    elsif inserting then
        action := ''Insert'';
        row_id := :NEW."Shop_id";
    elsif deleting then
        action := ''Delete'';
        row_id := :OLD."Shop_id";
    end if;

    insert into "WH_Logs"
        values (SYSDATE,USER, ''Shops'', action, row_id);
end;

create or replace trigger products_log
    after insert or update or delete on "WH_Products"
    for each row
declare
    action varchar(64);
    row_id number(10);
begin
    if updating then
        action := ''Update'';
        row_id := :OLD."Product_id";
    elsif inserting then
        action := ''Insert'';
        row_id := :NEW."Product_id";
    elsif deleting then
        action := ''Delete'';
        row_id := :OLD."Product_id";
    end if;

    insert into "WH_Logs"
        values (SYSDATE,USER, ''Products'', action, row_id);
end;

create or replace trigger ordered_products_log
    after insert or update or delete on "WH_Ordered_products"
    for each row
declare
    action varchar(64);
    row_id number(10);
begin
    if updating then
        action := ''Update'';
        row_id := :OLD."WH_Ordered_product_id";
    elsif inserting then
        action := ''Insert'';
        row_id := :NEW."WH_Ordered_product_id";
    elsif deleting then
        action := ''Delete'';
        row_id := :OLD."WH_Ordered_product_id";
    end if;

    insert into "WH_Logs"
        values (SYSDATE,USER, ''Ordered_products'', action, row_id);
end;
COMMIT;

INSERT INTO "WH_Employees" ("Employee_id", "First_name", "Last_name", "Salary")
VALUES (1, ''Apolonia'', ''Sadowska'', 3000);
INSERT INTO "WH_Employees" ("Employee_id", "First_name", "Last_name", "Salary")
VALUES (2, ''Justyna'', ''Andrzejewska'', 3000);
INSERT INTO "WH_Employees" ("Employee_id", "First_name", "Last_name", "Salary")
VALUES (3, ''Jeremi'', ''Nowakowski'', 2000);
INSERT INTO "WH_Employees" ("Employee_id", "First_name", "Last_name", "Salary")
VALUES (4, ''Lena'', ''Gajewska'', 5000);
INSERT INTO "WH_Employees" ("Employee_id", "First_name", "Last_name", "Salary")
VALUES (5, ''Anastazja'', ''Kowalska'', 3000);
INSERT INTO "WH_Employees" ("Employee_id", "First_name", "Last_name", "Salary")
VALUES (6, ''Tomasz'', ''Bąk'', 4000);
INSERT INTO "WH_Employees" ("Employee_id", "First_name", "Last_name", "Salary")
VALUES (7, ''Antoni'', ''Stępień'', 3000);
INSERT INTO "WH_Employees" ("Employee_id", "First_name", "Last_name", "Salary")
VALUES (8, ''Julia'', ''Zając'', 3000);
INSERT INTO "WH_Employees" ("Employee_id", "First_name", "Last_name", "Salary")
VALUES (9, ''Matylda'', ''Sadowska'', 6000);
INSERT INTO "WH_Employees" ("Employee_id", "First_name", "Last_name", "Salary")
VALUES (10, ''Ernest'', ''Zalewski'', 2000);
INSERT INTO "WH_Employees" ("Employee_id", "First_name", "Last_name", "Salary")
VALUES (11, ''Matylda'', ''Dąbrowska'', 3000);
INSERT INTO "WH_Employees" ("Employee_id", "First_name", "Last_name", "Salary")
VALUES (12, ''Marek'', ''Dudek'', 5000);
INSERT INTO "WH_Employees" ("Employee_id", "First_name", "Last_name", "Salary")
VALUES (13, ''Aurelia'', ''Mazur'', 4000);
INSERT INTO "WH_Employees" ("Employee_id", "First_name", "Last_name", "Salary")
VALUES (14, ''Emilia'', ''Gajewska'', 3000);
INSERT INTO "WH_Employees" ("Employee_id", "First_name", "Last_name", "Salary")
VALUES (15, ''Patryk'', ''Wilk'', 6000);
INSERT INTO "WH_Employees" ("Employee_id", "First_name", "Last_name", "Salary")
VALUES (16, ''Anna'', ''Jasińska'', 5000);
INSERT INTO "WH_Employees" ("Employee_id", "First_name", "Last_name", "Salary")
VALUES (17, ''Witold'', ''Jakubowski'', 5000);
INSERT INTO "WH_Employees" ("Employee_id", "First_name", "Last_name", "Salary")
VALUES (18, ''Jacek'', ''Kaźmierczak'', 2000);
INSERT INTO "WH_Employees" ("Employee_id", "First_name", "Last_name", "Salary")
VALUES (19, ''Gabriel'', ''Ziółkowski'', 3000);
INSERT INTO "WH_Employees" ("Employee_id", "First_name", "Last_name", "Salary")
VALUES (20, ''Kamil'', ''Grabowski'', 5000);
COMMIT;

INSERT INTO "WH_Shops"
VALUES (1, ''Lawendowa 06A/72, 30-510 Gniezno'', ''Tomaszewska s. c.'');
INSERT INTO "WH_Shops"
VALUES (2, ''Wróblewskiego Walerego 22A, 28-923 Jaroszowa Wola'', ''Wysocka sp. p.'');
INSERT INTO "WH_Shops"
VALUES (3, ''Modrzewiowa 82A/74, 85-951 Kraśnik'', ''Mazur-Gajewski'');
INSERT INTO "WH_Shops"
VALUES (4, ''Bema Józefa 96/39, 22-496 Konstancin-Jeziorna'', ''Sawicki S.A.'');
INSERT INTO "WH_Shops"
VALUES (5, ''Zakątek 03, 34-565 Tczew'', ''Wasilewska'');
INSERT INTO "WH_Shops"
VALUES (6, ''Bohaterów Westerplatte 06/86, 28-068 Ostrów Wielkopolski'', ''Maciejewska sp. p.'');
INSERT INTO "WH_Shops"
VALUES (7, ''Jagiellońska 85A/98, 13-646 Tczew'', ''Krupa'');
INSERT INTO "WH_Shops"
VALUES (8, ''Boczna 91A/28, 69-095 Krępiec'', ''Nowakowski'');
INSERT INTO "WH_Shops"
VALUES (9, ''Białostocka 48A, 99-556 Tomaszów Mazowiecki'', ''Kamińska sp. p.'');
INSERT INTO "WH_Shops"
VALUES (10, ''Żytnia 46A, 67-844 Kościan'', ''Spółdzielnia Jankowska'');
INSERT INTO "WH_Shops"
VALUES (11, ''Szymanowskiego Karola 95A, 63-665 Bieruń'', ''Borkowska'');
INSERT INTO "WH_Shops"
VALUES (12, ''Chełmońskiego Józefa 51/91, 98-111 Pruszków'', ''Dąbrowski S.K.A'');
INSERT INTO "WH_Shops"
VALUES (13, ''Jastrzębia 40A, 36-193 Szteklin'', ''Kowalska-Adamska'');
INSERT INTO "WH_Shops"
VALUES (14, ''Mazowiecka 01, 20-975 Świebodzice'', ''Sawicki-Dudek'');
INSERT INTO "WH_Shops"
VALUES (15, ''Wczasowa 92A, 56-315 Legionowo'', ''Fundacja Szymczak'');
COMMIT;

INSERT INTO "WH_Orders"
VALUES (1, 2, 13, ''Waiting'');
INSERT INTO "WH_Orders"
VALUES (2, 4, 2, ''Completed'');
INSERT INTO "WH_Orders"
VALUES (3, 12, 9, ''Completed'');
INSERT INTO "WH_Orders"
VALUES (4, 6, 18, ''Waiting'');
INSERT INTO "WH_Orders"
VALUES (5, 9, 12, ''Waiting'');
INSERT INTO "WH_Orders"
VALUES (6, 8, 15, ''Cancelled'');
INSERT INTO "WH_Orders"
VALUES (7, 3, 15, ''In progress'');
INSERT INTO "WH_Orders"
VALUES (8, 14, 8, ''In progress'');
INSERT INTO "WH_Orders"
VALUES (9, 3, 5, ''Completed'');
INSERT INTO "WH_Orders"
VALUES (10, 8, 2, ''In progress'');
INSERT INTO "WH_Orders"
VALUES (11, 7, 11, ''Completed'');
INSERT INTO "WH_Orders"
VALUES (12, 14, 11, ''Cancelled'');
INSERT INTO "WH_Orders"
VALUES (13, 10, 3, ''In progress'');
INSERT INTO "WH_Orders"
VALUES (14, 1, 18, ''In progress'');
INSERT INTO "WH_Orders"
VALUES (15, 5, 8, ''Waiting'');
INSERT INTO "WH_Orders"
VALUES (16, 8, 13, ''In progress'');
INSERT INTO "WH_Orders"
VALUES (17, 12, 6, ''Waiting'');
INSERT INTO "WH_Orders"
VALUES (18, 5, 10, ''Waiting'');
INSERT INTO "WH_Orders"
VALUES (19, 4, 17, ''Waiting'');
INSERT INTO "WH_Orders"
VALUES (20, 5, 15, ''In progress'');
INSERT INTO "WH_Orders"
VALUES (21, 8, 14, ''Cancelled'');
INSERT INTO "WH_Orders"
VALUES (22, 14, 15, ''Completed'');
INSERT INTO "WH_Orders"
VALUES (23, 11, 4, ''Cancelled'');
INSERT INTO "WH_Orders"
VALUES (24, 14, 19, ''In progress'');
INSERT INTO "WH_Orders"
VALUES (25, 5, 7, ''In progress'');
INSERT INTO "WH_Orders"
VALUES (26, 1, 5, ''In progress'');
INSERT INTO "WH_Orders"
VALUES (27, 14, 6, ''Waiting'');
INSERT INTO "WH_Orders"
VALUES (28, 11, 15, ''In progress'');
INSERT INTO "WH_Orders"
VALUES (29, 9, 18, ''Completed'');
INSERT INTO "WH_Orders"
VALUES (30, 13, 7, ''In progress'');
COMMIT;

INSERT INTO "WH_Products"
VALUES (1, ''Agar 500g'', 656);
INSERT INTO "WH_Products"
VALUES (2, ''Kohlrabi 1000g'', 737);
INSERT INTO "WH_Products"
VALUES (3, ''Bread 1000g'', 1470);
INSERT INTO "WH_Products"
VALUES (4, ''Kiwi Fruit 800g'', 1019);
INSERT INTO "WH_Products"
VALUES (5, ''Tapioca Flour 1000g'', 1239);
INSERT INTO "WH_Products"
VALUES (6, ''Jasmine rice 500g'', 1010);
INSERT INTO "WH_Products"
VALUES (7, ''Tuna 700g'', 1116);
INSERT INTO "WH_Products"
VALUES (8, ''Feijoa 800g'', 1408);
INSERT INTO "WH_Products"
VALUES (9, ''Bean Shoots 500g'', 468);
INSERT INTO "WH_Products"
VALUES (10, ''Lobster 500g'', 788);
INSERT INTO "WH_Products"
VALUES (11, ''Green Chicken Curry 600g'', 1412);
INSERT INTO "WH_Products"
VALUES (12, ''Provolone 1100g'', 1265);
INSERT INTO "WH_Products"
VALUES (13, ''Quinoa 1000g'', 1327);
INSERT INTO "WH_Products"
VALUES (14, ''Apricots 500g'', 1254);
INSERT INTO "WH_Products"
VALUES (15, ''Potatoes 1000g'', 1199);
INSERT INTO "WH_Products"
VALUES (16, ''Aniseed 600g'', 686);
INSERT INTO "WH_Products"
VALUES (17, ''Turkey 1200g'', 749);
INSERT INTO "WH_Products"
VALUES (18, ''Brown rice vinegar 600g'', 549);
INSERT INTO "WH_Products"
VALUES (19, ''White wine vinegar 900g'', 1047);
INSERT INTO "WH_Products"
VALUES (20, ''Edam 1100g'', 393);
INSERT INTO "WH_Products"
VALUES (21, ''Redfish 700g'', 626);
INSERT INTO "WH_Products"
VALUES (22, ''Mullet 800g'', 1092);
INSERT INTO "WH_Products"
VALUES (23, ''Lychees 1200g'', 1260);
INSERT INTO "WH_Products"
VALUES (24, ''Blood oranges 1000g'', 393);
INSERT INTO "WH_Products"
VALUES (25, ''Cumquat 1100g'', 963);
INSERT INTO "WH_Products"
VALUES (26, ''Cashews 1200g'', 349);
INSERT INTO "WH_Products"
VALUES (27, ''Mackerel 1200g'', 584);
INSERT INTO "WH_Products"
VALUES (28, ''Papaw 800g'', 1030);
INSERT INTO "WH_Products"
VALUES (29, ''Lemongrass 600g'', 771);
INSERT INTO "WH_Products"
VALUES (30, ''Harissa 800g'', 1338);
INSERT INTO "WH_Products"
VALUES (31, ''Nasturtium 800g'', 1404);
INSERT INTO "WH_Products"
VALUES (32, ''Black Eyed Beans 1100g'', 947);
INSERT INTO "WH_Products"
VALUES (33, ''Lemon 700g'', 316);
INSERT INTO "WH_Products"
VALUES (34, ''Pumpkin Seed 1200g'', 474);
INSERT INTO "WH_Products"
VALUES (35, ''Parsley 700g'', 371);
INSERT INTO "WH_Products"
VALUES (36, ''Apples 700g'', 689);
INSERT INTO "WH_Products"
VALUES (37, ''Nori 800g'', 1000);
INSERT INTO "WH_Products"
VALUES (38, ''Edamame 500g'', 238);
INSERT INTO "WH_Products"
VALUES (39, ''Broccoli 1000g'', 1267);
INSERT INTO "WH_Products"
VALUES (40, ''Bush Tomato 800g'', 1499);
INSERT INTO "WH_Products"
VALUES (41, ''Mangosteens 1000g'', 1195);
INSERT INTO "WH_Products"
VALUES (42, ''Garam Masala 500g'', 1358);
INSERT INTO "WH_Products"
VALUES (43, ''Walnut 700g'', 1363);
INSERT INTO "WH_Products"
VALUES (44, ''Soba 1000g'', 490);
INSERT INTO "WH_Products"
VALUES (45, ''Sour Dough Bread 800g'', 1174);
INSERT INTO "WH_Products"
VALUES (46, ''Besan 500g'', 696);
INSERT INTO "WH_Products"
VALUES (47, ''Alfalfa 700g'', 1199);
INSERT INTO "WH_Products"
VALUES (48, ''Wheatgrass juice 1100g'', 1313);
INSERT INTO "WH_Products"
VALUES (49, ''Flaxseed Oil 1200g'', 825);
INSERT INTO "WH_Products"
VALUES (50, ''Cous Cous 800g'', 1187);
INSERT INTO "WH_Products"
VALUES (51, ''Strawberries 1200g'', 1113);
INSERT INTO "WH_Products"
VALUES (52, ''Fingerlime 700g'', 704);
INSERT INTO "WH_Products"
VALUES (53, ''Bocconcini 500g'', 619);
INSERT INTO "WH_Products"
VALUES (54, ''Parmesan cheese 800g'', 329);
INSERT INTO "WH_Products"
VALUES (55, ''Haricot Beans 600g'', 1095);
INSERT INTO "WH_Products"
VALUES (56, ''Apricots 500g'', 1123);
INSERT INTO "WH_Products"
VALUES (57, ''Marjoram 900g'', 439);
INSERT INTO "WH_Products"
VALUES (58, ''Asparagus 800g'', 770);
INSERT INTO "WH_Products"
VALUES (59, ''Vermicelli Noodles 500g'', 325);
INSERT INTO "WH_Products"
VALUES (60, ''Coconut water 1000g'', 1004);
INSERT INTO "WH_Products"
VALUES (61, ''Shiitake Mushrooms 900g'', 390);
INSERT INTO "WH_Products"
VALUES (62, ''Red Wine Vinegar 700g'', 819);
INSERT INTO "WH_Products"
VALUES (63, ''Porcini mushrooms 800g'', 1388);
INSERT INTO "WH_Products"
VALUES (64, ''Wholemeal 1000g'', 1409);
INSERT INTO "WH_Products"
VALUES (65, ''Incaberries 900g'', 1003);
INSERT INTO "WH_Products"
VALUES (66, ''Fennel Seeds 1100g'', 901);
INSERT INTO "WH_Products"
VALUES (67, ''Wholemeal 500g'', 1434);
INSERT INTO "WH_Products"
VALUES (68, ''Jicama 1100g'', 339);
INSERT INTO "WH_Products"
VALUES (69, ''Vinegar 600g'', 1143);
INSERT INTO "WH_Products"
VALUES (70, ''Raspberry 800g'', 863);
INSERT INTO "WH_Products"
VALUES (71, ''SwedeSweet Chilli Sauce 900g'', 711);
INSERT INTO "WH_Products"
VALUES (72, ''Sesame seed 900g'', 1484);
INSERT INTO "WH_Products"
VALUES (73, ''Cake 800g'', 754);
INSERT INTO "WH_Products"
VALUES (74, ''Chinese Cabbage 800g'', 515);
INSERT INTO "WH_Products"
VALUES (75, ''Walnut 800g'', 875);
INSERT INTO "WH_Products"
VALUES (76, ''Pears 600g'', 227);
INSERT INTO "WH_Products"
VALUES (77, ''Agave Syrup 600g'', 1311);
INSERT INTO "WH_Products"
VALUES (78, ''Paprik 1000g'', 1275);
INSERT INTO "WH_Products"
VALUES (79, ''Omega Spread 500g'', 900);
INSERT INTO "WH_Products"
VALUES (80, ''Custard ApplesDaikon 600g'', 308);
INSERT INTO "WH_Products"
VALUES (81, ''Chicken 1100g'', 1042);
INSERT INTO "WH_Products"
VALUES (82, ''Pumpkin Seed 900g'', 248);
INSERT INTO "WH_Products"
VALUES (83, ''Beans 1200g'', 287);
INSERT INTO "WH_Products"
VALUES (84, ''Freekeh 900g'', 1120);
INSERT INTO "WH_Products"
VALUES (85, ''Star Anise 800g'', 1388);
INSERT INTO "WH_Products"
VALUES (86, ''Arrowroot 800g'', 1038);
INSERT INTO "WH_Products"
VALUES (87, ''Chia seeds 1000g'', 383);
INSERT INTO "WH_Products"
VALUES (88, ''Sake 800g'', 384);
INSERT INTO "WH_Products"
VALUES (89, ''Ajowan Seed 900g'', 1470);
INSERT INTO "WH_Products"
VALUES (90, ''Coriander Leaves 700g'', 272);
INSERT INTO "WH_Products"
VALUES (91, ''Safflower Oil 500g'', 722);
INSERT INTO "WH_Products"
VALUES (92, ''Purple RiceQuail 1200g'', 1157);
INSERT INTO "WH_Products"
VALUES (93, ''Dried Apricots 1100g'', 1241);
INSERT INTO "WH_Products"
VALUES (94, ''Melon 700g'', 949);
INSERT INTO "WH_Products"
VALUES (95, ''Allspice 1100g'', 527);
INSERT INTO "WH_Products"
VALUES (96, ''Coconut Oil 800g'', 1329);
INSERT INTO "WH_Products"
VALUES (97, ''FlourOat 1200g'', 363);
INSERT INTO "WH_Products"
VALUES (98, ''Bonza 1200g'', 725);
INSERT INTO "WH_Products"
VALUES (99, ''Edamame 1200g'', 564);
INSERT INTO "WH_Products"
VALUES (100, ''Swiss Chard 700g'', 290);
COMMIT;

INSERT INTO "WH_Ordered_products"
VALUES (1, 1, 85, 110);
INSERT INTO "WH_Ordered_products"
VALUES (2, 1, 64, 100);
INSERT INTO "WH_Ordered_products"
VALUES (3, 1, 58, 50);
INSERT INTO "WH_Ordered_products"
VALUES (4, 1, 72, 180);
INSERT INTO "WH_Ordered_products"
VALUES (5, 1, 34, 80);
INSERT INTO "WH_Ordered_products"
VALUES (6, 2, 62, 30);
INSERT INTO "WH_Ordered_products"
VALUES (7, 2, 47, 70);
INSERT INTO "WH_Ordered_products"
VALUES (8, 2, 23, 190);
INSERT INTO "WH_Ordered_products"
VALUES (9, 3, 22, 20);
INSERT INTO "WH_Ordered_products"
VALUES (10, 3, 79, 140);
INSERT INTO "WH_Ordered_products"
VALUES (11, 3, 15, 190);
INSERT INTO "WH_Ordered_products"
VALUES (12, 3, 50, 70);
INSERT INTO "WH_Ordered_products"
VALUES (13, 4, 1, 150);
INSERT INTO "WH_Ordered_products"
VALUES (14, 4, 37, 150);
INSERT INTO "WH_Ordered_products"
VALUES (15, 4, 75, 170);
INSERT INTO "WH_Ordered_products"
VALUES (16, 5, 86, 170);
INSERT INTO "WH_Ordered_products"
VALUES (17, 5, 57, 90);
INSERT INTO "WH_Ordered_products"
VALUES (18, 6, 16, 70);
INSERT INTO "WH_Ordered_products"
VALUES (19, 6, 88, 60);
INSERT INTO "WH_Ordered_products"
VALUES (20, 6, 36, 50);
INSERT INTO "WH_Ordered_products"
VALUES (21, 6, 98, 180);
INSERT INTO "WH_Ordered_products"
VALUES (22, 6, 84, 20);
INSERT INTO "WH_Ordered_products"
VALUES (23, 7, 26, 100);
INSERT INTO "WH_Ordered_products"
VALUES (24, 7, 54, 190);
INSERT INTO "WH_Ordered_products"
VALUES (25, 7, 89, 30);
INSERT INTO "WH_Ordered_products"
VALUES (26, 8, 3, 20);
INSERT INTO "WH_Ordered_products"
VALUES (27, 8, 84, 190);
INSERT INTO "WH_Ordered_products"
VALUES (28, 8, 71, 40);
INSERT INTO "WH_Ordered_products"
VALUES (29, 8, 29, 70);
INSERT INTO "WH_Ordered_products"
VALUES (30, 9, 76, 90);
INSERT INTO "WH_Ordered_products"
VALUES (31, 9, 34, 120);
INSERT INTO "WH_Ordered_products"
VALUES (32, 9, 3, 140);
INSERT INTO "WH_Ordered_products"
VALUES (33, 10, 70, 200);
INSERT INTO "WH_Ordered_products"
VALUES (34, 10, 89, 50);
INSERT INTO "WH_Ordered_products"
VALUES (35, 11, 5, 120);
INSERT INTO "WH_Ordered_products"
VALUES (36, 11, 12, 50);
INSERT INTO "WH_Ordered_products"
VALUES (37, 11, 65, 100);
INSERT INTO "WH_Ordered_products"
VALUES (38, 11, 8, 190);
INSERT INTO "WH_Ordered_products"
VALUES (39, 12, 58, 30);
INSERT INTO "WH_Ordered_products"
VALUES (40, 12, 70, 20);
INSERT INTO "WH_Ordered_products"
VALUES (41, 13, 54, 130);
INSERT INTO "WH_Ordered_products"
VALUES (42, 13, 47, 40);
INSERT INTO "WH_Ordered_products"
VALUES (43, 13, 60, 110);
INSERT INTO "WH_Ordered_products"
VALUES (44, 13, 34, 200);
INSERT INTO "WH_Ordered_products"
VALUES (45, 14, 74, 90);
INSERT INTO "WH_Ordered_products"
VALUES (46, 14, 67, 30);
INSERT INTO "WH_Ordered_products"
VALUES (47, 14, 76, 170);
INSERT INTO "WH_Ordered_products"
VALUES (48, 14, 53, 70);
INSERT INTO "WH_Ordered_products"
VALUES (49, 14, 15, 100);
INSERT INTO "WH_Ordered_products"
VALUES (50, 15, 78, 140);
INSERT INTO "WH_Ordered_products"
VALUES (51, 15, 16, 60);
INSERT INTO "WH_Ordered_products"
VALUES (52, 15, 39, 150);
INSERT INTO "WH_Ordered_products"
VALUES (53, 15, 8, 60);
INSERT INTO "WH_Ordered_products"
VALUES (54, 15, 11, 70);
INSERT INTO "WH_Ordered_products"
VALUES (55, 16, 73, 130);
INSERT INTO "WH_Ordered_products"
VALUES (56, 16, 93, 130);
INSERT INTO "WH_Ordered_products"
VALUES (57, 16, 68, 120);
INSERT INTO "WH_Ordered_products"
VALUES (58, 16, 91, 30);
INSERT INTO "WH_Ordered_products"
VALUES (59, 17, 51, 70);
INSERT INTO "WH_Ordered_products"
VALUES (60, 17, 18, 30);
INSERT INTO "WH_Ordered_products"
VALUES (61, 17, 41, 80);
INSERT INTO "WH_Ordered_products"
VALUES (62, 17, 40, 60);
INSERT INTO "WH_Ordered_products"
VALUES (63, 18, 8, 80);
INSERT INTO "WH_Ordered_products"
VALUES (64, 18, 92, 30);
INSERT INTO "WH_Ordered_products"
VALUES (65, 18, 70, 90);
INSERT INTO "WH_Ordered_products"
VALUES (66, 18, 59, 50);
INSERT INTO "WH_Ordered_products"
VALUES (67, 18, 86, 80);
INSERT INTO "WH_Ordered_products"
VALUES (68, 19, 89, 180);
INSERT INTO "WH_Ordered_products"
VALUES (69, 19, 10, 50);
INSERT INTO "WH_Ordered_products"
VALUES (70, 20, 87, 200);
INSERT INTO "WH_Ordered_products"
VALUES (71, 20, 20, 40);
INSERT INTO "WH_Ordered_products"
VALUES (72, 20, 32, 200);
INSERT INTO "WH_Ordered_products"
VALUES (73, 20, 89, 140);
INSERT INTO "WH_Ordered_products"
VALUES (74, 20, 49, 50);
INSERT INTO "WH_Ordered_products"
VALUES (75, 21, 44, 70);
INSERT INTO "WH_Ordered_products"
VALUES (76, 21, 38, 170);
INSERT INTO "WH_Ordered_products"
VALUES (77, 21, 71, 20);
INSERT INTO "WH_Ordered_products"
VALUES (78, 21, 21, 140);
INSERT INTO "WH_Ordered_products"
VALUES (79, 21, 82, 20);
INSERT INTO "WH_Ordered_products"
VALUES (80, 22, 61, 160);
INSERT INTO "WH_Ordered_products"
VALUES (81, 22, 54, 160);
INSERT INTO "WH_Ordered_products"
VALUES (82, 22, 6, 90);
INSERT INTO "WH_Ordered_products"
VALUES (83, 22, 24, 110);
INSERT INTO "WH_Ordered_products"
VALUES (84, 23, 62, 160);
INSERT INTO "WH_Ordered_products"
VALUES (85, 23, 26, 200);
INSERT INTO "WH_Ordered_products"
VALUES (86, 23, 38, 140);
INSERT INTO "WH_Ordered_products"
VALUES (87, 23, 85, 80);
INSERT INTO "WH_Ordered_products"
VALUES (88, 24, 61, 130);
INSERT INTO "WH_Ordered_products"
VALUES (89, 24, 19, 110);
INSERT INTO "WH_Ordered_products"
VALUES (90, 24, 15, 190);
INSERT INTO "WH_Ordered_products"
VALUES (91, 24, 28, 80);
INSERT INTO "WH_Ordered_products"
VALUES (92, 25, 61, 100);
INSERT INTO "WH_Ordered_products"
VALUES (93, 25, 54, 120);
INSERT INTO "WH_Ordered_products"
VALUES (94, 25, 77, 160);
INSERT INTO "WH_Ordered_products"
VALUES (95, 25, 8, 120);
INSERT INTO "WH_Ordered_products"
VALUES (96, 26, 48, 160);
INSERT INTO "WH_Ordered_products"
VALUES (97, 26, 67, 100);
INSERT INTO "WH_Ordered_products"
VALUES (98, 26, 79, 130);
INSERT INTO "WH_Ordered_products"
VALUES (99, 26, 39, 120);
INSERT INTO "WH_Ordered_products"
VALUES (100, 27, 61, 190);
INSERT INTO "WH_Ordered_products"
VALUES (101, 27, 91, 30);
INSERT INTO "WH_Ordered_products"
VALUES (102, 28, 68, 100);
INSERT INTO "WH_Ordered_products"
VALUES (103, 28, 25, 140);
INSERT INTO "WH_Ordered_products"
VALUES (104, 28, 29, 150);
INSERT INTO "WH_Ordered_products"
VALUES (105, 29, 30, 70);
INSERT INTO "WH_Ordered_products"
VALUES (106, 29, 4, 60);
INSERT INTO "WH_Ordered_products"
VALUES (107, 29, 36, 170);
INSERT INTO "WH_Ordered_products"
VALUES (108, 30, 39, 150);
INSERT INTO "WH_Ordered_products"
VALUES (109, 30, 45, 180);
INSERT INTO "WH_Ordered_products"
VALUES (110, 30, 60, 160);
INSERT INTO "WH_Ordered_products"
VALUES (111, 30, 56, 140);
COMMIT;

COMMIT;