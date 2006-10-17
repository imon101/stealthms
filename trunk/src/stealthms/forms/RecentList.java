package stealthms.forms;



import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.lcdui.*;

import stealthms.StealthMS;
import stealthms.storage.*;


public class RecentList extends List implements CommandListener, Runnable {

	private StealthMS midlet;
	
	private Command backCommand;
	
	private boolean ready;
	
	public RecentList(StealthMS midlet) {
		super("Последние", Choice.IMPLICIT);
		setReady(false);
		this.midlet = midlet;
		backCommand = new Command("Отмена", Command.BACK, 1);
		addCommand(backCommand);
		setCommandListener(this);
	}
	
	public void commandAction(Command comm, Displayable disp) {
		if (comm == backCommand) {
			midlet.displayPhone();
		}
		if (comm == List.SELECT_COMMAND) {
			int selectedIndex = getSelectedIndex();
			midlet.setPhoneAndSend(getString(selectedIndex));
		}
	}

	public void run() {
		setReady(false);
		String[] Titles = OptionsStorage.getTitles();
		int LastTitle = OptionsStorage.getLastTitle();
		Vector phones = new Vector();
		for (int i = LastTitle; i >= 0; i--) {
			HistoryStorage.loadMessage(i);
			if (Titles[i].compareTo("") != 0 && !phones.contains(HistoryStorage.getHPhone())) {
				phones.addElement(HistoryStorage.getHPhone());
			}
		}
		for (int i = 14; i > LastTitle; i--) {
			HistoryStorage.loadMessage(i);
			if (Titles[i].compareTo("") != 0 && !phones.contains(HistoryStorage.getHPhone())) {
				phones.addElement(HistoryStorage.getHPhone());
			}
		}
		for (int i = 0; i < size(); i++) {
			delete(i);
		}
		for (Enumeration e = phones.elements(); e.hasMoreElements();) {
			append((String)e.nextElement(), null);
		}
		setReady(true);
	}
	
	public boolean isReady() {
		return ready;		
	}
	
	public void setReady(boolean state) {
		ready = state;
	}
}
