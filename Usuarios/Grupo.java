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

    @Override
    public java.util.EnumSet<Permissao> getPermissoes() {
        java.util.EnumSet<Permissao> acumulado = java.util.EnumSet.noneOf(Permissao.class);
        for (int i = 0; i < quantidadeMembros; i++) {
            MembroGrupo mg = membros[i];
            if (mg != null && mg.getPapel() != null) {
                acumulado.addAll(mg.getPapel().getPermissoes());
            }
        }
        return acumulado;
    }

    public java.util.EnumSet<Permissao> getPermissoes(UsuarioIndividual usuario) {
        for (int i = 0; i < quantidadeMembros; i++) {
            MembroGrupo mg = membros[i];
            if (mg.getUsuario() == usuario) {
                return mg.getPapel().getPermissoes();
            }
        }
        return java.util.EnumSet.noneOf(Permissao.class);
    }

    public boolean pode(UsuarioIndividual usuario, Permissao permissao) {
        return getPermissoes(usuario).contains(permissao);
    }
}
