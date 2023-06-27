package github.com.yurirp15.service.impl;

import github.com.yurirp15.domain.entity.Cliente;
import github.com.yurirp15.domain.entity.ItemPedido;
import github.com.yurirp15.domain.entity.Pedido;
import github.com.yurirp15.domain.entity.Produto;
import github.com.yurirp15.domain.enums.StatusPedido;
import github.com.yurirp15.domain.repository.Clientes;
import github.com.yurirp15.domain.repository.ItemsPedido;
import github.com.yurirp15.domain.repository.PedidosJpa;
import github.com.yurirp15.domain.repository.Produtos;
import github.com.yurirp15.exception.PedidoNaoEncontradoExcpetion;
import github.com.yurirp15.exception.RegraNegocioExcpetion;
import github.com.yurirp15.rest.dto.ItemPedidoDTO;
import github.com.yurirp15.rest.dto.PedidoDTO;
import github.com.yurirp15.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidosJpa repository;
    private final Clientes clientesRepository;
    private final Produtos produtosRepository;
    private final ItemsPedido itemsPedidoRepository;

    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {
        Integer idCliente = dto.getCliente();
        Cliente cliente = clientesRepository
                .findById(idCliente)
                .orElseThrow(() ->new RegraNegocioExcpetion("Código de cliente inválido"));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itemsPedidos = converterItems(pedido, dto.getItems());
        repository.save(pedido);
        itemsPedidoRepository.saveAll(itemsPedidos);
        pedido.setItens(itemsPedidos);
        return pedido;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return repository.findByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void atualizaStatus(Integer id, StatusPedido statusPedido ) {
        repository
                .findById(id)
                .map( pedido -> {
                    pedido.setStatus(statusPedido);
                    return repository.save(pedido);
                } ).orElseThrow(() -> new PedidoNaoEncontradoExcpetion());
    }

    private List<ItemPedido> converterItems( Pedido pedido, List<ItemPedidoDTO> items) {
        if (items.isEmpty()) {
            throw new RegraNegocioExcpetion("Não é possível realizar um pedido sem items.");
        }

        return items
                .stream()
                .map( dto -> {
                    Integer idProduto = dto.getProduto();
                   Produto produto = produtosRepository
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