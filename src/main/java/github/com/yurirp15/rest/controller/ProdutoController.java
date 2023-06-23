package github.com.yurirp15.rest.controller;


import github.com.yurirp15.domain.entity.Cliente;
import github.com.yurirp15.domain.entity.Produto;
import github.com.yurirp15.domain.repository.ProdutosJpa;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {


    private ProdutosJpa produtosJpa;

    public ProdutoController(ProdutosJpa produtosJpa) {
        this.produtosJpa = produtosJpa;
    }

    @GetMapping("{id}")
    public Produto getProdutoByID(@PathVariable Integer id) {
        return produtosJpa
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(NOT_FOUND,
                                "produto não encontrado"));
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Produto save(@RequestBody Produto produto) {
        return produtosJpa.save(produto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        produtosJpa.findById(id)
                .map(p -> {
                    produtosJpa.delete(p);
                    return Void.TYPE;
                })
                .orElseThrow(() ->
                        new ResponseStatusException(NOT_FOUND,
                                "produto não encontrado"));
    }

    @PutMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void update(@PathVariable Integer id,
                       @RequestBody Produto produto) {
        produtosJpa
                .findById(id)
                .map(p -> {
                    produto.setId(p.getId());
                    produtosJpa.save(produto);
                    produtosJpa.save(produto);
                    return produto;
                })
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND,
                        "produto não encontrado"));
    }



   @GetMapping
    public List<Produto> find (Produto filtro ){
        ExampleMatcher matcher = ExampleMatcher
                                    .matching()
                                    .withIgnoreCase()
                                    .withStringMatcher( ExampleMatcher.StringMatcher.CONTAINING );


        Example example = Example.of(filtro, matcher);
        return produtosJpa.findAll(example);
    }


}
