package com.thread;

import com.bean.ChatBean;
import com.connection.Connection;
import com.param.ClientParam;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by xiao555 on 2017/5/11.
 */
public class ClientThread implements Runnable {
    private ClientParam param;
    private Socket client;
    private ChatBean bean;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public ClientThread(Socket client, ClientParam param) {
        this.client = client;
        this.param = param;
    }

    @Override
    public void run() {
        try {
            // 不停从服务端读取消息
            while (true) {
                // 读取信息
                ois = new ObjectInputStream(client.getInputStream());
                bean = (ChatBean)ois.readObject();
                switch (bean.getType()) {
                    case -1: {
                        System.out.println("已下线");
                        return;
                    }
                    case 0: { // 有人上下线
                        param.getOnClientRead().OnClientRead(bean);
                        break;
                    }
                    case 2: {   // 私聊
                        param.getOnClientRead().OnClientRead(bean);
                        break;
                    }
                    case 3: {   // 确认接收文件
                        param.getOnFileRead().OnFileRead(bean);
                        break;
                    }
                    case 4: {   // 文件传输
                        System.out.println("发送文件...");
                        param.getOnFileRecive().OnFileRecive(bean);
                        break;
                    }
                    case 5: {   // 取消传输
                        param.getOnClientRead().OnClientRead(bean);
                        break;
                    }
                    default: {
                        break;
                    }
                } // switch
            } // while

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            System.exit(0);
        }
    }
}
