package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.InsuranceSubPolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.SubPolicy;
import bigBang.library.client.HasParameters;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.presenter.ConversationViewPresenter;

public class InsuranceSubPolicyConversationViewPresenter extends ConversationViewPresenter<SubPolicy>{

	private InsuranceSubPolicyBroker broker;
	protected SubPolicy subPolicy;
	
	public InsuranceSubPolicyConversationViewPresenter(
			Display<SubPolicy> view) {
		super(view);
		this.broker = (InsuranceSubPolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
	}

	@Override
	protected void fillOwner(String ownerId, final ResponseHandler<SubPolicy> handler) {
		broker.getSubPolicy(ownerId, new ResponseHandler<SubPolicy>() {
			
			@Override
			public void onResponse(SubPolicy response) {
				subPolicy = response;
				setContacts();
				handler.onResponse(response);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetOwnerFailed();
			}
		});
	}
	
	@Override
	public void setParameters(HasParameters parameterHolder) {
		String subPolicyId = parameterHolder.getParameter("subpolicyid");
		parameterHolder.setParameter("ownerid", subPolicyId);
		parameterHolder.setParameter("ownertypeid", BigBangConstants.EntityIds.CLIENT);
		super.setParameters(parameterHolder);
	}
	
	protected void setContacts() {
		view.addContact("Apólice Adesão (" + subPolicy.number + ")", subPolicy.id, BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
		view.addContact("Apólice (" + subPolicy.mainPolicyNumber + ")", subPolicy.mainPolicyId, BigBangConstants.EntityIds.INSURANCE_POLICY);
		view.addContact("Tomador (" + subPolicy.inheritClientName + ")", subPolicy.inheritClientId, BigBangConstants.EntityIds.CLIENT);
		view.addContact("Subscritor (" + subPolicy.clientName + ")", subPolicy.clientId, BigBangConstants.EntityIds.CLIENT);
	}

}
