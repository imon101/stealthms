package stealthms.forms;



import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.lcdui.*;

import stealthms.StealthMS;




public class Sending extends Form implements CommandListener {

	private StealthMS midlet;

	private Gauge sendingGauge;

	private Command cancCommand;

	private Command backCommand;

	private Command exitCommand;

	private Command reptCommand;
	
	private Command reguCommand;
	
	private Timer exitTimer;
	
	private ExitTask exitTask;

	public Sending(StealthMS midlet) {
		super("Отправка");
		this.midlet = midlet;
		sendingGauge = new Gauge("Передача...", false, 10, 0);
		append(sendingGauge);

		cancCommand = new Command("Отмена", Command.CANCEL, 1);
		backCommand = new Command("Назад", Command.BACK, 1);
		exitCommand = new Command("Выход", Command.EXIT, 0);
		reptCommand = new Command("Повтор", Command.OK, 0);
		reguCommand = new Command("Обычное", Command.OK, 1);
		setCommandListener(this);
		
		exitTimer = new Timer();
		exitTask = new ExitTask();
	}

	public void setGaugeValue(int value) throws Exception {
		sendingGauge.setValue(value);
		if (midlet.isErrorState()) {
			throw (new Exception("Отменено"));			
		}
	}

	public void setGaugeLabel(String label) {
		sendingGauge.setLabel(label);
	}

	private void removeSendCommands() {
		removeCommand(cancCommand);
		removeCommand(exitCommand);
		removeCommand(reptCommand);
		removeCommand(reguCommand);
		removeCommand(backCommand);
	}

	public void setSendingState() {
		removeSendCommands();
		addCommand(cancCommand);
	}
	
	public void setErrorState() {
		removeSendCommands();
		addCommand(backCommand);
		addCommand(reptCommand);
		addCommand(reguCommand);
	}

	public void setSuccessState() {
		removeSendCommands();
		addCommand(backCommand);
		addCommand(exitCommand);
		exitTimer = new Timer();
		exitTimer.schedule(exitTask, 5000);
	}
	
	public void commandAction(Command comm, Displayable disp) {
		if (comm == reptCommand) {
			midlet.displaySending(false);
		}
		if (comm == exitCommand) {
			exitTimer.cancel();
			midlet.exitRequested();
		}
		if (comm == cancCommand) {
			midlet.setErrorState(true);
			midlet.displayPhone();
		}
		if (comm == backCommand) {
			exitTimer.cancel();
			midlet.displayPhone();
		}
		if (comm == reguCommand) {
			midlet.displaySending(true);
		}
	}
	
	private class ExitTask extends TimerTask {
		public void run() {
			exitTimer.cancel();
			if (!midlet.isErrorState()) {
				midlet.exitRequested();
			}
		}
	}

}
