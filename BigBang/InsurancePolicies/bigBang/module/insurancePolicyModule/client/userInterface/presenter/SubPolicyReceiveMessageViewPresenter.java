package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.InsuranceSubPolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.SubPolicy;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.presenter.ReceiveMessageViewPresenter;

public class SubPolicyReceiveMessageViewPresenter extends ReceiveMessageViewPresenter<SubPolicy>{

	private InsuranceSubPolicyBroker broker;
	
	public SubPolicyReceiveMessageViewPresenter(Display<SubPolicy> view) {
		super(view);
		broker = (InsuranceSubPolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
	}

	@Override
	protected void showOwner(String ownerId) {
		broker.getSubPolicy(ownerId, new ResponseHandler<SubPolicy>() {
			
			@Override
			public void onResponse(SubPolicy response) {
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
				onReceiveMessageFailed();
			}
		});				
	}
}
