package bigBang.module.casualtyModule.client.userInterface.view;

import bigBang.definitions.shared.Assessment;
import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.ConversationStub;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.SubCasualty;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.casualtyModule.client.userInterface.HistoryAndConversationAndSubProcessChildrenPanel;
import bigBang.module.casualtyModule.client.userInterface.DefaultActionsToolbar;
import bigBang.module.casualtyModule.client.userInterface.form.AssessmentForm;
import bigBang.module.casualtyModule.client.userInterface.form.SubCasualtyForm;
import bigBang.module.casualtyModule.client.userInterface.presenter.AssessmentViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.presenter.AssessmentViewPresenter.Action;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AssessmentView extends View implements AssessmentViewPresenter.Display{

	protected AssessmentForm form;
	private DefaultActionsToolbar toolbar;
	private ActionInvokedEventHandler<Action> handler;
	private SubCasualtyForm ownerForm;
	private HistoryAndConversationAndSubProcessChildrenPanel childrenPanel;


	public AssessmentView() {
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		initWidget(mainWrapper);
		mainWrapper.setSize("100%", "100%");

		toolbar = new DefaultActionsToolbar() {

			@Override
			protected void onSendMessage() {
				handler.onActionInvoked(new ActionInvokedEvent<AssessmentViewPresenter.Action>(Action.SEND_MESSAGE));
			}

			@Override
			public void onSaveRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<AssessmentViewPresenter.Action>(Action.SAVE));
			}

			@Override
			protected void onReceiveMessage() {
				handler.onActionInvoked(new ActionInvokedEvent<AssessmentViewPresenter.Action>(Action.RECEIVE_MESSAGE));				
			}

			@Override
			public void onEditRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<AssessmentViewPresenter.Action>(Action.EDIT));
			}

			@Override
			protected void onClose() {
				handler.onActionInvoked(new ActionInvokedEvent<AssessmentViewPresenter.Action>(Action.CLOSE));

			}

			@Override
			public void onCancelRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<AssessmentViewPresenter.Action>(Action.CANCEL));

			}
		};
		
		VerticalPanel ownerWrapper = new VerticalPanel();
		ownerWrapper.setSize("100%", "100%");
		ListHeader ownerHeader = new ListHeader("Sub-Sinistro");
		ownerHeader.setHeight("30px");
		ownerHeader.setLeftWidget(new Button("Voltar", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				handler.onActionInvoked(new ActionInvokedEvent<AssessmentViewPresenter.Action>(Action.BACK));
			}
		}));
		
		ownerForm = new SubCasualtyForm();
		
		ownerForm.setReadOnly(true);
		ownerWrapper.add(ownerHeader);
		ownerWrapper.add(ownerForm);
		ownerWrapper.setCellHeight(ownerForm, "100%");
		mainWrapper.addWest(ownerWrapper, 825);
		
		SplitLayoutPanel childWrapper = new SplitLayoutPanel();
		
		VerticalPanel assessmentWrapper = new VerticalPanel();
		assessmentWrapper.setSize("100%", "100%");
		ListHeader assessmentHeader = new ListHeader("Peritagem ou Averiguação");
		
		assessmentWrapper.add(assessmentHeader);
		assessmentHeader.setHeight("30px");
		
		VerticalPanel assessmentInnerWrapper = new VerticalPanel();
		assessmentInnerWrapper.setSize("100%", "100%");
		form = new AssessmentForm();
		
		assessmentInnerWrapper.add(toolbar);
		assessmentInnerWrapper.add(form);
		
		childWrapper.setSize("100%","100%");
		
		assessmentInnerWrapper.setCellHeight(form, "100%");
		
		childrenPanel = new HistoryAndConversationAndSubProcessChildrenPanel();
		childWrapper.addEast(childrenPanel, 260);
		childWrapper.add(assessmentInnerWrapper);
		
		assessmentWrapper.add(childWrapper);
		assessmentWrapper.setCellHeight(childWrapper, "100%");
		
		mainWrapper.add(assessmentWrapper);
		
		form.validate();
		
	}

	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasEditableValue<Assessment> getForm() {
		return form;
	}

	@Override
	public HasValue<SubCasualty> getOwnerForm() {
		return ownerForm;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.handler = handler;
	}

	@Override
	public void allowEdit(boolean b) {
		toolbar.allowEdit(b);
	}

	@Override
	public void setSaveMode(boolean b) {
		toolbar.setSaveModeEnabled(b);
	}

	@Override
	public void lockToolbar() {
		toolbar.lockAll();
	}

	@Override
	public void allowSendMessage(boolean hasPermission) {
		toolbar.allowSendMessage(hasPermission);
	}

	@Override
	public void allowReceiveMessage(boolean hasPermission) {
		toolbar.allowReceiveMessage(hasPermission);
	}

	@Override
	public void allowClose(boolean hasPermission) {
		toolbar.allowClose(hasPermission);
	}

	@Override
	public void clear() {
		form.clearInfo();
		ownerForm.clearInfo();
		toolbar.lockAll();
	}

	@Override
	public void setOwner(String id) {
		childrenPanel.setOwner(id);
	}

	@Override
	public HasValueSelectables<HistoryItemStub> getHistoryList() {
		return childrenPanel.historyList;
	}

	@Override
	public HasValueSelectables<BigBangProcess> getSubProcessList(){
		return childrenPanel.subProcessesList;
	}

	@Override
	public HasValueSelectables<ConversationStub> getConversationList() {
		return childrenPanel.conversationList;
	}
}
