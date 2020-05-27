package br.com.si.minhasFinancas.model.repository;

import br.com.si.minhasFinancas.model.entity.Usuario;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test"})
public class UsuarioRepositoryTeste {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void deveVerificarExistenciaDeUmEmail(){

        Usuario usuario = getUsuario();

        this.testEntityManager.persist(usuario);

        boolean result = this.usuarioRepository.existsByEmail("alexandre@gmail.com");

        Assertions.assertThat(result).isTrue();

    }

    @Test
    public void deveSalvarUmUsuario(){

        Usuario u = getUsuario();

        Usuario usuario = this.usuarioRepository.save(u);

        Assertions.assertThat(usuario.getId()).isNotNull();

    }

    @Test
    public void buscaUsuarioPeloEmail(){

        Usuario u = getUsuario();

        this.testEntityManager.persist(u);

        Optional<Usuario> usuario = this.usuarioRepository.findByEmail("alexandre@gmail.com");

       Assertions.assertThat(usuario.isPresent()).isTrue();
    }

    @Test
    public void buscaUsuarioQueNaoExistePeloEmail(){

        Optional<Usuario> usuario = this.usuarioRepository.findByEmail("alexandre@gmail.com");

        Assertions.assertThat(usuario.isPresent()).isFalse();
    }


    public static Usuario getUsuario(){
        return Usuario.builder()
                .nome("Alexandre Aderbal Dias")
                .email("alexandre@gmail.com")
                .senha("123456")
                .build();
    }

}
