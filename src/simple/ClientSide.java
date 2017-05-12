package simple;

import com.bean.ChatBean;
import com.client.SocketClient;
import com.connection.Connection;
import com.param.ClientParam;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by xiao555 on 2017/5/12.
 */
public class ClientSide {
    private static ObjectOutputStream oos;
    private static Socket clientSocket;
    private static String name;
    private static Connection conn;

    public ClientSide(String u_name, Connection _conn) {
        name = u_name;
        conn = _conn;
        clientSocket = conn.getSocket();
    }

    public static void main(String[] args) {
        ClientParam param = new ClientParam();
        // 成功建立连接回调
        param.setOnConnection(conn -> {
            new Thread(() -> {
                ClientSide client = new ClientSide(conn.getSocket().getLocalSocketAddress().toString(), conn);
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
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNext()) {
                    String msg = scanner.nextLine();
                    if (msg.equals("exit")) { // 下线
                        try {
                            oos = new ObjectOutputStream(clientSocket.getOutputStream());
                            // 记录上线客户的信息在ChatBean中，并发送给服务器
                            ChatBean bean = new ChatBean();
                            bean.setType(-1);
                            bean.setName(name);
                            oos.writeObject(bean);
                            oos.flush();
                            continue;
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    try {
                        oos = new ObjectOutputStream(clientSocket.getOutputStream());
                        // 记录上线客户的信息在ChatBean中，并发送给服务器
                        ChatBean bean = new ChatBean();
                        bean.setType(1);
                        bean.setName(name);
                        bean.setMessage(msg);
                        oos.writeObject(bean);
                        oos.flush();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }).start();
        });
        // 收到消息回调
        param.setOnClientRead(bean -> {
            System.out.println(bean.getMessage());
        });
        // 下线
        param.setOnClose(conn -> {
            System.exit(0);
        });
        try {
            SocketClient client = new SocketClient(param);
            client.connect("localhost", 8008);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
