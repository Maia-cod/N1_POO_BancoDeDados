package exception;

/** Classe mae das excecoes. Como todas herdam dela, o Main precisa de um catch so. */
public class BibliotecaException extends Exception {

    private static final long serialVersionUID = 1L;

    public BibliotecaException(String mensagem) {
        super(mensagem);
    }
}
