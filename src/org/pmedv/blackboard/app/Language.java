package org.pmedv.blackboard.app;

import java.util.Locale;

public class Language {

	private String displayString;
	private Locale locale;
	
	public Language(Locale locale) {
		this.locale = locale;
		this.displayString = locale.getDisplayCountry();
	}
	
	public String getDisplayString() {
		return displayString;
	}
	public void setDisplayString(String displayString) {
		this.displayString = displayString;
	}
	public Locale getLocale() {
		return locale;
	}
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	
	
}
