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

	protected class Point2D {
		int x;
		int y;
	}

	protected static final int SELECTION_BORDER_WIDTH = 2;

	protected Point2D selectionOrigin, selectionDestination;

	protected boolean cropMode = false;
	protected boolean cropping = false;

	protected AbsolutePanel rectangle;
	protected ToggleButton cropButton;

	public ReceiptImageHandlerPanel(){
		super();

		this.rectangle = new AbsolutePanel();
		this.rectangle.getElement().getStyle().setPosition(Position.ABSOLUTE);
		this.rectangle.getElement().getStyle().setBorderColor("red");
		this.rectangle.getElement().getStyle().setBorderWidth(SELECTION_BORDER_WIDTH, Unit.PX);
		this.rectangle.getElement().getStyle().setBorderStyle(BorderStyle.DASHED);
		this.rectangle.getElement().getStyle().setBackgroundColor("white");
		this.rectangle.getElement().getStyle().setOpacity(0.5);
		wrapper.add(this.rectangle);

		image.unsinkEvents(Event.MOUSEEVENTS);
		wrapper.sinkEvents(Event.MOUSEEVENTS);
		wrapper.addDomHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent event) {
				selectionOrigin = null;
				selectionDestination = null;

				drawRect();

				selectionOrigin = getProjectedPoint(event.getRelativeX(wrapper.getElement()), event.getRelativeY(wrapper.getElement()));
				cropping = cropMode;
			}
		}, MouseDownEvent.getType());
		wrapper.addDomHandler(new MouseUpHandler() {

			@Override
			public void onMouseUp(MouseUpEvent event) {
				selectionDestination = getProjectedPoint(event.getRelativeX(wrapper.getElement()), event.getRelativeY(wrapper.getElement()));
				drawRect();
				cropping = false;
			}
		}, MouseUpEvent.getType());
		wrapper.addDomHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent event) {
				if(cropping){
					selectionDestination = getProjectedPoint(event.getRelativeX(wrapper.getElement()), event.getRelativeY(wrapper.getElement()));
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

						selectionOrigin = null;
						selectionDestination = null;

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

								selectionOrigin = null;
								selectionDestination = null;

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

					selectionOrigin = null;
					selectionDestination = null;

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
		if(cropMode && selectionOrigin != null && selectionDestination != null) {

			Point2D normalizedOrigin = new Point2D();
			normalizedOrigin.x = Math.min(selectionOrigin.x, selectionDestination.x);
			normalizedOrigin.y = Math.min(selectionOrigin.y, selectionDestination.y);

			Point2D normalizedDestination = new Point2D();
			normalizedDestination.x = Math.max(selectionOrigin.x, selectionDestination.x);
			normalizedDestination.y = Math.max(selectionOrigin.y, selectionDestination.y);

			Point2D scaledSelectionOrigin = getScaledPoint(normalizedOrigin);
			Point2D scaledSelectionDestination = getScaledPoint(normalizedDestination);

			int offsetX = image.getElement().getAbsoluteLeft() - wrapper.getElement().getAbsoluteLeft();
			int offsetY = image.getElement().getAbsoluteTop() - wrapper.getElement().getAbsoluteTop();

			this.rectangle.getElement().getStyle().setLeft(offsetX + scaledSelectionOrigin.x, Unit.PX);
			this.rectangle.getElement().getStyle().setTop(offsetY + scaledSelectionOrigin.y, Unit.PX);
			this.rectangle.setSize(Math.abs(scaledSelectionDestination.x - scaledSelectionOrigin.x) + "px",
					Math.abs(scaledSelectionDestination.y - scaledSelectionOrigin.y) + "px");

			if(!this.rectangle.isVisible()) {
				this.rectangle.setVisible(true);
			}
		}else if(selectionOrigin != null){
			this.rectangle.setVisible(false);
			selectionOrigin = null;
			selectionDestination = null;
		}
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
		}else{
			drawRect();
		}
	}

	public Rectangle getSelection(){
		if(!this.rectangle.isVisible()){
			return null;
		}

		Rectangle result = new Rectangle();
		result.x1 = Math.min(selectionOrigin.x, selectionDestination.x);
		result.x2 = Math.max(selectionOrigin.x, selectionDestination.x);
		result.y1 = Math.min(selectionOrigin.y, selectionDestination.y);
		result.y2 = Math.max(selectionOrigin.y, selectionDestination.y);

		return result;
	}

	public void showCutOption(boolean show) {
		this.cropButton.setVisible(show);
	}

	protected Point2D getProjectedPoint(int x, int y) {
		Point2D scaledPoint = new Point2D();

		int offsetX = image.getElement().getAbsoluteLeft() - wrapper.getElement().getAbsoluteLeft();
		int offsetY = image.getElement().getAbsoluteTop() - wrapper.getElement().getAbsoluteTop();

		scaledPoint.x = x < offsetX ? 0 : x > offsetX + image.getOffsetWidth() ? image.getOffsetWidth() : x - offsetX;
		scaledPoint.y = y < offsetY ? 0 : y > offsetY + image.getOffsetHeight() ? image.getOffsetHeight() : y - offsetY;

		double scale = (double)originalImageHeight / (double)image.getOffsetHeight();
		Point2D result = new Point2D();
		result.x = (int)(((double)scaledPoint.x) * scale);
		result.y = (int)(((double)scaledPoint.y) * scale);

		return result;
	}

	protected Point2D getScaledPoint(Point2D originalPoint) {
		double scale = (double)originalImageHeight / (double)image.getOffsetHeight();
		Point2D result = new Point2D();
		result.x = (int)(((double)originalPoint.x) / scale);
		result.y = (int)(((double)originalPoint.y) / scale);

		return result;
	}

}
