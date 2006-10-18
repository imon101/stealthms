package stealthms.forms;

import javax.microedition.lcdui.*;
import stealthms.storage.*;
import stealthms.StealthMS;

public class Options extends Form implements CommandListener {
	private StealthMS midlet;
	
	private TextField userTextField;
	
	private TextField urlTextField;
	
	private TextField smuserTextField;
	
	private TextField smpassTextField;
	
	private TextField gatesTextField;
	
	private TextField copyTextField;
	
	private TextField familyTextField;
	
	private TextField httpTextField;

	private TextField E2STextField;

	private TextField E2SUserField;

	private TextField E2SPassField;

	private ChoiceGroup Translit;
	
	private ChoiceGroup AuthType;
	
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
		httpTextField = new TextField("Через KS", "", 100, TextField.ANY);
		append(httpTextField);
		E2STextField = new TextField("Через E2S", "", 100, TextField.ANY);
		append(E2STextField);
		E2SUserField = new TextField("Логин E2S", "", 50, TextField.ANY);
		append(E2SUserField);
		E2SPassField = new TextField("Пароль E2S", "", 20, TextField.PASSWORD);
		append(E2SPassField);
		
		String[] trOptions = {"Выкл", "Вкл"};
		int ChoiceType = ChoiceGroup.POPUP;
		if (System.getProperty("microedition.profiles").compareTo("MIDP-1.0") == 0) {
			ChoiceType = ChoiceGroup.EXCLUSIVE;
		}
		Translit = new ChoiceGroup("Транслит", ChoiceType, trOptions, null);	
		append(Translit);
		
		String[] auOptions = {"PLAIN", "LOGIN"};
		AuthType = new ChoiceGroup("Авторизация", ChoiceType, auOptions, null);	
		append(AuthType);
		
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
	
	public String getHttp() {
		return httpTextField.getString();
	}

	public void setHttp(String user) {
		httpTextField.setString(user);
	}

	public int getTranslit() {
		return Translit.getSelectedIndex();
	}

	public void setTranslit(int translit) {
		Translit.setSelectedIndex(translit, true);
	}
	
	public void setAuthLogin(int authlogin) {
		AuthType.setSelectedIndex(authlogin, true);
	}
	
	public int getAuthLogin() {
		return AuthType.getSelectedIndex();
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
			OptionsStorage.setHttp(getHttp());
			OptionsStorage.setE2S(getE2S());
			OptionsStorage.setE2SUser(getE2SUser());
			OptionsStorage.setE2SPass(getE2SPass());
			OptionsStorage.setTranslitStat(getTranslit());
			OptionsStorage.setAuthLogin(getAuthLogin());
			OptionsStorage.saveSettings();
			midlet.displayMessage();
		}
		if (comm == cancCommand) {
			midlet.displayMessage();
		}
	}

	private String getE2S() {
		return E2STextField.getString();
	}

	private String getE2SUser() {
		return E2SUserField.getString();
	}

	private String getE2SPass() {
		return E2SPassField.getString();
	}

	public void setE2S(String string) {
		E2STextField.setString(string);
	}

	public void setE2SUser(String User) {
		E2SUserField.setString(User);
	}

	public void setE2SPass(String Pass) {
		E2SPassField.setString(Pass);
	}
}
