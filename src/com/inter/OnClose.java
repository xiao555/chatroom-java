package com.inter;


import com.bean.ChatBean;
import com.connection.Connection;

public interface OnClose {
	public void onClose(Connection conn);
}
