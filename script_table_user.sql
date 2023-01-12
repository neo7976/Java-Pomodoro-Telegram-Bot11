create schema sobinda;

create table sobinda.user_timer
(
    id         bigserial,
    user_id    text not null,
    timer_type text not null,
    user_time  timestamp with time ZONE default now()

);

select *
from sobinda.user_timer;

insert into sobinda.user_timer(user_id, timer_type)
values ('2', 'test');

delete from sobinda.user_timer
where id >= 0;