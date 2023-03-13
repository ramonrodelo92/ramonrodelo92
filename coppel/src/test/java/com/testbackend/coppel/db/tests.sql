-- TESTS DATA
TRUNCATE usuario RESTART IDENTITY CASCADE;
insert into usuario (email, contrasena) values
('test@test.com', 'dev123');

TRUNCATE empleado RESTART IDENTITY CASCADE;
insert into empleado (apellido, nombre, puesto) values
('Smith', 'Michael', 'Tester'),
('Lopez', 'Juan', 'Manager'),
('Garza', 'Jose', 'Lead');

TRUNCATE inventario RESTART IDENTITY CASCADE;
insert into inventario (sku, cantidad, nombre) values
(111111, 100, 'Banco'),
(222222, 100, 'Silla'),
(333333, 100, 'Mesa'),
(444444, 98, 'Puerta'),
(555555, 98, 'Abanico');

TRUNCATE polizas RESTART IDENTITY CASCADE;
insert into polizas (cantidad, sku, id_empleado, fecha) values
(2, 444444, 2, now()),
(2, 555555, 3, now());