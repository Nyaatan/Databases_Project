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
/
create table "WH_Shops"
(
    "Shop_id" NUMBER       not null
        constraint WH_SHOPS_PK
            primary key,
    "Address" VARCHAR2(64) not null,
    "Name"    VARCHAR2(64) not null
);
/
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
    "Status"      VARCHAR2(11) default 'Waiting' not null
        check ( "Status" IN ('Waiting', 'In progress', 'Completed', 'Cancelled') )
);
/
create table "WH_Products"
(
    "Product_id" NUMBER       not null
        constraint WH_PRODUCTS_PK
            primary key,
    "Name"       VARCHAR2(64) not null,
    "Count"      NUMBER       not null
);
/
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
/
create table "WH_Logs"
(
	"Date" date not null,
	"User_name" varchar(64) not null,
	"Table_name" varchar(64) not null,
	"Action" varchar(64) not null,
    "Row_id" NUMBER not null
);
/
CREATE VIEW WH_OrderView AS
    SELECT s."Shop_id", s."Name" "Shop Name", COUNT( o."Shop_id") "WH_Orders"
FROM "WH_Orders" o JOIN "WH_Shops" s ON o."Shop_id" = s."Shop_id"
GROUP BY s."Shop_id", s."Name";
/
create or replace trigger employees_log
    after insert or update or delete on "WH_Employees"
    for each row
declare
    action varchar(64);
    row_id number(10);
begin
    if updating then
        action := 'Update';
        row_id := :OLD."Employee_id";
    elsif inserting then
        action := 'Insert';
        row_id := :NEW."Employee_id";
    elsif deleting then
        action := 'Delete';
        row_id := :OLD."Employee_id";
    end if;

    insert into "WH_Logs"
        values (SYSDATE,USER, 'Employees', action, row_id);
end;
/
create or replace trigger orders_log
    after insert or update or delete on "WH_Orders"
    for each row
declare
    action varchar(64);
    row_id number(10);
begin
    if updating then
        action := 'Update';
        row_id := :OLD."Order_id";
    elsif inserting then
        action := 'Insert';
        row_id := :NEW."Order_id";
    elsif deleting then
        action := 'Delete';
        row_id := :OLD."Order_id";
    end if;

    insert into "WH_Logs"
        values (SYSDATE,USER, 'Orders', action, row_id);
end;
/
create or replace trigger shops_log
    after insert or update or delete on "WH_Shops"
    for each row
declare
    action varchar(64);
    row_id number(10);
begin
    if updating then
        action := 'Update';
        row_id := :OLD."Shop_id";
    elsif inserting then
        action := 'Insert';
        row_id := :NEW."Shop_id";
    elsif deleting then
        action := 'Delete';
        row_id := :OLD."Shop_id";
    end if;

    insert into "WH_Logs"
        values (SYSDATE,USER, 'Shops', action, row_id);
end;
/
create or replace trigger products_log
    after insert or update or delete on "WH_Products"
    for each row
declare
    action varchar(64);
    row_id number(10);
begin
    if updating then
        action := 'Update';
        row_id := :OLD."Product_id";
    elsif inserting then
        action := 'Insert';
        row_id := :NEW."Product_id";
    elsif deleting then
        action := 'Delete';
        row_id := :OLD."Product_id";
    end if;

    insert into "WH_Logs"
        values (SYSDATE,USER, 'Products', action, row_id);
end;
/
create or replace trigger ordered_products_log
    after insert or update or delete on "WH_Ordered_products"
    for each row
declare
    action varchar(64);
    row_id number(10);
begin
    if updating then
        action := 'Update';
        row_id := :OLD."WH_Ordered_product_id";
    elsif inserting then
        action := 'Insert';
        row_id := :NEW."WH_Ordered_product_id";
    elsif deleting then
        action := 'Delete';
        row_id := :OLD."WH_Ordered_product_id";
    end if;

    insert into "WH_Logs"
        values (SYSDATE,USER, 'Ordered_products', action, row_id);
end;
/
COMMIT;