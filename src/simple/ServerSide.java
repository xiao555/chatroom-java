package simple;

import com.bean.ChatBean;
import com.bean.ClientBean;
import com.param.ServerParam;
import com.server.SocketServer;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by xiao555 on 2017/5/12.
 */
public class ServerSide {
    private static HashMap<String, ClientBean> onlines; // 保存在线连接
    public static void main(String[] args) {
        ServerParam param = new ServerParam("localhost", 8008);
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
                String m = " " + cbean.getName() + "上线啦！";
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
                // 生成信息
                String m = conn.getBean().getName() + " 说: " + conn.getBean().getMessage();
                ChatBean chatbean = new ChatBean(2, m, conn.getBean().getTo(), onlines);
                // 发送
                conn.sendAll(chatbean);
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
                String m = conn.getBean().getName() + " " + "is offline!";
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
