package bigBang.module.complaintModule.client.userInterface;

import bigBang.library.client.userInterface.view.SearchPanel;
import bigBang.library.shared.SearchResult;
import bigBang.module.complaintModule.interfaces.ComplaintService;

public class ComplaintSearchPanel extends SearchPanel {

	public ComplaintSearchPanel(){
		super(ComplaintService.Util.getInstance());
	}
	
	@Override
	public void doSearch() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResults(SearchResult[] results) {
		// TODO Auto-generated method stub

	}

}
