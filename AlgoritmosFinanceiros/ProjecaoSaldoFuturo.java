import java.util.ArrayList;

public class ProjecaoSaldoFuturo implements AlgoritmoFinanceiro {
    public String getNome() {
        return "Projecao de Saldo Futuro";
    }

    public void executar(Usuario usuario, ArrayList<LancamentoFinanceiro> lancamentos) {
        double saldoAtual = 0.0;

        if (lancamentos != null) {
            for (int i = 0; i < lancamentos.size(); i++) {
                LancamentoFinanceiro lancamento = lancamentos.get(i);
                if (lancamento == null) {
                    continue;
                }
                if (lancamento.getTipo() == LancamentoFinanceiro.TipoLancamento.RECEITA) {
                    saldoAtual = saldoAtual + lancamento.getValor();
                } else if (lancamento.getTipo() == LancamentoFinanceiro.TipoLancamento.DESPESA) {
                    saldoAtual = saldoAtual - lancamento.getValor();
                }
            }
        }

        double mediaDiaria = saldoAtual / 30.0;
        double saldoProjetado = saldoAtual + mediaDiaria * 30.0;

        System.out.println("Algoritmo: " + getNome());
        System.out.println("Saldo atual: " + saldoAtual);
        System.out.println("Media diaria: " + mediaDiaria);
        System.out.println("Saldo projetado: " + saldoProjetado);
    }
}
