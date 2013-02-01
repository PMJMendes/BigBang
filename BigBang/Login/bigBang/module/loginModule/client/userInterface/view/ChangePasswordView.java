package bigBang.module.loginModule.client.userInterface.view;

import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.PasswordTextBoxFormField;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.loginModule.client.userInterface.presenter.ChangePasswordViewPresenter;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ChangePasswordView extends View implements ChangePasswordViewPresenter.Display {

	private PasswordTextBoxFormField currentPassword;
	private PasswordTextBoxFormField newPassword;
	private PasswordTextBoxFormField newPasswordConfirmation;
	private Button confirmButton;
	
	public ChangePasswordView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setWidth("100%");
		
		currentPassword = new PasswordTextBoxFormField("Palavra-passe actual");
		newPassword = new PasswordTextBoxFormField("Palavra-passe nova");
		newPasswordConfirmation = new PasswordTextBoxFormField("Confirmação da nova palavra-passe");
		
		confirmButton = new Button("Alterar Palavra-passe");
		
		ListHeader header = new ListHeader("Alteração de Palavra-Passe");
		wrapper.add(header);
		wrapper.add(currentPassword);
		wrapper.add(newPassword);
		wrapper.add(newPasswordConfirmation);
		wrapper.add(confirmButton);
		
		wrapper.getElement().getStyle().setBackgroundColor("transparent");
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasValue<String> getCurrentPassword() {
		return currentPassword;
	}

	@Override
	public HasValue<String> getNewPassword() {
		return newPassword;
	}

	@Override
	public HasValue<String> getNewPasswordConfirmation() {
		return newPasswordConfirmation;
	}

	@Override
	public HasClickHandlers getConfirm() {
		return confirmButton;
	}

}
