package br.com.si.minhasFinancas.service;

import br.com.si.minhasFinancas.model.entity.Usuario;

public interface UsuarioService {

    Usuario autenticar(String email,String senha);

    Usuario salvar(Usuario usuario);

    boolean existsByEmail(String email);

    void validate(Usuario usuario);
}
