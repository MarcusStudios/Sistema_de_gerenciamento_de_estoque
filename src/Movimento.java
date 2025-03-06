import java.io.Serializable;
import java.time.LocalDateTime;

public class Movimento implements Serializable {
    private static final long serialVersionUID = 1L;

    private int produtoId;
    private String tipo; // ENTRADA, VENDA, DESCARTE, TRANSFERÊNCIA
    private int quantidade;
    private String observacao;
    private LocalDateTime dataHora;

    public Movimento(int produtoId, String tipo, int quantidade, String observacao, LocalDateTime dataHora) {
        this.produtoId = produtoId;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.observacao = observacao;
        this.dataHora = dataHora;
    }

    // Getters
    public int getProdutoId() {
        return produtoId;
    }

    public String getTipo() {
        return tipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public String getObservacao() {
        return observacao;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }
}