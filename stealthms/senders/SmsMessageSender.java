package stealthms.senders;

import com.siemens.mp.gsm.SMS;
import javax.microedition.io.*;
import javax.wireless.messaging.*;

import stealthms.utilities.Transliterator;

public class SmsMessageSender extends MessageSender {
	public void sendMessage(String message, String phone) throws Exception {
		Transliterator tr = new Transliterator();
		sendingForm.setGaugeValue(0);
		message = tr.translit(message);
		sendingForm.setGaugeValue(2);
		if (System.getProperty("microedition.profiles").compareTo("MIDP-1.0") == 0) {
			SMS.send(phone, message);
			sendingForm.setGaugeValue(10);
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



