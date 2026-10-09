package model;

import exception.LivroIndisponivelException;

/** Cuida do acervo. Quem conta os exemplares e a propria classe, por dentro de emprestar() e devolver(). */
public class Livro {

    /** Fica separado do resto porque no banco esse e o id gerado automaticamente. */
    private Long id;
    private final String titulo;
    private final String autor;
    private final int ano;
    private final int exemplares;
    private int exemplaresDisponiveis;

    public Livro(String titulo, String autor, int ano, int exemplares) {
        if (titulo == null || titulo.trim().length() < 2) {
            throw new IllegalArgumentException("O titulo deve ter pelo menos 2 caracteres.");
        }
        if (exemplares < 1) {
            throw new IllegalArgumentException("O livro deve ter pelo menos 1 exemplar.");
        }
        this.titulo = titulo.trim();
        this.autor = autor == null ? "Desconhecido" : autor.trim();
        this.ano = ano;
        this.exemplares = exemplares;
        this.exemplaresDisponiveis = exemplares;
    }

    public void emprestar() throws LivroIndisponivelException {
        if (exemplaresDisponiveis == 0) {
            throw new LivroIndisponivelException(titulo);
        }
        exemplaresDisponiveis--;
    }

    public void devolver() {
        if (exemplaresDisponiveis < exemplares) {
            exemplaresDisponiveis++;
        }
    }

    public boolean isDisponivel() {
        return exemplaresDisponiveis > 0;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public int getAno() {
        return ano;
    }

    public int getExemplares() {
        return exemplares;
    }

    public int getExemplaresDisponiveis() {
        return exemplaresDisponiveis;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("#%s | \"%s\" | %s (%d) | exemplares: %d/%d | %s",
                id == null ? "-" : id, titulo, autor, ano,
                exemplaresDisponiveis, exemplares,
                isDisponivel() ? "disponivel" : "indisponivel");
    }
}
