package bigBang.module.loginModule.client.userInterface.view;

import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.MessageBox.MessageBoxType;
import org.gwt.mosaic.ui.client.util.ButtonHelper;
import org.gwt.mosaic.ui.client.util.ButtonHelper.ButtonLabelType;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.loginModule.client.resources.Resources;
import bigBang.module.loginModule.client.userInterface.presenter.LoginViewPresenter;
import bigBang.module.loginModule.client.userInterface.presenter.LoginViewPresenter.LoginData;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoginView extends View implements LoginViewPresenter.Display {

	private VerticalPanel panel;

	private TextBox usernameField;
	private PasswordTextBox passwordField;
	private ToolButton submitButton;
	private ListBox domainList;
	private SimplePanel errorPanel;
	private Label errorLabel;
	protected PopupPanel loginPopup;

	public LoginView(){
		Resources resources = GWT.create(Resources.class);
		resources.css().ensureInjected();

		this.panel = new VerticalPanel();
		this.panel.getElement().getStyle().setMarginTop(150, Unit.PX);
		this.panel.setSize("100%", "100%");
		this.panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		this.panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		this.usernameField = new TextBox();
		this.passwordField = new PasswordTextBox();
		this.submitButton = new ToolButton(ButtonHelper.createButtonLabel(
				AbstractImagePrototype.create(resources.login()), "Entrar",
				ButtonLabelType.TEXT_ON_LEFT));
		this.domainList = new ListBox();

		HorizontalPanel formWrapper = new HorizontalPanel();
		formWrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		Image logoImage = new Image(resources.logo());
		logoImage.getElement().getStyle().setProperty("borderRight", "2px solid #1f5394");
		formWrapper.add(logoImage);

		Grid loginFormPanel = new Grid(4, 2);
		loginFormPanel.getElement().getStyle().setMarginLeft(20, Unit.PX);
		loginFormPanel.setCellSpacing(10);
		loginFormPanel.getElement().getStyle().setBorderWidth(1, Unit.PX);
		loginFormPanel.setWidget(0, 1, this.usernameField);
		loginFormPanel.setWidget(0, 0, new Label("Nome de utilizador"));
		loginFormPanel.setWidget(1, 1, this.passwordField);
		loginFormPanel.setWidget(1, 0, new Label("Palavra-passe"));
		loginFormPanel.setWidget(2, 1, this.domainList);
		loginFormPanel.setWidget(2, 0, new Label("Domínio"));
		this.domainList.setWidth("120px");
		loginFormPanel.setWidget(3, 1, this.submitButton);

		formWrapper.add(loginFormPanel);

		errorPanel = new SimplePanel();
		errorPanel.getElement().addClassName(resources.css().errorLabel());
		errorPanel.setWidth("400px");

		errorLabel = new Label();
		errorPanel.setWidget(errorLabel);

		this.panel.add(errorPanel);
		this.panel.setCellHeight(errorPanel, "70px");
		this.panel.add(formWrapper);
		this.panel.setCellHeight(formWrapper, "200px");
		SimplePanel fillerBottom = new SimplePanel();
		fillerBottom.setSize("100%", "100%");
		this.panel.add(fillerBottom);
		this.panel.setCellHeight(fillerBottom, "100%");

		this.addAttachHandler(new AttachEvent.Handler() {

			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(event.isAttached())
					usernameField.setFocus(true);
			}
		});

		this.submitButton.setEnabled(false);
		KeyUpHandler keyUpHandler = new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				LoginView.this.submitButton.setEnabled(usernameField.getValue().length() > 0 && passwordField.getValue().length() > 0);
				if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
					LoginView.this.submitButton.click();
			}
		};

		this.submitButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				submitButton.setEnabled(false);
				hideErrorMessage();
			}
		});

		this.usernameField.addKeyUpHandler(keyUpHandler);
		this.passwordField.addKeyUpHandler(keyUpHandler);

		hideErrorMessage();

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

	@Override
	public String getDomain() {
		return domainList.getValue(domainList.getSelectedIndex());
	}

	@Override
	public void setDomains(String[] domains) {
		domainList.clear();
		for(int i = 0; i < domains.length; i++) {
			domainList.addItem(domains[i], domains[i]);
		}
	}

	@Override
	public void setSelectedDomain(String domain) {
		int nDomains = domainList.getItemCount();
		for(int i = 0; i < nDomains; i++) {
			if(domain.equals(domainList.getValue(i))){
				domainList.setItemSelected(i, true);
				return;
			}
		}
	}

	public void showLoading(boolean show) {
		this.domainList.setEnabled(!show);
		this.usernameField.setEnabled(!show);
		this.passwordField.setEnabled(!show);
		this.submitButton.setEnabled(!show && (this.passwordField.getText().length() > 0 && this.usernameField.getText().length() > 0));
	}

	@Override
	public void showErrorMessage(String message) {
		this.errorLabel.setText(message);
		this.errorPanel.setVisible(true);
	}

	@Override
	public void hideErrorMessage() {
		this.errorPanel.setVisible(false);
	}

	@Override
	public void showCredentialsPrompt(ResponseHandler<LoginData> responseHandler) {
		MessageBox prompt = new MessageBox(MessageBoxType.PASSWORD,
				"Login Form") {
			@Override
			public void onClose(boolean result) {
				hide();
				if (result) {
					//TODO FJVC
				}
			}
		};
	}

	@Override
	public void hideCredentialsPrompt() {
		this.loginPopup.hidePopup();
	}

	@Override
	public void showCredentialsPromptLoginError(boolean show) {
		//TODO 
	}

}
