package bigBang.library.shared.userInterface;

import org.gwt.mosaic.ui.client.GlassPanel;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;

public class LoadingPanel extends Composite {

	private AbsolutePanel panel;
	private GlassPanel glassPanel;
	private AbsolutePanel animation;
	
	public LoadingPanel() {
		panel = new AbsolutePanel();
		panel.setStyleName("loadingPanelWrapper");
		
		glassPanel = new GlassPanel(false);
		glassPanel.addStyleName("whiteGlassPanel");
		glassPanel.setSize("100%", "100%");
		panel.add(glassPanel, 0, 0);
		
		animation = new AbsolutePanel();
		animation.setSize("100px", "100px");
		animation.setStyleName("loadingPanel");
		panel.add(animation, 0, 0);
		
		initWidget(panel);
		this.hide();
	}
	
	public void show(){
		this.getElement().getStyle().setDisplay(Display.BLOCK);
		this.setSize("100%", "100%");
		this.glassPanel.getElement().getStyle().setDisplay(Display.BLOCK);
		this.animation.getElement().getStyle().setDisplay(Display.BLOCK);
	}
	
	public void hide() {
		this.setSize("0px", "0px");
		this.getElement().getStyle().setDisplay(Display.NONE);
		this.glassPanel.getElement().getStyle().setDisplay(Display.NONE);
		this.animation.getElement().getStyle().setDisplay(Display.NONE);
	}
}
