package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.TotalLossFile;
import bigBang.definitions.shared.TotalLossFileStub;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.ExpandableSelectionFormFieldPanel;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.casualtyModule.client.dataAccess.TotalLossFileBroker;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class TotalLossFileSelectionViewPresenter extends ExpandableSelectionFormFieldPanel implements ViewPresenter{
	
	public interface Display{

		void setOperationId(String operationId);

		void registerActionHandler(ActionInvokedEventHandler<Action> handler);

		void allowConfirm(boolean allow);

		HasValue<TotalLossFile> getForm();

		HasValueSelectables<TotalLossFileStub> getList();

		Widget asWidget();
		
	}

	public enum Action{
		CONFIRM, CANCEL
		
	}
	
	private Display view;
	private boolean bound = false;
	private TotalLossFileBroker broker;
	
	public TotalLossFileSelectionViewPresenter(Display view) {
		broker = (TotalLossFileBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.TOTAL_LOSS_FILE);
		setView((UIObject)view);
	}
	
	@Override
	public void setView(UIObject view) {
		this.view = (Display)view;		
		
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());			
	}
	
	private void bind() {
		if(bound){return;}

		view.getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<TotalLossFileStub> selected = (ValueSelectable<TotalLossFileStub>) event.getFirstSelected();
				TotalLossFileStub medFile = selected == null ? null : selected.getValue();
				if(medFile == null) {
					view.getForm().setValue(null);
				}else{
					broker.getTotalLossFile(medFile.id, new ResponseHandler<TotalLossFile>(){

						@Override
						public void onResponse(TotalLossFile response) {
							view.getForm().setValue(response);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							view.getForm().setValue(null);
							view.getList().clearSelection();
						}
					});
				}
			}
		});

		view.getForm().addValueChangeHandler(new ValueChangeHandler<TotalLossFile>() {

			@Override
			public void onValueChange(ValueChangeEvent<TotalLossFile> event) {
				TotalLossFileStub medFile = event.getValue();
				view.allowConfirm(medFile != null);
			}
		});

		view.registerActionHandler(new ActionInvokedEventHandler<Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CONFIRM:
					onConfirm(view.getForm().getValue());
					break;
				case CANCEL:
					onCancel();
					break;
				}
			}
		});

		bound = true;				
	}
	
	protected void onCancel() {
		ValueChangeEvent.fire(this, "CANCELLED_SELECTION");
		
	}


	protected void onConfirm(TotalLossFile value) {
		ValueChangeEvent.fire(this, value == null ? null : value.id);
		
	}


	@Override
	public void setParameters(HasParameters parameters) {
		clearView();		
	}


	private void clearView() {
		view.getList().clearSelection();
		view.getForm().setValue(null);
		view.allowConfirm(false);				
	}


	@Override
	public String getValue() {
		return view.getForm().getValue() != null ? view.getForm().getValue().id : null;
	}

	@Override
	public void setValue(String value) {
		setValue(value, true);
		
	}

	@Override
	public void setValue(String value, final boolean fireEvents) {
		if(value != null){
			this.broker.getTotalLossFile(value, new ResponseHandler<TotalLossFile>() {

				@Override
				public void onResponse(TotalLossFile response) {
					view.getForm().setValue(response);
					if(fireEvents) {
						ValueChangeEvent.fire(TotalLossFileSelectionViewPresenter.this, response.id);
					}
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onResponse(null);
				}
			});
		}
		
		else{
			view.getForm().setValue(null);
			if(fireEvents) {
				ValueChangeEvent.fire(TotalLossFileSelectionViewPresenter.this, null);
			}
		}						
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<String> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public void setListId(String listId) {
		return;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		return;
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}

	public void go() {
		bind();
		this.view.asWidget().setSize("900px", "600px");
		initWidget(this.view.asWidget());
	}
	
}
