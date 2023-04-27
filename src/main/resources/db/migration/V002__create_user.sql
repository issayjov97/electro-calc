create table users
(
    id             bigint auto_increment not null
        primary key,
    created_at     timestamp null,
    email          varchar(255) null,
    enabled        bit null,
    first_name     varchar(255) null,
    last_name      varchar(255) null,
    password       varchar(255) null,
    username       varchar(255) null,
    firm_id bigint null,
    constraint uc_user_username unique (username),
    constraint fk_users_firm_id
        foreign key (firm_id) references firms (id)
);

create index idx_firm_id
    on users (firm_id);

create table authorities
(
    id   bigint auto_increment not null
        primary key,
    name varchar(255) null,
    constraint uc_user_authority_name unique (name)
);

create table user_authority
(
    user_id      bigint not null,
    authority_id bigint not null,
    primary key (user_id, authority_id),
    constraint pk_user_authority_user_id_authority_id
        foreign key (authority_id) references authorities (id),
    constraint fk_user_authority_user_id
        foreign key (user_id) references users (id)
);

create table otp
(
    id         bigint
        primary key,
    created_at timestamp null,
    value      varchar(30) not null,
    constraint fk_otp_id
        foreign key (id) references users (id)
);