import java.util.ArrayList;
import java.util.List;

public class RelatorioPorCategoria implements RelatorioFinanceiro {
    public String getNome() {
        return "Comparativo por Categoria";
    }

    public String gerar(List<LancamentoFinanceiro> lancamentos) {
        StringBuilder sb = new StringBuilder();
        ArrayList<String> categorias = new ArrayList<String>();
        ArrayList<Double> receitas = new ArrayList<Double>();
        ArrayList<Double> despesas = new ArrayList<Double>();

        if (lancamentos != null) {
            for (int i = 0; i < lancamentos.size(); i++) {
                LancamentoFinanceiro lancamento = lancamentos.get(i);
                if (lancamento == null) {
                    continue;
                }
                String categoria = lancamento.getCategoria();
                if (categoria == null) {
                    continue;
                }

                int indice = -1;
                for (int j = 0; j < categorias.size(); j++) {
                    String existente = categorias.get(j);
                    if (existente != null && existente.equalsIgnoreCase(categoria)) {
                        indice = j;
                        break;
                    }
                }

                if (indice == -1) {
                    categorias.add(categoria);
                    receitas.add(0.0);
                    despesas.add(0.0);
                    indice = categorias.size() - 1;
                }

                if (lancamento.getTipo() == LancamentoFinanceiro.TipoLancamento.RECEITA) {
                    double valor = receitas.get(indice);
                    valor = valor + lancamento.getValor();
                    receitas.set(indice, valor);
                } else if (lancamento.getTipo() == LancamentoFinanceiro.TipoLancamento.DESPESA) {
                    double valor = despesas.get(indice);
                    valor = valor + lancamento.getValor();
                    despesas.set(indice, valor);
                }
            }
        }

        for (int i = 0; i < categorias.size(); i++) {
            sb.append("Categoria: " + categorias.get(i) + ", Total de receitas: " + receitas.get(i) + ", Total de despesas: " + despesas.get(i) + "\n");
        }
        return sb.toString();
    }
}
