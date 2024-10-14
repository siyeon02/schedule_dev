create table schedule
(
    id bigint not null auto_increment,
    username varchar(25) not null ,
    title varchar(45),
    content varchar(200),
    created_at datetime,
    modified_at datetime,
    primary key (id)
);

create table user
(
    id bigint not null auto_increment,
    username varchar(25) not null ,
    email varchar(100),
    created_at datetime,
    modified_at datetime,
    primary key (id)
);

create table comment
(
    id bigint not null auto_increment,
    username varchar(25) not null ,
    comment varchar(200) not null,
    created_at datetime,
    modified_at datetime,
    primary key (id)
);