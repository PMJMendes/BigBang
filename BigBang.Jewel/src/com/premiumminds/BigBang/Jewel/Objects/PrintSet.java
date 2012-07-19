package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.TypeDefGUIDs;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBuilder;

public class PrintSet
	extends ObjectBase
{
	public static class I
	{
		public static int TEMPLATE  = 0;
		public static int DATE      = 1;
		public static int USER      = 2;
		public static int PRINTEDON = 3;
	}

    public static PrintSet GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (PrintSet)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_PrintSet), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static PrintSet GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (PrintSet)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_PrintSet), prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private User mrefUser;
	private Template mrefTemplate;

	public void Initialize()
		throws JewelEngineException
	{
		try
		{
			mrefTemplate = Template.GetInstance(getNameSpace(), (UUID)getAt(0));
		}
		catch (Throwable e)
		{
			throw new JewelEngineException(e.getMessage(), e);
		}
		mrefUser = User.GetInstance(getNameSpace(), (UUID)getAt(2));
	}

    public String AfterSave() 
    	throws JewelEngineException
    {
    	if ( (mrefUser == null) || (mrefTemplate == null) )
    		Initialize();

        return "";
    }

    public String getLabel()
    {
    	return mrefTemplate.getLabel() + " @ " + ((Timestamp)getAt(0)).toString().substring(0, 17) + " (" + mrefUser.getDisplayName() + ")";
    }

    public Template getTemplate()
    {
    	return mrefTemplate;
    }

    public User getUser()
    {
    	return mrefUser;
    }

    public PrintSetDocument[] getCurrentDocs()
    	throws BigBangJewelException
    {
		ArrayList<PrintSetDocument> larrAux;
		IEntity lrefDocuments;
        MasterDB ldb;
        ResultSet lrsDocuments;

		larrAux = new ArrayList<PrintSetDocument>();

		try
		{
			lrefDocuments = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PrintSetDocument)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsDocuments = lrefDocuments.SelectByMembers(ldb, new int[] {PrintSetDocument.I.SET},
					new java.lang.Object[] {getKey()}, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsDocuments.next() )
				larrAux.add(PrintSetDocument.GetInstance(getNameSpace(), lrsDocuments));
		}
		catch (BigBangJewelException e)
		{
			try { lrsDocuments.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw e;
		}
		catch (Throwable e)
		{
			try { lrsDocuments.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsDocuments.close();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return larrAux.toArray(new PrintSetDocument[larrAux.size()]);
    }

    public Table buildReportTable()
    	throws BigBangJewelException
    {
    	Table ltbl;
    	TR[] larrRows;

		larrRows = new TR[6];

		larrRows[0] = ReportBuilder.constructDualHeaderRowCell("Impressão de Documentos");

		larrRows[1] = ReportBuilder.constructDualRow("Tipo de Documento", getTemplate().getLabel(), TypeDefGUIDs.T_String);

		larrRows[2] = ReportBuilder.constructDualRow("Nº de Documentos", getCurrentDocs().length, TypeDefGUIDs.T_Integer);

		larrRows[3] = ReportBuilder.constructDualRow("Gerado em", getAt(I.DATE), TypeDefGUIDs.T_Date);

		larrRows[4] = ReportBuilder.constructDualRow("Gerado por", getUser().getDisplayName(), TypeDefGUIDs.T_String);

		larrRows[5] = ReportBuilder.constructDualRow("Impresso em", getAt(I.PRINTEDON), TypeDefGUIDs.T_Date);

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, false);

		return ltbl;
    }
}
