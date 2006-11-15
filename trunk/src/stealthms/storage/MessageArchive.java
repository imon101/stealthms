/*
 * MessageArchive.java
 *
 * Created on п'ятниця, 20, жовтня 2006, 16:30
 *
 */

package stealthms.storage;

/**
 *
 * @author Misha
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordEnumeration;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;
import stealthms.utilities.DateFormatter;

public class MessageArchive {
	
	private RecordStore rsArchive;
	private RecordStore rsArchiveHeaders;
	private String rsName;
	
	/** Creates a new instance of MessageArchive */
	public MessageArchive(String Name) {
		rsName=Name;
		try {
			rsArchive=RecordStore.openRecordStore(rsName,true);
			rsArchiveHeaders=RecordStore.openRecordStore(rsName+"_Headers",true);
		} catch (RecordStoreException ex) {
		}
	}
	
	public void SaveMessage(String Message,String Phone,int Index) {
		int recIndex=0;
		try {
			recIndex=rsArchive.getNextRecordID();
		} catch (RecordStoreNotOpenException ex) {
		} catch (RecordStoreException ex) {
		}
		if (Index==-1) {
			Index=recIndex;
		}
		//Запишемо заголовок
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		DataOutputStream outputStream=new DataOutputStream(baos);
		try {
			outputStream.writeUTF(Phone);
			DateFormatter dateFormatter = new DateFormatter();
			outputStream.writeUTF(dateFormatter.formatCurrentDate());
			int HeaderMsgLen=Message.length();
			if (HeaderMsgLen>20)
				HeaderMsgLen=20;
			String Msg=Message.trim().substring(0,HeaderMsgLen);
			outputStream.writeUTF(Msg);
			byte[] bytes=baos.toByteArray();
			if (rsArchive.getNextRecordID()<=Index) {
				rsArchiveHeaders.addRecord(bytes,0,bytes.length);
			} else {
				rsArchiveHeaders.setRecord(Index+1,bytes,0,bytes.length);
			}
			outputStream.close();
		} catch (Exception e) {
		}
		//А тепер і саме повідомлення
		baos=new ByteArrayOutputStream();
		outputStream=new DataOutputStream(baos);
		try {
			outputStream.writeUTF(Message);
			byte[] bytes=baos.toByteArray();
			if (rsArchive.getNextRecordID()<=Index) {
				rsArchive.addRecord(bytes,0,bytes.length);
			} else {
				rsArchive.setRecord(Index+1,bytes,0,bytes.length);
			}
			outputStream.close();
		} catch (Exception e) {
		}
	}
	
	public void DelMessage(int Index) {
		if (Index==-1)
			return;
		try {
			rsArchive.deleteRecord(Index);
			rsArchiveHeaders.deleteRecord(Index);
		} catch (Exception e) {
			System.out.println(e.toString());
			System.out.println(e.getMessage());
		}
	}
	
	public void DelAllMessages() {
		try {
			rsArchive.closeRecordStore();
			rsArchiveHeaders.closeRecordStore();
			RecordStore.deleteRecordStore(rsName);
			RecordStore.deleteRecordStore(rsName+"_Headers");
		} catch (RecordStoreException ex) {
		}
		try {
			rsArchive=RecordStore.openRecordStore(rsName,true);
			rsArchiveHeaders=RecordStore.openRecordStore(rsName+"_Headers",true);
		} catch (RecordStoreException ex) {
		}
	}
	
	public String ReadMessage(int Index) {
		String Message=new String();
		try {
			byte[] bytes=rsArchive.getRecord(Index);
			ByteArrayInputStream bais=new ByteArrayInputStream(bytes);
			DataInputStream inputStream=new DataInputStream(bais);
			Message=inputStream.readUTF();
		} catch (Exception ex) {
		}
		return Message;
	}
	
	public void OptimizeArchive() {
		Vector vecMessageHeaders=new Vector();
		Vector vecMessages=new Vector();
		try {
			RecordEnumeration enHeaders=rsArchiveHeaders.enumerateRecords(null,null,false);
			while (enHeaders.hasNextElement()) {
				try {
					vecMessageHeaders.addElement(enHeaders.nextRecord());
				} catch (InvalidRecordIDException ex) {
				} catch (RecordStoreException ex) {
				}
			}
		} catch (RecordStoreNotOpenException ex) {
		}
		try {
			RecordEnumeration enMessages=rsArchive.enumerateRecords(null,null,false);
			while (enMessages.hasNextElement()) {
				try {
					vecMessages.addElement(enMessages.nextRecord());
				} catch (InvalidRecordIDException ex) {
				} catch (RecordStoreException ex) {
				}
			}
		} catch (RecordStoreNotOpenException ex) {
		}
		try {
			rsArchive.closeRecordStore();
			rsArchiveHeaders.closeRecordStore();
			RecordStore.deleteRecordStore(rsName);
			RecordStore.deleteRecordStore(rsName+"_Headers");
		} catch (Exception ex) {
		}
		try {
			rsArchive=RecordStore.openRecordStore(rsName,true);
			rsArchiveHeaders=RecordStore.openRecordStore(rsName+"_Headers",true);
		} catch (RecordStoreException ex) {
		}
		int MsgCount=vecMessageHeaders.size();
		for (int i=0; i<MsgCount;i++) {
			byte [] MsgHeader=(byte[]) vecMessageHeaders.elementAt(i);
			try {
				rsArchiveHeaders.addRecord(MsgHeader,0,MsgHeader.length);
			} catch (Exception ex) {
			}
			byte [] Msg=(byte[]) vecMessages.elementAt(i);
			try {
				rsArchive.addRecord(Msg,0,Msg.length);
			} catch (Exception ex) {
			}
		}
	}
	
	public Hashtable getHeaders() {
		Hashtable htMessageHeaders=new Hashtable();
		int MsgCount=0;
		try {
			MsgCount = rsArchiveHeaders.getNextRecordID() - 1;
		} catch (Exception ex) {
		}
		for (int i=0; i<=MsgCount;i++) {
			try {
				byte[] bytes=rsArchiveHeaders.getRecord(i);
				ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
				DataInputStream inputStream = new DataInputStream(bais);
				String hPhone="";
				String hDate="";
				String hHeader="";
				try {
					hPhone=inputStream.readUTF();
					hDate=inputStream.readUTF();
					hHeader=inputStream.readUTF();
				} catch (Exception ex) {
				}
				htMessageHeaders.put(new Integer(i),hDate+";"+hPhone+";"+hHeader);
			} catch (Exception ex) {
			}
		}
		return htMessageHeaders;
	}
}
