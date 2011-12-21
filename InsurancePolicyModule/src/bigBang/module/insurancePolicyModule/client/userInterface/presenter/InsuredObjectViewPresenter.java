package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.InsurancePolicyDataBrokerClient;
import bigBang.definitions.client.dataAccess.InsuredObjectDataBrokerClient;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsuredObject;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;

public abstract class InsuredObjectViewPresenter implements ViewPresenter {

	public static interface Display{
		HasEditableValue<InsuredObject> getInsuredObjectForm();
		HasEditableValue<InsurancePolicy> getInsurancePolicyForm();

		void setInsurancePolicy(InsurancePolicy policy);
		void setInsuredObject(InsuredObject object);
		Widget asWidget();
	}

	public static enum Action{
		EDIT,
		SAVE,
		DELETE
	}

	protected EventBus eventBus;
	protected Display view;
	protected InsuredObjectDataBrokerClient insuredObjectBrokerClient;
	protected InsurancePolicyDataBrokerClient insurancePolicyBrokerClient;
	protected boolean bound = false;
	
	public InsuredObjectViewPresenter(EventBus eventBus, Display display){
		setEventBus(eventBus);
		setView((View) display);
		
		insuredObjectBrokerClient = getInsuredObjectBrokerClient();
		insurancePolicyBrokerClient = getInsurancePolicyBrokerClient();
	}

	@Override
	public void setService(Service service) {
		return;
	}

	@Override
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Override
	public void setView(View view) {
		this.view = (Display)view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	@Override
	public void bind() {
		if(bound){
			return;
		}
		bound = true;
	}
	
	public void setPolicy(InsurancePolicy policy){
		view.getInsurancePolicyForm().setValue(policy);
	}
	
	public void setInsuredObject(InsuredObject object){
		view.getInsuredObjectForm().setValue(object);
	}

	@Override
	public void registerEventHandlers(EventBus eventBus) {
		return;
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
		};
	}
	
	public abstract void onSave();
	public abstract void onCancel();
	public abstract void onDelete();

}
