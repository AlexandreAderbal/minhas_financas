package br.com.si.minhasFinancas.service;

import br.com.si.minhasFinancas.exception.AutenticarException;
import br.com.si.minhasFinancas.exception.CustomException;
import br.com.si.minhasFinancas.model.entity.Usuario;
import br.com.si.minhasFinancas.model.repository.UsuarioRepository;
import br.com.si.minhasFinancas.service.impl.UsuarioServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test"})
public class UsuarioServiceTest {

    @SpyBean
    private UsuarioServiceImpl usuarioService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @Test
    public void deveSalvarUsuario(){

        Mockito.doNothing().when(usuarioService).validate(Mockito.any());

        Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(getUsuario());

        Usuario usuario = usuarioService.salvar(new Usuario());

        Assertions.assertThat(usuario).isNotNull();
        Assertions.assertThat(usuario.getId()).isEqualTo(1l);
        Assertions.assertThat(usuario.getNome()).isEqualTo("Alexandre");
        Assertions.assertThat(usuario.getEmail()).isEqualTo("alexandre@gmail.com.br");
        Assertions.assertThat(usuario.getSenha()).isEqualTo("123456");

    }

    @Test
    public void deveSalvarUsuarioError(){

        Usuario usuario = getUsuario();

        Mockito.doThrow(CustomException.class).when(usuarioService).validate(usuario);

        usuarioService.salvar(new Usuario());

        Mockito.verify(usuarioRepository,Mockito.never()).save(usuario);

    }

    @Test()
    public void validaExisteEmail(){

        Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(false);

        this.usuarioService.existsByEmail("alexandre@gmail.com");

    }

    @Test
    public void validateErrorExisteEmail(){

        Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(true);

        this.usuarioService.existsByEmail("alexandre@gmail.com.br");

    }

    @Test
    public void deveAutenticarUmUsuarioComSucesso(){

        //cenario
        Usuario usuario = getUsuario();
        Mockito.when(usuarioRepository.findByEmail("alexandre@gmail.com.br")).thenReturn(Optional.of(usuario));

        //ação
        Usuario u = usuarioService.autenticar("alexandre@gmail.com.br","123456");

        //validação
        Assertions.assertThat(u).isNotNull();

    }

    @Test
    public void deveraRetornarErrorDeAtenticacaoDeEmailNaoEncontrado(){

        Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> {
            this.usuarioService.autenticar("alexandre@gmail.com","123456");
        }).isInstanceOf(AutenticarException.class)
                .hasMessage("Usuário não foi encontrado para o email: alexandre@gmail.com!");
    }

    @Test
    public void deveraRetornarErrorDeAtenticacaoSenhaInvalida(){

        Mockito.when(usuarioRepository.findByEmail("alexandre@gmail.com")).thenReturn(Optional.of(getUsuario()));

        Assertions.assertThatThrownBy(() -> {
            this.usuarioService.autenticar("alexandre@gmail.com","123");
        }).isInstanceOf(AutenticarException.class)
                .hasMessage("A senha informada e inválida!");
    }

    public static Usuario getUsuario(){
        return  Usuario.builder()
                .id(1l)
                .nome("Alexandre")
                .email("alexandre@gmail.com.br")
                .senha("123456").build();

    }

}
