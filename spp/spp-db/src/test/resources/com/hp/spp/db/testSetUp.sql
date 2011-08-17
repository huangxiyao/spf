create table test (
    id integer identity primary key,
    name varchar(200) not null
);

insert into test (id, name) values (null, 't1');
insert into test (id, name) values (null, 't2');
insert into test (id, name) values (null, 't3');
insert into test (id, name) values (null, 't4');
insert into test (id, name) values (17, 't5');

commit;
