package exception;

/** O livro procurado nao esta no acervo. */
public class LivroNaoEncontradoException extends BibliotecaException {

    private static final long serialVersionUID = 1L;

    public LivroNaoEncontradoException(String titulo) {
        super("Nao existe livro cadastrado com o titulo \"" + titulo + "\".");
    }
}
