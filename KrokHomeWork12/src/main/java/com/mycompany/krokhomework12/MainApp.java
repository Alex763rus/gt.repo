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
import exception.ReflectionApiEx;
import exception.SpecialtySQLEx;
import exception.StudentSQLEx;
import reflectionapi.DataBaseReflectionApi;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import specialty.Specialty;
import student.Student;

/**
 *
 * @author grigorevap
 */
public class MainApp {

    static final Logger log = LogManager.getLogger(MainApp.class);

    public static void main(String[] args) {
        log.trace("Start HomeWork12");
        SpecialtyDB specialtyDB = null;
        try {
            specialtyDB = new SpecialtyDB();
        } catch (DataBaseEx ex) {
            log.trace(ex);
        }
        StudentDB studentDb = null;
        try {
            studentDb = new StudentDB();
        } catch (DataBaseEx ex) {
            log.trace(ex);
        }
        DataBaseReflectionApi reflectionDB = null;
        try {
            reflectionDB = new DataBaseReflectionApi();
        } catch (DataBaseEx ex) {
            log.trace(ex);
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
                                printErrorArgument(commands);
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
                                printErrorArgument(commands);
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
                                printErrorArgument(commands);
                        }
                        break;
                    case 7:
                        studentDb.addStudent(Student.readStudentToJson(commands[1]));
                        break;
                    case 8:
                        System.out.println(reflectionDB.checkValidClass(commands[1]) ? "Валидный" : "Невалидный");
                        break;
                    case 9:
                        reflectionDB.dropTable(commands[1]);
                        reflectionDB.createTable(commands[1]);
                        break;
                    case 10:
                        reflectionDB.save(new Specialty(commands[1], commands[2]));
                        break;
                    case 11:
                        reflectionDB.save(new Student(commands[1], Float.parseFloat(commands[2]), Integer.parseInt(commands[3])));
                        break;
                    case 12:
                        System.out.println(reflectionDB.read(commands[1]));
                        break;
                    case 100:
                        break;
                    default:
                        printErrorArgument(commands);

                }
                System.out.print("==> Укажите режим работы:");
                commands = sc.nextLine().split(";");
                operType = commands[0].trim().contains("help") ? 0 : Integer.valueOf(commands[0]);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                printErrorArgument(commands);
                operType = 100;
            } catch (SpecialtySQLEx | StudentSQLEx | ReadWriteEx | ReflectionApiEx ex) {
                log.trace(ex);
                operType = 100;
            }

        } while (operType != -1);

        try {
            studentDb.disconnect();
        } catch (DataBaseDisconnectEx ex) {
            log.trace(ex);
        }
        try {
            specialtyDB.disconnect();
        } catch (DataBaseDisconnectEx ex) {
            log.trace(ex);
        }
        try {
            reflectionDB.disconnect();
        } catch (DataBaseDisconnectEx ex) {
            log.trace(ex);
        }
        log.trace("END HomeWork12");
    }

    public static void printErrorArgument(String[] arrays) {
        log.trace("Ошибка! Неверно указаны аргументы:" + Arrays.toString(arrays));
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
                + "\r\n  8)  Reflection Проверить класс на валидность:                 8;Полное имя класса"
                + "\r\n  9)  Reflection DROP AND CREATE TABLE таблицы:                 9;Полное имя класса"
                + "\r\n  10) Reflection API Добавить специальность в БД:               10;Название специальности; Описание специальности"
                + "\r\n  11) Reflection API Добавить студента в БД:                    11;Имя;Средний балл;ID специальности"
                + "\r\n  12) Reflection API прочитать класс:                           12;Полное имя класса"
                + "\r\n  12) Выход: -1"
                + "\r\n   0) Помощь: 0"
                + "\r\nИнформация:"
                + "\r\n  - Вводить необходимо цифры и значения, разделенные символом \";\""
                + "\r\n  - В образце знак \"|\" обозначает выбор одного из вариантов"
                + "\r\n  - В образце выражение, заключенное в кавычки: \"[]\" является не обязательным"
                + "\r\n===========================================\r\n");
    }
}
