package com.thread;

import com.bean.ChatBean;
import com.connection.Connection;
import com.param.ServerParam;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by xiao555 on 2017/5/10.
 */
public class ServerThread implements Runnable {
    private ServerParam param;
    private Socket client;
    private ChatBean bean;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public ServerThread(Socket client, ServerParam param) {
        this.client = client;
        this.param = param;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // 读取信息
                ois = new ObjectInputStream(client.getInputStream());
                bean = (ChatBean)ois.readObject();
                Connection connection = new Connection(bean, client, param);
                switch (bean.getType()) {
                    case 0: {   // 上线
                        param.getOnAccept().onAccept(connection);
                        break;
                    }
                    case -1: {  // 下线
                        param.getOnClose().onClose(connection);
                        return;
                    }
                    case 1: {   // 群发
                        param.getOnRead().onRead(connection);
                        break;
                    }
                    case 2: {   // 私聊
                        param.getOnRead().onRead(connection);
                        break;
                    }
                    case 3: {   // 接收文件请求
                        param.getOnRead().onRead(connection);
                        break;
                    }
                    case 4: {   // 确认接收文件
                        param.getOnRead().onRead(connection);
                        break;
                    }
                    case 5: {   // 取消接收文件
                        param.getOnRead().onRead(connection);
                        break;
                    }
                    default: {
                        break;
                    }
                } // switch
            } // while

        } catch (EOFException e) { // 捕捉客户端异常退出
            // TODO Auto-generated catch block
            try {
                client.close();
            } catch (IOException err) {
                // TODO Auto-generated catch block
                err.printStackTrace();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private void close() {
        if (oos != null) {
            try {
                oos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (ois != null) {
            try {
                ois.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
