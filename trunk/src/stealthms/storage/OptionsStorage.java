package stealthms.storage;

import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.rms.*;

public class OptionsStorage {
	private static Hashtable options;
	
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
			outputStream.writeInt(options.size());
			for (Enumeration hashKeys = options.keys(); hashKeys.hasMoreElements();) {
				String currentKey = (String)hashKeys.nextElement();
				String currentValue = (String)options.get(currentKey);
				outputStream.writeUTF(currentKey);
				outputStream.writeUTF(currentValue);
			}
			byte[] bytes = baos.toByteArray();
			if (recordStore.getNumRecords() == 0)
				recordStore.addRecord(bytes, 0, bytes.length);
			else
				recordStore.setRecord(1, bytes, 0, bytes.length);
			outputStream.close();
			recordStore.closeRecordStore();
		} catch (Exception e) {}
	}
	
	public static void loadSettings() {
		RecordStore recordStore;
		options = new Hashtable();
		try {
			recordStore = RecordStore.openRecordStore("SMS", false);
		} catch (RecordStoreException rse) {
			setUser("somebody@yandex.ru");
			setUrl("smtp.umc.ua");
			setSMUser("");
			setSMPass("");
//#if RU
//#                         setGates("960:7960%@sms.beemail.ru;903:7903%@sms.beemail.ru;905:7905%@sms.beemail.ru;906:7906%@sms.beemail.ru;909:7909%@sms.beemail.ru;961:7961%@sms.beemail.ru;962:7962%@sms.beemail.ru;963:7963%@sms.beemail.ru;917:7917%@volgase.mts.ru;919:7919%@volgase.mts.ru;927:7927%@sms.mgsm.ru;904:7904%@smsline.bashcell.com;913:7913%@sms.mtslife.ru;922:7922%@sms.ugsm.ru;923:7923%@sms.megafonsib.ru;924:7924%@sms.megafondv.ru;920:7920%@sms.megafoncenter.ru");
//#else
			setGates("8050:38050%@sms.umc.ua;8095:38095%@sms.umc.ua;8066:38066%@sms.umc.ua;8099:38099%@sms.umc.ua;8097:38097%@sms.kyivstar.net;8067:38067%@sms.kyivstar.net;8096:38096%@sms.kyivstar.net;8098:38098%@sms.kyivstar.net;8068:38068%@sms.beeline.ua");
//#endif
			setCopy("");
			setLastTitle(-1);
			setFamily("");
			setKS("8063%;8093%");
			setMTS("+7910%;+7915%;+7917%;+7916%");
			setTranslitStat(1);
			setAuthLogin(1);
			setE2S("");
			setE2SUser("");
			setE2SPass("");
			setLastMessage("");
//#if RU
//# 			setCountryPrefix("+7");
//#else
			setCountryPrefix("+3");
//#endif
			saveSettings();
			return;
		}
		try {
			byte[] bytes = recordStore.getRecord(1);
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			DataInputStream inputStream = new DataInputStream(bais);
			int hashSize = inputStream.readInt();
			for (int i = 0; i < hashSize; i++) {
				String key = inputStream.readUTF();
				String value = inputStream.readUTF();
				options.put(key, value);
			}
			inputStream.close();
			recordStore.closeRecordStore();
		} catch (Exception e) {}
	}
	
	public static String getCopy() {
		return (String)options.get("Copy");
	}
	
	public static void setCopy(String copy) {
		options.put("Copy", copy);
	}
	
	public static String getGates() {
		return (String)options.get("Gates");
	}
	
	public static void setGates(String gates) {
		options.put("Gates", gates);
	}
	
	public static String getSMPass() {
		return (String)options.get("SMPass");
	}
	
	public static void setSMPass(String pass) {
		options.put("SMPass", pass);
	}
	
	public static String getSMUser() {
		return (String)options.get("SMUser");
	}
	
	public static void setSMUser(String user) {
		options.put("SMUser", user);
	}
	
	public static String getUrl() {
		return (String)options.get("Url");
	}
	
	public static void setUrl(String url) {
		options.put("Url", url);
	}
	
	public static String getUser() {
		return (String)options.get("User");
	}
	
	public static void setUser(String user) {
		options.put("User", user);
	}
	
	public static int getLastTitle() {
		return Integer.parseInt((String)options.get("LastTitle"));
	}
	
	public static void setLastTitle(int lastTitle) {
		options.put("LastTitle", Integer.toString(lastTitle));
	}
	
	public static String getFamily() {
		return (String)options.get("Family");
	}
	
	public static void setFamily(String family) {
		options.put("Family", family);
	}
	
	public static String getKS() {
		return (String)options.get("HTTP");
	}
	
	public static String getMTS() {
		return (String)options.get("MTS");
	}

        public static void setKS(String family) {
		options.put("HTTP", family);
	}
	
        public static void setMTS(String family) {
		options.put("MTS", family);
	}
	
	public static int getTranslitStat() {
		return Integer.parseInt((String)options.get("TranslitStat"));
	}
	
	public static void setTranslitStat(int translitstat) {
		options.put("TranslitStat", Integer.toString(translitstat));
	}
	
	public static int getAuthLogin() {
		return Integer.parseInt((String)options.get("AuthLogin"));
	}
	
	public static void setAuthLogin(int authlogin) {
		options.put("AuthLogin", Integer.toString(authlogin));
	}
	
	public static String getE2SUser() {
		return (String)options.get("E2SUser");
	}
	
	public static void setE2SUser(String E2SUser) {
		options.put("E2SUser", E2SUser);
	}
	
	public static String getE2SPass() {
		return (String)options.get("E2SPass");
	}
	public static void setE2SPass(String E2SPass) {
		options.put("E2SPass", E2SPass);
	}
	
	public static String getE2S() {
		return (String)options.get("E2S");
	}
	
	public static void setE2S(String mask) {
		options.put("E2S", mask);
	}
	
	public static String getLastMessage() {
		return (options.get("LastMessage") != null)?(String)options.get("LastMessage"):"";		
	}
	
	public static void setLastMessage(String message) {
		options.put("LastMessage", message);
	}

        public static String getCountryPrefix() {
		return (String)options.get("Country");
        }

        public static void setCountryPrefix(String prefix) {
		options.put("Country", prefix);
        }
}
