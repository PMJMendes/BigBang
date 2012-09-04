package bigBang.library.client.userInterface.view;


import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.presenter.ManagerTransferViewPresenter;
import bigBang.library.client.userInterface.presenter.ManagerTransferViewPresenter.Action;

import com.google.gwt.user.client.ui.VerticalPanel;


public class ManagerTransferView extends View implements ManagerTransferViewPresenter.Display{

	ManagerTransferForm form;
	private FilterableList<Object> list;
	protected VerticalPanel wrapper;

	public ManagerTransferView(){

		this.wrapper = new VerticalPanel();
		initWidget(wrapper);
		list = new FilterableList<Object>(); 
		list.showFilterField(false);
		form = new ManagerTransferForm();

		wrapper.setSize("100%", "100%");
		wrapper.add(form.getNonScrollableContent());
		wrapper.add(new FormViewSection("Processos afectados").getHeader());
		wrapper.add(list);
		wrapper.setCellHeight(list, "100%");
	}

	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public ManagerTransferForm getForm() {
		return this.form;
	}

	public HasValueSelectables<Object> getList(){
		return this.list;
	}

	@Override
	public void clearList() {
		this.list.clear();
	}

	@Override
	public void setObjectType(String type) {
		form.setObjectType(type);
	}

	@Override
	public void addToList(ListEntry<Object> selectable) {
		this.list.add((ListEntry<Object>) selectable);
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		return;
	}

	@Override
	public void allowAccept(boolean allow) {
		return;
	}

	@Override
	public void allowCancel(boolean allow) {
		return;
	}

}
