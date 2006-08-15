package stealthms.senders;

import stealthms.StealthMS;
import stealthms.forms.Sending;

public class MessageSender {
	protected String Url;

	protected Sending sendingForm;

	protected String[] messageParts;

	protected String User;
	
	protected StealthMS midlet;

	protected int splitext(String text) {
		int maxlen = 153 - User.length();
		messageParts = new String[10];
		int currentPart = 1;
		if (text.length() <= (maxlen + 6)) {
			messageParts[currentPart] = text;
			return 1;
		}
		int currentIndex = 0;
		int lastSpace = 0;
		while ((text.length() - currentIndex) > maxlen) {
			lastSpace = text.lastIndexOf(' ', currentIndex + maxlen);
			messageParts[currentPart] = new String(text.substring(currentIndex,
					lastSpace));
			currentIndex = lastSpace + 1;
			currentPart++;
		}
		messageParts[currentPart] = text.substring(currentIndex, text.length());
		for (int i = 1; i <= currentPart; i++) {
			messageParts[i] = "(" + Integer.toString(i) + "/"
					+ Integer.toString(currentPart) + ") " + messageParts[i];
		}
		return currentPart;
	}
	
	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public String getUser() {
		return User;
	}

	public void setUser(String user) {
		User = user;
	}

	public Sending getSendingForm() {
		return sendingForm;
	}

	public void setSendingForm(Sending sendingForm) {
		this.sendingForm = sendingForm;
	}

	public StealthMS getMidlet() {
		return midlet;
	}

	public void setMidlet(StealthMS midlet) {
		this.midlet = midlet;
	}
}
