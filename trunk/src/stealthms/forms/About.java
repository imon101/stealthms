package stealthms.forms;

import javax.microedition.lcdui.*;
import stealthms.StealthMS;

public class About extends Form implements CommandListener {

	private StealthMS midlet;
	
	private Command backCommand;
	
	public About(StealthMS midlet) {
		super("О программе");
		this.midlet = midlet;
		
		append("StealthMS 1.73\n"
				//#ifdef MIDP2JSR75
//# 				+ "Версия MIDP2/JSR75\n"
				//#elifdef MIDP2
//# 				+ "Версия MIDP2\n"
				//#elifdef MIDP1
//# 				+ "Версия MIDP1\n"
				//#endif
				+ "Сборка 05.03.2007\n"
				+ "\n"
				+ "Программа предназначена для отправки SMS через GPRS. Изначально настроена под украинский UMC и отсылает на все украинские опсосы (для KS услугу e-mail2sms надо активировать у получателя, послав sms на номер 7021). Отсылается все через произвольный SMTP сервер, но обычно лучше всего работает операторский (smtp.umc.ua, smtp.jeans.com.ua, relay.kyivstar.net). В поля логин/пароль нужно что-то писать только если ваш SMTP требует аутентификации. В поле адрес нужно указать любой более или менее валидный e-mail (как минимум должен существовать домен). Писать можно по-русски, есть отключаемый транслит. Длинные сообщения разбиваются на куски. В поле Копия в настройках можно ввести свой номер телефона, тогда будут приходить подтверждения о доставке (работает только в пределах одного оператора). На номера из поля Семья будут слаться обычные SMS.");
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
