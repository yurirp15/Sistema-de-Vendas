package github.com.yurirp15.service;

import github.com.yurirp15.domain.entity.Pedido;
import github.com.yurirp15.domain.enums.StatusPedido;
import github.com.yurirp15.rest.dto.PedidoDTO;

import java.util.Optional;

public interface PedidoService {
    Pedido salvar (PedidoDTO dto);

    Optional<Pedido> obterPedidoCompleto(Integer id);
    void atualizaStatus(Integer id, StatusPedido statusPedido);
}
