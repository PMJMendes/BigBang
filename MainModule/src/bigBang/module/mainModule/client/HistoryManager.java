package bigBang.module.mainModule.client;

import java.util.ArrayList;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;

/*
 * Manages the application navigation history
 */
public class HistoryManager implements ValueChangeHandler<String> {
	
	/*
	 * An item managed by HistoryManager
	 */
	public class HistoryItem {
		private String token; //the item descriptor
		private Event event; //The event triggered by the history item

		public HistoryItem(String token, Event event){
			this.token = token;
			this.event = event;
		}

		public String getToken() {
			return token;
		}
		
		public Event getEvent(){
			return event;
		}
	}
	
	private ArrayList <HistoryItem> historyStack;
	@SuppressWarnings("unused")
	private int currentIndex;
	
	public HistoryManager(){
		historyStack = new ArrayList <HistoryItem> ();
		History.addValueChangeHandler(this);
	}
	
	public void onValueChange(ValueChangeEvent<String> event) {

		//for(i = historyStack.size())
		
		//Seek back until finds this token
	}
	
	public void push(HistoryItem item){
		historyStack.add(item);
		History.newItem(item.getToken());
	}
	
	public HistoryItem seek() {
		return null;
	}
	
	public void seekBack() {
		//this.historyStack.
	}
	
	public HistoryItem seekForward() {
		return null;
	}
	
	public void newItem(HistoryItem item){
		
	}

}
