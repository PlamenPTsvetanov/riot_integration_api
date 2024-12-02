create table riot_integration.summoner
(
    id              varchar(100)                        not null
        constraint summoner_pk
            primary key,
    puuid           varchar(200)                        not null
        constraint puuid_unique
            unique,
    account_id      varchar(100)                        not null,
    last_check_date timestamp default CURRENT_TIMESTAMP not null,
    profile_icon_id integer                             not null
);

alter table riot_integration.summoner
    owner to postgres;

create index summoner_puuid_index
    on riot_integration.summoner (puuid);

