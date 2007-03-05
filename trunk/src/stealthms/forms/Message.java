package stealthms.forms;

import javax.microedition.lcdui.*;
import stealthms.StealthMS;
import stealthms.storage.MessageArchive;

public class Message extends TextBox implements CommandListener {
	private StealthMS midlet;
	
	private Command exitCommand;

	private Command sendCommand;

	private Command optsCommand;

	private Command abotCommand;

	private Command histCommand;

	private Command loadLastMsgCommand;

	public Message(StealthMS midlet) {
		super("Сообщение", "", 700, 0x200000); // INITIAL_CAPS_SENTENCE
		this.midlet = midlet;
		exitCommand = new Command("Выход", Command.BACK, 3);
		optsCommand = new Command("Настройки", Command.OK, 1);
		abotCommand = new Command("О программе", Command.OK, 2);
		histCommand = new Command("Отправленные", Command.OK, 1);
		sendCommand = new Command("Передать", Command.OK, 0);
		loadLastMsgCommand = new Command("Последнее", Command.ITEM, 5);
		addCommand(exitCommand);
		addCommand(optsCommand);
		addCommand(abotCommand);
		addCommand(histCommand);
		addCommand(sendCommand);
		addCommand(loadLastMsgCommand);
		setCommandListener(this);
	}

	public void commandAction(Command comm, Displayable disp) {
		if (comm == exitCommand) {
			midlet.exitRequested();
		}
		if (comm == abotCommand) {
			midlet.displayAbout();
		}
		if (comm == optsCommand) {
			midlet.displayOptions();
		}
		if (comm == histCommand) {
			midlet.displayHistoryList(true);
		}
		if (comm == sendCommand) {
			if (midlet.getPhone().compareTo("") != 0) {
				midlet.displayPhone();
			} else {
				midlet.displayRecent();
			}
		}
		if (comm == loadLastMsgCommand) {
			String Msg=MessageArchive.LoadLastMsg();
                        if (Msg.trim().length()>0) {
                                setString(Msg);
                        }
		}
                if (comm!=loadLastMsgCommand) {
                        MessageArchive.SaveLastMessage(getString());
                }
	}

}
