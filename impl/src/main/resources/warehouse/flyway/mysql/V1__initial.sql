create table wah_warehouse_bay (
	id binary(16) not null,
	code integer,
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	deleted bit not null,
	deleted_date datetime,
	last_modified_by_id varchar(50),
	last_modified_by_name varchar(50),
	last_modified_date datetime,
	location_code varchar(20),
	rack_id binary(16),
	primary key (id)
) engine=InnoDB;

create table wah_warehouse_level (
	id binary(16) not null,
	bay_id binary(16),
	code integer,
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	deleted bit not null,
	deleted_date datetime,
	last_modified_by_id varchar(50),
	last_modified_by_name varchar(50),
	last_modified_date datetime,
	location_code varchar(20),
	primary key (id)
) engine=InnoDB;

create table wah_warehouse_location (
	id binary(16) not null,
	code varchar(20),
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	deleted bit not null,
	deleted_date datetime,
	last_modified_by_id varchar(50),
	last_modified_by_name varchar(50),
	last_modified_date datetime,
	primary key (id)
) engine=InnoDB;

create table wah_warehouse_pack (
	id binary(16) not null,
	code varchar(20),
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	item_id binary(255),
	item_lot_id binary(255),
	last_modified_by_id varchar(50),
	last_modified_by_name varchar(50),
	last_modified_date datetime,
	location_id binary(16),
	quantity decimal(19,2),
	status integer,
	primary key (id)
) engine=InnoDB;

create table wah_warehouse_rack (
	id binary(16) not null,
	code integer,
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	deleted bit not null,
	deleted_date datetime,
	last_modified_by_id varchar(50),
	last_modified_by_name varchar(50),
	last_modified_date datetime,
	location_code varchar(20),
	zone_id binary(16),
	primary key (id)
) engine=InnoDB;

create table wah_warehouse_site (
	id binary(16) not null,
	code varchar(20),
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	deleted bit not null,
	deleted_date datetime,
	last_modified_by_id varchar(50),
	last_modified_by_name varchar(50),
	last_modified_date datetime,
	location_code varchar(20),
	name varchar(50),
	primary key (id)
) engine=InnoDB;

create table wah_warehouse_station (
	id binary(16) not null,
	code varchar(20),
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	deleted bit not null,
	deleted_date datetime,
	last_modified_by_id varchar(50),
	last_modified_by_name varchar(50),
	last_modified_date datetime,
	location_code varchar(20),
	name varchar(50),
	site_id binary(16),
	primary key (id)
) engine=InnoDB;

create table wah_warehouse_transaction (
	id binary(16) not null,
	item_id binary(16),
	item_lot_id binary(16),
	quantity decimal(19,2),
	transacted_by_id varchar(50),
	transacted_by_name varchar(50),
	transacted_date datetime,
	related_company_id varchar(50),
	type varchar(20),
	station_id binary(16),
	primary key (id)
) engine=InnoDB;

create table wah_warehouse_transaction_order (
	id binary(16) not null,
	accepted_by_id varchar(50),
	accepted_by_name varchar(50),
	accepted_date datetime,
	canceled_by_id varchar(50),
	canceled_by_name varchar(50),
	canceled_date datetime,
	code varchar(20),
	committable bit not null,
	committed_by_id varchar(50),
	committed_by_name varchar(50),
	committed_date datetime,
	completed_by_id varchar(50),
	completed_by_name varchar(50),
	completed_date datetime,
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	due_date datetime,
	quantity_correction_policy varchar(20),
	request_id binary(16),
	station_id binary(16),
	status varchar(20),
	transaction_company_id varchar(50),
	type varchar(20),
	primary key (id)
) engine=InnoDB;

create table wah_warehouse_transaction_order_item (
	id binary(16) not null,
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	item_id binary(16),
	order_id binary(16),
	quantity decimal(19,2),
	remained_quantity decimal(19,2),
	primary key (id)
) engine=InnoDB;

create table wah_warehouse_transaction_order_item_lot (
	id binary(16) not null,
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	item_lot_id binary(16),
	order_id binary(16),
	order_item_id binary(16),
	quantity decimal(19,2),
	remained_quantity decimal(19,2),
	primary key (id)
) engine=InnoDB;

create table wah_warehouse_transaction_order_pack (
	id binary(16) not null,
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	order_id binary(16),
	pack_id binary(16),
	status varchar(20),
	primary key (id)
) engine=InnoDB;

create table wah_warehouse_transaction_request (
	id binary(16) not null,
	accepted_by_id varchar(50),
	accepted_by_name varchar(50),
	accepted_date datetime,
	canceled_by_id varchar(50),
	canceled_by_name varchar(50),
	canceled_date datetime,
	code varchar(20),
	committable bit not null,
	committed_by_id varchar(50),
	committed_by_name varchar(50),
	committed_date datetime,
	completed_by_id varchar(50),
	completed_by_name varchar(50),
	completed_date datetime,
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	due_date datetime,
	quantity_correction_policy varchar(20),
	station_id binary(16),
	status varchar(20),
	transaction_company_id varchar(50),
	type varchar(20),
	primary key (id)
) engine=InnoDB;

create table wah_warehouse_transaction_request_item (
	id binary(16) not null,
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	item_id binary(16),
	quantity decimal(19,2),
	request_id binary(16),
	primary key (id)
) engine=InnoDB;

create table wah_warehouse_transaction_request_item_lot (
	id binary(16) not null,
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	item_lot_id binary(16),
	quantity decimal(19,2),
	request_id binary(16),
	request_item_id binary(16),
	primary key (id)
) engine=InnoDB;

create table wah_warehouse_zone (
	id binary(16) not null,
	code varchar(20),
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	deleted bit not null,
	deleted_date datetime,
	last_modified_by_id varchar(50),
	last_modified_by_name varchar(50),
	last_modified_date datetime,
	location_code varchar(20),
	site_id binary(16),
	primary key (id)
) engine=InnoDB;

create index IDXcffuy16qtnoy8v951m945qm9k
	on wah_warehouse_bay (rack_id);

create index IDXc76fg4349k412bjrp2bna6yyi
	on wah_warehouse_level (bay_id);

create index IDXqlt8bgrfkijkqcs2s8eb9gqu1
	on wah_warehouse_pack (item_lot_id);

create index IDX5c39efxnmtt4s4lwxadbbnfpk
	on wah_warehouse_rack (zone_id);

create index IDXouhbrlykiidkc81cqkkotk446
	on wah_warehouse_station (site_id);

create index IDXjrhlvaqhk9gdmpgfa42uou5ug
	on wah_warehouse_transaction (item_id);

create index IDXld2gc4c60qworp2v20sna8klt
	on wah_warehouse_transaction_order_item (order_id);

alter table wah_warehouse_transaction_order_item
	add constraint UKes6pqfcn5gg8yjhyw1ij4k8sv unique (order_id,item_id);

create index IDXhs4t9l127k11q3yufgkfn3yff
	on wah_warehouse_transaction_order_item_lot (order_item_id);

create index IDXe2f6vcbqeiqxgs6j29gwye0bq
	on wah_warehouse_transaction_order_item_lot (order_id);

alter table wah_warehouse_transaction_order_item_lot
	add constraint UKorh56unfx5rhujrw62c3l20i0 unique (order_item_id,item_lot_id);

create index IDX5i3dg3e9ykcygqq6oo63kb5ac
	on wah_warehouse_transaction_order_pack (order_id);

alter table wah_warehouse_transaction_order_pack
	add constraint UKfcs3fitdugxorkgsju9i6ixqh unique (order_id,pack_id);

create index IDX9hu0bfoq5s8t0t315bnyi5uem
	on wah_warehouse_transaction_request_item (request_id);

alter table wah_warehouse_transaction_request_item
	add constraint UKctv84ywpbvayy23o5xw3tv01u unique (request_id,item_id);

create index IDX7vmn9wbba6e2hwgbc4tghy7am
	on wah_warehouse_transaction_request_item_lot (request_item_id);

create index IDX2wixs34d46c0eigvyfaktt2i
	on wah_warehouse_transaction_request_item_lot (request_id);

alter table wah_warehouse_transaction_request_item_lot
	add constraint UK1ekht4l8kki1rae9fuhhjo1pm unique (request_item_id,item_lot_id);

create index IDX6956bt67m04f451jggpbo6mc5
	on wah_warehouse_zone (site_id);

alter table wah_warehouse_transaction
	add constraint FK5mybufp4hgyxp08ly2m1aipft foreign key (station_id)
	references wah_warehouse_station (id);
