create table wah_warehouse_bay (
	id varchar(255) not null,
	code integer,
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	deleted bit not null,
	deleted_date datetime,
	last_modified_by_id varchar(50),
	last_modified_by_name varchar(50),
	last_modified_date datetime,
	location_code varchar(255),
	warehouse_rack_id varchar(255),
	primary key (id)
) engine=InnoDB;

create table wah_warehouse_level (
	id varchar(255) not null,
	code integer,
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	deleted bit not null,
	deleted_date datetime,
	last_modified_by_id varchar(50),
	last_modified_by_name varchar(50),
	last_modified_date datetime,
	location_code varchar(255),
	warehouse_bay_id varchar(255),
	primary key (id)
) engine=InnoDB;

create table wah_warehouse_rack (
	id varchar(255) not null,
	code integer,
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	deleted bit not null,
	deleted_date datetime,
	last_modified_by_id varchar(50),
	last_modified_by_name varchar(50),
	last_modified_date datetime,
	location_code varchar(255),
	warehouse_zone_id varchar(255),
	primary key (id)
) engine=InnoDB;

create table wah_warehouse_site (
	id varchar(255) not null,
	code varchar(255),
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	deleted bit not null,
	deleted_date datetime,
	last_modified_by_id varchar(50),
	last_modified_by_name varchar(50),
	last_modified_date datetime,
	location_code varchar(255),
	name varchar(50),
	primary key (id)
) engine=InnoDB;

create table wah_warehouse_zone (
	id varchar(255) not null,
	code varchar(255),
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	deleted bit not null,
	deleted_date datetime,
	last_modified_by_id varchar(50),
	last_modified_by_name varchar(50),
	last_modified_date datetime,
	location_code varchar(255),
	warehouse_site_id varchar(255),
	primary key (id)
) engine=InnoDB;

alter table wah_warehouse_bay
	add constraint FK13ix8wv673fhpfaaysjgc1l21 foreign key (warehouse_rack_id)
	references wah_warehouse_rack (id);

alter table wah_warehouse_level
	add constraint FK4crsxkxdcw2wwa16cjgx3jbvp foreign key (warehouse_bay_id)
	references wah_warehouse_bay (id);

alter table wah_warehouse_rack
	add constraint FKpj6k2ambry9tiamrw5kiahrbl foreign key (warehouse_zone_id)
	references wah_warehouse_zone (id);

alter table wah_warehouse_zone
	add constraint FK3f1r1vu6bjuaq4c7dym0adj6j foreign key (warehouse_site_id)
	references wah_warehouse_site (id);
