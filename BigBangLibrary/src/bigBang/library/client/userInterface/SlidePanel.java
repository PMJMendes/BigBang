package bigBang.library.client.userInterface;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.AttachEvent;
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

	public SlidePanel(){
		canvas = new AbsolutePanel();
		canvas.setSize("100%", "100%");
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.add(canvas);
		wrapper.setSize("100%", "100%");
		initWidget(wrapper);
	}

	public void slideInto(final Widget w, final Direction d){
		final Widget currentChild = canvas.getWidget(0);
		final int currentChildWidth = getOffsetWidth();
		final int currentChildHeight = getOffsetHeight();

		final int runTime = 500;

		w.addAttachHandler(new AttachEvent.Handler() {

			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(!event.isAttached())
					return;
				Animation animation = new Animation() {
					
					private SimplePanel cover;
					
					@Override
					protected void onStart() {
						cover = new SimplePanel();
						cover.setSize("100%", "100%");
						canvas.add(cover, 0, 0);
						super.onStart();
					}
					
					@Override
					protected void onComplete() {
						cover.removeFromParent();
						super.onComplete();
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
						if(progress == 1)
							currentChild.removeFromParent();
					}
				};



				animation.run(runTime);
			}
		});
		canvas.add(w);
	}

	public void add(final Widget w) {
		canvas.clear();
		w.addAttachHandler(new AttachEvent.Handler() {

			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(!event.isAttached())
					return;
				setSize(w.getOffsetWidth()+"px", w.getOffsetHeight()+"px");
			}
		});

		canvas.add(w, 0, 0);
	}
	
	public void clear(){
		this.canvas.clear();
	}
}
