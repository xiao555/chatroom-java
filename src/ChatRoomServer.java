import com.bean.ChatBean;
import com.bean.ClientBean;
import com.param.ServerParam;
import com.server.SocketServer;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by xiao555 on 2017/5/9.
 */

public class ChatRoomServer {
    private static HashMap<String, ClientBean> onlines; // 保存在线连接
    public static void main(String[] args) {
        ServerParam param = new ServerParam("localhost",8888);
        onlines = new HashMap<String, ClientBean>();
//        param.setBacklog(128);
        param.setOnlines(onlines);
        param.setOnAccept(conn -> {
            try {
                // 记录上线用户并保存在onlines中
                ClientBean cbean = new ClientBean();
                cbean.setName(conn.getBean().getName());
                cbean.setSocket(conn.getSocket());
                onlines.put(cbean.getName(), cbean);
                param.setOnlines(onlines);
                // 生成ChatBean
                String time = Utils.getTimer();
                String m = time + " " + cbean.getName() + "上线啦！";
                System.out.println(m);
                // 通知所有用户
                HashSet<String> users = new HashSet<String>();
                users.addAll(onlines.keySet());
                ChatBean chatbean = new ChatBean(0, m, users, onlines);
                // 发送
                conn.sendAll(chatbean);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        param.setOnRead(conn -> {
            try {
                if (conn.getBean().getType() == 3) { // 发送文件请求
                    String m = conn.getBean().getTimer() + "  " + conn.getBean().getName() + "向你传送文件,是否需要接受";
                    conn.getBean().setMessage(conn.getBean().getMessage());
                    conn.getBean().setOnlines(onlines);
                    conn.sendMessage(conn.getBean());
                } else if (conn.getBean().getType() == 4) { // 确认接收文件
                    System.out.println(conn.getBean().getName() + "同意接收文件");
                    conn.getBean().setOnlines(onlines);
                    conn.sendMessage(conn.getBean());
                } else if (conn.getBean().getType() == 5) { // 取消接收文件;
                    conn.getBean().setOnlines(onlines);
                    conn.sendMessage(conn.getBean());
                } else { // 普通信息
                    // 生成信息
                    String time = Utils.getTimer();
                    String m = time + conn.getBean().getName() + " 对 " + conn.getBean().getTo() + " 说:\r\n  " + conn.getBean().getMessage();
                    ChatBean chatbean = new ChatBean(2, m, conn.getBean().getTo(), onlines);
                    // 发送
                    conn.sendMessage(chatbean);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        param.setOnClose(conn -> {
            try {
                ChatBean bean = new ChatBean();
                bean.setType(-1);
                conn.send(conn.getSocket(), bean);
                // 删除用户
                onlines.remove(conn.getBean().getName());
                param.setOnlines(onlines);
                // 生成消息
                String time = Utils.getTimer();
                String m = time + conn.getBean().getName() + " " + "is offline!";
                System.out.println(m);
                // 通知所有用户
                HashSet<String> users = new HashSet<String>();
                users.addAll(onlines.keySet());
                ChatBean chatbean = new ChatBean(0, m, users, onlines);
                // 发送
                conn.sendAll(chatbean);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        SocketServer server = new SocketServer(param);
        try {
            server.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
