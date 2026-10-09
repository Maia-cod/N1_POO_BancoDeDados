package model;

import interfaces.Emprestavel;

/** Classe abstrata que serve de base para Aluno e Professor. Fica aqui o que as duas tem de igual. */
public abstract class Pessoa implements Emprestavel {

    /** Fica separado do resto porque no banco esse e o id gerado automaticamente. */


    
    private Long id;
    private final String nome;
    private final String documento;
    private int emprestimosAtivos;

    protected Pessoa(String nome, String documento) {
        if (nome == null || nome.trim().length() < 3) {
            throw new IllegalArgumentException("O nome deve ter pelo menos 3 caracteres.");
        }
        if (documento == null || documento.trim().isEmpty()) {
            throw new IllegalArgumentException("O documento nao pode ser vazio.");
        }
        this.nome = nome.trim();
        this.documento = documento.trim();
    }

    public abstract String getTipoDocumento();

    @Override
    public abstract int getLimiteDeEmprestimos();

    public void registrarEmprestimo() {
        this.emprestimosAtivos++;
    }

    public void registrarDevolucao() {
        this.emprestimosAtivos--;
    }

    public String getNome() {
        return nome;
    }

    public String getDocumento() {
        return documento;
    }

    public Long getId() {
        return id;
    }

    /** Unico setter do projeto, porque quem preenche o id e o banco. */
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int getEmprestimosAtivos() {
        return emprestimosAtivos;
    }

    public String getIdentificacao() {
        return getTipoDocumento() + ": " + documento;
    }

    public String exibirDados() {
        return String.format("%s | %s | emprestimos abertos: %d | limite: %d",
                nome, getIdentificacao(), emprestimosAtivos,
                getLimiteDeEmprestimos());
    }
}
