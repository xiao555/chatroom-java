package com.client;

import com.param.ClientParam;
import com.param.ServerParam;
import com.thread.ServerThread;
import com.worker.Connector;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by xiao555 on 2017/5/11.
 */
public class SocketClient {
    private ClientParam param;
    private Connector connector;
    public SocketClient(ClientParam param) throws IOException {
        this.param = param;
        this.connector = new Connector(param);
    }

    public void connect(String host,int port) throws IOException {
        this.connector.connect(host, port);
        new Thread(connector).start();
    }
}
