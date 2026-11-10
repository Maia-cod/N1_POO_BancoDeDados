package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import BD.ConexaoBD;
import BD.EmprestimoDAO;
import BD.LivroDAO;
import Model.Emprestimo;
import Model.Livro;
import Model.Pessoa;
import exception.BibliotecaException;
import exception.EmprestimoNaoEncontradoException;
import exception.LimiteEmprestimosExcedidoException;

/**
 * Regras do emprestimo. O mesmo metodo serve para Aluno e Professor, sem
 * perguntar o tipo.
 */
public class EmprestimoService {

    private final EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
    private final LivroDAO livroDAO = new LivroDAO();

    private final PessoaService pessoaService;
    private final LivroService livroService;

    public EmprestimoService(PessoaService pessoaService, LivroService livroService) {
        this.pessoaService = pessoaService;
        this.livroService = livroService;
    }

    /**
     * Emprestar grava em duas tabelas (livro e emprestimo), entao roda dentro de
     * uma transacao.
     */
    public void realizarEmprestimo(String documento, String tituloLivro, int dias)
            throws BibliotecaException {

        try (Connection con = ConexaoBD.conectar()) {
            Pessoa pessoa = pessoaService.buscarPorDocumento(documento);
            Livro livro = livroService.buscarPorTitulo(tituloLivro);

            if (emprestimoDAO.contarAtivos(con, pessoa) >= pessoa.getLimiteDeEmprestimos()) {
                throw new LimiteEmprestimosExcedidoException(
                        pessoa.getNome(), pessoa.getLimiteDeEmprestimos());
            }

            Emprestimo emprestimo = new Emprestimo(pessoa, livro, dias);
            livro.emprestar();

            con.setAutoCommit(false);
            try {
                livroDAO.atualizarExemplaresDisponiveis(con, livro);
                emprestimoDAO.inserir(con, emprestimo);
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new BibliotecaException("Erro ao acessar o banco: " + e.getMessage());
        }
    }

    /**
     * O contador nao fica na Pessoa: os emprestimos em aberto no banco sao a unica
     * fonte da verdade.
     */
    public int contarEmprestimosAtivos(Pessoa pessoa) throws BibliotecaException {
        try (Connection con = ConexaoBD.conectar()) {
            return emprestimoDAO.contarAtivos(con, pessoa);
        } catch (SQLException e) {
            throw new BibliotecaException("Erro ao acessar o banco: " + e.getMessage());
        }
    }

    /** Devolver tambem mexe em duas tabelas, entao tambem roda em transacao. */
    public Emprestimo devolverLivro(String documento, String tituloLivro)
            throws BibliotecaException {

        try (Connection con = ConexaoBD.conectar()) {
            Pessoa pessoa = pessoaService.buscarPorDocumento(documento);
            Livro livro = livroService.buscarPorTitulo(tituloLivro);

            Emprestimo emprestimo = emprestimoDAO.buscarAtivo(con, pessoa, livro);
            if (emprestimo == null) {
                throw new EmprestimoNaoEncontradoException(documento, tituloLivro);
            }

            emprestimo.registrarDevolucao(LocalDate.now());

            con.setAutoCommit(false);
            try {
                emprestimoDAO.registrarDevolucao(con, emprestimo);
                livroDAO.atualizarExemplaresDisponiveis(con, emprestimo.getLivro());
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
            return emprestimo;
        } catch (SQLException e) {
            throw new BibliotecaException("Erro ao acessar o banco: " + e.getMessage());
        }
    }

    public List<Emprestimo> listarTodos() throws BibliotecaException {
        try (Connection con = ConexaoBD.conectar()) {
            return emprestimoDAO.listarTodos(con);
        } catch (SQLException e) {
            throw new BibliotecaException("Erro ao acessar o banco: " + e.getMessage());
        }
    }
}
