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
	related_company_id varchar(50),
	transacted_by_id varchar(50),
	transacted_by_name varchar(50),
	transacted_date datetime,
	type varchar(20),
	station_id binary(16),
	primary key (id)
) engine=InnoDB;

create table wah_warehouse_transaction_request (
	id binary(16) not null,
	canceled_by_id varchar(50),
	canceled_by_name varchar(50),
	canceled_date datetime,
	committable bit not null,
	committed_by_id varchar(50),
	committed_by_name varchar(50),
	committed_date datetime,
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	due_date datetime,
	related_company_id varchar(50),
	station_id binary(16),
	status varchar(20),
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
	transaction_request_id binary(16),
	primary key (id)
) engine=InnoDB;

create table wah_warehouse_transaction_request_item_lot (
	id binary(16) not null,
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	item_lot_id binary(16),
	quantity decimal(19,2),
	transaction_request_id binary(16),
	transaction_request_item_id binary(16),
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

create index IDX5c39efxnmtt4s4lwxadbbnfpk
	on wah_warehouse_rack (zone_id);

create index IDXouhbrlykiidkc81cqkkotk446
	on wah_warehouse_station (site_id);

create index IDXjrhlvaqhk9gdmpgfa42uou5ug
	on wah_warehouse_transaction (item_id);

create index IDXajgv36fi8jdie9qvyguh9otuk
	on wah_warehouse_transaction_request_item (transaction_request_id);

alter table wah_warehouse_transaction_request_item
	add constraint UKk2wvnkdvhmdc8o6dcflhsmihg unique (transaction_request_id,item_id);

create index IDX9nbhtec5ojj1efcsu7kl06nou
	on wah_warehouse_transaction_request_item_lot (transaction_request_item_id);

create index IDXfigf3jgdex60u9xdxy2btg2sa
	on wah_warehouse_transaction_request_item_lot (transaction_request_id);

alter table wah_warehouse_transaction_request_item_lot
	add constraint UK59p72xwsdgt16ihh2ona1i8g1 unique (transaction_request_item_id,item_lot_id);

create index IDX6956bt67m04f451jggpbo6mc5
	on wah_warehouse_zone (site_id);

alter table wah_warehouse_transaction
	add constraint FK5mybufp4hgyxp08ly2m1aipft foreign key (station_id)
	references wah_warehouse_station (id);
