package dataaccess;

import exception.ResponseException;
import model.AuthData;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static java.sql.Types.NULL;

public class MySQLAuthDAO implements AuthDAO{

    public MySQLAuthDAO() throws ResponseException {
        DatabaseManager.configureDatabase(createStatements);
    }


    public AuthData createAuth(String username) throws ResponseException {
        if (username == null) { throw new ResponseException(500, "Error: Null username");}

        try {
            String authToken = UUID.randomUUID().toString();
            var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
            executeUpdate(statement, authToken, username);
            return new AuthData(authToken, username);
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Error: unable to read data: %s", e.getMessage()));
        }
    }

    public Collection<AuthData> listAuths() throws ResponseException {
        var result = new ArrayList<AuthData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM auth";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readAuth(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    public AuthData getAuth(String authToken) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM auth WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public void deleteAuth(String authToken) throws ResponseException {
        var statement = "DELETE FROM auth WHERE authToken=?";
        executeUpdate(statement, authToken);
    }

    public void deleteAllAuths() throws ResponseException {
        var statement = "TRUNCATE auth";
        executeUpdate(statement);
    }

    private AuthData readAuth(ResultSet rs) throws SQLException {
        var authToken = rs.getString("authToken");
        var username = rs.getString("username");
        return new AuthData(authToken, username);
    }


    private String executeUpdate(String statement, Object... params) throws ResponseException {
        try {
            DatabaseManager.updateHelper(statement, params );
            try {
                return getAuth(params[0].toString()).username();
            } catch (Exception e ) {
                return null;
            }

        } catch (Exception e) {
            throw new ResponseException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auth (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`),
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };


}
