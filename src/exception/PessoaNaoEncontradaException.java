package exception;

/** A pessoa procurada nao esta cadastrada. */
public class PessoaNaoEncontradaException extends BibliotecaException {

    private static final long serialVersionUID = 1L;

    public PessoaNaoEncontradaException(String documento) {
        super("Nao existe pessoa cadastrada com o documento " + documento + ".");
    }
}
