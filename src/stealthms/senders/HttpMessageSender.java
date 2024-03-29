package stealthms.senders;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import stealthms.storage.OptionsStorage;
import stealthms.utilities.TextFormatter;

public class HttpMessageSender extends MessageSender {
	final static String[] hex = {
		"%00", "%01", "%02", "%03", "%04", "%05", "%06", "%07",
		"%08", "%09", "%0a", "%0b", "%0c", "%0d", "%0e", "%0f",
		"%10", "%11", "%12", "%13", "%14", "%15", "%16", "%17",
		"%18", "%19", "%1a", "%1b", "%1c", "%1d", "%1e", "%1f",
		"%20", "%21", "%22", "%23", "%24", "%25", "%26", "%27",
		"%28", "%29", "%2a", "%2b", "%2c", "%2d", "%2e", "%2f",
		"%30", "%31", "%32", "%33", "%34", "%35", "%36", "%37",
		"%38", "%39", "%3a", "%3b", "%3c", "%3d", "%3e", "%3f",
		"%40", "%41", "%42", "%43", "%44", "%45", "%46", "%47",
		"%48", "%49", "%4a", "%4b", "%4c", "%4d", "%4e", "%4f",
		"%50", "%51", "%52", "%53", "%54", "%55", "%56", "%57",
		"%58", "%59", "%5a", "%5b", "%5c", "%5d", "%5e", "%5f",
		"%60", "%61", "%62", "%63", "%64", "%65", "%66", "%67",
		"%68", "%69", "%6a", "%6b", "%6c", "%6d", "%6e", "%6f",
		"%70", "%71", "%72", "%73", "%74", "%75", "%76", "%77",
		"%78", "%79", "%7a", "%7b", "%7c", "%7d", "%7e", "%7f",
		"%80", "%81", "%82", "%83", "%84", "%85", "%86", "%87",
		"%88", "%89", "%8a", "%8b", "%8c", "%8d", "%8e", "%8f",
		"%90", "%91", "%92", "%93", "%94", "%95", "%96", "%97",
		"%98", "%99", "%9a", "%9b", "%9c", "%9d", "%9e", "%9f",
		"%a0", "%a1", "%a2", "%a3", "%a4", "%a5", "%a6", "%a7",
		"%a8", "%a9", "%aa", "%ab", "%ac", "%ad", "%ae", "%af",
		"%b0", "%b1", "%b2", "%b3", "%b4", "%b5", "%b6", "%b7",
		"%b8", "%b9", "%ba", "%bb", "%bc", "%bd", "%be", "%bf",
		"%c0", "%c1", "%c2", "%c3", "%c4", "%c5", "%c6", "%c7",
		"%c8", "%c9", "%ca", "%cb", "%cc", "%cd", "%ce", "%cf",
		"%d0", "%d1", "%d2", "%d3", "%d4", "%d5", "%d6", "%d7",
		"%d8", "%d9", "%da", "%db", "%dc", "%dd", "%de", "%df",
		"%e0", "%e1", "%e2", "%e3", "%e4", "%e5", "%e6", "%e7",
		"%e8", "%e9", "%ea", "%eb", "%ec", "%ed", "%ee", "%ef",
		"%f0", "%f1", "%f2", "%f3", "%f4", "%f5", "%f6", "%f7",
		"%f8", "%f9", "%fa", "%fb", "%fc", "%fd", "%fe", "%ff"
	};
	
	private HttpConnection hcon;
	
	public String stripHost(String url) {
		url = url.substring(7);
		int slash = url.indexOf("/");
		return url.substring(0, slash);
	}
	
	public static String encode(String s) {
		StringBuffer sbuf = new StringBuffer();
		int len = s.length();
		for (int i = 0; i < len; i++) {
			int ch = s.charAt(i);
			if ('A' <= ch && ch <= 'Z') {		// 'A'..'Z'
				sbuf.append((char)ch);
			} else if ('a' <= ch && ch <= 'z') {	// 'a'..'z'
				sbuf.append((char)ch);
			} else if ('0' <= ch && ch <= '9') {	// '0'..'9'
				sbuf.append((char)ch);
			} else if (ch == ' ') {			// space
				sbuf.append('+');
			} else if (ch == '-' || ch == '_'		// unreserved
				|| ch == '.' || ch == '!'
				|| ch == '~' || ch == '*'
				|| ch == '\'' || ch == '('
				|| ch == ')') {
				sbuf.append((char)ch);
			} else if (ch <= 0x007f) {		// other ASCII
				sbuf.append(hex[ch]);
			} else if (ch <= 0x07FF) {		// non-ASCII <= 0x7FF
				sbuf.append(hex[0xc0 | (ch >> 6)]);
				sbuf.append(hex[0x80 | (ch & 0x3F)]);
			} else {					// 0x7FF < ch <= 0xFFFF
				sbuf.append(hex[0xe0 | (ch >> 12)]);
				sbuf.append(hex[0x80 | ((ch >> 6) & 0x3F)]);
				sbuf.append(hex[0x80 | (ch & 0x3F)]);
			}
		}
		return sbuf.toString();
	}
	
	private String getNickFromMail(String mail) {
		int dogIndex = mail.indexOf("@");
		if (dogIndex != -1) {
			return mail.substring(0, dogIndex);
		}
		return mail;
		
	}
	
 	private String getRandomCode() {
 		HttpRandom generator = new HttpRandom();
 		return String.valueOf(generator.nextHttpInt());
 	}
	
	public void sendMessage(String message, String phone, String site) throws Exception {
		TextFormatter tf = new TextFormatter();
		int numParts = splitext(tf.translit(message, false), getNickFromMail(User).length());
		try {
			for (int i = 1; i <= numParts; i++) {
                                if (site=="KS")
                                        sendPartKS(phone,i);
                                if (site=="MTS")
                                        sendPartMTS(phone,i);
			}
		} catch (SecurityException sex) {
			throw new IOException("��� ������� � ���������!");
		} catch (IOException e) {
			if (e.getMessage().startsWith("������������ ��� ������")) {
				throw new IOException(e.getMessage());
			} else {
				throw new IOException("������ ����������!");
			}
		}
		sendingForm.setGaugeValue(10);
		hcon.close();
	}

	private void sendPartMTS(String phone,int curPart) throws Exception {
		String number = phone.substring(phone.length() - 7);
		String mobcode = phone.substring(phone.length() - 10, phone.length() - 7);
		hcon = (HttpConnection) Connector.open("http://www.tver.mts.ru/cgi-bin/cgi.exe?function=sms_send");
		sendingForm.setGaugeValue(2);
		hcon.setRequestMethod(HttpConnection.POST);
                String request = "MMObjectType=0&MMObjectID=&To=7" + mobcode + number +
			"&Msg=" + encode(getNickFromMail(User) + "\n" + messageParts[curPart])+
                        "&count="+String.valueOf(messageParts[curPart].length())+
                        "&Hour=23&Min=59&Day=31&Mon=12&Year=2008&Lang=2";
		hcon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; uk-UA; rv:1.8.1.1) Gecko/20061204 Firefox/2.0.0.1");
                hcon.setRequestProperty("Accept", "text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
                hcon.setRequestProperty("Accept-Language", "uk-ua,ua;q=0.8,ru-ru;q=0.7,ru;q=0.5,en-us;q=0.3,en;q=0.2");
                hcon.setRequestProperty("Accept-Charset", "windows-1251");
                hcon.setRequestProperty("Referer", "http://www.tver.mts.ru/cgi-bin/cgi.exe?function=sms_send");
		hcon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		hcon.setRequestProperty("Content-Length", String.valueOf(request.length()));
		sendingForm.setGaugeValue(3);
		OutputStream os = hcon.openOutputStream();
		sendingForm.setGaugeValue(5);
		os.write(request.getBytes());
		os.close();
		sendingForm.setGaugeValue(7);
		int status = hcon.getResponseCode();
		hcon.close();
		if (status != HttpConnection.HTTP_OK) {
			throw new IOException("������������ ��� ������ " + Integer.toString(status));
		}
		sendingForm.setGaugeValue(9);
	}
 	private void sendPartKS(String phone,int curPart) throws Exception {
 		String number = phone.substring(phone.length() - 7);
 		String mobcode = phone.substring(phone.length() - 10, phone.length() - 7);
 		hcon = (HttpConnection) Connector.open("http://www.kyivstar.net/_sms.html");
 		sendingForm.setGaugeValue(2);
 		hcon.setRequestMethod(HttpConnection.POST);
 		String rCode = getRandomCode();
 		String lat = (OptionsStorage.getTranslitStat() == 0) ? "0" : "1";
 		String request = "submitted=true&number=" + number + "&mobcode=" +
 			mobcode + "&antispam=" + rCode + "&lang=ru&lat=" + lat + "&message=" +
 			encode(getNickFromMail(User) + "\n" + messageParts[curPart]);
 		hcon.setRequestProperty("User-Agent", "Opera/9.00 (Windows NT 5.1; U; ru)");
 		hcon.setRequestProperty("Cookie", "code=" + rCode);
 		hcon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
 		hcon.setRequestProperty("Content-Length", String.valueOf(request.length()));
 		sendingForm.setGaugeValue(3);
 		OutputStream os = hcon.openOutputStream();
 		sendingForm.setGaugeValue(5);
 		os.write(request.getBytes());
 		os.close();
 		sendingForm.setGaugeValue(7);
 		int status = hcon.getResponseCode();
 		if (status != HttpConnection.HTTP_MOVED_TEMP) {
 			throw new IOException("������������ ��� ������ " + Integer.toString(status));
 		}
 		sendingForm.setGaugeValue(9);
 	}
 
 	private class HttpRandom extends Random {
 		public int nextHttpInt() {
 			return 1000 + nextIntWithLimit(9000);
 		}
 		
 		private int nextIntWithLimit(int n) {
 			if (n <= 0) {
 				throw new IllegalArgumentException("n must be positive");
 			}
 			if ((n & -n) == n) { // i.e., n is a power of 2
 				return (int)((n * (long)next(31)) >> 31);
 			}
 			int bits, val;
 			do {
 				bits = next(31);
 				val = bits % n;
 			} while (bits - val + (n-1) < 0);
 			return val;
 		}
 	}
	
}
