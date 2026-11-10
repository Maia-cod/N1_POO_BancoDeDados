package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import BD.ConexaoBD;
import BD.LivroDAO;
import Model.Livro;
import exception.BibliotecaException;
import exception.LivroNaoEncontradoException;
import exception.RegistroDuplicadoException;

/**
 * Regras dos livros. Valida o titulo repetido e manda o DAO gravar no banco.
 */
public class LivroService {

    private final LivroDAO livroDAO = new LivroDAO();

    public void cadastrar(Livro livro) throws BibliotecaException {
        try (Connection con = ConexaoBD.conectar()) {
            if (livroDAO.buscarPorTitulo(con, livro.getTitulo()) != null) {
                throw new RegistroDuplicadoException(
                        "Ja existe um livro cadastrado com o titulo \""
                                + livro.getTitulo() + "\".");
            }
            livroDAO.inserir(con, livro);
        } catch (SQLException e) {
            throw new BibliotecaException("Erro ao acessar o banco: " + e.getMessage());
        }
    }

    public List<Livro> listarTodos() throws BibliotecaException {
        try (Connection con = ConexaoBD.conectar()) {
            return livroDAO.listarTodos(con);
        } catch (SQLException e) {
            throw new BibliotecaException("Erro ao acessar o banco: " + e.getMessage());
        }
    }

    public Livro buscarPorTitulo(String titulo)
            throws LivroNaoEncontradoException, BibliotecaException {
        try (Connection con = ConexaoBD.conectar()) {
            Livro livro = livroDAO.buscarPorTitulo(con, titulo);
            if (livro == null) {
                throw new LivroNaoEncontradoException(titulo);
            }
            return livro;
        } catch (SQLException e) {
            throw new BibliotecaException("Erro ao acessar o banco: " + e.getMessage());
        }
    }
}
