create table sistema_gestion_finanzas.plazos_fijos
(
    id               char(36)                      not null
        primary key,
    nombre           varchar(225)                  not null,
    descripcion      text                          null,
    montoOriginal    float                         not null,
    tipo             varchar(225) default 'Activo' null,
    tasaInteres      float        default 0        null,
    fechaInicio      date                          not null,
    plazo            int                           not null,
    fechaFinal       date                          not null,
    idUsuario        char(36)                      null,
    idCuentaBancaria char(36)                      null,
    constraint plazos_fijos_ibfk_1
        foreign key (idUsuario) references sistema_gestion_finanzas.usuarios (id),
    constraint plazos_fijos_ibfk_2
        foreign key (idCuentaBancaria) references sistema_gestion_finanzas.cuentas_bancarias (id)
);

create index idCliente
    on sistema_gestion_finanzas.plazos_fijos (idUsuario);

create index idCuentaBancaria
    on sistema_gestion_finanzas.plazos_fijos (idCuentaBancaria);

