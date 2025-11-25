import java.util.ArrayList;
import java.util.List;

public class RelatorioSaldoMensal implements RelatorioFinanceiro {
    public String getNome() {
        return "Evolucao de Saldo por Mes";
    }

    public String gerar(List<LancamentoFinanceiro> lancamentos) {
        StringBuilder sb = new StringBuilder();
        ArrayList<String> meses = new ArrayList<String>();
        ArrayList<Double> saldos = new ArrayList<Double>();

        if (lancamentos != null) {
            for (int i = 0; i < lancamentos.size(); i++) {
                LancamentoFinanceiro lancamento = lancamentos.get(i);
                if (lancamento == null) {
                    continue;
                }
                java.time.LocalDate data = lancamento.getData();
                if (data == null) {
                    continue;
                }
                int mes = data.getMonthValue();
                int ano = data.getYear();
                String chave = formatarMes(mes) + "/" + ano;

                int indice = -1;
                for (int j = 0; j < meses.size(); j++) {
                    String existente = meses.get(j);
                    if (existente != null && existente.equals(chave)) {
                        indice = j;
                        break;
                    }
                }

                if (indice == -1) {
                    meses.add(chave);
                    saldos.add(0.0);
                    indice = meses.size() - 1;
                }

                double saldo = saldos.get(indice);
                if (lancamento.getTipo() == LancamentoFinanceiro.TipoLancamento.RECEITA) {
                    saldo = saldo + lancamento.getValor();
                } else if (lancamento.getTipo() == LancamentoFinanceiro.TipoLancamento.DESPESA) {
                    saldo = saldo - lancamento.getValor();
                }
                saldos.set(indice, saldo);
            }
        }

        for (int i = 0; i < meses.size(); i++) {
            sb.append("Mes: " + meses.get(i) + ", Saldo: " + saldos.get(i) + "\n");
        }
        return sb.toString();
    }

    private String formatarMes(int mes) {
        String nome = "Mes " + mes;
        if (mes == 1) {
            nome = "Janeiro";
        } else if (mes == 2) {
            nome = "Fevereiro";
        } else if (mes == 3) {
            nome = "Marco";
        } else if (mes == 4) {
            nome = "Abril";
        } else if (mes == 5) {
            nome = "Maio";
        } else if (mes == 6) {
            nome = "Junho";
        } else if (mes == 7) {
            nome = "Julho";
        } else if (mes == 8) {
            nome = "Agosto";
        } else if (mes == 9) {
            nome = "Setembro";
        } else if (mes == 10) {
            nome = "Outubro";
        } else if (mes == 11) {
            nome = "Novembro";
        } else if (mes == 12) {
            nome = "Dezembro";
        }
        return nome;
    }
}
