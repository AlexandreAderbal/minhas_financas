package br.com.si.minhasFinancas.model.repository;

import br.com.si.minhasFinancas.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    boolean existsByEmail(String email);

    Optional<Usuario> findByEmail(String email);

}
