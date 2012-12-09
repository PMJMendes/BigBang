package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.client.HasParameters;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.presenter.SendMessageViewPresenter;

public class InsurancePolicySendMessageViewPresenter extends SendMessageViewPresenter<InsurancePolicy>{

	private InsurancePolicyBroker broker;
	protected InsurancePolicy insurancePolicy;

	public InsurancePolicySendMessageViewPresenter(Display<InsurancePolicy> view) {
		super(view);
		broker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
	}

	@Override
	public void setParameters(HasParameters parameterHolder){
		parameterHolder.setParameter("ownertypeid", BigBangConstants.EntityIds.INSURANCE_POLICY);
		super.setParameters(parameterHolder);
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
				onGetContactsFailed();
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
