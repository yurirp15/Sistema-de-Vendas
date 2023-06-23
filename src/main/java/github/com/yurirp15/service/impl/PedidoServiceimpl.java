package github.com.yurirp15.service.impl;

import github.com.yurirp15.domain.entity.Cliente;
import github.com.yurirp15.domain.entity.ItemPedido;
import github.com.yurirp15.domain.entity.Pedido;
import github.com.yurirp15.domain.entity.Produto;
import github.com.yurirp15.domain.repository.Clientes;
import github.com.yurirp15.domain.repository.Pedidos;
import github.com.yurirp15.domain.repository.ProdutosJpa;
import github.com.yurirp15.exception.RegraNegocioExcpetion;
import github.com.yurirp15.rest.dto.ItemPedidoDTO;
import github.com.yurirp15.rest.dto.PedidoDTO;
import github.com.yurirp15.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceimpl implements PedidoService {

    private final Pedidos pedidos;
    private final Clientes clientesRepository;
    private final ProdutosJpa produtosJpaRepository;

    @Override
    public Pedido salvar(PedidoDTO dto) {
        Integer idCliente = dto.getCliente();
        Cliente cliente = clientesRepository
                .findById(idCliente)
                .orElseThrow(() ->new RegraNegocioExcpetion("Código de cliente inválido"));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);

        List<ItemPedido> itemsPedidos = converterItems(pedido, dto.getItems());

        return null;
    }

    private List<ItemPedido> converterItems( Pedido pedido ,List<ItemPedidoDTO> items) {
        if (items.isEmpty()) {
            throw new RegraNegocioExcpetion("Não é possível realizar um pedido sem items.");
        }

        return items
                .stream()
                .map( dto -> {
                    Integer idProduto = dto.getProduto();
                   Produto produto = produtosJpaRepository
                            .findById(idProduto)
                            .orElseThrow(
                                    () ->new RegraNegocioExcpetion(
                                            "Código de produto inválido:" + idProduto
                                    ));

                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setQuantidade(dto.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setProduto(produto);
                    return itemPedido;

                }).collect(Collectors.toList());

    }
}