package BD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import Model.Aluno;
import Model.Pessoa;
import Model.Professor;

/** SQL da tabela pessoa. Quem abre e fecha a conexao e o Service, o DAO so usa. */
public class PessoaDAO {

    public void inserir(Connection con, Pessoa pessoa) throws SQLException {
        String sql = "INSERT INTO pessoa (tipo, nome, documento, curso, ano_entrada, departamento) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, pessoa instanceof Aluno ? "ALUNO" : "PROFESSOR");
            ps.setString(2, pessoa.getNome());
            ps.setString(3, pessoa.getDocumento());
            if (pessoa instanceof Aluno) {
                Aluno aluno = (Aluno) pessoa;
                ps.setString(4, aluno.getCurso());
                ps.setInt(5, aluno.getAnoDeEntrada());
                ps.setNull(6, Types.VARCHAR);
            } else {
                Professor professor = (Professor) pessoa;
                ps.setNull(4, Types.VARCHAR);
                ps.setNull(5, Types.INTEGER);
                ps.setString(6, professor.getDepartamento());
            }
            ps.executeUpdate();
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    pessoa.setId(chaves.getLong(1));
                }
            }
        }
    }

    public List<Pessoa> listarTodos(Connection con) throws SQLException {
        String sql = "SELECT id, tipo, nome, documento, curso, ano_entrada, departamento "
                + "FROM pessoa ORDER BY nome";
        List<Pessoa> pessoas = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                pessoas.add(montarPessoa(rs, rs.getLong("id")));
            }
        }
        return pessoas;
    }

    /** Devolve a pessoa dona do documento, ou null se nao existir. */
    public Pessoa buscarPorDocumento(Connection con, String documento) throws SQLException {
        String sql = "SELECT id, tipo, nome, documento, curso, ano_entrada, departamento "
                + "FROM pessoa WHERE documento = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, documento.trim());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? montarPessoa(rs, rs.getLong("id")) : null;
            }
        }
    }

    /** Converte a linha do SELECT em objeto Aluno ou Professor, conforme a coluna tipo. */
    static Pessoa montarPessoa(ResultSet rs, long id) throws SQLException {
        Pessoa pessoa;
        if ("ALUNO".equals(rs.getString("tipo"))) {
            pessoa = new Aluno(rs.getString("nome"), rs.getString("documento"),
                    rs.getString("curso"), rs.getInt("ano_entrada"));
        } else {
            pessoa = new Professor(rs.getString("nome"), rs.getString("documento"),
                    rs.getString("departamento"));
        }
        pessoa.setId(id);
        return pessoa;
    }
}
