import java.util.ArrayList;

public class SugestaoEconomiaPorCategoria implements AlgoritmoFinanceiro {
    public String getNome() {
        return "Sugestao de Economia por Categoria";
    }

    public void executar(Usuario usuario, ArrayList<LancamentoFinanceiro> lancamentos) {
        ArrayList<String> categorias = new ArrayList<String>();
        ArrayList<Double> totais = new ArrayList<Double>();

        if (lancamentos != null) {
            for (int i = 0; i < lancamentos.size(); i++) {
                LancamentoFinanceiro lancamento = lancamentos.get(i);
                if (lancamento == null) {
                    continue;
                }
                if (lancamento.getTipo() != LancamentoFinanceiro.TipoLancamento.DESPESA) {
                    continue;
                }
                String categoria = lancamento.getCategoria();
                if (categoria == null) {
                    continue;
                }

                int indiceCategoria = -1;
                for (int j = 0; j < categorias.size(); j++) {
                    String existente = categorias.get(j);
                    if (existente != null && existente.equalsIgnoreCase(categoria)) {
                        indiceCategoria = j;
                        break;
                    }
                }

                if (indiceCategoria == -1) {
                    categorias.add(categoria);
                    totais.add(lancamento.getValor());
                } else {
                    double total = totais.get(indiceCategoria);
                    total = total + lancamento.getValor();
                    totais.set(indiceCategoria, total);
                }
            }
        }

        for (int i = 0; i < categorias.size(); i++) {
            String categoria = categorias.get(i);
            double total = totais.get(i);
            double economia = total * 0.10;

            System.out.println("Categoria: " + categoria);
            System.out.println("Total gasto: " + total);
            System.out.println("Economia sugerida: " + economia);
        }
    }
}
