package bigBang.module.quoteRequestModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.NegotiationBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.Negotiation.Deletion;
import bigBang.definitions.shared.ProcessBase;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.NegotiationForm;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public abstract class NegotiationViewPresenter implements ViewPresenter{
	
	public static enum Action{
		EDIT, SAVE, CANCEL, DELETE
		
	}
	
	
	
	public static interface Display{

		Widget asWidget();

		void registerActionHandler(ActionInvokedEventHandler<Action> handler);

		HasValue<ProcessBase> getOwnerForm();

		HasEditableValue<Negotiation> getForm();
		
	}
	
	protected Display view;
	protected boolean bound = false;
	private NegotiationBroker negotiationBroker;
	
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
		
		//TODO
		
		bound = true;
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		// TODO Auto-generated method stub
		
	}
	
	protected abstract void createNegotiation(Negotiation negotiation);
	
	protected void cancelEditNegotiation(){
		//TODO
	}
	
	protected void updateNegotiation(Negotiation negotiation){
		negotiationBroker.updateNegotiation(negotiation, new ResponseHandler<Negotiation>() {
			
			@Override
			public void onResponse(Negotiation response) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	protected void removeNegotiation(Deletion delete){
		negotiationBroker.removeNegotiation(delete, new ResponseHandler<String>() {
			
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	

}
