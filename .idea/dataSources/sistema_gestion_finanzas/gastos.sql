create table sistema_gestion_finanzas.gastos
(
    id               char(36)                      not null,
    nombre           varchar(225)                  not null,
    descripcion      text                          null,
    montoOriginal    float                         not null,
    tipo             varchar(225) default 'Pasivo' null,
    fechaInicio      date                          not null,
    acreedor         varchar(225)                  not null,
    frecuencia       int                           not null,
    categoriaGasto   varchar(225)                  not null,
    estatus          tinyint(1)   default 0        null,
    idUsuario        char(36)                      not null,
    idCuentaBancaria char(36)                      not null
)
    comment 'Table ''sistema_gestion_finanzas.gastos'' doesn''t exist in engine';

