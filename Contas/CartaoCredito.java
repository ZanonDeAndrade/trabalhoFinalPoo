public class CartaoCredito extends ContaFinanceira {

    private double limite;
    private double fatura;

    public CartaoCredito(String id, String nome, double limite) {
        super(id, nome);
        this.limite = limite;
        this.fatura = 0.0;
    }

   
    public boolean sacar(double valor) {
        if (valor > 0 && (fatura + valor) <= limite) {
            fatura += valor;
            return true;
        }
        return false;
    }

    public void pagarFatura(double valor) {
        fatura -= valor;
        if (fatura < 0) fatura = 0;
    }

    public double getLimiteDisponivel() {
        return limite - fatura;
    }


    public String toString() {
        return nome + " | Fatura: R$ " + fatura + " | Limite disponível: R$ " + getLimiteDisponivel();
    }
}
