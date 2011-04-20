package bigBang.library.client.userInterface;

import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.util.ButtonHelper;
import org.gwt.mosaic.ui.client.util.ButtonHelper.ButtonLabelType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import bigBang.library.client.resources.Resources;
import bigBang.library.client.userInterface.view.View;

public class ListHeader extends View {

	private Label headerLabel;
	private HasWidgets rightWidgetContainer;
	private HasWidgets leftWidgetContainer;
	
	private PushButton refreshButton;
	private ToolButton newButton;

	public ListHeader(){
		HorizontalPanel headerWrapper = new HorizontalPanel();
		headerLabel = new Label();
		headerWrapper.setSize("100%", "40px");
		headerWrapper.setSpacing(5);
		headerWrapper.getElement().getStyle().setBackgroundImage("url(images/listHeaderBackground1.png)");
		headerWrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		headerWrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		rightWidgetContainer = new SimplePanel();
		leftWidgetContainer = new SimplePanel();
		
		headerWrapper.add((Widget) leftWidgetContainer);
		headerWrapper.add(headerLabel);
		headerWrapper.setCellWidth(headerLabel, "100%");
		headerWrapper.add((Widget) rightWidgetContainer);

		headerLabel.getElement().getStyle().setFontSize(14, Unit.PX);
		headerLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);

		initWidget(headerWrapper);
	}

	public void setText(String text){
		headerLabel.setText(text);
	}
	
	public void setRightWidget(Widget w){
		rightWidgetContainer.clear();
		if(w == null)
			return;
		rightWidgetContainer.add(w);
	}
	
	public void setLeftWidget(Widget w){
		leftWidgetContainer.clear();
		if(w == null)
			return;
		leftWidgetContainer.add(w);
	}
	
	public void showRefreshButton(){
		Resources r = GWT.create(Resources.class);
		refreshButton = new PushButton(new Image(r.listRefreshIcon()));
		refreshButton.setTitle("Refrescar");
		setLeftWidget(refreshButton);
	}
	
	public HasClickHandlers getRefreshButton() {
		return refreshButton;
	}
	
	public void showNewButton(String text){
		Resources r = GWT.create(Resources.class);
		newButton = new ToolButton(ButtonHelper.createButtonLabel(
				AbstractImagePrototype.create(r.listNewIcon()), text,
		        ButtonLabelType.TEXT_ON_LEFT));
		
		setRightWidget(newButton);
	}
	
	public ToolButton getNewButton() {
		return newButton;
	}

}
