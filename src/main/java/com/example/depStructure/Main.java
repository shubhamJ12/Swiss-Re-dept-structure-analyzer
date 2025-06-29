package com.example.depStructure;


public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: java Main <employee_csv_file>");
            return;
        }

        DepartmentStructureAnalyzer analyzer = new DepartmentStructureAnalyzer();
        analyzer.loadEmployees(args[0]);
        analyzer.analyze();
    }
}
