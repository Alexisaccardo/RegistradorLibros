import java.sql.*;
import java.util.Scanner;

public class Productos {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        Scanner scanner = new Scanner(System.in);


        System.out.println("¿Deseas registrar, consultar o editar? :");
        String respuesta = scanner.nextLine();

        if (respuesta.equals("registrar")){
            System.out.print("¿Deseas agregar un libro nuevo?: ");
            String registro = scanner.nextLine();
            while (registro.equals("si")){
                System.out.print("Ingresa codigo de registro: ");
                String code = scanner.nextLine();

                System.out.print("Ingresa autor libro: ");
                String author = scanner.nextLine();

                System.out.print("Ingresa edición libro: ");
                String edition = scanner.nextLine();

                System.out.print("Ingresa Nombre libro: ");
                String name = scanner.nextLine();

                if (code.equals("") || author.equals("") || edition.equals("") || name.equals("")) {
                    System.out.println("No se admiten datos vacios.");
                } else {
                    String driver = "com.mysql.cj.jdbc.Driver";
                    String url = "jdbc:mysql://localhost:3306/libreria";
                    String username = "root";
                    String password = "";
                    Books books = new Books(name, author, edition, code);
                    try {
                        Class.forName(driver);
                        Connection connection = DriverManager.getConnection(url, username, password);
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery("SELECT * FROM libros");

                        Insert(books, connection);
                        connection.close();
                        statement.close();
                        resultSet.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print("¿Deseas agregar un libro nuevo?: ");
                registro = scanner.nextLine();
            }

        }else if(respuesta.equals("consultar")){
            System.out.println("Ingrese el codigo a consultar: ");
            String id = scanner.nextLine();

            String driver = "com.mysql.cj.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/libreria";
            String username = "root";
            String password = "";

            try {
                Class.forName(driver);
                Connection connection = DriverManager.getConnection(url, username, password);

                String consultaSQL = "SELECT * FROM libros WHERE codigo = ?";

                PreparedStatement statement = connection.prepareStatement(consultaSQL);
                statement.setString(1, id); // Establecer el valor del parámetro

                // Ejecutar la consulta
                ResultSet resultSet = statement.executeQuery();

                // Procesar el resultado si existe
                if (resultSet.next()) {
                    String codigo = resultSet.getString("codigo");
                    String nombre = resultSet.getString("autor");
                    String autor = resultSet.getString("edicion");
                    String edicion = resultSet.getString("nombre");

                    System.out.println("Estes es el codigo del libro a consultar: " + codigo + "Con nombre "+nombre);

                } else {
                    System.out.println("No se encontró un registro con el codigo especificado.");
                }

                // Cerrar recursos
                resultSet.close();
                statement.close();
                connection.close();

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else if(respuesta.equals("editar")) {
            System.out.print("Ingresa codigo de registro: ");
            String code = scanner.nextLine();

            System.out.print("Ingresa nuevo autor libro: ");
            String author = scanner.nextLine();

            System.out.print("Ingresa nueva edición libro: ");
            String edition = scanner.nextLine();

            System.out.print("Ingresa nuevo Nombre libro: ");
            String name = scanner.nextLine();

            String driver2 = "com.mysql.cj.jdbc.Driver";
            String url2 = "jdbc:mysql://localhost:3306/libreria";
            String username2 = "root";
            String pass2 = "";

            Class.forName(driver2);
            Connection connection2 = DriverManager.getConnection(url2, username2, pass2);

            Statement statement2 = connection2.createStatement();

            String consulta = "UPDATE libros SET autor = ?, edicion = ?, nombre = ? WHERE codigo = ?";
            PreparedStatement preparedStatement = connection2.prepareStatement(consulta);
            preparedStatement.setString(1, author);
            preparedStatement.setString(2, edition);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, code);
            int filasActualizadas = preparedStatement.executeUpdate();
            if (filasActualizadas > 0) {
                System.out.println("Libro actualizado exitosamente");
            } else {
                System.out.println("No se encontró el registro para actualizar");
            }

            preparedStatement.close();
            connection2.close();

            System.out.println("Desear editar algun otro producto? ");
            respuesta = scanner.nextLine();


        }
    }
    public static void Insert(Books books, Connection connection){

        try {
            // Sentencia INSERT
            String sql = "INSERT INTO libros (codigo, autor, edicion, nombre) VALUES (?, ?, ?, ?)";

            // Preparar la sentencia
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, books.getCode());
            preparedStatement.setString(2, books.getAuthor());
            preparedStatement.setString(3, books.getEdition());
            preparedStatement.setString(4, books.getName());



            // Ejecutar la sentencia
            int filasAfectadas = preparedStatement.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Libro insertado exitosamente.");
            } else {
                System.out.println("No se pudo insertar el registro.");
            }

            preparedStatement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
}
