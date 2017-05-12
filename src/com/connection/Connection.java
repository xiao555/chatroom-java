package com.connection;

import com.bean.ChatBean;
import com.bean.ClientBean;
import com.param.ClientParam;
import com.param.ServerParam;
import com.sun.org.apache.regexp.internal.RE;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.ClosedChannelException;
import java.util.*;

/**
 * Created by xiao555 on 2017/5/10.
 */
public class Connection {
    private ServerParam param;
    private ClientParam clientParam;
    private ChatBean bean;
    private Socket socket;


    public Connection(ChatBean bean, Socket socket, ServerParam param) {
        this.bean = bean;
        this.socket = socket;
        this.param = param;
    }

    public Connection(Socket socket, ClientParam param) {
        this.socket = socket;
        this.clientParam = param;
    }

    public Socket getSocket() {
        return socket;
    }

    public ChatBean getBean() {
        return bean;
    }

    public static void CheckClosed(ChatBean serverBean) {
        Collection<ClientBean> clients = serverBean.getOnlines().values();
        Iterator<ClientBean> it = clients.iterator();
        while (it.hasNext()) {
            ClientBean item = it.next();
            Socket c = item.getSocket();
            if (c.isClosed()) {
                System.out.println(c.getRemoteSocketAddress());
                serverBean.getClients().remove(item.getName());
            }
        }
    }

    // 向选中的用户发送数据
    public void sendMessage(ChatBean serverBean) {
//        System.out.println("Send Message");
        CheckClosed(serverBean);
        // 首先取得所有的values
        Set<String> cbs = serverBean.getOnlines().keySet();
        Iterator<String> it = cbs.iterator();
        // 选中客户
        HashSet<String> clients = serverBean.getTo();
//        System.out.println(" " + clients.toString());
        while (it.hasNext()) {
            // 在线客户
            String client = it.next();
            // 是目标，发送 serverbean
            if (clients.contains(client)) {
                Socket c = serverBean.getOnlines().get(client).getSocket();
                send(c, serverBean);
            }
        }
    }

    // 向所有的用户发送数据
    public void sendAll(ChatBean serverBean) {
        CheckClosed(serverBean);
        Collection<ClientBean> clients = serverBean.getOnlines().values();
        Iterator<ClientBean> it = clients.iterator();
        while (it.hasNext()) {
            Socket c = it.next().getSocket();
            send(c, serverBean);
        }
    }

    public void send(Socket client, ChatBean bean) {
        if (client.isClosed()) {
            System.out.println(client.getRemoteSocketAddress());
            return;
        }
//        System.out.println("Send to" + client.getRemoteSocketAddress());
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(client.getOutputStream());
            oos.writeObject(bean);
            oos.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            // TODO handle exception exit
        }
    }
}
