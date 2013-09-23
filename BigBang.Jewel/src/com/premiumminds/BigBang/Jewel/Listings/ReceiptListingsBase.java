package com.premiumminds.BigBang.Jewel.Listings;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.TypeDefGUIDs;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.ILog;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Casualty;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualty;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.Payment;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBuilder;

public class ReceiptListingsBase
{
	protected static class ReceiptMarkers
	{
		public Timestamp mdtReceived;
		public Timestamp mdtAutoVal;
		public Timestamp mdtValidated;
		public Timestamp mdtSent;
		public Timestamp mdtPayed;
		public Timestamp mdtAccountedI;
		public Timestamp mdtAccountedM;
	}

	protected HashMap<UUID, ReceiptMarkers> mmapData;

	protected Table buildHeaderSection(String pstrHeader, Receipt[] parrReceipts, int plngMapSize)
	{
		BigDecimal ldblTotal;
		BigDecimal ldblTotalCom;
		BigDecimal ldblTotalRetro;
		int i;
		Table ltbl;
		TR[] larrRows;

		ldblTotal = BigDecimal.ZERO;
		ldblTotalCom = BigDecimal.ZERO;
		ldblTotalRetro = BigDecimal.ZERO;
		for ( i = 0; i < parrReceipts.length; i++ )
		{
			ldblTotal = ldblTotal.add((parrReceipts[i].getAt(Receipt.I.TOTALPREMIUM) == null ? BigDecimal.ZERO :
					(BigDecimal)parrReceipts[i].getAt(Receipt.I.TOTALPREMIUM)));
			ldblTotalCom = ldblTotalCom.add((parrReceipts[i].getAt(Receipt.I.COMMISSIONS) == null ? BigDecimal.ZERO :
					(BigDecimal)parrReceipts[i].getAt(Receipt.I.COMMISSIONS)));
			ldblTotalRetro = ldblTotalRetro.add((parrReceipts[i].getAt(Receipt.I.RETROCESSIONS) == null ? BigDecimal.ZERO :
					(BigDecimal)parrReceipts[i].getAt(Receipt.I.RETROCESSIONS)));
		}

		larrRows = new TR[6];

		larrRows[0] = ReportBuilder.constructDualHeaderRowCell(pstrHeader);

		larrRows[1] = ReportBuilder.constructDualRow("Nº de Gestores", plngMapSize, TypeDefGUIDs.T_Integer, false);

		larrRows[2] = ReportBuilder.constructDualRow("Nº de Recibos", parrReceipts.length, TypeDefGUIDs.T_Integer, false);

		larrRows[3] = ReportBuilder.constructDualRow("Total de Prémios", ldblTotal, TypeDefGUIDs.T_Decimal, false);

		larrRows[4] = ReportBuilder.constructDualRow("Total de Comissões", ldblTotalCom, TypeDefGUIDs.T_Decimal, false);

		larrRows[5] = ReportBuilder.constructDualRow("Total de Retrocessões", ldblTotalRetro, TypeDefGUIDs.T_Decimal, false);

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, false);

		return ltbl;
	}

	protected Table buildDataSection(String pstrHeader, ArrayList<Receipt> parrReceipts)
		throws BigBangJewelException
	{
		return buildDataSection(pstrHeader, parrReceipts.toArray(new Receipt[parrReceipts.size()]));
	}

	protected Table buildDataSection(String pstrHeader, Receipt[] parrReceipts)
		throws BigBangJewelException
	{
		Table ltbl;
		TR[] larrRows;

		larrRows = buildDataTable(pstrHeader, parrReceipts);

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, false);

		return ltbl;
	}

	protected TR[] buildDataTable(String pstrHeader, Receipt[] parrReceipts)
		throws BigBangJewelException
	{
		BigDecimal ldblTotal;
		BigDecimal ldblTotalCom;
		BigDecimal ldblTotalRetro;
		int i;
		TR[] larrRows;
		TD lcell;

		ldblTotal = BigDecimal.ZERO;
		ldblTotalCom = BigDecimal.ZERO;
		ldblTotalRetro = BigDecimal.ZERO;
		for ( i = 0; i < parrReceipts.length; i++ )
		{
			ldblTotal = ldblTotal.add((parrReceipts[i].getAt(Receipt.I.TOTALPREMIUM) == null ? BigDecimal.ZERO :
					(BigDecimal)parrReceipts[i].getAt(Receipt.I.TOTALPREMIUM)));
			ldblTotalCom = ldblTotalCom.add((parrReceipts[i].getAt(Receipt.I.COMMISSIONS) == null ? BigDecimal.ZERO :
					(BigDecimal)parrReceipts[i].getAt(Receipt.I.COMMISSIONS)));
			ldblTotalRetro = ldblTotalRetro.add((parrReceipts[i].getAt(Receipt.I.RETROCESSIONS) == null ? BigDecimal.ZERO :
					(BigDecimal)parrReceipts[i].getAt(Receipt.I.RETROCESSIONS)));
		}

		larrRows = new TR[6];

		larrRows[0] = ReportBuilder.constructDualHeaderRowCell(pstrHeader);

		lcell = new TD();
		lcell.setColSpan(2);
		lcell.addElement(buildInner(parrReceipts));
		ReportBuilder.styleInnerContainer(lcell);
		larrRows[1] = ReportBuilder.buildRow(new TD[] {lcell});

		larrRows[2] = ReportBuilder.constructDualRow("Nº de Recibos", parrReceipts.length, TypeDefGUIDs.T_Integer, false);

		larrRows[3] = ReportBuilder.constructDualRow("Total de Prémios", ldblTotal, TypeDefGUIDs.T_Decimal, false);

		larrRows[4] = ReportBuilder.constructDualRow("Total de Comissões", ldblTotalCom, TypeDefGUIDs.T_Decimal, false);

		larrRows[5] = ReportBuilder.constructDualRow("Total de Retrocessões", ldblTotalRetro, TypeDefGUIDs.T_Decimal, false);

		return larrRows;
	}

	protected Table buildInner(Receipt[] parrReceipts)
		throws BigBangJewelException
	{
		Table ltbl;
		TR[] larrRows;
		int i;

		larrRows = new TR[parrReceipts.length + 1];

		larrRows[0] = ReportBuilder.buildRow(buildInnerHeaderRow());
		ReportBuilder.styleRow(larrRows[0], true);

		for ( i = 1; i <= parrReceipts.length; i++ )
		{
			larrRows[i] = ReportBuilder.buildRow(buildRow(parrReceipts[i - 1]));
			ReportBuilder.styleRow(larrRows[i], false);
		}

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, true);

		return ltbl;
	}

	protected TD[] buildInnerHeaderRow()
	{
		TD[] larrCells;

		larrCells = new TD[17];

		larrCells[0] = ReportBuilder.buildHeaderCell("Recibo");
		ReportBuilder.styleCell(larrCells[0], false, false);

		larrCells[1] = ReportBuilder.buildHeaderCell("T");
		ReportBuilder.styleCell(larrCells[1], false, true);

		larrCells[2] = ReportBuilder.buildHeaderCell("Cliente");
		ReportBuilder.styleCell(larrCells[2], false, true);

		larrCells[3] = ReportBuilder.buildHeaderCell("Apólice");
		ReportBuilder.styleCell(larrCells[3], false, true);

		larrCells[4] = ReportBuilder.buildHeaderCell("Comp");
		ReportBuilder.styleCell(larrCells[4], false, true);

		larrCells[5] = ReportBuilder.buildHeaderCell("Ramo");
		ReportBuilder.styleCell(larrCells[5], false, true);

		larrCells[6] = ReportBuilder.buildHeaderCell("Descrição");
		ReportBuilder.styleCell(larrCells[6], false, true);

		larrCells[7] = ReportBuilder.buildHeaderCell("Prémio");
		ReportBuilder.styleCell(larrCells[7], false, true);

		larrCells[8] = ReportBuilder.buildHeaderCell("Comissão");
		ReportBuilder.styleCell(larrCells[8], false, true);

		larrCells[9] = ReportBuilder.buildHeaderCell("Retrocessão");
		ReportBuilder.styleCell(larrCells[9], false, true);

		larrCells[10] = ReportBuilder.buildHeaderCell("Emissão");
		ReportBuilder.styleCell(larrCells[10], false, true);

		larrCells[11] = ReportBuilder.buildHeaderCell("Vencimento");
		ReportBuilder.styleCell(larrCells[11], false, true);

		larrCells[12] = ReportBuilder.buildHeaderCell("Até");
		ReportBuilder.styleCell(larrCells[12], false, true);

		larrCells[13] = ReportBuilder.buildHeaderCell("Data Limite");
		ReportBuilder.styleCell(larrCells[13], false, true);

		larrCells[14] = ReportBuilder.buildHeaderCell("Data Cobrança");
		ReportBuilder.styleCell(larrCells[14], false, true);

		larrCells[15] = ReportBuilder.buildHeaderCell("Meios");
		ReportBuilder.styleCell(larrCells[15], false, true);

		larrCells[16] = ReportBuilder.buildHeaderCell("Perfil");
		ReportBuilder.styleCell(larrCells[16], false, true);

		setWidths(larrCells);

		return larrCells;
	}

	protected TD[] buildRow(Receipt pobjReceipt)
		throws BigBangJewelException
	{
		Policy lobjPolicy;
		Client lobjClient;
		ILog lobjLog;
		ObjectBase lobjProfile;
		TD[] larrCells;

		lobjPolicy = pobjReceipt.getAbsolutePolicy();
		lobjClient = lobjPolicy.GetClient();

		lobjLog = pobjReceipt.getPaymentLog();

		try
		{
			lobjProfile = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_OpProfile), pobjReceipt.getProfile());
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrCells = new TD[17];

		larrCells[0] = ReportBuilder.buildCell(pobjReceipt.getAt(Receipt.I.NUMBER), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[0], true, false);

		larrCells[1] = ReportBuilder.buildCell(pobjReceipt.getReceiptType(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[1], true, true);

		larrCells[2] = ReportBuilder.buildCell(lobjClient.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[2], true, true);

		larrCells[3] = ReportBuilder.buildCell(lobjPolicy.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[3], true, true);

		larrCells[4] = ReportBuilder.buildCell(lobjPolicy.GetCompany().getAt(1), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[4], true, true);

		larrCells[5] = ReportBuilder.buildCell(lobjPolicy.GetSubLine().getDescription(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[5], true, true);

		larrCells[6] = ReportBuilder.buildCell(pobjReceipt.getAt(Receipt.I.DESCRIPTION), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[6], true, true);

		larrCells[7] = ReportBuilder.buildCell(pobjReceipt.getAt(Receipt.I.TOTALPREMIUM), TypeDefGUIDs.T_Decimal, true);
		ReportBuilder.styleCell(larrCells[7], true, true);

		larrCells[8] = ReportBuilder.buildCell(pobjReceipt.getAt(Receipt.I.COMMISSIONS), TypeDefGUIDs.T_Decimal, true);
		ReportBuilder.styleCell(larrCells[8], true, true);

		larrCells[9] = ReportBuilder.buildCell(pobjReceipt.getAt(Receipt.I.RETROCESSIONS), TypeDefGUIDs.T_Decimal, true);
		ReportBuilder.styleCell(larrCells[9], true, true);

		larrCells[10] = ReportBuilder.buildCell(pobjReceipt.getAt(Receipt.I.ISSUEDATE), TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[10], true, true);

		larrCells[11] = ReportBuilder.buildCell(pobjReceipt.getAt(Receipt.I.MATURITYDATE), TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[11], true, true);

		larrCells[12] = ReportBuilder.buildCell(pobjReceipt.getAt(Receipt.I.ENDDATE), TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[12], true, true);

		larrCells[13] = ReportBuilder.buildCell(pobjReceipt.getAt(Receipt.I.DUEDATE), TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[13], true, true);

		larrCells[14] = ReportBuilder.buildCell((lobjLog == null ? null : lobjLog.GetTimestamp()), TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[14], true, true);

		larrCells[15] = ReportBuilder.buildCell((lobjLog == null ? null : getMeans(lobjLog)), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[15], true, true);

		larrCells[16] = ReportBuilder.buildCell(lobjProfile.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[16], true, true);

		setWidths(larrCells);

		return larrCells;
	}

	protected void setWidths(TD[] parrCells)
	{
		parrCells[ 0].setWidth(100);
		parrCells[ 1].setWidth( 20);
		parrCells[ 2].setWidth(300);
		parrCells[ 3].setWidth(130);
		parrCells[ 4].setWidth( 60);
		parrCells[ 5].setWidth(100);
		parrCells[ 6].setWidth(100);
		parrCells[ 7].setWidth( 80);
		parrCells[ 8].setWidth( 80);
		parrCells[ 9].setWidth( 80);
		parrCells[10].setWidth( 70);
		parrCells[11].setWidth( 90);
		parrCells[12].setWidth( 75);
		parrCells[13].setWidth( 88);
		parrCells[14].setWidth(100);
		parrCells[15].setWidth( 50);
		parrCells[16].setWidth( 50);
	}

	protected String getMeans(ILog pobjLog)
		throws BigBangJewelException
	{
		Payment lobjPayment;
		StringBuilder lstrAux;
		int i;

		try
		{
			lobjPayment = (Payment)pobjLog.GetOperationData();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		if ( lobjPayment == null )
			return "?";
		else
		{
			lstrAux = new StringBuilder();
			for ( i = 0; i < lobjPayment.marrData.length; i++ )
			{
				try
				{
					lstrAux.append(Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
							Constants.ObjID_PaymentType), lobjPayment.marrData[i].midPaymentType).getAt(1));
				}
				catch (Throwable e)
				{
					lstrAux.append("*");
				}
			}
			return lstrAux.toString();
		}
	}

	protected void filterByClient(StringBuilder pstrSQL, UUID pidClient)
		throws BigBangJewelException
	{
		IEntity lrefPolicies;
		IEntity lrefSubPolicies;
		IEntity lrefSubCasualties;
		IEntity lrefCasualties;

		try
		{
			lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
			lrefSubPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicy));
			lrefSubCasualties = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubCasualty));
			lrefCasualties = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Casualty));

			pstrSQL.append(" AND ([Policy] IN (SELECT [PK] FROM (")
					.append(lrefPolicies.SQLForSelectByMembers(new int[] {Policy.I.CLIENT}, new java.lang.Object[] {pidClient}, null))
					.append(") [AuxPols1]) OR [Sub Policy] IN (SELECT [PK] FROM (")
					.append(lrefSubPolicies.SQLForSelectByMembers(new int[] {SubPolicy.I.SUBSCRIBER}, new java.lang.Object[] {pidClient}, null))
					.append(") [AuxSPols1]) OR [Sub Casualty] IN ((SELECT [PK] FROM (")
					.append(lrefSubCasualties.SQLForSelectAll())
					.append(") [AuxSCas1] WHERE ([Policy] IN (SELECT [PK] FROM (")
					.append(lrefPolicies.SQLForSelectByMembers(new int[] {Policy.I.CLIENT}, new java.lang.Object[] {pidClient}, null))
					.append(") [AuxPols2]) OR [Sub Policy] IN (SELECT [PK] FROM (")
					.append(lrefSubPolicies.SQLForSelectByMembers(new int[] {SubPolicy.I.SUBSCRIBER}, new java.lang.Object[] {pidClient}, null))
					.append(") [AuxSPols2]))) UNION (SELECT [PK] FROM (")
					.append(lrefSubCasualties.SQLForSelectByMembers(new int[] {SubCasualty.I.POLICY,  SubCasualty.I.SUBPOLICY},
							new java.lang.Object[] {null, null}, null))
					.append(") [AuxSCas2] WHERE [Casualty] IN (SELECT [PK] FROM (")
					.append(lrefCasualties.SQLForSelectByMembers(new int[] {Casualty.I.CLIENT}, new java.lang.Object[] {pidClient}, null))
					.append(") [AuxCas]))))");
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	protected void filterByClientGroup(StringBuilder pstrSQL, UUID pidGroup)
		throws BigBangJewelException
	{
		IEntity lrefPolicies;
		IEntity lrefSubPolicies;
		IEntity lrefSubCasualties;
		IEntity lrefCasualties;
		IEntity lrefClients;

		try
		{
			lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
			lrefSubPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicy));
			lrefSubCasualties = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubCasualty));
			lrefCasualties = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Casualty));
			lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));

			pstrSQL.append(" AND ([Policy] IN (SELECT [PK] FROM (")
					.append(lrefPolicies.SQLForSelectAll())
					.append(") [AuxPols1] WHERE [Client] IN (SELECT [PK] FROM (")
					.append(lrefClients.SQLForSelectByMembers(new int[] {Client.I.GROUP}, new java.lang.Object[] {pidGroup}, null))
					.append(") [AuxCli1])) OR [Sub Policy] IN (SELECT [PK] FROM (")
					.append(lrefSubPolicies.SQLForSelectAll())
					.append(") [AuxSPols1] WHERE [Subscriber] IN (SELECT [PK] FROM (")
					.append(lrefClients.SQLForSelectByMembers(new int[] {Client.I.GROUP}, new java.lang.Object[] {pidGroup}, null))
					.append(") [AuxCli2])) OR [Sub Casualty] IN ((SELECT [PK] FROM (")
					.append(lrefSubCasualties.SQLForSelectAll())
					.append(") [AuxSCas1] WHERE ([Policy] IN (SELECT [PK] FROM (")
					.append(lrefPolicies.SQLForSelectAll())
					.append(") [AuxPols2] WHERE [Client] IN (SELECT [PK] FROM (")
					.append(lrefClients.SQLForSelectByMembers(new int[] {Client.I.GROUP}, new java.lang.Object[] {pidGroup}, null))
					.append(") [AuxCli3])) OR [Sub Policy] IN (SELECT [PK] FROM (")
					.append(lrefSubPolicies.SQLForSelectAll())
					.append(") [AuxSPols2] WHERE [Subscriber] IN (SELECT [PK] FROM (")
					.append(lrefClients.SQLForSelectByMembers(new int[] {Client.I.GROUP}, new java.lang.Object[] {pidGroup}, null))
					.append(") [AuxCli4])))) UNION (SELECT [PK] FROM (")
					.append(lrefSubCasualties.SQLForSelectByMembers(new int[] {SubCasualty.I.POLICY,  SubCasualty.I.SUBPOLICY},
							new java.lang.Object[] {null, null}, null))
					.append(") [AuxSCas2] WHERE [Casualty] IN (SELECT [PK] FROM (")
					.append(lrefCasualties.SQLForSelectAll())
					.append(") [AuxCas] WHERE [Client] IN (SELECT [PK] FROM (")
					.append(lrefClients.SQLForSelectByMembers(new int[] {Client.I.GROUP}, new java.lang.Object[] {pidGroup}, null))
					.append(") [AuxCli5])))))");
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	protected void filterByAgent(StringBuilder pstrSQL, UUID pidAgent)
		throws BigBangJewelException
	{
		IEntity lrefPolicies;
		IEntity lrefSubPolicies;
		IEntity lrefSubCasualties;
		IEntity lrefCasualties;
		IEntity lrefClients;

		try
		{
			lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
			lrefSubPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicy));
			lrefSubCasualties = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubCasualty));
			lrefCasualties = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Casualty));
			lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));

			pstrSQL.append(" AND ([Policy] IN ((SELECT [PK] FROM (")
					.append(lrefPolicies.SQLForSelectByMembers(new int[] {Policy.I.MEDIATOR}, new java.lang.Object[] {pidAgent}, null))
					.append(") [AuxPols1]) UNION (SELECT [PK] FROM (")
					.append(lrefPolicies.SQLForSelectByMembers(new int[] {Policy.I.MEDIATOR}, new java.lang.Object[] {null}, null))
					.append(") [AuxPols2] WHERE [Client] IN (SELECT [PK] FROM (")
					.append(lrefClients.SQLForSelectByMembers(new int[] {Client.I.MEDIATOR}, new java.lang.Object[] {pidAgent}, null))
					.append(") [AuxCli1]))) OR [Sub Policy] IN (SELECT [PK] FROM (")
					.append(lrefSubPolicies.SQLForSelectAll())
					.append(") [AuxSPols1] WHERE [Subscriber] IN (SELECT [PK] FROM (")
					.append(lrefClients.SQLForSelectByMembers(new int[] {Client.I.MEDIATOR}, new java.lang.Object[] {pidAgent}, null))
					.append(") [AuxCli2])) OR [Sub Casualty] IN ((SELECT [PK] FROM (")
					.append(lrefSubCasualties.SQLForSelectAll())
					.append(") [AuxSCas1] WHERE ([Policy] IN ((SELECT [PK] FROM (")
					.append(lrefPolicies.SQLForSelectByMembers(new int[] {Policy.I.MEDIATOR}, new java.lang.Object[] {pidAgent}, null))
					.append(") [AuxPols3]) UNION (SELECT [PK] FROM (")
					.append(lrefPolicies.SQLForSelectByMembers(new int[] {Policy.I.MEDIATOR}, new java.lang.Object[] {null}, null))
					.append(") [AuxPols4] WHERE [Client] IN (SELECT [PK] FROM (")
					.append(lrefClients.SQLForSelectByMembers(new int[] {Client.I.MEDIATOR}, new java.lang.Object[] {pidAgent}, null))
					.append(") [AuxCli3]))) OR [Sub Policy] IN (SELECT [PK] FROM (")
					.append(lrefSubPolicies.SQLForSelectAll())
					.append(") [AuxSPols2] WHERE [Subscriber] IN (SELECT [PK] FROM (")
					.append(lrefClients.SQLForSelectByMembers(new int[] {Client.I.MEDIATOR}, new java.lang.Object[] {pidAgent}, null))
					.append(") [AuxCli4])))) UNION (SELECT [PK] FROM (")
					.append(lrefSubCasualties.SQLForSelectByMembers(new int[] {SubCasualty.I.POLICY,  SubCasualty.I.SUBPOLICY},
							new java.lang.Object[] {null, null}, null))
					.append(") [AuxSCas2] WHERE [Casualty] IN (SELECT [PK] FROM (")
					.append(lrefCasualties.SQLForSelectAll())
					.append(") [AuxCas] WHERE [Client] IN (SELECT [PK] FROM (")
					.append(lrefClients.SQLForSelectByMembers(new int[] {Client.I.MEDIATOR}, new java.lang.Object[] {pidAgent}, null))
					.append(") [AuxCli5])))))");
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	protected void filterByCompany(StringBuilder pstrSQL, UUID pidCompany)
		throws BigBangJewelException
	{
		IEntity lrefPolicies;
		IEntity lrefSubPolicies;
		IEntity lrefSubCasualties;

		try
		{
			lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
			lrefSubPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicy));
			lrefSubCasualties = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubCasualty));

			pstrSQL.append(" AND ([Policy] IN (SELECT [PK] FROM (")
					.append(lrefPolicies.SQLForSelectByMembers(new int[] {Policy.I.COMPANY}, new java.lang.Object[] {pidCompany}, null))
					.append(") [AuxPols1]) OR [Sub Policy] IN (SELECT [PK] FROM (")
					.append(lrefSubPolicies.SQLForSelectAll())
					.append(") [AuxSPols1] WHERE [Policy] IN (SELECT [PK] FROM (")
					.append(lrefPolicies.SQLForSelectByMembers(new int[] {Policy.I.COMPANY}, new java.lang.Object[] {pidCompany}, null))
					.append(") [AuxPols2])) OR [Sub Casualty] IN (SELECT [PK] FROM (")
					.append(lrefSubCasualties.SQLForSelectAll())
					.append(") [AuxSCas] WHERE ([Policy] IN (SELECT [PK] FROM (")
					.append(lrefPolicies.SQLForSelectByMembers(new int[] {Policy.I.COMPANY}, new java.lang.Object[] {pidCompany}, null))
					.append(") [AuxPols3]) OR [Sub Policy] IN (SELECT [PK] FROM (")
					.append(lrefSubPolicies.SQLForSelectAll())
					.append(") [AuxSPols2] WHERE [Policy] IN (SELECT [PK] FROM (")
					.append(lrefPolicies.SQLForSelectByMembers(new int[] {Policy.I.COMPANY}, new java.lang.Object[] {pidCompany}, null))
					.append(") [AuxPols4])))))");
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	protected void getMarkersData()
		throws BigBangJewelException
	{
		IEntity lrefReceipts;
		IEntity lrefLogs;
		String lstrSQL;
		MasterDB ldb;
		ResultSet lrsSubCs;
		UUID lidAux;
		ReceiptMarkers lobjAux;

		if ( mmapData != null )
			return;

		try
		{
			lrefReceipts = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Receipt));
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));

			lstrSQL = "SELECT [PK], " +
					"(SELECT MAX([Timestamp]) FROM (" + lrefLogs.SQLForSelectByMembers(
							new int[] {Jewel.Petri.Constants.Undone_In_Log}, new java.lang.Object[] {false}, null) + ") [AuxLogs1] " +
							"WHERE [Operation] IN ('" +
							Constants.OPID_Receipt_TriggerImageOnCreate + "', '" + Constants.OPID_Receipt_ReceiveImage + "') " +
							"AND [Process] = [Main].[Process]), " +
					"(SELECT MAX([Timestamp]) FROM (" + lrefLogs.SQLForSelectByMembers(
							new int[] {Jewel.Petri.Constants.FKOperation_In_Log, Jewel.Petri.Constants.Undone_In_Log},
							new java.lang.Object[] {Constants.OPID_Receipt_TriggerAutoValidate, false}, null) + ") [AuxLogs2] " +
							"WHERE [Process] = [Main].[Process]), " +
					"(SELECT MAX([Timestamp]) FROM (" + lrefLogs.SQLForSelectByMembers(
							new int[] {Jewel.Petri.Constants.FKOperation_In_Log, Jewel.Petri.Constants.Undone_In_Log},
							new java.lang.Object[] {Constants.OPID_Receipt_ValidateReceipt, false}, null) + ") [AuxLogs3] " +
							"WHERE [Process] = [Main].[Process]), " +
					"(SELECT MAX([Timestamp]) FROM (" + lrefLogs.SQLForSelectByMembers(
							new int[] {Jewel.Petri.Constants.Undone_In_Log}, new java.lang.Object[] {false}, null) + ") [AuxLogs4] " +
							"WHERE [Operation] IN ('" +
							Constants.OPID_Receipt_CreatePaymentNotice + "', '" + Constants.OPID_Receipt_CreateSignatureRequest + "') " +
							"AND [Process] = [Main].[Process]), " +
					"(SELECT MAX([Timestamp]) FROM (" + lrefLogs.SQLForSelectByMembers(
							new int[] {Jewel.Petri.Constants.FKOperation_In_Log, Jewel.Petri.Constants.Undone_In_Log},
							new java.lang.Object[] {Constants.OPID_Receipt_Payment, false}, null) + ") [AuxLogs5] " +
							"WHERE [Process] = [Main].[Process]), " +
					"(SELECT MAX([Timestamp]) FROM (" + lrefLogs.SQLForSelectByMembers(
							new int[] {Jewel.Petri.Constants.FKOperation_In_Log, Jewel.Petri.Constants.Undone_In_Log},
							new java.lang.Object[] {Constants.OPID_Receipt_InsurerAccounting, false}, null) + ") [AuxLogs6] " +
							"WHERE [Process] = [Main].[Process]), " +
					"(SELECT MAX([Timestamp]) FROM (" + lrefLogs.SQLForSelectByMembers(
							new int[] {Jewel.Petri.Constants.FKOperation_In_Log, Jewel.Petri.Constants.Undone_In_Log},
							new java.lang.Object[] {Constants.OPID_Receipt_MediatorAccounting, false}, null) + ") [AuxLogs7] " +
							"WHERE [Process] = [Main].[Process]) " +
					"FROM (" + lrefReceipts.SQLForSelectAll() + ") [Main];";

			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsSubCs = ldb.OpenRecordset(lstrSQL.toString());
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		mmapData = new HashMap<UUID, ReceiptMarkers>();

		try
		{
			while ( lrsSubCs.next() )
			{
				lidAux = UUID.fromString(lrsSubCs.getString(1));
				lobjAux = new ReceiptMarkers();
				lobjAux.mdtReceived = lrsSubCs.getTimestamp(2);
				lobjAux.mdtAutoVal = lrsSubCs.getTimestamp(3);
				lobjAux.mdtValidated = lrsSubCs.getTimestamp(4);
				lobjAux.mdtSent = lrsSubCs.getTimestamp(5);
				lobjAux.mdtPayed = lrsSubCs.getTimestamp(6);
				lobjAux.mdtAccountedI = lrsSubCs.getTimestamp(7);
				lobjAux.mdtAccountedM = lrsSubCs.getTimestamp(8);
				mmapData.put(lidAux, lobjAux);
			}
		}
		catch (Throwable e)
		{
			try { lrsSubCs.close(); } catch (SQLException e1) {}
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsSubCs.close();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
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
	}
}
