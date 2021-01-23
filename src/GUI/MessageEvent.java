package GUI;

public class MessageEvent {

	public MessageEvent() {}
	
	public MessageEvent(String string) {
		type = INFO;
		message = string;
	}
	
	public static final int Promotion = 1;
	public static final int INFO = 2;
	public int type;
	public int imess;
	public String message;
	public Object source;
}
