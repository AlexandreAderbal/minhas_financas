package br.com.si.minhasFinancas.api.controller;

import br.com.si.minhasFinancas.api.dto.LancamentoDTO;
import br.com.si.minhasFinancas.api.dto.StatusDTO;
import br.com.si.minhasFinancas.exception.AutenticarException;
import br.com.si.minhasFinancas.exception.CustomException;
import br.com.si.minhasFinancas.model.entity.Lancamento;
import br.com.si.minhasFinancas.model.entity.Usuario;
import br.com.si.minhasFinancas.model.enums.StatusLancamento;
import br.com.si.minhasFinancas.model.enums.TipoLancamento;
import br.com.si.minhasFinancas.service.LancamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoController {

    private final LancamentoService lancamentoService;

    @PostMapping
    public ResponseEntity salvar(@RequestBody LancamentoDTO dto){
        try{
            Lancamento lancamento = lancamentoService.salvar(this.converter(dto));
            return new ResponseEntity(lancamento, HttpStatus.CREATED);
        }catch (CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody LancamentoDTO dto){
        try{
            Lancamento lancamento = this.converter(dto);
            lancamento.setId(id);
            return ResponseEntity.ok(lancamentoService.atualizar(lancamento));
        }catch (CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping("{id}")
    public ResponseEntity atualizar(@PathVariable Long id){
        try{
            lancamentoService.deletar(Lancamento.builder().id(id).build());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }catch (CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("{id}/atualizar/status")
    public ResponseEntity atualizarStatus(@PathVariable Long id, @RequestBody StatusDTO dto){
        try{
            return  ResponseEntity.ok(lancamentoService.atualizarStatus(id,StatusLancamento.valueOf(dto.getStatus())));
        }catch (CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping
    public ResponseEntity buscar(
            @RequestParam(value = "descricao", required = false) String descricao,
            @RequestParam(value = "mes", required = false) Integer mes,
            @RequestParam(value = "ano", required = false) Integer ano,
            @RequestParam(value = "usuario") Long idUsuario){


        try{

            Usuario usuario = Usuario.builder().id(idUsuario).build();

            Lancamento filter = Lancamento.builder()
                            .descricao(descricao)
                            .ano(ano)
                            .mes(mes)
                            .usuario(usuario).build();

            return ResponseEntity.ok(lancamentoService.buscar(filter));

        }catch (CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Lancamento converter(LancamentoDTO dto){
        return Lancamento.builder()
                .id(dto.getId())
                .descricao(dto.getDescricao())
                .ano(dto.getAno())
                .mes(dto.getMes())
                .statusLancamento( dto.getStatus() != null ? StatusLancamento.valueOf(dto.getStatus()) : null)
                .tipoLancamento(dto.getTipo() != null ? TipoLancamento.valueOf(dto.getTipo()) : null)
                .valor(dto.getValor())
                .usuario(Usuario.builder().id(dto.getIdUsuario()).build())
                .build();
    }

}
