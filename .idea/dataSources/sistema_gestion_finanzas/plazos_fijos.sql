create table sistema_gestion_finanzas.plazos_fijos
(
    id               char(36)                      not null,
    nombre           varchar(225)                  not null,
    descripcion      text                          null,
    montoOriginal    float                         not null,
    tipo             varchar(225) default 'Activo' null,
    tasaInteres      float        default 0        null,
    fechaInicio      date                          not null,
    plazo            int                           not null,
    fechaFinal       date                          not null,
    idUsuario        char(36)                      not null,
    idCuentaBancaria char(36)                      not null
)
    comment 'Table ''sistema_gestion_finanzas.plazos_fijos'' doesn''t exist in engine';

