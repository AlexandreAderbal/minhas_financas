package br.com.si.minhasFinancas.api.controller;

import br.com.si.minhasFinancas.api.dto.UsuarioDTO;
import br.com.si.minhasFinancas.exception.AutenticarException;
import br.com.si.minhasFinancas.exception.CustomException;
import br.com.si.minhasFinancas.model.entity.Usuario;
import br.com.si.minhasFinancas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping(value = "/autenticar")
    public ResponseEntity autenticar(@RequestBody UsuarioDTO usuarioDTO){

        try{
            Usuario usuario = usuarioService.autenticar(usuarioDTO.getEmail(),usuarioDTO.getSenha());
            return new ResponseEntity(usuario,HttpStatus.CREATED);
        }catch (AutenticarException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping
    public ResponseEntity save(@RequestBody UsuarioDTO usuarioDTO){

        Usuario usuario = Usuario.builder()
                                .id(usuarioDTO.getId())
                                .nome(usuarioDTO.getNome())
                                .email(usuarioDTO.getEmail())
                                .senha(usuarioDTO.getSenha()).build();

        try{
            return new ResponseEntity<Usuario>(usuarioService.salvar(usuario), HttpStatus.CREATED);
        }catch (CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("{id}/saldo")
    public ResponseEntity getSaldo(@PathVariable Long id){

        try{
            return ResponseEntity.ok(usuarioService.getSaldo(id));
        }catch (CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
