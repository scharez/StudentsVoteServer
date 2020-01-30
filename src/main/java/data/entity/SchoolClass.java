package data.entity;

import data.enums.Department;

import javax.persistence.*;

@Entity
public class SchoolClass {

    @Id
    @GeneratedValue
    private int id;

    private String name;
    private String department;

    private String currentDate;

    public SchoolClass() {}

    public SchoolClass(String name, String department, String currentDate) {
        this.name = name;
        this.department = department;
        this.currentDate = currentDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String ded) {
        this.currentDate = ded;
    }

}
