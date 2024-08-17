
--h2 is an in memory database (data gets cleared with project)
--but if you use MySQL etc :  the schema will persist
--copied this from official spring security schema (users.ddl) from github


create table users(username varchar_ignorecase(50) not null primary key,password varchar_ignorecase(500) not null,enabled boolean not null);
create table authorities (username varchar_ignorecase(50) not null,authority varchar_ignorecase(50) not null,constraint fk_authorities_users foreign key(username) references users(username));
create unique index ix_auth_username on authorities (username,authority);