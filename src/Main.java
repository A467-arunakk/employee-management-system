import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n==========================================");
            System.out.println("     EMPLOYEE MANAGEMENT SYSTEM");
            System.out.println("==========================================");
            System.out.println("1. Add Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Search Employee (by ID / Name)");
            System.out.println("4. Update Employee Salary");
            System.out.println("5. Delete Employee");
            System.out.println("6. Exit");
            System.out.print("Enter your choice (1-6): ");

            int choice = getValidInt(sc);

            switch (choice) {
                case 1:
                    addEmployee(sc);
                    break;
                case 2:
                    viewEmployees();
                    break;
                case 3:
                    searchEmployee(sc);
                    break;
                case 4:
                    updateEmployee(sc);
                    break;
                case 5:
                    deleteEmployee(sc);
                    break;
                case 6:
                    System.out.println("Exiting Application... Thank you!");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice! Please select between 1 and 6.");
            }
        }
    }

    // 1. ADD EMPLOYEE (With Input Validation)
    public static void addEmployee(Scanner sc) {
        System.out.println("\n--- Add New Employee ---");
        String name = getNonEmptyString(sc, "Enter Employee Name: ");
        String dept = getNonEmptyString(sc, "Enter Department: ");
        double salary = getValidSalary(sc);

        String query = "INSERT INTO employees (name, department, salary) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, name);
            ps.setString(2, dept);
            ps.setDouble(3, salary);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println(">> Employee added successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    // 2. VIEW ALL EMPLOYEES
    public static void viewEmployees() {
        String query = "SELECT * FROM employees";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            System.out.println("\n-------------------------------------------------------------");
            System.out.printf("%-6s | %-20s | %-15s | %-10s\n", "ID", "Name", "Department", "Salary");
            System.out.println("-------------------------------------------------------------");

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.printf("%-6d | %-20s | %-15s | %-10.2f\n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getDouble("salary"));
            }

            if (!hasData) {
                System.out.println("No employee records found.");
            }
            System.out.println("-------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    // 3. SEARCH EMPLOYEE BY ID OR NAME
    public static void searchEmployee(Scanner sc) {
        System.out.println("\n--- Search Employee ---");
        System.out.print("Enter Employee ID or Name to search: ");
        String input = sc.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println("Search input cannot be empty!");
            return;
        }

        String query = "SELECT * FROM employees WHERE id = ? OR name LIKE ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            int id = -1;
            try {
                id = Integer.parseInt(input);
            } catch (NumberFormatException ignored) {}

            ps.setInt(1, id);
            ps.setString(2, "%" + input + "%");

            ResultSet rs = ps.executeQuery();
            boolean found = false;

            System.out.println("\n-------------------------------------------------------------");
            System.out.printf("%-6s | %-20s | %-15s | %-10s\n", "ID", "Name", "Department", "Salary");
            System.out.println("-------------------------------------------------------------");

            while (rs.next()) {
                found = true;
                System.out.printf("%-6d | %-20s | %-15s | %-10.2f\n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getDouble("salary"));
            }

            if (!found) {
                System.out.println("No matching employee record found.");
            }
            System.out.println("-------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    // 4. UPDATE EMPLOYEE SALARY
    public static void updateEmployee(Scanner sc) {
        System.out.println("\n--- Update Salary ---");
        System.out.print("Enter Employee ID to update: ");
        int id = getValidInt(sc);
        double salary = getValidSalary(sc);

        String query = "UPDATE employees SET salary = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setDouble(1, salary);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println(">> Salary updated successfully!");
            } else {
                System.out.println(">> Employee ID not found.");
            }
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    // 5. DELETE EMPLOYEE
    public static void deleteEmployee(Scanner sc) {
        System.out.println("\n--- Delete Employee ---");
        System.out.print("Enter Employee ID to delete: ");
        int id = getValidInt(sc);

        String query = "DELETE FROM employees WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println(">> Employee deleted successfully!");
            } else {
                System.out.println(">> Employee ID not found.");
            }
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    // --- HELPER VALIDATION METHODS ---

    private static int getValidInt(Scanner sc) {
        while (true) {
            String input = sc.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Invalid integer! Please enter a valid number: ");
            }
        }
    }

    private static double getValidSalary(Scanner sc) {
        while (true) {
            System.out.print("Enter Salary: ");
            String input = sc.nextLine().trim();
            try {
                double salary = Double.parseDouble(input);
                if (salary < 0) {
                    System.out.println("Salary cannot be negative. Try again.");
                    continue;
                }
                return salary;
            } catch (NumberFormatException e) {
                System.out.println("Invalid format! Enter a valid numeric value for salary.");
            }
        }
    }

    private static String getNonEmptyString(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Field cannot be empty. Please enter a value.");
        }
    }
}