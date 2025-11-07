public enum Papel {
    PROPRIETARIO(java.util.EnumSet.allOf(Permissao.class)),
    ADMINISTRADOR(java.util.EnumSet.of(
            Permissao.VER_SALDO,
            Permissao.VER_LANCAMENTOS,
            Permissao.ADICIONAR_RECEITA,
            Permissao.ADICIONAR_DESPESA,
            Permissao.EDITAR_LANCAMENTO,
            Permissao.EXCLUIR_LANCAMENTO,
            Permissao.GERENCIAR_CONTAS,
            Permissao.GERENCIAR_METAS,
            Permissao.GERAR_RELATORIOS,
            Permissao.CONFIGURAR_ORCAMENTO,
            Permissao.GERENCIAR_MEMBROS
    )),
    MEMBRO(java.util.EnumSet.of(
            Permissao.VER_SALDO,
            Permissao.VER_LANCAMENTOS,
            Permissao.ADICIONAR_RECEITA,
            Permissao.ADICIONAR_DESPESA,
            Permissao.EDITAR_LANCAMENTO,
            Permissao.GERAR_RELATORIOS
    )),
    OBSERVADOR(java.util.EnumSet.of(
            Permissao.VER_SALDO,
            Permissao.VER_LANCAMENTOS,
            Permissao.GERAR_RELATORIOS
    ));

    private final java.util.EnumSet<Permissao> permissoes;

    Papel(java.util.EnumSet<Permissao> permissoes) {
        this.permissoes = permissoes;
    }

    public java.util.EnumSet<Permissao> getPermissoes() {
        return java.util.EnumSet.copyOf(permissoes);
    }
}
