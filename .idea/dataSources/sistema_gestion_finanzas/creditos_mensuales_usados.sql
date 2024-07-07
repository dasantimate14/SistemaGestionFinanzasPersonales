create table sistema_gestion_finanzas.creditos_mensuales_usados
(
    id                  int auto_increment
        primary key,
    creditoMensualUsado float    null,
    fechaCredito        date     null,
    idTarjetaCredito    char(36) null,
    constraint creditos_mensuales_usados_ibfk_1
        foreign key (idTarjetaCredito) references sistema_gestion_finanzas.tarjetas_creditos (id)
);

create index idTarjetaCredito
    on sistema_gestion_finanzas.creditos_mensuales_usados (idTarjetaCredito);

