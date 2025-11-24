import java.time.LocalDate;
import java.util.List;

public class MetaFinanceira extends RegistroCategoriaFinanceira {

    private LocalDate dataLimite;

    public MetaFinanceira(Long id, Usuario responsavel, String categoria, double valorObjetivo, LocalDate dataLimite) {
        super(id, responsavel, categoria, valorObjetivo);
        this.dataLimite = dataLimite;
    }

    public void atualizarProgresso(List<LancamentoFinanceiro> lancamentos) {
        double acumulado = 0.0;
        LocalDate hoje = LocalDate.now();

        if (lancamentos != null) {
            for (LancamentoFinanceiro lancamento : lancamentos) {
                if (lancamento == null) {
                    continue;
                }
                if (!categoriaCorrespondente(lancamento)) {
                    continue;
                }

                LocalDate dataLancamento = lancamento.getData();
                if (dataLancamento != null && dataLancamento.isAfter(hoje)) {
                    continue;
                }

                acumulado += valorSeguro(lancamento.getValor());
            }
        }

        this.valorAtual = acumulado;
    }

    public double calcularValorRestante() {
        double restante = valorReferencia - valorAtual;
        if (restante < 0) {
            return 0.0;
        }
        return restante;
    }

    public double percentualConcluido() {
        if (valorReferencia <= 0.0) {
            return 100.0;
        }
        return (valorAtual * 100.0) / valorReferencia;
    }

    public boolean estaConcluida() {
        return valorAtual >= valorReferencia;
    }

    public boolean estaAtrasada(LocalDate hoje) {
        LocalDate referencia = hoje;
        if (referencia == null) {
            referencia = LocalDate.now();
        }

        if (dataLimite == null) {
            return false;
        }

        return referencia.isAfter(dataLimite) && !estaConcluida();
    }

    public void adicionarValor(double incremento) {
        if (incremento <= 0.0) {
            return;
        }
        this.valorAtual += incremento;
    }

    public Usuario getResponsavel() {
        return this.usuario;
    }

    public double getValorObjetivo() {
        return this.valorReferencia;
    }

    public LocalDate getDataLimite() {
        return dataLimite;
    }

    private boolean categoriaCorrespondente(LancamentoFinanceiro lancamento) {
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
