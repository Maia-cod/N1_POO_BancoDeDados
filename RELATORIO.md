# Sistema de Gestão de Biblioteca

**Disciplina:** Programação Orientada a Objetos — N1  **Aluno:** Isabella

Este é um sistema de biblioteca que roda pelo console. Ele cadastra alunos, professores e livros, e depois acompanha quem pegou cada livro e por quantos dias.

## Requisitos

Antes de começar eu listei o que o sistema precisava fazer:

1. Cadastrar aluno, professor e livro.
2. Listar as pessoas, os livros e os empréstimos.
3. Registrar empréstimo e devolução.
4. Não deixar cadastrar duas vezes o mesmo documento ou o mesmo título.

E as regras de negócio são essas:

- Aluno pode ficar com no máximo **3** livros emprestados. Professor pode com **8**.
- O livro só sai da biblioteca se ainda tiver exemplar.
- O prazo do empréstimo tem que ser de pelo menos 1 dia.
- O nome precisa ter 3 letras no mínimo.

Teve uma coisa que eu me preocupei em fazer: nenhuma dessas regras pode fechar o programa. Quando o sistema recusa uma operação, ele só escreve o motivo na tela e volta para o menu.

## O que o sistema faz

O menu tem nove opções. O sistema começa vazio, então os dados vão sendo cadastrados durante o uso.

- **1 e 2** — cadastro de aluno e de professor.
- **3** — cadastro de livro (título, autor, ano e quantos exemplares existem).
- **4 e 5** — listagem das pessoas e dos livros.
- **6** — empréstimo: documento da pessoa, título do livro e prazo.
- **7** — devolução: documento e título. Eu peço os dois porque o título acha o livro no acervo, mas não acha o empréstimo — o mesmo livro pode estar emprestado para várias pessoas ao mesmo tempo.
- **8** — listagem dos empréstimos, com a data prevista e a data em que voltou.
- **9** — sair.

## Onde estão os conceitos da POO

| Conceito | Classes | Como aparece no código |
| --- | --- | --- |
| **Herança** | `Aluno` e `Professor` estendem `Pessoa` | Deixei na `Pessoa` o que as duas têm de igual: nome, documento e livros emprestados. Cada uma põe o que é seu — `Aluno` põe curso e ano de entrada, `Professor` põe departamento. |
| **Polimorfismo** | `Aluno`, `Professor` e `EmprestimoService` | As duas subclasses sobrescrevem `getLimiteDeEmprestimos()` (3 e 8), `getTipoDocumento()` ("RA" e "MATRICULA") e `exibirDados()`. O `EmprestimoService` chama os três sem perguntar de que tipo é a pessoa. |
| **Interface** | `Emprestavel`, implementada por `Pessoa` | Ela diz o que é preciso ter para retirar livros. Como quem implementa é a `Pessoa`, o serviço recebe a pessoa pelo contrato, não pela classe concreta. |
| **Abstração** | `Pessoa` | É abstrata, e `getTipoDocumento()` e `getLimiteDeEmprestimos()` ficam sem implementação. Isso obriga cada subclasse a dizer qual é a regra dela. |
| **Encapsulamento** | `Livro`, `Pessoa` e `Emprestimo` | Os atributos são privados. O contador de exemplares do `Livro` só muda dentro de `emprestar()` e `devolver()`, e o de livros emprestados da `Pessoa` só dentro de `registrarEmprestimo()` e `registrarDevolucao()`. |
| **Exceções** | `BibliotecaException` e 6 subclasses | Toda regra recusada lança uma exceção que herda de `BibliotecaException`, então o `Main` trata todas com um `catch` só. |

O lugar onde o polimorfismo aparece de verdade é o empréstimo. A regra do limite está escrita uma única vez e vale pros dois tipos:

```java
Emprestavel emprestante = pessoa;

if (emprestante.getEmprestimosAtivos() >= emprestante.getLimiteDeEmprestimos()) {
    throw new LimiteEmprestimosExcedidoException(
            pessoa.getNome(), emprestante.getLimiteDeEmprestimos());
}
```

Não existe nenhum `if` perguntando se a pessoa é aluna ou professora. Quem devolve 3 ou 8 é a subclasse, no momento da chamada. Por isso a mesma situação é recusada pra uma pessoa e aceita pra outra:

```
Aluno | Curso: ads | Ano de entrada: 2025 | Ana Souza | RA: 111 | emprestimos abertos: 3 | limite: 3
Professor | Departamento: Matematica | Bruno Lima | MATRICULA: FUNC-77 | emprestimos abertos: 4 | limite: 8
```

As duas linhas saem do mesmo método `exibirDados()`, que está na `Pessoa`. O que muda é só o limite, porque ele vem do método abstrato.

As exceções são `PessoaNaoEncontradaException`, `LivroNaoEncontradoException`, `EmprestimoNaoEncontradoException`, `RegistroDuplicadoException`, `LivroIndisponivelException` e `LimiteEmprestimosExcedidoException`. Todas estendem `BibliotecaException`, que é verificada, então o compilador me obriga a usar `try` e `catch`. Nenhuma delas fecha o programa:

```
Operacao recusada: Bruno Lima ja tem emprestimos em aberto e nao pode passar do limite de 8 livros.
Dado invalido: O livro deve ter pelo menos 1 exemplar.
```

## Pensando no banco de dados

O professor pediu para já deixar o sistema preparado pro banco, então separei em três camadas: o `Main` só cuida da tela, o `service` guarda as regras e os dados, e o `model` guarda as entidades.

Cada service tem a sua lista — as pessoas, os livros, os empréstimos — e nenhuma outra camada enxerga essas listas. É aí que entraria o `INSERT` e o `SELECT` depois, sem precisar mudar os métodos públicos nem o menu.

Algumas decisões já ficaram pensando nisso:

- As três entidades têm `Long id`, que é o tipo da chave primária. O `id` é o único atributo com setter, porque é o banco que preenche.
- Cada atributo que vai virar coluna tem o seu getter (`getCurso`, `getDataEmprestimo`...). É por eles que o objeto é montado de novo quando vem do banco.
- A `dataDevolucao` fica vazia enquanto o empréstimo está aberto. No banco isso vira um `WHERE data_devolucao IS NULL`.
- A devolução procura por documento **e** por título, que é a chave composta do próprio empréstimo.
