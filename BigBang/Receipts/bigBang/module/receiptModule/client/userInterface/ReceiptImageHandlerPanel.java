package bigBang.module.receiptModule.client.userInterface;

import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;

import bigBang.library.client.userInterface.ImageHandlerPanel;

public class ReceiptImageHandlerPanel extends ImageHandlerPanel {

	protected int rectOriginX = -1;
	protected int rectOriginY = -1;
	protected int rectDestX = -1;
	protected int rectDestY = -1;

	protected boolean cropMode = false;

	protected AbsolutePanel rectangle;

	public ReceiptImageHandlerPanel(){
		super();

		this.rectangle = new AbsolutePanel();
		this.rectangle.getElement().getStyle().setPosition(Position.ABSOLUTE);
		this.rectangle.getElement().getStyle().setBorderColor("red");
		this.rectangle.getElement().getStyle().setBorderWidth(3, Unit.PX);
		this.rectangle.getElement().getStyle().setBorderStyle(BorderStyle.DASHED);
		this.rectangle.getElement().getStyle().setBackgroundColor("white");
		this.rectangle.getElement().getStyle().setOpacity(0.5);

		image.unsinkEvents(Event.MOUSEEVENTS);
		wrapper.sinkEvents(Event.MOUSEEVENTS);
		wrapper.addDomHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent event) {
				rectOriginX = event.getRelativeX(wrapper.getElement());
				rectOriginY = event.getRelativeY(wrapper.getElement());
				cropMode = true;
				drawRect();
			}
		}, MouseDownEvent.getType());
		wrapper.addDomHandler(new MouseUpHandler() {

			@Override
			public void onMouseUp(MouseUpEvent event) {
				rectDestX = event.getRelativeX(wrapper.getElement());
				rectDestY = event.getRelativeY(wrapper.getElement());
				cropMode = false;
				drawRect();
			}
		}, MouseUpEvent.getType());
		wrapper.addDomHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent event) {
				if(cropMode){
					rectDestX = event.getRelativeX(wrapper.getElement());
					rectDestY = event.getRelativeY(wrapper.getElement());
				}
				drawRect();
			}
		}, MouseMoveEvent.getType());
	}

	protected void drawRect(){
		if(rectOriginX >= 0 && rectOriginY >= 0 && rectDestX >= 0 && rectDestY >= 0) {
			if(this.rectangle.getParent() == null || (this.rectangle.getParent().getElement() != this.wrapper.getElement())) {
				this.wrapper.add(this.rectangle);
			}
			
			int originX = rectOriginX < rectDestX ? rectOriginX : rectDestX;
			originX = originX < image.getOriginLeft() ? image.getOriginLeft() : originX;
			originX = originX > (image.getOriginLeft() + image.getOffsetWidth()) ? (image.getOriginLeft() + image.getOffsetWidth()) : originX;

			int originY = rectOriginY < rectDestY ? rectOriginY : rectDestY;
			originY = originY < image.getOriginTop() ? image.getOriginTop() : originY;
			originY = originY > (image.getOriginTop() + image.getOffsetHeight()) ? (image.getOriginTop() + image.getOffsetHeight()) : originY;
			
			int height = Math.abs(rectOriginY - rectDestY);
			height = (originY + height) > (image.getOriginTop() + image.getOffsetHeight()) ? (image.getOriginTop() + image.getOffsetHeight()) : height;
			
			int width = Math.abs(rectOriginX - rectDestX);
			width = (originX + width) > (image.getOriginLeft() + image.getOffsetWidth()) ? (image.getOriginLeft() + image.getOffsetWidth()) : width;

			this.rectangle.getElement().getStyle().setTop(originY, Unit.PX);
			this.rectangle.getElement().getStyle().setLeft(originX, Unit.PX);
			this.rectangle.setSize(width + "px", height + "px");
		}else{
			this.rectangle.removeFromParent();
			this.rectOriginX = rectOriginY = rectDestX = rectDestY = 0;
		}
	}


}
