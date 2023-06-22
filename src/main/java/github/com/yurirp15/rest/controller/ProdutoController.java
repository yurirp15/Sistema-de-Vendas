package github.com.yurirp15.rest.controller;


import github.com.yurirp15.domain.entity.Produto;
import github.com.yurirp15.domain.repository.Produtos;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping
public class ProdutoController {


    private Produtos produtos;

    public ProdutoController(Produtos produtos) {
        this.produtos = produtos;
    }

    @GetMapping("{id}")
    public Produto getProdutoByID(@PathVariable Integer id) {
        return produtos
                .findById(id)
                .orElseThrow( () ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "produto não encontrado"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Produto save(@RequestBody Produto produto) {
        return produtos.save(produto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id){
        produtos.findById(id)
                .map( produto ->{
                    produtos.delete(produto);
                    return produto;
                }  )
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "produto não encontrado"));
    }


}
