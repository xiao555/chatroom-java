package com.inter;

import com.connection.Connection;

public interface OnReadError {
	public void onReadError(Connection conn, Exception e);
}
