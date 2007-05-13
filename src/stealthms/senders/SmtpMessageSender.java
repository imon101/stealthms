package stealthms.senders;

import java.io.*;
import javax.microedition.io.*;
import stealthms.forms.Options;

import stealthms.storage.OptionsStorage;
import stealthms.utilities.*;

public class SmtpMessageSender extends MessageSender {
        private String Gates;
        
        private static String lastMask;
        
        private String SMUser;
        
        private String SMPass;
        
        private String Copy;
        
        private void executeCommand(InputStream is, OutputStream os, String command)
        throws Exception {
                if (os == null || is == null) {
                        throw (new Exception("Соединение не может быть установлено"));
                }
                if (command != "") {
                        command = command + "\r\n";
                        os.write(command.getBytes());
                        os.flush();
                }
                char firstSymb = (char) is.read();
                is.skip(is.available());
                if ((firstSymb == '4') || (firstSymb == '5')) {
                        throw new Exception("Ошибка в " + command);
                }
        }
        
        private String getAuthString(String user, String pass) {
                return base64Encode("\000" + user + "\000" + pass);
        }
        
        private String base64Encode(String str) {
                char[] charTab = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
                        .toCharArray();
                byte[] data = str.getBytes();
                StringBuffer buf = new StringBuffer();
                int start = 0;
                int len = data.length;
                int end = len - 3;
                int i = start;
                while (i <= end) {
                        int d = ((((int) data[i]) & 0x0ff) << 16)
                        | ((((int) data[i + 1]) & 0x0ff) << 8)
                        | (((int) data[i + 2]) & 0x0ff);
                        buf.append(charTab[(d >> 18) & 63]);
                        buf.append(charTab[(d >> 12) & 63]);
                        buf.append(charTab[(d >> 6) & 63]);
                        buf.append(charTab[d & 63]);
                        i += 3;
                }
                if (i == start + len - 2) {
                        int d = ((((int) data[i]) & 0x0ff) << 16)
                        | ((((int) data[i + 1]) & 255) << 8);
                        buf.append(charTab[(d >> 18) & 63]);
                        buf.append(charTab[(d >> 12) & 63]);
                        buf.append(charTab[(d >> 6) & 63]);
                        buf.append("=");
                } else if (i == start + len - 1) {
                        int d = (((int) data[i]) & 0x0ff) << 16;
                        buf.append(charTab[(d >> 18) & 63]);
                        buf.append(charTab[(d >> 12) & 63]);
                        buf.append("==");
                }
                return buf.toString();
        }
        
        private String getEmailAddr(String number) throws Exception {
                try {
                        int lastIndex = 0;
                        while (lastIndex < Gates.length()) {
                                int newIndex = Gates.indexOf(";", lastIndex);
                                if (newIndex == -1) {
                                        newIndex = Gates.length();
                                }
                                String currentOp = Gates.substring(lastIndex, newIndex);
                                lastIndex = newIndex + 1;
                                String currentPrefix = currentOp.substring(0, currentOp
                                        .indexOf(":"));
                                String CountryPrefix = OptionsStorage.getCountryPrefix();
                                String currentMask = currentOp
                                        .substring(currentOp.indexOf(":") + 1);
                                if (number.startsWith(currentPrefix)
                                || number.startsWith(CountryPrefix + currentPrefix)) {
                                        if (number.startsWith(CountryPrefix + currentPrefix)) {
                                                currentPrefix = CountryPrefix + currentPrefix;
                                        }
                                        String sevenNumber = number.substring(currentPrefix
                                                .length(), currentPrefix.length() + 7);
                                        String maskStart = currentMask.substring(0, currentMask
                                                .indexOf("%"));
                                        String maskEnd = currentMask.substring(currentMask
                                                .indexOf("%") + 1);
                                        lastMask = maskEnd;
                                        return maskStart + sevenNumber + maskEnd;
                                }
                        }
                } catch (Exception e) {
                }
                throw new Exception("Неправильный номер или гейт");
        }
        
        public void sendMessage(String message, String phone) throws Exception {
                String mail = getEmailAddr(phone);
                if (!Character.isDigit(Url.charAt(Url.length() - 1))) {
                        Url = Url + ":25";
                }
                try {
//			Connection con = Connector.open("socket://" + Url,
//					Connector.READ_WRITE);
//#if MIDP1|MIDP1_RU
//#                         StreamConnection con = (StreamConnection) Connector.open("socket://" + Url,
//# 					Connector.READ_WRITE);
//#elif !(MIDP1|MIDP1_RU)
                        SocketConnection con = (SocketConnection) Connector.open("socket://" + Url,
                                Connector.READ_WRITE);
//#endif
                        sendingForm.setGaugeValue(3);
                        InputStream is = ((InputConnection) con).openInputStream();
                        OutputStream os = ((OutputConnection) con).openOutputStream();
                        executeCommand(is, os, "");
                        sendingForm.setGaugeValue(4);
                        if (SMUser.compareTo("") != 0) {
                                executeCommand(is, os, "EHLO StealthMS");
                                if (OptionsStorage.getAuthLogin() == 0) {
                                        executeCommand(is, os, "AUTH PLAIN "
                                                + getAuthString(SMUser, SMPass));
                                } else {
                                        executeCommand(is, os, "AUTH LOGIN");
                                        executeCommand(is, os, base64Encode(SMUser));
                                        executeCommand(is, os, base64Encode(SMPass));
                                }
                        } else {
                                executeCommand(is, os, "HELO StealthMS");
                                sendingForm.setGaugeValue(5);
                        }
                        TextFormatter tf = new TextFormatter();
                        int numParts = 0;
                        numParts = splitext(tf.translit(message, true), User.length());
                        for (int i = numParts; i >= 0; i--) {
                                String tempMask = lastMask;
                                if (Copy.compareTo("") != 0 && i == 0) {
                                        mail = getEmailAddr(Copy);
                                        DateFormatter df = new DateFormatter();
                                        messageParts[0] = tf.translit("Сообщение для " + phone + " от " +
                                                df.formatCurrentDate() + " отправлено.", true);
                                }
                                if ((Copy.compareTo("") != 0 && tempMask.compareTo(lastMask) == 0)
                                || i != 0) {
                                        executeCommand(is, os, "MAIL FROM: <" + User + ">");
                                        sendingForm.setGaugeValue(6);
                                        executeCommand(is, os, "RCPT TO: <" + mail + ">");
                                        sendingForm.setGaugeValue(7);
                                        executeCommand(is, os, "DATA");
                                        sendingForm.setGaugeValue(8);
                                        String temp_message = "From: " + User + "\r\n";
                                        temp_message += "To: " + mail + "\r\n";
                                        temp_message += "Content-type: text/plain; charset=windows-1251\r\nContent-transfer-encoding: 8bit\r\n\r\n";
                                        temp_message += messageParts[i];
                                        temp_message += "\r\n.";
                                        executeCommand(is, os, temp_message);
                                        sendingForm.setGaugeValue(9);
                                }
                        }
                        executeCommand(is, os, "QUIT");
                        sendingForm.setGaugeValue(10);
                        con.close();
                } catch (ConnectionNotFoundException e) {
                        throw (new Exception("Требуемое TCP соединение неосуществимо"));
                } catch (IllegalArgumentException e) {
                        throw (new Exception("Указанный сервер и/или порт некорректны"));
                } catch (IOException e) {
                        throw (new Exception("Произошла ошибка ввода-вывода"));
                } catch (SecurityException e) {
                        throw (new Exception("Нет доступа к интернету"));
                } catch (Exception e) {
                        throw e;
                }
        }
        
        public String getCopy() {
                return Copy;
        }
        
        public void setCopy(String copy) {
                Copy = copy;
        }
        
        public String getGates() {
                return Gates;
        }
        
        public void setGates(String gates) {
                Gates = gates;
        }
        
        public String getSMPass() {
                return SMPass;
        }
        
        public void setSMPass(String pass) {
                SMPass = pass;
        }
        
        public String getSMUser() {
                return SMUser;
        }
        
        public void setSMUser(String user) {
                SMUser = user;
        }
}
