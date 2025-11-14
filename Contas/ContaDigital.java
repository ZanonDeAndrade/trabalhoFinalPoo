public class ContaDigital extends ContaFinanceira {

    public ContaDigital(String id, String nome) {
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
