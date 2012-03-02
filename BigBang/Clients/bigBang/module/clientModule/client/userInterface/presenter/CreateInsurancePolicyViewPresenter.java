package bigBang.module.clientModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicy.TableSection;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class CreateInsurancePolicyViewPresenter implements ViewPresenter {
	
	public enum Action {
		CREATE_POLICY,
		CANCEL_POLICY_CREATION,
		MODALITY_CHANGED
	}
	
	public interface Display {
		HasEditableValue<Client> getClientForm();
		HasEditableValue<InsurancePolicy> getInsurancePolicyForm();
		TableSection getCurrentTableSection();	
		
		boolean isInsurancePolicyFormValid();
		
		public void setActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
		String getInsuredObjectFilter();
		String getExerciseFilter();
	}
	
	protected boolean bound;
	protected Display view;
	protected ClientProcessBroker clientBroker;
	protected InsurancePolicyBroker policyBroker;
	protected Client client;
	
	public CreateInsurancePolicyViewPresenter(Display view){
		setView((UIObject) view);
		clientBroker = ((ClientProcessBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.CLIENT));
		policyBroker = ((InsurancePolicyBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.INSURANCE_POLICY));
	}

	public void setClient(Client client){
		this.client = client;
		this.view.getClientForm().setValue(client);
		
		InsurancePolicy newPolicy = new InsurancePolicy();
		newPolicy.clientId = client.id;
		newPolicy.clientNumber = client.clientNumber;
		newPolicy.clientName = client.name;
		view.getInsurancePolicyForm().setValue(newPolicy);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();		
		container.clear();
		container.add(this.view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		// TODO Auto-generated method stub
		
	}
	
	public void bind() {
		if(bound){return;}
		
		view.setActionHandler(new ActionInvokedEventHandler<CreateInsurancePolicyViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()) {
				
				case CREATE_POLICY:
					InsurancePolicy policy = view.getInsurancePolicyForm().getInfo();
					policyBroker.updatePolicy(policy, new ResponseHandler<InsurancePolicy>() {

						@Override
						public void onResponse(final InsurancePolicy response) {
							String insuredObjectId = view.getInsuredObjectFilter();
							String exerciseId = view.getExerciseFilter();
							TableSection data = view.getCurrentTableSection();
							
							policyBroker.saveCoverageDetailsPage(response.id, insuredObjectId, exerciseId, data, new ResponseHandler<TableSection>() {

								@Override
								public void onResponse(TableSection sectionResponse) {
									policyBroker.commitPolicy(response, new ResponseHandler<InsurancePolicy>() {

										@Override
										public void onResponse(InsurancePolicy response) {
											view.getInsurancePolicyForm().setValue(response);
											view.getInsurancePolicyForm().setReadOnly(true);
										}

										@Override
										public void onError(Collection<ResponseError> errors) {
											//TODO
										}
									});
								}

								@Override
								public void onError(
										Collection<ResponseError> errors) {
									// TODO Auto-generated method stub
								}
							});
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
						}
					});
					break;
				
				case CANCEL_POLICY_CREATION:
					policyBroker.closePolicyResource(view.getInsurancePolicyForm().getValue().id, new ResponseHandler<Void>() {

						@Override
						public void onResponse(Void response) {}

						@Override
						public void onError(Collection<ResponseError> errors) {}
					});
					break;
				
				case MODALITY_CHANGED:
					view.getInsurancePolicyForm().commit();
					InsurancePolicy changedPolicy = view.getInsurancePolicyForm().getValue();
					CreateInsurancePolicyViewPresenter.this.policyBroker.openPolicyResource(null, new ResponseHandler<InsurancePolicy>() { //TODO

						@Override
						public void onResponse(InsurancePolicy response) {
							view.getInsurancePolicyForm().setValue(response, false);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {}
					});
					break;
				}
			}
		});
		bound = true;
	}

}
