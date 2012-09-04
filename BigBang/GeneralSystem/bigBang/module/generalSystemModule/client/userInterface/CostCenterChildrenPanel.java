package bigBang.module.generalSystemModule.client.userInterface;

import bigBang.definitions.shared.CostCenter;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.user.client.ui.StackPanel;

public class CostCenterChildrenPanel extends View {

	protected CostCenter costCenter;

	public CostCenterMembersList membersList;
	
	public CostCenterChildrenPanel() {
		StackPanel wrapper = new StackPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		membersList = new CostCenterMembersList();
		
		wrapper.add(membersList, "Utilizadores membro");
	}

	@Override
	protected void initializeView() {
		return;
	}
	
	public void setCostCenter(CostCenter costCenter) {
		this.costCenter = costCenter;
		String costCenterId = costCenter == null ? null : costCenter.id;
		
		this.membersList.setOwner(costCenterId);
	}
	
}
