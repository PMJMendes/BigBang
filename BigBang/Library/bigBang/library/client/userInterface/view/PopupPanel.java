package bigBang.library.client.userInterface.view;

import java.util.Iterator;

import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.WindowPanel;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class PopupPanel extends View implements HasWidgets {

	private WindowPanel popup;
	private boolean showGlassPanel = false;
	private SimplePanel panel;

	public PopupPanel(boolean withGlassPanel, String title) {
		panel = new SimplePanel();
		initWidget(panel);
		
		showGlassPanel = withGlassPanel;
		
		panel.setSize("100%", "100%");

		this.popup = new WindowPanel(title);
		popup.setAnimationEnabled(true);
		popup.getHeader().add(Caption.IMAGES.window().createImage());
		
//		panel.addAttachHandler(new AttachEvent.Handler() {
//			
//			@Override
//			public void onAttachOrDetach(AttachEvent event) {
//				if(event.isAttached()) {
////					Element child = panel.getElement().getFirstChildElement();
////					child.getStyle().setProperty("minHeight", child.getOffsetHeight());
////					child.getStyle().setProperty("minWidth", child.getOffsetWidth());
//				}
//			}
//		});
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	public PopupPanel(){
		this(true, "");
	}
	
	public PopupPanel(String title) {
		this(true, title);
	}
	
	public void center(){
		popup.setWidget(this);
		if(this.showGlassPanel)
			popup.showModal();
		else{
			popup.center();
		}
	}

	public void hidePopup() {
		this.popup.hide();
	}

	public void add(Widget w) {
		this.panel.add(w);
	}

	public void clear() {
		this.panel.clear();
	}

	public Iterator<Widget> iterator() {
		return panel.iterator();
	}

	public boolean remove(Widget w) {
		return panel.remove(w);
	}
	
	public void setAutoHideEnabled(boolean hide) {
		this.popup.setAutoHideEnabled(hide);
	}

}
