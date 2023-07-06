--DROP TABLE temp_bpartner;

create table temp_bpartner(
c_bpartner_id NUMERIC (10) not null,  --1
bpartner_name varchar (120) not null, --2
tax_id varchar (20) null,             --3
value varchar (40) null,              --4
logo_id numeric (10) null,            --5
primary key (c_bpartner_id)
);

select * from temp_bpartner;

select * from customers c ;

delete from temp_bpartner 
where c_bpartner_id in (50008,50009)  ;

create table temp_prod_category(
m_product_category_id numeric (10) not null,
value varchar (40) not null,
name varchar (60) not null,
m_product_category_parent_id numeric (10) null,
primary key (m_product_category_id)
);

select * from temp_prod_category

select * from categories c

delete from temp_prod_category; --where m_product_category_id = 106

delete from categories; --where id = '';

update products p
set pricebuy = 1 ,
pricesell = 2 
where id = '133.0';

UPDATE STOCKCURRENT SET LOCATION = '102', PRODUCT = '123.0', ATTRIBUTESETINSTANCE_ID = null, UNITS = 10.0
where PRODUCT = '123.0';

select * from STOCKCURRENT;

select * from products p ;

select *

create table temp_vendor(
c_bpartner_id NUMERIC (10) not null,  --1
bpartner_name varchar (120) not null, --2
tax_id varchar (20) null,             --3
value varchar (40) null,              --4
logo_id numeric (10) null,            --5
primary key (c_bpartner_id)
);

-- Query ticket dan ticket line
select t.*, tl.* from tickets t, ticketlines tl
where t.id=tl.ticket;

-- table for record ID:
create table temp_recordid (
recordid numeric not null, 
isprocessed varchar(1), 
primary key ( recordid )
); 

UPDATE products
SET `code` = replace(code, '.0', '');

UPDATE products
SET `id` = replace(code, '.0', '');

-- automatic create SHOW item in Catalog
insert into products_cat (product)
 (select id from products) ;

