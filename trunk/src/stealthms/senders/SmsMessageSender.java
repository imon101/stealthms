package stealthms.senders;

import javax.microedition.io.*;
import javax.wireless.messaging.*;

import stealthms.utilities.TextFormatter;

public class SmsMessageSender extends MessageSender {
	public void sendMessage(String message, String phone) throws Exception {
		TextFormatter tf = new TextFormatter();
		sendingForm.setGaugeValue(0);
		message = tf.translit(message, false);
		sendingForm.setGaugeValue(2);
		if (System.getProperty("microedition.profiles").compareTo("MIDP-1.0") == 0) {
			try {
				Class.forName("com.siemens.mp.gsm.SMS");
				com.siemens.mp.gsm.SMS.send(phone, message);
				sendingForm.setGaugeValue(10);
			} catch (Throwable t) {
				throw new Exception("На Вашем телефоне отправка обычных SMS невозможна");
			}
		} else {
			String addr = "sms://" + phone;
			MessageConnection conn = (MessageConnection) Connector.open(addr);
			sendingForm.setGaugeValue(4);
			TextMessage msg = (TextMessage) conn.newMessage(MessageConnection.TEXT_MESSAGE);
			msg.setPayloadText(message);
			sendingForm.setGaugeValue(5);
			conn.send(msg);
			sendingForm.setGaugeValue(10);
		}
	}
}



