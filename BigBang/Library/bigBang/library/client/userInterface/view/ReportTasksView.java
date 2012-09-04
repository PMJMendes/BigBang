package bigBang.library.client.userInterface.view;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.Report.Section;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import  bigBang.library.client.userInterface.presenter.ReportTasksViewPresenter;
import bigBang.library.client.userInterface.presenter.ReportTasksViewPresenter.Action;

public class ReportTasksView extends View implements ReportTasksViewPresenter.Display {

	protected ActionInvokedEventHandler<Action> handler;
	protected VerticalPanel sectionsContainer;
	protected FlowPanel currentPanel;
	protected Button dismissButton;
	
	public ReportTasksView(){
		VerticalPanel sectionsWrapper = new VerticalPanel();
		initWidget(sectionsWrapper);
		sectionsWrapper.setSize("100%", "100%");
		sectionsWrapper.setStyleName("emptyContainer");
		
		sectionsContainer = new VerticalPanel();
		sectionsContainer.setWidth("100%");
		sectionsContainer.getElement().getStyle().setPadding(10, Unit.PX);

		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setSize("100%", "100%");
		scrollPanel.add(sectionsContainer);

		sectionsWrapper.add(scrollPanel);
		sectionsWrapper.setCellHeight(scrollPanel, "100%");
		
		dismissButton = new Button("Tomei Conhecimento");
		dismissButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				handler.onActionInvoked(new ActionInvokedEvent<ReportTasksViewPresenter.Action>(Action.DISMISS));
			}
		});
		
		sectionsContainer.add(dismissButton);
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public void clearReportSections() {
		this.sectionsContainer.clear();
		this.sectionsContainer.add(dismissButton);
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.handler = handler;
	}

	@Override
	public void addReportSection(Section section) {
		FlowPanel panel = new FlowPanel(); 
		currentPanel = panel;
		panel.setWidth("100%");
		panel.getElement().setInnerHTML(section.htmlContent);
		sectionsContainer.add(panel);
		sectionsContainer.add(dismissButton);
		
		currentPanel.getElement().getStyle().setMarginBottom(50, Unit.PX);
	}

}
