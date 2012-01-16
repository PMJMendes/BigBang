package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.InsurancePolicyDataBrokerClient;
import bigBang.definitions.client.dataAccess.InsuredObjectDataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsuredObject;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public abstract class InsuredObjectViewPresenter implements ViewPresenter {

	public static interface Display{
		HasEditableValue<InsuredObject> getInsuredObjectForm();
		HasEditableValue<InsurancePolicy> getInsurancePolicyForm();

		void setInsurancePolicy(InsurancePolicy policy);
		void setInsuredObject(InsuredObject object);
		Widget asWidget();
		void registerActionHandler(
				ActionInvokedEventHandler<Action> actionInvokedEventHandler);
	}

	public static enum Action{
		EDIT,
		SAVE,
		DELETE
	}

	protected Display view;
	protected InsuredObjectDataBrokerClient insuredObjectBrokerClient;
	protected InsurancePolicyDataBrokerClient insurancePolicyBrokerClient;
	protected boolean bound = false;
	protected String policyId;
	
	public InsuredObjectViewPresenter(Display display){
		insuredObjectBrokerClient = getInsuredObjectBrokerClient();
		insurancePolicyBrokerClient = getInsurancePolicyBrokerClient();
		setView((UIObject) display);		
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

	public void bind() {
		if(bound){
			return;
		}
		this.view.registerActionHandler(new ActionInvokedEventHandler<Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case SAVE:
					InsuredObject value = view.getInsuredObjectForm().getInfo();
					saveObject(value);
					break;
				case EDIT:
					break;
				case DELETE:
					break;
				}
			}
		});
		
		//APPLICATION-WIDE EVENTS
		
		bound = true;
	}
	
	public void setPolicy(InsurancePolicy policy){
		view.getInsurancePolicyForm().setValue(policy);
		setPolicyId(policy.id);
	}
	
	public void setInsuredObject(InsuredObject object){
		view.getInsuredObjectForm().setValue(object);
	}

	protected InsuredObjectDataBrokerClient getInsuredObjectBrokerClient(){
		return new InsuredObjectDataBrokerClient() {
			protected int version;

			@Override
			public void setDataVersionNumber(String dataElementId, int number) {
				if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.POLICY_INSURED_OBJECT)){
					this.version = number;
				}
			}
			
			@Override
			public int getDataVersion(String dataElementId) {
				if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.POLICY_INSURED_OBJECT)){
					return this.version;
				}
				return -1;
			}
			
			@Override
			public void updateInsuredObject(InsuredObject object) {
				String securedObjectId = view.getInsuredObjectForm().getValue().id;
				if(securedObjectId != null && securedObjectId.equalsIgnoreCase(object.id)){
					view.getInsuredObjectForm().setReadOnly(true);
					view.getInsuredObjectForm().setValue(object);
				}
			}
			
			@Override
			public void removeInsuredObject(String id) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void addInsuredObject(InsuredObject object) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void remapItemId(String newId, String oldId) {
				// TODO Auto-generated method stub
				
			}
		};
	}
	
	protected InsurancePolicyDataBrokerClient getInsurancePolicyBrokerClient(){
		return new InsurancePolicyDataBrokerClient() {
			
			@Override
			public void setDataVersionNumber(String dataElementId, int number) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public int getDataVersion(String dataElementId) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public void updateInsurancePolicy(InsurancePolicy policy) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void removeInsurancePolicy(String policyId) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void addInsurancePolicy(InsurancePolicy policy) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void remapItemId(String oldId, String newId) {
				// TODO Auto-generated method stub
				
			}
		};
	}
	
	protected void saveObject(InsuredObject object) {
		InsurancePolicyBroker broker = ((InsurancePolicyBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.INSURANCE_POLICY));
		broker.updateInsuredObject(this.policyId, object, new ResponseHandler<InsuredObject>() {
			
			@Override
			public void onResponse(InsuredObject response) {
				onSave();
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}
	
	public abstract void onSave();
	public abstract void onCancel();
	public abstract void onDelete();

}
