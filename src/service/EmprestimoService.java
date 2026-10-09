package service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import exception.BibliotecaException;
import exception.EmprestimoNaoEncontradoException;
import exception.LimiteEmprestimosExcedidoException;
import interfaces.Emprestavel;
import model.Emprestimo;
import model.Livro;
import model.Pessoa;

/** Regras do emprestimo. O mesmo metodo serve para Aluno e Professor, sem perguntar o tipo. */
public class EmprestimoService {

    /** Quando tiver banco, e aqui que entra o INSERT e o SELECT dos emprestimos. */
    private final List<Emprestimo> emprestimos = new ArrayList<>();

    /** No banco esse id seria gerado sozinho, aqui eu simulo com um contador. */
    private long proximoId = 1L;

    private final PessoaService pessoaService;
    private final LivroService livroService;

    public EmprestimoService(PessoaService pessoaService, LivroService livroService) {
        this.pessoaService = pessoaService;
        this.livroService = livroService;
    }

    public void realizarEmprestimo(String documento, String tituloLivro, int dias)
            throws BibliotecaException {

        Pessoa pessoa = pessoaService.buscarPorDocumento(documento);
        Livro livro = livroService.buscarPorTitulo(tituloLivro);

        Emprestavel emprestante = pessoa;

        if (emprestante.getEmprestimosAtivos() >= emprestante.getLimiteDeEmprestimos()) {
            throw new LimiteEmprestimosExcedidoException(
                    pessoa.getNome(), emprestante.getLimiteDeEmprestimos());
        }

        Emprestimo emprestimo = new Emprestimo(pessoa, livro, dias);

        livro.emprestar();
        pessoa.registrarEmprestimo();

        emprestimo.setId(proximoId++);
        emprestimos.add(emprestimo);
    }

    public Emprestimo devolverLivro(String documento, String tituloLivro)
            throws BibliotecaException {

        Emprestimo emprestimo = buscarEmprestimoAtivo(documento, tituloLivro);
        emprestimo.registrarDevolucao(LocalDate.now());
        return emprestimo;
    }

    public List<Emprestimo> listarTodos() {
        return emprestimos;
    }

    private Emprestimo buscarEmprestimoAtivo(String documento, String tituloLivro)
            throws BibliotecaException {

        String doc = documento.trim();
        String titulo = tituloLivro.trim();

        for (Emprestimo e : emprestimos) {
            if (e.isAtivo()
                    && e.getPessoa().getDocumento().equalsIgnoreCase(doc)
                    && e.getLivro().getTitulo().equalsIgnoreCase(titulo)) {
                return e;
            }
        }
        throw new EmprestimoNaoEncontradoException(doc, titulo);
    }
}
