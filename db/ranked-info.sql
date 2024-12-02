create table riot_integration.summoner_ranked_info
(
    id              varchar(36)  default gen_random_uuid() not null
        constraint pk_summoner_ranked_info
            primary key,
    tier            varchar(20)                            not null,
    summoner_id     varchar(100)                           not null
        constraint fk_summoner_ranked_info_summoner
            references riot_integration.summoner,
    league_points   integer                                not null,
    wins            integer                                not null,
    losses          integer                                not null,
    last_check_date timestamp(6) default CURRENT_TIMESTAMP not null,
    rank            varchar(5)                             not null
);

alter table riot_integration.summoner_ranked_info
    owner to postgres;

create index summoner_ranked_info_summoner_id_index
    on riot_integration.summoner_ranked_info (summoner_id);

