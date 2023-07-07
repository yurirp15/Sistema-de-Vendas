package github.com.yurirp15.rest.controller;

import github.com.yurirp15.domain.entity.Usuario;
import github.com.yurirp15.exception.SenhaInvalidaExcpetion;
import github.com.yurirp15.rest.dto.CredenciaisDTO;
import github.com.yurirp15.rest.dto.TokenDTO;
import github.com.yurirp15.security.jwt.JwtService;
import github.com.yurirp15.service.impl.UsuarioServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioServiceImpl usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario salvar(@RequestBody @Valid Usuario usuario) {
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        return usuarioService.salvar(usuario);
    }

    @PostMapping("/auth")
    public TokenDTO autenticar( @RequestBody CredenciaisDTO credenciais){
        try{
            Usuario usuario = Usuario.builder ()
                    .login (credenciais.getLogin ())
                    .senha (credenciais.getSenha ()).build ();
            UserDetails usuarioAutenticado = usuarioService.autenticar (usuario);
            String token = jwtService.gerarToken (usuario);
            return new TokenDTO (usuario.getLogin (), token);
        }catch (UsernameNotFoundException | SenhaInvalidaExcpetion e){
            throw new ResponseStatusException (HttpStatus.UNAUTHORIZED, e.getMessage ());
        }

    }
}