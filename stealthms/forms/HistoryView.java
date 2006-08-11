package stealthms.forms;



import javax.microedition.lcdui.*;

import stealthms.StealthMS;



public class HistoryView extends Form implements CommandListener {

	private StealthMS midlet;
	
	private Command chanCommand;
	
	private Command backCommand;
	
	private String HMessage;
	
	public HistoryView(StealthMS midlet, String HPhone, String HMessage) {
		super(HPhone);
		this.midlet = midlet;
		append(HMessage);
		this.HMessage = HMessage;
		chanCommand = new Command("Изменить", Command.OK, 0);
		backCommand = new Command("Назад", Command.BACK, 1);
		addCommand(chanCommand);
		addCommand(backCommand);
		setCommandListener(this);
	}

	public void commandAction(Command comm, Displayable disp) {
		if (comm == backCommand) {
			midlet.displayHistoryList(false);
		}
		if (comm == chanCommand) {
			midlet.displayMessage(HMessage);
		}
	}

}
