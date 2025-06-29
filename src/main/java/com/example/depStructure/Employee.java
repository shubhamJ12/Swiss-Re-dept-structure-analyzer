package com.example.depStructure;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an employee in the organization.
 */
public class Employee {
    public int id;
    public String name;
    public Integer managerId;
    public double salary;
    public List<Employee> subordinates = new ArrayList<>();

    public Employee(int id, String name, Integer managerId, double salary) {
        this.id = id;
        this.name = name;
        this.managerId = managerId;
        this.salary = salary;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public List<Employee> getSubordinates() {
        return subordinates;
    }

    public void setSubordinates(List<Employee> subordinates) {
        this.subordinates = subordinates;
    }
}
