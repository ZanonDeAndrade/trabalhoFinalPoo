import java.util.ArrayList;

public class Service {

    private ArrayList<Transacao> transacoes = new ArrayList<>();
    private ArrayList<CategoriaLimite> limites = new ArrayList<>();
    private int proximoId = 1;

    public boolean adicionarTransacao(Transacao t) {
        if (t == null) return false;

        String tipo = t.tipo.tipo.toLowerCase();

        if (tipo.equals("despesa")) {
            double saldo = calcularSaldo();
            if (saldo - t.valor < 0) {
                System.out.println("Saldo insuficiente.");
                return false;
            }
        }

        Transacao nova = copiar(t);
        nova.id = proximoId++;
        transacoes.add(nova);

        return true;
    }

    public double calcularSaldo() {
        double total = 0;

        for (Transacao t : transacoes) {
            String tipo = t.tipo.tipo.toLowerCase();

            if (tipo.equals("receita")) {
                total += t.valor;
            }

            if (tipo.equals("despesa")) {
                total -= t.valor;
            }
        }

        return total;
    }


    public void cadastrarLimitePorCategoria(String categoria, double limite) {
        for (CategoriaLimite cl : limites) {
            if (cl.categoria.equalsIgnoreCase(categoria)) {
                cl.limite = limite;
                return;
            }
        }

        CategoriaLimite novo = new CategoriaLimite();
        novo.categoria = categoria;
        novo.limite = limite;
        limites.add(novo);
    }


    public void verificarAlertasDeLimite() {

        for (CategoriaLimite cl : limites) {

            double totalGasto = 0;

            for (Transacao t : transacoes) {
                if (t.tipo.tipo.equalsIgnoreCase("despesa") &&
                    t.categoria != null &&
                    t.categoria.nome.equalsIgnoreCase(cl.categoria)) {
                    
                    totalGasto += t.valor;
                }
            }

            if (cl.limite > 0) {
                double porcentagem = (totalGasto * 100) / cl.limite;

                if (porcentagem >= 100) {
                    System.out.println("ALERTA: Categoria " + cl.categoria + " estourou o limite!");
                }
                else if (porcentagem >= 80) {
                    System.out.println("Aviso: Categoria " + cl.categoria + " atingiu " + porcentagem + "% do limite.");
                }
            }
        }
    }



    public ArrayList<Transacao> criarTransacaoParcelada(Transacao base, int parcelas) {

        ArrayList<Transacao> lista = new ArrayList<>();

        if (base == null || parcelas <= 0) return lista;

        double valorParcela = base.valor / parcelas;

        for (int i = 1; i <= parcelas; i++) {

            Transacao nova = copiar(base);
            nova.valor = valorParcela;
            nova.data.data = base.data.data + " - parcela " + i;
            nova.id = proximoId++;

            transacoes.add(nova);
            lista.add(nova);
        }

        return lista;
    }

    public ArrayList<Transacao> criarTransacaoRecorrente(Transacao base, int repeticoes) {

        ArrayList<Transacao> lista = new ArrayList<>();

        if (base == null || repeticoes <= 0) return lista;

        for (int i = 1; i <= repeticoes; i++) {

            Transacao nova = copiar(base);
            nova.data.data = base.data.data + " + repeticao " + i;
            nova.id = proximoId++;

            transacoes.add(nova);
            lista.add(nova);
        }

        return lista;
    }

    public ArrayList<Transacao> cadastrarDespesaCompartilhada(Transacao base, ArrayList<Participante> participantes) {

        ArrayList<Transacao> lista = new ArrayList<>();

        if (base == null || participantes == null) return lista;

        for (Participante p : participantes) {

            Transacao nova = copiar(base);

            if (p.valor > 0) {
                nova.valor = p.valor;
            } else {
                nova.valor = base.valor * (p.percentual / 100);
            }

            nova.participante = p.nome;
            nova.id = proximoId++;

            transacoes.add(nova);
            lista.add(nova);
        }

        return lista;
    }


    private Transacao buscarPorId(int id) {
        for (Transacao t : transacoes) {
            if (t.id == id) return t;
        }
        return null;
    }

    private Transacao copiar(Transacao b) {
        Transacao n = new Transacao();

        n.tipo = new Tipo();
        n.tipo.tipo = b.tipo.tipo;

        n.categoria = new Categoria();
        n.categoria.nome = b.categoria.nome;
        n.categoria.subcategoria = b.categoria.subcategoria;

        n.pagador = new Pagador();
        n.pagador.pagador = b.pagador.pagador;
        n.pagador.beneficiario = b.pagador.beneficiario;

        n.data = new Data();
        n.data.data = b.data.data;
        n.data.recorrencia = b.data.recorrencia;

        n.valor = b.valor;
        n.estornoDeId = b.estornoDeId;
        n.participante = b.participante;

        n.anexos = new ArrayList<>();
        for (String a : b.anexos) {
            n.anexos.add(a);
        }

        return n;
    }
}
