import java.util.List;

public interface RelatorioFinanceiro {
    String getNome();
    String gerar(List<LancamentoFinanceiro> lancamentos);
}
