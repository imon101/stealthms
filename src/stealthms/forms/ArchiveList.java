package stealthms.forms;



import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.*;

import stealthms.StealthMS;
import stealthms.storage.*;


public class ArchiveList extends List implements CommandListener {
	
	private StealthMS midlet;
	
	private Command backCommand;
	
	private Command delCommand;
	
	private Command delAllCommand;
	
	private Command optimizeCommand;
	
	private Hashtable htHeaders;
	
	private Vector Keys;
	
	private MessageArchive ArcSent;
	
	public ArchiveList(StealthMS midlet,MessageArchive Arc) {
		super("История", Choice.IMPLICIT);
		this.midlet = midlet;
		ArcSent = Arc;
		RefreshList();
		backCommand = new Command("Назад", Command.BACK, 1);
		addCommand(backCommand);
		delCommand = new Command("Стереть", Command.ITEM, 1);
		addCommand(delCommand);
		delAllCommand = new Command("Стереть все", Command.ITEM, 2);
		addCommand(delAllCommand);
		optimizeCommand = new Command("Оптимизировать", Command.ITEM, 3);
		addCommand(optimizeCommand);
		setCommandListener(this);
	}
	
	public void commandAction(Command comm, Displayable disp) {
		if (comm == backCommand) {
			midlet.displayMessage();
		}
		if (comm == delCommand) {
			int Key=-1;
			Key = Integer.parseInt(Keys.elementAt(getSelectedIndex()).toString());
			ArcSent.DelMessage(Key);
			RefreshList();
		}
		if (comm == delAllCommand) {
			ArcSent.DelAllMessages();
			RefreshList();
		}
		if (comm == optimizeCommand) {
			ArcSent.OptimizeArchive();
			RefreshList();
		}
		if (comm == List.SELECT_COMMAND) {
			int selectedIndex = getSelectedIndex();
			Integer Key = new Integer(0);
			Key = Integer.valueOf(Keys.elementAt(selectedIndex).toString());
			String Message = ArcSent.ReadMessage(Key.intValue());
			// reading phone
			String Header = htHeaders.get(Key).toString();
//			hDate = str.substring(0,str.indexOf(";"));
			int fDelim = Header.indexOf(";");
			String hPhone = Header.substring(fDelim + 1, Header.indexOf(";", fDelim + 1));
			midlet.displayHistoryView(hPhone, Message);
		}
	}
	
	private void RefreshList() {
		while (size()>0)
			delete(0);
		Keys = new Vector();
//		String hPhone;
		String hDate;
		String hMsgStart;
		htHeaders = ArcSent.getHeaders();
		Enumeration enHeaders = htHeaders.keys();
		while (enHeaders.hasMoreElements()) {
			Integer Key=new Integer(-1);
			Key = Integer.valueOf(enHeaders.nextElement().toString());
			String str = htHeaders.get(Key).toString();
			int PhoneSep = str.indexOf(";");
			hDate=str.substring(0, PhoneSep);
//			hPhone=str.substring(PhoneSep+1);
			hMsgStart = str.substring(str.indexOf(";", PhoneSep + 1) + 1);
			append(hDate + " " + hMsgStart, null);
			Keys.addElement(Key);
		}
	}
	
}
