import java.util.ArrayList;

public class Dashboard {
    private ArrayList<LancamentoFinanceiro> lancamentos;

    public Dashboard(ArrayList<LancamentoFinanceiro> lancamentos) {
        this.lancamentos = lancamentos;
    }

    public void relatorioGastosPorPeriodo(java.time.LocalDate dataInicio, java.time.LocalDate dataFim) {
        String texto = gerarRelatorioGastosPorPeriodoTexto(dataInicio, dataFim);
        System.out.println(texto);
    }

    public void relatorioComparativoPorCategoria() {
        String texto = gerarRelatorioComparativoPorCategoriaTexto();
        System.out.println(texto);
    }

    public void relatorioRankingMaioresDespesas() {
        String texto = gerarRelatorioRankingMaioresDespesasTexto();
        System.out.println(texto);
    }

    public void relatorioEvolucaoSaldoPorMes() {
        String texto = gerarRelatorioEvolucaoSaldoPorMesTexto();
        System.out.println(texto);
    }

    public void relatorioResumoUsuario(Usuario usuario) {
        String texto = gerarRelatorioResumoUsuarioTexto(usuario);
        System.out.println(texto);
    }

    public void salvarRelatoriosEmArquivo(java.time.LocalDate dataInicio, java.time.LocalDate dataFim) {
        StringBuilder sb = new StringBuilder();
        sb.append(gerarRelatorioGastosPorPeriodoTexto(dataInicio, dataFim));
        sb.append("\n");
        sb.append(gerarRelatorioComparativoPorCategoriaTexto());
        sb.append("\n");
        sb.append(gerarRelatorioRankingMaioresDespesasTexto());
        sb.append("\n");
        sb.append(gerarRelatorioEvolucaoSaldoPorMesTexto());
        sb.append("\n");
        sb.append(gerarRelatorioResumoUsuarioTexto(null));

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

    private String gerarRelatorioGastosPorPeriodoTexto(java.time.LocalDate dataInicio, java.time.LocalDate dataFim) {
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
                java.time.LocalDate data = lancamento.getData();
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

    private String gerarRelatorioComparativoPorCategoriaTexto() {
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

    private String gerarRelatorioRankingMaioresDespesasTexto() {
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

    private String gerarRelatorioEvolucaoSaldoPorMesTexto() {
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

    private String gerarRelatorioResumoUsuarioTexto(Usuario usuario) {
        StringBuilder sb = new StringBuilder();
        double totalReceitas = 0.0;
        double totalDespesas = 0.0;

        if (lancamentos != null) {
            for (int i = 0; i < lancamentos.size(); i++) {
                LancamentoFinanceiro lancamento = lancamentos.get(i);
                if (lancamento == null) {
                    continue;
                }
                if (lancamento.getTipo() == LancamentoFinanceiro.TipoLancamento.RECEITA) {
                    totalReceitas = totalReceitas + lancamento.getValor();
                } else if (lancamento.getTipo() == LancamentoFinanceiro.TipoLancamento.DESPESA) {
                    totalDespesas = totalDespesas + lancamento.getValor();
                }
            }
        }

        double saldo = totalReceitas - totalDespesas;

        if (usuario != null && usuario instanceof Grupo) {
            sb.append("Resumo do Grupo\n");
        } else {
            sb.append("Resumo Individual\n");
        }
        sb.append("Total de receitas: " + totalReceitas + "\n");
        sb.append("Total de despesas: " + totalDespesas + "\n");
        sb.append("Saldo: " + saldo + "\n");
        sb.append("Metas atingidas: " + 0 + "\n");
        sb.append("Orcamentos configurados: " + 0 + "\n");
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
