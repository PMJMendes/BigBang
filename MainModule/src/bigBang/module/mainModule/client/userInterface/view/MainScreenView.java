package bigBang.module.mainModule.client.userInterface.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.gwt.mosaic.ui.client.SheetPanel.Resources;

import bigBang.library.client.userInterface.MenuSection;
import bigBang.library.client.userInterface.presenter.SectionViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.mainModule.client.userInterface.presenter.MainScreenViewPresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
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
	private HashMap<MenuSection, Widget> sectionWrappers;
	private ArrayList<SectionViewPresenter> sectionIndexes;
	
	private MenuItem usernameMenuItem;
	private MenuItem domainMenuItem;
	private MenuItem logoutMenuItem;

	public MainScreenView(){
		this.sectionWrappers = new HashMap<MenuSection, Widget>();
		this.sectionIndexes = new ArrayList<SectionViewPresenter>();
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
				
		domainMenuItem = new MenuItem("", new Command() {

			public void execute() {

			}
		});
		menuBar.addItem(domainMenuItem);		
		
		menuBar.addSeparator();
		menuBar.addItem(new MenuItem("Preferências", new Command() {

			public void execute() {
				showPreferencesPanel();
			}
		}));
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
		
		disableTextSelection(true);
	}

	public void createMenuSection(SectionViewPresenter sectionPresenter, int index) {
		sectionIndexes.add(sectionPresenter);
		rearrangeTabs();
	}
	
	public void rearrangeTabs(){
		Collections.sort(this.sectionIndexes, new Comparator<SectionViewPresenter>() {

			@Override
			public int compare(SectionViewPresenter o1, SectionViewPresenter o2) {
				return o1.getSection().getMenuIndex().compareTo(o2.getSection().getMenuIndex());
			}
		});
		
		this.mainTabPanel.clear();
		sectionWrappers.clear();
		
		for(SectionViewPresenter sectionPresenter : this.sectionIndexes){
			MenuSection section = sectionPresenter.getSection();
			AbsolutePanel tabPanelWidget = new AbsolutePanel();
			tabPanelWidget.setSize("100px", "35px");

			Label label = new Label(section.getShortDescription());
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
			
			sectionWrappers.put(section, wrapper);
		}
		this.mainTabPanel.selectTab(0);
	}

	public void showPreferencesPanel(){
		PreferencesPanelView preferencesView = new PreferencesPanelView(((Resources)GWT.create(Resources.class)));
		preferencesView.show();
	}

	public void showSection(MenuSection section) throws Exception {
		int index = this.mainTabPanel.getDeckPanel().getWidgetIndex(sectionWrappers.get(section));
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

	@Override
	public void setDomain(String domain) {
		domainMenuItem.setText(domain);
	}

}
