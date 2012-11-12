package bigBang.library.client.userInterface.view;

import bigBang.definitions.shared.SubCasualty;
import bigBang.definitions.shared.SubCasualtyStub;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.presenter.SubCasualtySelectionViewPresenter;
import bigBang.library.client.userInterface.presenter.SubCasualtySelectionViewPresenter.Action;
import bigBang.module.casualtyModule.client.userInterface.SubCasualtySearchPanel;
import bigBang.module.casualtyModule.client.userInterface.form.SubCasualtyForm;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SubCasualtySelectionView extends View implements SubCasualtySelectionViewPresenter.Display{
	
	private SubCasualtySearchPanel list;
	private SubCasualtyForm form;
	private ActionInvokedEventHandler<SubCasualtySelectionViewPresenter.Action> handler;
	private Button confirmButton, cancelButton;
	
	public SubCasualtySelectionView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		list = new SubCasualtySearchPanel();
		
		confirmButton = new Button("Confirmar", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				handler.onActionInvoked(new ActionInvokedEvent<Action>(Action.CONFIRM));
			}
		});
		cancelButton = new Button("Cancelar", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				handler.onActionInvoked(new ActionInvokedEvent<Action>(Action.CANCEL));
			}
		});

		VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");

		HorizontalPanel buttonsWrapper = new HorizontalPanel();
		buttonsWrapper.setSpacing(5);
		buttonsWrapper.add(confirmButton);
		buttonsWrapper.add(cancelButton);
		
		ListHeader header = new ListHeader("Sub-Sinistro");
		header.setRightWidget(buttonsWrapper);

		form = new SubCasualtyForm();
		form.setSize("100%", "100%");
		form.setReadOnly(true);

		formWrapper.add(header);
		formWrapper.add(form);
		formWrapper.setCellHeight(form, "100%");

		VerticalPanel listWrapper = new VerticalPanel();
		listWrapper.setSize("100%", "100%");
		listWrapper.add(new ListHeader("Lista de Sub-Sinistros"));
		listWrapper.add(list);
		listWrapper.setCellHeight(list, "100%");
		
		wrapper.addWest(listWrapper, 300);
		wrapper.add(formWrapper);
	}

	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasValueSelectables<SubCasualtyStub> getList() {
		return this.list;
	}

	@Override
	public HasEditableValue<SubCasualty> getForm() {
		return this.form;
	}

	@Override
	public void allowConfirm(boolean allow) {
		this.confirmButton.setEnabled(allow);
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.handler = handler;
	}
	
	@Override
	public void setOperationId(String operationId){
		list.setOperationId(operationId);
		list.doSearch();
	}

	@Override
	public void setOwnerId(String ownerId) {
		list.setOwner(ownerId);
	}

}