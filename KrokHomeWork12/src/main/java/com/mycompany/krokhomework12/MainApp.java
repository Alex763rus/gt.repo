/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.krokhomework12;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import databases.SpecialtyDB;
import databases.StudentDB;
import exception.DataBaseDisconnectEx;
import exception.DataBaseEx;
import exception.ReadWriteEx;
import exception.SpecialtySQLEx;
import exception.StudentSQLEx;
import java.io.IOException;
import reflectionapi.DataBaseReflectionApi;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import student.Student;
import university.Specialty;

/**
 *
 * @author grigorevap
 */
public class MainApp {

//    static final Logger rootLogger = LogManager.getRootLogger();
    static final Logger log = LogManager.getLogger(MainApp.class);

    public static void main(String[] args) throws IOException {
        log.debug("StartHomeWork12");
        SpecialtyDB specialtyDB = null;
        try {
            specialtyDB = new SpecialtyDB();
        } catch (DataBaseEx ex) {
            log.error(ex);
            ex.printStackTrace();
        }
        StudentDB studentDb = null;
        try {
            studentDb = new StudentDB();
        } catch (DataBaseEx ex) {
            log.error(ex);
            ex.printStackTrace();
        }

        String[] commands = null;
        Scanner sc = new Scanner(System.in);
        int operType = 100;
        do {
            try {
                switch (operType) {
                    case 0:
                        printInfo();
                        break;
                    case 1:
                        switch (Integer.valueOf(commands[1])) {
                            case 1:
                                specialtyDB.dropAndcreateTable();
                                break;
                            case 2:
                                studentDb.dropAndcreateTable();
                                break;
                            default:
                                printErrorArgument(Arrays.toString(commands));
                        }
                        break;
                    case 2:
                        specialtyDB.addSpecialty(new Specialty(commands[1], commands[2]));
                        break;
                    case 3:
                        studentDb.addStudent(new Student(commands[1], Float.parseFloat(commands[2]), Integer.parseInt(commands[3])));
                        break;
                    case 4:
                        switch (Integer.valueOf(commands[1])) {
                            case 1:
                                System.out.println(specialtyDB.getSpecialtys());
                                break;
                            case 2:
                                if (commands.length > 2) {
                                    System.out.println(studentDb.getStudentsFromSpecialty(commands[2]));
                                } else {
                                    System.out.println(studentDb.getStudents());
                                }
                                break;
                            default:
                                printErrorArgument(Arrays.toString(commands));
                        }
                        break;
                    case 5:
                        System.out.println(specialtyDB.getAverageScoreFromSpecialty(commands[1]));
                        break;
                    case 6:
                        switch (Integer.valueOf(commands[1])) {
                            case 1:
                                Student.writeStudentToJson(studentDb.getStudents(), commands[2]);
                                break;
                            case 2:
                                List<Specialty> specialtys = specialtyDB.getSpecialtys();
                                List<Student> students = studentDb.getStudents();
                                specialtys.stream().forEach((specialty) -> {
                                    specialty.addStudents(students);
                                });
                                Specialty.writeStudentToXML(specialtys, commands[2]);
                                break;
                            default:
                                printErrorArgument(Arrays.toString(commands));
                        }
                        break;
                    case 7:
                        studentDb.addStudent(Student.readStudentToJson(commands[1]));
                        break;
                    case 100:
                        break;
                    default:
                        printErrorArgument(Arrays.toString(commands));
                }
                System.out.print("==> Укажите режим работы:");
                commands = sc.nextLine().split(";");
                operType = commands[0].trim().contains("help") ? 0 : Integer.valueOf(commands[0]);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                log.error(ex);
                printErrorArgument(Arrays.toString(commands));
                operType = 100;
            } catch (SpecialtySQLEx | StudentSQLEx | ReadWriteEx ex) {
                log.error(ex);
                ex.printStackTrace();
                operType = 100;
            }

        } while (operType != -1);
        System.out.println("Конец работы");
        log.debug("Конец работы");
        try {
            studentDb.disconnect();
        } catch (DataBaseDisconnectEx ex) {
            ex.printStackTrace();
        }
        try {
            specialtyDB.disconnect();
        } catch (DataBaseDisconnectEx ex) {
            ex.printStackTrace();
        }
    }

    public static void printErrorArgument(Object msg) {
        log.info("Ошибка! Неверно указаны аргументы:" + msg);
        System.out.println("Ошибка! Неверно указаны аргументы:" + msg);
    }

    static void printInfo() {
        System.out.println("================ Помощь: ================"
                + "\r\nРежимы работы:                                                  Пример команды:"
                + "\r\n  1)  DROP AND CREATE TABLE специальность|студент:              1;1|2"
                + "\r\n  2)  Добавить в БД специальность:                              2;Название специальности;Описание специальности"
                + "\r\n  3)  Добавить в БД студента:                                   3;Имя;Средний балл;ID специальности"
                + "\r\n  4)  Показать специальности|студентов в БД [на специальности]: 4;1|4;2;[Название специальности]"
                + "\r\n  5)  Показать средний балл студентов на специальности:         5;Название специальности"
                + "\r\n  6)  Экспорт студентов из БД в JSON|Экспорт всего в XML:       6;1|2;Имя файла"
                + "\r\n  7)  Импорт  студентов из JSON в БД:                           7;Имя файла"
                + "\r\nReflection API:"
                + "\r\n  8)  Reflection API DROP | CREATE TABLE:                       8;1|2;Имя класса"
                //                + "\r\n  9)  Reflection API Добавить специальность:                    9;Название специальности;Описание специальности"
                //                + "\r\n  10) Reflection API Добавить в БД студента:                    3;Имя;Средний балл;ID специальности"

                //                + "\r\n9 - Reflection API Добавить таблицу специальностей/студентов (9 1|2)"
                //                + "\r\n10 - Reflection API Удалить таблицу специальностей/студентов (10 1|2)"
                //                + "\r\n11 - Reflection API Очистить таблицу специальностей/студентов (11 1|2)"
                + "\r\n  12) Выход: -1"
                + "\r\n   0) Помощь: 0"
                + "\r\nИнформация:"
                + "\r\n  - Вводить необходимо цифры и значения, разделенные символом \";\""
                + "\r\n  - В образце знак \"|\" обозначает выбор одного из вариантов"
                + "\r\n  - В образце выражение, заключенное в кавычки: \"[]\" является не обязательным"
                + "\r\n===========================================\r\n");
    }

    public static void test1() throws IllegalArgumentException, IllegalAccessException {
        List<Specialty> specialtys = new ArrayList();
        specialtys.add(new Specialty(1, "Programmer specialty", "This is programmer specialty"));
        specialtys.add(new Specialty(2, "Test specialty", "This is testing specialty"));

        List<Student> studentList = new ArrayList();
        studentList.add(new Student("Alex", 4.9f, 1));
        studentList.add(new Student("Ivan", 4.8f, 1));
        studentList.add(new Student("Oleg", 4.5f, 2));

        DataBaseReflectionApi dbs = new DataBaseReflectionApi();
        dbs.dropTable(Student.class);
        dbs.createTable(Student.class);
        for (Student student : studentList) {
            dbs.save(student);
        }

        dbs.dropTable(Specialty.class);
        dbs.createTable(Specialty.class);
        for (Specialty specialty : specialtys) {
            dbs.save(specialty);
        }
        dbs.disconnect();

    }
}
