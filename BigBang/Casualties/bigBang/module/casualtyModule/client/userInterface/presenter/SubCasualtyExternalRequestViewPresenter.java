package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.SubCasualtyDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.definitions.shared.SubCasualty;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ExternalRequestViewPresenter;

public class SubCasualtyExternalRequestViewPresenter extends ExternalRequestViewPresenter<SubCasualty>{

	private SubCasualtyDataBroker broker;

	public SubCasualtyExternalRequestViewPresenter(Display<SubCasualty> view) {
		super(view);
		broker = (SubCasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.SUB_CASUALTY);
	}

	@Override
	public void setParameters(final HasParameters parameterHolder) {
		ownerId = parameterHolder.getParameter("subcasualtyid");
		ownerTypeId = BigBangConstants.EntityIds.SUB_CASUALTY;

		broker.getSubCasualty(ownerId, new ResponseHandler<SubCasualty>() {

			@Override
			public void onResponse(SubCasualty response) {
				view.getOwnerForm().setValue(response);
				setParentParameters(parameterHolder);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {

			}
		});		
	}

	protected void setParentParameters(HasParameters parameterHolder) {
		super.setParameters(parameterHolder);		
	}

	@Override
	protected void createExternalInfoRequest(ExternalInfoRequest toSend) {
		
		broker.createExternalInfoRequest(toSend, new ResponseHandler<ExternalInfoRequest>() {


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
