package model;

/** Subclasse de Pessoa. Aqui entram curso, ano de entrada e o limite de 3 livros. */
public class Aluno extends Pessoa {

    public static final int LIMITE_DE_EMPRESTIMOS = 3;

    private final String curso;
    private final int anoDeEntrada;

    public Aluno(String nome, String ra, String curso, int anoDeEntrada) {
        super(nome, ra);
        this.curso = curso;
        this.anoDeEntrada = anoDeEntrada;
    }

    @Override
    public String getTipoDocumento() {
        return "RA";
    }

    @Override
    public int getLimiteDeEmprestimos() {
        return LIMITE_DE_EMPRESTIMOS;
    }

    @Override
    public String exibirDados() {
        return "Aluno | Curso: " + curso + " | Ano de entrada: " + anoDeEntrada
                + " | " + super.exibirDados();
    }

    public String getCurso() {
        return curso;
    }

    public int getAnoDeEntrada() {
        return anoDeEntrada;
    }
}
