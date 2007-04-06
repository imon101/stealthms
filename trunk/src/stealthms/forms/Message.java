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
                super("Сообщение", "", 700, TextField.ANY);
//#if MIDP1
//#                 int tfConstraints=TextField.ANY;
//#else
                int tfConstraints=TextField.ANY|TextField.INITIAL_CAPS_SENTENCE;
//#endif
                setConstraints(tfConstraints);
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
                        try {
                                String Msg=MessageArchive.LoadLastMsg();
                                if (Msg.trim().length()>0) {
                                        setString(Msg);
                                }
                        } catch (Exception ex) {
                                midlet.ShowError(ex.getMessage());
                        }
                }
                if (comm!=loadLastMsgCommand) {
                        try {
                                MessageArchive.SaveLastMessage(getString());
                        } catch (Exception ex) {
                                midlet.ShowError(ex.getMessage());
                        }
                }
        }
        
}
