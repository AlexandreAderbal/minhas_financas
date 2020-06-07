package br.com.si.minhasFinancas.model.repository;

import br.com.si.minhasFinancas.model.entity.Lancamento;
import br.com.si.minhasFinancas.model.entity.Usuario;
import br.com.si.minhasFinancas.model.enums.StatusLancamento;
import br.com.si.minhasFinancas.model.enums.TipoLancamento;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test"})
public class LancamentoRepositoryTest {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void deveSalvarLancamento(){

        Lancamento lancamento = getLancamento();

        lancamento = lancamentoRepository.save(lancamento);

        Assertions.assertThat(lancamento).isNotNull();

    }

    @Test
    public void deveDeletarUmUsuario(){

        Lancamento lancamento = getLancamento();

        lancamento = testEntityManager.persist(lancamento);

        lancamento = testEntityManager.find(Lancamento.class,lancamento.getId());

        lancamentoRepository.delete(lancamento);

        Lancamento lancamentoDeletado = testEntityManager.find(Lancamento.class,lancamento.getId());

        Assertions.assertThat(lancamentoDeletado).isNull();
    }

    public static Lancamento getLancamento(){
        return  Lancamento.builder()
                            .statusLancamento(StatusLancamento.PENDENTE)
                            .tipoLancamento(TipoLancamento.DESPESA)
                            .ano(2020)
                            .mes(10)
                            .descricao("teste")
                            .usuario(
                                    Usuario.builder()
                                            .email("alexandre.aderbal@gmail.com")
                                            .nome("Alexandre")
                                            .senha("123456").build()
                            ).valor(BigDecimal.valueOf(200))
                            .dataCadastro(LocalDate.now()).build();
    }


}
