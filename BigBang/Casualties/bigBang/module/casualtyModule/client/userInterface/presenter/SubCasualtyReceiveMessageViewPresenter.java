package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.SubCasualtyDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.SubCasualty;
import bigBang.library.client.HasParameters;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.presenter.ReceiveMessageViewPresenter;

public class SubCasualtyReceiveMessageViewPresenter extends ReceiveMessageViewPresenter<SubCasualty>{

	private SubCasualtyDataBroker broker;

	public SubCasualtyReceiveMessageViewPresenter(Display<SubCasualty> view) {
		super(view);
		broker = (SubCasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.SUB_CASUALTY);
	}

	@Override
	public void setParameters(final HasParameters parameterHolder) {
		parameterHolder.setParameter("ownertypeid",  BigBangConstants.EntityIds.SUB_CASUALTY);
		super.setParameters(parameterHolder);
	}
	@Override
	protected void showOwner(String ownerId) {
		broker.getSubCasualty(ownerId, new ResponseHandler<SubCasualty>() {

			@Override
			public void onResponse(SubCasualty response) {
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
