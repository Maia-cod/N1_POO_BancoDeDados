package BD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Livro;

/** SQL da tabela livro. Quem abre e fecha a conexao e o Service, o DAO so usa. */
public class LivroDAO {

    public void inserir(Connection con, Livro livro) throws SQLException {
        String sql = "INSERT INTO livro (titulo, autor, ano, exemplares, exemplares_disponiveis) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, livro.getTitulo());
            ps.setString(2, livro.getAutor());
            ps.setInt(3, livro.getAno());
            ps.setInt(4, livro.getExemplares());
            ps.setInt(5, livro.getExemplaresDisponiveis());
            ps.executeUpdate();
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    livro.setId(chaves.getLong(1));
                }
            }
        }
    }

    public List<Livro> listarTodos(Connection con) throws SQLException {
        String sql = "SELECT id, titulo, autor, ano, exemplares, exemplares_disponiveis "
                + "FROM livro ORDER BY titulo";
        List<Livro> livros = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                livros.add(montarLivro(rs, rs.getLong("id")));
            }
        }
        return livros;
    }

    /** Devolve o livro com esse titulo, ou null se nao existir. */
    public Livro buscarPorTitulo(Connection con, String titulo) throws SQLException {
        String sql = "SELECT id, titulo, autor, ano, exemplares, exemplares_disponiveis "
                + "FROM livro WHERE titulo = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, titulo.trim());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? montarLivro(rs, rs.getLong("id")) : null;
            }
        }
    }

    /** Grava a contagem de exemplares depois que o livro.emprestar() ou devolver() mudar em memoria. */
    public void atualizarExemplaresDisponiveis(Connection con, Livro livro) throws SQLException {
        String sql = "UPDATE livro SET exemplares_disponiveis = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, livro.getExemplaresDisponiveis());
            ps.setLong(2, livro.getId());
            ps.executeUpdate();
        }
    }

    /** Converte a linha do SELECT em objeto Livro. */
    static Livro montarLivro(ResultSet rs, long id) throws SQLException {
        return new Livro(id, rs.getString("titulo"), rs.getString("autor"),
                rs.getInt("ano"), rs.getInt("exemplares"), rs.getInt("exemplares_disponiveis"));
    }
}
