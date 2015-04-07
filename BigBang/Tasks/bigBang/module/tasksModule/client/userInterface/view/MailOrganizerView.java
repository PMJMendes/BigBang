package bigBang.module.tasksModule.client.userInterface.view;

import bigBang.definitions.shared.Document;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.HasNavigationStateChangedHandlers;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.shared.ScanItem;
import bigBang.module.tasksModule.client.userInterface.DocumentImagePanel;
import bigBang.module.tasksModule.client.userInterface.MailOrganizerToolbar;
import bigBang.module.tasksModule.client.userInterface.form.MailOrganizerForm;
import bigBang.module.tasksModule.client.userInterface.presenter.MailOrganizerViewPresenter;
import bigBang.module.tasksModule.client.userInterface.presenter.MailOrganizerViewPresenter.Action;

import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MailOrganizerView extends View implements MailOrganizerViewPresenter.Display{

	private SplitLayoutPanel wrapper;
	private DocumentImagePanel documentPanel;
	private MailOrganizerForm form;
	private MailOrganizerToolbar toolbar;
	private ActionInvokedEventHandler<MailOrganizerViewPresenter.Action> actionHandler;

	@Override
	protected void initializeView() {
		return;
	}

	public MailOrganizerView(){
		wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		VerticalPanel listWrapper = new VerticalPanel();

		listWrapper.setSize("100%", "100%");

		documentPanel = new DocumentImagePanel();
		listWrapper.add(documentPanel);
		documentPanel.setSize("100%", "100%");
		listWrapper.setCellHeight(documentPanel, "100%");
		wrapper.addWest(listWrapper, 600);

		VerticalPanel right = new VerticalPanel();
		ListHeader rightHeader = new ListHeader("Documento");

		toolbar = new MailOrganizerToolbar(){

			@Override
			protected void saveDocument() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<MailOrganizerViewPresenter.Action>(Action.CONFIRM));
			}

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<MailOrganizerViewPresenter.Action>(Action.CANCEL));				
			}

		};


		right.setSize("100%", "100%");
		form = new MailOrganizerForm();

		right.add(rightHeader);
		right.add(toolbar);
		right.add(form);
		right.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		right.setCellHeight(form, "100%");
		toolbar.setEnabled(false);
		wrapper.add(right);	
		//form.setReadOnly(true);
		toolbar.setEnabled(true);
	}

	@Override
	public void registerActionHandler(
			ActionInvokedEventHandler<Action> actionInvokedEventHandler) {
		this.actionHandler = actionInvokedEventHandler;
	}

	@Override
	public void clear() {
		form.clearInfo();
	}

	@Override
	public DocumentImagePanel getPanel(){
		return documentPanel;
	}

	@Override
	public HasEditableValue<Document> getForm() {
		return form;

	}

	@Override
	public ScanItem getSelectedScanItem() {
		return documentPanel.getCurrentItem();
	}

	@Override
	public String getScanHandle() {
		return documentPanel.getCurrentItem().handle;
	}

	@Override
	public String getDirectoryHandle() {
		if(documentPanel.getCurrentLocationItem() != null){
			return documentPanel.getCurrentLocationItem().handle;
		}
		return null;
	}
	
	@Override
	public HasNavigationStateChangedHandlers getNavigationPanel(){
		return documentPanel.getPanel();
	}
	
	@Override
	public void panelNavigateBack(){
		documentPanel.getPanel().navigateBack();
	}

	@Override
	public void setReadOnly(boolean b) {
		form.setReadOnly(b);
		toolbar.setEnabled(!b);
	}

	@Override
	public void clearNavigationPanel() {
		documentPanel.navigateToDirectoryList(null);
	}
}
