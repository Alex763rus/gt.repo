/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reflectionapi;

import exception.SpecialtySQLEx;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import student.Student;
import university.Specialty;

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

    public void save(List objects) throws IllegalArgumentException, IllegalAccessException {
        for (Object object : objects) {
            save(object);
        }
    }

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

    public Object read(Class cls) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (!cls.isAnnotationPresent(XTable.class)) {
            throw new RuntimeException("Тут надо доделать!");
        }
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT ");
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(XField.class)) {
                builder.append(field.getName()).append(", ");
            }
        }
        builder.setLength(builder.length() - 2);
        builder.append(" FROM ");
        builder.append(((XTable) cls.getAnnotation(XTable.class)).title());
        List<Object> clsGet = new ArrayList();
        try {

            Object object = cls.newInstance();
            PreparedStatement psStmt = connection.prepareStatement(builder.toString());
            ResultSet rs = psStmt.executeQuery();
            while (rs.next()) {
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (field.isAnnotationPresent(XField.class)) {
                        field.setAccessible(true);
                        if (field.isAnnotationPresent(XField.class)) {
                            if (field.getType().toString().equals("float")) {
                                field.set(object, Float.valueOf(rs.getObject(field.getName()).toString()));
                            } else {
                                field.set(object, rs.getObject(field.getName()));
                            }
                        }
                    }
                }
                clsGet.add(object);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clsGet;
    }

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
