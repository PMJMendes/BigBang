package bigBang.module.casualtyModule.client.userInterface.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.MedicalFile;
import bigBang.definitions.shared.MedicalFileStub;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.casualtyModule.client.userInterface.MedicalFileSearchPanel;
import bigBang.module.casualtyModule.client.userInterface.form.MedicalFileForm;
import bigBang.module.casualtyModule.client.userInterface.presenter.MedicalFileSelectionViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.presenter.MedicalFileSelectionViewPresenter.Action;

public class MedicalFileSelectionView extends View implements MedicalFileSelectionViewPresenter.Display{

	private MedicalFileSearchPanel list;
	private MedicalFileForm form;
	private ActionInvokedEventHandler<MedicalFileSelectionViewPresenter.Action> handler;
	private Button confirmButton, cancelButton;

	public MedicalFileSelectionView() {
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		list = new MedicalFileSearchPanel();
		
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
		
		ListHeader header = new ListHeader("Ficha Clínica");
		header.setRightWidget(buttonsWrapper);

		form = new MedicalFileForm();
		form.setSize("100%", "100%");
		form.setReadOnly(true);

		formWrapper.add(header);
		formWrapper.add(form);
		formWrapper.setCellHeight(form, "100%");

		VerticalPanel listWrapper = new VerticalPanel();
		listWrapper.setSize("100%", "100%");
		listWrapper.add(new ListHeader("Lista de Fichas Clínicas"));
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
	public HasValueSelectables<MedicalFileStub> getList() {
		return list;
	}


	@Override
	public HasValue<MedicalFile> getForm() {
		return form;
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
	public void setOperationId(String operationId) {
		list.setOperationId(operationId);
		list.doSearch();
		
	}
	
}
