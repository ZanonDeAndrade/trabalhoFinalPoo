import java.util.ArrayList;
import java.util.List;

public class Dashboard {
    private ArrayList<LancamentoFinanceiro> lancamentos;

    public Dashboard(ArrayList<LancamentoFinanceiro> lancamentos) {
        this.lancamentos = lancamentos;
    }

    public void relatorioGastosPorPeriodo(java.time.LocalDate dataInicio, java.time.LocalDate dataFim) {
        imprimir(new RelatorioGastosPorPeriodo(dataInicio, dataFim));
    }

    public void relatorioComparativoPorCategoria() {
        imprimir(new RelatorioPorCategoria());
    }

    public void relatorioRankingMaioresDespesas() {
        imprimir(new RelatorioMaioresDespesas());
    }

    public void relatorioEvolucaoSaldoPorMes() {
        imprimir(new RelatorioSaldoMensal());
    }

    public void relatorioResumoUsuario(Usuario usuario) {
        imprimir(new RelatorioUsuario(usuario));
    }

    public void salvarRelatoriosEmArquivo(java.time.LocalDate dataInicio, java.time.LocalDate dataFim) {
        StringBuilder sb = new StringBuilder();
        List<RelatorioFinanceiro> relatorios = new ArrayList<RelatorioFinanceiro>();
        relatorios.add(new RelatorioGastosPorPeriodo(dataInicio, dataFim));
        relatorios.add(new RelatorioPorCategoria());
        relatorios.add(new RelatorioMaioresDespesas());
        relatorios.add(new RelatorioSaldoMensal());
        relatorios.add(new RelatorioUsuario(null));

        for (RelatorioFinanceiro relatorio : relatorios) {
            sb.append(relatorio.gerar(lancamentos)).append("\n");
        }

        java.io.FileWriter writer = null;
        try {
            writer = new java.io.FileWriter("relatorio.txt");
            writer.write(sb.toString());
        } catch (java.io.IOException e) {
            System.out.println("Erro ao salvar relatorio: " + e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (java.io.IOException e) {
                    System.out.println("Erro ao fechar arquivo: " + e.getMessage());
                }
            }
        }
    }

    private void imprimir(RelatorioFinanceiro relatorio) {
        System.out.println(relatorio.gerar(lancamentos));
    }
}
