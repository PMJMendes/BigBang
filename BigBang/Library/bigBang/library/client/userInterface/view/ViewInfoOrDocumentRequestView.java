package bigBang.library.client.userInterface.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.ProcessBase;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.InfoOrDocumentRequestChildrenPanel;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.ViewInfoOrDocumentRequestOperationsToolbar;
import bigBang.library.client.userInterface.presenter.ViewInfoOrDocumentRequestViewPresenter;
import bigBang.library.client.userInterface.presenter.ViewInfoOrDocumentRequestViewPresenter.Action;
import bigBang.module.clientModule.client.userInterface.presenter.ViewClientInfoRequestViewPresenter;

public abstract class ViewInfoOrDocumentRequestView<T extends ProcessBase> extends View implements ViewClientInfoRequestViewPresenter.Display<T>  {

	private ActionInvokedEventHandler<Action> handler;
	private FormView<T> ownerForm;
	private ViewInfoOrDocumentRequestForm form;
	private InfoOrDocumentRequestChildrenPanel childrenPanel;
	private ViewInfoOrDocumentRequestOperationsToolbar toolbar;
	
	public ViewInfoOrDocumentRequestView(FormView<T> ownerForm){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		VerticalPanel ownerFormWrapper = new VerticalPanel();
		ownerFormWrapper.setSize("100%", "100%");
				
		ListHeader ownerFormHeader = new ListHeader("Ficha do Processo");
		ownerFormHeader.setLeftWidget(new Button("Voltar", new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				handler.onActionInvoked(new ActionInvokedEvent<ViewInfoOrDocumentRequestViewPresenter.Action>(Action.ON_CLICK_BACK));
			}
		}));
		ownerFormHeader.setHeight("30px");
		ownerFormWrapper.add(ownerFormHeader);
		
		this.ownerForm = ownerForm;
		ownerForm.setReadOnly(true);
		ownerForm.setSize("100%", "100%");
		ownerFormWrapper.add(ownerForm);
		ownerFormWrapper.setCellHeight(ownerForm, "100%");
		
		wrapper.addWest(ownerFormWrapper, 600);
		
		VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");
		
		ListHeader formHeader = new ListHeader("Pedido de Informação ou Documento");
		formHeader.setHeight("30px");
		formWrapper.add(formHeader);

		toolbar = new ViewInfoOrDocumentRequestOperationsToolbar() {
			
			@Override
			public void onRepeat() {
				handler.onActionInvoked(new ActionInvokedEvent<ViewClientInfoRequestViewPresenter.Action>(Action.REPEAT_REQUEST));
			}
			
			@Override
			public void onReceiveResponse() {
				handler.onActionInvoked(new ActionInvokedEvent<ViewClientInfoRequestViewPresenter.Action>(Action.RECEIVE_RESPONSE));
			}
			
			@Override
			public void onCancel() {
				handler.onActionInvoked(new ActionInvokedEvent<ViewClientInfoRequestViewPresenter.Action>(Action.CANCEL_REQUEST));
			}
		};
		formWrapper.add(toolbar);
		
		form = new ViewInfoOrDocumentRequestForm();
		form.setReadOnly(true);
		form.setSize("100%", "100%");
		formWrapper.add(form);
		formWrapper.setCellHeight(form, "100%");
		
		SplitLayoutPanel requestWrapper = new SplitLayoutPanel();
		requestWrapper.setSize("100%", "100%");

		this.childrenPanel = new InfoOrDocumentRequestChildrenPanel();
		this.childrenPanel.setSize("100%", "100%");
		requestWrapper.addEast(this.childrenPanel, 250);
		
		requestWrapper.add(formWrapper);
		wrapper.add(requestWrapper);
		
		form.addValueChangeHandler(new ValueChangeHandler<InfoOrDocumentRequest>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<InfoOrDocumentRequest> event) {
				childrenPanel.setOwner(event.getValue());
			}
		});
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasValue<InfoOrDocumentRequest> getForm() {
		return this.form;
	}

	@Override
	public HasValue<T> getParentForm() {
		return this.ownerForm;
	}

	@Override
	public HasValueSelectables<HistoryItemStub> getHistoryList() {
		return childrenPanel.historyList;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.handler = handler;
	}

	@Override
	public void clearAllowedPermissions() {
		this.toolbar.lockAll();
	}

	@Override
	public void allowRepeat(boolean allow) {
		this.toolbar.allowRepeat(allow);
	}

	@Override
	public void allowReceiveReply(boolean allow) {
		this.toolbar.allowReceiveResponse(allow);
	}

	@Override
	public void allowCancelRequest(boolean allow) {
		this.toolbar.allowCancel(allow);
	}

}
