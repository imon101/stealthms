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
	
	private TextField KSTextField;

	private TextField MTSTextField;

        private TextField E2STextField;

	private TextField E2SUserField;

	private TextField E2SPassField;

        private TextField CountryPrefixField;

	private ChoiceGroup Translit;
	
	private ChoiceGroup AuthType;
	
	private Command cancCommand;

	private Command saveCommand;
	
	public Options(StealthMS midlet) {
		super("���������");
		this.midlet = midlet;

		// Adding fields
		userTextField = new TextField("�����/���", "", 50, TextField.ANY);
		append(userTextField);
		urlTextField = new TextField("������", "", 50, TextField.URL);
		append(urlTextField);
		smuserTextField = new TextField("�����", "", 50, TextField.ANY);
		append(smuserTextField);
		smpassTextField = new TextField("������", "", 50, TextField.PASSWORD);
		append(smpassTextField);
		gatesTextField = new TextField("�����", "", 1000, TextField.ANY);
		append(gatesTextField);
		copyTextField = new TextField("�����", "", 15, TextField.ANY);
		append(copyTextField);
		familyTextField = new TextField("�����", "", 1000, TextField.ANY);
		append(familyTextField);
		KSTextField = new TextField("����� KS", "", 1000, TextField.ANY);
		append(KSTextField);
		MTSTextField = new TextField("����� MTS", "", 1000, TextField.ANY);
		append(MTSTextField);
		E2STextField = new TextField("����� E2S", "", 1000, TextField.ANY);
		append(E2STextField);
		E2SUserField = new TextField("����� E2S", "", 50, TextField.ANY);
		append(E2SUserField);
		E2SPassField = new TextField("������ E2S", "", 50, TextField.PASSWORD);
		append(E2SPassField);
		
		String[] trOptions = {"����", "���"};
		int ChoiceType = 4; //ChoiceGroup.POPUP
		if (System.getProperty("microedition.profiles").compareTo("MIDP-1.0") == 0) {
			ChoiceType = ChoiceGroup.EXCLUSIVE;
		}
		Translit = new ChoiceGroup("��������", ChoiceType, trOptions, null);	
		append(Translit);
		
		String[] auOptions = {"PLAIN", "LOGIN"};
		AuthType = new ChoiceGroup("�����������", ChoiceType, auOptions, null);	
		append(AuthType);

		CountryPrefixField = new TextField("��� ������", "", 50, TextField.ANY);
		append(CountryPrefixField);
                
		// Adding commands
		cancCommand = new Command("������", Command.BACK, 0);
		saveCommand = new Command("���������", Command.OK, 0);
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
	
	public String getKS() {
		return KSTextField.getString();
	}

	public void setKS(String user) {
		KSTextField.setString(user);
	}

	public String getMTS() {
		return MTSTextField.getString();
	}

        public void setMTS(String user) {
		MTSTextField.setString(user);
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
			OptionsStorage.setKS(getKS());
			OptionsStorage.setMTS(getMTS());
			OptionsStorage.setE2S(getE2S());
			OptionsStorage.setE2SUser(getE2SUser());
			OptionsStorage.setE2SPass(getE2SPass());
			OptionsStorage.setTranslitStat(getTranslit());
			OptionsStorage.setAuthLogin(getAuthLogin());
			OptionsStorage.setCountryPrefix(getCountryPrefix());
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

	private String getCountryPrefix() {
		return CountryPrefixField.getString();
	}

	public void setCountryPrefix(String string) {
		CountryPrefixField.setString(string);
	}

}
