package tk.cyriellecentori.brainbot.profiles;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Vector;

import tk.cyriellecentori.brainbot.shop.Aliment;

public abstract class AchievementFood extends Achievement {
	
	
	public AchievementFood(String name, String desc, boolean secret, int reward) {
		super(name, desc, secret, reward);
		
	}
	
	public abstract boolean isUnlock(Vector<Aliment> frigo);
	
	public static class Note extends AchievementFood {
		
		private int note;
		boolean equals;
		
		public Note(String name, String desc, boolean secret, int reward, int note, boolean equals) {
			super(name, desc, secret, reward);
			this.note = note;
			this.equals = equals;
		}

		@Override
		public boolean isUnlock(Vector<Aliment> frigo) {
			return equals ? (Profile.getFrigoScore(frigo) == note) : (Profile.getFrigoScore(frigo) >= note);
		}
	}
	
	public static class Contains extends AchievementFood {

		Aliment[] frigo;
		LinkedHashMap<String, Integer> count = new LinkedHashMap<String, Integer>();
		boolean equals;
		
		public Contains(String name, String desc, boolean secret, int reward, Aliment[] frigo, boolean equals) {
			super(name, desc, secret, reward);
			this.frigo = frigo;
			this.equals = equals;
			for(Aliment a : this.frigo) {
				if(this.count.containsKey(a.name)) {
					count.put(a.name, count.get(a.name) + 1);
				} else {
					count.put(a.name, 1);
				}
			}
		}

		@Override
		public boolean isUnlock(Vector<Aliment> frigo) {
			System.out.println("Vérification de l'achievement.");
			boolean isOk = true;
			LinkedHashMap<String, Integer> count = new LinkedHashMap<String, Integer>();
			for(Aliment a : frigo) {
				if(count.containsKey(a.name)) {
					count.put(a.name, count.get(a.name) + 1);
				} else {
					count.put(a.name, 1);
				}
			}
			for(Entry<String, Integer> es : this.count.entrySet()) {
				if(count.containsKey(es.getKey())) {
					if(!equals)
						isOk = isOk && (count.get(es.getKey()).intValue() >= es.getValue().intValue());
					else
						isOk = isOk && (count.get(es.getKey()).intValue() == es.getValue().intValue());
				} else {
					isOk = false;
				}
			}
			if(equals) {
				for(Entry<String, Integer> es : count.entrySet()) {
					isOk = isOk && this.count.containsKey(es.getKey());
				}
			}
			
				
			return isOk;
		}
		
	}

	public static AchievementFood[] frigoAchievements;

}
