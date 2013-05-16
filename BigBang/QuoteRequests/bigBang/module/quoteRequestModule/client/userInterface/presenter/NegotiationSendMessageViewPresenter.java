package bigBang.module.quoteRequestModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.NegotiationBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.Negotiation;
import bigBang.library.client.HasParameters;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.presenter.SendMessageViewPresenter;

public class NegotiationSendMessageViewPresenter extends SendMessageViewPresenter<Negotiation>{

	NegotiationBroker broker;
	protected Negotiation negotiation;

	public NegotiationSendMessageViewPresenter(Display<Negotiation> view) {
		super(view);
		broker = (NegotiationBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.NEGOTIATION);
	}

	@Override
	protected void fillOwner(String ownerId, final ResponseHandler<Negotiation> handler) {
		broker.getNegotiation(ownerId, new ResponseHandler<Negotiation>() {

			@Override
			public void onResponse(Negotiation response) {
				negotiation = response;
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

		view.addContact("Negociação (" + negotiation.companyName + ")",negotiation.id, BigBangConstants.EntityIds.NEGOTIATION);
		view.addContact((negotiation.ownerTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY) ? "Apólice" : "Consulta de Mercado") + " (" + negotiation.ownerLabel + ")",negotiation.ownerId, negotiation.ownerTypeId);
		view.addContact("Seguradora (" + negotiation.companyName + ")", negotiation.companyId, BigBangConstants.EntityIds.INSURANCE_AGENCY);

		if(BigBangConstants.EntityIds.INSURANCE_POLICY.equalsIgnoreCase(negotiation.ownerTypeId)){
			view.addContact("Mediador (" + negotiation.inheritMediatorName + ")", negotiation.inheritMediatorId , BigBangConstants.EntityIds.MEDIATOR);
			view.addContact("Cliente (" + negotiation.inheritClientName + ")", negotiation.inheritClientId, BigBangConstants.EntityIds.CLIENT);
		}
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
	
	@Override
	public void setParameters(HasParameters parameterHolder) {
		parameterHolder.setParameter("ownertypeid", BigBangConstants.EntityIds.NEGOTIATION);
		super.setParameters(parameterHolder);
	}
}
