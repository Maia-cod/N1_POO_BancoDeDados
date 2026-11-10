package BD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Ponto unico de conexao com o MySQL do XAMPP. Quem chama e fecha a conexao. */
public class ConexaoBD {

    private static final String URL =
            "jdbc:mysql://localhost:3306/biblioteca?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USUARIO = "root";
    private static final String SENHA = "123456";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
