package bigBang.module.generalSystemModule.client.userInterface.view;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.tools.ant.taskdefs.condition.IsReference;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.MessageBox.ConfirmationCallback;

import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.client.userInterface.CostCenterList;
import bigBang.module.generalSystemModule.client.userInterface.CostCenterListEntry;
import bigBang.module.generalSystemModule.client.userInterface.CostCenterMemberList;
import bigBang.module.generalSystemModule.client.userInterface.CostCenterMemberListEntry;
import bigBang.module.generalSystemModule.client.userInterface.UserList;
import bigBang.module.generalSystemModule.client.userInterface.UserListEntry;
import bigBang.module.generalSystemModule.client.userInterface.presenter.CostCenterManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.shared.CostCenter;
import bigBang.module.generalSystemModule.shared.User;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CostCenterManagementOperationView extends View implements CostCenterManagementOperationViewPresenter.Display {

	private static final int LIST_WIDTH = 400; //px

	private CostCenterList costCenterList;
	private CostCenterMemberList memberList;
	private CostCenterForm form;

	private Button addMemberSubmitButton;
	private PopupPanel availableUsersPopup;

	private UserList usersForMembershipList;
	private Button newCostCenterButton;
	private PopupPanel newCostCenterPopup;
	private Button submitNewCostCenterButton;
	private CostCenterForm newCostCenterForm;

	public CostCenterManagementOperationView() {
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		wrapper.setSize("100%", "100%");

		costCenterList = new CostCenterList();
		costCenterList.setSize("100%", "100%");
		wrapper.addWest(costCenterList, LIST_WIDTH);
		wrapper.setWidgetMinSize(costCenterList, LIST_WIDTH);

		VerticalPanel previewWrapper = new VerticalPanel();
		previewWrapper.setSize("100%", "100%");

		this.newCostCenterButton = new Button("Novo");
		this.submitNewCostCenterButton = new Button("Submeter");
		Button editCostCenterButton = new Button("Editar");		
		
		form = new CostCenterForm();
		form.addButton(this.newCostCenterButton);
		form.addButton(editCostCenterButton);
		
		newCostCenterForm = new CostCenterForm();
		newCostCenterForm.addButton(this.submitNewCostCenterButton);
		newCostCenterForm.setReadOnly(false);

		previewWrapper.add(form.getNonScrollableContent());

		memberList = new CostCenterMemberList();
		previewWrapper.add(memberList);

		previewWrapper.setCellHeight(memberList, "100%");

		wrapper.add(previewWrapper);

		initWidget(wrapper);

		editCostCenterButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				form.setReadOnly(!form.isReadOnly());
			}
		});
		
		newCostCenterPopup = new PopupPanel();
		newCostCenterPopup.add(newCostCenterForm.getNonScrollableContent());
		
		this.addMemberSubmitButton = new Button("Associar Membro(s)");
		this.availableUsersPopup = new PopupPanel("Associar membros a Centro de Custo");
	}

	public HasValue<CostCenter> getCostCenterList() {
		return this.costCenterList;
	}

	public void showDetailsForCostCenter(CostCenter costCenter) {
		getName().setValue(costCenter.name);
		getCode().setValue(costCenter.code);
		memberList.clear();
		for(int i = 0; i < costCenter.members.length; i++){
			memberList.addListEntry(new CostCenterMemberListEntry(costCenter.members[i]));
		}
	}

	public void setCostCenterEntries(CostCenter[] entries) {
		costCenterList.clear();
		for(int i = 0; i < entries.length; i++) {
			CostCenterListEntry listEntry = new CostCenterListEntry(entries[i]);
			costCenterList.addListEntry(listEntry);
		}
		if(this.costCenterList.getSelectedEntry() == null && this.costCenterList.size() > 0)
			this.costCenterList.select(0);
	}

	public HasValue<String> getName() {
		return form.getNameField();
	}

	public HasValue<String> getCode() {
		return form.getCodeField();
	}

	public String[] getSelectedMemberIds() {
		ArrayList<ListEntry<User>> entries = this.memberList.getMultipleSelectionEntries();
		String[] result = new String[entries.size()];
		for(int i = 0; i < result.length; i++){
			result[i] = entries.get(i).getValue().id;
		}
		return result;
	}

	public HasClickHandlers getAddMemberButton() {
		return this.memberList.getAddButton();
	}

	public HasClickHandlers getremoveMemberButton() {
		return this.memberList.getRemoveButton();
	}

	public void removeMembers(CostCenter costCenter, String[] memberIds) {
		Collection<ListEntry<User>> entries = memberList.getListEntries();

		for(int i = 0; i < memberIds.length; i++){
			for(ListEntry<User> e : entries) {
				if(e.getValue().id.equals(memberIds[i])){
					memberList.removeListEntry(e);
					costCenterList.getSelectedEntry().setValue(costCenter);
					break;
				}
			}
		}

	}
	
	public HasClickHandlers getAddMemberSubmitButton(){
		return this.addMemberSubmitButton;
	}

	public void showUsersForMembership(String costCenterId, final User[] availableUsers) {
		availableUsersPopup.clear();
		HorizontalPanel wrapper = new HorizontalPanel();
		wrapper.setSize("100%", "500px");
		//popup.setSize("400px", "400px");
		UserList list = new UserList();
		this.usersForMembershipList = list;
		list.setHeaderText("Utilizadores");
		list.setWidth("300px");
		
		for(int i = 0; i < availableUsers.length; i++) {
			list.addListEntry(new UserListEntry(availableUsers[i]));
		}
		list.setCheckable(true);
		
		wrapper.add(list);
		
		final UserForm form = new UserForm(null, null);
		addMemberSubmitButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				availableUsersPopup.hidePopup();
			}
		});
		form.addSubmitButton(this.addMemberSubmitButton);
		wrapper.add(form);
		wrapper.setCellWidth(form, "600px");
		
		list.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			public void onValueChange(ValueChangeEvent<String> event) {
				for(int i = 0; i < availableUsers.length; i++) {
					if(availableUsers[i].id.equals(event.getValue())){
						form.setUser(availableUsers[i]);
						break;
					}
				}
			}
		});
		
		if(list.size() > 0)
			list.select(0);
		
		availableUsersPopup.add(wrapper);
		availableUsersPopup.center();
	}
	
	public String[] getSelectedUsersForMembership(){
		ArrayList<ListEntry<String>> entries =this.usersForMembershipList.getCheckedEntries();
		int size = entries.size();
		String[] result = new String[size];
		
		for(int i = 0; i < size; i++) {
			result[i] = entries.get(i).getValue();
		}
		
		return result;
	}
	
	public void showConfirmRemoveMember(ConfirmationCallback callback){
		MessageBox.confirm("Confirmar remoção", "Tem certeza que pretende remover o(s) membro(s) seleccionado(s)?", callback);
	}

	public void updateCostCenterInfo(CostCenter result) {
		for(ListEntry<CostCenter> e : this.costCenterList.getListEntries()){
			if(e.getValue().equals(result.id)){
				e.setInfo(result);
				if(this.costCenterList.getSelectedEntry() == e)
					this.showDetailsForCostCenter(result);
				break;
			}
		}
	}

	@Override
	public HasClickHandlers getNewCostCenterButton() {
		return this.newCostCenterButton;
	}

	@Override
	public HasClickHandlers getSubmitNewCostCenterButton() {
		return this.submitNewCostCenterButton;
	}

	@Override
	public CostCenter getNewCostCenter() {
		return (CostCenter) this.newCostCenterForm.getInfo();
	}

	@Override
	public void showNewCostCenterForm(boolean show) {
		if(show){
			this.newCostCenterForm.clearInfo();
			this.newCostCenterPopup.setSize("650px", "200px");
			this.newCostCenterPopup.center();
		}else
			this.newCostCenterPopup.hidePopup();
	}

	@Override
	public boolean isNewCostCenterFormValid() {
		return this.newCostCenterForm.validate();
	}

}
