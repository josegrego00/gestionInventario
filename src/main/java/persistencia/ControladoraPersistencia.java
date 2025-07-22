/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import logica.Categoria;
import logica.Producto;
import logica.Proveedor;

/**
 *
 * @author josepino
 */
public class ControladoraPersistencia {
    
    private CategoriaJpaController categoriaJpaController;
    private ProveedorJpaController proveedorJpaController;
    private ProductoJpaController productoJpaController;

    public ControladoraPersistencia() {
        
        categoriaJpaController= new CategoriaJpaController();
        proveedorJpaController= new ProveedorJpaController();
        productoJpaController= new ProductoJpaController();
        
    }

    
    
    
    //------------------------------Persistencia de Categoria -------------------------------
    
    public Categoria buscarCategoriaPorId(int categoriaId) {
        return categoriaJpaController.findCategoria(categoriaId);
    }

    
    
    
    //-------------------------------Persistencia de Proveedor--------------------------------
    public Proveedor buscarProveedorPorId(int proveedorId) {
        return proveedorJpaController.findProveedor(proveedorId);
    }

    
    //--------------------------------PErsistencia para PRoductos------------------------------
    
    public void guardarProductoNuevo(Producto producto) {
        productoJpaController.create(producto);
    }
    
}
