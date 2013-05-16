package bigBang.library.client.userInterface.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.CasualtyStub;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.presenter.CasualtySelectionViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.CasualtySearchPanel;
import bigBang.module.casualtyModule.client.userInterface.form.CasualtyForm;

public class CasualtySelectionView extends View implements CasualtySelectionViewPresenter.Display{

	private CasualtySearchPanel list;
	private CasualtyForm form;
	private ActionInvokedEventHandler<CasualtySelectionViewPresenter.Action> handler;
	private Button confirmButton, cancelButton;

	public CasualtySelectionView() {
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		list = new CasualtySearchPanel();

		confirmButton = new Button("Confirmar", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				handler.onActionInvoked(new ActionInvokedEvent<CasualtySelectionViewPresenter.Action>(CasualtySelectionViewPresenter.Action.CONFIRM));
			}
		});
		cancelButton = new Button("Cancelar", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				handler.onActionInvoked(new ActionInvokedEvent<CasualtySelectionViewPresenter.Action>(CasualtySelectionViewPresenter.Action.CANCEL));
			}
		});

		VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");

		HorizontalPanel buttonsWrapper = new HorizontalPanel();
		buttonsWrapper.setSpacing(5);
		buttonsWrapper.add(confirmButton);
		buttonsWrapper.add(cancelButton);

		ListHeader header = new ListHeader("Sinistro");
		header.setRightWidget(buttonsWrapper);

		form = new CasualtyForm();
		form.setSize("100%", "100%");
		form.setReadOnly(true);

		formWrapper.add(header);
		formWrapper.add(form);
		formWrapper.setCellHeight(form, "100%");

		VerticalPanel listWrapper = new VerticalPanel();
		listWrapper.setSize("100%", "100%");
		listWrapper.add(new ListHeader("Lista de Sinistros"));
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
	public HasValueSelectables<CasualtyStub> getList() {
		return this.list;
	}

	@Override
	public HasEditableValue<Casualty> getForm() {
		return this.form;
	}

	@Override
	public void allowConfirm(boolean allow) {
		this.confirmButton.setEnabled(allow);
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<CasualtySelectionViewPresenter.Action> handler) {
		this.handler = handler;
	}

	@Override
	public void setOperationId(String operationId){
		list.setOperationId(operationId);
		list.doSearch();
	}
}