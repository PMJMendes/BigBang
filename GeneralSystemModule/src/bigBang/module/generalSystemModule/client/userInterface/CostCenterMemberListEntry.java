package bigBang.module.generalSystemModule.client.userInterface;

import bigBang.library.client.resources.Resources;
import bigBang.library.client.userInterface.ListEntry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.module.generalSystemModule.shared.User;

public class CostCenterMemberListEntry extends ListEntry<String> {

	private Resources resources;
	private Panel viewIconWrapper;
	
	public CostCenterMemberListEntry(User user) {
		super(user.id);
		resources = GWT.create(Resources.class);
		viewIconWrapper = new VerticalPanel();
		viewIconWrapper.addDomHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				onDoubleClick(null);
			}
		}, ClickEvent.getType());
		
		setLeftWidget(viewIconWrapper);
		
		HorizontalPanel content = new HorizontalPanel();
		content.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		content.setSize("100%", "100%");
		Label nameLabel = new Label(user.name);
		Label usernameLabel = new Label("("+user.username+")");
		
		nameLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		nameLabel.getElement().getStyle().setFontSize(14, Unit.PX);
		
		usernameLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		usernameLabel.getElement().getStyle().setFontSize(11, Unit.PX);
		usernameLabel.getElement().getStyle().setMarginLeft(20, Unit.PX);
		
		content.add(nameLabel);
		content.add(usernameLabel);
		content.setCellHorizontalAlignment(usernameLabel, HasHorizontalAlignment.ALIGN_LEFT);
		content.setCellWidth(usernameLabel, "100%");
		
		setWidget(content);
		
		HorizontalPanel rightWidgetWrapper = new HorizontalPanel();
		rightWidgetWrapper.setSize("100px", "100%");
		Label label = new Label(user.role.name);
		label.getElement().getStyle().setFontWeight(FontWeight.NORMAL);		
		rightWidgetWrapper.add(label);
		setRightWidget(rightWidgetWrapper);
		
		setHeight("30px");
		setSelected(isSelected());
		setDoubleClickable(true);
	}
	
	@Override
	public void setSelected(boolean selected) {
		super.setSelected(selected);
		if(this.viewIconWrapper == null)
			return;
		this.viewIconWrapper.clear();
		this.viewIconWrapper.add(new Image(selected ? resources.viewIconSmallWhite() : resources.viewIconSmallBlack()));
	}
	
	@Override
	public void onDoubleClick(Event event){
		super.onDoubleClick(event);
		
	}

}
