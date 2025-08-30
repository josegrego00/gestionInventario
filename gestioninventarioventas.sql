create database gestion_inventario_venta;
use gestion_inventario_venta;

show databases;
show tables;
describe venta_detallada;
describe producto;

set SQL_SAFE_UPDATES=0;
update venta 
set estado_factura=1 where estado_factura is null;

create table cliente(
id int primary key auto_increment,
dniCliente varchar(20) not null unique,
nombre_cliente varchar(200) not null
);
describe compra;
Select * from venta_detallada;
Select * from compra;
Select * from cliente;

alter table compra 
modify column estado_compra boolean not null;

create table producto(
id int primary key auto_increment,
nombre_producto varchar(200) not null unique,
descripcion_producto varchar(200),
costo_producto decimal(20,2),
fecha_creacion timestamp,
id_proveedor int not null,
id_categoria int not null,
inventario decimal(10,2)
);
alter table producto
modify column codigo_barra varchar(200) not null unique;
alter table producto 
add constraint fk_producto_idproveedor foreign key(id_proveedor) references proveedor(id);
alter table producto 
add constraint fk_producto_idcategoria foreign key(id_categoria) references categoria(id);

create table proveedor(
id int primary key auto_increment,
nombre_proveedor varchar(200) not null,
descripcion_proveedor varchar(200)
);
create table categoria(
id int primary key auto_increment,
nombre_categoria varchar(200) not null,
descripcion_categoria varchar(200) not null
);

create table venta(
id int primary key auto_increment,
fecha_venta timestamp not null,
descripcion_venta varchar(2000),
total_venta decimal(20,2) not null
);

create table venta_detallada(
id int primary key auto_increment,
id_venta int not null,
id_producto int not null,
cantidad_vendida decimal(10,2) not null,
precio_producto decimal(20,2) not null
);
alter table venta_detallada 
add constraint fk_ventadetallada_idventa foreign key(id_venta) references venta(id);

create table usuarios(
id int primary key auto_increment,
nombre_usuario varchar(200) not null,
descripcion_usuario varchar(200),
id_rol int not null
);
create table rol(
id int primary key auto_increment,
nombre_rol varchar(200) not null
);
alter table usuarios
add column contrasenna varchar(50) not null after nombre_usuario;

-- Aqui van los Store Procedure--
-- aqui coloco el Strore procedure paar que cuando se genere una factura la misma descuente del inventario todo lo q se compro.
-- lo importante de esto es q no toca el codigo de java ya que queda todo en manos de MYSQl

drop PROCEDURE  if exists sp_descontar_inventario;

DELIMITER $$
$$
CREATE PROCEDURE sp_descontar_inventario(id_factura INT)
BEGIN
	-- AQUI DECLARO SI HAY UNA EXCEPTION PARA QUE REGRESE TODO A SU INICIO
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN 
		ROLLBACK;
    END;

-- INICIO DE TRANS 
	START TRANSACTION;
    UPDATE producto p
    JOIN venta_detallada vd ON p.id = vd.id_producto
    SET p.inventario = p.inventario - vd.cantidad_vendida        
		WHERE vd.id_venta = id_factura;
    COMMIT;
END;
$$	

CALL sp_descontar_inventario(126); 

-- Esta son las Tablas de Compras, es decir lo q la empresa compra --

create table compra(
id int primary key auto_increment,
n_factura varchar(300) not null unique,
fecha_compra timestamp not null,
descripcion_compra varchar(200),
total_compra decimal(20,2) not null,
id_proveedor int not null, -- Si tienes proveedores
estado_compra varchar(50)-- PENDIENTE, FINALIZADA, ANULADA
);

create table compra_detallada(
id int primary key auto_increment,
n_factura varchar(300) not null,
id_producto int not null,
cantidad_comprada decimal(10,2) not null,
precio_producto_comprado decimal(20,2) not null
);


-- Agregar la clave foránea en compra_detallada que referencia a compra
ALTER TABLE compra_detallada 
ADD CONSTRAINT fk_compra_factura 
FOREIGN KEY (n_factura) REFERENCES compra(n_factura);

-- Agregar la clave foránea en compra que referencia a proveedores (si existe)
ALTER TABLE compra 
ADD CONSTRAINT fk_compra_proveedor 
FOREIGN KEY (id_proveedor) REFERENCES proveedor(id);

-- Agregar la clave foránea en compra_detallada que referencia a productos (si 

ALTER TABLE compra_detallada 
ADD CONSTRAINT fk_compra_producto 
FOREIGN KEY (id_producto) REFERENCES producto(id);
```

