package stealthms.utilities;

import java.util.Enumeration;
import java.util.Vector;

import stealthms.forms.RecentList;
import stealthms.storage.*;

public class RecentUpdater extends Thread {
	
	private boolean ready;
	
	private RecentList recentForm;
	
	private MessageArchive archive;
	
	public RecentUpdater(RecentList recentForm, MessageArchive arc) {
		this.recentForm = recentForm;
		this.archive = arc;
		this.ready = false;
	}
	
	public void run() {
		setReady(false);
		Vector phones = new Vector();
		Vector displayNames = new Vector();
		for (Enumeration e = archive.getHeaders().elements(); e.hasMoreElements() && phones.size() < 21;) {
			MessageHeader header = (MessageHeader) e.nextElement();
			if (!phones.contains(header.getPhone())) {
				phones.addElement(header.getPhone());
				displayNames.addElement(header.getName());
			}
			yield();
		}
		yield();
		recentForm.clearForm();
		yield();
		Enumeration d = displayNames.elements();
		for (Enumeration e = phones.elements(); e.hasMoreElements();) {
			recentForm.addPhone((String)e.nextElement(), (String)d.nextElement());
			yield();
		}
		setReady(recentForm.size() > 0);
	}


	public boolean isReady() {
		return this.ready;
	}


	public void setReady(boolean ready) {
		this.ready = ready;
	}
}
