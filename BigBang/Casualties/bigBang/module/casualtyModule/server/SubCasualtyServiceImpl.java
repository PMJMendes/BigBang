package bigBang.module.casualtyModule.server;

import java.math.BigDecimal;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortParameter;
import bigBang.definitions.shared.SubCasualty;
import bigBang.definitions.shared.SubCasualtyStub;
import bigBang.library.server.BigBangPermissionServiceImpl;
import bigBang.library.server.ExternRequestServiceImpl;
import bigBang.library.server.InfoOrDocumentRequestServiceImpl;
import bigBang.library.server.MessageBridge;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.casualtyModule.interfaces.SubCasualtyService;
import bigBang.module.casualtyModule.shared.SubCasualtySearchParameter;
import bigBang.module.casualtyModule.shared.SubCasualtySortParameter;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.SubCasualtyData;
import com.premiumminds.BigBang.Jewel.Data.SubCasualtyItemData;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualtyItem;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.Operations.SubCasualty.CloseProcess;
import com.premiumminds.BigBang.Jewel.Operations.SubCasualty.CreateExternRequest;
import com.premiumminds.BigBang.Jewel.Operations.SubCasualty.CreateInfoRequest;
import com.premiumminds.BigBang.Jewel.Operations.SubCasualty.DeleteSubCasualty;
import com.premiumminds.BigBang.Jewel.Operations.SubCasualty.ManageData;
import com.premiumminds.BigBang.Jewel.Operations.SubCasualty.MarkForClosing;
import com.premiumminds.BigBang.Jewel.Operations.SubCasualty.RejectClosing;

public class SubCasualtyServiceImpl
	extends SearchServiceBase
	implements SubCasualtyService
{
	private static final long serialVersionUID = 1L;

	public static SubCasualty sGetSubCasualty(UUID pidSubCasualty)
		throws BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubCasualty lobjSubCasualty;
		IProcess lobjProcess;
		com.premiumminds.BigBang.Jewel.Objects.Casualty lobjCasualty;
		ObjectBase lobjOwner;
		Policy lobjPolicy;
		SubCasualtyItem[] larrItems;
		SubCasualty lobjResult;
		BigDecimal ldblTotal;
		BigDecimal ldblLocal;
		int i;

		try
		{
			lobjSubCasualty = com.premiumminds.BigBang.Jewel.Objects.SubCasualty.GetInstance(Engine.getCurrentNameSpace(), pidSubCasualty);
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjSubCasualty.GetProcessID());
			lobjCasualty = (com.premiumminds.BigBang.Jewel.Objects.Casualty)lobjProcess.GetParent().GetData();
			lobjOwner = ( lobjSubCasualty.getAt(com.premiumminds.BigBang.Jewel.Objects.SubCasualty.I.POLICY) == null ?
					SubPolicy.GetInstance(Engine.getCurrentNameSpace(),
							(UUID)lobjSubCasualty.getAt(com.premiumminds.BigBang.Jewel.Objects.SubCasualty.I.SUBPOLICY)) :
					Policy.GetInstance(Engine.getCurrentNameSpace(),
							(UUID)lobjSubCasualty.getAt(com.premiumminds.BigBang.Jewel.Objects.SubCasualty.I.POLICY)) );
			lobjPolicy = (Policy)( Constants.ObjID_Policy.equals(lobjOwner.getDefinition().getDefObject().getKey()) ? lobjOwner :
					PNProcess.GetInstance(Engine.getCurrentNameSpace(), ((SubPolicy)lobjOwner).GetProcessID()).GetParent().GetData() );
			larrItems = lobjSubCasualty.GetCurrentItems();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new SubCasualty();
		lobjResult.id = lobjSubCasualty.getKey().toString();
		lobjResult.processId = lobjProcess.getKey().toString();
		lobjResult.number = lobjSubCasualty.getLabel();
		lobjResult.casualtyId = lobjCasualty.getKey().toString();
		lobjResult.referenceId = lobjOwner.getKey().toString();
		lobjResult.referenceTypeId = lobjOwner.getDefinition().getDefObject().getKey().toString();
		lobjResult.referenceNumber = lobjOwner.getLabel();
		lobjResult.categoryName = lobjPolicy.GetSubLine().getLine().getCategory().getLabel();
		lobjResult.lineName = lobjPolicy.GetSubLine().getLine().getLabel();
		lobjResult.subLineName = lobjPolicy.GetSubLine().getLabel();
		lobjResult.insurerProcessNumber = (String)lobjSubCasualty.getAt(com.premiumminds.BigBang.Jewel.Objects.SubCasualty.I.INSURERPROCESS);
		lobjResult.isOpen = lobjProcess.IsRunning();
		lobjResult.hasJudicial = (Boolean)lobjSubCasualty.getAt(com.premiumminds.BigBang.Jewel.Objects.SubCasualty.I.HASJUDICIAL);
		lobjResult.text = (String)lobjSubCasualty.getAt(com.premiumminds.BigBang.Jewel.Objects.SubCasualty.I.DESCRIPTION);
		lobjResult.internalNotes = (String)lobjSubCasualty.getAt(com.premiumminds.BigBang.Jewel.Objects.SubCasualty.I.NOTES);

		ldblTotal = null;
		lobjResult.items = new SubCasualty.SubCasualtyItem[larrItems.length];
		for ( i = 0; i < lobjResult.items.length; i++ )
		{
			ldblLocal = (BigDecimal)larrItems[i].getAt(SubCasualtyItem.I.DAMAGES);

			lobjResult.items[i] = new SubCasualty.SubCasualtyItem();
			lobjResult.items[i].id = larrItems[i].getKey().toString();
			if ( Constants.ObjID_Policy.equals(lobjOwner.getDefinition().getDefObject().getKey()) )
			{
				lobjResult.items[i].insuredObjectId = ( larrItems[i].getAt(SubCasualtyItem.I.POLICYOBJECT) == null ? null :
						((UUID)larrItems[i].getAt(SubCasualtyItem.I.POLICYOBJECT)).toString() );
				lobjResult.items[i].coverageId = ( larrItems[i].getAt(SubCasualtyItem.I.POLICYCOVERAGE) == null ? null :
						((UUID)larrItems[i].getAt(SubCasualtyItem.I.POLICYCOVERAGE)).toString() );
			}
			else
			{
				lobjResult.items[i].insuredObjectId = ( larrItems[i].getAt(SubCasualtyItem.I.SUBPOLICYOBJECT) == null ? null :
						((UUID)larrItems[i].getAt(SubCasualtyItem.I.SUBPOLICYOBJECT)).toString() );
				lobjResult.items[i].coverageId = ( larrItems[i].getAt(SubCasualtyItem.I.SUBOPOLICYCOVERAGE) == null ? null :
						((UUID)larrItems[i].getAt(SubCasualtyItem.I.SUBOPOLICYCOVERAGE)).toString() );
			}
			lobjResult.items[i].damageTypeId = ( larrItems[i].getAt(SubCasualtyItem.I.TYPE) == null ? null :
					((UUID)larrItems[i].getAt(SubCasualtyItem.I.TYPE)).toString() );
			lobjResult.items[i].damages = ( ldblLocal == null ? null : ldblLocal.doubleValue());
			lobjResult.items[i].settlement = ( larrItems[i].getAt(SubCasualtyItem.I.SETTLEMENT) == null ? null :
					((BigDecimal)larrItems[i].getAt(SubCasualtyItem.I.SETTLEMENT)).doubleValue() );

			ldblTotal = ( ldblTotal == null ? ldblLocal : (ldblLocal == null ? ldblTotal : ldblTotal.add(ldblLocal)) );
		}
		lobjResult.totalDamages = (ldblTotal == null ? null : ldblTotal.toPlainString());

		lobjResult.permissions = BigBangPermissionServiceImpl.sGetProcessPermissions(lobjProcess.getKey());

		return lobjResult;
	}

	public SubCasualty getSubCasualty(String subCasualtyId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return sGetSubCasualty(UUID.fromString(subCasualtyId));
	}

	public SubCasualty editSubCasualty(SubCasualty subCasualty)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubCasualty lobjSubCasualty;
		boolean lbPolicy;
		ManageData lopMD;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjSubCasualty = com.premiumminds.BigBang.Jewel.Objects.SubCasualty.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(subCasualty.id));
		}
		catch (BigBangJewelException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lbPolicy = (Constants.ObjID_Policy.equals(UUID.fromString(subCasualty.referenceTypeId)));

		lopMD = new ManageData(lobjSubCasualty.GetProcessID());
		lopMD.mobjData = new SubCasualtyData();
		lopMD.mobjData.mid = lobjSubCasualty.getKey();
		lopMD.mobjData.mstrNumber = subCasualty.number;
		lopMD.mobjData.midProcess = UUID.fromString(subCasualty.processId);
		lopMD.mobjData.midPolicy = ( lbPolicy ? UUID.fromString(subCasualty.referenceId) : null );
		lopMD.mobjData.midSubPolicy = ( lbPolicy ? null : UUID.fromString(subCasualty.referenceId) );
		lopMD.mobjData.mstrInsurerProc = subCasualty.insurerProcessNumber;
		lopMD.mobjData.mstrDescription = subCasualty.text;
		lopMD.mobjData.mstrNotes = subCasualty.internalNotes;
		lopMD.mobjData.mbHasJudicial = subCasualty.hasJudicial;

		if ( subCasualty.items != null )
		{
			lopMD.mobjData.marrItems = new SubCasualtyItemData[subCasualty.items.length];
			for ( i = 0; i < lopMD.mobjData.marrItems.length; i++ )
			{
				lopMD.mobjData.marrItems[i] = new SubCasualtyItemData();
				lopMD.mobjData.marrItems[i].mid = ( subCasualty.items[i].id == null ? null : UUID.fromString(subCasualty.items[i].id) );
				lopMD.mobjData.marrItems[i].midSubCasualty = lopMD.mobjData.mid;
				lopMD.mobjData.marrItems[i].midPolicyObject = ( subCasualty.items[i].insuredObjectId == null ? null :
						(lbPolicy ? UUID.fromString(subCasualty.items[i].insuredObjectId) : null) );
				lopMD.mobjData.marrItems[i].midSubPolicyObject = ( subCasualty.items[i].insuredObjectId == null ? null :
						(lbPolicy ? null : UUID.fromString(subCasualty.items[i].insuredObjectId)) );
				lopMD.mobjData.marrItems[i].midPolicyCoverage = ( subCasualty.items[i].coverageId == null ? null :
						(lbPolicy ? UUID.fromString(subCasualty.items[i].coverageId) : null) );
				lopMD.mobjData.marrItems[i].midSubPolicyCoverage = ( subCasualty.items[i].coverageId == null ? null :
						(lbPolicy ? null : UUID.fromString(subCasualty.items[i].coverageId)) );
				lopMD.mobjData.marrItems[i].midType = ( subCasualty.items[i].damageTypeId == null ? null :
						UUID.fromString(subCasualty.items[i].damageTypeId) );
				lopMD.mobjData.marrItems[i].mdblDamages = ( subCasualty.items[i].damages == null ? null :
						new BigDecimal(subCasualty.items[i].damages+"") );
				lopMD.mobjData.marrItems[i].mdblSettlement = ( subCasualty.items[i].settlement == null ? null :
						new BigDecimal(subCasualty.items[i].settlement+"") );
				lopMD.mobjData.marrItems[i].mbIsManual = subCasualty.items[i].isManual;

				lopMD.mobjData.marrItems[i].mbNew = ( !subCasualty.items[i].deleted && (subCasualty.items[i].id == null) );
				lopMD.mobjData.marrItems[i].mbDeleted = subCasualty.items[i].deleted;
			}
		}
		else
			lopMD.mobjData.marrItems = null;

		lopMD.mobjContactOps = null;
		lopMD.mobjDocOps = null;

		try
		{
			lopMD.Execute();
		}
		catch (JewelPetriException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetSubCasualty(lopMD.mobjData.mid);
	}

	public InfoOrDocumentRequest createInfoRequest(InfoOrDocumentRequest request)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubCasualty lobjSubCasualty;
		CreateInfoRequest lopCIR;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjSubCasualty = com.premiumminds.BigBang.Jewel.Objects.SubCasualty.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(request.parentDataObjectId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e); 
		}

		lopCIR = new CreateInfoRequest(lobjSubCasualty.GetProcessID());
		lopCIR.midRequestType = UUID.fromString(request.requestTypeId);
		lopCIR.mobjMessage = MessageBridge.outgoingToServer(request.message);
		lopCIR.mlngDays = request.replylimit;

		try
		{
			lopCIR.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e); 
		}

		return InfoOrDocumentRequestServiceImpl.sGetRequest(lopCIR.midRequestObject);
	}

	public ExternalInfoRequest createExternalRequest(ExternalInfoRequest request)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubCasualty lobjSubCasualty;
		CreateExternRequest lopCER;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjSubCasualty = com.premiumminds.BigBang.Jewel.Objects.SubCasualty.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(request.parentDataObjectId));

			lopCER = new CreateExternRequest(lobjSubCasualty.GetProcessID());
			lopCER.mstrSubject = request.subject;
			lopCER.mobjMessage = MessageBridge.incomingToServer(request.message, Constants.ObjID_SubCasualty);
			lopCER.mlngDays = request.replylimit;

			lopCER.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e); 
		}

		return ExternRequestServiceImpl.sGetRequest(lopCER.midRequestObject);
	}

	public SubCasualty markForClosing(String subCasualtyId, String revisorId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubCasualty lobjSubCasualty;
		MarkForClosing lopMFC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjSubCasualty = com.premiumminds.BigBang.Jewel.Objects.SubCasualty.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(subCasualtyId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopMFC = new MarkForClosing(lobjSubCasualty.GetProcessID());
		lopMFC.midReviewer = UUID.fromString(revisorId);

		try
		{
			lopMFC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetSubCasualty(lobjSubCasualty.getKey());
	}

	public SubCasualty closeProcess(String subCasualtyId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubCasualty lobjSubCasualty;
		CloseProcess lopCP;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjSubCasualty = com.premiumminds.BigBang.Jewel.Objects.SubCasualty.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(subCasualtyId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCP = new CloseProcess(lobjSubCasualty.GetProcessID());

		try
		{
			lopCP.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetSubCasualty(lobjSubCasualty.getKey());
	}

	public SubCasualty rejectClosing(String subCasualtyId, String reason)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubCasualty lobjSubCasualty;
		RejectClosing lopRC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjSubCasualty = com.premiumminds.BigBang.Jewel.Objects.SubCasualty.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(subCasualtyId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopRC = new RejectClosing(lobjSubCasualty.GetProcessID());
		lopRC.mstrReason = reason;

		try
		{
			lopRC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetSubCasualty(lobjSubCasualty.getKey());
	}

	public void deleteSubCasualty(String subCasualtyId, String reason)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubCasualty lobjSubCasualty;
		DeleteSubCasualty lobjDSC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjSubCasualty = com.premiumminds.BigBang.Jewel.Objects.SubCasualty.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(subCasualtyId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjDSC = new DeleteSubCasualty(lobjSubCasualty.GetProcessID());
		lobjDSC.midSubCasualty = UUID.fromString(subCasualtyId);
		lobjDSC.mstrReason = reason;

		try
		{
			lobjDSC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	protected UUID getObjectID()
	{
		return Constants.ObjID_SubCasualty;
	}

	protected String[] getColumns()
	{
		return new String[] {"[:Number]", "[:Process]", "[:Insurer Process]"};
	}

	protected boolean buildFilter(StringBuilder pstrBuffer, SearchParameter pParam)
		throws BigBangException
	{
		SubCasualtySearchParameter lParam;
		String lstrAux;
		IEntity lrefCasualties;

		if ( !(pParam instanceof SubCasualtySearchParameter) )
			return false;
		lParam = (SubCasualtySearchParameter)pParam;

		if ( (lParam.freeText != null) && (lParam.freeText.trim().length() > 0) )
		{
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			pstrBuffer.append(" AND [:Number] LIKE N'%").append(lstrAux).append("%'");
		}

		if ( lParam.ownerId != null )
		{
			pstrBuffer.append(" AND [:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefCasualties = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Casualty));
				pstrBuffer.append(lrefCasualties.SQLForSelectMulti());
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
		SubCasualtySortParameter lParam;

		if ( !(pParam instanceof SubCasualtySortParameter) )
			return false;
		lParam = (SubCasualtySortParameter)pParam;

		if ( lParam.field == SubCasualtySortParameter.SortableField.RELEVANCE )
		{
			if ( !buildRelevanceSort(pstrBuffer, parrParams) )
				return false;
		}

		if ( lParam.field == SubCasualtySortParameter.SortableField.NUMBER )
			pstrBuffer.append("[:Number]");

		return true;
	}

	protected SearchResult buildResult(UUID pid, Object[] parrValues)
	{
		com.premiumminds.BigBang.Jewel.Objects.SubCasualty lobjSubCasualty;
		IProcess lobjProcess;
		com.premiumminds.BigBang.Jewel.Objects.Casualty lobjCasualty;
		ObjectBase lobjOwner;
		Policy lobjPolicy;
		SubCasualtyItem[] larrItems;
		SubCasualtyStub lobjResult;
		BigDecimal ldblTotal;
		BigDecimal ldblLocal;
		int i;

		lobjSubCasualty = null;
		lobjOwner = null;
		lobjPolicy = null;
		try
		{
			lobjSubCasualty = com.premiumminds.BigBang.Jewel.Objects.SubCasualty.GetInstance(Engine.getCurrentNameSpace(), pid);
			lobjOwner = ( lobjSubCasualty.getAt(com.premiumminds.BigBang.Jewel.Objects.SubCasualty.I.POLICY) == null ?
					SubPolicy.GetInstance(Engine.getCurrentNameSpace(),
							(UUID)lobjSubCasualty.getAt(com.premiumminds.BigBang.Jewel.Objects.SubCasualty.I.SUBPOLICY)) :
					Policy.GetInstance(Engine.getCurrentNameSpace(),
							(UUID)lobjSubCasualty.getAt(com.premiumminds.BigBang.Jewel.Objects.SubCasualty.I.POLICY)) );
			lobjPolicy = (Policy)( Constants.ObjID_Policy.equals(lobjOwner.getDefinition().getDefObject().getKey()) ? lobjOwner :
					PNProcess.GetInstance(Engine.getCurrentNameSpace(), ((SubPolicy)lobjOwner).GetProcessID()).GetParent().GetData() );
		}
		catch (Throwable e)
		{
		}
		lobjProcess = null;
		lobjCasualty = null;
		try
		{
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), (UUID)parrValues[1]);
			lobjCasualty = (com.premiumminds.BigBang.Jewel.Objects.Casualty)lobjProcess.GetParent().GetData();
		}
		catch (Throwable e)
		{
		}
		larrItems = null;
		try
		{
			larrItems = lobjSubCasualty.GetCurrentItems();
		}
		catch (Throwable e)
		{
		}

		lobjResult = new SubCasualtyStub();
		lobjResult.id = pid.toString();
		lobjResult.number = (String)parrValues[0];
		lobjResult.processId = ((UUID)parrValues[1]).toString();
		lobjResult.casualtyId = (lobjCasualty == null ? null : lobjCasualty.getKey().toString());
		lobjResult.referenceId = (lobjOwner == null ? null : lobjOwner.getKey().toString());
		lobjResult.referenceTypeId = (lobjOwner == null ? null : lobjOwner.getDefinition().getDefObject().getKey().toString());
		lobjResult.referenceNumber = (lobjOwner == null ? null : lobjOwner.getLabel());
		lobjResult.categoryName = (lobjPolicy == null ? null : lobjPolicy.GetSubLine().getLine().getCategory().getLabel());
		lobjResult.lineName = (lobjPolicy == null ? null : lobjPolicy.GetSubLine().getLine().getLabel());
		lobjResult.subLineName = (lobjPolicy == null ? null : lobjPolicy.GetSubLine().getLabel());
		lobjResult.insurerProcessNumber = (String)parrValues[2];
		lobjResult.isOpen = lobjProcess.IsRunning();

		ldblTotal = null;
		if ( larrItems != null )
		{
			for ( i = 0; i < larrItems.length; i++ )
			{
				ldblLocal = (BigDecimal)larrItems[i].getAt(SubCasualtyItem.I.DAMAGES);
				ldblTotal = ( ldblTotal == null ? ldblLocal : (ldblLocal == null ? ldblTotal : ldblTotal.add(ldblLocal)) );
			}
		}
		lobjResult.totalDamages = (ldblTotal == null ? null : ldblTotal.toPlainString());

		return lobjResult;
	}

	private boolean buildRelevanceSort(StringBuilder pstrBuffer, SearchParameter[] parrParams)
		throws BigBangException
	{
		SubCasualtySearchParameter lParam;
		String lstrAux;
		boolean lbFound;
		int i;

		if ( (parrParams == null) || (parrParams.length == 0) )
			return false;

		lbFound = false;
		for ( i = 0; i < parrParams.length; i++ )
		{
			if ( !(parrParams[i] instanceof SubCasualtySearchParameter) )
				continue;
			lParam = (SubCasualtySearchParameter) parrParams[i];
			if ( (lParam.freeText == null) || (lParam.freeText.trim().length() == 0) )
				continue;
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			if ( lbFound )
				pstrBuffer.append(" + ");
			lbFound = true;
			pstrBuffer.append("CASE WHEN [:Number] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-PATINDEX(N'%").append(lstrAux).append("%', [:Number]) ELSE ")
					.append("0 END");
		}

		return lbFound;
	}
}
