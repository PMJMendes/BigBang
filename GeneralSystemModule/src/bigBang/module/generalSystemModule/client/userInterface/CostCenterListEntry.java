package bigBang.module.generalSystemModule.client.userInterface;

import bigBang.library.client.userInterface.ListEntry;
import bigBang.module.generalSystemModule.shared.CostCenter;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class CostCenterListEntry extends ListEntry<CostCenter> {

	private Label nMembersLabel;
	
	public CostCenterListEntry(CostCenter costCenter) {
		super(costCenter);
		setHeight("40px");
		HorizontalPanel labelWrapper = new HorizontalPanel();
		labelWrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		labelWrapper.setSize("150px", "100%");
		
		nMembersLabel = new Label();
		labelWrapper.add(nMembersLabel);
		setRightWidget(labelWrapper);
		
		setInfo(costCenter);
	}
	
	@Override
	public <I extends Object> void setInfo(I infoGeneric){
		CostCenter info	= (CostCenter) infoGeneric;
		
		setTitle(info.name);
		setText(info.code);
		
		String text = (info.members.length == 1 ? " membro" : " membros");
		if(info.members.length == 0)
			text = "Sem membros";
		else
			text = info.members.length + text;
		nMembersLabel.setText(text);
		
		nMembersLabel.getElement().getStyle().setFontWeight(FontWeight.NORMAL);		
	}

}
