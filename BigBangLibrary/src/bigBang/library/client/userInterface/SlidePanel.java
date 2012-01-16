package bigBang.library.client.userInterface;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import bigBang.library.client.userInterface.view.View;

public class SlidePanel extends View {

	public enum Direction {
		UP,
		DOWN,
		LEFT,
		RIGHT
	}

	protected AbsolutePanel canvas;
	protected Widget mainWidget;
	protected HandlerRegistration widgetAttachHandler;
	protected Widget currentWidget;
	
	public SlidePanel(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		
		canvas = new AbsolutePanel();
		canvas.setSize("100%", "100%");
		
		wrapper.add(canvas);
		wrapper.setSize("100%", "100%");
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	public void slideInto(Widget w, Direction d){
		slideInto(w, d, null);
	}
	
	public boolean slideInto(final Widget w, final Direction d, final AsyncCallback<Void> callback){
		if(w == this.mainWidget)
			return false;

		final Widget currentChild = mainWidget;
		final int currentChildWidth = getOffsetWidth();
		final int currentChildHeight = getOffsetHeight();

		final int runTime = 500;
		
		AttachEvent.Handler attachHandler = new AttachEvent.Handler() {

			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(!event.isAttached() || w.getParent().getElement() != canvas.getElement()){
					widgetAttachHandler.removeHandler();
					return;
				}
				
				Animation animation = new Animation() {
					
					private SimplePanel cover;
					
					@Override
					protected void onStart() {
						super.onStart();
						if(widgetAttachHandler != null){
							widgetAttachHandler.removeHandler();
							widgetAttachHandler = null;
						}
						cover = new SimplePanel();
						cover.setSize(getOffsetWidth() + "px", getOffsetHeight() + "px");
						canvas.add(cover, 0, 0);
					}
					
					@Override
					protected void onCancel() {
						super.onCancel();
					}
					
					@Override
					protected void onComplete() {
						super.onComplete();
						if(callback != null)
							callback.onSuccess(null);
						if(mainWidget != null && mainWidget.isAttached() && mainWidget.getParent() != null){
							try{
								mainWidget.removeFromParent();
							}catch(Exception e){
								
							}
						}
						if(cover != null)
							cover.removeFromParent();
						mainWidget = w;
					}
					
					@Override
					protected void onUpdate(double progress) {
						double offset, top, left;

						switch(d) {
						case UP:
							canvas.setWidgetPosition(w, 0, currentChildHeight);
							offset = progress * currentChildHeight;
							top = -offset;
							currentChild.getElement().getStyle().setTop(top, Unit.PX);
							w.getElement().getStyle().setTop(top + currentChildHeight, Unit.PX);
							break;
						case DOWN:
							canvas.setWidgetPosition(w, 0, -w.getOffsetHeight());
							offset = progress * currentChildHeight;
							top = offset;
							currentChild.getElement().getStyle().setTop(top, Unit.PX);
							w.getElement().getStyle().setTop(top - currentChildHeight, Unit.PX);
							break;
						case LEFT:
							canvas.setWidgetPosition(w, currentChildWidth, 0);
							offset = progress * currentChildWidth;
							left = -offset;
							currentChild.getElement().getStyle().setLeft(left, Unit.PX);
							w.getElement().getStyle().setLeft(left + currentChildWidth, Unit.PX);
							break;
						case RIGHT:
							canvas.setWidgetPosition(w, -w.getOffsetWidth(), 0);
							offset = progress * currentChildWidth;
							left = offset;
							currentChild.getElement().getStyle().setLeft(left, Unit.PX);
							w.getElement().getStyle().setLeft(left - currentChildWidth, Unit.PX);
							break;
						default:
							break;
						}
					}
				};
				animation.run(runTime);
			}
		};
		widgetAttachHandler = w.addAttachHandler(attachHandler);
		canvas.add(w);
		currentWidget = w;
		return true;
	}

	public Widget getCurrentWidget(){
		return this.currentWidget;
	}
	
	public void add(final Widget w) {
		clear();
		mainWidget = w;
		canvas.add(w, 0, 0);
	}
	
	public void clear(){
		if(this.widgetAttachHandler != null)
			this.widgetAttachHandler.removeHandler();
		this.canvas.clear();
	}
}
