package main;
import java.sql.*;
public class GameDatabase {

    private Connection connection;

    public GameDatabase() {
        connect();
        createTableIfNotExists();
    }

    //Stabileste conexiunea la baza de date SQLite
    private void connect(){
        try{
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:game_data.db");
            System.out.println("Database connection established.");
        }catch (ClassNotFoundException | SQLException e){
            System.err.println("Error connecting to database: " + e.getMessage());
        }
    }

    //Creeaza tabelul game_data daca nu exista deja
    private void createTableIfNotExists(){
        String createTableSQL = "CREATE TABLE IF NOT EXISTS game_data (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "level INTEGER NOT NULL," +
                "player_x INTEGER NOT NULL," +
                "player_y INTEGER NOT NULL," +
                "currentHealth INTEGER NOT NULL)";
        try(Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
            System.out.println("Table created or already exists.");
        }catch (SQLException e){
            System.err.println("Error creating table: " + e.getMessage());
        }
    }

    //Salveaza datele jocului in baza de date
    public void saveGameData(int level, int playerX, int playerY, int currentHealth){
        String insertSQL = "INSERT INTO game_data (level, player_x, player_y, currentHealth) VALUES (?, ?, ?, ?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)){
            preparedStatement.setInt(1, level);
            preparedStatement.setInt(2, playerX);
            preparedStatement.setInt(3, playerY);
            preparedStatement.setInt(4, currentHealth);
            preparedStatement.executeUpdate();
            System.out.println("Game data saved successfully.");
        }catch (SQLException e){
            System.err.println("Error saving game data: " + e.getMessage());
        }
    }

    //Incarca ultimele date salvate ale jocului din baza de date
    public int[] loadGameData(){
        String selectSQL = "SELECT * FROM game_data ORDER BY id DESC LIMIT 1";
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectSQL)){
            if(resultSet.next()){
                int level = resultSet.getInt("level");
                int playerX = resultSet.getInt("player_x");
                int playerY = resultSet.getInt("player_y");
                int currentHealth = resultSet.getInt("currentHealth");
                System.out.println("Game data loaded successfully.");
                return new int[]{level, playerX, playerY, currentHealth};
            }else{
                System.out.println("No game data found.");
                return null;
            }
        }catch (SQLException e){
            System.err.println("Error loading game data: " + e.getMessage());
            return null;
        }
    }

    //Inchiderea conexiunii la baza de date
    public void closeConnection(){
        if(connection != null){
            try{
                connection.close();
                System.out.println("Database connection closed.");
            }catch (SQLException e){
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
}
