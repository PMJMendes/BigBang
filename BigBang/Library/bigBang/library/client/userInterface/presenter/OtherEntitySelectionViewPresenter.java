package bigBang.library.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.OtherEntityBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.OtherEntity;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.ExpandableSelectionFormFieldPanel;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;


public class OtherEntitySelectionViewPresenter extends ExpandableSelectionFormFieldPanel implements ViewPresenter{

	public static enum Action{
		CONFIRM,
		CANCEL
	}
	
	public static interface Display{
		HasValueSelectables<OtherEntity> getList();
		HasValue<OtherEntity> getForm();
		
		void allowConfirm(boolean allow);
		
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		
		Widget asWidget();
		void setOperationId(String operationId);
	}
	
	private Display view;
	private boolean bound = false;
	private OtherEntityBroker broker;
	
	public OtherEntitySelectionViewPresenter(Display view){
		broker = (OtherEntityBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.OTHER_ENTITY);
		setView((UIObject)view);
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
			broker.getOtherEntity(value, new ResponseHandler<OtherEntity>() {
				
				@Override
				public void onResponse(OtherEntity response) {
					view.getForm().setValue(response);
					if(fireEvents){
						ValueChangeEvent.fire(OtherEntitySelectionViewPresenter.this, response.id);
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
			if(fireEvents){
				ValueChangeEvent.fire(OtherEntitySelectionViewPresenter.this, null);
			}
		}
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<String> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
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
				ValueSelectable<OtherEntity> selected = (ValueSelectable<OtherEntity>) event.getFirstSelected();
				OtherEntity ent = selected == null ? null : selected.getValue();
				
				view.getForm().setValue(ent);
			}
		});
		
		view.getForm().addValueChangeHandler(new ValueChangeHandler<OtherEntity>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<OtherEntity> event) {
				OtherEntity ent = event.getValue();
				view.allowConfirm(ent != null);
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

	protected void onConfirm(OtherEntity value) {
		ValueChangeEvent.fire(this, value == null ? null : value.id);
		
	}

	@Override
	public void setListId(String listId) {
return;		
	}

	private void clearView() {
		view.getList().clearSelection();
		view.getForm().setValue(null);
		view.allowConfirm(false);				
		
	}

	protected void onGetListFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível obter a lista de entidades"), TYPE.ALERT_NOTIFICATION));
		
	}

	@Override
	public void setParameters(HasParameters parameters) {
		clearView();		
		
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}
	
	public void go(){
		bind();
		this.view.asWidget().setSize("900px", "600px");
		initWidget(this.view.asWidget());
	}
	


}
