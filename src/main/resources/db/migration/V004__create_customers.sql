create table customers
(
    id bigint auto_increment
        primary key,
    email varchar(255) null,
    name varchar(255) not null,
    note text null,
    phone varchar(255) null,
    firm_id bigint null,
    constraint uc_customers_name unique (name, firm_id),
        constraint fk_customers_firm_id
        foreign key (firm_id) references firms (id)
);

create index idx_customers_firm_id
    on customers (firm_id);