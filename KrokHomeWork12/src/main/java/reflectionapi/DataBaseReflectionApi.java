/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reflectionapi;

import databases.DataBaseConnection;
import exception.ClassValidationEx;
import exception.DataBaseEx;
import exception.NullObjectEx;
import exception.ReflectiomApiNotAccessEx;
import exception.ReflectionApiEx;
import exception.ReflectionApiSQLEx;
import exception.ReflectionClassNotFoundEx;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author grigorevap
 */
public class DataBaseReflectionApi extends DataBaseConnection {

    static final Logger log = LogManager.getLogger(DataBaseConnection.class);

    public DataBaseReflectionApi() throws DataBaseEx {
    }

    public void save(List objects) throws ReflectionApiSQLEx, ReflectiomApiNotAccessEx, ClassValidationEx, NullObjectEx {
        for (Object object : objects) {
            save(object);
        }
    }

    public void save(Object object) throws ReflectionApiSQLEx, ReflectiomApiNotAccessEx, ClassValidationEx, NullObjectEx {
        log.debug("run save()");
        if (object == null) {
            throw new NullObjectEx();
        }
        Class cls = object.getClass();
        if (!checkValidClass(cls)) {
            log.error("checkValidClass = false " + cls.getName());
            throw new ClassValidationEx();
        }
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ");
        builder.append(((XTable) cls.getAnnotation(XTable.class)).title());
        builder.append(" (");
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(XField.class)) {
                try {
                    if (field.isAnnotationPresent(Autoincrement.class) && (int) field.get(object) == 0) {
                        continue;
                    }
                } catch (IllegalAccessException ex) {
                    log.error(ex);
                    throw new ReflectiomApiNotAccessEx(field.getName());
                }
                builder.append(field.getName()).append(", ");
            }
        }
        builder.setLength(builder.length() - 2);
        builder.append(") VALUES (");

        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(XField.class)) {
                try {
                    if (field.isAnnotationPresent(Autoincrement.class) && (int) field.get(object) == 0) {
                        continue;
                    }
                } catch (IllegalAccessException ex) {
                    log.error(ex);
                    throw new ReflectiomApiNotAccessEx(field.getName());
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
        } catch (SQLException ex) {
            log.error(ex);
            throw new ReflectionApiSQLEx();
        } catch (IllegalAccessException ex) {
            log.error(ex);
            throw new ReflectiomApiNotAccessEx();
        }
    }

    public boolean checkValidClass(Class cls) {
        log.debug("run checkValidClass()");
        if (!cls.isAnnotationPresent(XTable.class)) {
            return false;
        }
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(XField.class)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkValidClass(String fullClassName) {
        try {
            return checkValidClass(getClassForName(fullClassName));
        } catch (ReflectionClassNotFoundEx ex) {
            return false;
        }
    }

    public Class getClassForName(String fullClassName) throws ReflectionClassNotFoundEx {
        try {
            return Class.forName(fullClassName);
        } catch (ClassNotFoundException ex) {
            log.error(ex);
            throw new ReflectionClassNotFoundEx();
        }
    }

    public void createTable(String fullClassName) throws ReflectionClassNotFoundEx, ReflectionApiSQLEx, ClassValidationEx {
        createTable(getClassForName(fullClassName));
    }

    public void createTable(Class cls) throws ReflectionApiSQLEx, ClassValidationEx {
        log.debug("run createTable()");
        if (!checkValidClass(cls)) {
            log.error("checkValidClass = false " + cls.getName());
            throw new ClassValidationEx();
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
            log.error(ex);
            throw new ReflectionApiSQLEx();
        }
    }

    public void dropTable(String fullClassName) throws ReflectionApiSQLEx, ClassValidationEx, ReflectionClassNotFoundEx {
        dropTable(getClassForName(fullClassName));
    }

    public void dropTable(Class cls) throws ReflectionApiSQLEx, ClassValidationEx {
        log.debug("run dropTable()");
        if (!checkValidClass(cls)) {
            log.error("checkValidClass = false " + cls.getName());
            throw new ClassValidationEx();
        }
        StringBuilder builder = new StringBuilder();
        builder.append("DROP TABLE IF EXISTS ");
        builder.append(((XTable) cls.getAnnotation(XTable.class)).title());
        try {
            stmt.executeUpdate(builder.toString());
        } catch (SQLException ex) {
            log.error(ex);
            throw new ReflectionApiSQLEx();
        }
    }

    public Object read(String fullClassName) throws ClassValidationEx, ReflectionApiSQLEx, ReflectionApiEx {
        return read(getClassForName(fullClassName));
    }

    public Object read(Class cls) throws ClassValidationEx, ReflectionApiSQLEx, ReflectionApiEx {
        log.debug("run read()");
        if (!checkValidClass(cls)) {
            log.error("checkValidClass = false " + cls.getName());
            throw new ClassValidationEx();
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
        } catch (SQLException ex) {
            log.error(ex);
            throw new ReflectionApiSQLEx();
        } catch (InstantiationException ex) {
            log.error(ex);
            throw new ReflectionApiEx();
        } catch (IllegalAccessException ex) {
            log.error(ex);
            throw new ReflectiomApiNotAccessEx();
        }
        return clsGet;
    }

}
