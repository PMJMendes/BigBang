package bigBang.module.receiptModule.client.userInterface;

import bigBang.definitions.shared.ImageItem;
import bigBang.definitions.shared.Rectangle;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.userInterface.ImageHandlerPanel;

import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ToggleButton;

public class ReceiptImageHandlerPanel extends ImageHandlerPanel {

	protected int rectOriginX = -1;
	protected int rectOriginY = -1;
	protected int rectDestX = -1;
	protected int rectDestY = -1;

	protected Rectangle selection;

	protected boolean cropMode = false;
	protected boolean cropping = false;

	protected AbsolutePanel rectangle;
	protected ToggleButton cropButton;

	public ReceiptImageHandlerPanel(){
		super();

		this.rectangle = new AbsolutePanel();
		this.rectangle.getElement().getStyle().setPosition(Position.ABSOLUTE);
		this.rectangle.getElement().getStyle().setBorderColor("red");
		this.rectangle.getElement().getStyle().setBorderWidth(2, Unit.PX);
		this.rectangle.getElement().getStyle().setBorderStyle(BorderStyle.DASHED);
		this.rectangle.getElement().getStyle().setBackgroundColor("white");
		this.rectangle.getElement().getStyle().setOpacity(0.5);
		wrapper.add(this.rectangle);

		image.unsinkEvents(Event.MOUSEEVENTS);
		wrapper.sinkEvents(Event.MOUSEEVENTS);
		wrapper.addDomHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent event) {
				rectOriginX = rectDestX = rectOriginY = rectDestY = -1;
				drawRect();
				rectOriginX = event.getRelativeX(wrapper.getElement());
				rectOriginY = event.getRelativeY(wrapper.getElement());
				cropping = cropMode;
			}
		}, MouseDownEvent.getType());
		wrapper.addDomHandler(new MouseUpHandler() {

			@Override
			public void onMouseUp(MouseUpEvent event) {
				rectDestX = event.getRelativeX(wrapper.getElement());
				rectDestY = event.getRelativeY(wrapper.getElement());
				drawRect();
				cropping = false;
			}
		}, MouseUpEvent.getType());
		wrapper.addDomHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent event) {
				if(cropping){
					rectDestX = event.getRelativeX(wrapper.getElement());
					rectDestY = event.getRelativeY(wrapper.getElement());
				}
				drawRect();
			}
		}, MouseMoveEvent.getType());

		showToolbar();

		this.cropButton = new ToggleButton("Recortar Talão", "Manipular Imagem");
		cropButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if(event.getValue()) {
					if(currentImageItem.pageNumber == 0) {
						cropMode = true;
						rectOriginX = rectOriginY = rectDestX = rectDestY = -1;
						image.getElement().getStyle().setCursor(Cursor.CROSSHAIR);
						fitToViewport();
					}else{
						imageService.getItemAsImage(currentImageItem.id, 0, new BigBangAsyncCallback<ImageItem>() {

							@Override
							public void onResponseSuccess(ImageItem result) {
								handleImageItem(result);
								formatButtons();
								fitToViewport();
								cropMode = true;
								rectOriginX = rectOriginY = rectDestX = rectDestY = -1;
								image.getElement().getStyle().setCursor(Cursor.CROSSHAIR);
							}

							@Override
							public void onResponseFailure(Throwable caught) {
								super.onResponseFailure(caught);
							}
						});
					}
				}else{
					cropMode = false;
					cropping = false;
					rectOriginX = rectOriginY = rectDestX = rectDestY = -1;
					drawRect();
					rectangle.setVisible(false);
					image.getElement().getStyle().setCursor(Cursor.MOVE);
				}
			}
		});
		cropButton.setWidth("150px");
		toolbar.setLeftWidget(cropButton);
		cropButton.setValue(false);
	}

	protected void drawRect(){
		if(cropMode && rectOriginX >= 0 && rectOriginY >= 0 && rectDestX >= 0 && rectDestY >= 0) {
			int originX = rectOriginX < rectDestX ? rectOriginX : rectDestX;
			originX = originX < (image.getElement().getAbsoluteLeft() - wrapper.getElement().getAbsoluteLeft()) ? (image.getElement().getAbsoluteLeft() - wrapper.getElement().getAbsoluteLeft()) : originX;
			originX = originX > (image.getElement().getAbsoluteLeft() - wrapper.getElement().getAbsoluteLeft() + image.getOffsetWidth()) ? (image.getElement().getAbsoluteLeft() - wrapper.getElement().getAbsoluteLeft() + image.getOffsetWidth()) : originX;

			int destX = rectOriginX > rectDestX ? rectOriginX : rectDestX;
			destX = destX < (image.getElement().getAbsoluteLeft() - wrapper.getElement().getAbsoluteLeft()) ? (image.getElement().getAbsoluteLeft() - wrapper.getElement().getAbsoluteLeft()) : destX;
			destX = destX > (image.getElement().getAbsoluteLeft() - wrapper.getElement().getAbsoluteLeft() + image.getOffsetWidth()) ? (image.getElement().getAbsoluteLeft() - wrapper.getElement().getAbsoluteLeft() + image.getOffsetWidth()) : destX;

			int originY = rectOriginY < rectDestY ? rectOriginY : rectDestY;
			originY = originY < (image.getElement().getAbsoluteTop() - wrapper.getElement().getAbsoluteTop()) ? (image.getElement().getAbsoluteTop() - wrapper.getElement().getAbsoluteTop()) : originY;
			originY = originY > (image.getElement().getAbsoluteTop() - wrapper.getElement().getAbsoluteTop() + image.getOffsetHeight()) ? (image.getElement().getAbsoluteTop() - wrapper.getElement().getAbsoluteTop() + image.getOffsetHeight()) : originY;

			int destY = rectOriginY > rectDestY ? rectOriginY : rectDestY;
			destY = destY < (image.getElement().getAbsoluteTop() - wrapper.getElement().getAbsoluteTop()) ? (image.getElement().getAbsoluteTop() - wrapper.getElement().getAbsoluteTop()) : destY;
			destY = destY > (image.getElement().getAbsoluteTop() - wrapper.getElement().getAbsoluteTop() + image.getOffsetHeight()) ? (image.getElement().getAbsoluteTop() - wrapper.getElement().getAbsoluteTop() + image.getOffsetHeight()) : destY;			

			int height = Math.abs(originY - destY);
			height = height > image.getOffsetHeight() ? image.getOffsetHeight() : height;

			int width = Math.abs(originX - destX);
			width = width > image.getOffsetWidth() ? image.getOffsetWidth() : width;

			this.rectangle.getElement().getStyle().setTop(originY, Unit.PX);
			this.rectangle.getElement().getStyle().setLeft(originX, Unit.PX);
			this.rectangle.setSize(width + "px", height + "px");

			int xOffset = image.getAbsoluteLeft() - wrapper.getAbsoluteLeft();
			int yOffset = image.getAbsoluteTop() - wrapper.getAbsoluteTop();
			this.selection = projectToRealImageCoordinates(originX - xOffset, originY - yOffset, destX - xOffset, destY - yOffset);

			if(!this.rectangle.isVisible()) {
				this.rectangle.setVisible(true);
			}
		}else if(rectOriginX != -1 && rectOriginY != -1){
			this.rectangle.setVisible(false);
			rectOriginX = rectOriginY = rectDestX = rectDestY = -1;
			selection = null;
		}
	}

	private Rectangle projectToRealImageCoordinates(int originX, int originY,
			int destX, int destY) {
		Rectangle result = new Rectangle();

		double scale = originalImageHeight / image.getOffsetHeight();
		result.x1 = (int)(((double)originX) * scale);
		result.x2 = (int)(((double)destX) * scale);
		result.y1 = (int)(((double)originY) * scale);
		result.y2 = (int)(((double)destY) * scale);
		return result;
	}

	@Override
	protected void formatButtons() {
		super.formatButtons();
		showToolbar();
	}

	@Override
	public void zoom(double scale, int viewpointX, int viewpointY) {
		if(!cropMode){
			super.zoom(scale, viewpointX, viewpointY);
		}
	}

	public Rectangle getSelection(){
		if(!this.rectangle.isVisible()){
			this.selection = null;
		}
		return this.selection;
	}

	public void showCutOption(boolean show) {
		this.cropButton.setVisible(show);
	}
	
}
