package br.com.si.minhasFinancas.service;

import br.com.si.minhasFinancas.exception.AutenticarException;
import br.com.si.minhasFinancas.exception.CustomException;
import br.com.si.minhasFinancas.model.entity.Lancamento;
import br.com.si.minhasFinancas.model.entity.Usuario;
import br.com.si.minhasFinancas.model.enums.StatusLancamento;
import br.com.si.minhasFinancas.model.repository.LancamentoRepository;
import br.com.si.minhasFinancas.model.repository.LancamentoRepositoryTest;
import br.com.si.minhasFinancas.service.impl.LancamentoServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test"})
public class LancamentoServiceTest {

    @SpyBean
    private LancamentoServiceImpl lancamentoService;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private LancamentoRepository lancamentoRepository;

    @Test
    public void deveSalvarOLancamento(){

        Lancamento lancamentoASalvar = LancamentoRepositoryTest.getLancamento();
        Mockito.doNothing().when(lancamentoService).validar(lancamentoASalvar);

        Lancamento lancamentoSalvo = LancamentoRepositoryTest.getLancamento();
        lancamentoSalvo.setId(1L);
        Mockito.when(lancamentoRepository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);

        Lancamento lancamento = lancamentoService.salvar(lancamentoASalvar);

        Assertions.assertThat(lancamento.getId()).isNotNull();
        Assertions.assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
        Assertions.assertThat(lancamento.getStatusLancamento()).isEqualTo(StatusLancamento.PENDENTE);


    }

    @Test
    public void deveLancarExeptionSalvarOLancamento(){

        Lancamento lancamento = LancamentoRepositoryTest.getLancamento();

        Mockito.doThrow(CustomException.class).when(lancamentoService).validar(lancamento);

        Assertions.catchThrowableOfType(
                () ->  lancamentoService.salvar(lancamento),CustomException.class);

        Mockito.verify(lancamentoRepository,Mockito.never()).save(lancamento);


    }

    @Test
    public void deveAtualizarLancamento(){

        Lancamento lancamentoSalvo = LancamentoRepositoryTest.getLancamento();
        lancamentoSalvo.setId(1L);

        Mockito.doNothing().when(lancamentoService).validar(lancamentoSalvo);

        Mockito.when(lancamentoRepository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);

        lancamentoService.atualizar(lancamentoSalvo);

        Mockito.verify(lancamentoRepository,Mockito.times(1)).save(lancamentoSalvo);

    }

    @Test
    public void deveLancarExeptionAtualizarLancamento(){

        Lancamento lancamento = LancamentoRepositoryTest.getLancamento();

        Assertions.catchThrowableOfType(
                () ->  lancamentoService.atualizar(lancamento),NullPointerException.class);

        Mockito.verify(lancamentoRepository,Mockito.never()).save(lancamento);


    }

    @Test
    public void deveDeletarLancamento(){

        Lancamento lancamento = LancamentoRepositoryTest.getLancamento();
        lancamento.setId(1l);

        Mockito.when(lancamentoRepository.findById(lancamento.getId())).thenReturn(Optional.of(lancamento));

        lancamentoService.deletar(lancamento);

        Mockito.verify(lancamentoRepository).delete(lancamento);


    }

    @Test
    public void deveLancarExceptionDeletarLancamento(){

        Lancamento lancamento = LancamentoRepositoryTest.getLancamento();

        Assertions.catchThrowableOfType(
                () ->  lancamentoService.deletar(lancamento),NullPointerException.class);

        Mockito.verify(lancamentoService).deletar(lancamento);


    }

    @Test
    public void deveFiltrarLancamento(){

        Lancamento lancamento = LancamentoRepositoryTest.getLancamento();
        lancamento.setId(1l);

        List<Lancamento> list = Arrays.asList(lancamento);
        Mockito.when(lancamentoRepository.findAll(Mockito.any(Example.class))).thenReturn(list);

        List<Lancamento> result = lancamentoService.buscar(lancamento);

        Assertions.assertThat(result).isNotEmpty().hasSize(1).contains(lancamento);


    }

    @Test
    public void deveAtualizarStatusLancamento(){

        Lancamento lancamento = LancamentoRepositoryTest.getLancamento();
        lancamento.setId(1L);

        StatusLancamento novoStatus = StatusLancamento.CANCELADO;

        Mockito.doReturn(lancamento).when(lancamentoService).atualizar(Mockito.any());
        Mockito.when(lancamentoRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(lancamento));

        lancamentoService.atualizarStatus(lancamento.getId(),novoStatus);

        Assertions.assertThat(lancamento.getStatusLancamento()).isEqualTo(novoStatus);
        Mockito.verify(lancamentoRepository).save(lancamento);

    }

    @Test void deveRetornarUmOptionalLancamento(){
        Lancamento lancamento = LancamentoRepositoryTest.getLancamento();
        lancamento.setId(1L);

        Mockito.when(lancamentoRepository.findById(1L)).thenReturn(Optional.of(lancamento));

        Optional<Lancamento> resultado = lancamentoService.findById(1L);

        Assertions.assertThat(resultado.isPresent()).isTrue();
    }

    @Test
    public void deveRetornarUmOptionalVazio(){

        Mockito.when(lancamentoRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        Optional<Lancamento> resultado = lancamentoService.findById(1L);

        Assertions.assertThat(resultado.isPresent()).isFalse();
    }

    @Test
    public void testeDeValidate(){

        Lancamento lancamento = new Lancamento();
        lancamento.setId(1L);

        Assertions.assertThatThrownBy(
                () -> { lancamentoService.validar(lancamento);
                }).isInstanceOf(CustomException.class)
                .hasMessage("Não foi encontrado um lançamento com o id: " + lancamento.getId());

        lancamento.setId(null);

        Assertions.assertThatThrownBy(
                () -> { lancamentoService.validar(lancamento);
                }).isInstanceOf(CustomException.class)
                .hasMessage("Informe uma descrição válida!");

        lancamento.setDescricao("");

        Assertions.assertThatThrownBy(
                () -> { lancamentoService.validar(lancamento);
                }).isInstanceOf(CustomException.class)
                .hasMessage("Informe uma descrição válida!");

        lancamento.setDescricao("teste");

        Assertions.assertThatThrownBy(
                () -> { lancamentoService.validar(lancamento);
                }).isInstanceOf(CustomException.class)
                .hasMessage("Informe uma mes válida!");

        lancamento.setMes(13);

        Assertions.assertThatThrownBy(
                () -> { lancamentoService.validar(lancamento);
                }).isInstanceOf(CustomException.class)
                .hasMessage("Informe uma mes válida!");

        lancamento.setMes(1);

        Assertions.assertThatThrownBy(
                () -> { lancamentoService.validar(lancamento);
                }).isInstanceOf(CustomException.class)
                .hasMessage("Informe uma ano válida!");

        lancamento.setAno(202);

        Assertions.assertThatThrownBy(
                () -> { lancamentoService.validar(lancamento);
                }).isInstanceOf(CustomException.class)
                .hasMessage("Informe uma ano válida!");

        lancamento.setAno(2020);

        Assertions.assertThatThrownBy(
                () -> { lancamentoService.validar(lancamento);
                }).isInstanceOf(CustomException.class)
                .hasMessage("Informe uma usuario!");

        lancamento.setUsuario(new Usuario());
        lancamento.getUsuario().setId(1L);

        Assertions.assertThatThrownBy(
                () -> { lancamentoService.validar(lancamento);
                }).isInstanceOf(CustomException.class)
                .hasMessage("Não foi encontrado o usaurio com o id: " + lancamento.getUsuario().getId());

        Mockito.when(usuarioService.findById(Mockito.any())).thenReturn(Optional.of(lancamento.getUsuario()));

        Assertions.assertThatThrownBy(
                () -> { lancamentoService.validar(lancamento);}).isInstanceOf(CustomException.class)
                .hasMessage("Informe uma valor válido!");

        lancamento.setValor(BigDecimal.valueOf(200));

        Assertions.assertThatThrownBy(
                () -> { lancamentoService.validar(lancamento);}).isInstanceOf(CustomException.class)
                .hasMessage("Informe uma tipo de lancamento!");

    }

}
