CREATE DATABASE IF NOT EXISTS practica;

use practica;

create table usuario(
	id_usuario int AUTO_INCREMENT  NOT NULL primary key,
    nombre varchar(255) not null,
    matricula varchar(255),
    fecha_nacimiento date,
    correo_electronico varchar(255)
);

create table libro(
	id_libro int auto_increment not null primary key,
    titulo varchar(255) not null,
    autores varchar(255),
    edicion varchar(255),
    editorial varchar(255),
    copias int 
);

create table prestamo(
	id_prestamo int not null auto_increment primary key,
    id_libro int not null ,
    id_usuario int not null,
    fecha_prestado date not null,
    fecha_devuelto date,
    foreign key (id_libro) references libro(id_libro),
    foreign key (id_usuario) references usuario(id_usuario)
);

DESCRIBE prestamo;
