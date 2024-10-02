import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
public class RMILoginClientView extends JFrame implements
        ActionListener{
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    public RMILoginClientView(){
        super("RMI Login MVC");
        txtUsername = new JTextField(15);
        txtPassword = new JPasswordField(15);
        txtPassword.setEchoChar('*');
        btnLogin = new JButton("Login");
        JPanel content = new JPanel();
        content.setLayout(new FlowLayout());
        content.add(new JLabel("Username:"));
        content.add(txtUsername);
        content.add(new JLabel("Password:"));
        content.add(txtPassword);
        content.add(btnLogin);
        btnLogin.addActionListener(this);
        this.setContentPane(content);
        this.pack();
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(btnLogin)){
            User user = new User(txtUsername.getText(),
                    txtPassword.getText());
            RMILoginClientControl rlcc = new RMILoginClientControl();
            if(rlcc.remoteCheckLogin(user).equals("ok")){
                showMessage("Login succesfully!");
            }else{
                showMessage("Invalid username and/or password!");
            }
        }
    }
    public void showMessage(String msg){
        JOptionPane.showMessageDialog(this, msg);
    }
}