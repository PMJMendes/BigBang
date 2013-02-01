package bigBang.library.client.userInterface.view;

import org.gwt.mosaic.ui.client.MessageBox;

import bigBang.library.client.RightClickable;
import bigBang.library.client.event.DoubleClickEvent;

import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public abstract class View extends Composite implements RightClickable, HasAllMouseHandlers {

	private boolean rightClickable = false;
	private boolean doubleClickable = false;

	public View(){
		super();
	}

	public Widget asWidget(){
		this.getElement().getStyle().setBackgroundColor("#FFFFFF");
		return this;
	}

	@Override
	protected void initWidget(Widget widget) {
		super.initWidget(widget);
		initializeView();
	}

	protected abstract void initializeView();

	public void setRightClickable(boolean rightClickable) {
		this.rightClickable = rightClickable;
		//if(rightClickable)
		sinkEvents(Event.ONCONTEXTMENU | Event.MOUSEEVENTS);
		//else
		//unsinkEvents(Event.ONCONTEXTMENU);
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
				onRightClick(event);
			}
			break;
		case Event.ONDBLCLICK:
			onDoubleClick(event);
			break;
		case Event.ONCONTEXTMENU:
			onRightClick(event);
			break;
		}
	}

	public void onRightClick(Event event){
		event.stopPropagation();
		event.preventDefault();
	}

	public void onDoubleClick(Event event){
		event.preventDefault();
		fireEvent(new DoubleClickEvent());
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

	public void showMessage(String message){
		MessageBox.info("Informação", message);
	}

	public void showErrorMessage(String message){
		MessageBox.error("Erro", message);
	}

	public void showWarningMessage(String message) {
		MessageBox.alert("Aviso", message);
	}

	@Override
	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
		return addDomHandler(handler, MouseDownEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
		return addDomHandler(handler, MouseUpEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return addDomHandler(handler, MouseOutEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return addDomHandler(handler, MouseOverEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
		return addDomHandler(handler, MouseMoveEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
		return addDomHandler(handler, MouseWheelEvent.getType());
	}

}
