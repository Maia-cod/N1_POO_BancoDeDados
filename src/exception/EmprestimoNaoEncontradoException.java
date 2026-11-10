package exception;

/** Nao achei emprestimo em aberto dessa pessoa para esse livro. */
public class EmprestimoNaoEncontradoException extends BibliotecaException {

    private static final long serialVersionUID = 1L;

    public EmprestimoNaoEncontradoException(String documento, String titulo) {
        super("Nao existe emprestimo em aberto de " + documento
                + " para o livro: " + titulo);
    }
}
