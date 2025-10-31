public class Grupo extends Usuario{
    private UsuarioIndividual[] membros;
    private int quantidadeMembros;


    public Grupo(String id, String nome, String email, String senha, int capacidadeMaxima) {
        super(id, nome, email, senha);
        this.membros = new UsuarioIndividual[capacidadeMaxima];
        this.quantidadeMembros = 0;
    }

    public void adicionarMembro(UsuarioIndividual usuario) {
        if (quantidadeMembros < membros.length) {
            membros[quantidadeMembros] = usuario;
            quantidadeMembros++;
        } else {
            System.out.println("Limite máximo");
        }
    }
}
