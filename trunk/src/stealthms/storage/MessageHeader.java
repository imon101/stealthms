package stealthms.storage;

public class MessageHeader {
	private String hPhone;
	private String hDate;
	private String hMessage;
	private String hName;
	
	public MessageHeader(String pPhone, String pDate, String pMessage, String pName) {
		setPhone(pPhone);
		setDate(pDate);
		setMessage(pMessage);
		setName(pName);
	}
	
	public MessageHeader() {}
	
	public String getPhone() {
		return hPhone;
	}
	
	public void setPhone(String pPhone) {
		hPhone = pPhone;
	}
	
	public String getDate() {
		return hDate;
	}
	
	public void setDate(String pDate) {
		hDate = pDate;
	}
	
	public String getMessage() {
		return hMessage;
	}
	
	public void setMessage(String pMessage) {
		hMessage = pMessage;
	}
	
	public String getName() {
		return hName;
	}
	
	public void setName(String pName) {
		hName = pName;
	}
}