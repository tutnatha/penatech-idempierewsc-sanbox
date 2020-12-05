create table temp_products(
prod_id 		 varchar (40),    ------>1
prod_reference 	 varchar (10),    ------>2
prod_code 		 varchar (10),    ------>3
prod_name 		 varchar (225),   ------>4
prod_pricebuy 	 float 	 ,	  ------>5
prod_pricesell 	 float   ,	  ------>6
prod_category_id varchar (10),    ------>7
prod_taxcat_id 	 varchar (10),    ------>8
prod_stockcost	 float   ,	  ------>9
prod_stockvolume float   ,	  ------>10
prod_iscom 		 bool    , ------>11
prod_isscale 	 bool    , ------>12
prod_isconstant  bool    , ------>13
prod_printkb 	 bool    , ------>14
prod_sendstatus  bool    , ------>15
prod_issercive 	 bool    , ------>16
prod_isvprice 	 bool    , ------>17
prod_isverpatrib bool    , ------>18
prod_warranty 	 bool    , ------>19
prod_stockunits  float   ,	  ------>20
primary key (prod_id));

-- public.temp_products2 definition

-- Drop table

-- DROP TABLE public.temp_products2;

CREATE TABLE temp_products2 (
	m_product_id numeric(10) NOT NULL,
	value varchar(40) NULL,
	"name" varchar(225) NULL,
	c_uom_id numeric(10) NULL,
	issummary varchar(1) NULL,
	isstocked varchar(1) NULL,
	ispurchased varchar(1) NULL,
	issold varchar(1) NULL,
	isbom varchar(1) NULL,
	isinvoiceprintdetails varchar(1) NULL,
	ispicklistprintdetails varchar(1) NULL,
	isverified varchar(1) NULL,
	m_product_category_id varchar(10) NULL,
	c_taxcategory_id varchar(10) NULL,
	producttype varchar(1) NULL,
	iswebstorefeatured varchar(1) NULL,
	isselfservice varchar(1) NULL,
	isdropship varchar(1) NULL,
	isexcludeautodelivery varchar(1) NULL,
	lowlevel numeric(10) NULL,
	unitsperpack numeric(10) NULL,
	iskanban varchar(1) NULL,
	ismanufactured varchar(1) NULL,
	isownbox varchar(1) NULL,
	CONSTRAINT temp_products2_pkey PRIMARY KEY (m_product_id)
);


--drop table temp_products2 ;

delete from temp_products2 ;

select * from temp_products2 tp 

select * from products where 1=1;

delete from products where 1 = 1;

select * from products_cat pc ;

select * from categories c ;

insert into taxcategories(id,name) values (107,'Standard');

select * from taxcategories;

select * from stockdiary;

select * from locations l ;

insert into stockcurrent (location , product ,units ) values (0,122.0,2);

select * from stockcurrent 

SELECT UNITS FROM stockcurrent 
WHERE LOCATION = '0' AND PRODUCT = '122.0' AND ATTRIBUTESETINSTANCE_ID IS null

select * from customers

SELECT 
    products.id,
    products.name,
    stockcurrent.units,
    locations.name,
    products.pricesell,
    taxes.rate,
    products.pricesell + (products.pricesell * taxes.rate) AS SellIncTax
    FROM (((stockcurrent stockcurrent
    INNER JOIN locations locations
    ON (stockcurrent.location = locations.id)) 
    INNER JOIN products products 
    ON (stockcurrent.product = products.id)) 
    INNER JOIN taxcategories taxcategories 
    ON (products.taxcat = taxcategories.id)) 
    INNER JOIN taxes taxes 
    ON (taxes.category = taxcategories.id)
 -- where 1=1
GROUP BY  products.id,products.name,stockcurrent.units,locations.name,taxes.rate
                        
--Mencari Tax Rate of Product
SELECT 
    products.id,
    products.name,
    products.pricesell,
    taxes.rate,
    products.pricesell + (products.pricesell * taxes.rate) AS SellIncTax
from products products
INNER JOIN taxcategories taxcategories 
    ON (products.taxcat = taxcategories.id) 
    INNER JOIN taxes taxes 
    ON (taxes.category = taxcategories.id);
    
   
SELECT products.id, locations.id as Location, stockcurrent.units AS Current, stocklevel.stocksecurity AS Minimum, stocklevel.stockmaximum AS Maximum, products.pricebuy, products.pricesell, products.memodate FROM locations INNER JOIN ((products INNER JOIN stockcurrent ON products.id = stockcurrent.product) LEFT JOIN stocklevel ON products.id = stocklevel.product) ON locations.id = stockcurrent.location WHERE products.id = 122.0 AND locations.id = ?

--Create tabel Baru menyimpan Product Price
create table temp_m_productprice (
m_pricelist_version_id numeric not null,
m_product_id numeric not null,
ad_client_id numeric not null,
ad_org_id numeric not null,
isactive bpchar not null default 'Y',
created timestamp not null default now (),
createdby numeric not null,
updated timestamp not null default now (),
updatedby numeric not null,
pricelist numeric not null,
pricestd numeric not null,
pricelimit numeric not null,
m_product_price_id numeric null,
primary key (m_product_price_id)
);

select * from temp_m_productprice;

delete from temp_m_productprice;

create table temp_m_storageonhand(
ad_client_id numeric not null,
ad_org_id numeric not null,
created timestamp not null,
createdby numeric not null,
isactive bpchar not null,
m_attributesetinstance_id numeric not null,
m_locator_id numeric not null,
m_product_id numeric not null,
qtyonhand numeric not null,
updated timestamp not null,
updatedby numeric not null,
datematerialpolicy timestamp not null,
primary key (m_locator_id, m_product_id, m_attributesetinstance_id, datematerialpolicy)
);


select * from temp_m_storageonhand;

delete from temp_m_storageonhand ;

SELECT ticket, --"+
	line, --"+
	product, --"+
	units, --"+
	price, --"+
	taxid FROM ticketlines;
	    	    		
select t.*, r.*, t.id, r.id from tickets t 
inner join receipts r on r.id = t.id  

select * from tickets t2 

SELECT t.id, --"+
		    		t.tickettype, --"+
		    		t.ticketid, --"+
		    		t.person, --"+
		    		t.customer, --"+
		    		t.status, --"+
		    		t.hostsync, --"+
		    		t.fiscalnumber, --"+
		    		t.cuponno, --"+
		    		t.fiscalserial, --"+
		    		t.notes, --"+
		    		t.ticketrefundnumber, --"+	    		
		    		r.datenew FROM tickets t --" +
		    		inner join receipts r on r.id = t.id  --"+
		    		--where t.id = 'f7ea19d4-e82d-4814-a3e6-80b4f9f9e78c'

select id 
from tickets t 
where ticketid = (select max(ticketid )from tickets t2 )

select * from ticketlines t 
where ticket = '71650d54-c8e8-46b1-8deb-d1b55c89bd3b'  --data yang terambil bukan data seharusnya tapi data yang sebelumnya