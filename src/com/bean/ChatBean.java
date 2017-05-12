package com.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by xiao555 on 2017/5/10.
 */
public class ChatBean implements Serializable {
    private int type;
    private HashSet<String> clients;
    private HashSet<String> to;
    public HashMap<String, ClientBean> onlines;

    private String message;
    private String timer;
    private String name;
    private String fileName;
    private int size;
    private String ip;
    private int port;

    public ChatBean(){};

    public ChatBean(int type, String message, HashSet<String> to, HashMap<String, ClientBean> onlines){
        this.type = type;
        this.message = message;
        this.to = to;
        this.onlines = onlines;
        HashSet<String> users = new HashSet<String>();
        users.addAll(onlines.keySet());
        this.clients = users;
    }


    public HashSet<String> getTo() {
        return to;
    }

    public void setTo(HashSet<String> to) {
        this.to = to;
    }


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public HashSet<String> getClients() {
        return clients;
    }

    public void setClients(HashSet<String> clients) {
        this.clients = clients;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public HashMap<String, ClientBean> getOnlines() {
        return onlines;
    }

    public void setOnlines(HashMap<String, ClientBean> onlines) {
        this.onlines = onlines;
    }
}
