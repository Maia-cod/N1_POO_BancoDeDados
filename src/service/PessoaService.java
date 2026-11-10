package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import BD.ConexaoBD;
import BD.PessoaDAO;
import Model.Pessoa;
import exception.BibliotecaException;
import exception.PessoaNaoEncontradaException;
import exception.RegistroDuplicadoException;

/** Regras das pessoas. Valida o duplicado e manda o DAO gravar no banco. */
public class PessoaService {

    private final PessoaDAO pessoaDAO = new PessoaDAO();

    public void cadastrar(Pessoa pessoa) throws BibliotecaException {
        try (Connection con = ConexaoBD.conectar()) {
            if (pessoaDAO.buscarPorDocumento(con, pessoa.getDocumento()) != null) {
                throw new RegistroDuplicadoException(
                        "Ja existe uma pessoa cadastrada com o documento "
                                + pessoa.getDocumento() + ".");
            }
            pessoaDAO.inserir(con, pessoa);
        } catch (SQLException e) {
            throw new BibliotecaException("Erro ao acessar o banco: " + e.getMessage());
        }
    }

    public List<Pessoa> listarTodos() throws BibliotecaException {
        try (Connection con = ConexaoBD.conectar()) {
            return pessoaDAO.listarTodos(con);
        } catch (SQLException e) {
            throw new BibliotecaException("Erro ao acessar o banco: " + e.getMessage());
        }
    }

    public Pessoa buscarPorDocumento(String documento)
            throws PessoaNaoEncontradaException, BibliotecaException {
        try (Connection con = ConexaoBD.conectar()) {
            Pessoa pessoa = pessoaDAO.buscarPorDocumento(con, documento);
            if (pessoa == null) {
                throw new PessoaNaoEncontradaException(documento);
            }
            return pessoa;
        } catch (SQLException e) {
            throw new BibliotecaException("Erro ao acessar o banco: " + e.getMessage());
        }
    }
}
