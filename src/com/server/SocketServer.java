package com.server;

import com.bean.ClientBean;
import com.param.ServerParam;
import com.worker.Accepter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by xiao555 on 2017/5/10.
 */
public class SocketServer {
    private ServerParam param;
    private ServerSocket ss;

    public SocketServer(ServerParam serverParam) {
        this.param = serverParam;
    }

    public void start() throws IOException {
        try {
            ss = new ServerSocket(param.getPort());
            Accepter accepter = new Accepter(param, ss);
            new Thread(accepter).start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
