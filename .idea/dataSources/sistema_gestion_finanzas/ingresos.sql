create table sistema_gestion_finanzas.ingresos
(
    id               char(36)                      not null,
    nombre           varchar(225)                  not null,
    descripcion      text                          null,
    montoOriginal    float                         not null,
    tipo             varchar(225) default 'Activo' null,
    fechaInicio      date                          not null,
    fuente           varchar(225)                  not null,
    frecuencia       int          default 0        null,
    idUsuario        char(36)                      not null,
    idCuentaBancaria char(36)                      not null
)
    comment 'Table ''sistema_gestion_finanzas.ingresos'' doesn''t exist in engine';

