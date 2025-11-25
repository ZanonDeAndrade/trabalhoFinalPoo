import java.time.LocalDate;

public class LancamentoFinanceiro {
    public enum TipoLancamento {
        RECEITA,
        DESPESA
    }

    private TipoLancamento tipo;
    private double valor;
    private String categoria;
    private LocalDate data;

    public LancamentoFinanceiro(TipoLancamento tipo, double valor, String categoria, LocalDate data) {
        this.tipo = tipo;
        this.valor = valor;
        this.categoria = categoria;
        this.data = data;
    }

    public TipoLancamento getTipo() {
        return tipo;
    }

    public void setTipo(TipoLancamento tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}
