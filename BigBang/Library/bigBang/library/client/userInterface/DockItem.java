package bigBang.library.client.userInterface;

import org.gwt.mosaic.ui.client.util.ButtonHelper.ButtonLabelType;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DockItem extends ToggleButton implements NavigationItem {

	public DockItem(String text, AbstractImagePrototype image, ClickHandler handler, Object value){
		super();
		setLabel(text, image);
		if(handler != null)
			this.addClickHandler(handler);
		this.setHeight("35px");
		this.representedValue = value;
	}

	public DockItem(String text, AbstractImagePrototype image, ClickHandler handler){
		super();
		setLabel(text, image);
		this.addClickHandler(handler);
		this.setHeight("35px");
	}

	public DockItem(String text, AbstractImagePrototype image){
		super();
		setLabel(text, image);
		this.setHeight("35px");
	}

	public DockItem(String text, AbstractImagePrototype image, Object value){
		super();
		setLabel(text, image);
		setRepresentedValue(value);
		this.setHeight("35px");
	}
	
	private void setLabel(String text, AbstractImagePrototype image){
		String label = getLabelHTML(text, image, ButtonLabelType.TEXT_ON_RIGHT, null);
		this.setHTML(label);
	}

	private String getLabelHTML(String text, AbstractImagePrototype image, ButtonLabelType type, String cssName){
		final HTML html = new HTML(text, false);
		final Image img = image.createImage();
		if (cssName != null) {
			html.addStyleDependentName(cssName);
			img.addStyleDependentName(cssName);
		}
		if (type == ButtonLabelType.TEXT_ONLY) {
			return text;
		} else if (type == ButtonLabelType.NO_TEXT) {
			return image.getHTML();
		} else if (type == ButtonLabelType.TEXT_ON_LEFT
				|| type == ButtonLabelType.TEXT_ON_RIGHT) {
			HorizontalPanel hpanel = new HorizontalPanel();
			if (cssName != null) {
				hpanel.addStyleName(cssName);
			}
			if (type == ButtonLabelType.TEXT_ON_LEFT) {
				hpanel.add(html);
				hpanel.add(new HTML("&nbsp;"));
				hpanel.add(img);
			} else {
				hpanel.add(img);
				hpanel.add(new HTML("&nbsp;"));
				hpanel.add(html);
			}
			hpanel.setCellVerticalAlignment(html, HasVerticalAlignment.ALIGN_MIDDLE);
			hpanel.setCellVerticalAlignment(img, HasVerticalAlignment.ALIGN_MIDDLE);
			return hpanel.getElement().getString();
		} else {
			VerticalPanel vpanel = new VerticalPanel();
			vpanel.setStyleName("dockItemButton");
			if (type == ButtonLabelType.TEXT_ON_TOP) {
				vpanel.add(html);
				vpanel.add(img);
			} else {
				vpanel.add(img);
				vpanel.add(html);
			}
			vpanel.setCellHorizontalAlignment(html,
					HasHorizontalAlignment.ALIGN_CENTER);
			vpanel.setCellHorizontalAlignment(img,
					HasHorizontalAlignment.ALIGN_CENTER);
			return vpanel.getElement().getString();
		}
	}

	private Object representedValue;

	public void setRepresentedValue(Object o) {
		this.representedValue = o;
	}

	public Object getRepresentedValue(){
		return this.representedValue;
	}
}

