package br.com.si.minhasFinancas.service;

import br.com.si.minhasFinancas.model.entity.Lancamento;
import br.com.si.minhasFinancas.model.enums.StatusLancamento;

import java.util.List;
import java.util.Optional;

public interface LancamentoService {

    Lancamento salvar(Lancamento lancamento);

    Lancamento atualizar(Lancamento lancamento);

    void deletar(Lancamento lancamento);

    List<Lancamento> buscar(Lancamento lancamento);

    Lancamento atualizarStatus(Long idLancamento, StatusLancamento statusLancamento);

    void validar(Lancamento lancamento);

    Optional<Lancamento> findById(Long id);
}
