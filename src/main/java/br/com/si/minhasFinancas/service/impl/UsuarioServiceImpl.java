package br.com.si.minhasFinancas.service.impl;

import br.com.si.minhasFinancas.exception.AutenticarException;
import br.com.si.minhasFinancas.exception.CustomException;
import br.com.si.minhasFinancas.model.entity.Usuario;
import br.com.si.minhasFinancas.model.enums.TipoLancamento;
import br.com.si.minhasFinancas.model.repository.UsuarioRepository;
import br.com.si.minhasFinancas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Qualifier("usuarioService")
public class UsuarioServiceImpl implements UsuarioService {

    private UsuarioRepository usuarioRepository;

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

    @Override
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getSaldo(Long idusuario) {

        usuarioRepository.findById(idusuario).orElseThrow(
            () -> new CustomException("Não foi possivel localizar o usuario com o id: " + idusuario )
        );

        BigDecimal receita = usuarioRepository.obterSaldoPorUsuario(idusuario, TipoLancamento.RECEITA);
        BigDecimal despesas = usuarioRepository.obterSaldoPorUsuario(idusuario, TipoLancamento.DESPESA);

        if(receita == null) receita = BigDecimal.ZERO;

        if(despesas == null) despesas = BigDecimal.ZERO;

        return receita.subtract(despesas);
    }
}
