create table sistema_gestion_finanzas.tarjetas_creditos
(
    id                 char(36)                      not null
        primary key,
    nombre             varchar(225)                  not null,
    descripcion        text                          null,
    montoOriginal      float                         not null,
    tipo               varchar(225) default 'Pasivo' null,
    fechaInicio        date                          not null,
    tipoTarjeta        varchar(225)                  not null,
    limiteCredito      float                         not null,
    terminacionTarjeta int(4)                        null,
    idUsuario          char(36)                      null,
    idCuentaBancaria   char(36)                      null,
    constraint tarjetas_creditos_ibfk_1
        foreign key (idUsuario) references sistema_gestion_finanzas.usuarios (id),
    constraint tarjetas_creditos_ibfk_2
        foreign key (idCuentaBancaria) references sistema_gestion_finanzas.cuentas_bancarias (id)
);

create index idCuentaBancaria
    on sistema_gestion_finanzas.tarjetas_creditos (idCuentaBancaria);

create index idUsuario
    on sistema_gestion_finanzas.tarjetas_creditos (idUsuario);

