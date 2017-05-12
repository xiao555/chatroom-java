package com.param;


import com.inter.*;

/**
 * Created by xiao555 on 2017/5/10.
 */
public abstract class Param {
    private OnRead onRead;
    private OnReadError onReadError;
    private OnWrite onWrite;
    private OnWriteError onWriteError;
    private OnClose onClose;
    private OnClientRead onClientRead;
    private OnFileRead onFileRead;
    private OnFileRecive onFileRecive;

    public OnFileRecive getOnFileRecive() {
        return onFileRecive;
    }
    public void setOnFileRecive(OnFileRecive onFileRecive) {
        this.onFileRecive = onFileRecive;
    }
    public OnFileRead getOnFileRead() {
        return onFileRead;
    }
    public void setOnFileRead(OnFileRead onFileRead) {
        this.onFileRead = onFileRead;
    }
    public OnClientRead getOnClientRead() {
        return onClientRead;
    }
    public void setOnClientRead(OnClientRead onClientRead) {
        this.onClientRead = onClientRead;
    }
    public OnReadError getOnReadError() {
        return onReadError;
    }
    public void setOnReadError(OnReadError onReadError) {
        this.onReadError = onReadError;
    }
    public OnWriteError getOnWriteError() {
        return onWriteError;
    }
    public void setOnWriteError(OnWriteError onWriteError) {
        this.onWriteError = onWriteError;
    }
    public OnRead getOnRead() {
        return onRead;
    }
    public void setOnRead(OnRead onRead) {
        this.onRead = onRead;
    }
    public OnWrite getOnWrite() {
        return onWrite;
    }
    public void setOnWrite(OnWrite onWrite) {
        this.onWrite = onWrite;
    }
    public OnClose getOnClose() {
        return onClose;
    }
    public void setOnClose(OnClose onClose) {
        this.onClose = onClose;
    }

    public abstract OnAccept getOnAccept();
    public abstract OnConnection getOnConnection();
    public abstract boolean isServerParam();
}
