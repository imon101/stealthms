package stealthms.forms;

import java.util.Vector;

import javax.microedition.lcdui.*;

import stealthms.StealthMS;

public class RecentList extends List implements CommandListener {

	private StealthMS midlet;
	
	private Command backCommand;
	
	private Vector phones;
	
	private Vector names;
	
	public RecentList(StealthMS midlet) {
		super("Последние", Choice.IMPLICIT);
		this.midlet = midlet;
		phones = new Vector();
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
			midlet.setPhone((String)phones.elementAt(selectedIndex));
			midlet.displaySending(false);
		}
	}
	
	public void addPhone(String phoneNum, String displayNum) {
		if (phoneNum.compareTo(displayNum) == 0) {
			append(displayNum, null);
		} else {
			append(displayNum + " [" + phoneNum + "]", null);
		}
		phones.addElement(phoneNum);
		names.addElement(displayNum);
	}
	
	public void clearForm() {
		for (int i = 0; i < size(); i++) {
			delete(i);
		}
		phones = new Vector();
		names = new Vector();
	}
	
	public String searchName(String phone) {
		int idx = phones.indexOf(phone);
		if (idx != -1) {
			return (String) names.elementAt(idx);
		}
		return "";
	}
}
