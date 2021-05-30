create schema photodb;

create table Album (
                       id int not null primary key auto_increment,
                       name varchar(50) not null unique
);

create table Photo (
                       id int not null primary key auto_increment,
                       albumId int not null,
                       name varchar(100) not null,
                       date date not null,
                       filePath nvarchar(100) not null,
                       thumbnailPath varchar(200) not null,
                       CONSTRAINT fk_album FOREIGN KEY(albumId) REFERENCES Album(id),
                       CONSTRAINT name UNIQUE (albumId, name)
);

select * from Album;
select * from Photo;