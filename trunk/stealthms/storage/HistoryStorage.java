package stealthms.storage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

import stealthms.utilities.DateFormatter;

public class HistoryStorage {
	private static String HMessage;

	private static String HPhone;
	
	public static void loadMessage(int i) {
		RecordStore recordStore;
		try {
			recordStore = RecordStore.openRecordStore("History", false);
			byte[] bytes = recordStore.getRecord(i + 1);
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			DataInputStream inputStream = new DataInputStream(bais);
			HPhone = inputStream.readUTF();
			HMessage = inputStream.readUTF();
			inputStream.close();
			recordStore.closeRecordStore();
		} catch (Exception e) {
			HPhone = "";
			HMessage = "";
		}
	}

	public static void saveMessage(int i) {
		RecordStore recordStore;
		try {
			recordStore = RecordStore.openRecordStore("History", true);
		} catch (RecordStoreException e) {
			return;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream outputStream = new DataOutputStream(baos);
		try {
			outputStream.writeUTF(HPhone);
			outputStream.writeUTF(HMessage);
			byte[] bytes = baos.toByteArray();
			if (recordStore.getNumRecords() <= i)
				recordStore.addRecord(bytes, 0, bytes.length);
			else
				recordStore.setRecord(i + 1, bytes, 0, bytes.length);
			outputStream.close();
			recordStore.closeRecordStore();
		} catch (Exception e) {
		}
	}
	
	public static void addNewMessage(String message, String phone) {
		OptionsStorage.setLastTitle(OptionsStorage.getLastTitle() + 1);
		if (OptionsStorage.getLastTitle() >= 15) {
			OptionsStorage.setLastTitle(0);
		}
		DateFormatter dateFormatter = new DateFormatter();
		String[] Titles = OptionsStorage.getTitles();
		Titles[OptionsStorage.getLastTitle()] = dateFormatter.formatCurrentDate();
		OptionsStorage.setTitles(Titles);
		HPhone = phone;
		HMessage = message;
		HistoryStorage.saveMessage(OptionsStorage.getLastTitle());
		OptionsStorage.saveSettings();
	}

	public static String getHMessage() {
		return HMessage;
	}

	public static void setHMessage(String message) {
		HMessage = message;
	}

	public static String getHPhone() {
		return HPhone;
	}

	public static void setHPhone(String phone) {
		HPhone = phone;
	}
}
