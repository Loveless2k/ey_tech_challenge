alter table users add column isactive BOOLEAN default true;

-- en caso de ya tener registros, los actualizamos.
update users set isactive = true;