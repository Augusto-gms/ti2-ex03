package dao;

import model.Carro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarroDAO {
    private Connection connection;
    private int maxId = 0;

    private static final String URL = "jdbc:postgresql://localhost:5432/";
    private static final String USUARIO = "augusto";
    private static final String SENHA = "123";

    public CarroDAO() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(URL + "postgres", USUARIO, SENHA);
            criarTabelaSeNaoExistir();
            carregarMaxId();
        } catch (ClassNotFoundException e) {
            System.out.println("Driver do PostgreSQL não encontrado!");
            e.printStackTrace();
            throw new SQLException("Driver JDBC não encontrado");
        }
    }

    private void criarTabelaSeNaoExistir() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS carros (" +
                     "id SERIAL PRIMARY KEY, " +
                     "modelo VARCHAR(100) NOT NULL, " +
                     "marca VARCHAR(100) NOT NULL, " +
                     "tipo VARCHAR(50) NOT NULL)";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    private void carregarMaxId() throws SQLException {
        String sql = "SELECT COALESCE(MAX(id), 0) as max_id FROM carros";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                this.maxId = rs.getInt("max_id");
            }
        }
    }

    public int getMaxId() {
        return maxId;
    }

    public void add(Carro carro) {
        String sql = "INSERT INTO carros (modelo, marca, tipo) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, carro.getModelo());
            pstmt.setString(2, carro.getMarca());
            pstmt.setString(3, carro.getTipo());
            
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    carro.setId(rs.getInt(1));
                    this.maxId = Math.max(carro.getId(), this.maxId);
                }
            }
        } catch (SQLException e) {
            System.out.println("ERRO ao gravar o carro '" + carro.getModelo() + "' no banco!");
            e.printStackTrace();
        }
    }

    public Carro get(int id) {
        String sql = "SELECT * FROM carros WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Carro(
                        rs.getInt("id"),
                        rs.getString("modelo"),
                        rs.getString("marca"),
                        rs.getString("tipo")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("ERRO ao buscar carro com id " + id);
            e.printStackTrace();
        }
        return null;
    }

    public void update(Carro carro) {
        String sql = "UPDATE carros SET modelo = ?, marca = ?, tipo = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, carro.getModelo());
            pstmt.setString(2, carro.getMarca());
            pstmt.setString(3, carro.getTipo());
            pstmt.setInt(4, carro.getId());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("ERRO ao atualizar carro id " + carro.getId());
            e.printStackTrace();
        }
    }

    public void remove(Carro carro) {
        String sql = "DELETE FROM carros WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, carro.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("ERRO ao remover carro id " + carro.getId());
            e.printStackTrace();
        }
    }

    public List<Carro> getAll() {
        List<Carro> carros = new ArrayList<>();
        String sql = "SELECT * FROM carros ORDER BY id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                carros.add(new Carro(
                    rs.getInt("id"),
                    rs.getString("modelo"),
                    rs.getString("marca"),
                    rs.getString("tipo")
                ));
            }
        } catch (SQLException e) {
            System.out.println("ERRO ao listar todos os carros");
            e.printStackTrace();
        }
        return carros;
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
