package bigBang.module.quoteRequestModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.NegotiationBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.Negotiation.Deletion;
import bigBang.definitions.shared.ProcessBase;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public abstract class NegotiationViewPresenter implements ViewPresenter{

	public static enum Action{
		EDIT, SAVE, CANCEL, DELETE, SELECTION_CHANGED_CONTACT, SELECTION_CHANGED_DOCUMENT

	}


	public static interface Display{

		Widget asWidget();

		void registerActionHandler(ActionInvokedEventHandler<Action> handler);

		HasValue<ProcessBase> getOwnerForm();

		HasEditableValue<Negotiation> getForm();

		void setToolbarSaveMode(boolean b);

		void setParentHeaderTitle(String title);

		void disableToolbar();

		void allowEdit(boolean b);

		void allowDelete(boolean b);

		String getContactId();

		String getDocumentId();

		void applyOwnerToList(String negotiationId);

	}

	protected Display view;
	protected boolean bound = false;
	private NegotiationBroker negotiationBroker;
	private String negotiationId;
	protected String ownerId;
	private boolean hasPermissions;
	protected String ownerTypeId;
	protected String managerId;
	protected String companyId;


	public NegotiationViewPresenter(Display view){
		setView((UIObject)view);
		negotiationBroker = (NegotiationBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.NEGOTIATION);
	} 

	@Override
	public void setView(UIObject view) {
		this.view = (Display)view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(view.asWidget());
	}

	private void bind() {
		if(bound){
			return;
		}

		view.registerActionHandler(new ActionInvokedEventHandler<NegotiationViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CANCEL:{
					view.getForm().revert();
					view.setToolbarSaveMode(false);
					view.getForm().setReadOnly(true);
					break;
				}
				case DELETE:{
					//LANÇA A FORM DO DELETE
					Deletion deletion = new Deletion();
					deletion.negotiationId = view.getForm().getValue().id;
					//TODO create form to delete;
					removeNegotiation(deletion);
					break;
				}
				case EDIT:{
					view.getForm().setReadOnly(false);
					view.setToolbarSaveMode(true);
					break;
				}
				case SAVE:{
					Negotiation newNeg = view.getForm().getInfo();
					if(newNeg.id == null){
						newNeg.ownerId = ownerId;
						createNegotiation(newNeg);
					}
					else{
						updateNegotiation(newNeg);
					}
					break;
				}
				case SELECTION_CHANGED_CONTACT:{
					//TODO
					break;
				}
				case SELECTION_CHANGED_DOCUMENT:{
					//TODO
					break;
				}
				}

			}
		});

		bound = true;
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {

		hasPermissions = true; //TODO TEM DE VIR DE FORA
		negotiationId = parameterHolder.getParameter("negotiationid");
		ownerTypeId = parameterHolder.getParameter("ownertypeid");

		if(negotiationId == null){
			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível mostrar a negociação."), TYPE.ALERT_NOTIFICATION));
			view.getForm().setReadOnly(true);
			view.disableToolbar();
		}
		else if(negotiationId.equalsIgnoreCase("new")){
			Negotiation newNeg = new Negotiation();
			newNeg.companyId = companyId;
			newNeg.managerId = managerId;
			newNeg.ownerId = ownerId;
			view.getForm().setInfo(newNeg);
			view.getForm().setReadOnly(!hasPermissions);
			view.setToolbarSaveMode(true);
			view.allowDelete(false);
			view.allowEdit(!hasPermissions);
			//TODO BLOCK ALLOW CREATION ON DOCUMENT AND CONTACT LIST -> ventura
		}else{
			
			negotiationBroker.getNegotiation(negotiationId, new ResponseHandler<Negotiation>() {
				
				@Override
				public void onResponse(Negotiation response) {
					view.getForm().setInfo(response);
					view.applyOwnerToList(negotiationId);
				}
				
				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível mostrar a negociação."), TYPE.ALERT_NOTIFICATION));
					view.getForm().setReadOnly(true);
					view.disableToolbar();
				}
			});
			
			view.getForm().setReadOnly(true);
			view.setToolbarSaveMode(false);
			view.allowDelete(hasPermissions);
			view.allowEdit(hasPermissions);

		}


	}

	protected abstract void createNegotiation(Negotiation negotiation);

	protected void updateNegotiation(Negotiation negotiation){

		negotiationBroker.updateNegotiation(negotiation, new ResponseHandler<Negotiation>() {

			@Override
			public void onResponse(Negotiation response) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Negociação gravada com sucesso."), TYPE.TRAY_NOTIFICATION));
				view.setToolbarSaveMode(false);
				view.getForm().setReadOnly(true);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Erro ao gravar a negociação."), TYPE.ALERT_NOTIFICATION));
			}
		});
	}

	protected void removeNegotiation(Deletion delete){
		negotiationBroker.removeNegotiation(delete, new ResponseHandler<String>() {

			@Override
			public void onResponse(String response) {
				

			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				// TODO Auto-generated method stub

			}
		});
	}



}
