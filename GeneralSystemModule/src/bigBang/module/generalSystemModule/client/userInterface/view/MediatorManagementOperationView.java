package bigBang.module.generalSystemModule.client.userInterface.view;

import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.MessageBox.ConfirmationCallback;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SplitLayoutPanel;

import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.client.userInterface.MediatorList;
import bigBang.module.generalSystemModule.client.userInterface.MediatorListEntry;
import bigBang.module.generalSystemModule.client.userInterface.presenter.MediatorManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.shared.ComissionProfile;
import bigBang.module.generalSystemModule.shared.Mediator;

public class MediatorManagementOperationView extends View implements MediatorManagementOperationViewPresenter.Display {

	private final int LIST_WIDTH = 400; //px
	
	private MediatorList mediatorList;
	private MediatorForm mediatorForm;
	private MediatorForm newMediatorForm;

	private Button editMediatorButton;
	private Button saveMediatorButton;
	private Button newMediatorButton;
	private Button deleteMediatorButton;
	private Button submitNewMediatorButton;
	
	private PopupPanel newMediatorFormPopupPanel; 
	
	public MediatorManagementOperationView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		wrapper.setSize("100%", "100%");
		
		mediatorList = new MediatorList();
		mediatorForm = new MediatorForm();
		
		wrapper.addWest(mediatorList, LIST_WIDTH);
		wrapper.setWidgetMinSize(mediatorList, LIST_WIDTH);
		wrapper.add(mediatorForm);
		
		editMediatorButton = new Button("Editar");
		saveMediatorButton = new Button("Guardar");
		deleteMediatorButton = new Button("Apagar");
		newMediatorButton = new Button("Novo");

		mediatorForm.addButton(editMediatorButton);
		mediatorForm.addButton(saveMediatorButton);
		mediatorForm.addButton(deleteMediatorButton);
		mediatorForm.addButton(newMediatorButton);

		
		submitNewMediatorButton = new Button("Submeter");
		newMediatorForm = new MediatorForm();
		newMediatorForm.addButton(submitNewMediatorButton);
		
		newMediatorFormPopupPanel = new PopupPanel();
		newMediatorFormPopupPanel.add(newMediatorForm);
		
		initWidget(wrapper);
	}
	
	@Override
	public HasValue<Mediator> getMediatorList() {
		return this.mediatorList;
	}

	@Override
	public HasClickHandlers getEditMediatorButton() {
		return editMediatorButton;
	}

	@Override
	public HasClickHandlers getSaveMediatorButton() {
		return saveMediatorButton;
	}

	@Override
	public void setMediatorFormEditable(boolean editable) {
		this.mediatorForm.setReadOnly(!editable);
	}

	@Override
	public Mediator getMediatorInfo() {
		return (Mediator)mediatorForm.getInfo();
	}

	@Override
	public boolean isMediatorFormValid() {
		return mediatorForm.validate();
	}

	@Override
	public HasClickHandlers getNewMediatorButton() {
		return newMediatorButton;
	}

	@Override
	public HasClickHandlers getSubmitNewMediatorButton() {
		return submitNewMediatorButton;
	}

	@Override
	public void setNewMediatorFormEditable(boolean editable) {
		newMediatorForm.setReadOnly(!editable);
	}

	@Override
	public boolean isNewMediatorFormValid() {
		return this.newMediatorForm.validate();
	}

	@Override
	public Mediator getNewMediatorInfo() {
		return (Mediator)newMediatorForm.getInfo();
	}

	@Override
	public void showNewMediatorForm(boolean show) {
		if(show){
			newMediatorForm.setSize("600px", "450px");
			newMediatorFormPopupPanel.center();
		}else
			newMediatorFormPopupPanel.hidePopup();
	}

	@Override
	public void setMediatorListValues(Mediator[] values) {
		this.mediatorList.clear();
		for(int i = 0; i < values.length; i++){
			this.mediatorList.addListEntry(new MediatorListEntry(values[i]));
		}
	}

	@Override
	public void showDetailsForMediator(Mediator mediator) {
		if(mediator == null)
			this.mediatorForm.clearInfo();
		else
			this.mediatorForm.setInfo(mediator);
	}

	@Override
	public void removeMediatorListValues(Mediator[] values) {
		for(int i = 0; i < values.length; i++) {
			for(ListEntry<Mediator> e : mediatorList.getListEntries()){
				if(values[i].id.equals(e.getValue().id)){
					mediatorList.removeListEntry(e);
					break;
				}
			}
		}
	}

	@Override
	public HasClickHandlers getDeleteMediatorButton() {
		return this.deleteMediatorButton;
	}

	@Override
	public void showConfirmDelete(ConfirmationCallback callback) {
		MessageBox.confirm("Confirmação de remoção", "Pretende realmente remover o mediador", callback);
	}

	@Override
	public void setMediatorComissionProfiles(ComissionProfile[] profiles) {
		this.mediatorForm.setComissionProfiles(profiles);
	}

	@Override
	public void setNewMediatorComissionProfiles(ComissionProfile[] profiles) {
		this.newMediatorForm.setComissionProfiles(profiles);
	}

}
