create table sistema_gestion_finanzas.ingresos
(
    id               char(36)                      not null
        primary key,
    nombre           varchar(225)                  not null,
    descripcion      text                          null,
    montoOriginal    float                         not null,
    tipo             varchar(225) default 'Activo' null,
    tasaInteres      float        default 0        null,
    fechaInicio      date                          not null,
    fuente           varchar(225)                  not null,
    frecuencia       int          default 0        null,
    idCliente        char(36)                      null,
    idCuentaBancaria char(36)                      null,
    constraint ingresos_ibfk_1
        foreign key (idCliente) references sistema_gestion_finanzas.usuarios (id),
    constraint ingresos_ibfk_2
        foreign key (idCuentaBancaria) references sistema_gestion_finanzas.cuentas_bancarias (id)
);

create index idCliente
    on sistema_gestion_finanzas.ingresos (idCliente);

create index idCuentaBancaria
    on sistema_gestion_finanzas.ingresos (idCuentaBancaria);

