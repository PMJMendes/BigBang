package bigBang.module.mainModule.client.userInterface.view;

import java.util.HashMap;

import org.gwt.mosaic.ui.client.SheetPanel.Resources;

import bigBang.library.client.userInterface.MenuSection;
import bigBang.library.client.userInterface.presenter.SectionViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.mainModule.client.userInterface.presenter.MainScreenViewPresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MainScreenView extends View implements MainScreenViewPresenter.Display {

	private VerticalPanel panel;
	private TabPanel mainTabPanel;
	private HashMap <String, Integer> processSectionIndexes;
	
	private MenuItem usernameMenuItem;
	private MenuItem logoutMenuItem;

	public MainScreenView(){
		this.processSectionIndexes = new HashMap <String, Integer>();
		panel = new VerticalPanel();
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		panel.setSize(Window.getClientWidth() + "px", (Window.getClientHeight() - 5) + "px");
		Window.setMargin("0px");
		Window.enableScrolling(false);
		Window.addResizeHandler(new ResizeHandler() {

			public void onResize(ResizeEvent event) {
				panel.setSize(Window.getClientWidth() + "px", (Window.getClientHeight() - 5) + "px");
			}
		});
		final MenuBar menuBar = new MenuBar();
		menuBar.setAnimationEnabled(true);

		usernameMenuItem = new MenuItem("", new Command() {

			public void execute() {
				
			}
		});
		menuBar.addItem(usernameMenuItem);
		menuBar.addSeparator();
		menuBar.addItem(new MenuItem("Preferências", new Command() {

			public void execute() {
				showPreferencesPanel();
			}
		}));
		menuBar.addSeparator();
		
		MenuBar subMenu = new MenuBar(true);
		final MenuItem menuItem = new MenuItem("", subMenu);
		menuItem.setWidth("80px");
		menuItem.getElement().getStyle().setProperty("textAlign", "center");
		
		final MenuItem crediteItem, aMartinsItem;
		Command command = new Command() {

			public void execute() {
				menuItem.setText("CrediteEgs");

			}
		};
		crediteItem =new MenuItem("CrediteEGS", command);
		subMenu.addItem(crediteItem);
		
		aMartinsItem = new MenuItem("AMartins", new Command() {

			public void execute() {
				menuItem.setText("AMartins");
			}
		});
		subMenu.addItem(aMartinsItem);
		menuItem.setSubMenu(subMenu);
		menuBar.addItem(menuItem);
		menuBar.addSeparator();
		logoutMenuItem = new MenuItem("Sair", new Command() {

			public void execute() {

			}
		});
		menuBar.addItem(logoutMenuItem);

		panel.add(menuBar);
		panel.setCellHorizontalAlignment(menuBar, HasHorizontalAlignment.ALIGN_RIGHT);

		this.mainTabPanel = new TabPanel();
		this.mainTabPanel.setWidth("100%");
		this.mainTabPanel.setHeight("100%");
		this.mainTabPanel.getDeckPanel().setHeight("100%");
		this.mainTabPanel.getDeckPanel().getElement().getStyle().setPadding(0, Unit.PX);
		panel.add(mainTabPanel);
		panel.setCellHeight(mainTabPanel, "100%");

		initWidget(panel);
	}

	public void createMenuSection(SectionViewPresenter sectionPresenter) {
		AbsolutePanel tabPanelWidget = new AbsolutePanel();
		tabPanelWidget.setSize("100px", "35px");

		MenuSection section = sectionPresenter.getSection();
		
		Label label = new Label(section.getDescription());
		label.getElement().getStyle().setMarginTop(12, Unit.PX);
		label.setWidth("100%");
		tabPanelWidget.add(label, 0, 0);

		if(section.hasBadge()){
			section.getBadge().getElement().getStyle().setPosition(Position.ABSOLUTE);
			tabPanelWidget.add(section.getBadge());
			section.getBadge().getElement().getStyle().setRight(30, Unit.PX);
		}

		SimplePanel wrapper = new SimplePanel();
		wrapper.setSize("100%", "100%");
		this.mainTabPanel.add(wrapper, tabPanelWidget);
		sectionPresenter.go(wrapper);
		
		this.mainTabPanel.selectTab(0);
	}

	public void showPreferencesPanel(){
		PreferencesPanelView preferencesView = new PreferencesPanelView(((Resources)GWT.create(Resources.class)));
		preferencesView.show();
	}

	public void showSection(MenuSection section) throws Exception {
		int index = this.mainTabPanel.getDeckPanel().getWidgetIndex((Widget) section);
		if(index == -1)
			throw new Exception("Could not select the menu section. it does not exist.");
		this.mainTabPanel.selectTab(index);
	}

	@Override
	public void setUsername(String username) {
		usernameMenuItem.setText(username);
	}

	@Override
	public MenuItem getLogoutButton() {
		return logoutMenuItem;
	}

}
