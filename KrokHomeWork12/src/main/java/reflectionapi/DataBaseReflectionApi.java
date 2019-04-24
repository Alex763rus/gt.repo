/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reflectionapi;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 *
 * @author grigorevap
 */
public class DataBaseReflectionApi {

    private static Connection connection;
    private static Statement stmt;

    public DataBaseReflectionApi() {
        connect();
    }

//    public void save(List<Object> objects) {
//        for (Object object : objects) {
//            save(object);
//        }
//    }
    public void save(Object object) throws IllegalArgumentException, IllegalAccessException {
        Class cls = object.getClass();
        if (!cls.isAnnotationPresent(XTable.class)) {
            throw new RuntimeException("Тут надо доделать!");
        }
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ");
        builder.append(((XTable) cls.getAnnotation(XTable.class)).title());
        builder.append(" (");
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(XField.class)) {
                if (field.isAnnotationPresent(Autoincrement.class) && (int) field.get(object) == 0) {
                    continue;
                }
                builder.append(field.getName()).append(", ");
            }
        }
        builder.setLength(builder.length() - 2);
        builder.append(") VALUES (");

        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(XField.class)) {
                if (field.isAnnotationPresent(Autoincrement.class) && (int) field.get(object) == 0) {
                    continue;
                }
                builder.append("?, ");
            }
        }
        builder.setLength(builder.length() - 2);
        builder.append(");");
        try {
            PreparedStatement ps = connection.prepareStatement(builder.toString());
            int counter = 1;
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(XField.class)) {
                    if (field.isAnnotationPresent(Autoincrement.class) && (int) field.get(object) == 0) {
                        continue;
                    }
                    ps.setObject(counter, field.get(object));
                    counter++;
                }
            }
            ps.executeUpdate();
        } catch (SQLException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    public void createTable(Class cls) {
        if (!cls.isAnnotationPresent(XTable.class)) {
            throw new RuntimeException("Тут надо доделать!");
        }
        HashMap<Class, String> converter = new HashMap();
        converter.put(String.class, "TEXT");
        converter.put(int.class, "INTEGER");
        converter.put(float.class, "FLOAT");

        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ");
        builder.append(((XTable) cls.getAnnotation(XTable.class)).title());
        builder.append(" (");
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(XField.class)) {
                builder.append(field.getName()).append(" ");
                builder.append(converter.get(field.getType()));
                if (field.isAnnotationPresent(PrimaryKey.class)) {
                    builder.append(" PRIMARY KEY ");
                }
                if (field.isAnnotationPresent(Autoincrement.class)) {
                    builder.append(" AUTOINCREMENT ");
                }
                builder.append(", ");
            }
        }
        builder.setLength(builder.length() - 2);
        builder.append(");");
        try {
            stmt.executeUpdate(builder.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void dropTable(Class cls) {
        if (!cls.isAnnotationPresent(XTable.class)) {
            throw new RuntimeException("Тут надо доделать!");
        }
        StringBuilder builder = new StringBuilder();
        builder.append("DROP TABLE IF EXISTS ");
        builder.append(((XTable) cls.getAnnotation(XTable.class)).title());
        try {
            stmt.executeUpdate(builder.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

//    public Object read(Class cls) {
//        List products = new ArrayList();
//        PreparedStatement psStmt = null;
//        try {
//            psStmt = connection.prepareStatement("SELECT id, name, cost, quantity FROM product WHERE name = ? ;");
//            psStmt.setString(1, name);
//            ResultSet rs = psStmt.executeQuery();
//            while (rs.next()) {
//                products.add(new Product(rs.getInt("id"), rs.getString("name"), rs.getFloat("cost"), rs.getInt("quantity")));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            closeStatement(psStmt);
//        }
//        return products;
//    }
//    public List<Grocery> getProducts(float quantityStart, float quantityEnd) {
//        List products = new ArrayList();
//        PreparedStatement psStmt = null;
//        try {
//            psStmt = connection.prepareStatement("SELECT id, name, cost, quantity FROM product WHERE cost BETWEEN ? and ? ;");
//            psStmt.setFloat(1, quantityStart);
//            psStmt.setFloat(2, quantityEnd);
//            ResultSet rs = psStmt.executeQuery();
//            while (rs.next()) {
//                products.add(new Product(rs.getInt("id"), rs.getString("name"), rs.getFloat("cost"), rs.getInt("quantity")));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            closeStatement(psStmt);
//        }
//        return products;
//    }
//    public List<Grocery> getProducts() {
//        List products = new ArrayList();
//        Statement stmt = null;
//        try {
//            stmt = connection.createStatement();
//            ResultSet rs = stmt.executeQuery("SELECT id, name, cost, quantity FROM product");
//            while (rs.next()) {
//                products.add(new Product(rs.getInt("id"), rs.getString("name"), rs.getFloat("cost"), rs.getInt("quantity")));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            closeStatement(stmt);
//        }
//        return products;
//    }
//    public void deleteProduct(int id) {
//        PreparedStatement psStmt = null;
//        try {
//            psStmt = connection.prepareStatement("DELETE FROM product WHERE id = ?;");
//            psStmt.setInt(1, id);
//            psStmt.addBatch();
//            psStmt.executeBatch();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            closeStatement(psStmt);
//        }
//    }
//    public void addProduct(Grocery grocery) {
//        PreparedStatement psStmt = null;
//        try {
//            psStmt = connection.prepareStatement("INSERT INTO product (name, cost, quantity) VALUES (?, ?, ?);");
//            psStmt.setString(1, grocery.getName());
//            psStmt.setFloat(2, grocery.getCost());
//            psStmt.setInt(3, grocery.getQuantity());
//            psStmt.addBatch();
//            psStmt.executeBatch();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            closeStatement(psStmt);
//        }
//
//    }
//    public void dropAndCreateTable() {
//        Statement stmt = null;
//        try {
//            stmt = connection.createStatement();
//            stmt.executeUpdate("DROP TABLE IF EXISTS product;");
//            stmt.executeUpdate("CREATE TABLE product (\n"
//                    + "        id    INTEGER PRIMARY KEY AUTOINCREMENT,\n"
//                    + "        name  TEXT,\n"
//                    + "        cost  REAL,\n"
//                    + "        quantity INTEGER\n"
//                    + "    );");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            closeStatement(stmt);
//        }
//    }
//    private void closeStatement(Statement statement) {
//        try {
//            statement.close();
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//    }
    private void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:main.db");
            stmt = connection.createStatement();
        } catch (ClassNotFoundException | SQLException ex) {
            new RuntimeException("unable connect to database");
        }
    }

    public void disconnect() {
        try {
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
