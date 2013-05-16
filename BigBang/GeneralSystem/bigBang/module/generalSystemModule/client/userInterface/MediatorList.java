package bigBang.module.generalSystemModule.client.userInterface;

import java.util.Collection;

import com.google.gwt.event.dom.client.HasClickHandlers;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.MediatorBroker;
import bigBang.definitions.client.dataAccess.MediatorDataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Mediator;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListHeader;

public class MediatorList extends FilterableList<Mediator> implements MediatorDataBrokerClient {

	protected ListHeader header;
	protected MediatorBroker broker;
	protected int mediatorDataVersion;
	
	public MediatorList() {
		super();
		header = new ListHeader();
		header.setText("Mediadores");
		header.showNewButton("Novo");
		header.showRefreshButton();
		setHeaderWidget(header);
		onSizeChanged();
		showFilterField(false);
		
		broker = (MediatorBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.MEDIATOR); 
		broker.registerClient(this);
	}
	
	@Override
	public void updateFooterText() {
		int size = size();
		String text = new String();
		if(size == 0)
			text = "Sem mediadores";
		if(size == 1)
			text = "1 mediador";
		if(size > 1)
			text = size + " mediadores";
		
		setFooterText(text);
	}
	
	public HasClickHandlers getNewButton(){
		return header.getNewButton();
	}
	
	public HasClickHandlers getRefreshButton(){
		return header.getRefreshButton();
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		if(dataElementId.equals(BigBangConstants.EntityIds.MEDIATOR)){
			this.mediatorDataVersion = number;
		}
	}

	@Override
	public int getDataVersion(String dataElementId) {
		if(dataElementId.equals(BigBangConstants.EntityIds.MEDIATOR)){
			return this.mediatorDataVersion;
		}
		return -1;
	}

	@Override
	public void setMediators(Mediator[] mediators) {
		clear();
		for(int i = 0; i < mediators.length; i++) {
			add(new MediatorListEntry(mediators[i]));
		}
	}

	@Override
	public void addMediator(Mediator mediator) {
		add(0, new MediatorListEntry(mediator));
	}

	@Override
	public void updateMediator(Mediator mediator) {
		for(ValueSelectable<Mediator> vs : this) {
			if(mediator.id.equals(vs.getValue().id)){
				vs.setValue(mediator, false);
				break;
			}
		}
	}

	@Override
	public void removeMediator(String mediatorId) {
		for(ValueSelectable<Mediator> vs : this) {
			if(mediatorId.equals(vs.getValue().id)){
				remove(vs);
				break;
			}
		}
	}
	
	@Override
	protected void onAttach() {
		super.onAttach();
		broker.requireDataRefresh();
		broker.getMediators(new ResponseHandler<Mediator[]>() {
			
			@Override
			public void onResponse(Mediator[] response) {
				return;
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}

}
