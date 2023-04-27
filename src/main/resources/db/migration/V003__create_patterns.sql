create table patterns
(
    id bigint auto_increment
        primary key,
    price_withoutvat decimal(19,2) null,
    description text null,
    duration double null,
    measure_unit varchar(255) null,
    name varchar(255) not null,
    firm_id bigint null,
    constraint fk_patterns_firm_id
        foreign key (firm_id) references firms (id)
);

create index idx_pattern_name
    on patterns (name);

create index idx_pattern_firm_id
    on patterns (firm_id);

create table firm_pattern
(
    firm_id    bigint not null,
    pattern_id bigint not null,
    primary key (firm_id, pattern_id),
    constraint fk_firm_pattern_pattern_id
        foreign key (pattern_id) references patterns (id),
    constraint fk_firm_pattern_firm_id
        foreign key (firm_id) references firms (id)
);