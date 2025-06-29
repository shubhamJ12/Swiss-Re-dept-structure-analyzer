package com.example.depStructure;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Analyzes the department structure by evaluating: - Manager salary rules (must earn 20%-50% more
 * than average salary of direct subordinates) - Employee reporting chain depth (should not exceed 4
 * levels below CEO)
 */
public class DepartmentStructureAnalyzer {

  private final Map<Integer, Employee> employeeMap = new HashMap<>();
  private Employee ceo;

  /**
   * Loads employees from a CSV file. Expected CSV header: Id,FirstName,LastName,Salary,ManagerId
   *
   * @param filePath path to the CSV file
   * @throws IOException when file read or parse fails
   */
  public void loadEmployees(String filePath) throws IOException {
    System.out.println("📂 Loading employee data from file: " + filePath);
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      reader.readLine(); // Skip header line
      String line;
      while ((line = reader.readLine()) != null) {
        parseAndAddEmployee(line);
      }
    } catch (IOException | NumberFormatException e) {
      System.err.println("Error reading employee CSV file: " + e.getMessage());
      throw e;
    }

    buildManagerRelationships();
    System.out.println("Employee data loaded. Total employees: " + employeeMap.size());
  }

  /**
   * Runs structural and salary validation rules.
   */
  public void analyze() {
    System.out.println("\n🔍 Starting analysis...\n");
    checkManagerSalaryViolations();
    checkReportingDepthViolations();
    System.out.println("\n✅ Analysis completed.");
  }

  private void parseAndAddEmployee(String line) {
    String[] parts = line.split(",", -1); // -1 keeps trailing empty fields
    if (parts.length < 5) {
      System.out.println("⚠️ Skipping malformed line: " + line);
      return;
    }
    try {
      int id = Integer.parseInt(parts[0].trim());
      String name = parts[1].trim() + " " + parts[2].trim();
      double salary = Double.parseDouble(parts[3].trim());
      Integer managerId = parts[4].trim().isEmpty() ? null : Integer.parseInt(parts[4].trim());

      Employee emp = new Employee(id, name, managerId, salary);
      employeeMap.put(id, emp);

      if (managerId == null) {
        ceo = emp;
        System.out.println("👔 CEO Identified: " + emp.name + " (ID: " + emp.id + ")");
      }
    } catch (NumberFormatException e) {
      System.out.println("⚠️ Invalid number format in line: " + line);
    }
  }

  private void buildManagerRelationships() {
    for (Employee emp : employeeMap.values()) {
      if (emp.managerId != null) {
        Employee manager = employeeMap.get(emp.managerId);
        if (manager != null) {
          manager.subordinates.add(emp);
        } else {
          System.out.println("⚠️ Manager not found for employee ID: " + emp.id);
        }
      }
    }
  }

  /**
   * Identifies managers violating the salary band (20%-50% above average of their direct reports).
   */
  private void checkManagerSalaryViolations() {
    System.out.println("💰 Checking manager salary violations...");

    for (Employee manager : employeeMap.values()) {
      List<Employee> subs = manager.subordinates;
        if (subs.isEmpty()) {
            continue;
        }

      double avgSalary = subs.stream().mapToDouble(e -> e.salary).average().orElse(0.0);
      double minExpected = avgSalary * 1.2;
      double maxExpected = avgSalary * 1.5;

      if (manager.salary < minExpected) {
        double deficit = minExpected - manager.salary;
        System.out.printf("⚠️ Manager %s (ID: %d) earns %.2f LESS than allowed. Expected ≥ %.2f%n",
            manager.name, manager.id, deficit, minExpected);
      } else if (manager.salary > maxExpected) {
        double excess = manager.salary - maxExpected;
        System.out.printf("⚠️ Manager %s (ID: %d) earns %.2f MORE than allowed. Expected ≤ %.2f%n",
            manager.name, manager.id, excess, maxExpected);
      }
    }
  }

  /**
   * Finds employees more than 4 levels below CEO.
   */
  private void checkReportingDepthViolations() {
    System.out.println("🧬 Checking hierarchy depth violations...");

    for (Employee emp : employeeMap.values()) {
      int depth = calculateDepthFromCEO(emp);
      if (depth > 4) {
        System.out.printf("⚠️ Employee %s (ID: %d) is too deep in hierarchy (depth = %d)%n",
            emp.name, emp.id, depth);
      }
    }
  }

  /**
   * Calculates levels between given employee and CEO.
   */
  private int calculateDepthFromCEO(Employee emp) {
    int depth = 0;
    while (emp.managerId != null) {
      emp = employeeMap.get(emp.managerId);
        if (emp == null) {
            break;
        }
      depth++;
    }
    return depth;
  }


  public Map<Integer, Employee> getEmployeeMap() {
    return employeeMap;
  }

  public int getDepthFromCEO(Employee emp) {
    return calculateDepthFromCEO(emp);
  }
}
