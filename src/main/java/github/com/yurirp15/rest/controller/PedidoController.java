package github.com.yurirp15.rest.controller;

import github.com.yurirp15.domain.entity.ItemPedido;
import github.com.yurirp15.domain.entity.Pedido;
import github.com.yurirp15.domain.enums.StatusPedido;
import github.com.yurirp15.rest.dto.AtualizacaoStatusPedidoDTO;
import github.com.yurirp15.rest.dto.InformacaoItemPedidoDTO;
import github.com.yurirp15.rest.dto.InformacoesPedidoDTO;
import github.com.yurirp15.rest.dto.PedidoDTO;
import github.com.yurirp15.service.PedidoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    private static final Logger LOG = LoggerFactory.getLogger(PedidoController.class);

    private PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }


    @PostMapping
    @ResponseStatus(CREATED)
    public Integer save(@RequestBody @Valid PedidoDTO dto) {
        Pedido pedido = service.salvar(dto);
        return pedido.getId();
    }
    @GetMapping("{id}")
    public InformacoesPedidoDTO getByID(@PathVariable Integer id){
        return service
                .obterPedidoCompleto(id)
                .map( p -> converter(p))
                .orElseThrow(() ->
                        new ResponseStatusException(NOT_FOUND, "Pedido não encontrado."));
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@PathVariable Integer id,
                             @RequestBody AtualizacaoStatusPedidoDTO dto) {
        String novoStatus = dto.getNovoStatus();
        service.atualizaStatus(id, StatusPedido.valueOf(novoStatus));
        LOG.info(novoStatus);
    }

    private InformacoesPedidoDTO converter(Pedido pedido) {
        return InformacoesPedidoDTO
                .builder()
                .codigo(pedido.getId())
                .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .cpf(pedido.getCliente().getCpf())
                .nomeCliente(pedido.getCliente().getNome())
                .total(pedido.getTotal())
                .status(pedido.getStatus().name())
                .items(converter(pedido.getItens()))
                .build();
    }

    private List<InformacaoItemPedidoDTO> converter(List<ItemPedido> itens) {
        if(CollectionUtils.isEmpty(itens)) {
            return Collections.emptyList();
        }
        return itens.stream().map( item -> InformacaoItemPedidoDTO
                                               .builder().descricaoProduto(item.getProduto().getDescricao())
                                               .precoUnitario(item.getProduto().getPreco())
                                               .quantidade(item.getQuantidade())
                                               .build()
        ).collect(Collectors.toList());
    }

}
