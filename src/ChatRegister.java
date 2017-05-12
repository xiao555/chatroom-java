import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by xiao555 on 2017/5/12.
 */
public class ChatRegister extends JFrame {
    private JPanel contentPane;
    private JTextField textField;
    private JPasswordField passwordField;
    private JPasswordField passwordField_1;
    private JLabel lblNewLabel;
    private JLabel user;
    private JLabel passwd;
    private JLabel confirmPasswd;

    public ChatRegister() {
        setTitle("注册\n");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(350, 250, 450, 300);
        contentPane = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(new ImageIcon("images/22.jpg").getImage(), 0,0, getWidth(), getHeight(), null);
            }
        };
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        user = new JLabel("用户名：");
        user.setBounds(30,30,80,30);
        contentPane.add(user);

        textField = new JTextField();
        textField.setBounds(120, 30, 150, 30);
        textField.setOpaque(false);
        contentPane.add(textField);
        textField.setColumns(10);

        passwd = new JLabel("密码：");
        passwd.setBounds(30,90,80,30);
        contentPane.add(passwd);

        passwordField = new JPasswordField();
        passwordField.setEchoChar('*');
        passwordField.setOpaque(false);
        passwordField.setBounds(120, 90, 150, 30);
        contentPane.add(passwordField);

        confirmPasswd = new JLabel("确认密码：");
        confirmPasswd.setBounds(30,150,80,30);
        contentPane.add(confirmPasswd);

        passwordField_1 = new JPasswordField();
        passwordField_1.setBounds(120, 150, 150, 30);
        passwordField_1.setOpaque(false);
        contentPane.add(passwordField_1);

        //注册按钮
        final JButton btnNewButton_1 = new JButton("注册");
        btnNewButton_1.setBounds(50, 220, 80, 40);
        getRootPane().setDefaultButton(btnNewButton_1);
        contentPane.add(btnNewButton_1);

        //返回按钮
        final JButton btnNewButton = new JButton("返回");
        btnNewButton.setBounds(150, 220, 80, 40);
        contentPane.add(btnNewButton);

        //提示信息
        lblNewLabel = new JLabel();
        lblNewLabel.setBounds(50, 190, 185, 20);
        lblNewLabel.setForeground(Color.red);
        contentPane.add(lblNewLabel);

        //返回按钮监听
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnNewButton.setEnabled(false);
                //返回登陆界面
                ChatLogin frame = new ChatLogin();
                frame.setVisible(true);
                setVisible(false);
            }
        });

        //注册按钮监听
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Properties userPro = new Properties();
                File file = new File("Users.properties");
                Utils.loadPro(userPro, file);

                String u_name = textField.getText();
                String u_pwd = new String(passwordField.getPassword());
                String u_pwd_ag = new String(passwordField_1.getPassword());

                // 判断用户名是否在普通用户中已存在
                if (u_name.length() != 0) {

                    if (userPro.containsKey(u_name)) {
                        lblNewLabel.setText("用户名已存在!");
                    } else {
                        isPassword(userPro, file, u_name, u_pwd, u_pwd_ag);
                    }
                } else {
                    lblNewLabel.setText("用户名不能为空！");
                }
            }

            private void isPassword(Properties userPro,
                                    File file, String u_name, String u_pwd, String u_pwd_ag) {
                if (u_pwd.equals(u_pwd_ag)) {
                    if (u_pwd.length() != 0) {
                        userPro.setProperty(u_name, u_pwd_ag);
                        try {
                            userPro.store(new FileOutputStream(file),
                                    "Copyright (c) Boxcode Studio");
                        } catch (FileNotFoundException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        btnNewButton_1.setEnabled(false);
                        //返回登陆界面
                        ChatLogin frame = new ChatLogin();
                        frame.setVisible(true);
                        setVisible(false);
                    } else {
                        lblNewLabel.setText("密码为空！");
                    }
                } else {
                    lblNewLabel.setText("密码不一致！");
                }
            }
        });
    }
}
