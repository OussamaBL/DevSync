create table tags
(
    id   serial
        primary key,
    name varchar(255) not null
        constraint uk_t48xdq560gs3gap9g7jg36kgc
            unique
);

alter table tags
    owner to "Devsync";

create table users
(
    id           bigserial
        primary key,
    email        varchar(255) not null
        constraint uk_6dotkott2kjsp8vw4d0m25fb7
            unique,
    first_name   varchar(255) not null,
    last_name    varchar(255) not null,
    password     varchar(255) not null,
    role_user    varchar(255) not null
        constraint users_role_user_check
            check ((role_user)::text = ANY ((ARRAY ['MANAGER'::character varying, 'USER'::character varying])::text[])),
    tokens       integer,
    username     varchar(255) not null
        constraint uk_r43af9ap4edm43mmtq01oddj6
            unique,
    dailytoken   integer,
    monthlytoken integer
);

alter table users
    owner to "Devsync";

create table tasks
(
    id              serial
        primary key,
    date_create     date         not null,
    date_fin        date,
    description     varchar(255),
    status          varchar(255) not null
        constraint tasks_status_check
            check ((status)::text = ANY
        ((ARRAY ['NOT_STARTED'::character varying, 'IN_PROGRESS'::character varying, 'COMPLETED'::character varying, 'OVERDUE'::character varying])::text[])),
    title           varchar(255) not null
        constraint uk_lbxbc3okttmi5ach9s94l296f
            unique,
    user_assigne_id bigint
        constraint fkiw2xkernl4o9vcjbq2abklwc1
            references users,
    user_create_id  bigint       not null
        constraint fk5gdf2ci6wcnc3gkr3pusfx62m
            references users,
    date_start      date,
    isrefused       boolean
);

alter table tasks
    owner to "Devsync";

create table task_tags
(
    task_id integer not null
        constraint fk7xi1reghkj37gqwlr1ujxrxll
            references tasks,
    tag_id  integer not null
        constraint fkeiqe3k9ent7icelm1cihqn164
            references tags
);

alter table task_tags
    owner to "Devsync";

create table taskrequest
(
    id           serial
        primary key,
    date_request date,
    status       varchar(255)
        constraint taskrequest_status_check
            check ((status)::text = ANY
        ((ARRAY ['PENDING'::character varying, 'ACCEPTED'::character varying, 'REJECTED'::character varying])::text[])),
    type         varchar(255)
        constraint taskrequest_type_check
            check ((type)::text = ANY ((ARRAY ['REJECT'::character varying, 'DELETE'::character varying])::text[])),
    task_id      integer
        constraint fko0saqbfvcfp0x9b9qbswmks3u
            references tasks,
    user_id      bigint
        constraint fknfn5vyxg8o6y8p3x9qmhwucix
            references users
);

alter table taskrequest
    owner to "Devsync";

