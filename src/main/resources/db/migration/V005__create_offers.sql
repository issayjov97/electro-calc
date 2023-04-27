create table offers
(
    id bigint auto_increment
        primary key,
    price_withoutvat decimal(19,2) null,
    created_date timestamp null,
    description text null,
    distance double not null,
    name varchar(255) null,
    note text null,
    status varchar(255) null,
    customer_id bigint null,
    firm_id bigint null,
    constraint uc_offers_name unique (name, firm_id),
    constraint fk_offers_firm_id
        foreign key (firm_id) references firms (id),
    constraint fk_offers_customer_id
        foreign key (customer_id) references customers (id)
);

create index idx_offers_firm_id
    on offers (firm_id);

create table offer_pattern
(
    count int null,
    offer_id bigint not null,
    pattern_id bigint not null,
    primary key (offer_id, pattern_id),
    constraint fk_offer_pattern_offer_id
        foreign key (offer_id) references offers (id),
    constraint fk_offer_pattern_pattern_id
        foreign key (pattern_id) references patterns (id)
);
