create table riot_integration.team
(
    id       varchar(36) default gen_random_uuid() not null
        constraint team_pk
            primary key,
    team_id  integer                               not null,
    win      boolean                               not null,
    match_id varchar(20)                           not null
        constraint team_match_id_fk
            references riot_integration.match
);

alter table riot_integration.team
    owner to postgres;

create index team_match_id_index
    on riot_integration.team (match_id);

