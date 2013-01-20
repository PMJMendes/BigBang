package bigBang.library.client.userInterface.view;

import bigBang.definitions.shared.OtherEntity;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.presenter.OtherEntitySelectionViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.OtherEntityList;
import bigBang.module.generalSystemModule.client.userInterface.form.OtherEntityForm;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class OtherEntitySelectionView extends View implements OtherEntitySelectionViewPresenter.Display{

	private OtherEntityList list;
	private OtherEntityForm form;
	private ActionInvokedEventHandler<OtherEntitySelectionViewPresenter.Action> handler;
	private Button confirmButton, cancelButton;
	
	
	public OtherEntitySelectionView() {
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		list = new OtherEntityList();
		
		confirmButton = new Button("Confirmar", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				handler.onActionInvoked(new ActionInvokedEvent<OtherEntitySelectionViewPresenter.Action>(OtherEntitySelectionViewPresenter.Action.CONFIRM));
			}
		});
		cancelButton = new Button("Cancelar", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				handler.onActionInvoked(new ActionInvokedEvent<OtherEntitySelectionViewPresenter.Action>(OtherEntitySelectionViewPresenter.Action.CANCEL));
			}
		});
		
		VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");

		HorizontalPanel buttonsWrapper = new HorizontalPanel();
		buttonsWrapper.setSpacing(5);
		buttonsWrapper.add(confirmButton);
		buttonsWrapper.add(cancelButton);
		
		list.hideNewButton();
		list.hideRefreshButton();
		
		ListHeader header = new ListHeader("Entidade");
		header.setRightWidget(buttonsWrapper);

		form = new OtherEntityForm();
		form.setSize("100%", "100%");
		form.setReadOnly(true);

		formWrapper.add(header);
		formWrapper.add(form);
		formWrapper.setCellHeight(form, "100%");

		VerticalPanel listWrapper = new VerticalPanel();
		listWrapper.setSize("100%", "100%");
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
	public HasValueSelectables<OtherEntity> getList() {
		return list;
	}

	@Override
	public HasValue<OtherEntity> getForm() {
		return form;
	}

	@Override
	public void allowConfirm(boolean allow) {
		this.confirmButton.setEnabled(allow);
		
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<OtherEntitySelectionViewPresenter.Action> handler) {
		this.handler = handler;
		
	}

	@Override
	public void setOperationId(String operationId) {
		return;
	}

}
