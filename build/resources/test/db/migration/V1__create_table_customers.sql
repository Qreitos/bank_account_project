create table customers
(
    customer_id  bigint  not null auto_increment,
    birth_date   varchar(255),
    email        varchar(255),
    enabled      bit     not null,
    for_name     varchar(255),
    login_number integer not null,
    password     varchar(255),
    sur_name     varchar(255),
    primary key (customer_id)
) engine=MyISAM