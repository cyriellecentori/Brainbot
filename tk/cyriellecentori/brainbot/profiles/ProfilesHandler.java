package tk.cyriellecentori.brainbot.profiles;

import java.util.LinkedHashMap;

import tk.cyriellecentori.brainbot.BotExceptions.*;
import tk.cyriellecentori.brainbot.Brainbot;

public final class ProfilesHandler {
	private LinkedHashMap<Long, Profile> profilesMap = new LinkedHashMap<Long, Profile>();

	public int size() { return profilesMap.size(); }

	public Profile getNC(Long id) { return profilesMap.get(id); }
	
	public Profile get(Long id) {
		Profile profile = profilesMap.get(id);
		if(profile == null) {
			profilesMap.put(id, new Profile(id));
			profile = profilesMap.get(id);
		}
		profile.check();
		return profile;
	}
	
	public void checkAll() {
		for(Profile pro : profilesMap.values()) {
			pro.check();
		}
	}


	public void pay(Long id, long money) throws NumberException, MoneyException {
		if(id == Brainbot.cyrielleID) {
			return;
		}
		if(money < 0) {
			throw new NegativeNumberException();
		}else if(money == 0) {
			throw new NullNumberException();
		}
		Profile profile = this.get(id);
		if(profile.money < money) {
			throw new MoneyException();
		}else {
			profile.money -= money;
		}
	}

}