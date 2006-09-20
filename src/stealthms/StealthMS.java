package stealthms;

import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.*;

import stealthms.forms.*;
import stealthms.senders.*;
import stealthms.storage.*;

public class StealthMS extends MIDlet implements Runnable {
	private boolean errorState;

	// forms
	private Message messageText;

	private Phone phoneText;

	private Sending sendingForm;

	private Options optsForm;

	private About aboutForm;

	private HistoryList historyList;

	private HistoryView historyView;
	
	private boolean regularSending;

	protected void pauseApp() {
	}

	protected void startApp() {
		OptionsStorage.loadSettings();
		// initialize forms
		messageText = new Message(this);
		phoneText = new Phone(this);
		optsForm = new Options(this);
		sendingForm = new Sending(this);
		aboutForm = new About(this);
		// go to message
		displayMessage();
	}

	protected void destroyApp(boolean b) {
	}

	public void exitRequested() {
		destroyApp(false);
		notifyDestroyed();
	}
	
	private boolean isInMask(String phone, String mask) {
		int lastIndex = 0;
		while (lastIndex < mask.length()) {
			int newIndex = mask.indexOf(";", lastIndex);
			if (newIndex == -1) {
				newIndex = mask.length();
			}
			String currentNum = mask.substring(lastIndex, newIndex);
			lastIndex = newIndex + 1;
			int currentPercentPos = currentNum.indexOf("%");
			if (currentPercentPos == -1) {
				currentPercentPos = currentNum.length();
			}
			String currentPrefix = currentNum.substring(0, currentPercentPos);
			if (phone.startsWith(currentPrefix)) {
				return true;
			}
		}
		return false;
	}

	public void run() {
		errorState = false;
		String message = messageText.getString();
		String phone = phoneText.getString();
//		boolean httpMode = OptionsStorage.getUrl().startsWith("http://");
		boolean httpMode = isInMask(phone, OptionsStorage.getHttp());
		try {
			if (isInMask(phone, OptionsStorage.getFamily()) || isRegularSending()) {
				SmsMessageSender messSender = new SmsMessageSender();
				messSender.setMidlet(this);
				messSender.setSendingForm(sendingForm);
				messSender.sendMessage(message, phone);
			} else if (!httpMode) {
				SmtpMessageSender messSender = new SmtpMessageSender();
				messSender.setMidlet(this);
				messSender.setUrl(OptionsStorage.getUrl());
				messSender.setCopy(OptionsStorage.getCopy());
				messSender.setGates(OptionsStorage.getGates());
				messSender.setSMUser(OptionsStorage.getSMUser());
				messSender.setSMPass(OptionsStorage.getSMPass());
				messSender.setUser(OptionsStorage.getUser());
				messSender.setSendingForm(sendingForm);
				messSender.sendMessage(message, phone);
			} else {
				HttpMessageSender messSender = new HttpMessageSender();
				messSender.setMidlet(this);
				messSender.setUrl(OptionsStorage.getUrl());
				messSender.setUser(OptionsStorage.getUser());
				messSender.setSendingForm(sendingForm);
				messSender.sendMessage(message, phone);
			}
		} catch (Exception e) {
			String error = e.getMessage();
			if (error.compareTo("Отменено") != 0) {
				if (error.length() > 50) {
					error = error.substring(0, 50);
				}
				sendingForm.setGaugeLabel("Ошибка: " + error);
				errorState = true;
				AlertType.ERROR.playSound(Display.getDisplay(this));
			}
		}
		if (!errorState) {
			HistoryStorage.addNewMessage(message, phone);
			sendingForm.setGaugeLabel("Отправлено");
			sendingForm.setSuccessState();
		} else {
			sendingForm.setErrorState();
		}
	}

	public void displayMessage(String message) {
		messageText.setString(message);
		Display.getDisplay(this).setCurrent(messageText);
	}

	public void displayMessage() {
		Display.getDisplay(this).setCurrent(messageText);
	}

	public void displayPhone() {
		Display.getDisplay(this).setCurrent(phoneText);
	}

	public void displayOptions() {
		optsForm.setUrl(OptionsStorage.getUrl());
		optsForm.setUser(OptionsStorage.getUser());
		optsForm.setSMUser(OptionsStorage.getSMUser());
		optsForm.setSMPass(OptionsStorage.getSMPass());
		optsForm.setCopy(OptionsStorage.getCopy());
		optsForm.setGates(OptionsStorage.getGates());
		optsForm.setFamily(OptionsStorage.getFamily());
		optsForm.setHttp(OptionsStorage.getHttp());
		Display.getDisplay(this).setCurrent(optsForm);
	}

	public void displayAbout() {
		Display.getDisplay(this).setCurrent(aboutForm);
	}

	public void displayHistoryList(boolean reload) {
		if (reload) {
			historyList = new HistoryList(this);
		}
		Display.getDisplay(this).setCurrent(historyList);
	}

	public void displayHistoryView(String HPhone, String HMessage) {
		historyView = new HistoryView(this, HPhone, HMessage);
		Display.getDisplay(this).setCurrent(historyView);
	}

	public void displaySending(boolean regular) {
		sendingForm.setGaugeValue(0);
		sendingForm.setGaugeLabel("Передача...");
		sendingForm.setSendingState();
		Display.getDisplay(this).setCurrent(sendingForm);
		Thread sendingThread = new Thread(this);
		setRegularSending(regular);
		sendingThread.start();
	}

	public boolean isErrorState() {
		return errorState;
	}

	public void setErrorState(boolean errorState) {
		this.errorState = errorState;
	}

	public boolean isRegularSending() {
		return regularSending;
	}

	public void setRegularSending(boolean regularSending) {
		this.regularSending = regularSending;
	}
}