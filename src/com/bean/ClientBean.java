package com.bean;

import java.io.Serializable;
import java.net.Socket;

/**
 * Created by xiao555 on 2017/5/10.
 */
public class ClientBean implements Serializable {
    private String name;
    private transient Socket socket;

    public void setName(String name) {
        this.name = name;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getName() {
        return name;
    }

    public Socket getSocket() {
        return  socket;
    }
}
