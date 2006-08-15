package stealthms.senders;

import java.io.IOException;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import stealthms.utilities.TextFormatter;

public class HttpMessageSender extends MessageSender {
	private HttpConnection hcon;

	public String stripHost(String url) {
		url = url.substring(7);
		int slash = url.indexOf("/");
		return url.substring(0, slash);
	}

	private String numToHex(int num) {
		switch (num) {
		case 0:
			return "0";
		case 1:
			return "1";
		case 2:
			return "2";
		case 3:
			return "3";
		case 4:
			return "4";
		case 5:
			return "5";
		case 6:
			return "6";
		case 7:
			return "7";
		case 8:
			return "8";
		case 9:
			return "9";
		case 10:
			return "A";
		case 11:
			return "B";
		case 12:
			return "C";
		case 13:
			return "D";
		case 14:
			return "E";
		case 15:
			return "F";
		default:
			return "";
		}
	}

	private String urlEncode(String text) {
		StringBuffer out = new StringBuffer();
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			int num = (int) ch;
			// stupid hack for russian (unicode?)
			if (num > 848)
				num -= 848;
			if (num != 45 && num != 46 && !(num >= 48 && num <= 57)
					&& !(num >= 65 && num <= 90) && num != 95
					&& !(num >= 97 && num <= 122)) {
				// character should be encoded
				int rst = (int) (num / 16);
				int rem = num - rst * 16;
				out.append("%" + numToHex(rst) + numToHex(rem));
			} else
				out.append(ch);
		}
		return out.toString();
	}

	public void sendMessage(String message, String phone) throws Exception {
		TextFormatter tf = new TextFormatter();
			int numParts = splitext(tf.translit(message, false));
		for (int i = 1; i <= numParts; i++) {
			hcon = (HttpConnection) Connector.open(Url);
			sendingForm.setGaugeValue(2);
			hcon.setRequestMethod(HttpConnection.POST);
			hcon.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			String request = "from=" + User + "&phone=" + phone + "&message="
					+ urlEncode(messageParts[i]);
			hcon.setRequestProperty("Content-Length", Integer.toString(request
					.length()));
			hcon.setRequestProperty("Host", stripHost(Url));
			OutputStream os = hcon.openOutputStream();
			sendingForm.setGaugeValue(5);
			os.write(request.getBytes());
			os.flush();
			sendingForm.setGaugeValue(7);
			int status = hcon.getResponseCode();
			if (status != HttpConnection.HTTP_OK) {
				throw new IOException("Неправильный код ответа");
			}
			sendingForm.setGaugeValue(9);
		}
		sendingForm.setGaugeValue(10);
		hcon.close();
	}
}
