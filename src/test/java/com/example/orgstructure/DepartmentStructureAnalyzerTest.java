package com.example.orgstructure;

import com.example.depStructure.DepartmentStructureAnalyzer;
import com.example.depStructure.Employee;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.*;

public class DepartmentStructureAnalyzerTest {

    private DepartmentStructureAnalyzer analyzer;

    @Before
    public void setUp() throws IOException {
        analyzer = new DepartmentStructureAnalyzer();
        analyzer.loadEmployees("src/test/resources/employees.csv");
    }

    @Test
    public void testEmployeeLoading() {
        Map<Integer, Employee> map = analyzer.getEmployeeMap();
        assertEquals("Employee count should match CSV entries", 5, map.size());
        assertTrue(map.containsKey(123));
        assertTrue(map.containsKey(124));
        assertTrue(map.containsKey(125));
        assertTrue(map.containsKey(300));
        assertTrue(map.containsKey(305));
    }

    @Test
    public void testManagerSalaryWithinRange() {
        Map<Integer, Employee> map = analyzer.getEmployeeMap();

        // Martin (124) manages Alice (300)
        Employee martin = map.get(124);
        Employee alice = map.get(300);
        assertNotNull(martin);
        assertNotNull(alice);

        double aliceSalary = alice.getSalary();
        double expectedMin = aliceSalary * 1.2;
        double expectedMax = aliceSalary * 1.5;

        assertTrue("Martin's salary should be >= 20% more than Alice",
            martin.getSalary() >= expectedMin);
        assertTrue("Martin's salary should be <= 50% more than Alice",
            martin.getSalary() <= expectedMax);
    }

    @Test
    public void testHierarchyDepthLimit() {
        Map<Integer, Employee> map = analyzer.getEmployeeMap();

        Employee brett = map.get(305);
        assertNotNull(brett);

        int depth = analyzer.getDepthFromCEO(brett);
        assertTrue("Employee should not be more than 4 levels deep", depth <= 4);
    }

    @Test
    public void testCEOIdentification() {
        Employee ceo = analyzer.getEmployeeMap().get(123);
        assertNotNull("CEO should be identified", ceo);
        assertNull("CEO should have no manager", ceo.getManagerId());
    }
}
