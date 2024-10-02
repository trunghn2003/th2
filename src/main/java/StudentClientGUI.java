import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class StudentClientGUI extends JFrame {
    private JTextField nameField;
    private JTextField minGPAField;
    private JTextField maxGPAField;
    private JPanel resultPanel;
    private StudentClientControl studentControl;

    public StudentClientGUI() {
        setTitle("Student Search System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize the RMI control
        studentControl = new StudentClientControl();

        // Setup GUI components
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 10, 10));

        // Name input
        inputPanel.add(new JLabel("Student Full Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        // GPA input (min and max)
        inputPanel.add(new JLabel("Minimum GPA:"));
        minGPAField = new JTextField();
        inputPanel.add(minGPAField);

        inputPanel.add(new JLabel("Maximum GPA:"));
        maxGPAField = new JTextField();
        inputPanel.add(maxGPAField);

        add(inputPanel, BorderLayout.NORTH);

        // Result panel (for student details and update buttons)
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(resultPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton searchByNameButton = new JButton("Search by Name");
        JButton searchByGPAButton = new JButton("Search by GPA Range");
        buttonPanel.add(searchByNameButton);
        buttonPanel.add(searchByGPAButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners to buttons
        searchByNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchByName();
            }
        });

        searchByGPAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchByGPA();
            }
        });

        // Load all students when the GUI loads
        loadAllStudents();
    }

    // Method to load and display all students when the application starts
    private void loadAllStudents() {
        try {
            List<Student> students = studentControl.getAllStudents();
            displayResults(students);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading all students.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void searchByName() {
        try {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a full name.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<Student> students = studentControl.searchByName(name);
            displayResults(students);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error during search by name.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void searchByGPA() {
        try {
            float minGPA = Float.parseFloat(minGPAField.getText().trim());
            float maxGPA = Float.parseFloat(maxGPAField.getText().trim());

            List<Student> students = studentControl.searchByGPA(minGPA, maxGPA);
            displayResults(students);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid GPA values.", "Input Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error during search by GPA.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Method to display students with update buttons
    private void displayResults(List<Student> students) {
        resultPanel.removeAll();  // Clear previous results
        if (students == null || students.isEmpty()) {
            resultPanel.add(new JLabel("No students found."));
        } else {
            for (Student student : students) {
                JPanel studentPanel = new JPanel(new BorderLayout());

                // Create label to display student info
                JLabel studentLabel = new JLabel(student.toString());
                studentPanel.add(studentLabel, BorderLayout.CENTER);

                // Create update button for each student
                JButton updateButton = new JButton("Update");
                updateButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        updateStudent(student);
                    }
                });
                studentPanel.add(updateButton, BorderLayout.EAST);

                resultPanel.add(studentPanel);
            }
        }
        resultPanel.revalidate();
        resultPanel.repaint();
    }

    // Method to update all attributes of the selected student (except the id)
    private void updateStudent(Student student) {
        // Create a dialog with multiple input fields for updating student attributes
        JPanel updatePanel = new JPanel(new GridLayout(0, 2, 10, 10));

        // Name
        updatePanel.add(new JLabel("Full Name:"));
        JTextField nameField = new JTextField(student.getFullName());
        updatePanel.add(nameField);

        // Student Code
        updatePanel.add(new JLabel("Student Code:"));
        JTextField codeField = new JTextField(student.getStudentCode());
        updatePanel.add(codeField);

        // Year of Birth
        updatePanel.add(new JLabel("Year of Birth:"));
        JTextField yearField = new JTextField(String.valueOf(student.getYearOfBirth()));
        updatePanel.add(yearField);

        // Hometown
        updatePanel.add(new JLabel("Hometown:"));
        JTextField hometownField = new JTextField(student.getHometown());
        updatePanel.add(hometownField);

        // GPA
        updatePanel.add(new JLabel("GPA:"));
        JTextField gpaField = new JTextField(String.valueOf(student.getGPA()));
        updatePanel.add(gpaField);

        // Show dialog
        int result = JOptionPane.showConfirmDialog(this, updatePanel, "Update Student", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            // Update the student object with new values
            student.setFullName(nameField.getText().trim());
            student.setStudentCode(codeField.getText().trim());
            student.setYearOfBirth(Integer.parseInt(yearField.getText().trim()));
            student.setHometown(hometownField.getText().trim());
            student.setGPA(Float.parseFloat(gpaField.getText().trim()));

            // Simulate sending updated student data to the server
            studentControl.updateStudent(student); // Call the method in the control class

            JOptionPane.showMessageDialog(this, "Student updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadAllStudents(); // Reload students to refresh the list
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                StudentClientGUI gui = new StudentClientGUI();
                gui.setVisible(true);
            }
        });
    }
}
