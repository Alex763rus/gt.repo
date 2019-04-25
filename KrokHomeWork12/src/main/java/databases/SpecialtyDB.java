/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databases;

import exception.DataBaseEx;
import exception.SpecialtySQLEx;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import specialty.Specialty;

/**
 *
 * @author Алексей
 */
public class SpecialtyDB extends DataBaseConnection {

    private final String psStmtSelectAll = "SELECT id, name, description FROM specialty;";
    private final String psStmtInsertSpecialty = "INSERT INTO specialty (name, description) VALUES (?, ?);";
    private final String psStmtSelectAverageScoreFromSpecialty = "SELECT avg(averageScore) averageScore FROM student st INNER JOIN specialty sp on sp.id = st.specialtyId  WHERE sp.name = ?;";

    public SpecialtyDB() throws DataBaseEx {
    }

    public List<Specialty> getSpecialtys() throws SpecialtySQLEx {
        log.debug("run getSpecialtys()");
        List specialtys = new ArrayList();
        try {
            PreparedStatement psStmt = connection.prepareStatement(psStmtSelectAll);
            ResultSet rs = psStmt.executeQuery();
            while (rs.next()) {
                specialtys.add(new Specialty(rs.getInt("id"), rs.getString("name"), rs.getString("description")));
            }
        } catch (SQLException ex) {
            log.error(ex);
            throw new SpecialtySQLEx();
        }
        return specialtys;
    }

    public void addSpecialty(Specialty specialty) throws SpecialtySQLEx {
        log.debug("run addSpecialty()");
        PreparedStatement psStmt = null;
        try {
            psStmt = connection.prepareStatement(psStmtInsertSpecialty);
            psStmt.setString(1, specialty.getName());
            psStmt.setString(2, specialty.getDescription());
            psStmt.addBatch();
            psStmt.executeBatch();
        } catch (SQLException ex) {
            log.error(ex);
            throw new SpecialtySQLEx();
        }
    }

    public float getAverageScoreFromSpecialty(String specialtyName) throws SpecialtySQLEx {
        log.debug("run getAverageScoreFromSpecialty()");
        try {
            PreparedStatement psStmt = connection.prepareStatement(psStmtSelectAverageScoreFromSpecialty);
            psStmt.setString(1, specialtyName);
            ResultSet rs = psStmt.executeQuery();
            if (rs.next()) {
                return rs.getFloat("averageScore");
            }
        } catch (SQLException ex) {
            log.error(ex);
            throw new SpecialtySQLEx();
        }
        return 0;
    }

    public void dropAndcreateTable() throws SpecialtySQLEx {
        log.debug("run dropAndcreateTable()");
        try {
            stmt.executeUpdate("DROP TABLE IF EXISTS specialty;");
            stmt.executeUpdate("CREATE TABLE specialty (\n"
                    + "        id    INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + "        name  TEXT,\n"
                    + "        description  TEXT\n"
                    + "    );");
        } catch (SQLException ex) {
            log.error(ex);
            throw new SpecialtySQLEx();
        }
        log.info("DROP AND CREATE SPECIALTY");
    }

}
