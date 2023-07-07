package github.com.yurirp15.rest.controller;

import github.com.yurirp15.domain.entity.Cliente;
import github.com.yurirp15.domain.repository.Clientes;
import io.swagger.annotations.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/clientes")
@Api("Api Clientes")
public class ClienteController {


    private Clientes clientes;

    public ClienteController(Clientes clientes) {
        this.clientes = clientes;
    }

    @GetMapping("{id}")
    @ApiOperation ("Obter detalhes de um cliente")
    @ApiResponses ({
            @ApiResponse (code = 200, message = "Cliente encontrado."),
            @ApiResponse (code = 404, message = "Cliente não encontrado para o id informado")
    })
    public Cliente getClienteById(
            @PathVariable
            @ApiParam("Id do cliente") Integer id) {
        return clientes
                .findById(id)
                .orElseThrow( () ->
                        new ResponseStatusException( HttpStatus.NOT_FOUND,
                                "cliente não encontrado"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation ("Salva um novo cliente")
    @ApiResponses ({
            @ApiResponse (code = 201, message = "Cliente salvo com sucesso."),
            @ApiResponse (code = 400, message = "Erro de validação.")
    })
    public Cliente save(@RequestBody @Valid Cliente cliente) {
        return clientes.save(cliente);
    }
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update( @PathVariable Integer id,
                        @RequestBody @Valid Cliente cliente ) {
        clientes
                .findById(id)
                .map(clienteExistente -> {
                    cliente.setId(clienteExistente.getId());
                    clientes.save(cliente);
                    return cliente;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "cliente não encontrado"));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        clientes.findById(id)
                .map( cliente -> {
                    clientes.delete(cliente );
                return cliente;
                })
                .orElseThrow(() ->new ResponseStatusException( HttpStatus.NOT_FOUND,
                        "cliente não encontrado"));

    }

    @GetMapping
    public List<Cliente> find ( Cliente filtro ){
        ExampleMatcher matcher = ExampleMatcher
                                    .matching()
                                    .withIgnoreCase()
                                    .withStringMatcher( ExampleMatcher.StringMatcher.CONTAINING );

        Example example = Example.of(filtro, matcher);
        return clientes.findAll(example);

    }

}
