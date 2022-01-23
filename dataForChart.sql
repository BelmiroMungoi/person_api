select array_agg( '''' || full_name || '''') from usuario where salario > 0 and full_name <> ''
union all
select cast(array_agg(salario) as character varying[]) from usuario where salario > 0 and full_name <> ''