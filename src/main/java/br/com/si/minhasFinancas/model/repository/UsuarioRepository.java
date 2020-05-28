package br.com.si.minhasFinancas.model.repository;

import br.com.si.minhasFinancas.model.entity.Usuario;
import br.com.si.minhasFinancas.model.enums.TipoLancamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    boolean existsByEmail(String email);

    Optional<Usuario> findByEmail(String email);

    @Query(
            value = "select sum(l.valor) from Lancamento l join l.usuario u " +
                    "where u.id = :id and l.tipoLancamento = :tipo "
    )
    BigDecimal obterSaldoPorUsuario(@Param("id") Long idusuario,@Param("tipo") TipoLancamento tipo);
}
