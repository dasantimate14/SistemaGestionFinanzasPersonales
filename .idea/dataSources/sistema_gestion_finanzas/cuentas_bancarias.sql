create table sistema_gestion_finanzas.cuentas_bancarias
(
    id            char(36)                      not null,
    nombre        varchar(225)                  not null,
    descripcion   text                          null,
    montoOriginal float                         not null,
    tipo          varchar(225) default 'Activo' null,
    tasaInteres   float        default 0        null,
    fechaInicio   date                          not null,
    banco         varchar(255)                  not null,
    numeroCuenta  varchar(20)                   not null,
    tipoCuenta    varchar(225)                  not null,
    idUsuario     char(36)                      not null
)
    comment 'Table ''sistema_gestion_finanzas.cuentas_bancarias'' doesn''t exist in engine';

