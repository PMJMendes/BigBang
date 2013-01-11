package bigBang.module.casualtyModule.client.userInterface.view;

import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.ConversationStub;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.MedicalFile;
import bigBang.definitions.shared.SubCasualty;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.casualtyModule.client.userInterface.DefaultActionsToolbar;
import bigBang.module.casualtyModule.client.userInterface.HistoryAndConversationAndSubProcessChildrenPanel;
import bigBang.module.casualtyModule.client.userInterface.form.MedicalFileForm;
import bigBang.module.casualtyModule.client.userInterface.form.SubCasualtyForm;
import bigBang.module.casualtyModule.client.userInterface.presenter.MedicalFileViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.presenter.MedicalFileViewPresenter.Action;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MedicalFileView extends View implements MedicalFileViewPresenter.Display{

	protected MedicalFileForm form;
	private DefaultActionsToolbar toolbar;
	private ActionInvokedEventHandler<Action> handler;
	private SubCasualtyForm ownerForm;
	private HistoryAndConversationAndSubProcessChildrenPanel childrenPanel;

	
	public MedicalFileView() {
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		initWidget(mainWrapper);
		mainWrapper.setSize("100%", "100%");
		
		toolbar = new DefaultActionsToolbar() {

			@Override
			protected void onSendMessage() {
				handler.onActionInvoked(new ActionInvokedEvent<Action>(Action.SEND_MESSAGE));
			}

			@Override
			public void onSaveRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<Action>(Action.SAVE));
			}

			@Override
			protected void onReceiveMessage() {
				handler.onActionInvoked(new ActionInvokedEvent<Action>(Action.RECEIVE_MESSAGE));				
			}

			@Override
			public void onEditRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<Action>(Action.EDIT));
			}

			@Override
			protected void onClose() {
				handler.onActionInvoked(new ActionInvokedEvent<Action>(Action.CLOSE));

			}

			@Override
			public void onCancelRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<Action>(Action.CANCEL));

			}
		};
		
		VerticalPanel ownerWrapper = new VerticalPanel();
		ownerWrapper.setSize("100%", "100%");
		ListHeader ownerHeader = new ListHeader("Sub-Sinistro");
		ownerHeader.setHeight("30px");
		ownerHeader.setLeftWidget(new Button("Voltar", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				handler.onActionInvoked(new ActionInvokedEvent<Action>(Action.BACK));
			}
		}));
		
		ownerForm = new SubCasualtyForm();
		
		ownerForm.setReadOnly(true);
		ownerWrapper.add(ownerHeader);
		ownerWrapper.add(ownerForm);
		ownerWrapper.setCellHeight(ownerForm, "100%");
		mainWrapper.addWest(ownerWrapper, 825);
		
		SplitLayoutPanel childWrapper = new SplitLayoutPanel();
		
		VerticalPanel medicalFileWrapper = new VerticalPanel();
		medicalFileWrapper.setSize("100%", "100%");
		ListHeader medicalFileHeader = new ListHeader("Ficha Clínica");
		
		medicalFileWrapper.add(medicalFileHeader);
		medicalFileHeader.setHeight("30px");
		
		VerticalPanel medicalFileInnerWrapper = new VerticalPanel();
		medicalFileInnerWrapper.setSize("100%", "100%");
		form = new MedicalFileForm();
		
		medicalFileInnerWrapper.add(toolbar);
		medicalFileInnerWrapper.add(form);
		
		childWrapper.setSize("100%","100%");
		
		medicalFileInnerWrapper.setCellHeight(form, "100%");
		
		childrenPanel = new HistoryAndConversationAndSubProcessChildrenPanel();
		childWrapper.addEast(childrenPanel, 260);
		childWrapper.add(medicalFileInnerWrapper);
		
		medicalFileWrapper.add(childWrapper);
		medicalFileWrapper.setCellHeight(childWrapper, "100%");
		
		mainWrapper.add(medicalFileWrapper);
		
		form.validate();
	}
	
	@Override
	protected void initializeView() {
		return;		
	}

	@Override
	public HasEditableValue<MedicalFile> getForm() {
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
	public HasValueSelectables<BigBangProcess> getSubProcessList() {
		return childrenPanel.subProcessesList;
	}

	@Override
	public HasValueSelectables<HistoryItemStub> getHistoryList() {
		return childrenPanel.historyList;
	}

	@Override
	public HasValueSelectables<ConversationStub> getConversationList() {
		return childrenPanel.conversationList;
	}

}
