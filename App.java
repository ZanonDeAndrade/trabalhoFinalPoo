import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class App {

    private final Scanner scanner = new Scanner(System.in);
    private final ArrayList<UsuarioIndividual> usuarios = new ArrayList<UsuarioIndividual>();
    private final ArrayList<Grupo> grupos = new ArrayList<Grupo>();
    private final ArrayList<LancamentoFinanceiro> lancamentos = new ArrayList<LancamentoFinanceiro>();
    private final ArrayList<MetaFinanceira> metas = new ArrayList<MetaFinanceira>();
    private final ArrayList<OrcamentoCategoria> orcamentos = new ArrayList<OrcamentoCategoria>();
    private final Map<String, ArrayList<IConta>> contasPorUsuario = new HashMap<String, ArrayList<IConta>>();
    private final AlgoritmoFinanceiroService algoritmoService = new AlgoritmoFinanceiroService();

    public void executar() {
        boolean rodando = true;
        while (rodando) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1) Usuarios");
            System.out.println("2) Contas");
            System.out.println("3) Lancamentos");
            System.out.println("4) Relatorios");
            System.out.println("5) Metas e Simulacoes");
            System.out.println("6) Rateio e Grupos");
            System.out.println("0) Sair");
            System.out.print("Escolha: ");
            String opcao = scanner.nextLine();
            if ("1".equals(opcao)) {
                menuUsuarios();
            } else if ("2".equals(opcao)) {
                menuContas();
            } else if ("3".equals(opcao)) {
                menuLancamentos();
            } else if ("4".equals(opcao)) {
                menuRelatorios();
            } else if ("5".equals(opcao)) {
                menuMetasESimulacoes();
            } else if ("6".equals(opcao)) {
                menuRateioEGrupos();
            } else if ("0".equals(opcao)) {
                rodando = false;
            } else {
                System.out.println("Opcao invalida.");
            }
        }
        System.out.println("Encerrando...");
    }

    private void menuUsuarios() {
        System.out.println("\n--- Usuarios ---");
        System.out.println("1) Cadastrar usuario individual");
        System.out.println("2) Listar usuarios");
        System.out.print("Escolha: ");
        String opcao = scanner.nextLine();
        if ("1".equals(opcao)) {
            System.out.print("ID: ");
            String id = scanner.nextLine();
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Senha: ");
            String senha = scanner.nextLine();
            UsuarioIndividual novo = new UsuarioIndividual(id, nome, email, senha);
            usuarios.add(novo);
            System.out.println("Usuario cadastrado.");
        } else if ("2".equals(opcao)) {
            for (UsuarioIndividual u : usuarios) {
                System.out.println(u.getId() + " - " + u.getNome() + " (" + u.getEmail() + ")");
            }
        } else {
            System.out.println("Opcao invalida.");
        }
    }

    private void menuContas() {
        System.out.println("\n--- Contas ---");
        System.out.println("1) Cadastrar conta para usuario");
        System.out.println("2) Depositar");
        System.out.println("3) Sacar");
        System.out.println("4) Listar contas de usuario");
        System.out.print("Escolha: ");
        String opcao = scanner.nextLine();
        if ("1".equals(opcao)) {
            UsuarioIndividual usuario = escolherUsuario();
            if (usuario == null) return;
            System.out.println("Tipos: 1) Corrente 2) Digital 3) Poupanca 4) Carteira Investimento 5) Cartao Credito");
            String tipo = scanner.nextLine();
            System.out.print("ID da conta: ");
            String idConta = scanner.nextLine();
            System.out.print("Nome da conta: ");
            String nomeConta = scanner.nextLine();
            IConta conta = null;
            if ("1".equals(tipo)) {
                System.out.print("Limite: ");
                double limite = lerDouble();
                conta = new ContaCorrente(idConta, nomeConta, limite);
            } else if ("2".equals(tipo)) {
                conta = new ContaDigital(idConta, nomeConta);
            } else if ("3".equals(tipo)) {
                conta = new Poupanca(idConta, nomeConta);
            } else if ("4".equals(tipo)) {
                System.out.print("Rendimento mensal (ex: 0.01 para 1%): ");
                double rend = lerDouble();
                conta = new CarteiraInvestimento(idConta, nomeConta, rend);
            } else if ("5".equals(tipo)) {
                System.out.print("Limite do cartao: ");
                double limite = lerDouble();
                conta = new CartaoCredito(idConta, nomeConta, limite);
            }
            if (conta != null) {
                getContasDoUsuario(usuario).add(conta);
                System.out.println("Conta cadastrada.");
            } else {
                System.out.println("Tipo invalido.");
            }
        } else if ("2".equals(opcao) || "3".equals(opcao) || "4".equals(opcao)) {
            UsuarioIndividual usuario = escolherUsuario();
            if (usuario == null) return;
            ArrayList<IConta> contas = getContasDoUsuario(usuario);
            if (contas.isEmpty()) {
                System.out.println("Nenhuma conta para este usuario.");
                return;
            }
            for (int i = 0; i < contas.size(); i++) {
                IConta c = contas.get(i);
                System.out.println((i + 1) + ") " + c.getClass().getSimpleName() + " - Saldo: " + c.getSaldo());
            }
            if ("4".equals(opcao)) {
                return;
            }
            System.out.print("Escolha o numero da conta: ");
            int idx = lerInt() - 1;
            if (idx < 0 || idx >= contas.size()) {
                System.out.println("Indice invalido.");
                return;
            }
            IConta conta = contas.get(idx);
            System.out.print("Valor: ");
            double valor = lerDouble();
            if ("2".equals(opcao)) {
                conta.depositar(valor);
                System.out.println("Deposito realizado. Saldo: " + conta.getSaldo());
            } else {
                boolean ok = conta.sacar(valor);
                System.out.println("Saque " + (ok ? "realizado" : "falhou") + ". Saldo: " + conta.getSaldo());
            }
        } else {
            System.out.println("Opcao invalida.");
        }
    }

    private void menuLancamentos() {
        System.out.println("\n--- Lancamentos ---");
        System.out.println("1) Adicionar lancamento");
        System.out.println("2) Listar lancamentos");
        System.out.print("Escolha: ");
        String opcao = scanner.nextLine();
        if ("1".equals(opcao)) {
            System.out.print("Tipo (R para receita, D para despesa): ");
            String tipo = scanner.nextLine();
            LancamentoFinanceiro.TipoLancamento tipoLanc = "R".equalsIgnoreCase(tipo) ? LancamentoFinanceiro.TipoLancamento.RECEITA : LancamentoFinanceiro.TipoLancamento.DESPESA;
            System.out.print("Valor: ");
            double valor = lerDouble();
            System.out.print("Categoria: ");
            String categoria = scanner.nextLine();
            System.out.print("Data (YYYY-MM-DD): ");
            LocalDate data = lerData();
            LancamentoFinanceiro lanc = new LancamentoFinanceiro(tipoLanc, valor, categoria, data);
            lancamentos.add(lanc);
            System.out.println("Lancamento adicionado.");
        } else if ("2".equals(opcao)) {
            for (int i = 0; i < lancamentos.size(); i++) {
                LancamentoFinanceiro l = lancamentos.get(i);
                System.out.println((i + 1) + ") " + l.getTipo() + " | " + l.getCategoria() + " | " + l.getValor() + " | " + l.getData());
            }
        } else {
            System.out.println("Opcao invalida.");
        }
    }

    private void menuRelatorios() {
        Dashboard dashboard = new Dashboard(lancamentos);
        System.out.println("\n--- Relatorios ---");
        System.out.println("1) Gastos por periodo");
        System.out.println("2) Comparativo por categoria");
        System.out.println("3) Ranking maiores despesas");
        System.out.println("4) Evolucao saldo por mes");
        System.out.println("5) Resumo usuario");
        System.out.print("Escolha: ");
        String opcao = scanner.nextLine();
        if ("1".equals(opcao)) {
            System.out.print("Data inicio (YYYY-MM-DD): ");
            LocalDate ini = lerData();
            System.out.print("Data fim (YYYY-MM-DD): ");
            LocalDate fim = lerData();
            dashboard.relatorioGastosPorPeriodo(ini, fim);
        } else if ("2".equals(opcao)) {
            dashboard.relatorioComparativoPorCategoria();
        } else if ("3".equals(opcao)) {
            dashboard.relatorioRankingMaioresDespesas();
        } else if ("4".equals(opcao)) {
            dashboard.relatorioEvolucaoSaldoPorMes();
        } else if ("5".equals(opcao)) {
            Usuario usuario = escolherUsuario();
            dashboard.relatorioResumoUsuario(usuario);
        } else {
            System.out.println("Opcao invalida.");
        }
    }

    private void menuMetasESimulacoes() {
        System.out.println("\n--- Metas e Simulacoes ---");
        System.out.println("1) Criar meta financeira");
        System.out.println("2) Criar orcamento por categoria");
        System.out.println("3) Atualizar e listar metas");
        System.out.println("4) Listar orcamentos");
        System.out.println("5) Rodar algoritmos de simulacao");
        System.out.print("Escolha: ");
        String opcao = scanner.nextLine();
        if ("1".equals(opcao)) {
            UsuarioIndividual usuario = escolherUsuario();
            if (usuario == null) return;
            System.out.print("ID da meta: ");
            Long id = lerLong();
            System.out.print("Categoria: ");
            String categoria = scanner.nextLine();
            System.out.print("Valor objetivo: ");
            double valor = lerDouble();
            System.out.print("Data limite (YYYY-MM-DD): ");
            LocalDate dataLimite = lerData();
            MetaFinanceira meta = new MetaFinanceira(id, usuario, categoria, valor, dataLimite);
            metas.add(meta);
            System.out.println("Meta criada.");
        } else if ("2".equals(opcao)) {
            UsuarioIndividual usuario = escolherUsuario();
            if (usuario == null) return;
            System.out.print("ID do orcamento: ");
            Long id = lerLong();
            System.out.print("Categoria: ");
            String categoria = scanner.nextLine();
            System.out.print("Limite: ");
            double limite = lerDouble();
            OrcamentoCategoria o = new OrcamentoCategoria(id, usuario, categoria, limite);
            orcamentos.add(o);
            System.out.println("Orcamento criado.");
        } else if ("3".equals(opcao)) {
            for (MetaFinanceira m : metas) {
                m.atualizarProgresso(lancamentos);
                System.out.println("Meta " + m.getId() + " | Categoria: " + m.getCategoria() + " | Progresso: " + m.getValorAtual() + "/" + m.getValorReferencia() + " (" + m.percentualConcluido() + "%)");
            }
        } else if ("4".equals(opcao)) {
            for (OrcamentoCategoria o : orcamentos) {
                o.registrarDespesaEncontrada(lancamentos);
                System.out.println("Orcamento " + o.getId() + " | Categoria: " + o.getCategoria() + " | Gasto: " + o.getGastoAtual() + "/" + o.getLimite() + " Estourado: " + o.isEstourado());
            }
        } else if ("5".equals(opcao)) {
            System.out.println("Algoritmos disponiveis:");
            algoritmoService.listarAlgoritmos();
            System.out.print("Escolha numero: ");
            int idx = lerInt();
            Usuario usuario = escolherUsuario();
            algoritmoService.executarPorIndice(idx, usuario, lancamentos);
        } else {
            System.out.println("Opcao invalida.");
        }
    }

    private void menuRateioEGrupos() {
        System.out.println("\n--- Rateio e Grupos ---");
        System.out.println("1) Criar grupo");
        System.out.println("2) Adicionar membro a grupo");
        System.out.println("3) Checar permissao em grupo");
        System.out.println("4) Registrar despesa rateada");
        System.out.print("Escolha: ");
        String opcao = scanner.nextLine();
        if ("1".equals(opcao)) {
            System.out.print("ID do grupo: ");
            String id = scanner.nextLine();
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Senha: ");
            String senha = scanner.nextLine();
            System.out.print("Capacidade de membros: ");
            int capacidade = lerInt();
            Grupo g = new Grupo(id, nome, email, senha, capacidade);
            grupos.add(g);
            System.out.println("Grupo criado.");
        } else if ("2".equals(opcao)) {
            Grupo grupo = escolherGrupo();
            if (grupo == null) return;
            UsuarioIndividual usuario = escolherUsuario();
            if (usuario == null) return;
            System.out.println("Papéis: 1 PROPRIETARIO, 2 ADMINISTRADOR, 3 MEMBRO, 4 OBSERVADOR");
            String p = scanner.nextLine();
            Papel papel = Papel.MEMBRO;
            if ("1".equals(p)) papel = Papel.PROPRIETARIO;
            else if ("2".equals(p)) papel = Papel.ADMINISTRADOR;
            else if ("4".equals(p)) papel = Papel.OBSERVADOR;
            boolean ok = grupo.adicionarMembro(usuario, papel);
            System.out.println(ok ? "Membro adicionado." : "Nao foi possivel adicionar.");
        } else if ("3".equals(opcao)) {
            Grupo grupo = escolherGrupo();
            if (grupo == null) return;
            UsuarioIndividual usuario = escolherUsuario();
            if (usuario == null) return;
            System.out.println("Permissao para checar (ex: GERENCIAR_MEMBROS): ");
            String perm = scanner.nextLine();
            try {
                Permissao permissao = Permissao.valueOf(perm);
                System.out.println("Pode? " + grupo.pode(usuario, permissao));
            } catch (IllegalArgumentException e) {
                System.out.println("Permissao invalida.");
            }
        } else if ("4".equals(opcao)) {
            System.out.print("Descricao/categoria da despesa: ");
            String categoria = scanner.nextLine();
            System.out.print("Valor total: ");
            double total = lerDouble();
            System.out.print("Quantidade de participantes: ");
            int qtd = lerInt();
            if (qtd <= 0) {
                System.out.println("Quantidade invalida.");
                return;
            }
            double valorPorPessoa = total / qtd;
            for (int i = 0; i < qtd; i++) {
                System.out.print("Nome do participante " + (i + 1) + ": ");
                String nome = scanner.nextLine();
                LancamentoFinanceiro l = new LancamentoFinanceiro(LancamentoFinanceiro.TipoLancamento.DESPESA, valorPorPessoa, categoria + " - " + nome, LocalDate.now());
                lancamentos.add(l);
            }
            System.out.println("Despesa rateada registrada em " + qtd + " lancamentos.");
        } else {
            System.out.println("Opcao invalida.");
        }
    }

    private UsuarioIndividual escolherUsuario() {
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuario cadastrado.");
            return null;
        }
        System.out.println("Escolha usuario por ID:");
        for (UsuarioIndividual u : usuarios) {
            System.out.println("- " + u.getId() + ": " + u.getNome());
        }
        String id = scanner.nextLine();
        for (UsuarioIndividual u : usuarios) {
            if (u.getId().equals(id)) return u;
        }
        System.out.println("Usuario nao encontrado.");
        return null;
    }

    private Grupo escolherGrupo() {
        if (grupos.isEmpty()) {
            System.out.println("Nenhum grupo cadastrado.");
            return null;
        }
        System.out.println("Escolha grupo por ID:");
        for (Grupo g : grupos) {
            System.out.println("- " + g.getId() + ": " + g.getNome());
        }
        String id = scanner.nextLine();
        for (Grupo g : grupos) {
            if (g.getId().equals(id)) return g;
        }
        System.out.println("Grupo nao encontrado.");
        return null;
    }

    private ArrayList<IConta> getContasDoUsuario(UsuarioIndividual usuario) {
        ArrayList<IConta> contas = contasPorUsuario.get(usuario.getId());
        if (contas == null) {
            contas = new ArrayList<IConta>();
            contasPorUsuario.put(usuario.getId(), contas);
        }
        return contas;
    }

    private double lerDouble() {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private int lerInt() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private Long lerLong() {
        try {
            return Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    private LocalDate lerData() {
        try {
            String txt = scanner.nextLine();
            if (txt == null || txt.isEmpty()) {
                return LocalDate.now();
            }
            return LocalDate.parse(txt);
        } catch (Exception e) {
            return LocalDate.now();
        }
    }
}
