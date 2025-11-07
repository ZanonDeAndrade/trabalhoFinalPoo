public class MembroGrupo {
    private final UsuarioIndividual usuario;
    private Papel papel;

    public MembroGrupo(UsuarioIndividual usuario, Papel papel) {
        this.usuario = usuario;
        this.papel = papel;
    }

    public UsuarioIndividual getUsuario() {
        return usuario;
    }

    public Papel getPapel() {
        return papel;
    }

    public void setPapel(Papel papel) {
        this.papel = papel;
    }
}

