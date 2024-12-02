create table riot_integration.player_stats
(
    id              varchar(36)  default gen_random_uuid() not null
        primary key,
    win_percentage  varchar(20),
    avg_minions     varchar(20),
    minions_per_10  varchar(20),
    champion_name   varchar(40),
    games_played    integer,
    kills           varchar(3),
    deaths          varchar(3),
    assists         varchar(3),
    kda             varchar(5),
    last_check_date timestamp(6) default CURRENT_TIMESTAMP not null,
    puuid           varchar(200)                           not null
);

alter table riot_integration.player_stats
    owner to postgres;

