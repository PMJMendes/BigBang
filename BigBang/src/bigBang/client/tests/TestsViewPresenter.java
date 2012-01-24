package bigBang.client.tests;

import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.TypifiedText;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.dataAccess.TypifiedTextBroker;
import bigBang.library.client.dataAccess.TypifiedTextClient;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class TestsViewPresenter implements ViewPresenter, TypifiedTextClient {
	
	
	private List<TypifiedText> texts;
	private TypifiedTextBroker broker;

	
	public static interface Display {
		Widget asWidget();
	}
	
	private Display view;
	
	public TestsViewPresenter(Display view){
		
		broker = (TypifiedTextBroker)DataBrokerManager.staticGetBroker(BigBangConstants.TypifiedListIds.TYPIFIED_TEXT);
		setView((UIObject) view);
		
	}
	
	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(this.view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		
		broker.unregisterClient(this);
		broker.registerClient("TEST", this);
		
		broker.getTexts("TEST", new ResponseHandler<List<TypifiedText>>() {
			
			@Override
			public void onResponse(List<TypifiedText> response) {
				
				texts = response;
				
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				
				
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível mostrar os textos pedidos."), TYPE.ALERT_NOTIFICATION));
				
			}
		});
				
	}

	@Override
	public int getTypifiedDataVersionNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setTypifiedDataVersionNumber(int number) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeText(TypifiedText text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addText(TypifiedText text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateText(TypifiedText text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTypifiedTexts(List<TypifiedText> texts) {
		// TODO Auto-generated method stub
		
	}

}
