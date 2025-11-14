public class CarteiraInvestimento extends ContaFinanceira {

    private double rendimentoMensal; 

    public CarteiraInvestimento(String id, String nome, double rendimentoMensal) {
        super(id, nome);
        this.rendimentoMensal = rendimentoMensal;
    }

    @Override
    public boolean sacar(double valor) {
        if (valor > 0 && valor <= saldo) {
            saldo -= valor;
            return true;
        }
        return false;
    }

    public double projetarSaldo(int meses) {
        double futuro = saldo;
        for (int i = 0; i < meses; i++) {
            futuro += futuro * rendimentoMensal;
        }
        return futuro;
    }
}
