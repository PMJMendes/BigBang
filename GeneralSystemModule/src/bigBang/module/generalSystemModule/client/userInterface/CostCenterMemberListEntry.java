package bigBang.module.generalSystemModule.client.userInterface;

import bigBang.definitions.shared.User;
import bigBang.library.client.userInterface.ListEntry;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CostCenterMemberListEntry extends ListEntry<User> {

	//private Resources resources;
	private Panel viewIconWrapper;
	
	public CostCenterMemberListEntry(User user) {
		super(user);
		//resources = GWT.create(Resources.class);
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
		Label nameLabel = new Label();
		Label usernameLabel = new Label();
		
		nameLabel.getElement().getStyle().setProperty("whiteSpace", "nowrap");
		
		titleLabel = nameLabel;
		textLabel = usernameLabel;
		
		setTitle(user.name);
		setText("("+user.username+")");
		
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
		Label label = new Label(user.profile.name);
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
		//this.viewIconWrapper.add(new Image(selected ? resources.viewIconSmallWhite() : resources.viewIconSmallBlack()));
	}

}
