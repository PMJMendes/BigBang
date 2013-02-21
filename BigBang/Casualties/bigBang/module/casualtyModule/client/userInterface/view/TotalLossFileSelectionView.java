package bigBang.module.casualtyModule.client.userInterface.view;

import bigBang.definitions.shared.TotalLossFile;
import bigBang.definitions.shared.TotalLossFileStub;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.casualtyModule.client.userInterface.TotalLossFileSearchPanel;
import bigBang.module.casualtyModule.client.userInterface.form.TotalLossFileForm;
import bigBang.module.casualtyModule.client.userInterface.presenter.TotalLossFileSelectionViewPresenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TotalLossFileSelectionView extends View implements TotalLossFileSelectionViewPresenter.Display{

	private TotalLossFileSearchPanel list;
	private TotalLossFileForm form;
	private ActionInvokedEventHandler<TotalLossFileSelectionViewPresenter.Action> handler;
	private Button confirmButton, cancelButton;

	public TotalLossFileSelectionView() {
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		list = new TotalLossFileSearchPanel();

		confirmButton = new Button("Confirmar", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				handler.onActionInvoked(new ActionInvokedEvent<TotalLossFileSelectionViewPresenter.Action>(TotalLossFileSelectionViewPresenter.Action.CONFIRM));
			}
		});
		cancelButton = new Button("Cancelar", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				handler.onActionInvoked(new ActionInvokedEvent<TotalLossFileSelectionViewPresenter.Action>(TotalLossFileSelectionViewPresenter.Action.CANCEL));
			}
		});

		VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");

		HorizontalPanel buttonsWrapper = new HorizontalPanel();
		buttonsWrapper.setSpacing(5);
		buttonsWrapper.add(confirmButton);
		buttonsWrapper.add(cancelButton);

		ListHeader header = new ListHeader("Perda Total");
		header.setRightWidget(buttonsWrapper);

		form = new TotalLossFileForm();
		form.setSize("100%", "100%");
		form.setReadOnly(true);

		formWrapper.add(header);
		formWrapper.add(form);
		formWrapper.setCellHeight(form, "100%");

		VerticalPanel listWrapper = new VerticalPanel();
		listWrapper.setSize("100%", "100%");
		listWrapper.add(new ListHeader("Lista de Perdas Totais"));
		listWrapper.add(list);
		listWrapper.setCellHeight(list, "100%");

		wrapper.addWest(listWrapper, 300);
		wrapper.add(formWrapper);			}
	@Override
	protected void initializeView() {
		return;		
	}


	@Override
	public HasValueSelectables<TotalLossFileStub> getList() {
		return list;
	}


	@Override
	public HasValue<TotalLossFile> getForm() {
		return form;
	}


	@Override
	public void allowConfirm(boolean allow) {
		this.confirmButton.setEnabled(allow);

	}


	@Override
	public void registerActionHandler(ActionInvokedEventHandler<TotalLossFileSelectionViewPresenter.Action> handler) {
		this.handler = handler;

	}


	@Override
	public void setOperationId(String operationId) {
		list.setOperationId(operationId);
		list.doSearch();

	}

}
