create table sistema_gestion_finanzas.prestamos
(
    id               char(36)                      not null
        primary key,
    nombre           varchar(225)                  not null,
    descripcion      text                          null,
    montoOriginal    float                         not null,
    tipo             varchar(225) default 'Activo' null,
    tasaInteres      float        default 0        null,
    fechaInicio      date                          not null,
    tipoPrestamo     varchar(225)                  not null,
    plazo            int                           not null,
    fechaVencimiento date                          not null,
    estatus          tinyint(1)   default 0        null,
    cuotaMensual     float                         null,
    idUsuario        char(36)                      null,
    constraint prestamos_ibfk_1
        foreign key (idUsuario) references sistema_gestion_finanzas.usuarios (id)
);

create index idUsuario
    on sistema_gestion_finanzas.prestamos (idUsuario);

