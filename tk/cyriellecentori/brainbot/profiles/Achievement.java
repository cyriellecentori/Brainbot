package tk.cyriellecentori.brainbot.profiles;

public class Achievement {
	public final String name;
	public final String desc;
	public final boolean secret;
	public final int reward;
	
	public Achievement(String name, String desc, boolean secret, int reward) {
		this.name = name;
		this.desc = desc;
		this.secret = secret;
		this.reward = reward;
	}
	
	public String toString() {
		return (secret ? "*" : "") + name + " : " + desc + (secret ? "*" : "") ;
	}
	
}
