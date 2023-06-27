package github.com.yurirp15.exception;

public class PedidoNaoEncontradoExcpetion extends RuntimeException {
    public PedidoNaoEncontradoExcpetion() {
        super("Pedido não encontrado.");
    }
}
