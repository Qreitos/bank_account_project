create table transactions
(
    id                   bigint not null auto_increment,
    amount               decimal(19, 2),
    currency             varchar(255),
    from_iban            varchar(255),
    realisation_date     datetime,
    status               varchar(255),
    to_iban              varchar(255),
    customer_customer_id bigint,
    primary key (id)
) engine=MyISAM