import java.util.ArrayList;
import java.util.List;

public class RelatorioMaioresDespesas implements RelatorioFinanceiro {
    public String getNome() {
        return "Ranking de Maiores Despesas";
    }

    public String gerar(List<LancamentoFinanceiro> lancamentos) {
        StringBuilder sb = new StringBuilder();
        sb.append("Ranking de Maiores Despesas:\n");
        ArrayList<String> categorias = new ArrayList<String>();
        ArrayList<Double> valores = new ArrayList<Double>();

        if (lancamentos != null) {
            for (int i = 0; i < lancamentos.size(); i++) {
                LancamentoFinanceiro lancamento = lancamentos.get(i);
                if (lancamento == null) {
                    continue;
                }
                if (lancamento.getTipo() != LancamentoFinanceiro.TipoLancamento.DESPESA) {
                    continue;
                }
                categorias.add(lancamento.getCategoria());
                valores.add(lancamento.getValor());
            }
        }

        for (int i = 0; i < valores.size(); i++) {
            int indiceMaior = i;
            for (int j = i + 1; j < valores.size(); j++) {
                if (valores.get(j) > valores.get(indiceMaior)) {
                    indiceMaior = j;
                }
            }
            if (indiceMaior != i) {
                double valorTemp = valores.get(i);
                valores.set(i, valores.get(indiceMaior));
                valores.set(indiceMaior, valorTemp);

                String catTemp = categorias.get(i);
                categorias.set(i, categorias.get(indiceMaior));
                categorias.set(indiceMaior, catTemp);
            }
        }

        for (int i = 0; i < valores.size(); i++) {
            int posicao = i + 1;
            sb.append(posicao + ") Categoria " + categorias.get(i) + ", Valor " + valores.get(i) + "\n");
        }
        return sb.toString();
    }
}
