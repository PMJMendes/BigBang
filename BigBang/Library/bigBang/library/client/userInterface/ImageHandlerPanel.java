package bigBang.library.client.userInterface;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.library.client.userInterface.view.View;

public class ImageHandlerPanel extends View {

	protected final double STEP_SCALE = 0.1;
	
	protected ScrollPanel viewport;
	protected AbsolutePanel cover;
	protected Image image;
	
	protected boolean dragMode;
	protected int dragX;
	protected int dragY;

	public ImageHandlerPanel(){
		AbsolutePanel wrapper = new AbsolutePanel();
		initWidget(wrapper);

		wrapper.setSize("100%", "100");
		
		image = new Image();
		viewport = new ScrollPanel();
		viewport.setSize("100%", "100%");
		viewport.getElement().getStyle().setBackgroundColor("#000");
		wrapper.add(viewport, 0, 0);
		
		cover = new AbsolutePanel();
		cover.setSize("100%", "100%");
		wrapper.add(cover, 0, 0);

		VerticalPanel imageWrapper = new VerticalPanel();
		imageWrapper.setSize("100%", "100%");
		imageWrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		imageWrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		imageWrapper.add(image);
		
		viewport.add(imageWrapper);
		dragMode = false;

		image.addLoadHandler(new LoadHandler() {
			
			@Override
			public void onLoad(LoadEvent event) {
				showLoading(false);
			}
		});
		image.addMouseWheelHandler(new MouseWheelHandler() {
			
			@Override
			public void onMouseWheel(MouseWheelEvent event) {
				event.preventDefault();
				int upDown = event.getDeltaY() > 0 ? -1 : event.getDeltaY() < 0 ? 1 : 0;
				double scale = 1 + (upDown * STEP_SCALE); 
				int viewpointX = event.getRelativeX(image.getElement());
				int viewpointY = event.getRelativeY(image.getElement());
				zoom(scale, viewpointX, viewpointY);
			}
		});
		image.addDragStartHandler(new DragStartHandler() {
			
			@Override
			public void onDragStart(DragStartEvent event) {
				event.preventDefault();
			}
		});
		image.addMouseDownHandler(new MouseDownHandler() {
			
			@Override
			public void onMouseDown(MouseDownEvent event) {
				dragMode = true;
				dragX = event.getRelativeX(image.getElement());
				dragY = event.getRelativeY(image.getElement());
			}
		});
		image.addMouseUpHandler(new MouseUpHandler() {
			
			@Override
			public void onMouseUp(MouseUpEvent event) {
				dragMode = false;
				dragX = 0;
				dragY = 0;
			}
		});
		image.addMouseMoveHandler(new MouseMoveHandler() {
			
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				if(dragMode){
					int deltaX = event.getRelativeX(image.getElement()) - dragX;
					int deltaY = event.getRelativeY(image.getElement()) - dragY;
					scrollRelative(deltaX, deltaY);
				}
			}
		});
		image.getElement().getStyle().setCursor(Cursor.MOVE);
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	public void setImage(String url){
		image.setSize("", "");
		showLoading(true);
		image.setUrl(url);
		image.addLoadHandler(new LoadHandler() {
			
			@Override
			public void onLoad(LoadEvent event) {
				fitToViewport();
			}
		});
	}
	
	public void fitToViewport(){
		int imageWidth = image.getOffsetWidth();
		int imageHeight = image.getOffsetHeight();
		
		if(imageWidth > imageHeight){
			fitToViewportWidth();
		}else{
			fitToViewportHeight();
		}
	}

	protected void fitToViewportHeight(){
		double imageWidth = image.getOffsetWidth();
		double imageHeight = image.getOffsetHeight();
		
		if(imageWidth == 0 || imageHeight == 0){
			return;
		}
		double ratio = (double)(imageWidth / imageHeight);
		int viewportHeight = this.getViewportHeight();

		image.setHeight((int)viewportHeight + "px");
		image.setWidth((int)viewportHeight * ratio + "px");
	}

	protected void fitToViewportWidth(){
		double imageWidth = image.getOffsetWidth();
		double imageHeight = image.getOffsetHeight();

		if(imageWidth == 0 || imageHeight == 0){
			return;
		}
		
		double ratio = (double)(imageHeight / imageWidth);

		int viewportWidth = this.getViewportWidth();

		image.setWidth((int)viewportWidth + "px");
		image.setHeight((int)viewportWidth * ratio + "px");
	}
	
	public void centerViewport(){
		centerViewport(image.getOffsetWidth() / 2, image.getOffsetHeight() / 2);
	}
	
	public void centerViewport(int x, int y){
		int offsetX = getViewportWidth();
		int offsetY = getViewportHeight();

		if(offsetX <= 0){
			offsetX = 0;
		}else{
			offsetX = x - offsetX / 2;
		}
		if(offsetY <= 0){
			offsetY = 0;
		}else{
			offsetY = y - offsetY / 2;
		}

		viewport.setHorizontalScrollPosition(offsetX);
		viewport.setVerticalScrollPosition(offsetY);
	}
	
	public void zoom(double scale){
		int viewportCenterX = this.viewport.getAbsoluteLeft() - this.image.getAbsoluteLeft() + (this.getViewportWidth() / 2);
		int viewportCenterY = this.viewport.getAbsoluteTop() - this.image.getAbsoluteTop() + (this.getViewportHeight() / 2);
		this.zoom(scale, viewportCenterX, viewportCenterY);
	}

	public void zoom(double scale, int viewpointX, int viewpointY){
		int width = image.getOffsetWidth();
		int height = image.getOffsetHeight();

		width *= scale;
		height *= scale;

		if(width < this.getViewportWidth()
				&& height < this.getViewportHeight()){
			fitToViewport();
		}else{
			image.setSize(width+"px", height+"px");
			centerViewport(viewpointX, viewpointY);
		}
	}

	public void showLoading(boolean show) {
		if(show){
			this.cover.clear();
			VerticalPanel wrapper = new VerticalPanel();
			wrapper.setSize("100%", "100%");
			
			wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			wrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			
			Label loadingLabel = new Label("A carregar imagem...");
			loadingLabel.getElement().getStyle().setColor("#FFF");
			
			wrapper.add(loadingLabel);
			this.cover.add(wrapper);			
		}else{
			this.cover.clear();
		}
		this.cover.setVisible(show);
	}
	
	protected void scrollRelative(int x, int y){
		int newScrollX = viewport.getHorizontalScrollPosition() - x;
		int newScrollY = viewport.getVerticalScrollPosition() - y;
		
		if(newScrollX < 0){
			newScrollX = 0;
		}
		if(newScrollY < 0){
			newScrollY = 0;
		}
		
		viewport.setHorizontalScrollPosition(newScrollX);
		viewport.setVerticalScrollPosition(newScrollY);
	}
	
	protected int getViewportHeight(){
		return this.viewport.getOffsetHeight() - 20;
	}
	
	protected int getViewportWidth(){
		return this.viewport.getOffsetWidth() - 20;
	}
}
