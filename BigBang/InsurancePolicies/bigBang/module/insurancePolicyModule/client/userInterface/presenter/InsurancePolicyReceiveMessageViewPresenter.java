package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.presenter.ReceiveMessageViewPresenter;

public class InsurancePolicyReceiveMessageViewPresenter extends
ReceiveMessageViewPresenter<InsurancePolicy> {

	private InsurancePolicyBroker broker;

	public InsurancePolicyReceiveMessageViewPresenter(
			Display<InsurancePolicy> view) {
		super(view);
		broker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
	}

	@Override
	protected void showOwner(String ownerId) {
		broker.getPolicy(ownerId, new ResponseHandler<InsurancePolicy>() {

			@Override
			public void onResponse(InsurancePolicy response) {
				view.getOwnerForm().setValue(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetOwnerFailed();
			}
		});
	}
	
	@Override
	protected void receive() {
		broker.receiveMessage(view.getForm().getInfo(), new ResponseHandler<Conversation>() {

			@Override
			public void onResponse(Conversation response) {
				onReceiveMessageSuccess();
				navigateBack();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onReceiveMessageSuccess();
			}
		});
		
	}
	}
