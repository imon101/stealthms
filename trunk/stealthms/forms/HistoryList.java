package stealthms.forms;



import javax.microedition.lcdui.*;

import stealthms.StealthMS;
import stealthms.storage.*;


public class HistoryList extends List implements CommandListener {

	private StealthMS midlet;
	
	private Command backCommand;
	
	private int LastTitle;
	
	public HistoryList(StealthMS midlet) {
		super("История", Choice.IMPLICIT);
		this.midlet = midlet;
		String[] Titles = OptionsStorage.getTitles();
		LastTitle = OptionsStorage.getLastTitle();
		for (int i = LastTitle; i >= 0; i--) {
			if (Titles[i].compareTo("") != 0) {
				append(Titles[i], null);
			}
		}
		for (int i = 14; i > LastTitle; i--) {
			if (Titles[i].compareTo("") != 0) {
				append(Titles[i], null);
			}
		}
		backCommand = new Command("Назад", Command.BACK, 1);
		addCommand(backCommand);
		setCommandListener(this);
	}
	
	public void commandAction(Command comm, Displayable disp) {
		if (comm == backCommand) {
			midlet.displayMessage();
		}
		if (comm == List.SELECT_COMMAND) {
			int selectedIndex = getSelectedIndex();
			int messageIndex = LastTitle - selectedIndex;
			if (messageIndex < 0) {
				messageIndex += 15;
			}
			HistoryStorage.loadMessage(messageIndex);
			midlet.displayHistoryView(HistoryStorage.getHPhone(), HistoryStorage.getHMessage());
		}
	}

}
