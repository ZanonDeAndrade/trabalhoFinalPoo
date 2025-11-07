public class Main {
    public static void main(String[] args) {
        UsuarioIndividual ana = new UsuarioIndividual("u1", "Ana", "ana@example.com", "123");
        UsuarioIndividual bruno = new UsuarioIndividual("u2", "Bruno", "bruno@example.com", "123");

        Grupo familia = new Grupo("g1", "Familia", "familia@example.com", "secret", 5);

        familia.adicionarMembro(ana, Papel.PROPRIETARIO);
        familia.adicionarMembro(bruno, Papel.OBSERVADOR);

        System.out.println("Bruno pode GERENCIAR_MEMBROS? " + familia.pode(bruno, Permissao.GERENCIAR_MEMBROS));
        System.out.println("Ana pode GERENCIAR_MEMBROS? " + familia.pode(ana, Permissao.GERENCIAR_MEMBROS));
        System.out.println("Bruno pode VER_LANCAMENTOS? " + familia.pode(bruno, Permissao.VER_LANCAMENTOS));
    }
}
