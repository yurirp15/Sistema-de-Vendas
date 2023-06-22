package github.com.yurirp15.domain.repository;

import github.com.yurirp15.domain.entity.Cliente;
import github.com.yurirp15.domain.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Pedidos extends JpaRepository<Pedido, Integer> {
    List<Pedido> findByCliente(Cliente cliente);
}
