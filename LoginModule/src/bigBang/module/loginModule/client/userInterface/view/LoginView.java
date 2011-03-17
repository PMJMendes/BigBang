package bigBang.module.loginModule.client.userInterface.view;

import bigBang.library.shared.userInterface.view.View;
import bigBang.module.loginModule.client.userInterface.presenter.LoginViewPresenter;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoginView extends View implements LoginViewPresenter.Display {

	private VerticalPanel panel;
	
	private TextBox usernameField;
	private PasswordTextBox passwordField;
	private Button submitButton;
	
	public LoginView(){		
		this.panel = new VerticalPanel();
		this.panel.setSize("100%", "100%");
		this.panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		this.panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		this.usernameField = new TextBox();
		this.passwordField = new PasswordTextBox();
		this.submitButton = new Button("Entrar");
		
		VerticalPanel loginFormPanel = new VerticalPanel();
		loginFormPanel.add(this.usernameField);
		loginFormPanel.add(this.passwordField);
		loginFormPanel.add(this.submitButton);
		
		SimplePanel fillerTop = new SimplePanel();
		this.panel.add(fillerTop);
		this.panel.setCellHeight(fillerTop, "100%");
		this.panel.add(loginFormPanel);
		SimplePanel fillerBottom = new SimplePanel();
		this.panel.add(fillerBottom);
		this.panel.setCellHeight(fillerBottom, "100%");
		
		initWidget(this.panel);
	}

	public HasClickHandlers getSubmitButton() {
		return submitButton;
	}

	public HasValue<String> getUsername() {
		return usernameField;
	}

	public HasValue<String> getPassword() {
		return passwordField;
	}
	
}
