create table sistema_gestion_finanzas.usuarios
(
    id         char(36)     not null
        primary key,
    nombre     varchar(255) not null,
    email      varchar(255) not null,
    contrasena varchar(225) null,
    constraint email
        unique (email)
);

