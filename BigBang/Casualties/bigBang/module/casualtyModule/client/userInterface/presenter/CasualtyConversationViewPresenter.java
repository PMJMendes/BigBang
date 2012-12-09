package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.CasualtyDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Casualty;
import bigBang.library.client.HasParameters;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.presenter.ConversationViewPresenter;

public class CasualtyConversationViewPresenter extends ConversationViewPresenter<Casualty>{

	private CasualtyDataBroker broker;
	protected Casualty casualty;
	
	public CasualtyConversationViewPresenter(Display<Casualty> view) {
		super(view);
		broker = (CasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CASUALTY);
	}
	
	@Override
	public void setParameters(HasParameters parameterHolder) {
		String casualtyId = parameterHolder.getParameter("casualtyid");
		parameterHolder.setParameter("ownerid", casualtyId);
		parameterHolder.setParameter("ownertypeid", BigBangConstants.EntityIds.CASUALTY);
		super.setParameters(parameterHolder);
	}

	@Override
	protected void fillOwner(String ownerId, final ResponseHandler<Casualty> handler) {
		broker.getCasualty(ownerId, new ResponseHandler<Casualty>() {
			
			@Override
			public void onResponse(Casualty response) {
				casualty = response;
				setContacts();
				handler.onResponse(casualty);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetOwnerFailed();
			}
		});
	}
	

	protected void setContacts() {
		view.addContact("Sinistro (" + casualty.processNumber + ")", casualty.id, BigBangConstants.EntityIds.CASUALTY);
		view.addContact("Cliente (" + casualty.clientName + ")", casualty.clientId, BigBangConstants.EntityIds.CLIENT);		
		view.addContact("Mediador (" + casualty.inheritMediatorName + ")", casualty.inheritMediatorId, BigBangConstants.EntityIds.MEDIATOR);
	}


}
