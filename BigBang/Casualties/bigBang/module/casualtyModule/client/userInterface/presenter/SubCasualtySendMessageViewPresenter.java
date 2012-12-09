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
import bigBang.library.client.userInterface.presenter.SendMessageViewPresenter;

public class SubCasualtySendMessageViewPresenter extends SendMessageViewPresenter<SubCasualty>{

	private SubCasualtyDataBroker broker;
	protected SubCasualty subCasualty;

	public SubCasualtySendMessageViewPresenter(Display<SubCasualty> view) {
		super(view);
		broker = (SubCasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.SUB_CASUALTY);
	}

	@Override
	protected void fillOwner(String ownerId,
			final ResponseHandler<SubCasualty> handler) {
		broker.getSubCasualty(ownerId, new ResponseHandler<SubCasualty>() {

			@Override
			public void onResponse(SubCasualty response) {
				subCasualty = response;
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
		view.addContact("Sub-Sinistro (" + subCasualty.number + ")", subCasualty.id , BigBangConstants.EntityIds.SUB_CASUALTY);
		view.addContact("Apólice " + (BigBangConstants.EntityIds.INSURANCE_SUB_POLICY.equalsIgnoreCase(subCasualty.referenceTypeId) ? "Adesão " : "") + "(" + subCasualty.referenceNumber + ")", subCasualty.referenceId, subCasualty.referenceTypeId);
		view.addContact("Seguradora (" + subCasualty.inheritInsurerName + ")", subCasualty.inheritInsurerId, BigBangConstants.EntityIds.INSURANCE_AGENCY);
		if (BigBangConstants.EntityIds.INSURANCE_SUB_POLICY.equalsIgnoreCase(subCasualty.referenceTypeId)) {
			view.addContact("Cliente Principal (" + subCasualty.inheritMasterClientName + ")", subCasualty.inheritMasterClientId, BigBangConstants.EntityIds.CLIENT);
			view.addContact("Mediador do Cliente Principal (" + subCasualty.inheritMasterMediatorName + ")", subCasualty.inheritMasterMediatorId, BigBangConstants.EntityIds.MEDIATOR);
		}
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		parameterHolder.setParameter("ownertypeid", BigBangConstants.EntityIds.SUB_CASUALTY);
		super.setParameters(parameterHolder);
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
