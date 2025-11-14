public abstract class ContaFinanceira implements IConta {

    protected String id;
    protected String nome;
    protected double saldo;

    public ContaFinanceira(String id, String nome) {
        this.id = id;
        this.nome = nome;
        this.saldo = 0.0;
    }

    public void depositar(double valor) {
        if (valor > 0) {
            saldo += valor;
        }
    }

    public double getSaldo() {
        return saldo;
    }

    public String toString() {
        return nome + " | Saldo: R$ " + saldo;
    }
}
