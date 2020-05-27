package br.com.si.minhasFinancas.api.controller;

import br.com.si.minhasFinancas.api.dto.UsuarioDTO;
import br.com.si.minhasFinancas.exception.AutenticarException;
import br.com.si.minhasFinancas.exception.CustomException;
import br.com.si.minhasFinancas.model.entity.Usuario;
import br.com.si.minhasFinancas.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/usuario")
public class UsuarioController {

    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping(value = "/autenticar")
    public ResponseEntity autenticar(@RequestBody UsuarioDTO usuarioDTO){

        try{
            Usuario usuario = usuarioService.autenticar(usuarioDTO.getEmail(),usuarioDTO.getSenha());
            return ResponseEntity.ok(usuario);
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
}
