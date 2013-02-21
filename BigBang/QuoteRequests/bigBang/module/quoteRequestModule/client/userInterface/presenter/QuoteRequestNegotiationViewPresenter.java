package bigBang.module.quoteRequestModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.QuoteRequestBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Negotiation;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.definitions.shared.QuoteRequest;


public class QuoteRequestNegotiationViewPresenter extends NegotiationViewPresenter{

	private QuoteRequestBroker quoteRequestBroker;

	public QuoteRequestNegotiationViewPresenter(Display view) {
		super(view);
		quoteRequestBroker = (QuoteRequestBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.QUOTE_REQUEST);
	}

	@Override
	protected void createNegotiation(Negotiation negotiation) {
		negotiation.ownerTypeId = ownerTypeId;
		quoteRequestBroker.createNegotiation(negotiation, new ResponseHandler<Negotiation>() {

			@Override
			public void onResponse(Negotiation response) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Negociação criada com sucesso."), TYPE.TRAY_NOTIFICATION));
				NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
				navig.setParameter("negotiationid", response.id);
				NavigationHistoryManager.getInstance().go(navig);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar a negociação."), TYPE.ALERT_NOTIFICATION));
			}
		});
	}

	@Override
	public void setParameters(final HasParameters parameterHolder){

		ownerId = parameterHolder.getParameter("quoterequestid");
		ownerTypeId = BigBangConstants.EntityIds.QUOTE_REQUEST;
		quoteRequestBroker.getQuoteRequest(ownerId, new ResponseHandler<QuoteRequest>() {

			@Override
			public void onResponse(QuoteRequest response) {
				view.getOwnerForm().setValue(response);	
				managerId = response.managerId;
				setParentParameters(parameterHolder);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível mostrar a apólice."), TYPE.ALERT_NOTIFICATION));

			}
		});
	}

	protected void setParentParameters(HasParameters parameterHolder) {
		super.setParameters(parameterHolder);

	}

}
