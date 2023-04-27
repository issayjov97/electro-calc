create table firms
(
    id bigint auto_increment
        primary key,
    cin varchar(255) null,
    vatin varchar(255) null,
    city varchar(255) null,
    copy_default_patterns bit not null,
    email varchar(255) null,
    mobile varchar(255) null,
    name varchar(255) not null,
    phone varchar(255) null,
    post_code varchar(255) null,
    state varchar(255) null,
    street varchar(255) null,
    constraint uc_firm_name unique (name)
);

create table firm_settings
(
    id bigint primary key,
    charge_per_hour double not null,
    cost_per_km double not null,
    dph double not null,
    incision int not null,
    sale double not null,
    working_hours double not null,
    constraint fk_firm_settings_id
        foreign key (id) references firms (id)
);