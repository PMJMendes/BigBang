package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.library.client.ContactManager;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.userInterface.ContactsPreviewList;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.client.userInterface.InsuranceAgencyList;
import bigBang.module.generalSystemModule.client.userInterface.InsuranceAgencyListEntry;
import bigBang.module.generalSystemModule.client.userInterface.presenter.InsuranceAgencyManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.shared.InsuranceAgency;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SplitLayoutPanel;

public class InsuranceAgencyManagementOperationView extends View implements InsuranceAgencyManagementOperationViewPresenter.Display {
	
	private static final int LIST_WIDTH = 400; //px

	private InsuranceAgencyList insuranceAgencyList;
	private InsuranceAgencyForm insuranceAgencyForm;
	private ContactsPreviewList contactsPreviewList;
	
	public InsuranceAgencyManagementOperationView() {
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		wrapper.setSize("100%", "100%");

		insuranceAgencyList = new InsuranceAgencyList();
		insuranceAgencyList.setSize("100%", "100%");
		wrapper.addWest(insuranceAgencyList, LIST_WIDTH);
		wrapper.setWidgetMinSize(insuranceAgencyList, LIST_WIDTH);

		insuranceAgencyForm = new InsuranceAgencyForm();
		
		SplitLayoutPanel formWrapper = new SplitLayoutPanel();
		
		contactsPreviewList = new ContactsPreviewList();
		contactsPreviewList.setSize("100%", "100%");
		
		formWrapper.addEast(contactsPreviewList, 250);
		formWrapper.add(insuranceAgencyForm);
		
		wrapper.add(formWrapper);

		initWidget(wrapper);
	}

	@Override
	public HasValueSelectables<InsuranceAgency> getList() {
		return (HasValueSelectables<InsuranceAgency>)this.insuranceAgencyList;
	}
	
	@Override
	public void clearList(){
		this.insuranceAgencyList.clear();
	}
	
	@Override
	public void addValuesToList(InsuranceAgency[] result) {
		for(int i = 0; i < result.length; i++)
			this.insuranceAgencyList.add(new InsuranceAgencyListEntry(result[i]));
	}

	@Override
	public void removeInsuranceAgencyFromList(InsuranceAgency c) {
		for(ListEntry<InsuranceAgency> e : this.insuranceAgencyList){
			if(e.getValue() == c || e.getValue().id.equals(c.id)){
				this.insuranceAgencyList.remove(e);
				break;
			}
		}
	}
	
	@Override
	public HasEditableValue<InsuranceAgency> getForm() {
		return this.insuranceAgencyForm;
	}

	@Override
	public void prepareNewInsuranceAgency() {
		for(ValueSelectable<InsuranceAgency> s : this.insuranceAgencyList){
			if(s.getValue().id == null){
				s.setSelected(true, true);
				return;
			}
		}
		InsuranceAgencyListEntry entry = new InsuranceAgencyListEntry(new InsuranceAgency());
		this.insuranceAgencyList.add(entry);
		this.insuranceAgencyList.getScrollable().scrollToBottom();
		entry.setSelected(true, true);
	}
	
	@Override
	public void removeNewInsuranceAgencyPreparation(){
		for(ValueSelectable<InsuranceAgency> s : this.insuranceAgencyList){
			if(s.getValue().id == null){
				this.removeInsuranceAgencyFromList(s.getValue());
				break;
			}
		}
	}
	
	@Override
	public HasClickHandlers getNewButton() {
		return this.insuranceAgencyList.newButton;
	}

	@Override
	public HasClickHandlers getRefreshButton() {
		return this.insuranceAgencyList.refreshButton;
	}
	
	@Override
	public HasClickHandlers getSaveButton() {
		return this.insuranceAgencyForm.getSaveButton();
	}

	@Override
	public HasClickHandlers getEditButton() {
		return this.insuranceAgencyForm.getEditButton();
	}
	
	@Override
	public HasClickHandlers getDeleteButton() {
		return this.insuranceAgencyForm.getDeleteButton();
	}

	@Override
	public boolean isFormValid() {
		return this.insuranceAgencyForm.validate();
	}

	@Override
	public void lockForm(boolean lock) {
		this.insuranceAgencyForm.lock(lock);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		((Button)this.insuranceAgencyList.newButton).setEnabled(!readOnly);
		this.insuranceAgencyForm.setReadOnly(readOnly);
	}
	
	@Override
	public void clear(){
		this.contactsPreviewList.clear();
		this.insuranceAgencyForm.clearInfo();
		this.insuranceAgencyList.clear();
		this.insuranceAgencyList.clearFilters();
	}

	@Override
	public void setContactManager(ContactManager contactManager) {
		this.contactsPreviewList.setManager(contactManager);
	}

}
