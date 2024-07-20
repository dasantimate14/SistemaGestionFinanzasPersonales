create table sistema_gestion_finanzas.stocks
(
    id                   char(36)                      not null
        primary key,
    nombre               varchar(225)                  not null,
    descripcion          text                          null,
    montoOriginal        float                         not null,
    tipo                 varchar(225) default 'Activo' null,
    fechaInicio          date                          not null,
    nombreEmpresa        varchar(225)                  not null,
    simbolo              varchar(225)                  not null,
    cantidad             int                           not null,
    precioCompra         float                         not null,
    sector               varchar(225)                  not null,
    dividendoPorAccion   float        default 0        null,
    frecuenciaDividendos int          default 0        null,
    idUsuario            char(36)                      null,
    constraint fk_stocks_usuarios
        foreign key (idUsuario) references sistema_gestion_finanzas.usuarios (id)
);

