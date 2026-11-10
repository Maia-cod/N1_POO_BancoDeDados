package BD;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import Model.Emprestimo;
import Model.Livro;
import Model.Pessoa;

/** SQL da tabela emprestimo. A listagem vem sempre junto com a pessoa e o livro dela. */
public class EmprestimoDAO {

    public void inserir(Connection con, Emprestimo emprestimo) throws SQLException {
        String sql = "INSERT INTO emprestimo (pessoa_id, livro_id, data_emprestimo, data_prevista, data_devolucao) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, emprestimo.getPessoa().getId());
            ps.setLong(2, emprestimo.getLivro().getId());
            ps.setDate(3, Date.valueOf(emprestimo.getDataEmprestimo()));
            ps.setDate(4, Date.valueOf(emprestimo.getDataPrevistaDevolucao()));
            if (emprestimo.getDataDevolucao() == null) {
                ps.setNull(5, Types.DATE);
            } else {
                ps.setDate(5, Date.valueOf(emprestimo.getDataDevolucao()));
            }
            ps.executeUpdate();
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    emprestimo.setId(chaves.getLong(1));
                }
            }
        }
    }

    public List<Emprestimo> listarTodos(Connection con) throws SQLException {
        String sql = "SELECT e.id AS emprestimo_id, e.data_emprestimo, e.data_prevista, e.data_devolucao, "
                + "p.id AS pessoa_id, p.tipo, p.nome, p.documento, p.curso, p.ano_entrada, p.departamento, "
                + "l.id AS livro_id, l.titulo, l.autor, l.ano, l.exemplares, l.exemplares_disponiveis "
                + "FROM emprestimo e "
                + "JOIN pessoa p ON p.id = e.pessoa_id "
                + "JOIN livro l ON l.id = e.livro_id "
                + "ORDER BY e.id";
        List<Emprestimo> emprestimos = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pessoa pessoa = PessoaDAO.montarPessoa(rs, rs.getLong("pessoa_id"));
                Livro livro = LivroDAO.montarLivro(rs, rs.getLong("livro_id"));
                emprestimos.add(new Emprestimo(rs.getLong("emprestimo_id"), pessoa, livro,
                        rs.getDate("data_emprestimo").toLocalDate(),
                        rs.getDate("data_prevista").toLocalDate(),
                        rs.getDate("data_devolucao") == null ? null
                                : rs.getDate("data_devolucao").toLocalDate()));
            }
        }
        return emprestimos;
    }

    /** Devolve o emprestimo em aberto dessa pessoa com esse livro, ou null se nao existir. */
    public Emprestimo buscarAtivo(Connection con, Pessoa pessoa, Livro livro) throws SQLException {
        String sql = "SELECT id, data_emprestimo, data_prevista, data_devolucao "
                + "FROM emprestimo WHERE pessoa_id = ? AND livro_id = ? AND data_devolucao IS NULL";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, pessoa.getId());
            ps.setLong(2, livro.getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return new Emprestimo(rs.getLong("id"), pessoa, livro,
                        rs.getDate("data_emprestimo").toLocalDate(),
                        rs.getDate("data_prevista").toLocalDate(),
                        rs.getDate("data_devolucao") == null ? null
                                : rs.getDate("data_devolucao").toLocalDate());
            }
        }
    }

    /** Quantos emprestimos em aberto a pessoa tem agora. */
    public int contarAtivos(Connection con, Pessoa pessoa) throws SQLException {
        String sql = "SELECT COUNT(*) FROM emprestimo WHERE pessoa_id = ? AND data_devolucao IS NULL";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, pessoa.getId());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public void registrarDevolucao(Connection con, Emprestimo emprestimo) throws SQLException {
        String sql = "UPDATE emprestimo SET data_devolucao = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(emprestimo.getDataDevolucao()));
            ps.setLong(2, emprestimo.getId());
            ps.executeUpdate();
        }
    }
}
