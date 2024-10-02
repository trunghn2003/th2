import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class StudentClientControl {
    private String serverHost = "localhost";
    private int serverPort = 3232;
    private StudentService studentService;
    private Registry registry;
    private String rmiService = "rmiStudentService";

    public StudentClientControl() {
        try {
            // Get the registry
            registry = LocateRegistry.getRegistry(serverHost, serverPort);

            // Lookup the StudentService
            studentService = (StudentService) registry.lookup(rmiService);
            System.out.println("Connected to Student RMI Service");
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("Error connecting to RMI Registry");
        } catch (NotBoundException e) {
            e.printStackTrace();
            System.out.println("Error: Service not bound in RMI Registry");
        }
    }

    // Method to search students by name
    public List<Student> searchByName(String name) {
        List<Student> result = null;
        try {
            result = studentService.findByName(name);
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("Error during remote name search");
        }
        return result;
    }

    // Method to search students by GPA range
    public List<Student> searchByGPA(float minGPA, float maxGPA) {
        List<Student> result = null;
        try {
            result = studentService.findByGPA(minGPA, maxGPA);
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("Error during remote GPA search");
        }
        return result;
    }
    public List<Student> getAllStudents(){
        List<Student> result = null;
        try {
            result = studentService.getAll();
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("Error during remote GPA search");
        }
        return result;
    }

    public void updateStudent(Student student) {
        try {
            studentService.updateStudent(student);  // This will invoke the server-side update
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("Error updating student.");
        }
    }

}
