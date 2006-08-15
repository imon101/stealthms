package stealthms.storage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

public class OptionsStorage {
	private static String User;

	private static String Url;

	private static String SMUser;

	private static String SMPass;

	private static String Gates;

	private static String Copy;
	
	private static String[] Titles;
	
	private static int LastTitle;
	
	private static String Family;

	private static int TranslitStat;
	
	public static void saveSettings() {
		RecordStore recordStore;
		try {
			recordStore = RecordStore.openRecordStore("SMS", true);
		} catch (RecordStoreException e) {
			return;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream outputStream = new DataOutputStream(baos);
		try {
			outputStream.writeUTF(User);
			outputStream.writeUTF(Url);
			outputStream.writeUTF(SMUser);
			outputStream.writeUTF(SMPass);
			outputStream.writeUTF(Gates);
			outputStream.writeUTF(Copy);
			outputStream.writeInt(LastTitle);
			for (int i = 0; i < 15; i++) {
				outputStream.writeUTF(Titles[i]);
			}
			outputStream.writeUTF(Family);
			outputStream.writeInt(TranslitStat);
			byte[] bytes = baos.toByteArray();
			if (recordStore.getNumRecords() == 0)
				recordStore.addRecord(bytes, 0, bytes.length);
			else
				recordStore.setRecord(1, bytes, 0, bytes.length);
			outputStream.close();
			recordStore.closeRecordStore();
		} catch (Exception e) {
		}
	}

	public static void loadSettings() {
		RecordStore recordStore;
		Titles = new String[15];
		try {
			recordStore = RecordStore.openRecordStore("SMS", false);
		} catch (RecordStoreException rse) {
			User = "somebody@cmc.ru";
			Url = "smtp.umc.ua";
			SMUser = "";
			SMPass = "";
			Gates = "8050:38050%@sms.umc.com.ua;8095:38095%@sms.umc.com.ua;8066:38066%@sms.jeans.com.ua;8097:38097%@sms.kyivstar.net;8067:38067%@sms.kyivstar.net;8096:38096%@sms.kyivstar.net;8068:38068%@sms.mobi.ua";
			Copy = "";
			LastTitle = -1;
			for (int i = 0; i < 15; i++) {
				Titles[i] = "";
			}
			Family = "8063%";
			TranslitStat = 0;
			saveSettings();
			return;
		}
		try {
			byte[] bytes = recordStore.getRecord(1);
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			DataInputStream inputStream = new DataInputStream(bais);
			User = inputStream.readUTF();
			Url = inputStream.readUTF();
			SMUser = inputStream.readUTF();
			SMPass = inputStream.readUTF();
			Gates = inputStream.readUTF();
			Copy = inputStream.readUTF();
			LastTitle = inputStream.readInt();
			for (int i = 0; i < 15; i++) {
				Titles[i] = inputStream.readUTF();
			}
			Family = inputStream.readUTF();
			TranslitStat = inputStream.readInt();			
			inputStream.close();
			recordStore.closeRecordStore();
		} catch (Exception e) {
		}
	}

	public static String getCopy() {
		return Copy;
	}

	public static void setCopy(String copy) {
		Copy = copy;
	}

	public static String getGates() {
		return Gates;
	}

	public static void setGates(String gates) {
		Gates = gates;
	}

	public static String getSMPass() {
		return SMPass;
	}

	public static void setSMPass(String pass) {
		SMPass = pass;
	}

	public static String getSMUser() {
		return SMUser;
	}

	public static void setSMUser(String user) {
		SMUser = user;
	}

	public static String getUrl() {
		return Url;
	}

	public static void setUrl(String url) {
		Url = url;
	}

	public static String getUser() {
		return User;
	}

	public static void setUser(String user) {
		User = user;
	}

	public static int getLastTitle() {
		return LastTitle;
	}

	public static void setLastTitle(int lastTitle) {
		LastTitle = lastTitle;
	}

	public static String[] getTitles() {
		return Titles;
	}

	public static void setTitles(String[] titles) {
		Titles = titles;
	}
	
	public static String getFamily() {
		return Family;
	}

	public static void setFamily(String family) {
		Family = family;
	}

	public static int getTranslitStat() {
		return TranslitStat;
	}

	public static void setTranslitStat(int translitstat) {
		TranslitStat = translitstat;
	}		
}
