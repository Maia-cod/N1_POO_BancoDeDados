package interfaces;

/** Interface que diz o que uma pessoa precisa ter para poder pegar livro. */


public interface Emprestavel {

    int getLimiteDeEmprestimos();

    int getEmprestimosAtivos();
}
