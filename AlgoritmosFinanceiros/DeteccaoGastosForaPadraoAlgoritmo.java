import java.util.ArrayList;

public class DeteccaoGastosForaPadraoAlgoritmo implements AlgoritmoFinanceiro {
    public String getNome() {
        return "Deteccao de Gastos Fora do Padrao";
    }

    public void executar(Usuario usuario, ArrayList<LancamentoFinanceiro> lancamentos) {
        ArrayList<String> categorias = new ArrayList<String>();
        ArrayList<Double> somaPorCategoria = new ArrayList<Double>();
        ArrayList<Integer> quantidadePorCategoria = new ArrayList<Integer>();

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
                    somaPorCategoria.add(lancamento.getValor());
                    quantidadePorCategoria.add(1);
                } else {
                    double soma = somaPorCategoria.get(indiceCategoria);
                    soma = soma + lancamento.getValor();
                    somaPorCategoria.set(indiceCategoria, soma);

                    int quantidade = quantidadePorCategoria.get(indiceCategoria);
                    quantidade = quantidade + 1;
                    quantidadePorCategoria.set(indiceCategoria, quantidade);
                }
            }
        }

        ArrayList<Double> mediasPorCategoria = new ArrayList<Double>();
        for (int i = 0; i < categorias.size(); i++) {
            double soma = somaPorCategoria.get(i);
            int quantidade = quantidadePorCategoria.get(i);
            double media = soma / quantidade;
            mediasPorCategoria.add(media);
        }

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

                if (indiceCategoria != -1) {
                    double media = mediasPorCategoria.get(indiceCategoria);
                    double limite = media * 2.0;

                    if (lancamento.getValor() > limite) {
                        System.out.println("Categoria: " + categoria);
                        System.out.println("Valor lancamento: " + lancamento.getValor());
                        System.out.println("Media da categoria: " + media);
                        System.out.println("Limite considerado: " + limite);
                    }
                }
            }
        }
    }
}
