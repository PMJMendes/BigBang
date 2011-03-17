package bigBang.module.mainModule.client.userInterface.view;

import org.gwt.mosaic.ui.client.SheetPanel;
import org.gwt.mosaic.ui.client.layout.AbsoluteLayout;
import org.gwt.mosaic.ui.client.layout.AbsoluteLayout.DimensionPolicy;
import org.gwt.mosaic.ui.client.layout.AbsoluteLayout.MarginPolicy;
import org.gwt.mosaic.ui.client.layout.AbsoluteLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

public class PreferencesPanelView extends SheetPanel {
	
	public PreferencesPanelView(Resources resources) {
		super(resources);
		StyleInjector.inject(resources.sheetPanelCss().getText());

		this.setWidth("500px");
		this.setHeight("400px");
		this.setWidget(getContent());
	}

	private Widget getContent(){
		// Create a layout panel to align the widgets
		final LayoutPanel layoutPanel = new LayoutPanel(new AbsoluteLayout("32em",
		"24em"));
		layoutPanel.setPadding(0);

		layoutPanel.add(new Button("Icon"), new AbsoluteLayoutData("1em", "1em",
				"8em", "8em", MarginPolicy.RIGHT_BOTTOM, true));
		layoutPanel.add(new Button("mensagem para o user."),
				new AbsoluteLayoutData("10em", "1em", "21em", "19em",
						MarginPolicy.NONE, DimensionPolicy.BOTH));

		final ClickHandler clickHandler = new ClickHandler() {
			public void onClick(ClickEvent clickEvent) {
				hide();
			}
		};

		layoutPanel.add(new Button("OK", clickHandler), new AbsoluteLayoutData(
				"14em", "21em", "8em", "2em", MarginPolicy.LEFT_TOP));
		layoutPanel.add(new Button("Cancel", clickHandler), new AbsoluteLayoutData(
				"23em", "21em", "8em", "2em", MarginPolicy.LEFT_TOP));

		layoutPanel.add(new Button("Help"), new AbsoluteLayoutData("1em", "21em",
				"8em", "2em", MarginPolicy.RIGHT_TOP));

		return layoutPanel;
	}
}
