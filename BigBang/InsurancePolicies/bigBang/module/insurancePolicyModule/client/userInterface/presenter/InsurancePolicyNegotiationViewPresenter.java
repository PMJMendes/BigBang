package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.NegotiationBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.Negotiation.Deletion;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationViewPresenter;

public class InsurancePolicyNegotiationViewPresenter extends NegotiationViewPresenter{

	private InsurancePolicyBroker insurancePolicyBroker;

	
	
	public InsurancePolicyNegotiationViewPresenter(Display view) {
		super(view);
		insurancePolicyBroker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
		
	}
	
	@Override
	public void setParameters(final HasParameters parameterHolder){
		
		ownerId = parameterHolder.getParameter("id");
		ownerTypeId = parameterHolder.getParameter("ownertypeid");
		insurancePolicyBroker.getPolicy(ownerId, new ResponseHandler<InsurancePolicy>() {
			
			@Override
			public void onResponse(InsurancePolicy response) {
				view.getOwnerForm().setValue(response);	
				managerId = response.managerId;
				companyId = response.insuranceAgencyId;
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

	@Override
	protected void createNegotiation(Negotiation negotiation) {
		negotiation.ownerTypeId = ownerTypeId;
		insurancePolicyBroker.createNegotiation(negotiation, new ResponseHandler<Negotiation>() {
			
			@Override
			public void onResponse(Negotiation response) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Negociação criada com sucesso."), TYPE.TRAY_NOTIFICATION));
				NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
				navItem.setParameter("negotiationid", response.id);
				NavigationHistoryManager.getInstance().go(navItem);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar a negociação."), TYPE.ALERT_NOTIFICATION));
			}
		});
	}


}
