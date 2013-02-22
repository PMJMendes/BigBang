package bigBang.library.client.userInterface.view;

import bigBang.library.client.resources.Resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.logical.shared.HasOpenHandlers;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class CollapsibleFormViewSection extends FormViewSection {

	protected DisclosurePanel disclosurePanel;
	protected Image headerExpandImage;
	
	public CollapsibleFormViewSection(String title) {
		this(title, true);
	}
	
	public CollapsibleFormViewSection(String title, boolean collapsed) {
		super(title);
		((Widget)content).getElement().getStyle().setProperty("minHeight", "0");
		
		this.disclosurePanel = new DisclosurePanel();
		this.disclosurePanel.setStyleName("bigBangDisclosurePanel");
		this.disclosurePanel.setAnimationEnabled(true);
		super.content.add(disclosurePanel);
		
		FlowPanel p = new FlowPanel();
		p.setWidth("100%");

		p.setStyleName("formSection");
		this.disclosurePanel.add(p);
		
		super.content = p;
		super.content.add((Widget) this.currentContainer);

		super.header.getElement().getStyle().setCursor(Cursor.POINTER);
		
		super.header.addDomHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(disclosurePanel.isOpen()) {
					collapse();
				}else{
					expand();
				}
			}
		}, ClickEvent.getType());
		
		if(collapsed){
			collapse();
		}else{
			expand();
		}
	}
	
	@Override
	protected Widget getSectionHeader(String text) {
		this.headerExpandImage = new Image();
		HorizontalPanel headerWrapper = new HorizontalPanel();
		headerWrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		headerWrapper.setWidth("100%");
		headerWrapper.setStylePrimaryName("formDisclosureSectionHeader");
		headerWrapper.add(this.headerExpandImage);
		Label titleLabel = new Label(text);
		titleLabel.getElement().getStyle().setProperty("marginLeft", "5px");
		this.headerLabel = titleLabel;
		titleLabel.getElement().getStyle().setColor("#FFFFFF");
		titleLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		headerWrapper.add(titleLabel);
		headerWrapper.setCellWidth(titleLabel, "100%");
		headerWrapper.getElement().getStyle().setPaddingLeft(10, Unit.PX);
		headerWrapper.setHeight("22px");
		this.header = headerWrapper;
		return headerWrapper;
	}
	
	public void expand(){
		this.disclosurePanel.setOpen(true);
		Resources r = GWT.create(Resources.class);
		this.headerExpandImage.setResource(r.arrowUp());
	}
	
	public void collapse(){
		this.disclosurePanel.setOpen(false);
		Resources r = GWT.create(Resources.class);
		this.headerExpandImage.setResource(r.arrowDown());
	}

	public HasOpenHandlers<DisclosurePanel> getCollapsiblePanelOpenHandler(){
		return disclosurePanel;
	}
	
	public HasCloseHandlers<DisclosurePanel> getCollapsiblePanelCloseHandler(){
		return disclosurePanel;
	}
}
