create table user
(
    id       int auto_increment
        primary key,
    name     varchar(255)         null,
    password varchar(255)         null,
    email    char                 null,
    ban      tinyint(1) default 0 not null,
    Wechat   int                  null,
    Role     varchar(10)          not null
);

create index idx_username
    on user (name);

create index user__index
    on user (email);
CREATE TABLE messages (
                          Messageid INT PRIMARY KEY,
                          Message TEXT NOT NULL,
                          senderid INT NOT NULL,
                          receiverid INT NOT NULL
);
