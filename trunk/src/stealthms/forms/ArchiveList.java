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
	
	public ArchiveList(StealthMS midlet, MessageArchive Arc) {
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
			Integer Key = (Integer) Keys.elementAt(getSelectedIndex());
			ArcSent.DelMessage(Key.intValue());
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
			Integer Key = (Integer) Keys.elementAt(selectedIndex);
			String Message = ArcSent.ReadMessage(Key.intValue());
			// reading phone
			MessageHeader Header = (MessageHeader) htHeaders.get(Key);
			midlet.displayHistoryView(Header.getName(), Message);
		}
	}
	
	private void RefreshList() {
		while (size() > 0) {
			delete(0);
		}
		Keys = new Vector();
		String hDate;
		String hMsgStart;
		htHeaders = ArcSent.getHeaders();
		Enumeration enHeaders = htHeaders.keys();
		while (enHeaders.hasMoreElements()) {
			Integer Key = (Integer) enHeaders.nextElement();
			MessageHeader hdr = (MessageHeader) htHeaders.get(Key);
			hDate = hdr.getDate();
			hMsgStart = hdr.getMessage();
			append(hDate + " " + hMsgStart, null);
			Keys.addElement(Key);
		}
	}
}
