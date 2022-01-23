/* 1 - Descobrir o nome da constraint*/

SELECT constraint_name from information_schema.constraint_column_usage 
where table_name = 'usuario_roles' and column_name = 'role_id' 
and constraint_name <> 'unique_role_user';



/* 2 - Deletar a constraint*/

alter table usuario_roles drop constraint uk_jge8w8lp4rud8a3v1ni300ml5;



/* 3 - Inserir o usuario padrao*/
insert into usuario_roles(usuario_id, role_id) 
values(30, (select id from role where name_role = 'ROLE_USER'));