package com.param;

import com.bean.ClientBean;
import com.inter.OnAccept;
import com.inter.OnAcceptError;
import com.inter.OnConnection;
import com.sun.xml.internal.bind.v2.TODO;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;

/**
 * Created by xiao555 on 2017/5/10.
 */
public class ServerParam extends Param {
    private String host;
    private int port;
    private int backlog = 32;
    private static HashMap<String, ClientBean> onlines; // 保存在线连接
    private OnAccept onAccept;
    private OnAcceptError onAcceptError;

    public ServerParam(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static HashMap<String, ClientBean> getOnlines() {
        return onlines;
    }

    public static void setOnlines(HashMap<String, ClientBean> onlines) {
        ServerParam.onlines = onlines;
    }

    public int getBacklog() {
        return backlog;
    }

    public void setBacklog(int backlog) {
        this.backlog = backlog;
    }


    public void setOnAccept(OnAccept onAccept) {
        this.onAccept = onAccept;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public OnAccept getOnAccept() {
        // TODO Auto-generated method stub
        return this.onAccept;
    }

    public OnAcceptError getOnAcceptError() {
        return onAcceptError;
    }

    public void setOnAcceptError(OnAcceptError onAcceptError) {
        this.onAcceptError = onAcceptError;
    }

    public OnConnection getOnConnection() {
        // TODO Auto-generated method stub
        return null;
    }


    public boolean isServerParam() {
        // TODO Auto-generated method stub
        return true;
    }
}
