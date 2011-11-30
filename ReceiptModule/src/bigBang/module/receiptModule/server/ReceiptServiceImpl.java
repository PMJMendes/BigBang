package bigBang.module.receiptModule.server;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.ReceiptStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.receiptModule.interfaces.ReceiptService;
import bigBang.module.receiptModule.shared.ReceiptSearchParameter;
import bigBang.module.receiptModule.shared.ReceiptSortParameter;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ReceiptData;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Line;
import com.premiumminds.BigBang.Jewel.Objects.Mediator;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.SubLine;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.DeleteReceipt;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.ManageReceiptData;

public class ReceiptServiceImpl
	extends SearchServiceBase
	implements ReceiptService
{
	private static final long serialVersionUID = 1L;

	public Receipt getReceipt(String receiptId)
		throws SessionExpiredException, BigBangException
	{
		UUID lid;
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		Receipt lobjResult;
		IProcess lobjProc;
		Policy lobjPolicy;
		Client lobjClient;
		Mediator lobjMed;
		SubLine lobjSubLine;
		Line lobjLine;
		ObjectBase lobjCategory;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lid = UUID.fromString(receiptId);
		try
		{
			lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(), lid);
			if ( lobjReceipt.GetProcessID() == null )
				throw new BigBangException("Erro: Recibo sem processo de suporte. (Recibo n. "
						+ lobjReceipt.getAt(0).toString() + ")");
			lobjProc = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjReceipt.GetProcessID());
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), lobjProc.GetParent().GetData().getKey());
			lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), lobjProc.GetParent().GetParent().GetData().getKey());
			lobjMed = Mediator.GetInstance(Engine.getCurrentNameSpace(),
					(lobjPolicy.getAt(11) == null ?  (UUID)lobjClient.getAt(8) : (UUID)lobjPolicy.getAt(11)) );
			lobjSubLine = SubLine.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjPolicy.getAt(3));
			lobjLine = Line.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjSubLine.getAt(1));
			lobjCategory = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_LineCategory),
					(UUID)lobjLine.getAt(1));
			
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new Receipt();

		lobjResult.id = lobjReceipt.getKey().toString();
		lobjResult.number = (String)lobjReceipt.getAt(0);
		lobjResult.clientId = lobjClient.getKey().toString();
		lobjResult.clientNumber = ((Integer)lobjClient.getAt(1)).toString();
		lobjResult.clientName = lobjClient.getLabel();
		lobjResult.policyId = lobjPolicy.getKey().toString();
		lobjResult.policyNumber = lobjPolicy.getLabel();
		lobjResult.categoryId = lobjCategory.getKey().toString();
		lobjResult.categoryName = lobjCategory.getLabel();
		lobjResult.lineId = lobjLine.getKey().toString();
		lobjResult.lineName = lobjLine.getLabel();
		lobjResult.subLineId = lobjSubLine.getKey().toString();
		lobjResult.subLineName = lobjSubLine.getLabel();
		lobjResult.typeId = ((UUID)lobjReceipt.getAt(1)).toString();
		lobjResult.totalPremium = ((BigDecimal)lobjReceipt.getAt(3)).toPlainString();
		lobjResult.maturityDate = (lobjReceipt.getAt(9) == null ? null :
				((Timestamp)lobjReceipt.getAt(9)).toString().substring(0, 10));
		lobjResult.description = (String)lobjReceipt.getAt(14);
		lobjResult.processId = lobjProc.getKey().toString();
		lobjResult.salesPremium = (lobjReceipt.getAt(4) == null ? null : ((BigDecimal)lobjReceipt.getAt(4)).toPlainString());
		lobjResult.comissions = ((BigDecimal)lobjReceipt.getAt(5)).toPlainString();
		lobjResult.retrocessions = ((BigDecimal)lobjReceipt.getAt(6)).toPlainString();
		lobjResult.FATValue = (lobjReceipt.getAt(7) == null ? null : ((BigDecimal)lobjReceipt.getAt(7)).toPlainString());
		lobjResult.issueDate = ((Timestamp)lobjReceipt.getAt(8)).toString().substring(0, 10);
		lobjResult.endDate = (lobjReceipt.getAt(10) == null ? null :
			((Timestamp)lobjReceipt.getAt(10)).toString().substring(0, 10));
		lobjResult.dueDate = (lobjReceipt.getAt(11) == null ? null :
			((Timestamp)lobjReceipt.getAt(11)).toString().substring(0, 10));
		lobjResult.mediatorId = (lobjReceipt.getAt(12) == null ? null : ((UUID)lobjReceipt.getAt(12)).toString());
		lobjResult.inheritMediatorId = lobjMed.getKey().toString();
		lobjResult.inheritMediatorName = lobjMed.getLabel();
		lobjResult.notes = (String)lobjReceipt.getAt(13);

		lobjResult.managerId = lobjProc.GetManagerID().toString();

		return lobjResult;
	}

	public Receipt editReceipt(Receipt receipt)
		throws SessionExpiredException, BigBangException
	{
		ManageReceiptData lopMRD;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lopMRD = new ManageReceiptData(UUID.fromString(receipt.processId));
			lopMRD.mobjData = new ReceiptData();

			lopMRD.mobjData.mid = UUID.fromString(receipt.id);

			lopMRD.mobjData.mstrNumber = receipt.number;
			lopMRD.mobjData.midType = UUID.fromString(receipt.typeId);
			lopMRD.mobjData.mdblTotal = new BigDecimal(receipt.totalPremium);
			lopMRD.mobjData.midType = UUID.fromString(receipt.typeId);
			lopMRD.mobjData.mdblTotal = new BigDecimal(receipt.totalPremium);
			lopMRD.mobjData.mdblCommercial = (receipt.salesPremium == null ? null : new BigDecimal(receipt.salesPremium));
			lopMRD.mobjData.mdblCommissions = (receipt.comissions == null ? new BigDecimal(0) : new BigDecimal(receipt.comissions));
			lopMRD.mobjData.mdblRetrocessions = (receipt.retrocessions == null ? new BigDecimal(0) :
					new BigDecimal(receipt.retrocessions));
			lopMRD.mobjData.mdblFAT = (receipt.FATValue == null ? null : new BigDecimal(receipt.FATValue));
			lopMRD.mobjData.mdtIssue = Timestamp.valueOf(receipt.issueDate + " 00:00:00.0");
			lopMRD.mobjData.mdtMaturity = (receipt.maturityDate == null ? null :
					Timestamp.valueOf(receipt.maturityDate + " 00:00:00.0"));
			lopMRD.mobjData.mdtEnd = (receipt.endDate == null ? null : Timestamp.valueOf(receipt.endDate + " 00:00:00.0"));
			lopMRD.mobjData.mdtDue = (receipt.dueDate == null ? null : Timestamp.valueOf(receipt.dueDate + " 00:00:00.0"));
			lopMRD.mobjData.midMediator = ( receipt.mediatorId == null ? null : UUID.fromString(receipt.mediatorId) );
			lopMRD.mobjData.mstrNotes = receipt.notes;
			lopMRD.mobjData.mstrDescription = receipt.description;
			lopMRD.mobjData.midProcess = UUID.fromString(receipt.processId);

			lopMRD.mobjData.midManager = null;

			lopMRD.mobjData.mobjPrevValues = null;

			lopMRD.mobjContactOps = null;
			lopMRD.mobjDocOps = null;

			lopMRD.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return getReceipt(receipt.id);
	}

	public void deleteReceipt(String receiptId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		DeleteReceipt lopDP;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(receiptId));

			lopDP = new DeleteReceipt(lobjReceipt.GetProcessID());
			lopDP.midReceipt = UUID.fromString(receiptId);
			lopDP.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	protected UUID getObjectID()
	{
		return Constants.ObjID_Receipt;
	}

	protected String[] getColumns()
	{
		return new String[] {"[:Number]", "[:Process]", "[:Type]", "[:Total Premium]", "[:Maturity Date]", "[:Description]"};
	}

	protected boolean buildFilter(StringBuilder pstrBuffer, SearchParameter pParam)
		throws BigBangException
	{
		ReceiptSearchParameter lParam;
		String lstrAux;
		IEntity lrefPolicies;

		if ( !(pParam instanceof ReceiptSearchParameter) )
			return false;
		lParam = (ReceiptSearchParameter)pParam;

		if ( (lParam.freeText != null) && (lParam.freeText.trim().length() > 0) )
		{
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			pstrBuffer.append(" AND ([:Number] LIKE N'%").append(lstrAux).append("%')");
		}

		if ( lParam.ownerId != null )
		{
			pstrBuffer.append(" AND [:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxOwner] WHERE [:Process:Data] = '").append(lParam.ownerId).append("')");
		}

		return true;
	}

	protected boolean buildSort(StringBuilder pstrBuffer, SortParameter pParam, SearchParameter[] parrParams)
		throws BigBangException
	{
		ReceiptSortParameter lParam;

		if ( !(pParam instanceof ReceiptSortParameter) )
			return false;
		lParam = (ReceiptSortParameter)pParam;

		if ( lParam.field == ReceiptSortParameter.SortableField.RELEVANCE )
		{
			if ( !buildRelevanceSort(pstrBuffer, parrParams) )
				return false;
		}

		if ( lParam.field == ReceiptSortParameter.SortableField.NUMBER )
			pstrBuffer.append("[:Number]");

		if ( lParam.order == SortOrder.ASC )
			pstrBuffer.append(" ASC");

		if ( lParam.order == SortOrder.DESC )
			pstrBuffer.append(" DESC");

		return true;
	}

	protected SearchResult buildResult(UUID pid, Object[] parrValues)
	{
		IProcess lobjProcess;
		Policy lobjPolicy;
		Client lobjClient;
		ObjectBase lobjSubLine, lobjLine, lobjCategory;
		ReceiptStub lobjResult;

		try
		{
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), (UUID)parrValues[1]);
			try
			{
				lobjPolicy = (Policy)lobjProcess.GetParent().GetData();
				try
				{
					lobjSubLine = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubLine),
							(UUID)lobjPolicy.getAt(3));
					try
					{
						lobjLine = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Line),
								(UUID)lobjSubLine.getAt(1));
						try
						{
							lobjCategory = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
									Constants.ObjID_LineCategory), (UUID)lobjLine.getAt(1));
						}
						catch (Throwable e)
						{
							lobjCategory = null;
						}
					}
					catch (Throwable e)
					{
						lobjLine = null;
						lobjCategory = null;
					}
				}
				catch (Throwable e)
				{
					lobjSubLine = null;
					lobjLine = null;
					lobjCategory = null;
				}
			}
			catch (Throwable e)
			{
				lobjPolicy = null;
				lobjSubLine = null;
				lobjLine = null;
				lobjCategory = null;
			}
			try
			{
				lobjClient = (Client)lobjProcess.GetParent().GetParent().GetData();
			}
			catch (Throwable e)
			{
				lobjClient = null;
			}
		}
		catch (Throwable e)
		{
			lobjProcess = null;
			lobjPolicy = null;
			lobjSubLine = null;
			lobjLine = null;
			lobjCategory = null;
			lobjClient = null;
		}

		lobjResult = new ReceiptStub();

		lobjResult.id = pid.toString();
		lobjResult.number = (String)parrValues[0];
		lobjResult.clientId = (lobjClient == null ? null : lobjClient.getKey().toString());
		lobjResult.clientNumber = (lobjClient == null ? "" : ((Integer)lobjClient.getAt(1)).toString());
		lobjResult.clientName = (lobjClient == null ? "(Erro)" : lobjClient.getLabel());
		lobjResult.policyId = (lobjPolicy == null ? null : lobjPolicy.getKey().toString());
		lobjResult.policyNumber = (lobjPolicy == null ? "(Erro)" : lobjPolicy.getLabel());
		lobjResult.categoryId = (lobjCategory == null ? null : lobjCategory.getKey().toString());
		lobjResult.categoryName = (lobjCategory == null ? null : lobjCategory.getLabel());
		lobjResult.lineId = (lobjLine == null ? null : lobjLine.getKey().toString());
		lobjResult.lineName = (lobjLine == null ? null : lobjLine.getLabel());
		lobjResult.subLineId = (lobjSubLine == null ? null : lobjSubLine.getKey().toString());
		lobjResult.subLineName = (lobjSubLine == null ? null : lobjSubLine.getLabel());
		lobjResult.typeId = ((UUID)parrValues[2]).toString();
		lobjResult.totalPremium = ((BigDecimal)parrValues[3]).toPlainString();
		lobjResult.maturityDate = (parrValues[4] == null ? null : ((Timestamp)parrValues[4]).toString().substring(0, 10));
		lobjResult.description = (String)parrValues[5];
		lobjResult.processId = (lobjProcess == null ? null : lobjProcess.getKey().toString());
		return lobjResult;
	}

	private boolean buildRelevanceSort(StringBuilder pstrBuffer, SearchParameter[] parrParams)
		throws BigBangException
	{
		ReceiptSearchParameter lParam;
		String lstrAux;
		boolean lbFound;
		int i;

		if ( (parrParams == null) || (parrParams.length == 0) )
			return false;

		lbFound = false;
		for ( i = 0; i < parrParams.length; i++ )
		{
			if ( !(parrParams[i] instanceof ReceiptSearchParameter) )
				continue;
			lParam = (ReceiptSearchParameter) parrParams[i];
			if ( (lParam.freeText == null) || (lParam.freeText.trim().length() == 0) )
				continue;
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			if ( lbFound )
				pstrBuffer.append(" + ");
			lbFound = true;
			pstrBuffer.append("-PATINDEX(N'%").append(lstrAux).append("%', [:Number])");
		}

		return lbFound;
	}
}
