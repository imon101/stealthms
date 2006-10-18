package stealthms.senders;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import stealthms.storage.OptionsStorage;

public class E2SMessageSender extends MessageSender {
	
	private HttpConnection hcon;
	
	private static byte m_aaB[];
	private static byte m_baB[];
	
	public String stripHost(String url) {
		url = url.substring(7);
		int slash = url.indexOf("/");
		return url.substring(0, slash);
	}
	
	public void sendMessage(String message, String phone) throws Exception {
		String ServerAnswer=new String();
		try {
			ServerAnswer=SendHTTP(message,phone);
		} catch (SecurityException sex) {
			throw new IOException("Нет доступа к интернету!");
		} catch (IOException e) {
			throw new IOException("Ошибка соединения!");
		}
		String sOk=new String();
		try {
			sOk=ServerAnswer.substring(0,2).trim();
		} catch (IndexOutOfBoundsException iex) {
		}
		if (sOk.equals("ok")) {
			String strNumPages=new String();
			try {
				strNumPages=ServerAnswer.substring(3,ServerAnswer.length()).trim();
			} catch (IndexOutOfBoundsException iex) {
			}
			int numPages=0;
			try {
				numPages=Integer.valueOf(strNumPages).intValue();
			} catch (NumberFormatException ex) {
			}
			String resMsg=strNumPages;
			if (numPages>0) {
				switch (numPages) {
					case 1: {
						resMsg=resMsg+" страница";
						break;
					}
					case 2: {
						resMsg=resMsg+" страницы";
						break;
					}
					case 3: {
						resMsg=resMsg+" страницы";
						break;
					}
					case 4: {
						resMsg=resMsg+" страницы";
						break;
					}
					default: resMsg=resMsg+" страниц";
				}
				sendingForm.setGaugeLabel(resMsg);
			} else {
				throw new IOException("Не отправлено!\nОтвет: "+ServerAnswer);
			}
		} else {
			throw new IOException("Не отправлено!\nОтвет: "+ServerAnswer);
		}
		sendingForm.setGaugeValue(10);
	}
	
	private String SendHTTP(String message, String phone) throws Exception {
		hcon = (HttpConnection) Connector.open("http://sender.e2s.org.ua/1021/");
		sendingForm.setGaugeValue(2);
		hcon.setRequestMethod(HttpConnection.POST);
		OutputStream os=hcon.openOutputStream();
		String strRequest=getRequest(phone,message);
		sendingForm.setGaugeValue(5);
		os.write(strRequest.getBytes());
		sendingForm.setGaugeValue(7);
		hcon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		hcon.setRequestProperty("Content-Length", String.valueOf(strRequest.length()));
		int status = hcon.getResponseCode();
		if (status != HttpConnection.HTTP_OK) {
			throw new IOException("Неправильный код ответа " + Integer.toString(status));
		}
		sendingForm.setGaugeValue(9);
		InputStream is = hcon.openInputStream();
		int j = (int)hcon.getLength();
		String ServerAnswer = null;
		if(j > 0) {
			byte abyte0[] = new byte[j];
			is.read(abyte0);
			ServerAnswer = new String(abyte0);
		} else {
			StringBuffer stringbuffer = new StringBuffer();
			int k;
			while((k = is.read()) != -1) {
				stringbuffer.append((char)k);
				ServerAnswer = stringbuffer.toString();
			}
		}
		hcon.close();
		return ServerAnswer;
	}
	
	private String getRequest(String phone,String mess) {
//		String number = phone.substring(phone.length() - 7);
		String mobcode = phone.substring(phone.length() - 10, phone.length() - 7);
		String lat = (OptionsStorage.getTranslitStat() == 0) ? "no" : "yes";
		String split="no";
		String numlat="160";
		String numcyr="70";
		String encod="Win-1251";
		//Для ЮМС треба зробити опцію відсилання кирилицею
		if ((mobcode=="8067")||(mobcode=="8097")||(mobcode=="8096")) {
			lat="yes";
			split="yes";
		} else if ((mobcode=="8050")||(mobcode=="8095")||(mobcode=="8066")) {
			lat="yes";
			split="yes";
		} else if (mobcode=="8068") {
			numlat="350";
			numcyr="350";
		}
		String strRecipient=phone+"!"+"AUTO"+"!"+lat+"!"+split+"!"+User+"!"+numlat+"!"+numcyr+"!"+encod+";";
		return new String("act="+encodeString("send")+"&user="+encodeString(OptionsStorage.getE2SUser())+"&pass="+encodeString(OptionsStorage.getE2SPass())+"&rcpt="+encodeString(strRecipient)+"&mess="+encodeString("- "+mess)+"&");
	}
	static {
		m_aaB = new byte[255];
		m_baB = new byte[64];
		for(int i = 0; i < 255; i++)
			m_aaB[i] = -1;
		for(int j = 90; j >= 65; j--)
			m_aaB[j] = (byte)(j - 65);
		for(int k = 122; k >= 97; k--)
			m_aaB[k] = (byte)((k - 97) + 26);
		for(int l = 57; l >= 48; l--)
			m_aaB[l] = (byte)((l - 48) + 52);
		m_aaB[43] = 62;
		m_aaB[47] = 63;
		for(int i1 = 0; i1 <= 25; i1++)
			m_baB[i1] = (byte)(65 + i1);
		int j1 = 26;
		for(int k1 = 0; j1 <= 51; k1++) {
			m_baB[j1] = (byte)(97 + k1);
			j1++;
		}
		j1 = 52;
		for(int l1 = 0; j1 <= 61; l1++) {
			m_baB[j1] = (byte)(48 + l1);
			j1++;
		}
		m_baB[62] = 43;
		m_baB[63] = 47;
	}
	private static String encodeString(String s) {
		return new String(_aaBaB(_bStringaB(s)));
	}
	
	private static byte[] _bStringaB(String s) {
		try {
			ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
			DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);
			dataoutputstream.writeUTF(s);
			return bytearrayoutputstream.toByteArray();
		} catch (Exception ex) {
			return null;
		}
	}
	
	private static byte[] _aaBaB(byte abyte0[]) {
		int i;
		int j = (i = abyte0.length * 8) % 24;
		int k = i / 24;
		byte abyte1[] = null;
		if(j != 0)
			abyte1 = new byte[(k + 1) * 4];
		else
			abyte1 = new byte[k * 4];
//		boolean flag = false;
//		boolean flag1 = false;
//		boolean flag2 = false;
//		boolean flag3 = false;
//		boolean flag4 = false;
		int l = 0;
		int i1 = 0;
		int j1 = 0;
		for(j1 = 0; j1 < k; j1++) {
			i1 = j1 * 3;
			byte byte5 = abyte0[i1];
			byte byte8 = abyte0[i1 + 1];
			byte byte10 = abyte0[i1 + 2];
			byte byte3 = (byte)(byte8 & 0xf);
			byte byte0 = (byte)(byte5 & 3);
			l = j1 * 4;
			byte byte11 = (byte5 & 0xffffff80) != 0 ? (byte)(byte5 >> 2 ^ 0xc0) : (byte)(byte5 >> 2);
			byte byte14 = (byte8 & 0xffffff80) != 0 ? (byte)(byte8 >> 4 ^ 0xf0) : (byte)(byte8 >> 4);
			byte byte16 = (byte10 & 0xffffff80) != 0 ? (byte)(byte10 >> 6 ^ 0xfc) : (byte)(byte10 >> 6);
			abyte1[l] = m_baB[byte11];
			abyte1[l + 1] = m_baB[byte14 | byte0 << 4];
			abyte1[l + 2] = m_baB[byte3 << 2 | byte16];
			abyte1[l + 3] = m_baB[byte10 & 0x3f];
		}
		i1 = j1 * 3;
		l = j1 * 4;
		if(j == 8) {
			byte byte6;
			byte byte1 = (byte)((byte6 = abyte0[i1]) & 3);
			byte byte12 = (byte6 & 0xffffff80) != 0 ? (byte)(byte6 >> 2 ^ 0xc0) : (byte)(byte6 >> 2);
			abyte1[l] = m_baB[byte12];
			abyte1[l + 1] = m_baB[byte1 << 4];
			abyte1[l + 2] = 61;
			abyte1[l + 3] = 61;
		} else
			if(j == 16)	{
			byte byte7 = abyte0[i1];
			byte byte9;
			byte byte4 = (byte)((byte9 = abyte0[i1 + 1]) & 0xf);
			byte byte2 = (byte)(byte7 & 3);
			byte byte13 = (byte7 & 0xffffff80) != 0 ? (byte)(byte7 >> 2 ^ 0xc0) : (byte)(byte7 >> 2);
			byte byte15 = (byte9 & 0xffffff80) != 0 ? (byte)(byte9 >> 4 ^ 0xf0) : (byte)(byte9 >> 4);
			abyte1[l] = m_baB[byte13];
			abyte1[l + 1] = m_baB[byte15 | byte2 << 4];
			abyte1[l + 2] = m_baB[byte4 << 2];
			abyte1[l + 3] = 61;
			}
		return abyte1;
	}
}
