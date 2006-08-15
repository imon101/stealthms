package stealthms.forms;

import javax.microedition.lcdui.*;
import stealthms.StealthMS;

public class About extends Form implements CommandListener {

	private StealthMS midlet;
	
	private Command backCommand;
	
	public About(StealthMS midlet) {
		super("О программе");
		this.midlet = midlet;
		
		append("StealthMS 1.55\n"
				+ "Сборка 15.08.2006\n"
				+ "\n"
				+ "Программа предназначена для отправки SMS через e-mail2sms гейты операторов. Разрабатывалась для себя и отсылает на все украинские опсосы, кроме Life, у которого нет e-mail2sms шлюза (для KS услугу e-mail2sms надо активировать у получателя, послав sms на номер 7021). Отсылается все через произвольный SMTP сервер, но обычно лучше всего работает операторский (smtp.umc.ua, smtp.jeans.com.ua, relay.kyivstar.net). В поля логин/пароль нужно что-то писать только если ваш SMTP требует аутентификации. В поле адрес нужно указать любой более или менее валидный e-mail (как минимум должен существовать домен). Писать можно по-русски, транслит автоматом. Длинные сообщения разбиваются на куски. В поле Копия в настройках можно ввести свой номер телефона, тогда будут приходить подтверждения о доставке (работает только в пределах одного оператора). На номера из поля Семья будут слаться обычные SMS. Транслит - включение автотранслитерации.");
		backCommand = new Command("Назад", Command.BACK, 1);
		addCommand(backCommand);
		setCommandListener(this);
	}

	public void commandAction(Command comm, Displayable disp) {
		if (comm == backCommand) {
			midlet.displayMessage();
		}
	}

}
