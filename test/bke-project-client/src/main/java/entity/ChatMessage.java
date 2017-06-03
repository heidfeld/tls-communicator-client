package entity;

import java.io.*;

public class ChatMessage implements Serializable {

	protected static final long serialVersionUID = 1112122200L;

	public static final int WHOISIN = 0, MESSAGE = 1, LOGOUT = 2, LOGIN = 3;
	private int type;
	private String message;

	public ChatMessage(int type, String message) {
		this.type = type;
		this.message = message;
	}

	public int getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}
}
