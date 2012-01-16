package bigBang.library.client.userInterface.view;


import bigBang.definitions.shared.SearchResult;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.presenter.ManagerTransferViewPresenter;
import bigBang.library.client.userInterface.presenter.ManagerTransferViewPresenter.BarStatus;

import com.google.gwt.user.client.ui.VerticalPanel;


public class ManagerTransferView extends View implements ManagerTransferViewPresenter.Display{

	public class ObjectList extends FilterableList<SearchResult>{

		public ObjectList(){

			super();
		}

	}

	ManagerTransferForm form;
	private ObjectList list;
	protected VerticalPanel wrapper;

	public ManagerTransferView(){
		
		this.wrapper = new VerticalPanel();
		initWidget(wrapper);
		list = new ObjectList(); 
		list.setTitle("Objectos");
		form = new ManagerTransferForm();
		//wrapper.setSpacing(10);
		wrapper.setSize("100%", "100%");
		wrapper.add(form.getNonScrollableContent());
		wrapper.add(new FormViewSection("Objectos").getHeader());
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

	@Override
	public void addObject(ListEntry<SearchResult> object) {
		
		this.list.add(object);
		
		
		
	}
	
	public HasValueSelectables<SearchResult> getList(){
		
		return this.list;
		
	}

	@Override
	public void setToolBarState(BarStatus status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearList() {
		this.list.clear();
		
	}
}
