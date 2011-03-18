package bigBang.library.client.userInterface.view;

import java.util.Iterator;

import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.Caption.CaptionRegion;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.WindowPanel.WindowState;
import org.gwt.mosaic.ui.client.WindowPanel.WindowStateListener;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class PopupPanel extends View implements HasWidgets {

	private WindowPanel popup;
	private boolean showGlassPanel = false;
	private SimplePanel panel;

	public PopupPanel(boolean withGlassPanel, String title) {
		super();

		popup = new WindowPanel(title);
		popup.setAnimationEnabled(true);
		
		panel = new SimplePanel();
		panel.setSize("100%", "100%");
		
		initWidget(panel);
		popup.setWidget(this);

		popup.getHeader().add(Caption.IMAGES.window().createImage());

		//addMaximizeButton(popup, CaptionRegion.RIGHT);

		showGlassPanel = withGlassPanel;
	}

	public PopupPanel(){
		this(true, "");
	}
	
	public PopupPanel(String title) {
		this(true, title);
	}
	
	public void center(){
		if(this.showGlassPanel)
			popup.showModal();
		else
			popup.center();
	}

	public void hidePopup() {
		this.popup.hide();
	}

	private void addMaximizeButton(final WindowPanel windowPanel,
			CaptionRegion captionRegion) {
		final ImageButton maximizeBtn = new ImageButton(
				Caption.IMAGES.windowMaximize());
		maximizeBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (windowPanel.getWindowState() == WindowState.MAXIMIZED) {
					windowPanel.setWindowState(WindowState.NORMAL);
				} else {
					windowPanel.setWindowState(WindowState.MAXIMIZED);
				}
			}
		});
		windowPanel.addWindowStateListener(new WindowStateListener() {
			public void onWindowStateChange(WindowPanel sender,
					WindowState oldWindowState, WindowState newWindowState) {
				if (newWindowState == WindowState.MAXIMIZED) {
					maximizeBtn.setImage(Caption.IMAGES.windowRestore().createImage());
				} else {
					maximizeBtn.setImage(Caption.IMAGES.windowMaximize().createImage());
				}

			}
		});
		windowPanel.getHeader().add(maximizeBtn, captionRegion);
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

}
