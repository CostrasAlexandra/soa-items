create table users
(
    id       uuid not null
        primary key,
    password varchar(255),
    username varchar(255)
);

alter table users
    owner to postgres;

create table items
(
    id          uuid not null
        primary key,
    description varchar(255),
    user_id     uuid
);

alter table items
    owner to postgres;

select *
from items;
select *
from users;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

insert into users (id, username, password)
values ('73ebc0c7-9454-4791-aa0c-e586e8dfd2ca', 'Florin', 'password'),
       ('79c6f31e-8c58-4623-a8c0-6d7978d3d38e', 'Mircea', 'password'),
       ('1a0119d5-f28f-4d61-8eb0-fb5e8fc3521f', 'Ionela', 'password'),
       ('1ca524c1-ca97-493c-b91b-8b5137996fd3', 'Bogdan', 'password');


insert into items
values (gen_random_uuid(), 'Item 1', '73ebc0c7-9454-4791-aa0c-e586e8dfd2ca'),
       (gen_random_uuid(), 'Item 2', '79c6f31e-8c58-4623-a8c0-6d7978d3d38e'),
       (gen_random_uuid(), 'Item 3', '1a0119d5-f28f-4d61-8eb0-fb5e8fc3521f'),
       (gen_random_uuid(), 'Item 4', '79c6f31e-8c58-4623-a8c0-6d7978d3d38e'),
       (gen_random_uuid(), 'Item 5', '1ca524c1-ca97-493c-b91b-8b5137996fd3'),
       (gen_random_uuid(), 'Item 6', '1a0119d5-f28f-4d61-8eb0-fb5e8fc3521f');

select u.username, description
from items
         inner join users u on u.id = items.user_id
order by username