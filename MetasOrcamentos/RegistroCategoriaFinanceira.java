import java.time.LocalDate;

public abstract class RegistroCategoriaFinanceira {

    protected Long id;
    protected Usuario usuario;
    protected String categoria;
    protected double valorReferencia;
    protected double valorAtual;

    protected RegistroCategoriaFinanceira(Long id, Usuario usuario, String categoria, double valorReferencia) {
        this.id = id;
        this.usuario = usuario;
        this.categoria = categoria;
        this.valorReferencia = valorReferencia;
        this.valorAtual = 0.0;
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getCategoria() {
        return categoria;
    }

    public double getValorReferencia() {
        return valorReferencia;
    }

    public double getValorAtual() {
        return valorAtual;
    }

    protected double valorSeguro(Double valor) {
        if (valor == null) {
            return 0.0;
        }
        return valor;
    }
}
