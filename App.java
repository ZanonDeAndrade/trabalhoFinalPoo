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
    private final Map<String, Service> financeiroPorUsuario = new HashMap<String, Service>();
    private final AlgoritmoFinanceiroService algoritmoService = new AlgoritmoFinanceiroService();
    private UsuarioIndividual usuarioLogado = null;

    public void executar() {
        autenticar();
        if (usuarioLogado == null) {
            System.out.println("Encerrando...");
            return;
        }
        boolean rodando = true;
        while (rodando) {
            limparTela();
            cabecalho("MENU PRINCIPAL", usuarioLogado);
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
        limparTela();
        cabecalho("USUARIOS", usuarioLogado);
        System.out.println("1) Cadastrar usuario individual");
        System.out.println("2) Listar usuarios");
        System.out.println("3) Trocar usuario logado");
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
            usuarioLogado = novo;
            System.out.println("Usuario cadastrado e logado.");
        } else if ("2".equals(opcao)) {
            for (UsuarioIndividual u : usuarios) {
                System.out.println(u.getId() + " - " + u.getNome() + " (" + u.getEmail() + ")");
            }
        } else if ("3".equals(opcao)) {
            UsuarioIndividual novo = escolherUsuario();
            if (novo != null) {
                usuarioLogado = novo;
                System.out.println("Logado como " + novo.getNome());
            }
        } else {
            System.out.println("Opcao invalida.");
        }
    }

    private void menuContas() {
        limparTela();
        cabecalho("CONTAS", usuarioLogado);
        System.out.println("1) Cadastrar conta para usuario");
        System.out.println("2) Depositar");
        System.out.println("3) Sacar");
        System.out.println("4) Listar contas de usuario");
        System.out.println("5) Comprar com cartao de credito");
        System.out.println("6) Pagar fatura cartao");
        System.out.println("7) Ver fatura/limite cartao");
        System.out.print("Escolha: ");
        String opcao = scanner.nextLine();
        if ("1".equals(opcao)) {
            if (usuarioLogado == null) {
                System.out.println("Nenhum usuario logado.");
                return;
            }
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
                getContasDoUsuario(usuarioLogado).add(conta);
                System.out.println("Conta cadastrada.");
            } else {
                System.out.println("Tipo invalido.");
            }
        } else if ("2".equals(opcao) || "3".equals(opcao) || "4".equals(opcao)) {
            if (usuarioLogado == null) {
                System.out.println("Nenhum usuario logado.");
                return;
            }
            ArrayList<IConta> contas = getContasDoUsuario(usuarioLogado);
            if (contas.isEmpty()) {
                System.out.println("Nenhuma conta para este usuario.");
                return;
            }
            for (int i = 0; i < contas.size(); i++) {
                IConta c = contas.get(i);
                System.out.println((i + 1) + ") " + c.toString());
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
        } else if ("5".equals(opcao) || "6".equals(opcao) || "7".equals(opcao)) {
            if (usuarioLogado == null) {
                System.out.println("Nenhum usuario logado.");
                return;
            }
            ArrayList<IConta> contas = getContasDoUsuario(usuarioLogado);
            ArrayList<CartaoCredito> cartoes = new ArrayList<CartaoCredito>();
            for (IConta c : contas) {
                if (c instanceof CartaoCredito) {
                    cartoes.add((CartaoCredito) c);
                }
            }
            if (cartoes.isEmpty()) {
                System.out.println("Nenhum cartao cadastrado.");
                return;
            }
            for (int i = 0; i < cartoes.size(); i++) {
                CartaoCredito cc = cartoes.get(i);
                System.out.println((i + 1) + ") " + cc.toString());
            }
            System.out.print("Escolha o cartao: ");
            int idx = lerInt() - 1;
            if (idx < 0 || idx >= cartoes.size()) {
                System.out.println("Indice invalido.");
                return;
            }
            CartaoCredito cc = cartoes.get(idx);
            if ("5".equals(opcao)) {
                System.out.print("Valor da compra: ");
                double v = lerDouble();
                boolean ok = cc.sacar(v);
                System.out.println(ok ? "Compra registrada. Limite disponivel: " + cc.getLimiteDisponivel() : "Compra nao registrada (limite insuficiente).");
            } else if ("6".equals(opcao)) {
                System.out.print("Valor do pagamento da fatura: ");
                double v = lerDouble();
                cc.pagarFatura(v);
                System.out.println("Pagamento registrado. " + cc.toString());
            } else {
                System.out.println(cc.toString());
            }
        } else {
            System.out.println("Opcao invalida.");
        }
    }

    private void menuLancamentos() {
        limparTela();
        cabecalho("LANCAMENTOS", usuarioLogado);
        System.out.println("1) Adicionar lancamento simples");
        System.out.println("2) Listar lancamentos simples");
        System.out.println("3) Adicionar transacao parcelada");
        System.out.println("4) Adicionar transacao recorrente");
        System.out.println("5) Cadastrar limite por categoria");
        System.out.println("6) Verificar alertas de limite");
        System.out.println("7) Cadastrar despesa compartilhada");
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
            lanc.setUsuario(usuarioLogado);
            lancamentos.add(lanc);
            System.out.println("Lancamento adicionado.");
        } else if ("2".equals(opcao)) {
            List<LancamentoFinanceiro> lista = getLancamentosDoUsuario();
            for (int i = 0; i < lista.size(); i++) {
                LancamentoFinanceiro l = lista.get(i);
                System.out.println((i + 1) + ") " + l.getTipo() + " | " + l.getCategoria() + " | " + l.getValor() + " | " + l.getData());
            }
        } else if ("3".equals(opcao)) {
            Service svc = getFinanceiroAtual();
            if (svc == null) {
                System.out.println("Nenhum usuario logado.");
                return;
            }
            Transacao base = lerTransacaoDetalhada();
            System.out.print("Quantidade de parcelas: ");
            int qtd = lerInt();
            svc.criarTransacaoParcelada(base, qtd);
            System.out.println("Transacao parcelada criada.");
        } else if ("4".equals(opcao)) {
            Service svc = getFinanceiroAtual();
            if (svc == null) {
                System.out.println("Nenhum usuario logado.");
                return;
            }
            Transacao base = lerTransacaoDetalhada();
            System.out.print("Quantidade de repeticoes: ");
            int qtd = lerInt();
            svc.criarTransacaoRecorrente(base, qtd);
            System.out.println("Transacao recorrente criada.");
        } else if ("5".equals(opcao)) {
            Service svc = getFinanceiroAtual();
            if (svc == null) {
                System.out.println("Nenhum usuario logado.");
                return;
            }
            System.out.print("Categoria: ");
            String categoria = scanner.nextLine();
            System.out.print("Limite: ");
            double limite = lerDouble();
            svc.cadastrarLimitePorCategoria(categoria, limite);
            System.out.println("Limite cadastrado.");
        } else if ("6".equals(opcao)) {
            Service svc = getFinanceiroAtual();
            if (svc == null) {
                System.out.println("Nenhum usuario logado.");
                return;
            }
            svc.verificarAlertasDeLimite();
        } else if ("7".equals(opcao)) {
            Service svc = getFinanceiroAtual();
            if (svc == null) {
                System.out.println("Nenhum usuario logado.");
                return;
            }
            Transacao base = lerTransacaoDetalhada();
            System.out.print("Quantidade de participantes: ");
            int qtd = lerInt();
            ArrayList<Participante> partes = new ArrayList<Participante>();
            for (int i = 0; i < qtd; i++) {
                Participante p = new Participante();
                System.out.print("Nome do participante " + (i + 1) + ": ");
                p.nome = scanner.nextLine();
                System.out.print("Valor fixo (0 para usar percentual): ");
                p.valor = lerDouble();
                if (p.valor == 0) {
                    System.out.print("Percentual (ex 25 para 25%): ");
                    p.percentual = lerDouble();
                }
                partes.add(p);
            }
            svc.cadastrarDespesaCompartilhada(base, partes);
            System.out.println("Despesa compartilhada cadastrada.");
        } else {
            System.out.println("Opcao invalida.");
        }
    }

    private void menuRelatorios() {
        limparTela();
        Dashboard dashboard = new Dashboard(new ArrayList<LancamentoFinanceiro>(getLancamentosDoUsuario()));
        cabecalho("RELATORIOS", usuarioLogado);
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
            dashboard.relatorioResumoUsuario(usuarioLogado);
        } else {
            System.out.println("Opcao invalida.");
        }
    }

    private void menuMetasESimulacoes() {
        limparTela();
        cabecalho("METAS E SIMULACOES", usuarioLogado);
        System.out.println("1) Criar meta financeira");
        System.out.println("2) Criar orcamento por categoria");
        System.out.println("3) Atualizar e listar metas");
        System.out.println("4) Listar orcamentos");
        System.out.println("5) Rodar algoritmos de simulacao");
        System.out.print("Escolha: ");
        String opcao = scanner.nextLine();
        if ("1".equals(opcao)) {
            System.out.print("ID da meta: ");
            Long id = lerLong();
            System.out.print("Categoria: ");
            String categoria = scanner.nextLine();
            System.out.print("Valor objetivo: ");
            double valor = lerDouble();
            System.out.print("Data limite (YYYY-MM-DD): ");
            LocalDate dataLimite = lerData();
            MetaFinanceira meta = new MetaFinanceira(id, usuarioLogado, categoria, valor, dataLimite);
            metas.add(meta);
            System.out.println("Meta criada.");
        } else if ("2".equals(opcao)) {
            System.out.print("ID do orcamento: ");
            Long id = lerLong();
            System.out.print("Categoria: ");
            String categoria = scanner.nextLine();
            System.out.print("Limite: ");
            double limite = lerDouble();
            OrcamentoCategoria o = new OrcamentoCategoria(id, usuarioLogado, categoria, limite);
            orcamentos.add(o);
            System.out.println("Orcamento criado.");
        } else if ("3".equals(opcao)) {
            for (MetaFinanceira m : metas) {
                if (m.getResponsavel() != usuarioLogado) continue;
                m.atualizarProgresso(getLancamentosDoUsuario());
                System.out.println("Meta " + m.getId() + " | Categoria: " + m.getCategoria() + " | Progresso: " + m.getValorAtual() + "/" + m.getValorReferencia() + " (" + m.percentualConcluido() + "%)");
            }
        } else if ("4".equals(opcao)) {
            for (OrcamentoCategoria o : orcamentos) {
                if (o.getDono() != usuarioLogado) continue;
                o.registrarDespesaEncontrada(getLancamentosDoUsuario());
                System.out.println("Orcamento " + o.getId() + " | Categoria: " + o.getCategoria() + " | Gasto: " + o.getGastoAtual() + "/" + o.getLimite() + " Estourado: " + o.isEstourado());
            }
        } else if ("5".equals(opcao)) {
            System.out.println("Algoritmos disponiveis:");
            algoritmoService.listarAlgoritmos();
            System.out.print("Escolha numero: ");
            int idx = lerInt();
            algoritmoService.executarPorIndice(idx, usuarioLogado, new ArrayList<LancamentoFinanceiro>(getLancamentosDoUsuario()));
        } else {
            System.out.println("Opcao invalida.");
        }
    }

    private void menuRateioEGrupos() {
        limparTela();
        cabecalho("RATEIO E GRUPOS", usuarioLogado);
        if (usuarioLogado == null) {
            System.out.println("Nenhum usuario logado.");
            return;
        }
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
            g.adicionarMembro(usuarioLogado, Papel.PROPRIETARIO);
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
            System.out.println("Escolha a permissao pelo numero:");
            Permissao[] perms = Permissao.values();
            for (int i = 0; i < perms.length; i++) {
                System.out.println((i + 1) + ") " + perms[i].name());
            }
            int idx = lerInt() - 1;
            if (idx < 0 || idx >= perms.length) {
                System.out.println("Opcao invalida.");
                return;
            }
            Permissao permissao = perms[idx];
            boolean pode = grupo.pode(usuarioLogado, permissao);
            System.out.println(pode ? "Tem permissao." : "Nao tem permissao.");
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
                l.setUsuario(usuarioLogado);
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

    private Transacao lerTransacaoDetalhada() {
        Transacao t = new Transacao();
        t.tipo = new Tipo();
        System.out.print("Tipo (receita/despesa): ");
        t.tipo.tipo = scanner.nextLine();

        t.categoria = new Categoria();
        System.out.print("Categoria: ");
        t.categoria.nome = scanner.nextLine();
        System.out.print("Subcategoria (opcional): ");
        t.categoria.subcategoria = scanner.nextLine();

        t.pagador = new Pagador();
        System.out.print("Pagador: ");
        t.pagador.pagador = scanner.nextLine();
        System.out.print("Beneficiario: ");
        t.pagador.beneficiario = scanner.nextLine();

        t.data = new Data();
        System.out.print("Data (texto livre): ");
        t.data.data = scanner.nextLine();
        System.out.print("Recorrencia (opcional): ");
        t.data.recorrencia = scanner.nextLine();

        System.out.print("Valor: ");
        t.valor = lerDouble();
        t.estornoDeId = null;
        return t;
    }

    private ArrayList<IConta> getContasDoUsuario(UsuarioIndividual usuario) {
        ArrayList<IConta> contas = contasPorUsuario.get(usuario.getId());
        if (contas == null) {
            contas = new ArrayList<IConta>();
            contasPorUsuario.put(usuario.getId(), contas);
        }
        return contas;
    }

    private Service getFinanceiroAtual() {
        if (usuarioLogado == null) return null;
        Service svc = financeiroPorUsuario.get(usuarioLogado.getId());
        if (svc == null) {
            svc = new Service();
            financeiroPorUsuario.put(usuarioLogado.getId(), svc);
        }
        return svc;
    }

    private List<LancamentoFinanceiro> getLancamentosDoUsuario() {
        List<LancamentoFinanceiro> lista = new ArrayList<LancamentoFinanceiro>();
        for (LancamentoFinanceiro l : lancamentos) {
            if (l != null && l.getUsuario() == usuarioLogado) {
                lista.add(l);
            }
        }
        return lista;
    }

    private UsuarioIndividual buscarUsuarioPorId(String id) {
        for (UsuarioIndividual u : usuarios) {
            if (u.getId().equals(id)) {
                return u;
            }
        }
        return null;
    }

    private void aguardarEnter() {
        System.out.println("Pressione ENTER para continuar...");
        scanner.nextLine();
    }

    private void autenticar() {
        while (usuarioLogado == null) {
            limparTela();
            cabecalho("AUTENTICACAO", null);
            System.out.println("1) Login");
            System.out.println("2) Cadastro");
            System.out.println("0) Sair");
            System.out.print("Escolha: ");
            String opcao = scanner.nextLine();
            if ("1".equals(opcao)) {
                System.out.print("Informe ID do usuario: ");
                String id = scanner.nextLine();
                UsuarioIndividual u = buscarUsuarioPorId(id);
                if (u != null) {
                    usuarioLogado = u;
                    System.out.println("Login realizado como " + u.getNome());
                } else {
                    System.out.println("Usuario nao encontrado.");
                }
            } else if ("2".equals(opcao)) {
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
                usuarioLogado = novo;
                System.out.println("Cadastro realizado e login efetuado.");
            } else if ("0".equals(opcao)) {
                break;
            } else {
                System.out.println("Opcao invalida.");
            }
            aguardarEnter();
        }
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

    private void limparTela() {
        try {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    private void cabecalho(String titulo) {
        cabecalho(titulo, usuarioLogado);
    }

    private void cabecalho(String titulo, Usuario usuario) {
        System.out.println("================================");
        System.out.println(" " + titulo + (usuario != null ? " | Usuario: " + usuario.getNome() : ""));
        System.out.println("================================");
    }
}
