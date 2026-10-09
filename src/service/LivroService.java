package service;

import java.util.ArrayList;
import java.util.List;

import exception.BibliotecaException;
import exception.LivroNaoEncontradoException;
import exception.RegistroDuplicadoException;
import model.Livro;

/** Guarda os livros cadastrados. Recusa titulo repetido. */
public class LivroService {

    /** Quando tiver banco, e aqui que entra o INSERT e o SELECT dos livros. */
    private final List<Livro> livros = new ArrayList<>();

    /** No banco esse id seria gerado sozinho, aqui eu simulo com um contador. */
    private long proximoId = 1L;

    public void cadastrar(Livro livro) throws BibliotecaException {
        if (buscarPorTituloSilencioso(livro.getTitulo()) != null) {
            throw new RegistroDuplicadoException(
                    "Ja existe um livro cadastrado com o titulo \""
                            + livro.getTitulo() + "\".");
        }
        livro.setId(proximoId++);
        livros.add(livro);
    }

    public List<Livro> listarTodos() {
        return livros;
    }

    public Livro buscarPorTitulo(String titulo) throws LivroNaoEncontradoException {
        Livro livro = buscarPorTituloSilencioso(titulo);
        if (livro == null) {
            throw new LivroNaoEncontradoException(titulo);
        }
        return livro;
    }

    private Livro buscarPorTituloSilencioso(String titulo) {
        for (Livro l : livros) {
            if (l.getTitulo().equalsIgnoreCase(titulo.trim())) {
                return l;
            }
        }
        return null;
    }
}
