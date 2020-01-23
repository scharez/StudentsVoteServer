package data.entity;

import data.enums.Department;

import javax.persistence.*;

@Entity
public class SchoolClass {

    @Id
    @GeneratedValue
    private int id;

    private String name;
    private Department department;

    private String currentDate;

    public SchoolClass() {}

    public SchoolClass(String name, Department department, String currentDate) {
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

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String ded) {
        this.currentDate = ded;
    }

}
