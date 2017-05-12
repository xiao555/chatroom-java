package com.param;

import com.bean.ChatBean;
import com.inter.*;

/**
 * Created by xiao555 on 2017/5/11.
 */
public class ClientParam extends Param {
    private OnConnection onConnection;
    private OnConnError onConnError;

    public ClientParam() {
        // TODO Auto-generated constructor stub
    }

    public OnConnError getOnConnError() {
        return onConnError;
    }


    public void setOnConnError(OnConnError onConnError) {
        this.onConnError = onConnError;
    }


    public void setOnConnection(OnConnection onConnection) {
        this.onConnection = onConnection;
    }

    @Override
    public OnAccept getOnAccept() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OnConnection getOnConnection() {
        // TODO Auto-generated method stub
        return this.onConnection;
    }


    @Override
    public boolean isServerParam() {
        // TODO Auto-generated method stub
        return false;
    }
}
