create table table_name
(
    id number(*) generated as identity
        constraint table_name_pk
            primary key,
    word nvarchar2(35),
    frequency number(*)
);