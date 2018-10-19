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
	warehouse_rack_id binary(16),
	primary key (id)
) engine=InnoDB;

create table wah_warehouse_level (
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
	warehouse_bay_id binary(16),
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
	item_lot_id varchar(255),
	last_modified_by_id varchar(50),
	last_modified_by_name varchar(50),
	last_modified_date datetime,
	quantity decimal(19,2),
	status integer,
	warehouse_location_id binary(16),
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
	warehouse_zone_id binary(16),
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
	warehouse_site_id binary(16),
	primary key (id)
) engine=InnoDB;

create table wah_warehouse_transaction (
	id binary(16) not null,
	item_id varchar(16),
	item_lot_id varchar(16),
	quantity decimal(19,2),
	transacted_by_id varchar(50),
	transacted_by_name varchar(50),
	transacted_date datetime,
	type varchar(20),
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
	warehouse_site_id binary(16),
	primary key (id)
) engine=InnoDB;

create index WAH_WAREHOUSE_TRANSACTION_ITEM_ID_IDX
	on wah_warehouse_transaction (item_id);

alter table wah_warehouse_bay
	add constraint FK13ix8wv673fhpfaaysjgc1l21 foreign key (warehouse_rack_id)
	references wah_warehouse_rack (id);

alter table wah_warehouse_level
	add constraint FK4crsxkxdcw2wwa16cjgx3jbvp foreign key (warehouse_bay_id)
	references wah_warehouse_bay (id);

alter table wah_warehouse_pack
	add constraint FKoy5lruvr4h1kd4en5vquxa93l foreign key (warehouse_location_id)
	references wah_warehouse_location (id);

alter table wah_warehouse_rack
	add constraint FKpj6k2ambry9tiamrw5kiahrbl foreign key (warehouse_zone_id)
	references wah_warehouse_zone (id);

alter table wah_warehouse_station
	add constraint FKg7n8hf067q01woho8bn3vxpcr foreign key (warehouse_site_id)
	references wah_warehouse_site (id);

alter table wah_warehouse_zone
	add constraint FK3f1r1vu6bjuaq4c7dym0adj6j foreign key (warehouse_site_id)
	references wah_warehouse_site (id);
