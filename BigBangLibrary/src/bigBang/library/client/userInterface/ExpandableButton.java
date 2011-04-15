package bigBang.library.client.userInterface;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class ExpandableButton extends ToggleButton {

	protected Widget widget;
	protected int x = -1, y = -1;
	protected PopupPanel popup;
	
	public ExpandableButton(){
		this("", null);
	}
	
	public ExpandableButton(Widget w) {
		this("", w);
	}
	
	public ExpandableButton(String text) {
		this(text, null);
	}
	
	public ExpandableButton(String text, Widget w) {
		super();
		setText(text == null ? "" : text);
		setWidget(w);
		
		popup = new PopupPanel(true);
		popup.addCloseHandler(new CloseHandler<PopupPanel>() {
			
			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				hideWidget();
			}
		});
		
		addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(isDown())
					showWidget();
				else
					hideWidget();
			}
		});
	}
	
	public void setWidget(Widget w) {
		this.widget = w;
	}
	
	
	public void showWidget(){
		if(!isAttached())
			return;
		if(this.widget == null)
			throw new RuntimeException("ExpandableButton widget was not initialized");
		
		popup.setWidget(widget);
		popup.setPopupPositionAndShow(new PositionCallback() {
			
			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {
				popup.setPopupPosition(0, 0);
			}
		});
		setDown(true);
	}
	
	public void hideWidget(){
		popup.hide();
		setDown(false);
	}
	
	
	
	
	
}
