package stealthms.forms;



import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

import stealthms.StealthMS;
import stealthms.storage.*;

public class Options extends Form implements CommandListener {
	private StealthMS midlet;
	
	private TextField userTextField;
	
	private TextField urlTextField;
	
	private TextField smuserTextField;
	
	private TextField smpassTextField;
	
	private TextField gatesTextField;
	
	private TextField copyTextField;
	
	private TextField familyTextField;
	
	private Command cancCommand;

	private Command saveCommand;
	
	public Options(StealthMS midlet) {
		super("Настройки");
		this.midlet = midlet;

		// Adding fields
		userTextField = new TextField("Адрес/имя", "", 50, TextField.ANY);
		append(userTextField);
		urlTextField = new TextField("Сервер", "", 50, TextField.URL);
		append(urlTextField);
		smuserTextField = new TextField("Логин", "", 20, TextField.ANY);
		append(smuserTextField);
		smpassTextField = new TextField("Пароль", "", 20, TextField.PASSWORD);
		append(smpassTextField);
		gatesTextField = new TextField("Гейты", "", 500, TextField.ANY);
		append(gatesTextField);
		copyTextField = new TextField("Копия", "", 15, TextField.PHONENUMBER);
		append(copyTextField);
		familyTextField = new TextField("Семья", "", 100, TextField.ANY);
		append(familyTextField);
		
		// Adding commands
		cancCommand = new Command("Отмена", Command.BACK, 0);
		saveCommand = new Command("Сохранить", Command.OK, 0);
		addCommand(cancCommand);
		addCommand(saveCommand);
		setCommandListener(this);
	}

	public String getCopy() {
		return copyTextField.getString();
	}

	public void setCopy(String copy) {
		copyTextField.setString(copy);
	}

	public String getGates() {
		return gatesTextField.getString();
	}

	public void setGates(String gates) {
		gatesTextField.setString(gates);
	}

	public String getSMPass() {
		return smpassTextField.getString();
	}

	public void setSMPass(String pass) {
		smpassTextField.setString(pass);
	}

	public String getSMUser() {
		return smuserTextField.getString();
	}

	public void setSMUser(String user) {
		smuserTextField.setString(user);
	}

	public String getUrl() {
		return urlTextField.getString();
	}

	public void setUrl(String url) {
		urlTextField.setString(url);
	}

	public String getUser() {
		return userTextField.getString();
	}

	public void setUser(String user) {
		userTextField.setString(user);
	}
	
	public String getFamily() {
		return familyTextField.getString();
	}

	public void setFamily(String user) {
		familyTextField.setString(user);
	}

	public void commandAction(Command comm, Displayable disp) {
		if (comm == saveCommand) {
			OptionsStorage.setUrl(getUrl());
			OptionsStorage.setUser(getUser());
			OptionsStorage.setSMUser(getSMUser());
			OptionsStorage.setSMPass(getSMPass());
			OptionsStorage.setGates(getGates());
			OptionsStorage.setCopy(getCopy());
			OptionsStorage.setFamily(getFamily());
			OptionsStorage.saveSettings();
			midlet.displayMessage();
		}
		if (comm == cancCommand) {
			midlet.displayMessage();
		}
	}
}
