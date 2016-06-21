package com.betterjr.common.utils;

import java.io.Serializable;

public class TaobaoIpLocation implements Serializable {
	private static final long serialVersionUID = -6235188101185321031L;
	private int code;
	private IP2LocationInfo data;

	public int getCode() {
		return code;
	}

	public void setCode(int anCode) {
		code = anCode;
	}

	public IP2LocationInfo getData() {
		return data;
	}

	public void setData(IP2LocationInfo anData) {
		data = anData;
	}
}
