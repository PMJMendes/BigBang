package bigBang.module.generalSystemModule.client.userInterface;

import bigBang.definitions.shared.OtherEntity;
import bigBang.library.client.userInterface.ListEntry;

public class OtherEntityListEntry
	extends ListEntry<OtherEntity>
{
	public OtherEntityListEntry(OtherEntity value)
	{
		super(value);
		setHeight("40px");
	}

	@Override
	public <I extends Object> void setInfo(I infoGeneric)
	{
		OtherEntity info = (OtherEntity) infoGeneric;

		if(info.id == null)
		{
			setTitle("Nova Entidade");
			return;
		}

		setTitle(info.name);
		setText(info.typeLabel);
		
		setMetaData(new String[] { info.name, info.typeLabel });
	}
}
