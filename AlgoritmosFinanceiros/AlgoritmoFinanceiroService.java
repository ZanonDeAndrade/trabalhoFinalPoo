import java.util.ArrayList;

public class AlgoritmoFinanceiroService {
    private ArrayList<AlgoritmoFinanceiro> algoritmos;

    public AlgoritmoFinanceiroService() {
        algoritmos = new ArrayList<AlgoritmoFinanceiro>();
        algoritmos.add(new ProjecaoSaldoFuturo());
        algoritmos.add(new SugestaoEconomiaPorCategoria());
        algoritmos.add(new DeteccaoGastosForaPadraoAlgoritmo());
    }

    public void listarAlgoritmos() {
        for (int i = 0; i < algoritmos.size(); i++) {
            AlgoritmoFinanceiro algoritmo = algoritmos.get(i);
            System.out.println((i + 1) + " - " + algoritmo.getNome());
        }
    }

    public void executarPorIndice(int indice, Usuario usuario, ArrayList<LancamentoFinanceiro> lancamentos) {
        int posicao = indice - 1;
        if (posicao < 0 || posicao >= algoritmos.size()) {
            System.out.println("Indice invalido.");
            return;
        }

        AlgoritmoFinanceiro algoritmo = algoritmos.get(posicao);
        algoritmo.executar(usuario, lancamentos);
    }
}
