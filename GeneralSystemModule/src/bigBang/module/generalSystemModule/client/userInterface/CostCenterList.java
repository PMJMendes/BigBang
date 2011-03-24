package bigBang.module.generalSystemModule.client.userInterface;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

import bigBang.library.client.userInterface.List;
import bigBang.module.generalSystemModule.shared.CostCenter;

public class CostCenterList extends List<CostCenter> {

	public CostCenterList(){
		super();
		HorizontalPanel headerWrapper = new HorizontalPanel();
		headerWrapper.setSize("100%", "40px");
		headerWrapper.getElement().getStyle().setBackgroundImage("url(images/listHeaderBackground1.png)");
		headerWrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		headerWrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		Label headerLabel = new Label("Centros de Custo");
		headerLabel.getElement().getStyle().setFontSize(14, Unit.PX);
		headerLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		headerWrapper.add(headerLabel);
		setHeaderWidget(headerWrapper);
	}
	
	@Override
	protected void updateFooterText(){
		this.setFooterText(this.size() + " Centros de Custo");
	}

}
