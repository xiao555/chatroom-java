package com.inter;

import com.connection.Connection;

public interface OnWriteError {
	public void onWriteError(Connection conn, Exception e);
}
