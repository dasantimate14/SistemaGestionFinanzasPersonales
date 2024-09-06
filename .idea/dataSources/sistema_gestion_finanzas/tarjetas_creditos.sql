create table sistema_gestion_finanzas.tarjetas_creditos
(
    id                 char(36)                      not null,
    nombre             varchar(225)                  not null,
    descripcion        text                          null,
    montoOriginal      float                         not null,
    tipo               varchar(225) default 'Pasivo' null,
    fechaInicio        date                          not null,
    tipoTarjeta        varchar(225)                  not null,
    limiteCredito      float                         not null,
    terminacionTarjeta int(4)                        null,
    idUsuario          char(36)                      not null,
    idCuentaBancaria   char(36)                      not null
)
    comment 'Table ''sistema_gestion_finanzas.tarjetas_creditos'' doesn''t exist in engine';

