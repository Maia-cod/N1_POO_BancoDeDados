package exception;

/** A pessoa ja esta com livros emprestados demais para o limite dela. */
public class LimiteEmprestimosExcedidoException extends BibliotecaException {

    private static final long serialVersionUID = 1L;

    public LimiteEmprestimosExcedidoException(String nome, int limite) {
        super(nome + " ja tem emprestimos em aberto e nao pode passar do limite de " + limite + " livros.");
    }
}
