package bigBang.module.generalSystemModule.client.userInterface;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.OtherEntityBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.OtherEntity;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;

import com.google.gwt.event.dom.client.HasClickHandlers;

public class OtherEntityList extends FilterableList<OtherEntity> implements OtherEntityBrokerClient{

	protected ListHeader header;
	protected OtherEntityBroker broker;
	protected int otherEntityDataVersion;

	public static class OtherEntityEntry extends ListEntry<OtherEntity>{

		public OtherEntityEntry(OtherEntity value) {
			super(value);
			setHeight("40px");
		}

		@Override
		public <I extends Object> void setInfo(I infoGeneric){
			OtherEntity info = (OtherEntity) infoGeneric;

			if(info.id == null){
				setTitle("Nova Entidade");
				return;
			}

			setTitle(info.name);
			setText(info.typeLabel);
			
			setMetaData(new String[]{
					info.name,
					info.typeLabel
			});
		}


	}

	public OtherEntityList() {
		super();

		header = new ListHeader();
		header.setText("Outras Entidades");
		header.showNewButton("Novo");
		header.showRefreshButton();
		setHeaderWidget(header);
		onSizeChanged();
		showFilterField(false);
		
		broker.requireDataRefresh();
		broker.getOtherEntities(new ResponseHandler<OtherEntity[]>() {
			
			@Override
			public void onResponse(OtherEntity[] response) {
				return;
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}

	@Override
	protected void onSizeChanged(){
		int size = this.size();
		String text;
		switch(size){
		case 0:
			text = "Sem Entidades";
			break;
		case 1:
			text = "1 Entidade";
			break;
		default:
			text = size + " Entidades";
			break;
		}

		setFooterText(text);

		broker = (OtherEntityBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.OTHER_ENTITY);
		broker.registerClient(this);
	}

	public HasClickHandlers getNewButton(){
		return header.getNewButton();
	}

	public HasClickHandlers getRefreshButton(){
		return header.getRefreshButton();
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		if(dataElementId.equals(BigBangConstants.EntityIds.OTHER_ENTITY)){
			this.otherEntityDataVersion = number;
		}

	}

	@Override
	public int getDataVersion(String dataElementId) {
		if(dataElementId.equals(BigBangConstants.EntityIds.OTHER_ENTITY)){
			return this.otherEntityDataVersion;
		}
		return -1;	}

	@Override
	public void setOtherEntities(OtherEntity[] entities) {
		clear();
		for(OtherEntity entity : entities){
			add(new OtherEntityEntry(entity));
		}
	}

	@Override
	public void addOtherEntity(OtherEntity entity) {
		add(0, new OtherEntityEntry(entity));
	}

	@Override
	public void updateOtherEntity(OtherEntity entity) {
		for(ValueSelectable<OtherEntity> ent : this){
			if(entity.id.equalsIgnoreCase(ent.getValue().id)){
				ent.setValue(entity, false);
				break;
			}
		}
	}

	@Override
	public void removeOtherEntity(String entityId) {
		for(ValueSelectable<OtherEntity> ent : this){
			if(entityId.equalsIgnoreCase(ent.getValue().id)){
				remove(ent);
			}
			break;
		}		// TODO Auto-generated method stub

	}

	public void hideNewButton() {
		header.getNewButton().setVisible(false);
	}

	public void hideRefreshButton() {
		header.getRefreshButton().setVisible(false);
	}

}
