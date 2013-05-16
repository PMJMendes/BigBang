package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.client.HasParameters;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.presenter.ConversationViewPresenter;

public class InsurancePolicyConversationViewPresenter extends ConversationViewPresenter<InsurancePolicy>{

	private InsurancePolicyBroker broker;
	protected InsurancePolicy insurancePolicy;
	
	public InsurancePolicyConversationViewPresenter(
			bigBang.library.client.userInterface.presenter.ConversationViewPresenter.Display<InsurancePolicy> view) {
		super(view);
		this.broker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
	}

	@Override
	protected void fillOwner(String ownerId,
			final ResponseHandler<InsurancePolicy> handler) {
		broker.getPolicy(ownerId, new ResponseHandler<InsurancePolicy>() {

			@Override
			public void onResponse(InsurancePolicy response) {
				insurancePolicy = response;
				setContacts();
				handler.onResponse(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetOwnerFailed();
			}
		});

	}

	protected void setContacts() {
		view.addContact("Apólice (" + insurancePolicy.number + ")", insurancePolicy.id, BigBangConstants.EntityIds.INSURANCE_POLICY);
		view.addContact("Seguradora (" + insurancePolicy.insuranceAgencyName + ")", insurancePolicy.insuranceAgencyId, BigBangConstants.EntityIds.INSURANCE_AGENCY);
		view.addContact("Mediador (" + insurancePolicy.inheritMediatorName + ")", insurancePolicy.inheritMediatorId, BigBangConstants.EntityIds.MEDIATOR);
		view.addContact("Cliente (" + insurancePolicy.clientName + ")", insurancePolicy.clientId, BigBangConstants.EntityIds.CLIENT);
	}
	
	@Override
	public void setParameters(HasParameters parameterHolder) {
		String policyId = parameterHolder.getParameter("policyid");
		parameterHolder.setParameter("ownerid", policyId);
		parameterHolder.setParameter("ownertypeid", BigBangConstants.EntityIds.INSURANCE_POLICY);
		super.setParameters(parameterHolder);
	}
	

}
