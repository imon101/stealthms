package stealthms.forms;

import javax.microedition.lcdui.*;

import stealthms.StealthMS;

public class RecentList extends List implements CommandListener {

	private StealthMS midlet;
	
	private Command backCommand;
	
	public RecentList(StealthMS midlet) {
		super("Последние", Choice.IMPLICIT);
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
}
