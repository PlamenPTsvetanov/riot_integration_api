create table riot_integration.icon
(
    id            varchar(36) default gen_random_uuid() not null
        constraint pk_icon
            primary key,
    riot_id       integer,
    type          varchar                               not null,
    image         bytea                                 not null,
    champion_name varchar(20),
    constraint icon_pk
        unique (riot_id, type)
);

alter table riot_integration.icon
    owner to postgres;

