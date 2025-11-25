public class UsuarioIndividual extends Usuario {

    public UsuarioIndividual(String id, String nome, String email, String senha) {
        super(id, nome, email, senha);
    }

    @Override
    public java.util.EnumSet<Permissao> getPermissoes() {
        return java.util.EnumSet.of(
                Permissao.VER_SALDO,
                Permissao.VER_LANCAMENTOS,
                Permissao.ADICIONAR_RECEITA,
                Permissao.ADICIONAR_DESPESA,
                Permissao.EDITAR_LANCAMENTO,
                Permissao.EXCLUIR_LANCAMENTO,
                Permissao.GERENCIAR_CONTAS,
                Permissao.GERENCIAR_METAS,
                Permissao.GERAR_RELATORIOS,
                Permissao.CONFIGURAR_ORCAMENTO
        );
    }
}

