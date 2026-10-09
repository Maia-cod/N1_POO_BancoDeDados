package model;

/** Subclasse de Pessoa tambem. O que e so dele e o departamento e o limite de 8 livros. */
public class Professor extends Pessoa {

    public static final int LIMITE_DE_EMPRESTIMOS = 8;

    private final String departamento;

    public Professor(String nome, String matricula, String departamento) {
        super(nome, matricula);
        this.departamento = departamento;
    }

    @Override
    public String getTipoDocumento() {
        return "MATRICULA";
    }

    @Override
    public int getLimiteDeEmprestimos() {
        return LIMITE_DE_EMPRESTIMOS;
    }

    @Override
    public String exibirDados() {
        return "Professor | Departamento: " + departamento
                + " | " + super.exibirDados();
    }

    public String getDepartamento() {
        return departamento;
    }
}
