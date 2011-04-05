package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.view.FormViewSection;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.client.userInterface.CostCenterList;
import bigBang.module.generalSystemModule.client.userInterface.CostCenterListEntry;
import bigBang.module.generalSystemModule.client.userInterface.CostCenterMemberList;
import bigBang.module.generalSystemModule.client.userInterface.CostCenterMemberListEntry;
import bigBang.module.generalSystemModule.client.userInterface.presenter.CostCenterManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.shared.CostCenter;
import bigBang.module.generalSystemModule.shared.User;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CostCenterManagementOperationView extends View implements CostCenterManagementOperationViewPresenter.Display {

	private static final int LIST_WIDTH = 400; //px

	private CostCenterList costCenterList;
	private CostCenterMemberList memberList;
	private CostCenterForm costCenterForm;
	
	public CostCenterManagementOperationView() {
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		wrapper.setSize("100%", "100%");

		costCenterList = new CostCenterList();
		costCenterList.setSize("100%", "100%");
		wrapper.addWest(costCenterList, LIST_WIDTH);
		wrapper.setWidgetMinSize(costCenterList, LIST_WIDTH);

		final VerticalPanel previewWrapper = new VerticalPanel();
		previewWrapper.setSize("100%", "100%");
		
		costCenterForm = new CostCenterForm();

		previewWrapper.add(costCenterForm.getNonScrollableContent());
		
		previewWrapper.add(new FormViewSection("Membros").getHeader());
		
		memberList = new CostCenterMemberList();
		previewWrapper.add(memberList);
		previewWrapper.setCellHeight(memberList, "100%");

		wrapper.add(previewWrapper);

		initWidget(wrapper);
	}

	@Override
	public HasValueSelectables<CostCenter> getList() {
		return (HasValueSelectables<CostCenter>)this.costCenterList;
	}
	
	@Override
	public void clearList(){
		this.costCenterList.clear();
	}
	
	@Override
	public void addValuesToList(CostCenter[] result) {
		for(int i = 0; i < result.length; i++)
			this.costCenterList.add(new CostCenterListEntry(result[i]));
	}

	@Override
	public void removeCostCenterFromList(CostCenter c) {
		for(ListEntry<CostCenter> e : this.costCenterList){
			if(e.getValue() == c || e.getValue().id.equals(c.id)){
				this.costCenterList.remove(e);
				break;
			}
		}
	}
	
	@Override
	public HasValueSelectables<User> getMembersList() {
		return this.memberList;
	}

	@Override
	public void clearMembersList() {
		this.memberList.clear();
	}

	@Override
	public void addValuesToMembersList(User[] result) {
		for(int i = 0; i < result.length; i++)
			this.memberList.add(new CostCenterMemberListEntry(result[i]));
	}

	@Override
	public HasEditableValue<CostCenter> getForm() {
		return this.costCenterForm;
	}

	@Override
	public void prepareNewCostCenter() {
		for(ValueSelectable<CostCenter> s : this.costCenterList){
			if(s.getValue().id == null){
				s.setSelected(true, true);
				return;
			}
		}
		CostCenterListEntry entry = new CostCenterListEntry(new CostCenter());
		this.costCenterList.add(entry);
		this.costCenterList.getScrollable().scrollToBottom();
		entry.setSelected(true, true);
	}
	
	@Override
	public void removeNewCostCenterPreparation(){
		for(ValueSelectable<CostCenter> s : this.costCenterList){
			if(s.getValue().id == null){
				this.removeCostCenterFromList(s.getValue());
				break;
			}
		}
	}

	@Override
	public HasClickHandlers getNewButton() {
		return this.costCenterList.newButton;
	}

	@Override
	public HasClickHandlers getRefreshButton() {
		return this.costCenterList.refreshButton;
	}
	
	@Override
	public HasClickHandlers getSaveButton() {
		return this.costCenterForm.getSaveButton();
	}

	@Override
	public HasClickHandlers getEditButton() {
		return this.costCenterForm.getEditButton();
	}
	
	@Override
	public HasClickHandlers getDeleteButton() {
		return this.costCenterForm.getDeleteButton();
	}

	@Override
	public boolean isFormValid() {
		return this.costCenterForm.validate();
	}

	@Override
	public void lockForm(boolean lock) {
		this.costCenterForm.lock(lock);
	}

}
