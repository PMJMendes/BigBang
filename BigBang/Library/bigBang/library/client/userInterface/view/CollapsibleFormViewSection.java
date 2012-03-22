package bigBang.library.client.userInterface.view;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class CollapsibleFormViewSection extends FormViewSection {

	protected DisclosurePanel disclosurePanel;
	
	public CollapsibleFormViewSection(String title) {
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
	}
	
	public void expand(){
		this.disclosurePanel.setOpen(true);
	}
	
	public void collapse(){
		this.disclosurePanel.setOpen(false);
	}

}
