import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface StudentService extends Remote {

    List<Student> findByName(String name) throws RemoteException;


    List<Student> findByGPA(float minGPA, float maxGPA) throws RemoteException;

    List<Student> getAll() throws RemoteException;
    void updateStudent(Student student) throws RemoteException;
    Student getStudentByCode(String code) throws RemoteException;


}
