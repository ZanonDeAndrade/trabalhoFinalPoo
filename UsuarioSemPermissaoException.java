public class UsuarioSemPermissaoException extends RuntimeException {
    public UsuarioSemPermissaoException(String mensagem) {
        super(mensagem);
    }
}
