package bigBang.module.loginModule.client.userInterface.view;

import bigBang.library.client.userInterface.view.View;
import bigBang.module.loginModule.client.userInterface.presenter.LoginViewPresenter;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
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
	private Button submitButton;
	private ListBox domainList;
	
	public LoginView(){		
		this.panel = new VerticalPanel();
		this.panel.getElement().getStyle().setMarginTop(150, Unit.PX);
		this.panel.setSize("100%", "100%");
		this.panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		this.panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		this.usernameField = new TextBox();
		this.passwordField = new PasswordTextBox();
		this.submitButton = new Button("Entrar");
		this.domainList = new ListBox();
		
		Grid loginFormPanel = new Grid(4, 2);
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
	
}
