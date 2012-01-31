package bigBang.module.mainModule.client.userInterface.view;

import org.gwt.mosaic.ui.client.SheetPanel;

import com.google.gwt.dom.client.StyleInjector;

public class PreferencesPanelView extends SheetPanel {
	
	public PreferencesPanelView(Resources resources) {
		super(resources);
		StyleInjector.inject(resources.sheetPanelCss().getText());
		
		this.setWidth("500px");
		this.setHeight("300px");
	}

}
