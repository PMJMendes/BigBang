package bigBang.library.server;

import java.sql.ResultSet;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import bigBang.definitions.shared.ZipCode;
import bigBang.library.interfaces.ZipCodeService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.premiumminds.BigBang.Jewel.Constants;

public class ZipCodeServiceImpl
	extends EngineImplementor
	implements ZipCodeService
{
	private static final long serialVersionUID = 1L;

	public ZipCode getZipCode(String code)
		throws SessionExpiredException, BigBangException
	{
		ObjectBase lobjAux;
		UUID lidZipCodes;
		IEntity lrefZipCodes;
		MasterDB ldb;
		ResultSet lrsCodes;
		ZipCode lobjResult;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lobjAux = null;
		try
		{
			lidZipCodes = Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_PostalCode);
			lrefZipCodes = Entity.GetInstance(lidZipCodes);
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lrsCodes = lrefZipCodes.SelectByMembers(ldb, new int[] {Constants.ZipCode_In_PostalCode},
					new java.lang.Object[] {"!" + code}, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			if ( lrsCodes.next() )
				lobjAux = Engine.GetWorkInstance(lidZipCodes, lrsCodes);
		}
		catch (Throwable e)
		{
			try { lrsCodes.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		if ( lobjAux == null )
			return null;

		lobjResult = new ZipCode();
		lobjResult.code = (String)lobjAux.getAt(0);
		lobjResult.city = (String)lobjAux.getAt(1);
		lobjResult.county = (String)lobjAux.getAt(2);
		lobjResult.district = (String)lobjAux.getAt(3);
		lobjResult.country = (String)lobjAux.getAt(4);

		return lobjResult;
	}
}
