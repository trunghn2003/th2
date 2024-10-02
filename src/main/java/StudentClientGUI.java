import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class StudentClientGUI extends JFrame {
    private JTextField nameField;
    private JTextField minGPAField;
    private JTextField maxGPAField;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private StudentClientControl studentControl;

    public StudentClientGUI() {
        setTitle("Student Search System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        studentControl = new StudentClientControl();

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 10, 10));

        inputPanel.add(new JLabel("Student Full Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Minimum GPA:"));
        minGPAField = new JTextField();
        inputPanel.add(minGPAField);

        inputPanel.add(new JLabel("Maximum GPA:"));
        maxGPAField = new JTextField();
        inputPanel.add(maxGPAField);

        add(inputPanel, BorderLayout.NORTH);

        String[] columnNames = {"Full Name", "Student Code", "Year of Birth", "Hometown", "GPA", "Update"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 6; 
            }
        };
        studentTable = new JTable(tableModel);

        studentTable.getColumn("Update").setCellRenderer(new ButtonRenderer());
        studentTable.getColumn("Update").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(studentTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton searchByNameButton = new JButton("Search by Name");
        JButton searchByGPAButton = new JButton("Search by GPA Range");
        buttonPanel.add(searchByNameButton);
        buttonPanel.add(searchByGPAButton);
        add(buttonPanel, BorderLayout.SOUTH);

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

        loadAllStudents();
    }

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

   
    private void displayResults(List<Student> students) {
        tableModel.setRowCount(0); 
        if (students == null || students.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students found.", "Result", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (Student student : students) {
                Object[] row = {
                        student.getFullName(),
                        student.getStudentCode(),
                        student.getYearOfBirth(),
                        student.getHometown(),
                        student.getGPA(),
                        "Update"
                };
                tableModel.addRow(row);
            }
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "Update" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private int selectedRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    updateStudent(selectedRow);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            selectedRow = row;
            label = (value == null) ? "Update" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
  
    private void updateStudent(int row) {
        try {
            String fullName = (String) tableModel.getValueAt(row, 0);
            String studentCode = (String) tableModel.getValueAt(row, 1);
            int yearOfBirth = Integer.parseInt(tableModel.getValueAt(row, 2).toString());
            String hometown = (String) tableModel.getValueAt(row, 3);
            float gpa = Float.parseFloat(tableModel.getValueAt(row, 4).toString());

            Student student = studentControl.getStudentByCode(studentCode);

            if (student != null) {
                student.setFullName(fullName);
                student.setYearOfBirth(yearOfBirth);
                student.setHometown(hometown);
                student.setGPA(gpa);

                studentControl.updateStudent(student);

                JOptionPane.showMessageDialog(this, "Student updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                loadAllStudents(); 
            } else {
            
                JOptionPane.showMessageDialog(this, "Student not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating student.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isCellEditable(int row, int column) {
        return column != 5;
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
