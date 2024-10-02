import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentServiceImpl extends UnicastRemoteObject implements StudentService {
    private int serverPort = 3232;
    private Registry registry;
    private Connection con;
    private String rmiService = "rmiStudentService";

    public StudentServiceImpl() throws RemoteException {
        getDBConnection("thuchanh1", "root", "trunghn2003");

        try {
            registry = LocateRegistry.createRegistry(serverPort);
            registry.rebind(rmiService, this);
            System.out.println("Student RMI Server is running...");
        } catch (RemoteException e) {
            throw e;
        }
    }

    @Override
    public List<Student> findByName(String name) throws RemoteException {
        List<Student> result = new ArrayList<>();
        String query = "SELECT * FROM students WHERE fullName LIKE '%" + name + "%' ;";
        System.out.println(query);
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Student student = new Student(
                        rs.getInt("studentId"),
                        rs.getString("studentCode"),
                        rs.getString("fullName"),
                        rs.getInt("yearOfBirth"),
                        rs.getString("hometown"),
                        rs.getFloat("GPA")
                );
                result.add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Student> findByGPA(float minGPA, float maxGPA) throws RemoteException {
        List<Student> result = new ArrayList<>();
        String query = "SELECT * FROM students WHERE GPA >= " + minGPA + " AND GPA <= " + maxGPA;
        System.out.println(query);
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Student student = new Student(
                        rs.getInt("studentId"),
                        rs.getString("studentCode"),
                        rs.getString("fullName"),
                        rs.getInt("yearOfBirth"),
                        rs.getString("hometown"),
                        rs.getFloat("GPA")
                );
                result.add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Student> getAll() throws RemoteException {
        List<Student> result = new ArrayList<>();
        String query = "SELECT * FROM students ";

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Student student = new Student(
                        rs.getInt("studentId"),
                        rs.getString("studentCode"),
                        rs.getString("fullName"),
                        rs.getInt("yearOfBirth"),
                        rs.getString("hometown"),
                        rs.getFloat("GPA")
                );
                result.add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override

    public void updateStudent(Student student) throws RemoteException {
        try {
            String query = "UPDATE students SET fullName = ?, studentCode = ?, yearOfBirth = ?, hometown = ?, GPA = ? WHERE studentId = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, student.getFullName());
            stmt.setString(2, student.getStudentCode());
            stmt.setInt(3, student.getYearOfBirth());
            stmt.setString(4, student.getHometown());
            stmt.setFloat(5, student.getGPA());
            stmt.setInt(6, student.getStudentId());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Student updated successfully in the database.");
            } else {
                System.out.println("No student found with the given ID.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error updating student in the database.");
        }
    }

    @Override
    public Student getStudentByCode(String code) throws RemoteException {
        List<Student> result = this.getAll();
        for (Student student : result) {
            if (student.getStudentCode().equals(code)) {
                return student;
            }
        }
        return null;
    }

   
    private void getDBConnection(String dbName, String username, String password) {
        String dbUrl = "jdbc:mysql://localhost:3307/" + dbName;
        String dbClass = "com.mysql.cj.jdbc.Driver"; 

        try {
            Class.forName(dbClass);
            con = DriverManager.getConnection(dbUrl, username, password);
            System.out.println("Database connected successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            new StudentServiceImpl();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
