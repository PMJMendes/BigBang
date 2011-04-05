package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.client.userInterface.MediatorList;
import bigBang.module.generalSystemModule.client.userInterface.MediatorListEntry;
import bigBang.module.generalSystemModule.client.userInterface.presenter.MediatorManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.shared.CommissionProfile;
import bigBang.module.generalSystemModule.shared.Mediator;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MediatorManagementOperationView extends View implements MediatorManagementOperationViewPresenter.Display {

	private static final int LIST_WIDTH = 400; //px

	private MediatorList mediatorList;
	private MediatorForm mediatorForm;
	
	public MediatorManagementOperationView() {
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		wrapper.setSize("100%", "100%");

		mediatorList = new MediatorList();
		mediatorList.setSize("100%", "100%");
		wrapper.addWest(mediatorList, LIST_WIDTH);
		wrapper.setWidgetMinSize(mediatorList, LIST_WIDTH);

		final VerticalPanel previewWrapper = new VerticalPanel();
		previewWrapper.setSize("100%", "100%");
		
		mediatorForm = new MediatorForm();

		previewWrapper.add(mediatorForm);
		
		wrapper.add(previewWrapper);

		initWidget(wrapper);
	}

	@Override
	public HasValueSelectables<Mediator> getList() {
		return (HasValueSelectables<Mediator>)this.mediatorList;
	}
	
	@Override
	public void clearList(){
		this.mediatorList.clear();
	}
	
	@Override
	public void addValuesToList(Mediator[] result) {
		for(int i = 0; i < result.length; i++)
			this.mediatorList.add(new MediatorListEntry(result[i]));
	}

	@Override
	public void removeMediatorFromList(Mediator c) {
		for(ListEntry<Mediator> e : this.mediatorList){
			if(e.getValue() == c || e.getValue().id.equals(c.id)){
				this.mediatorList.remove(e);
				break;
			}
		}
	}
	
	@Override
	public HasEditableValue<Mediator> getForm() {
		return this.mediatorForm;
	}

	@Override
	public void prepareNewMediator() {
		for(ValueSelectable<Mediator> s : this.mediatorList){
			if(s.getValue().id == null){
				s.setSelected(true, true);
				return;
			}
		}
		MediatorListEntry entry = new MediatorListEntry(new Mediator());
		this.mediatorList.add(entry);
		this.mediatorList.getScrollable().scrollToBottom();
		entry.setSelected(true, true);
	}
	
	@Override
	public void removeNewMediatorPreparation(){
		for(ValueSelectable<Mediator> s : this.mediatorList){
			if(s.getValue().id == null){
				this.removeMediatorFromList(s.getValue());
				break;
			}
		}
	}

	@Override
	public void setCommissionProfiles(CommissionProfile[] profiles) {
		this.mediatorForm.setComissionProfiles(profiles);
	}
	
	@Override
	public HasClickHandlers getNewButton() {
		return this.mediatorList.newButton;
	}

	@Override
	public HasClickHandlers getRefreshButton() {
		return this.mediatorList.refreshButton;
	}
	
	@Override
	public HasClickHandlers getSaveButton() {
		return this.mediatorForm.getSaveButton();
	}

	@Override
	public HasClickHandlers getEditButton() {
		return this.mediatorForm.getEditButton();
	}
	
	@Override
	public HasClickHandlers getDeleteButton() {
		return this.mediatorForm.getDeleteButton();
	}

	@Override
	public boolean isFormValid() {
		return this.mediatorForm.validate();
	}

	@Override
	public void lockForm(boolean lock) {
		this.mediatorForm.lock(lock);
	}

}
