import java.util.List;

public class RelatorioUsuario implements RelatorioFinanceiro {
    private Usuario usuario;

    public RelatorioUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNome() {
        return "Resumo do Usuario";
    }

    public String gerar(List<LancamentoFinanceiro> lancamentos) {
        StringBuilder sb = new StringBuilder();
        double totalReceitas = 0.0;
        double totalDespesas = 0.0;

        if (lancamentos != null) {
            for (int i = 0; i < lancamentos.size(); i++) {
                LancamentoFinanceiro lancamento = lancamentos.get(i);
                if (lancamento == null) {
                    continue;
                }
                if (lancamento.getTipo() == LancamentoFinanceiro.TipoLancamento.RECEITA) {
                    totalReceitas = totalReceitas + lancamento.getValor();
                } else if (lancamento.getTipo() == LancamentoFinanceiro.TipoLancamento.DESPESA) {
                    totalDespesas = totalDespesas + lancamento.getValor();
                }
            }
        }

        double saldo = totalReceitas - totalDespesas;

        if (usuario != null && usuario instanceof Grupo) {
            sb.append("Resumo do Grupo\n");
        } else {
            sb.append("Resumo Individual\n");
        }
        sb.append("Total de receitas: " + totalReceitas + "\n");
        sb.append("Total de despesas: " + totalDespesas + "\n");
        sb.append("Saldo: " + saldo + "\n");
        sb.append("Metas atingidas: " + 0 + "\n");
        sb.append("Orcamentos configurados: " + 0 + "\n");
        return sb.toString();
    }
}
