package bigBang.library.shared;

import com.google.gwt.event.shared.GwtEvent;

public class Notification {
	
	public static enum TYPE {
		TRAY_NOTIFICATION,
		ALERT_NOTIFICATION,
		WARNING_NOTIFICATION,
		INFO_NOTIFICATION
	} 

	
	private String caption;
	private String content;
	private GwtEvent<?> event;
	
	public Notification(String caption, String content) {
		this.setCaption(caption);
		this.setContent(content);
	}

	public Notification(String caption, String content, GwtEvent<?> event) {
		this(caption, content);
		this.setEvent(event);
	}
	
	private void setEvent(GwtEvent<?> event) {
		this.event = event;
	}

	public void setCaption(String caption) {
		this.caption = caption;
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
	
	public GwtEvent<?> getEvent(){
		return event;
	}
	
}
