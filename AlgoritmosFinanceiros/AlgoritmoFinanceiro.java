import java.util.ArrayList;

public interface AlgoritmoFinanceiro {
    String getNome();
    void executar(Usuario usuario, ArrayList<LancamentoFinanceiro> lancamentos);
}
