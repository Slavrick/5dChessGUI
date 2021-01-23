package GUI;

import java.util.ArrayList;

public class EventSource {
	ArrayList<MessageListener> subscribers;
	
	public EventSource() {
		subscribers = new ArrayList<MessageListener>();
	}
	
	public void addListener(MessageListener sub) {
		subscribers.add(sub);
	}
	
	public void broadcastEvent(MessageEvent e) {
		for(MessageListener sub: subscribers) {
			sub.handleMessage(e);
		}
	}
}

