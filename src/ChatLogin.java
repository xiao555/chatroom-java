import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * Created by xiao555 on 2017/5/12.
 */
public class ChatLogin extends JFrame {
    private JPanel contentPane;
    private JTextField textField;
    private JPasswordField passwordField;
    private JLabel user;
    private JLabel passwd;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    // 启动登陆界面
                    ChatLogin frame = new ChatLogin();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ChatLogin() {
        setTitle("登陆\n");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(350, 250, 300, 220);
        contentPane = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        user = new JLabel("用户名：");
        user.setBounds(30,30,80,30);

        textField = new JTextField();
        textField.setForeground(Color.red);
        textField.setBounds(120, 30, 150, 30);
        textField.setOpaque(false);
        textField.setColumns(10);

        passwd = new JLabel("密码：");
        passwd.setBounds(30,90,80,30);

        passwordField = new JPasswordField();
        passwordField.setForeground(Color.red);
        passwordField.setEchoChar('*');
        passwordField.setOpaque(false);
        passwordField.setBounds(120, 90, 150, 30);

        final JButton btnNewButton = new JButton("登录");
        btnNewButton.setBounds(50, 160, 50, 25);
        getRootPane().setDefaultButton(btnNewButton);

        final JButton btnNewButton_1 = new JButton("注册");
        btnNewButton_1.setBounds(150, 160, 50, 25);

        // 提示信息
        final JLabel lblNewLabel = new JLabel();
        lblNewLabel.setBounds(50, 130, 150, 20);
        lblNewLabel.setForeground(Color.red);

        contentPane.add(user);
        contentPane.add(passwd);
        contentPane.add(textField);
        contentPane.add(passwordField);
        contentPane.add(btnNewButton);
        contentPane.add(btnNewButton_1);
        contentPane.add(lblNewLabel);

        // 监听登陆按钮
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Properties userPro = new Properties();
                File file = new File("Users.properties");
                Utils.loadPro(userPro, file);
                String u_name = textField.getText();
                if (file.length() != 0) {

                    if (userPro.containsKey(u_name)) {
                        String u_pwd = new String(passwordField.getPassword());
                        if (u_pwd.equals(userPro.getProperty(u_name))) {
                            new ChatRoomClient().start(u_name);
                            btnNewButton.setEnabled(false);
                            setVisible(false);// 隐藏掉登陆界面
                        } else {
                            lblNewLabel.setText("您输入的密码有误！");
                            textField.setText("");
                            passwordField.setText("");
                            textField.requestFocus();
                        }
                    } else {
                        lblNewLabel.setText("您输入昵称不存在！");
                        textField.setText("");
                        passwordField.setText("");
                        textField.requestFocus();
                    }
                } else {
                    lblNewLabel.setText("您输入昵称不存在！");
                    textField.setText("");
                    passwordField.setText("");
                    textField.requestFocus();
                }
            }
        });

        //注册按钮监听
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnNewButton_1.setEnabled(false);
                ChatRegister frame = new ChatRegister();
                frame.setVisible(true);// 显示注册界面
                setVisible(false);// 隐藏掉登陆界面
            }
        });
    }

    protected void errorTip(String str) {
        // TODO Auto-generated method stub
        JOptionPane.showMessageDialog(contentPane, str, "Error Message",
                JOptionPane.ERROR_MESSAGE);
        textField.setText("");
        passwordField.setText("");
        textField.requestFocus();
    }
}
