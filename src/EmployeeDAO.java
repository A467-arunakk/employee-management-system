import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class EmployeeDAO {

    public void addEmployee(Employee emp) {
        String sql = "INSERT INTO employees (name, department, salary) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, emp.getName());
            stmt.setString(2, emp.getDepartment());
            stmt.setDouble(3, emp.getSalary());
            stmt.executeUpdate();
            System.out.println("✅ Employee Added Successfully: " + emp.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showAllEmployees() {
        String sql = "SELECT * FROM employees";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- EMPLOYEE LIST ---");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                                   " | Name: " + rs.getString("name") +
                                   " | Dept: " + rs.getString("department") +
                                   " | Salary: ₹" + rs.getDouble("salary"));
            }
            System.out.println("---------------------\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deleteEmployee(int id) {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            
            if (rowsDeleted > 0) {
                System.out.println("✅ Employee ID " + id + " Deleted Successfully!");
            } else {
                System.out.println("⚠️ No Employee found with ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateEmployee(int id, double newSalary) {
        String sql = "UPDATE employees SET salary = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, newSalary);
            stmt.setInt(2, id);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Employee ID " + id + " Salary Updated Successfully!");
            } else {
                System.out.println("⚠️ No Employee found with ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}