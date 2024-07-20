create table sistema_gestion_finanzas.cuentas_bancarias
(
    id            char(36)                      not null
        primary key,
    nombre        varchar(225)                  not null,
    descripcion   text                          null,
    montoOriginal float                         not null,
    tipo          varchar(225) default 'Activo' null,
    tasaInteres   float        default 0        null,
    fechaInicio   date                          not null,
    banco         varchar(255)                  not null,
    numeroCuenta  varchar(20)                   not null,
    tipoCuenta    varchar(225)                  not null,
    idUsuario     char(36)                      null,
    constraint unique_numeroCuenta
        unique (numeroCuenta),
    constraint cuentas_bancarias_ibfk_1
        foreign key (idUsuario) references sistema_gestion_finanzas.usuarios (id),
    constraint chk_numero_cuenta
        check (`numeroCuenta` regexp '^[0-9-]+$')
);

create index idUsuario
    on sistema_gestion_finanzas.cuentas_bancarias (idUsuario);

