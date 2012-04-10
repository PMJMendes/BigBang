package bigBang.module.mainModule.client.userInterface.view;

import org.gwt.mosaic.ui.client.SheetPanel.Resources;

import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.mainModule.client.userInterface.presenter.MainScreenViewPresenter;
import bigBang.module.mainModule.client.userInterface.presenter.MainScreenViewPresenter.Action;
import bigBang.module.mainModule.client.userInterface.presenter.MainScreenViewPresenter.Section;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MainScreenView extends View implements MainScreenViewPresenter.Display {

	private MenuItem usernameMenuItem;
	private MenuItem domainMenuItem;
	private MenuItem backofficeMenuitem;
	private ActionInvokedEventHandler<Action> actionHandler;
	private HasWidgets container;
	private HasWidgets preferencesContainer;
	private PreferencesPanelView preferencesView;
	private TabBar tabBar;
	
	public MainScreenView(){
		final VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		
		wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		wrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		wrapper.setSize(Window.getClientWidth() + "px", (Window.getClientHeight() - 5) + "px");

		Window.setMargin("0px");
		Window.enableScrolling(false);
		Window.addResizeHandler(new ResizeHandler() {

			public void onResize(ResizeEvent event) {
				wrapper.setSize(Window.getClientWidth() + "px", (Window.getClientHeight() - 5) + "px");
			}
		});
		final MenuBar menuBar = new MenuBar();
		menuBar.setAnimationEnabled(true);

		usernameMenuItem = new MenuItem("", new Command() {

			public void execute() {
				return;
			}
		});
		menuBar.addItem(usernameMenuItem);
		
		backofficeMenuitem = new MenuItem("Backoffice", new Command() {
			
			@Override
			public void execute() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<MainScreenViewPresenter.Action>(Action.BACKOFFICE));
			}
		});
		menuBar.addItem(backofficeMenuitem);
		menuBar.addSeparator();
		
		domainMenuItem = new MenuItem("", new Command() {

			public void execute() {
				return;
			}
		});
		menuBar.addItem(domainMenuItem);

		menuBar.addSeparator();
		menuBar.addItem(new MenuItem("Definições", new Command() {

			public void execute() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<MainScreenViewPresenter.Action>(Action.SHOW_PREFERENCES));
			}
		}));
		menuBar.addSeparator();

		MenuItem logoutMenuItem = new MenuItem("Sair", new Command() {

			public void execute() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<MainScreenViewPresenter.Action>(Action.LOGOUT));
			}
		});
		menuBar.addItem(logoutMenuItem);

		wrapper.add(menuBar);
		wrapper.setCellHorizontalAlignment(menuBar, HasHorizontalAlignment.ALIGN_RIGHT);

		this.tabBar = new TabBar();
		wrapper.add(tabBar);
		
		this.buildTabBar();
		
		SimplePanel container = new SimplePanel();
		container.setSize("100%", "100%");
		wrapper.add(container);
		wrapper.setCellHeight(container, "100%");
		this.container = container;

		this.preferencesView = new PreferencesPanelView(((Resources)GWT.create(Resources.class)));
		VerticalPanel preferencesPanelWrapper = new VerticalPanel();
		preferencesPanelWrapper.setSize("100%", "100%");
		this.preferencesView.add(preferencesPanelWrapper);
		
		SimplePanel preferencesWrapper = new SimplePanel();
		preferencesPanelWrapper.add(preferencesWrapper);
		preferencesPanelWrapper.getElement().getStyle().setBackgroundColor("#F6F6F6");
		preferencesPanelWrapper.setCellHeight(preferencesWrapper, "100%");
		Button closeButton = new Button("Fechar");
		closeButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<MainScreenViewPresenter.Action>(Action.HIDE_PREFERENCES));
			}
		});
		preferencesPanelWrapper.add(closeButton);
		closeButton.setWidth("150px");
		preferencesPanelWrapper.setCellHorizontalAlignment(closeButton,HasHorizontalAlignment.ALIGN_CENTER);
		
		preferencesWrapper.setSize("100%", "100%");
		this.preferencesContainer = preferencesWrapper;
		
		showBackOffice(false);
	}
	
	@Override
	protected void initializeView() {
		return;
	}
	
	private void buildTabBar(){
		this.tabBar.addTab("Agenda");
		this.tabBar.addTab("Sistema Geral");
		this.tabBar.addTab("Clientes");
		this.tabBar.addTab("C. de Mercado");
		this.tabBar.addTab("Apólices");
		this.tabBar.addTab("Recibos");
		this.tabBar.addTab("Análises de Risco");
		this.tabBar.addTab("Sinistros");
		//TODO
		
		this.tabBar.addSelectionHandler(new SelectionHandler<Integer>() {
			
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				switch(event.getSelectedItem()){
				case 0:
					actionHandler.onActionInvoked(new ActionInvokedEvent<MainScreenViewPresenter.Action>(Action.SHOW_SECTION_TASKS));
					break;
				case 1:
					actionHandler.onActionInvoked(new ActionInvokedEvent<MainScreenViewPresenter.Action>(Action.SHOW_SECTION_GENERAL_SYSTEM));
					break;
				case 2:
					actionHandler.onActionInvoked(new ActionInvokedEvent<MainScreenViewPresenter.Action>(Action.SHOW_SECTION_CLIENT));
					break;
				case 3:
					actionHandler.onActionInvoked(new ActionInvokedEvent<MainScreenViewPresenter.Action>(Action.SHOW_SECTION_QUOTE_REQUEST));
					break;
				case 4:
					actionHandler.onActionInvoked(new ActionInvokedEvent<MainScreenViewPresenter.Action>(Action.SHOW_SECTION_INSURANCE_POLICY));
					break;
				case 5:
					actionHandler.onActionInvoked(new ActionInvokedEvent<MainScreenViewPresenter.Action>(Action.SHOW_SECTION_RECEIPT));
					break;
				case 6:
					actionHandler.onActionInvoked(new ActionInvokedEvent<MainScreenViewPresenter.Action>(Action.SHOW_SECTION_RISK_ANALISYS));
					break;
				case 7:
					actionHandler.onActionInvoked(new ActionInvokedEvent<MainScreenViewPresenter.Action>(Action.SHOW_SECTION_CASUALTY));
					break;
					//TODO
				}
			}
		});
	}

	@Override
	public void showSection(Section section) {
		switch(section) {
		case TASKS:
			this.tabBar.selectTab(0, false);
			break;
		case GENERAL_SYSTEM:
			this.tabBar.selectTab(1, false);
			break;
		case CLIENT:
			this.tabBar.selectTab(2, false);
			break;
		case QUOTE_REQUEST:
			this.tabBar.selectTab(3, false);
			break;
		case INSURANCE_POLICY:
			this.tabBar.selectTab(4, false);
			break;
		case RECEIPT:
			this.tabBar.selectTab(5, false);
			break;
		case RISK_ANALISYS:
			this.tabBar.selectTab(6, false);
			break;
		case CASUALTY:
			this.tabBar.selectTab(7, false);
			break;
		}
	}
	
	@Override
	public void showPreferences(boolean show){
		if(show){
			preferencesView.show();
		}else{
			preferencesView.hide();
		}
	}

	@Override
	public void setUsername(String username) {
		usernameMenuItem.setText(username);
	}

	@Override
	public void setDomain(String domain) {
		domainMenuItem.setText(domain);
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}
	
	@Override
	public void showBackOffice(boolean show) {
		this.backofficeMenuitem.setVisible(show);
	}
	
	@Override
	public HasWidgets getContainer() {
		return this.container;
	}

	@Override
	public HasWidgets getPreferencesContainer() {
		return this.preferencesContainer;
	}
	
}
