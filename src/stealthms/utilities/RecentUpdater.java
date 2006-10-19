package stealthms.utilities;

import java.util.Enumeration;
import java.util.Vector;

import stealthms.forms.RecentList;
import stealthms.storage.HistoryStorage;
import stealthms.storage.OptionsStorage;

public class RecentUpdater extends Thread {
	
	private boolean ready;
	
	private RecentList recentForm;
	
	public RecentUpdater(RecentList recentForm) {
		this.recentForm = recentForm;
		this.ready = false;
	}
	
	public void run() {
		setReady(false);
		String[] Titles = OptionsStorage.getTitles();
		int LastTitle = OptionsStorage.getLastTitle();
		Vector phones = new Vector();
		for (int i = LastTitle; i >= 0; i--) {
			HistoryStorage.loadMessage(i);
			yield();
			if (Titles[i].compareTo("") != 0 && !phones.contains(HistoryStorage.getHPhone())) {
				phones.addElement(HistoryStorage.getHPhone());
			}
		}
		for (int i = 14; i > LastTitle; i--) {
			HistoryStorage.loadMessage(i);
			yield();
			if (Titles[i].compareTo("") != 0 && !phones.contains(HistoryStorage.getHPhone())) {
				phones.addElement(HistoryStorage.getHPhone());
			}
		}
		for (int i = 0; i < recentForm.size(); i++) {
			recentForm.delete(i);
		}
		yield();
		for (Enumeration e = phones.elements(); e.hasMoreElements();) {
			recentForm.append((String)e.nextElement(), null);
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
