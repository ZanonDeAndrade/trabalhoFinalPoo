public abstract class Usuario {
    protected String id;
    protected String nome;
    protected String email;
    protected String senha;

    public Usuario(String id, String nome, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }

    public abstract java.util.EnumSet<Permissao> getPermissoes();

    public boolean pode(Permissao permissao) {
        return getPermissoes().contains(permissao);
    }
}
