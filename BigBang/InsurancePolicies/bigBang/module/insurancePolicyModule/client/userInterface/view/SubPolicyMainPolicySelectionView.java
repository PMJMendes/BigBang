package bigBang.module.insurancePolicyModule.client.userInterface.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyForm;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicySearchPanel;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyMainPolicySelectionViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyMainPolicySelectionViewPresenter.Action;

public class SubPolicyMainPolicySelectionView extends View implements SubPolicyMainPolicySelectionViewPresenter.Display {

	private InsurancePolicySearchPanel list;
	private InsurancePolicyForm form;
	private ActionInvokedEventHandler<SubPolicyMainPolicySelectionViewPresenter.Action> handler;
	private Button confirmButton, cancelButton;

	public SubPolicyMainPolicySelectionView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		list = new InsurancePolicySearchPanel();
		list.setOperationId(BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_SUB_POLICY);

		confirmButton = new Button("Confirmar", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				handler.onActionInvoked(new ActionInvokedEvent<SubPolicyMainPolicySelectionViewPresenter.Action>(Action.CONFIRM));
			}
		});
		cancelButton = new Button("Cancelar", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				handler.onActionInvoked(new ActionInvokedEvent<SubPolicyMainPolicySelectionViewPresenter.Action>(Action.CANCEL));
			}
		});

		VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");

		HorizontalPanel buttonsWrapper = new HorizontalPanel();
		buttonsWrapper.setSpacing(5);
		buttonsWrapper.add(confirmButton);
		buttonsWrapper.add(cancelButton);
		
		ListHeader header = new ListHeader("Apólice Principal");
		header.setRightWidget(buttonsWrapper);

		form = new InsurancePolicyForm() {
			
			@Override
			public void onSubLineChanged(String subLineId) {
				return;
			}
		};
		form.setSize("100%", "100%");
		form.setReadOnly(true);

		formWrapper.add(header);
		formWrapper.add(form);
		formWrapper.setCellHeight(form, "100%");

		VerticalPanel listWrapper = new VerticalPanel();
		listWrapper.setSize("100%", "100%");
		listWrapper.add(new ListHeader("Lista de Apólices"));
		listWrapper.add(list);
		listWrapper.setCellHeight(list, "100%");
		
		wrapper.addWest(listWrapper, 300);
		wrapper.add(formWrapper);
		
		list.doSearch();
	}

	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasValueSelectables<InsurancePolicyStub> getList() {
		return this.list;
	}

	@Override
	public HasEditableValue<InsurancePolicy> getForm() {
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

}
