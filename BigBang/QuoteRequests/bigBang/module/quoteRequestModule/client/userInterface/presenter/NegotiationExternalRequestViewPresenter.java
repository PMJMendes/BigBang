package bigBang.module.quoteRequestModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.NegotiationBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.definitions.shared.Negotiation;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ExternalRequestViewPresenter;

public class NegotiationExternalRequestViewPresenter extends ExternalRequestViewPresenter<Negotiation>{

	private NegotiationBroker negotiationBroker;

	public NegotiationExternalRequestViewPresenter(Display<Negotiation> view) {
		super(view);
		negotiationBroker = (NegotiationBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.NEGOTIATION);
	}

	@Override
	public void setParameters(final HasParameters parameterHolder) {
		
		ownerId = parameterHolder.getParameter("negotiationId");
		ownerTypeId = BigBangConstants.EntityIds.NEGOTIATION;
		
		negotiationBroker.getNegotiation(ownerId, new ResponseHandler<Negotiation>() {
			
			@Override
			public void onResponse(Negotiation response) {
				view.getOwnerForm().setValue(response);	
				setParentParameters(parameterHolder);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível mostrar a negociação."), TYPE.ALERT_NOTIFICATION));
				
			}
		});
		
	}

	protected void setParentParameters(HasParameters parameterHolder) {
		super.setParameters(parameterHolder);
		
	}

	@Override
	protected void createExternalInfoRequest(ExternalInfoRequest toSend) {
		negotiationBroker.createExternalInfoRequest(toSend, new ResponseHandler<ExternalInfoRequest>() {


			@Override
			public void onResponse(ExternalInfoRequest response) {

				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Pedido de Informação Externo guardado com sucesso."), TYPE.TRAY_NOTIFICATION));
				NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
				navig.popFromStackParameter("display");
				navig.removeParameter("externalrequestid");	
				NavigationHistoryManager.getInstance().go(navig);
				counter = 0;
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível guardar o Pedido de Informação Externo."), TYPE.ALERT_NOTIFICATION));
				counter = 0;
			}
		});	
		
	}

}
