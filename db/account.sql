create table riot_integration.account
(
    puuid             varchar(200)                           not null
        constraint pk_account
            primary key,
    account_name      varchar(100)                           not null,
    tag_line          varchar(5)                             not null,
    last_checked_date timestamp(6) default CURRENT_TIMESTAMP not null,
    constraint uk_account
        unique (account_name, tag_line)
);

alter table riot_integration.account
    owner to postgres;

