package tk.cyriellecentori.brainbot;

public class BotExceptions {
	public static class NumberException extends Exception {
		private static final long serialVersionUID = 1L;}
	public static class NegativeNumberException extends NumberException {
		private static final long serialVersionUID = 1L;}
	public static class NullNumberException extends NumberException {
		private static final long serialVersionUID = 1L;}
	public static class MoneyException extends Exception {
		private static final long serialVersionUID = 1L;}
}
