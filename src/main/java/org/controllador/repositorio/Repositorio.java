package org.controllador.repositorio;

import java.util.List;

public interface Repositorio<T> {
    void crear(T t);
    T buscarPorID(int i);
    List<T> listar();
    void actualizar(T t);
    void eliminarPorID(int i);

}
