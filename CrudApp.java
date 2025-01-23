import java.sql.*;
import java.util.Scanner;

public class CrudApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            Class.forName("com.mysql.jdbc.Driver");

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db5","root","password");
                                      //use your database password in place of password & database name instead of db5
            System.out.println("Connected to the database successfully!");

            int choice;
            do {
                System.out.println("\n--- Employee Management System ---");
                System.out.println("1. Create Record");
                System.out.println("2. Read Records");
                System.out.println("3. Update Record");
                System.out.println("4. Delete Record");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        createRecord(connection, scanner);
                        break;
                    case 2:
                        readRecords(connection);
                        break;
                    case 3:
                        updateRecord(connection, scanner);
                        break;
                    case 4:
                        deleteRecord(connection, scanner);
                        break;
                    case 5:
                        System.out.println("Exiting application...");
                        break;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            } while (choice != 5);

            connection.close();
            scanner.close();

        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Database error!");
            e.printStackTrace();
        }
    }

    private static void createRecord(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter name: ");
        String name = scanner.next();
        System.out.print("Enter age: ");
        int age = scanner.nextInt();

        String sql = "INSERT INTO users (name, age) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, name);
        preparedStatement.setInt(2, age);

        int rowsInserted = preparedStatement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("Record created successfully!");
        }
    }

    private static void readRecords(Connection connection) throws SQLException {
        String sql = "SELECT * FROM users";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        System.out.println("\n--- Records ---");
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            int age = resultSet.getInt("age");
            System.out.println("ID: " + id + ", Name: " + name + ", Age: " + age);
        }
    }

    private static void updateRecord(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter ID of the record to update: ");
        int id = scanner.nextInt();
        System.out.print("Enter new name: ");
        String name = scanner.next();
        System.out.print("Enter new age: ");
        int age = scanner.nextInt();

        String sql = "UPDATE users SET name = ?, age = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, name);
        preparedStatement.setInt(2, age);
        preparedStatement.setInt(3, id);

        int rowsUpdated = preparedStatement.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Record updated successfully!");
        } else {
            System.out.println("Record not found.");
        }
    }

    private static void deleteRecord(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter ID of the record to delete: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM users WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);

        int rowsDeleted = preparedStatement.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("Record deleted successfully!");
        } else {
            System.out.println("Record not found.");
        }
    }
}