package bigBang.library.shared.userInterface.view;

import bigBang.library.shared.RightClickable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class View extends Composite implements RightClickable {

	private AbsolutePanel loadingPanel;
	private boolean rightClickable = false;
	private boolean doubleClickable = false;

	public View(){
		super();
	}

	public Widget asWidget(){
		this.getElement().getStyle().setBackgroundColor("#FFFFFF");
		return this;
	}

	public View getInstance() {
		return new View();
	}

	public void setRightClickable(boolean rightClickable) {
		this.rightClickable = rightClickable;
		if(rightClickable)
			sinkEvents(Event.ONCONTEXTMENU);
		else
			unsinkEvents(Event.ONCONTEXTMENU);
	}

	public boolean isRightClickable(){
		return this.rightClickable;
	}

	public void setDoubleClickable(boolean doubleClickable) {
		this.doubleClickable = doubleClickable;
		if(doubleClickable)
			sinkEvents(Event.ONDBLCLICK);
		else
			unsinkEvents(Event.ONDBLCLICK);
	}

	public boolean isDoubleClickable(){
		return this.doubleClickable;
	}

	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);

		switch (DOM.eventGetType(event)) {
		case Event.ONMOUSEUP:
			if (DOM.eventGetButton(event) == Event.BUTTON_RIGHT) {
				if(this.rightClickable)
					onRightClick(event);
			}
			break;
		case Event.ONDBLCLICK:
			if(this.doubleClickable)
				onDoubleClick(event);
			break;
		case Event.ONCONTEXTMENU:
			if(this.rightClickable)
				onRightClick(event);
			break;

		default:
			break;
		}
	}

	public void onRightClick(Event event){
	}

	public void onDoubleClick(Event event){

	}

	public void reset(){
	}
	
	public void disableTextSelection(boolean disable){
		disableTextSelectInternal(this.getElement(), disable);
	}

	private native static void disableTextSelectInternal(Element e, boolean disable)/*-{ 
		  if (disable) { 
		    e.ondrag = function () { return false; }; 
		    e.onselectstart = function () { return false; }; 
		  } else { 
		    e.ondrag = null; 
		    e.onselectstart = null; 
		} 
	}-*/; 
}
