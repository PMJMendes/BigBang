package bigBang.library.shared.userInterface;

import bigBang.library.shared.EventBus;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;

public class TextBadge extends Composite {

	private GwtEvent.Type<?> eventType;
	private EventHandler handler;
	private SimplePanel panel;
	private String text;
	private boolean autoHide = true;
	

	public TextBadge(String text, GwtEvent.Type<?> eventType, EventHandler handler){
		this(text);
		setEventType(eventType);
		setEventHandler(handler);
	}
	
	public TextBadge(String text){
		this.text = text;
		panel = new SimplePanel();
		panel.setHeight("15px");		
		initWidget(panel);
		setText(text);
	}

	public void setText(String text) {
		updateText(text);
		
		this.panel.getElement().getStyle().setDisplay(autoHide && (text == null || text.equals("")) ? Display.NONE : Display.BLOCK);
	}
	
	public String getText() {
		return this.text;
	}

	public void setEventType(GwtEvent.Type<?> eventType) {
		this.eventType = eventType;
	}

	public GwtEvent.Type<?> getEventType() {
		return eventType;
	}

	public void setEventHandler(EventHandler handler) {
		this.handler = handler;
	}

	public EventHandler getEventHandler() {
		return handler;
	}
	
	private void updateText(String text) {
		panel.getElement().setInnerHTML("" +
				"<table cellpadding=\"0\" cellspacing=\"0\"><tr><td class=\"badge-cell\">" + 
				"<div class=\"badge-left\"></div> " +
				"<div class=\"badge-middle\"></div> " +
				"<div class=\"badge-right\"></div> " +
				"<div class=\"badge-label\">" + text + "</div>" + 
				"</td></tr></table>");
	}
	
	public void setAutoHide(boolean autoHide) {
		this.autoHide = autoHide;
	}

}
