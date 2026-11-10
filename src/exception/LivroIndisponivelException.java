package exception;

/** O livro existe, mas nao tem exemplar disponivel. */
public class LivroIndisponivelException extends BibliotecaException {

    private static final long serialVersionUID = 1L;

    public LivroIndisponivelException(String titulo) {
        super("Nao ha exemplar disponivel do livro \"" + titulo + "\".");
    }
}
