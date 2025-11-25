import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RelatorioGastosPorPeriodo implements RelatorioFinanceiro {
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public RelatorioGastosPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public String getNome() {
        return "Gastos por Periodo";
    }

    public String gerar(List<LancamentoFinanceiro> lancamentos) {
        StringBuilder sb = new StringBuilder();
        double total = 0.0;
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
                LocalDate data = lancamento.getData();
                if (data == null) {
                    continue;
                }
                boolean depoisInicio = true;
                if (dataInicio != null) {
                    if (data.isBefore(dataInicio)) {
                        depoisInicio = false;
                    }
                }
                boolean antesFim = true;
                if (dataFim != null) {
                    if (data.isAfter(dataFim)) {
                        antesFim = false;
                    }
                }
                if (!depoisInicio || !antesFim) {
                    continue;
                }

                total = total + lancamento.getValor();

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
                    totais.add(lancamento.getValor());
                } else {
                    double valor = totais.get(indice);
                    valor = valor + lancamento.getValor();
                    totais.set(indice, valor);
                }
            }
        }

        sb.append("Total de gastos entre " + dataInicio + " e " + dataFim + ": " + total + "\n");
        for (int i = 0; i < categorias.size(); i++) {
            sb.append("Categoria: " + categorias.get(i) + ", Total gasto: " + totais.get(i) + "\n");
        }
        return sb.toString();
    }
}
