package bigBang.module.insurancePolicyModule.client.userInterface.view;


import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.ClientStub;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.clientModule.client.userInterface.ClientSearchPanel;
import bigBang.module.clientModule.client.userInterface.view.ClientFormView;
import bigBang.module.generalSystemModule.client.userInterface.InsurancePolicyTransferToClientToolbar;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicyTransferToClientViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyClientSelectionViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicyTransferToClientViewPresenter.Action;

public class InsurancePolicyTransferToClientView extends View implements InsurancePolicyTransferToClientViewPresenter.Display{

	private ClientSearchPanel list;
	private ClientFormView form;
	private ActionInvokedEventHandler<InsurancePolicyTransferToClientViewPresenter.Action> handler;
	private InsurancePolicyTransferToClientToolbar toolbar;
	
	public InsurancePolicyTransferToClientView(){
		
		VerticalPanel outsidewrapper = new VerticalPanel();
		initWidget(outsidewrapper);
		
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		
		wrapper.setSize("100%", "100%");
		outsidewrapper.setSize("900px", "650px");
		
		list = new ClientSearchPanel();
		list.setOperationId(BigBangConstants.OperationIds.ClientProcess.CREATE_POLICY);
		
		toolbar = new InsurancePolicyTransferToClientToolbar() {
			
			@Override
			public void onTransferToClientRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<InsurancePolicyTransferToClientViewPresenter.Action>(Action.CONFIRM));
			}
			
			@Override
			public void onCancelRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<InsurancePolicyTransferToClientViewPresenter.Action>(Action.CANCEL));
			}
		};
		
		outsidewrapper.add(toolbar);
		toolbar.setSize("100%", "21px");
		
		VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");
		
		ListHeader header = new ListHeader("Cliente");
		
		form = new ClientFormView();
		form.setSize("100%","100%");
		form.setReadOnly(true);
		
		formWrapper.add(header);
		formWrapper.add(form);
		formWrapper.setCellHeight(form, "100%");
		

		
		VerticalPanel listWrapper = new VerticalPanel();
		listWrapper.setSize("100%", "100%");
		listWrapper.add(new ListHeader("Lista de Clientes"));
		listWrapper.add(list);
		listWrapper.setCellHeight(list, "100%");
		
		wrapper.addWest(listWrapper, 300);
		wrapper.add(formWrapper);
		
		outsidewrapper.add(wrapper);
		outsidewrapper.setCellHeight(wrapper, "100%");
		
		list.doSearch();
		
		
	}
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasValueSelectables<ClientStub> getList() {
		return list;
	}

	@Override
	public HasEditableValue<Client> getForm() {
		return form;
	}

	@Override
	public void allowTransferToClient(boolean allow) {
		toolbar.allowTransferToClient(allow);
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.handler = handler;
	}

}
