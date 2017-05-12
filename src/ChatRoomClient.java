import com.bean.ChatBean;
import com.bean.ClientBean;
import com.client.SocketClient;
import com.connection.Connection;
import com.param.ClientParam;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * Created by xiao555 on 2017/5/9.
 */

class CellRenderer extends JLabel implements ListCellRenderer {
    CellRenderer() {
        setOpaque(true);
    }

    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {

        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));// 加入宽度为5的空白边框

        if (value != null) {
            setText(value.toString());
//            setIcon(new ImageIcon("images/1.jpg"));
        }
        if (isSelected) {
            setBackground(new Color(255, 255, 153));// 设置背景色
            setForeground(Color.black);
        } else {
            // 设置选取与取消选取的前景与背景颜色.
            setBackground(Color.white); // 设置背景色
            setForeground(Color.black);
        }
        setEnabled(list.isEnabled());
        setFont(new Font("TimesRoman", Font.PLAIN,13));
        setOpaque(true);
        return this;
    }
}

class UUListModel extends AbstractListModel {

    private Vector vs;

    public UUListModel(Vector vs) {
        this.vs = vs;
    }

    @Override
    public Object getElementAt(int index) {
        // TODO Auto-generated method stub
        return vs.get(index);
    }

    @Override
    public int getSize() {
        // TODO Auto-generated method stub
        return vs.size();
    }

}

public class ChatRoomClient extends JFrame {

    private static String name;
    private static Vector onlines;
    private static boolean isSendFile = false;
    private static boolean isReceiveFile = false;
    private static Connection conn;
    private static Socket clientSocket;
    private static String filePath;

    private static JPanel contentPane;
    private static JTextArea textArea;
    private static AbstractListModel listmodel;
    private static JList list;
    private static JProgressBar progressBar;
    private static JLabel lblNewLabel;
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;


    public ChatRoomClient(){}

    public ChatRoomClient(String u_name, Connection _conn) {
        name = u_name;
        conn = _conn;
        clientSocket = conn.getSocket();
        onlines = new Vector();

        SwingUtilities.updateComponentTreeUI(this);



        setTitle(name);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(200, 100, 688, 510);
        contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                URL url = ChatRoomClient.class.getResource("bg.jpg");
                g.drawImage(new ImageIcon(url).getImage(), 0, 0,
                        688, 510, null);
            }

        };
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // 聊天信息显示区域
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 10, 410, 300);
        getContentPane().add(scrollPane);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);// 激活自动换行功能
        textArea.setWrapStyleWord(true);// 激活断行不断字功能
        textArea.setFont(new Font("sdf", Font.BOLD, 13));
        scrollPane.setViewportView(textArea);

        // 打字区域
        JScrollPane inputScrollPane = new JScrollPane();
        inputScrollPane.setBounds(10, 347, 411, 97);
        getContentPane().add(inputScrollPane);

        final JTextArea inputTextArea = new JTextArea();
        inputTextArea.setLineWrap(true);// 激活自动换行功能
        inputTextArea.setWrapStyleWord(true);// 激活断行不断字功能
        inputScrollPane.setViewportView(inputTextArea);

        // 关闭按钮
        final JButton exitButton = new JButton("\u5173\u95ED");
        exitButton.setBounds(214, 448, 60, 30);
        getContentPane().add(exitButton);

        // 发送按钮
        JButton sendButton = new JButton("\u53D1\u9001");
        sendButton.setBounds(313, 448, 60, 30);
        getRootPane().setDefaultButton(sendButton);
        getContentPane().add(sendButton);

        // 在线客户列表
        listmodel = new UUListModel(onlines);
        list = new JList(listmodel);
        list.setCellRenderer(new CellRenderer());
        list.setOpaque(false);
        Border etch = BorderFactory.createEtchedBorder();
        list.setBorder(BorderFactory.createTitledBorder(etch, "在线客户:", TitledBorder.LEADING, TitledBorder.TOP,
                new Font("sdf", Font.BOLD, 18), Color.red));

        JScrollPane scrollPane_2 = new JScrollPane(list);
        scrollPane_2.setBounds(430, 10, 245, 375);
        scrollPane_2.setOpaque(false);
        scrollPane_2.getViewport().setOpaque(false);
        getContentPane().add(scrollPane_2);

        // 文件传输栏
        progressBar = new JProgressBar();
        progressBar.setBounds(430, 390, 245, 15);
        progressBar.setMinimum(1);
        progressBar.setMaximum(100);
        getContentPane().add(progressBar);

        // 文件传输提示
        lblNewLabel = new JLabel("");
        lblNewLabel.setFont(new Font("SimSun", Font.PLAIN, 12));
        lblNewLabel.setBackground(Color.WHITE);
        lblNewLabel.setBounds(430, 410, 245, 15);
        getContentPane().add(lblNewLabel);

        // 退出
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });

        // 关闭按钮
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });
        // 发送按钮
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = inputTextArea.getText();
                List to = list.getSelectedValuesList();

                if (message.equals("")) {
                    JOptionPane.showMessageDialog(getContentPane(), "消息不能为空");
                    return;
                }

                if ((int)to.size() < 1) {
                    JOptionPane.showMessageDialog(getContentPane(), "请选择聊天对象");
                    return;
                }

                ChatBean clientBean = new ChatBean();
                clientBean.setType(2);
                clientBean.setName(name);
                String time = Utils.getTimer();
                clientBean.setTimer(time);
                clientBean.setMessage(message);
                HashSet set = new HashSet();
                set.addAll(to);
                clientBean.setTo(set);
                sendMessage(clientBean);
                // 显示自己发的内容
                textArea.append(time + " 我对" + to + "说:\r\n  " + message + "\r\n");
                inputTextArea.setText(null);
                inputTextArea.requestFocus();
            }
        });
        // 发送文件
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    // 双击打开文件文件选择框
                    JFileChooser chooser = new JFileChooser();
                    chooser.setDialogTitle("选择文件框"); // 标题哦...
                    chooser.showDialog(getContentPane(), "选择"); // 这是按钮的名字..

                    // 判定是否选择了文件
                    if (chooser.getSelectedFile() != null) {
                        // 获取路径
                        filePath = chooser.getSelectedFile().getPath();
                        File file = new File(filePath);
                        // 文件为空
                        if (file.length() == 0) {
                            JOptionPane.showMessageDialog(getContentPane(),
                                    filePath + "文件为空,不允许发送.");
                            return;
                        }

                        ChatBean clientBean = new ChatBean();
                        clientBean.setType(3);// 发送文件请求
                        clientBean.setSize(new Long(file.length()).intValue());
                        clientBean.setName(name);
                        clientBean.setTimer(Utils.getTimer());
                        clientBean.setFileName(file.getName()); // 记录文件的名称
                        clientBean.setMessage(name + "向您发送文件，是否接收？");

                        // 判断要发送给谁
                        HashSet<String> set = new HashSet<String>();
                        set.addAll(list.getSelectedValuesList());
                        clientBean.setTo(set);
                        sendMessage(clientBean);
                    }
                }
            }
        });
    }

    public void exit() {
        if (isSendFile || isReceiveFile) {
            JOptionPane.showMessageDialog(contentPane,
                    "正在传输文件中，请您稍等...", "Error Message",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            int result = JOptionPane.showConfirmDialog(
                    getContentPane(), "您确定要离开吗？朋友们会想你哦"
            );
            if (result == 0) {
                ChatBean clientBean = new ChatBean();
                clientBean.setType(-1);
                clientBean.setName(name);
                clientBean.setTimer(Utils.getTimer());
                sendMessage(clientBean);
            }
        }
    }
    public static void sendMessage(ChatBean clientBean) {
        try {
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            oos.writeObject(clientBean);
            oos.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setOnFileRead(ChatBean bean) {
        // 由于等待目标客户确认是否接收文件是个阻塞状态，所以这里用线程处理
        new Thread() {
            public void run() {
                // 显示是否接收文件对话框
                int result = JOptionPane.showConfirmDialog(
                        getContentPane(), bean.getMessage());
                System.out.println(result);
                switch (result) {
                    case 0: { // 接收文件
                        String saveFilePath = "";
                        try {
                            JFileChooser chooser = new JFileChooser();
                            chooser.setDialogTitle("保存文件框");
                            // 默认文件名称还有放在当前目录下
                            chooser.setSelectedFile(new File(bean.getFileName()));
                            chooser.showDialog(getContentPane(), "保存");
                            // 保存路径
                            saveFilePath = chooser
                                    .getSelectedFile().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // 创建客户ChatBean
                        ChatBean clientBean = new ChatBean();
                        clientBean.setType(4);
                        clientBean.setName(name); // 接收文件的客户名字
                        clientBean.setTimer(Utils.getTimer());
                        clientBean.setFileName(saveFilePath);
                        clientBean.setMessage("确定接收文件");

                        // 判断要发送给谁
                        HashSet<String> set = new HashSet<String>();
                        set.add(bean.getName());
                        clientBean.setClients(bean.getTo()); // 文件目标
                        clientBean.setTo(set);  // 文件源

                        // 创建新的tcp socket 接收数据, 这是额外增加的功能, 大家请留意...
                        try {
                            ServerSocket ss = new ServerSocket(0); // 0可以获取空闲的端口号

                            clientBean.setIp(clientSocket.getInetAddress().getHostAddress());
                            clientBean.setPort(ss.getLocalPort());
                            sendMessage(clientBean); // 先通过服务器告诉发送方,
                            // 你可以直接发送文件到我这里了...
                            System.out.println("Send Message");

                            isReceiveFile = true;
                            // 等待文件来源的客户，输送文件....目标客户从网络上读取文件，并写在本地上
                            Socket sk = ss.accept();
                            textArea.append(Utils.getTimer()
                                    + "  " + bean.getFileName()
                                    + "文件保存中.\r\n");
                            DataInputStream dis = new DataInputStream( // 从网络上读取文件
                                    new BufferedInputStream(
                                            sk.getInputStream()));
                            DataOutputStream dos = new DataOutputStream( // 写在本地上
                                    new BufferedOutputStream(
                                            new FileOutputStream(
                                                    saveFilePath)));

                            int count = 0;
                            int num = bean.getSize() / 100;
                            int index = 0;
                            while (count < bean.getSize()) {
                                int t = dis.read();
                                dos.write(t);
                                count++;
                                if (num > 0) {
                                    if (count % num == 0
                                            && index < 100) {
                                        progressBar
                                                .setValue(++index);
                                    }
                                    lblNewLabel.setText("下载进度:"
                                            + count + "/"
                                            + bean.getSize()
                                            + "  整体" + index + "%");
                                } else {
                                    lblNewLabel
                                            .setText("下载进度:"
                                                    + count
                                                    + "/"
                                                    + bean.getSize()
                                                    + "  整体:"
                                                    + new Double(
                                                    new Double(
                                                            count)
                                                            .doubleValue()
                                                            / new Double(
                                                            bean.getSize())
                                                            .doubleValue()
                                                            * 100)
                                                    .intValue()
                                                    + "%");
                                    if (count == bean.getSize()) {
                                        progressBar.setValue(100);
                                    }
                                }

                            }

                            // 给文件来源客户发条提示，文件保存完毕
                            PrintWriter out = new PrintWriter(
                                    sk.getOutputStream(), true);
                            out.println(Utils.getTimer() + " 发送给"
                                    + name + "的文件["
                                    + bean.getFileName() + "]"
                                    + "文件保存完毕.\r\n");
                            out.flush();
                            dos.flush();
                            dos.close();
                            out.close();
                            dis.close();
                            sk.close();
                            ss.close();
                            textArea.append(Utils.getTimer()
                                    + "  " + bean.getFileName()
                                    + "文件保存完毕.存放位置为:"
                                    + saveFilePath + "\r\n");
                            isReceiveFile = false;
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        break;
                    }
                    default: {
                        ChatBean clientBean = new ChatBean();
                        clientBean.setType(5);
                        clientBean.setName(name); // 接收文件的客户名字
                        clientBean.setTimer(Utils.getTimer());
                        clientBean.setFileName(bean.getFileName());
                        clientBean.setMessage(Utils.getTimer()
                                + "  " + name + "取消接收文件["
                                + bean.getFileName() + "]");

                        // 判断要发送给谁
                        HashSet<String> set = new HashSet<String>();
                        set.add(bean.getName());
                        clientBean.setClients(bean.getTo()); // 文件目标
                        clientBean.setTo(set);// 文件源

                        sendMessage(clientBean);

                        break;

                    }
                }
            };
        }.start();
    }

    public void setOnFileRecive(ChatBean bean) {
        System.out.println(bean.getTimer() + " " + bean.getName()
                + "确定接收文件" + ",文件传送中..\r\n");
        textArea.append(bean.getTimer() + " " + bean.getName()
                + "确定接收文件" + ",文件传送中..\r\n");
        new Thread() {
            public void run() {
                try {
                    isSendFile = true;
                    // 创建要接收文件的客户套接字
                    Socket s = new Socket(bean.getIp(),
                            bean.getPort());
                    DataInputStream dis = new DataInputStream(
                            new FileInputStream(filePath)); // 本地读取该客户刚才选中的文件
                    DataOutputStream dos = new DataOutputStream(
                            new BufferedOutputStream(
                                    s.getOutputStream())); // 网络写出文件

                    int size = dis.available();
                    int count = 0; // 读取次数
                    int num = size / 100;
                    int index = 0;
                    while (count < size) {
                        int t = dis.read();
                        dos.write(t);
                        count++; // 每次只读取一个字节
                        if (num > 0) {
                            if (count % num == 0 && index < 100) {
                                progressBar.setValue(++index);
                            }
                            lblNewLabel.setText("上传进度:" + count
                                    + "/" + size + "  整体"
                                    + index + "%");
                        } else {
                            lblNewLabel
                                    .setText("上传进度:"
                                            + count
                                            + "/"
                                            + size
                                            + "  整体:"
                                            + new Double(
                                            new Double(
                                                    count)
                                                    .doubleValue()
                                                    / new Double(
                                                    size)
                                                    .doubleValue()
                                                    * 100)
                                            .intValue()
                                            + "%");
                            if (count == size) {
                                progressBar.setValue(100);
                            }
                        }
                    }
                    dos.flush();
                    dis.close();
                    // 读取目标客户的提示保存完毕的信息...
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(
                                    s.getInputStream()));
                    textArea.append(br.readLine() + "\r\n");
                    isSendFile = false;
                    br.close();
                    s.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            };
        }.start();
    }

    public void start(String name) {
        ClientParam param = new ClientParam();
        // 成功建立连接回调
        param.setOnConnection(conn -> {
            new Thread(() -> {
                try {
                    ChatRoomClient frame = new ChatRoomClient( name, conn);
                    frame.setVisible(true);
                    try {
                        oos = new ObjectOutputStream(clientSocket.getOutputStream());
                        // 记录上线客户的信息在ChatBean中，并发送给服务器
                        ChatBean bean = new ChatBean();
                        bean.setType(0);
                        bean.setName(name);
                        oos.writeObject(bean);
                        oos.flush();

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }).start();
        });
        // 收到消息回调
        param.setOnClientRead(bean -> {
            if (bean.getType() == 5) {
                textArea.append(bean.getMessage() + "\r\n");
                return;
            }
            System.out.println(bean.getMessage());
            // 更新列表
            onlines.clear();
            HashSet<String> clients = bean.getClients();
            Iterator<String> it = clients.iterator();
            while (it.hasNext()) {
                String ele = it.next();
                if (!name.equals(ele)) {
                    onlines.add(ele);
                }
            }
            System.out.println(onlines);
            listmodel = new UUListModel(onlines);
            list.setModel(listmodel);
            String m = bean.getMessage();
            if (m.contains(name)) {
                m = m.replace(name, "我");
            }
            textArea.append(m + "\r\n");
        });
        // 收到发送文件请求回调
        param.setOnFileRead(bean -> {
            new ChatRoomClient().setOnFileRead(bean);
        });
        // 对方确认接收文件回调
        param.setOnFileRecive(bean -> {
            System.out.println(bean.getTimer() + " " + bean.getName()
                    + "确定接收文件" + ",文件传送中..\r\n");
            new ChatRoomClient().setOnFileRecive(bean);
        });
        try {
            SocketClient client = new SocketClient(param);
            client.connect("localhost", 8888);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
