package bigBang.library.client.userInterface.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.ProcessBase;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ExternalInfoRequestChildrenPanel;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.ViewExternalInfoRequestForm;
import bigBang.library.client.userInterface.ViewExternalInfoRequestOperationsToolbar;
import bigBang.library.client.userInterface.presenter.ViewExternalRequestViewPresenter;
import bigBang.library.client.userInterface.presenter.ViewExternalRequestViewPresenter.Action;

public abstract class ViewExternalRequestView<T extends ProcessBase> extends View implements ViewExternalRequestViewPresenter.Display<T> {

	protected FormView<T> ownerForm;
	protected ViewExternalInfoRequestForm form;
	protected ActionInvokedEventHandler<Action> handler;
	protected ViewExternalInfoRequestOperationsToolbar toolbar;
	protected ExternalInfoRequestChildrenPanel childrenPanel;

	public ViewExternalRequestView(FormView<T> ownerForm){
		this.ownerForm = ownerForm;
		
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		VerticalPanel ownerWrapper = new VerticalPanel();
		ownerWrapper.setSize("100%", "100%");
		
		ListHeader ownerFormHeader = new ListHeader("Ficha de Processo");
		ownerFormHeader.setLeftWidget(new Button("Voltar", new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				handler.onActionInvoked(new ActionInvokedEvent<ViewExternalRequestViewPresenter.Action>(Action.ON_BACK_BUTTON));
			}
			
			
			
		}));
		ownerFormHeader.setHeight("30px");
		
		ownerWrapper.add(ownerFormHeader);
		ownerWrapper.add(ownerForm);
		ownerForm.setSize("100%", "100%");
		ownerWrapper.setCellHeight(ownerForm, "100%");
		
		wrapper.addWest(ownerWrapper, 600);
		
		VerticalPanel requestWrapper = new VerticalPanel();
		requestWrapper.setSize("100%", "100%");
		
		ListHeader requestHeader = new ListHeader("Pedido Externo de Informação");
		requestHeader.setHeight("30px");
		
		SplitLayoutPanel requestDataWrapper = new SplitLayoutPanel();
		requestDataWrapper.setSize("100%", "100%");
		requestWrapper.add(requestDataWrapper);
		requestWrapper.setCellHeight(requestDataWrapper, "100%");
		
		childrenPanel = new ExternalInfoRequestChildrenPanel();
		childrenPanel.setSize("100%", "100%");
		requestDataWrapper.addEast(childrenPanel, 250);
		
		VerticalPanel requestFormWrapper = new VerticalPanel();
		requestFormWrapper.setSize("100%", "100%");
		requestDataWrapper.add(requestFormWrapper);
		
		requestFormWrapper.add(requestHeader);
		
		toolbar = new ViewExternalInfoRequestOperationsToolbar() {
			
			@Override
			public void onReply() {
				handler.onActionInvoked(new ActionInvokedEvent<ViewExternalRequestViewPresenter.Action>(Action.REPLY));
			}
			
			@Override
			public void onContinue() {
				handler.onActionInvoked(new ActionInvokedEvent<ViewExternalRequestViewPresenter.Action>(Action.CONTINUE));
			}
			
			@Override
			public void onClose() {
				handler.onActionInvoked(new ActionInvokedEvent<ViewExternalRequestViewPresenter.Action>(Action.CLOSE));
			}
		};
		requestFormWrapper.add(toolbar);
		
		this.form = new ViewExternalInfoRequestForm();
		this.form.setSize("100%", "100%");
		requestFormWrapper.add(this.form);
		requestFormWrapper.setCellHeight(form, "100%");
		
		wrapper.add(requestWrapper);
		
		this.ownerForm.setReadOnly(true);
		this.form.setReadOnly(true);
		
		this.form.addValueChangeHandler(new ValueChangeHandler<ExternalInfoRequest>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<ExternalInfoRequest> event) {
				childrenPanel.setOwner(event.getValue());
			}
		});
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasValue<ExternalInfoRequest> getForm() {
		return this.form;
	}

	@Override
	public HasValue<T> getOwnerForm() {
		return this.ownerForm;
	}

	@Override
	public HasValueSelectables<HistoryItemStub> getHistoryList() {
		return this.childrenPanel.historyList;
	}

	@Override
	public void registerActionHandler(
			ActionInvokedEventHandler<bigBang.library.client.userInterface.presenter.ViewExternalRequestViewPresenter.Action> handler) {
		this.handler = handler;
	}
	
	public void clearAllowedPermissions() {
		toolbar.lockAll();
	}

	public void allowReply(boolean allow) {
		toolbar.allowReply(allow);
	}

	public void allowContinue(boolean allow) {
		toolbar.allowContinue(allow);
	}

	public void allowClose(boolean allow) {
		toolbar.allowClose(allow);
	}

}
