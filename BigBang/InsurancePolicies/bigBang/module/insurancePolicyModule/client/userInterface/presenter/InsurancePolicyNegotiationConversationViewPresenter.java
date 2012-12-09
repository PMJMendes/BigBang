package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.NegotiationBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Negotiation;
import bigBang.library.client.HasParameters;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.presenter.ConversationViewPresenter;

public class InsurancePolicyNegotiationConversationViewPresenter extends ConversationViewPresenter<Negotiation>{

	private NegotiationBroker broker;
	protected Negotiation negotiation;
	
	public InsurancePolicyNegotiationConversationViewPresenter(Display<Negotiation> view) {
		super(view);
		broker = (NegotiationBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.NEGOTIATION);
	}

	@Override
	protected void fillOwner(String ownerId,
			final ResponseHandler<Negotiation> handler) {
		broker.getNegotiation(ownerId, new ResponseHandler<Negotiation>() {
			
			@Override
			public void onResponse(Negotiation response) {
				negotiation = response;
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
		String negotiationId = parameterHolder.getParameter("negotiationid");
		parameterHolder.setParameter("ownerid", negotiationId);
		parameterHolder.setParameter("ownertypeid", BigBangConstants.EntityIds.NEGOTIATION);
		super.setParameters(parameterHolder);
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

}
