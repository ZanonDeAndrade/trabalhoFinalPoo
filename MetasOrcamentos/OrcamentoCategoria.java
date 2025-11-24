public class OrcamentoCategoria extends RegistroCategoriaFinanceira {

    public OrcamentoCategoria(Long id, Usuario dono, String categoria, double limite) {
        super(id, dono, categoria, limite);
    }

    public void registrarDespesa(LancamentoFinanceiro lancamento) {
        if (lancamento == null) {
            return;
        }

        if (lancamento.getTipo() != LancamentoFinanceiro.TipoLancamento.DESPESA) {
            return;
        }

        if (!categoriaCorresponde(lancamento)) {
            return;
        }

        this.valorAtual += valorSeguro(lancamento.getValor());
    }

    public double calcularSaldoRestante() {
        double restante = valorReferencia - valorAtual;
        if (restante < 0.0) {
            return 0.0;
        }
        return restante;
    }

    public double percentualUtilizado() {
        if (valorReferencia <= 0.0) {
            return 0.0;
        }
        return (valorAtual * 100.0) / valorReferencia;
    }

    public boolean isEstourado() {
        return valorAtual > valorReferencia;
    }

    public Usuario getDono() {
        return this.usuario;
    }

    public double getLimite() {
        return this.valorReferencia;
    }

    public double getGastoAtual() {
        return this.valorAtual;
    }

    private boolean categoriaCorresponde(LancamentoFinanceiro lancamento) {
        if (categoria == null) {
            return false;
        }

        String categoriaLancamento = lancamento.getCategoria();
        if (categoriaLancamento == null) {
            return false;
        }

        return categoria.equalsIgnoreCase(categoriaLancamento);
    }
}
