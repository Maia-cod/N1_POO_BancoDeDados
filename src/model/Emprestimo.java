package model;

import java.time.LocalDate;

/** Guarda quem pegou o livro, qual livro foi e as datas. Data de devolucao vazia = emprestimo aberto. */
public class Emprestimo {

    /** Fica separado do resto porque no banco esse e o id gerado automaticamente. */
    private Long id;
    private final Pessoa pessoa;
    private final Livro livro;
    private final LocalDate dataEmprestimo;
    private final LocalDate dataPrevistaDevolucao;
    /** Fica vazia enquanto o emprestimo esta aberto. E esse campo que vira o WHERE no banco. */
    private LocalDate dataDevolucao;

    public Emprestimo(Pessoa pessoa, Livro livro, int diasEmprestimo) {
        if (diasEmprestimo < 1) {
            throw new IllegalArgumentException("O prazo do emprestimo deve ser de pelo menos 1 dia.");
        }
        this.pessoa = pessoa;
        this.livro = livro;
        this.dataEmprestimo = LocalDate.now();
        this.dataPrevistaDevolucao = dataEmprestimo.plusDays(diasEmprestimo);
    }

    public void registrarDevolucao(LocalDate data) {
        this.dataDevolucao = data;
        livro.devolver();
        pessoa.registrarDevolucao();
    }

    public boolean isAtivo() {
        return dataDevolucao == null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public Livro getLivro() {
        return livro;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    @Override
    public String toString() {
        String situacao = isAtivo()
                ? "em aberto (previsao " + dataPrevistaDevolucao + ")"
                : "devolvido em " + dataDevolucao;
        return String.format("#%s | %s pegou \"%s\" | %s",
                id == null ? "-" : id, pessoa.getNome(), livro.getTitulo(), situacao);
    }
}
