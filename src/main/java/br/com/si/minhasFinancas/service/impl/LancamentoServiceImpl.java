package br.com.si.minhasFinancas.service.impl;

import br.com.si.minhasFinancas.exception.CustomException;
import br.com.si.minhasFinancas.model.entity.Lancamento;
import br.com.si.minhasFinancas.model.enums.StatusLancamento;
import br.com.si.minhasFinancas.model.repository.LancamentoRepository;
import br.com.si.minhasFinancas.service.LancamentoService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class LancamentoServiceImpl implements LancamentoService {

    private LancamentoRepository repository;

    public LancamentoServiceImpl(LancamentoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Lancamento salvar(Lancamento lancamento) {
        this.validar(lancamento);
        lancamento.setStatusLancamento(StatusLancamento.PENDENTE);
        return repository.save(lancamento);
    }

    @Override
    @Transactional
    public Lancamento atualizar(Lancamento lancamento) {
        Objects.requireNonNull(lancamento);
        return repository.save(lancamento);
    }

    @Override
    @Transactional
    public void deletar(Lancamento lancamento) {
        Objects.requireNonNull(lancamento.getId());
        this.validar(lancamento);
        repository.delete(lancamento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lancamento> buscar(Lancamento lancamento) {
        Example example = Example.of(lancamento,
                                    ExampleMatcher.matching()
                                                .withIgnoreCase()
                                                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
        return repository.findAll(example);
    }

    @Override
    @Transactional
    public void atualizarStatus(Lancamento lancamento, StatusLancamento statusLancamento) {
        lancamento.setStatusLancamento(statusLancamento);
        repository.save(lancamento);
    }

    @Override
    public void validar(Lancamento lancamento) {

        if(lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")){
            throw new CustomException("Informe uma descrição válida!");
        }

        if(lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12){
            throw new CustomException("Informe uma mes válida!");
        }

        if(lancamento.getAno() == null || lancamento.getAno().toString().length() != 4){
            throw new CustomException("Informe uma ano válida!");
        }

        if(lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null){
            throw new CustomException("Informe uma usuario!");
        }

        if(lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1){
            throw new CustomException("Informe uma valor válido!");
        }

        if(lancamento.getTipoLancamento() == null){
            throw new CustomException("Informe uma tipo de lancamento!");
        }
    }
}
