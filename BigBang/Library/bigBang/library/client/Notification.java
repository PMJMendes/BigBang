package bigBang.library.client;

import com.google.gwt.event.shared.GwtEvent;

public class Notification {
	
	public static enum TYPE {
		TRAY_NOTIFICATION,
		INFO_TRAY_NOTIFICATION,
		ALERT_NOTIFICATION,
		WARNING_NOTIFICATION,
		INFO_NOTIFICATION,
		LONG_ALERT_NOTIFICATION, 
		ERROR_NOTIFICATION,
		ERROR_TRAY_NOTIFICATION
	} 

	
	private String caption;
	private String content;
	private String details;
	private GwtEvent<?> event;
	
	public Notification(String caption, String content) {
		this.setCaption(caption);
		this.setContent(content);
	}

	public Notification(String caption, String content, GwtEvent<?> event) {
		this(caption, content);
		this.setEvent(event);
	}
	
	public Notification(String caption, String content, String details) {
		this(caption, content);
		this.details = details;
		this.setEvent(event);
	}
	
	private void setEvent(GwtEvent<?> event) {
		this.event = event;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	public void setDetails(String details) {
		this.details = details;
	}

	public String getCaption() {
		return caption;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}
	
	public String getDetails(){
		return this.details;
	}
	
	public GwtEvent<?> getEvent(){
		return event;
	}
	
}
