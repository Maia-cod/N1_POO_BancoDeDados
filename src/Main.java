import java.util.List;
import java.util.Scanner;

import exception.BibliotecaException;
import model.Aluno;
import model.Emprestimo;
import model.Livro;
import model.Pessoa;
import model.Professor;
import service.EmprestimoService;
import service.LivroService;
import service.PessoaService;

/** Cuida so da tela. O menu e a leitura dos dados ficam aqui, as regras nos services. */
public class Main {

    private static final int OPCAO_SAIR = 9;

    private static final Scanner scanner = new Scanner(System.in);
    private static final PessoaService pessoaService = new PessoaService();
    private static final LivroService livroService = new LivroService();
    private static final EmprestimoService emprestimoService =
            new EmprestimoService(pessoaService, livroService);

    public static void main(String[] args) {
        int opcao = 0;

        while (opcao != OPCAO_SAIR) {
            exibirMenu();
            opcao = lerInteiro("Escolha uma opcao: ");

            try {
                switch (opcao) {
                    case 1:
                        cadastrarAluno();
                        break;
                    case 2:
                        cadastrarProfessor();
                        break;
                    case 3:
                        cadastrarLivro();
                        break;
                    case 4:
                        listarPessoas();
                        break;
                    case 5:
                        listarLivros();
                        break;
                    case 6:
                        realizarEmprestimo();
                        break;
                    case 7:
                        devolverLivro();
                        break;
                    case 8:
                        listarEmprestimos();
                        break;
                    case OPCAO_SAIR:
                        System.out.println("Saindo do sistema...");
                        break;
                    default:
                        System.out.println("Opcao invalida!");
                        break;
                }
            } catch (BibliotecaException e) {
                System.out.println("Operacao recusada: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Dado invalido: " + e.getMessage());
            }
            System.out.println();
        }
        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("========== SISTEMA DE BIBLIOTECA ==========");
        System.out.println("1 - Cadastrar Aluno");
        System.out.println("2 - Cadastrar Professor");
        System.out.println("3 - Cadastrar Livro");
        System.out.println("4 - Listar Pessoas (Alunos/Professores)");
        System.out.println("5 - Listar Livros");
        System.out.println("6 - Realizar Emprestimo");
        System.out.println("7 - Devolver Livro");
        System.out.println("8 - Listar Emprestimos");
        System.out.println(OPCAO_SAIR + " - Sair");
        System.out.println("===========================================");
    }

    private static void cadastrarAluno() throws BibliotecaException {
        System.out.println("--- Cadastro de Aluno ---");
        String nome = lerString("Nome: ");
        String ra = lerString("RA: ");
        String curso = lerString("Curso: ");
        int anoDeEntrada = lerInteiro("Ano de Entrada: ");

        pessoaService.cadastrar(new Aluno(nome, ra, curso, anoDeEntrada));
        System.out.println("Aluno cadastrado com sucesso!");
    }

    private static void cadastrarProfessor() throws BibliotecaException {
        System.out.println("--- Cadastro de Professor ---");
        String nome = lerString("Nome: ");
        String matricula = lerString("Matricula: ");
        String departamento = lerString("Departamento: ");

        pessoaService.cadastrar(new Professor(nome, matricula, departamento));
        System.out.println("Professor cadastrado com sucesso!");
    }

    private static void cadastrarLivro() throws BibliotecaException {
        System.out.println("--- Cadastro de Livro ---");
        String titulo = lerString("Titulo: ");
        String autor = lerString("Autor: ");
        int ano = lerInteiro("Ano de Publicacao: ");
        int exemplares = lerInteiro("Quantidade de Exemplares: ");

        livroService.cadastrar(new Livro(titulo, autor, ano, exemplares));
        System.out.println("Livro cadastrado com sucesso!");
    }

    private static void listarPessoas() {
        System.out.println("--- Pessoas Cadastradas ---");
        List<Pessoa> pessoas = pessoaService.listarTodos();

        if (pessoas.isEmpty()) {
            System.out.println("Nenhuma pessoa cadastrada.");
            return;
        }

        for (Pessoa p : pessoas) {
            System.out.println(p.exibirDados());
        }
    }

    private static void listarLivros() {
        System.out.println("--- Livros Cadastrados ---");
        List<Livro> livros = livroService.listarTodos();

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
            return;
        }

        for (Livro l : livros) {
            System.out.println(l);
        }
    }

    private static void realizarEmprestimo() throws BibliotecaException {
        System.out.println("--- Realizar Emprestimo ---");
        String documento = lerString("Documento (RA/Matricula): ");
        String tituloLivro = lerString("Titulo do Livro: ");
        int dias = lerInteiro("Dias de Emprestimo: ");

        emprestimoService.realizarEmprestimo(documento, tituloLivro, dias);
        System.out.println("Emprestimo realizado com sucesso!");
    }

    private static void devolverLivro() throws BibliotecaException {
        System.out.println("--- Devolver Livro ---");
        String documento = lerString("Documento (RA/Matricula): ");
        String tituloLivro = lerString("Titulo do Livro: ");

        emprestimoService.devolverLivro(documento, tituloLivro);
        System.out.println("Livro devolvido com sucesso!");
    }

    private static void listarEmprestimos() {
        System.out.println("--- Emprestimos Registrados ---");
        List<Emprestimo> emprestimos = emprestimoService.listarTodos();

        if (emprestimos.isEmpty()) {
            System.out.println("Nenhum emprestimo registrado.");
            return;
        }

        for (Emprestimo e : emprestimos) {
            System.out.println(e);
        }
    }

    private static String lerString(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }

    private static int lerInteiro(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                String valor = scanner.nextLine().trim();
                return Integer.parseInt(valor);
            } catch (NumberFormatException e) {
                System.out.println("Valor invalido! Digite um numero inteiro.");
            }
        }
    }
}
