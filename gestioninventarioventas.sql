create database gestion_inventario_venta;
use gestion_inventario_venta;
drop table producto, proveedor, inventario;
show tables;

create table producto(
id int primary key auto_increment,
nombre_producto varchar(200) not null,
descripcion_producto varchar(200),
costo_producto decimal(20,2),
fecha_creacion timestamp,
id_proveedor int not null,
id_categoria int not null,
inventario decimal(10,2)
);
alter table producto
add column costo_producto decimal(20,2) not null after descripcion_producto;
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




