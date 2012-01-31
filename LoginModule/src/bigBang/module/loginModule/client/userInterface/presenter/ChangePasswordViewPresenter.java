package bigBang.module.loginModule.client.userInterface.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.loginModule.interfaces.AuthenticationService;
import bigBang.module.loginModule.interfaces.AuthenticationServiceAsync;

public class ChangePasswordViewPresenter implements ViewPresenter {

	public static interface Display {
		HasValue<String> getCurrentPassword();
		HasValue<String> getNewPassword();
		HasValue<String> getNewPasswordConfirmation();
		HasClickHandlers getConfirm();

		Widget asWidget();
	}
	
	private AuthenticationServiceAsync service;
	private Display view;
	private boolean bound;
	
	public ChangePasswordViewPresenter(Display view){
		service = AuthenticationService.Util.getInstance();
		setView((UIObject) view);
	}
	
	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
//		clearView();
	}
	
	private void bind(){
		if(bound){return;}
		
		view.getConfirm().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				changePassword();
			}
		});
		//APPLICATION-WIDE EVENTS
	}
	
	private void changePassword(){
		String newPassword = view.getNewPassword().getValue();
		if(!newPassword.equalsIgnoreCase(view.getNewPasswordConfirmation().getValue())){
			onPasswordDoesNotMatch();
			clearView();
		}else{
			service.changePassword(view.getCurrentPassword().getValue(), newPassword, new BigBangAsyncCallback<String>() {

				@Override
				public void onSuccess(String result) {
					onPasswordChangeSuccess();
					clearView();
				}
				
				@Override
				public void onFailure(Throwable caught) {
					onPasswordChangeFailed();
					clearView();
					super.onFailure(caught);
				}
			});
		}
	}
	
	private void clearView(){
		this.view.getCurrentPassword().setValue("");
		this.view.getNewPassword().setValue("");
		this.view.getNewPasswordConfirmation().setValue("");
	}
	
	private void onPasswordChangeSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A palavra-passe foi alterada com sucesso"), TYPE.TRAY_NOTIFICATION));
	}
	
	private void onPasswordChangeFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível alterar a palavra-passe"), TYPE.ALERT_NOTIFICATION));
	}

	private void onPasswordDoesNotMatch(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A confirmação da nova palavra-passe não coincide"), TYPE.ALERT_NOTIFICATION));
	}
	
}
