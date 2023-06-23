package github.com.yurirp15.service;

import github.com.yurirp15.domain.entity.Pedido;
import github.com.yurirp15.rest.dto.PedidoDTO;

public interface PedidoService {
    Pedido salvar (PedidoDTO dto);
}
