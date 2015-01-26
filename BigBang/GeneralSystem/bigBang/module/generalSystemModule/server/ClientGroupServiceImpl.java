package bigBang.module.generalSystemModule.server;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import bigBang.definitions.shared.ClientGroup;
import bigBang.library.server.EngineImplementor;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.generalSystemModule.interfaces.ClientGroupService;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.GeneralSystem;
import com.premiumminds.BigBang.Jewel.Operations.General.ManageClientGroups;

public class ClientGroupServiceImpl
	extends EngineImplementor
	implements ClientGroupService
{
	private static final long serialVersionUID = 1L;

	public ClientGroup[] getClientGroupList()
		throws SessionExpiredException, BigBangException
	{
		ArrayList<ClientGroup> larrAux;
		Entity lrefGroups;
        MasterDB ldb;
        ResultSet lrsGroups;
		com.premiumminds.BigBang.Jewel.Objects.ClientGroup lobjGroup;
		ClientGroup lobjTmp;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new ArrayList<ClientGroup>();

		try
		{
			lrefGroups = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ClientGroup));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

        try
        {
	        lrsGroups = lrefGroups.SelectAllSort(ldb, new int[] {0});
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
	        while (lrsGroups.next())
	        {
	        	lobjGroup = com.premiumminds.BigBang.Jewel.Objects.ClientGroup.GetInstance(Engine.getCurrentNameSpace(), lrsGroups);
	        	lobjTmp = new ClientGroup();
	        	lobjTmp.id = lobjGroup.getKey().toString();
	        	lobjTmp.name = (String)lobjGroup.getAt(0);
	        	lobjTmp.parentGroupId = (lobjGroup.getAt(1) == null ? null : ((UUID)lobjGroup.getAt(1)).toString());
	        	lobjTmp.mediatorId = (lobjGroup.getAt(3) == null ? null : ((UUID)lobjGroup.getAt(3)).toString());
//	        	lobjTmp.subGroups = getSubGroupsForGroup(lobjGroup);
	        	lobjTmp.subGroups = null;
	        	larrAux.add(lobjTmp);
	        }
        }
/*		catch (BigBangException e)
        {
			try { lrsGroups.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
        	throw e;
        } */
        catch (Throwable e)
        {
			try { lrsGroups.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
        	throw new BigBangException(e.getMessage(), e);
        }

        try
        {
        	lrsGroups.close();
        }
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return larrAux.toArray(new ClientGroup[larrAux.size()]);
	}

	public ClientGroup createClientGroup(ClientGroup clientGroup)
		throws SessionExpiredException, BigBangException
	{
		ClientGroup[] larrAux;
		ManageClientGroups lopMCG;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new ClientGroup[1];
		larrAux[0] = clientGroup;

		try
		{
			lopMCG = new ManageClientGroups(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());

			lopMCG.marrCreateGroups = BuildGroupArray(lopMCG, larrAux, null, true);

			lopMCG.marrModifyGroups = null;
			lopMCG.marrDeleteGroups = null;

			lopMCG.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		TagGroups(lopMCG.marrCreateGroups, larrAux);

		return clientGroup;
	}

	public ClientGroup saveClientGroup(ClientGroup clientGroup)
		throws SessionExpiredException, BigBangException
	{
		ClientGroup[] larrAux;
		ManageClientGroups lopMCG;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new ClientGroup[1];
		larrAux[0] = clientGroup;

		try
		{
			lopMCG = new ManageClientGroups(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());

			lopMCG.marrModifyGroups = BuildGroupArray(lopMCG, larrAux, null, false);

			lopMCG.marrCreateGroups = null;
			lopMCG.marrDeleteGroups = null;

			lopMCG.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return clientGroup;
	}

	public void deleteClientGroup(String id)
		throws SessionExpiredException, BigBangException
	{
		ClientGroup[] larrAux;
		ManageClientGroups lopMCG;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new ClientGroup[1];
		larrAux[0] = new ClientGroup();
		larrAux[0].id = id;

		try
		{
			lopMCG = new ManageClientGroups(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());

			lopMCG.marrDeleteGroups = BuildGroupArray(lopMCG, larrAux, null, false);

			lopMCG.marrCreateGroups = null;
			lopMCG.marrModifyGroups = null;

			lopMCG.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

/*	private ClientGroup[] getSubGroupsForGroup(com.premiumminds.BigBang.Jewel.Objects.ClientGroup pobjGroup)
		throws BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.ClientGroup[] larrSubGroups;
		ClientGroup[] larrResult;
		int i;

		try
		{
			larrSubGroups = pobjGroup.GetCurrentSubGroups();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		if ( larrSubGroups == null )
			return new ClientGroup[0];

		larrResult = new ClientGroup[larrSubGroups.length];
		for ( i = 0; i < larrSubGroups.length; i++ )
		{
			larrResult[i] = new ClientGroup();
			larrResult[i].id = larrSubGroups[i].getKey().toString();
			larrResult[i].name = (String)larrSubGroups[i].getAt(0);
			larrResult[i].parentGroupId = ((UUID)larrSubGroups[i].getAt(1)).toString();
	        larrResult[i].mediatorId = (larrSubGroups[i].getAt(3) == null ? null : ((UUID)larrSubGroups[i].getAt(3)).toString());
			larrResult[i].subGroups = getSubGroupsForGroup(larrSubGroups[i]);
		}

		return larrResult;
	} */

	private ManageClientGroups.GroupData[] BuildGroupArray(ManageClientGroups prefOp, ClientGroup[] parrGroups,
			UUID pidParent, boolean pbRecurse)
	{
		ManageClientGroups.GroupData[] larrResult;
		int i;

		larrResult = new ManageClientGroups.GroupData[parrGroups.length];
		for ( i = 0; i < parrGroups.length; i++ )
		{
			larrResult[i] = prefOp.new GroupData();
			larrResult[i].mid = (parrGroups[i].id == null ? null : UUID.fromString(parrGroups[i].id));
			larrResult[i].mstrName = parrGroups[i].name;
			larrResult[i].midParent = (pidParent == null ?
					(parrGroups[i].parentGroupId == null ? null : UUID.fromString(parrGroups[i].parentGroupId)) : pidParent);
			larrResult[i].midMediator = (parrGroups[i].mediatorId == null ? null : UUID.fromString(parrGroups[i].mediatorId));
			larrResult[i].marrSubGroups = (pbRecurse && parrGroups[i].subGroups != null ?
					BuildGroupArray(prefOp, parrGroups[i].subGroups, larrResult[i].mid, pbRecurse) : null);
			larrResult[i].mobjPrevValues = null;
		}

		return larrResult;
	}

	private void TagGroups(ManageClientGroups.GroupData[] parrSource, ClientGroup[] parrGroups)
	{
		int i;

		for ( i = 0; i < parrSource.length; i++ )
		{
			parrGroups[i].id = parrSource[i].mid.toString();
			if ( (parrSource[i].marrSubGroups != null) && (parrGroups[i].subGroups != null) )
				TagGroups(parrSource[i].marrSubGroups, parrGroups[i].subGroups);
		}
	}
}
