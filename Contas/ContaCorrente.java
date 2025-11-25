public class ContaCorrente extends ContaFinanceira {

    private double limite;

    public ContaCorrente(String id, String nome, double limite) {
        super(id, nome);
        this.limite = limite;
    }

   
    public boolean sacar(double valor) {
        double disponivel = saldo + limite;

        if (valor > 0 && valor <= disponivel) {
            saldo -= valor;
            return true;
        }
        return false;
    }

  
    public double getLimiteDisponivel() {
        return (saldo + limite);
    }

    @Override
    public String toString() {
        return nome + " | Saldo: R$ " + saldo + " | Limite: R$ " + limite + " | Disponivel: R$ " + getLimiteDisponivel();
    }
}
