create table sistema_gestion_finanzas.prestamos
(
    id               char(36)                      not null,
    nombre           varchar(225)                  not null,
    descripcion      text                          null,
    montoOriginal    float                         not null,
    tipo             varchar(225) default 'Pasivo' null,
    fechaInicio      date                          not null,
    tipoPrestamo     varchar(225)                  not null,
    plazo            int                           not null,
    fechaVencimiento date                          not null,
    estatus          int          default 0        null,
    cuotaMensual     float                         null,
    idUsuario        char(36)                      not null,
    idCuentaBancaria char(36)                      not null,
    tasaInteres      float        default 0        null
)
    comment 'Table ''sistema_gestion_finanzas.prestamos'' doesn''t exist in engine';

