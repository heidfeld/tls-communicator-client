package gui;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {

	private String date;
	private String message;
	private String author;

	public Message() {
	}

	public Message(String date, String message, String author) {
		this.date = date;
		this.message = message;
		this.author = author;
	}
	
	public Message(Date date, String message, String author) {
		setDate(date);
		this.message = message;
		this.author = author;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	public void setDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-hh HH:mm");
		String textDate = sdf.format(date);
		this.date = textDate;
	}

	@Override
	public String toString() {
		return author + "[" + date + "] : " + message;
		
	}
	
}
