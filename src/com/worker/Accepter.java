package com.worker;

import com.param.ServerParam;
import com.server.SocketServer;
import com.thread.ServerThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by xiao555 on 2017/5/10.
 */
public class Accepter implements Runnable {
    private ServerSocket ss;
    private ServerParam param;

    public Accepter(ServerParam param, ServerSocket ss) throws IOException {
        this.param = param;
        this.ss = ss;
    }

    @Override
    public void run() {
        try {
            while (true) {
                ServerThread client = new ServerThread(ss.accept(), param);
                new Thread(client).start();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
