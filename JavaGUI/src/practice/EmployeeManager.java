package practice;

import java.util.ArrayList;

public class EmployeeManager {
    public static ArrayList<Employee> employeeList = new ArrayList<>();

    public static void addEmployee(Employee employee) {
        employeeList.add(employee);
    }

    public static ArrayList<Employee> getEmployees() {
        return employeeList;
    }
}