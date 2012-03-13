package bigBang.library.client.userInterface.presenter;

import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.definitions.shared.IncomingMessage;
import bigBang.definitions.shared.ProcessBase;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.interfaces.ExchangeService;
import bigBang.library.interfaces.ExchangeServiceAsync;
import bigBang.library.shared.ExchangeItem;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public abstract class ExternalRequestViewPresenter implements ViewPresenter{
	
	protected String ownerId;
	protected String ownerTypeId;
	private String externalRequestId;
	
	public static enum Action{
		
	}
	
	public static interface Display{
		Widget asWidget();
		HasValue<ProcessBase> getOwnerForm();
		HasEditableValue<ExternalInfoRequest> getForm();
		void disableToolbar();
		void setToolbarSaveMode(boolean b);
		void allowEdit(boolean b);
		
	}
	
	@Override
	public void setParameters(final HasParameters parameterHolder){
		
		externalRequestId = parameterHolder.getParameter("externalrequestid");

		
		if(externalRequestId == null){
			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível mostrar o pedido de informação externo."), TYPE.ALERT_NOTIFICATION));
			view.getForm().setReadOnly(true);
			view.disableToolbar();
		}
		else if(externalRequestId.equalsIgnoreCase("new")){
			ExternalInfoRequest externalRequest = new ExternalInfoRequest();
			externalRequest.parentDataObjectId = ownerId;
			externalRequest.parentDataTypeId = ownerTypeId;
			view.getForm().setInfo(externalRequest);
			view.getForm().setReadOnly(false);
			view.setToolbarSaveMode(true);
			view.allowEdit(true);
		}
		else{
			//TODO IR BUSCAR OS DADOS AO SERVIÇO!
		}
	}
	
	
	protected Display view;
	protected boolean bound = false;
	private ExchangeServiceAsync service;
	
	
	public ExternalRequestViewPresenter(Display view){
		setView((UIObject)view);
		service = ExchangeService.Util.getInstance();
	}
	
	@Override
	public void setView(UIObject view){
		this.view = (Display)view;
	}
	@Override
	public void go(HasWidgets container){
		bind();
		container.clear();
		container.add(view.asWidget());
	}
	
	private void bind(){
		if(bound){
			return;
		}
		//TODO
	}
}
