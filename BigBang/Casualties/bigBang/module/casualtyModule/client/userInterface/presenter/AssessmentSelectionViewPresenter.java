package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.AssessmentBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Assessment;
import bigBang.definitions.shared.AssessmentStub;
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

public class AssessmentSelectionViewPresenter extends ExpandableSelectionFormFieldPanel implements ViewPresenter{

	public static enum Action{
		CONFIRM,
		CANCEL
	}
	
	public static interface Display{
		HasValueSelectables<AssessmentStub> getList();
		HasValue<Assessment> getForm();
		
		void allowConfirm(boolean allow);
		
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
		void setOperationId(String operationId);
	}

	private Display view;
	private boolean bound = false;
	private AssessmentBroker broker;
	
	public AssessmentSelectionViewPresenter(Display view){
		broker = (AssessmentBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.ASSESSMENT);
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
			this.broker.getAssessment(value, new ResponseHandler<Assessment>() {

				@Override
				public void onResponse(Assessment response) {
					view.getForm().setValue(response);
					if(fireEvents) {
						ValueChangeEvent.fire(AssessmentSelectionViewPresenter.this, response.id);
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
				ValueChangeEvent.fire(AssessmentSelectionViewPresenter.this, null);
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
				ValueSelectable<AssessmentStub> selected = (ValueSelectable<AssessmentStub>) event.getFirstSelected();
				AssessmentStub assessment = selected == null ? null : selected.getValue();
				if(assessment == null) {
					view.getForm().setValue(null);
				}else{
					broker.getAssessment(assessment.id, new ResponseHandler<Assessment>(){

						@Override
						public void onResponse(Assessment response) {
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

		view.getForm().addValueChangeHandler(new ValueChangeHandler<Assessment>() {

			@Override
			public void onValueChange(ValueChangeEvent<Assessment> event) {
				Assessment assessment = event.getValue();
				view.allowConfirm(assessment != null);
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

	protected void onConfirm(Assessment value) {
		ValueChangeEvent.fire(this, value == null ? null : value.id);
		
	}

	@Override
	public void setListId(String listId) {
		return;
		
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
	
	public void setOperationId(String operationId) {
		view.setOperationId(operationId);		
	}
	
	private void clearView() {
		view.getList().clearSelection();
		view.getForm().setValue(null);
		view.allowConfirm(false);		
	}
	
	public void go() {
		bind();
		this.view.asWidget().setSize("900px", "600px");
		initWidget(this.view.asWidget());
	}
	
}
