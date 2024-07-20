create table sistema_gestion_finanzas.gastos
(
    id               char(36)                      not null
        primary key,
    nombre           varchar(225)                  not null,
    descripcion      text                          null,
    montoOriginal    float                         not null,
    tipo             varchar(225) default 'Pasivo' null,
    fechaInicio      date                          not null,
    acreedor         varchar(225)                  not null,
    frecuencia       int                           not null,
    categoriaGasto   varchar(225)                  not null,
    estatus          tinyint(1)   default 0        null,
    idUsuario        char(36)                      null,
    idCuentaBancaria char(36)                      null,
    constraint gastos_ibfk_1
        foreign key (idUsuario) references sistema_gestion_finanzas.usuarios (id),
    constraint gastos_ibfk_2
        foreign key (idCuentaBancaria) references sistema_gestion_finanzas.cuentas_bancarias (id)
);

create index idCuentaBancaria
    on sistema_gestion_finanzas.gastos (idCuentaBancaria);

create index idUsuario
    on sistema_gestion_finanzas.gastos (idUsuario);

