package br.com.si.minhasFinancas.service.impl;

import br.com.si.minhasFinancas.exception.AutenticarException;
import br.com.si.minhasFinancas.exception.CustomException;
import br.com.si.minhasFinancas.model.entity.Usuario;
import br.com.si.minhasFinancas.model.repository.UsuarioRepository;
import br.com.si.minhasFinancas.service.UsuarioService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        super();
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario autenticar(String email, String senha) {

        Optional<Usuario> u = this.usuarioRepository.findByEmail(email);

        if(!u.isPresent()){
            throw new AutenticarException("Usuário não foi encontrado para o email: " + email + "!");
        }

        if(!u.get().getSenha().equals(senha)){
            throw new AutenticarException("A senha informada e inválida!");
        }

        return u.get();
    }

    @Override
    public Usuario salvar(Usuario usuario) {

        this.validate(usuario);

        return this.usuarioRepository.save(usuario);
    }

    @Override
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    @Override
    public void validate(Usuario usuario) {

        if(this.existsByEmail(usuario.getEmail())){
            throw new CustomException("O Email " + usuario.getEmail() + " já foi informado!");
        }

    }
}
