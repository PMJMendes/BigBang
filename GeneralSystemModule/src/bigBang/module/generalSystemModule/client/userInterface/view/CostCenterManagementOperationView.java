package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.client.userInterface.CostCenterList;
import bigBang.module.generalSystemModule.client.userInterface.CostCenterListEntry;
import bigBang.module.generalSystemModule.client.userInterface.CostCenterMemberList;
import bigBang.module.generalSystemModule.client.userInterface.CostCenterMemberListEntry;
import bigBang.module.generalSystemModule.client.userInterface.presenter.CostCenterManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.shared.CostCenter;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
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

	private Button newCostCenterButton;
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

		this.newCostCenterButton = new Button("Novo");
		this.submitNewCostCenterButton = new Button("Submeter");
		this.editCostCenterButton = new Button("Editar");	
		this.saveCostCenterButton = new Button("Guardar");
		this.deleteCostCenterButton = new Button("Apagar");
		
		costCenterForm = new CostCenterForm();
		costCenterForm.addButton(this.newCostCenterButton);
		costCenterForm.addButton(editCostCenterButton);
		costCenterForm.addButton(saveCostCenterButton);
		costCenterForm.addButton(deleteCostCenterButton);
		
		newCostCenterForm = new CostCenterForm();
		newCostCenterForm.addButton(this.submitNewCostCenterButton);
		newCostCenterForm.setReadOnly(false);

		previewWrapper.add(costCenterForm.getNonScrollableContent());

		costCenterForm.addHandler(new ResizeHandler() {
			
			@Override
			public void onResize(ResizeEvent event) {
				previewWrapper.setCellHeight(costCenterForm, costCenterForm.getOffsetHeight() + "px");
			}
		}, ResizeEvent.getType());
		
		memberList = new CostCenterMemberList();
		previewWrapper.add(memberList);
		previewWrapper.setCellHeight(memberList, "100%");

		wrapper.add(previewWrapper);

		initWidget(wrapper);
		
		newCostCenterPopup = new PopupPanel();
		newCostCenterPopup.add(newCostCenterForm.getNonScrollableContent());
	}

	public HasValue<CostCenter> getCostCenterList() {
		return this.costCenterList;
	}

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
			if(e.getValue().equals(result.id)){
				e.setInfo(result);
				if(this.costCenterList.getSelectedEntry() == e)
					this.showDetailsForCostCenter(result);
				break;
			}
		}
	}

	@Override
	public HasClickHandlers getNewCostCenterButton() {
		return this.newCostCenterButton;
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
			this.newCostCenterPopup.setSize("650px", "200px");
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

}
