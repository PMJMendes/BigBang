package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.view.FormViewSection;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.client.userInterface.CostCenterList;
import bigBang.module.generalSystemModule.client.userInterface.CostCenterListEntry;
import bigBang.module.generalSystemModule.client.userInterface.CostCenterMemberList;
import bigBang.module.generalSystemModule.client.userInterface.presenter.CostCenterManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.shared.CostCenter;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CostCenterManagementOperationView extends View implements CostCenterManagementOperationViewPresenter.Display {

	private static final int LIST_WIDTH = 400; //px

	private CostCenterList costCenterList;
	private CostCenterMemberList memberList;
	private CostCenterForm costCenterForm;
	private CostCenterForm newCostCenterForm;

	private Button submitNewCostCenterButton;
	private Button editCostCenterButton;
	private Button saveCostCenterButton;
	private Button deleteCostCenterButton;
	
	private PopupPanel newCostCenterPopup;

	public CostCenterManagementOperationView() {
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		wrapper.setSize("100%", "100%");

		costCenterList = new CostCenterList();
		costCenterList.setSize("100%", "100%");
		wrapper.addWest(costCenterList, LIST_WIDTH);
		wrapper.setWidgetMinSize(costCenterList, LIST_WIDTH);

		final VerticalPanel previewWrapper = new VerticalPanel();
		previewWrapper.setSize("100%", "100%");

		this.submitNewCostCenterButton = new Button("Submeter");
		this.editCostCenterButton = new Button("Editar");	
		this.saveCostCenterButton = new Button("Guardar");
		this.deleteCostCenterButton = new Button("Apagar");
		
		costCenterForm = new CostCenterForm();
		costCenterForm.addButton(editCostCenterButton);
		costCenterForm.addButton(saveCostCenterButton);
		costCenterForm.addButton(deleteCostCenterButton);
		
		newCostCenterForm = new CostCenterForm();
		newCostCenterForm.addButton(this.submitNewCostCenterButton);
		newCostCenterForm.setReadOnly(false);

		previewWrapper.add(costCenterForm.getNonScrollableContent());
		
		previewWrapper.add(new FormViewSection("Membros").getHeader());
		
		memberList = new CostCenterMemberList();
		previewWrapper.add(memberList);
		previewWrapper.setCellHeight(memberList, "100%");

		wrapper.add(previewWrapper);

		initWidget(wrapper);
		
		newCostCenterPopup = new PopupPanel();
		newCostCenterPopup.add(newCostCenterForm.getNonScrollableContent());
	}

	@Override
	public HasValueSelectables<CostCenter> getList() {
		return (HasValueSelectables<CostCenter>)this.costCenterList;
	}
	
	@Override
	public void addValuesToList(CostCenter[] result) {
		for(int i = 0; i < result.length; i++)
			this.costCenterList.add(new CostCenterListEntry(result[i]));
	}

	@Override
	public void removeCostCenterFromList(CostCenter c) {
		for(ListEntry<CostCenter> e : this.costCenterList){
			if(e.getValue().id.equals(c.id))
				this.costCenterList.remove(e);
		}
	}

	@Override
	public HasValue<CostCenter> getForm() {
		return this.costCenterForm;
	}

	@Override
	public HasClickHandlers getDeleteButton() {
		// TODO Auto-generated method stub
		return null;
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
	public void prepareNewCostCenter() {
		for(ValueSelectable<CostCenter> s : this.costCenterList){
			if(s.getValue().id == null){
				s.setSelected(true, true);
				return;
			}
		}
		CostCenterListEntry entry = new CostCenterListEntry(new CostCenter());
		this.costCenterList.add(entry);
		entry.setSelected(true, true);
	}
	
	/*

	public void showDetailsForCostCenter(CostCenter costCenter) {
		if(costCenter == null) {
			costCenterForm.clearInfo();
			memberList.clear();
			return;
		}
		costCenterForm.setInfo(costCenter);
		memberList.clear();
		for(int i = 0; i < costCenter.members.length; i++){
			memberList.addListEntry(new CostCenterMemberListEntry(costCenter.members[i]));
		}
	}

	public void setCostCenterEntries(CostCenter[] entries) {
		costCenterList.clear();
		for(int i = 0; i < entries.length; i++) {
			CostCenterListEntry listEntry = new CostCenterListEntry(entries[i]);
			costCenterList.addListEntry(listEntry);
		}
		if(this.costCenterList.getSelectedEntry() == null && this.costCenterList.size() > 0)
			this.costCenterList.select(0);
	}

	public void updateCostCenterInfo(CostCenter result) {
		for(ListEntry<CostCenter> e : this.costCenterList.getListEntries()){
			if(e.getValue().id.equals(result.id)){
				e.setInfo(result);
				if(this.costCenterList.getSelectedEntry() == e)
					this.showDetailsForCostCenter(result);
				break;
			}
		}
	}

	@Override
	public HasClickHandlers getNewCostCenterButton() {
		return costCenterList.newButton;
	}

	@Override
	public HasClickHandlers getSubmitNewCostCenterButton() {
		return this.submitNewCostCenterButton;
	}
	
	@Override
	public CostCenter getNewCostCenterInfo() {
		return (CostCenter) this.newCostCenterForm.getInfo();
	}

	@Override
	public void showNewCostCenterForm(boolean show) {
		if(show){
			this.newCostCenterForm.clearInfo();
			this.newCostCenterPopup.setWidth("650px");
			this.newCostCenterPopup.center();
		}else
			this.newCostCenterPopup.hidePopup();
	}

	@Override
	public boolean isNewCostCenterFormValid() {
		return this.newCostCenterForm.validate();
	}

	@Override
	public HasClickHandlers getEditCostCenterButton() {
		return editCostCenterButton;
	}

	@Override
	public HasClickHandlers getSaveCostCenterButton() {
		return saveCostCenterButton;
	}

	@Override
	public HasClickHandlers getDeleteCostCenterButton() {
		return deleteCostCenterButton;
	}

	@Override
	public void setCostCenterFormEditable(boolean editable) {
		this.costCenterForm.setReadOnly(!editable);
	}

	@Override
	public boolean isCostCenterFormEditable() {
		return !costCenterForm.isReadOnly();
	}

	@Override
	public boolean isCostCenterFormValid() {
		return costCenterForm.validate();
	}

	@Override
	public CostCenter getCostCenterInfo() {
		return (CostCenter) costCenterForm.getInfo();
	}

	@Override
	public void removeCostCenter(String id) {
		for(ListEntry<CostCenter> e : this.costCenterList.getListEntries()) {
			if(e.getValue().id.equals(id)){
				this.costCenterList.removeListEntry(e);
				break;
			}
		}
	}

	@Override
	public HasClickHandlers getRefreshListButton() {
		return costCenterList.refreshButton;
	}

	@Override
	public void addCostCenter(CostCenter c) {
		CostCenterListEntry newEntry = new CostCenterListEntry(c);
		costCenterList.addListEntry(newEntry);
		costCenterList.select(newEntry);
	}

	@Override
	public void clearCostCenterListFilters() {
		this.costCenterList.clearFilters();
	}

	@Override
	public void showConfirmDelete(ConfirmationCallback callback) {
		MessageBox.confirm("Remover Centro de Custo", "Deseja realmente remover o centro de custo?", callback);
	}*/

	

}
