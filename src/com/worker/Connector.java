package com.worker;

import com.bean.ChatBean;
import com.connection.Connection;
import com.param.ClientParam;
import com.param.ServerParam;
import com.thread.ClientThread;
import com.thread.ServerThread;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by xiao555 on 2017/5/11.
 */
public class Connector implements Runnable {
    private static Socket clientSocket;
    private ClientParam param;
    private static ObjectOutputStream oos;

    public Connector(ClientParam param) throws IOException {
        this.param = param;
    }

    public void connect(String host,int port) throws IOException {
        clientSocket = new Socket("localhost", 8888);
        Connection connection = new Connection(clientSocket, param);
        if (param.getOnConnection() != null) {
            param.getOnConnection().onConnection(connection);
        }
    }

    @Override
    public void run() {
        try {
            ClientThread client = new ClientThread(clientSocket, param);
            new Thread(client).start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
