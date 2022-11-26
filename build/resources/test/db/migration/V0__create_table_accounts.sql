create table accounts
(
    account_id            bigint not null auto_increment,
    account_creation_date date,
    account_number        bigint,
    account_type          integer,
    balance               bigint,
    currency              varchar(255),
    iban                  varchar(255),
    customer_customer_id  bigint,
    primary key (account_id)
) engine = MyISAM