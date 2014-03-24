package bigBang.library.server;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import bigBang.definitions.shared.TypifiedText;
import bigBang.library.interfaces.TypifiedTextService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.premiumminds.BigBang.Jewel.Constants;

public class TypifiedTextServiceImpl
	extends EngineImplementor
	implements TypifiedTextService
{
	private static final long serialVersionUID = 1L;

	public TypifiedText[] getTexts(String tag)
		throws SessionExpiredException, BigBangException
	{
        MasterDB ldb;
        ResultSet lrsTexts;
		ArrayList<TypifiedText> larrAux;
		com.premiumminds.BigBang.Jewel.Objects.TypifiedText lobjItem;
		TypifiedText lobjAux;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new ArrayList<TypifiedText>();

		try
		{
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

        try
        {
	        lrsTexts = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
	        		Constants.ObjID_TypifiedText)).SelectByMembers(ldb, new int[] {1}, new java.lang.Object[] {tag}, new int[] {0});
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
	        while (lrsTexts.next())
	        {
	        	lobjItem = com.premiumminds.BigBang.Jewel.Objects.TypifiedText.GetInstance(Engine.getCurrentNameSpace(), lrsTexts);
	        	lobjAux = new TypifiedText();
	        	lobjAux.id = lobjItem.getKey().toString();
	        	lobjAux.tag = (String)lobjItem.getAt(1);
	        	lobjAux.label = lobjItem.getLabel();
	        	lobjAux.subject = (String)lobjItem.getAt(2);
	        	lobjAux.text = lobjItem.getText();
	        	larrAux.add(lobjAux);
	        }
        }
        catch (Throwable e)
        {
			try { lrsTexts.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
        	throw new BigBangException(e.getMessage(), e);
        }

        try
        {
        	lrsTexts.close();
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

		return larrAux.toArray(new TypifiedText[larrAux.size()]);
	}

	public TypifiedText createText(TypifiedText text)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.TypifiedText lobjAux;
        MasterDB ldb;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjAux = com.premiumminds.BigBang.Jewel.Objects.TypifiedText.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjAux.setAt(0, text.label);
			lobjAux.setAt(1, text.tag);
			lobjAux.setAt(2, text.subject);
			lobjAux.setText(text.text);
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lobjAux.SaveToDb(ldb);
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

		text.id = lobjAux.getKey().toString();
		return text;
	}

	public TypifiedText saveText(TypifiedText text)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.TypifiedText lobjAux;
        MasterDB ldb;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjAux = com.premiumminds.BigBang.Jewel.Objects.TypifiedText.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(text.id));
			lobjAux.setAt(0, text.label);
			lobjAux.setAt(1, text.tag);
			lobjAux.setAt(2, text.subject);
			lobjAux.setText(text.text);
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lobjAux.SaveToDb(ldb);
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

		return text;
	}

	public void deleteText(String textId)
		throws SessionExpiredException, BigBangException
	{
		IEntity lrefList;
        MasterDB ldb;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lrefList = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_TypifiedText));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lrefList.Delete(ldb, UUID.fromString(textId));
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
	}
}
