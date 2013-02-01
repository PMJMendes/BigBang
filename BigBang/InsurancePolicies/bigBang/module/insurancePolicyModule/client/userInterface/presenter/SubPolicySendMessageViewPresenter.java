package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.InsuranceSubPolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.SubPolicy;
import bigBang.library.client.HasParameters;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.presenter.SendMessageViewPresenter;

public class SubPolicySendMessageViewPresenter extends
SendMessageViewPresenter<SubPolicy> {

	protected InsuranceSubPolicyBroker broker;
	protected SubPolicy subPolicy;

	public SubPolicySendMessageViewPresenter(
			bigBang.library.client.userInterface.presenter.SendMessageViewPresenter.Display<SubPolicy> view) {
		super(view);
		broker = (InsuranceSubPolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		parameterHolder.setParameter("ownertypeid", BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
		super.setParameters(parameterHolder);
	}

	@Override
	protected void fillOwner(String ownerId,
			final ResponseHandler<SubPolicy> handler) {
		broker.getSubPolicy(ownerId, new ResponseHandler<SubPolicy>() {

			@Override
			public void onResponse(SubPolicy response) {
				subPolicy = response;
				setContacts();
				handler.onResponse(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetContactsFailed();
			}
		});

	}

	protected void setContacts() {
		view.addContact("Apólice Adesão (" + subPolicy.number + ")", subPolicy.id, BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
		view.addContact("Apólice (" + subPolicy.mainPolicyNumber + ")", subPolicy.mainPolicyId, BigBangConstants.EntityIds.INSURANCE_POLICY);
		view.addContact("Tomador (" + subPolicy.inheritClientName + ")", subPolicy.inheritClientId, BigBangConstants.EntityIds.CLIENT);
		view.addContact("Subscritor (" + subPolicy.clientName + ")", subPolicy.clientId, BigBangConstants.EntityIds.CLIENT);
	}

	@Override
	protected void send() {
		broker.sendMessage(view.getForm().getInfo(), new ResponseHandler<Conversation>() {

			@Override
			public void onResponse(Conversation response) {
				onSendRequestSuccess();
				navigateBack();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onSendRequestFailed();
			}
		});		
	}
}
