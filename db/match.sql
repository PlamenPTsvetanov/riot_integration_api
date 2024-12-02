create table riot_integration.match
(
    id   varchar(20) not null
        constraint pk_match
            primary key,
    data text        not null
);

alter table riot_integration.match
    owner to postgres;

