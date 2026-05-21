package com.Eric.ventaeventos.repository;

import com.Eric.ventaeventos.model.Usuario;
import java.util.List;

/*
 * IUsuarioRepository
 *
 * Misma idea que IEventoRepository pero para usuarios.
 * Separamos las interfaces por entidad para cumplir ISP:
 * una clase que solo necesita buscar eventos no tiene por qué
 * conocer los métodos de usuarios.
 */
public interface IUsuarioRepository {
    List<Usuario> obtenerUsuarios();
    Usuario buscarUsuarioPorId(String id);
    void agregar(Usuario usuario);
}