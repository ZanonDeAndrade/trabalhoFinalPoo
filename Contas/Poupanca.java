public class Poupanca extends ContaFinanceira {

    public CofrinhoVirtual(String id, String nome) {
        super(id, nome);
    }

    public boolean sacar(double valor) {
        if (valor > 0 && valor <= saldo) {
            saldo -= valor;
            return true;
        }
        return false;
    }
}
