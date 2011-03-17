package bigBang.module.generalSystemModule.client.userInterface;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

import bigBang.library.shared.userInterface.ListEntry;
import bigBang.module.generalSystemModule.shared.CostCenter;

public class CostCenterListEntry extends ListEntry<String> {

	public CostCenterListEntry(CostCenter costCenter) {
		super(costCenter.id);
		setTitle(costCenter.name);
		HorizontalPanel labelWrapper = new HorizontalPanel();
		labelWrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		labelWrapper.setSize("150px", "100%");
		String text = (costCenter.members.length == 1 ? " membro" : " membros");
		if(costCenter.members.length == 0)
			text = "Sem membros";
		
		Label label = new Label(costCenter.members.length + text);
		label.getElement().getStyle().setFontWeight(FontWeight.NORMAL);		
		labelWrapper.add(label);
		setRightWidget(labelWrapper);
	}

}
