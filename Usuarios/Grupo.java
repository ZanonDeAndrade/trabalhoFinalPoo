public class Grupo extends Usuario{
    private MembroGrupo[] membros;
    private int quantidadeMembros;

    public Grupo(String id, String nome, String email, String senha, int capacidadeMaxima) {
        super(id, nome, email, senha);
        this.membros = new MembroGrupo[capacidadeMaxima];
        this.quantidadeMembros = 0;
    }

    public boolean adicionarMembro(UsuarioIndividual usuario, Papel papel) {
        if (quantidadeMembros < membros.length) {
            membros[quantidadeMembros] = new MembroGrupo(usuario, papel);
            quantidadeMembros++;
            return true;
        } else {
            System.out.println("Limite maximo de membros atingido.");
            return false;
        }
    }

    public java.util.EnumSet<Permissao> permissoesDo(UsuarioIndividual usuario) {
        for (int i = 0; i < quantidadeMembros; i++) {
            MembroGrupo mg = membros[i];
            if (mg.getUsuario() == usuario) {
                return mg.getPapel().getPermissoes();
            }
        }
        return java.util.EnumSet.noneOf(Permissao.class);
    }

    public boolean pode(UsuarioIndividual usuario, Permissao permissao) {
        return permissoesDo(usuario).contains(permissao);
    }
}
