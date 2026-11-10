package exception;

/** Ja existe cadastro com esse mesmo documento ou titulo. */
public class RegistroDuplicadoException extends BibliotecaException {

    private static final long serialVersionUID = 1L;

    public RegistroDuplicadoException(String mensagem) {
        super(mensagem);
    }
}
