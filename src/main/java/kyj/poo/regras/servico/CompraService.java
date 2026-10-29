package kyj.poo.regras.servico;

import java.util.ArrayList;
import java.util.List;
import kyj.poo.persistencia.CompraRepositorio;
import kyj.poo.regras.Comprador;
import kyj.poo.regras.carrinho.Carrinho;
import kyj.poo.regras.carrinho.ItemDoCarrinho;
import kyj.poo.regras.compra.Compra;
import kyj.poo.regras.compra.ItemDaCompra;
import kyj.poo.regras.excecao.EstoqueInsuficienteException;
import kyj.poo.regras.excecao.PagamentoRecusadoException;
import kyj.poo.regras.excecao.PersistenciaException;
import kyj.poo.regras.excecao.QuantidadeInvalidaException;
import kyj.poo.regras.pagamento.FormaPagamento;

public class CompraService {

    private final ProdutoService produtoService;
    private final List<Compra> compras;
    private final CompraRepositorio repositorio;

    public CompraService(
        ProdutoService produtoService,
        CompraRepositorio repositorio
    ) throws PersistenciaException {
        this.produtoService = produtoService;
        this.repositorio = repositorio;
        this.compras = new ArrayList<>(repositorio.carregar());
    }

    public Compra finalizarCompra(
        Comprador comprador,
        FormaPagamento formaPagamento
    )
        throws QuantidadeInvalidaException, PagamentoRecusadoException, EstoqueInsuficienteException, PersistenciaException {
        Carrinho carrinho = comprador.getCarrinho();
        List<ItemDoCarrinho> itens = carrinho.getItens();

        if (itens == null) throw new QuantidadeInvalidaException(
            "O carrinho está vazio."
        );

        for (ItemDoCarrinho item : itens) {
            if (!item.getProduto().verificarEstoque(item.getQtd())) {
                throw new EstoqueInsuficienteException(
                    item.getProduto().getNome()
                );
            }
        }

        Compra compra = new Compra(comprador.getNome());

        for (ItemDoCarrinho item : itens) {
            compra.addItem(ItemDaCompra.de(item));
        }

        compra.fechar(formaPagamento);

        if (!formaPagamento.processar(compra.calcularTotal())) {
            throw new PagamentoRecusadoException();
        }

        for (ItemDoCarrinho item : itens) {
            item.getProduto().diminuirEstoque(item.getQtd());
        }

        this.compras.add(compra);

        try {
            this.produtoService.salvar();
            this.repositorio.salvar(compras);
        } catch (PersistenciaException e) {
            this.compras.remove(compra);

            for (ItemDoCarrinho item : itens) {
                item.getProduto().setEstoque(
                    item.getProduto().getEstoque() + item.getQtd()
                );
            }

            try {
                this.produtoService.salvar();
            } catch (PersistenciaException falhaAoDesfazer) {
                e.addSuppressed(falhaAoDesfazer);
            }

            throw e;
        }

        comprador.addCompra(compra);
        carrinho.esvaziarCarrinho();
        return compra;
    }

    public List<Compra> historico(String compradorNome) {
        List<Compra> resultado = new ArrayList<>();

        for (Compra compra : this.compras) {
            if (
                compra.getCompradorNome().equalsIgnoreCase(compradorNome.trim())
            ) {
                resultado.add(compra);
            }
        }

        return resultado;
    }

    public long totalGasto(String compradorNome) {
        long total = 0;

        for (Compra compra : historico(compradorNome)) {
            if (!compra.isCancelada()) {
                total += compra.getValorFinal();
            }
        }

        return total;
    }
}
