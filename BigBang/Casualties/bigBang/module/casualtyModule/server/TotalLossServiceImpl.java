package bigBang.module.casualtyModule.server;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.ConversationStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.definitions.shared.TotalLossFile;
import bigBang.definitions.shared.TotalLossFileStub;
import bigBang.library.server.BigBangPermissionServiceImpl;
import bigBang.library.server.ConversationServiceImpl;
import bigBang.library.server.MessageBridge;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.casualtyModule.interfaces.TotalLossService;
import bigBang.module.casualtyModule.shared.TotalLossSearchParameter;
import bigBang.module.casualtyModule.shared.TotalLossSortParameter;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ConversationData;
import com.premiumminds.BigBang.Jewel.Data.MessageData;
import com.premiumminds.BigBang.Jewel.Data.TotalLossData;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualty;
import com.premiumminds.BigBang.Jewel.Objects.TotalLoss;
import com.premiumminds.BigBang.Jewel.Operations.TotalLoss.CloseProcess;
import com.premiumminds.BigBang.Jewel.Operations.TotalLoss.CreateConversation;
import com.premiumminds.BigBang.Jewel.Operations.TotalLoss.ManageData;
import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;

public class TotalLossServiceImpl
	extends SearchServiceBase
	implements TotalLossService
{
	private static final long serialVersionUID = 1L;

	public static TotalLossFile sGetTotalLoss(UUID pidFile)
		throws BigBangException
	{
		TotalLoss lobjLoss;
		SubCasualty lobjSubC;
		Client lobjCli;
		String lstrObj;
		IProcess lobjProcess;
		TotalLossFile lobjResult;
		ObjectBase lobjSalvageType;

		try
		{
			lobjLoss = TotalLoss.GetInstance(Engine.getCurrentNameSpace(), pidFile);
			lobjSubC = SubCasualty.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjLoss.getAt(TotalLoss.I.SUBCASUALTY));
			lobjCli = lobjSubC.GetCasualty().GetClient();
			lstrObj = lobjSubC.GetObjectName();
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjLoss.GetProcessID());
			lobjSalvageType = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SalvageType),
					(UUID)lobjLoss.getAt(TotalLoss.I.SALVAGETYPE));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new TotalLossFile();
		lobjResult.id = lobjLoss.getKey().toString();
		lobjResult.processId = lobjProcess.getKey().toString();
		lobjResult.reference = lobjLoss.getLabel();
		lobjResult.salvageTypeId = lobjSalvageType.getKey().toString();
		lobjResult.salvageTypeLabel = lobjSalvageType.getLabel();
		lobjResult.inheritClientName = lobjCli.getLabel();
		lobjResult.inheritObjectName = lstrObj;
		lobjResult.isRunning = lobjProcess.IsRunning();
		lobjResult.subCasualtyId = lobjSubC.getKey().toString();
		lobjResult.subCasualtyNumber = lobjSubC.getLabel();
		lobjResult.capital = (lobjLoss.getAt(TotalLoss.I.CAPITAL) == null ? null :
				((BigDecimal)lobjLoss.getAt(TotalLoss.I.CAPITAL)).doubleValue());
		lobjResult.deductible = (lobjLoss.getAt(TotalLoss.I.DEDUCTIBLE) == null ? null :
				((BigDecimal)lobjLoss.getAt(TotalLoss.I.DEDUCTIBLE)).doubleValue());
		lobjResult.settlement = (lobjLoss.getAt(TotalLoss.I.SETTLEMENT) == null ? null :
				((BigDecimal)lobjLoss.getAt(TotalLoss.I.SETTLEMENT)).doubleValue());
		lobjResult.salvageValue = (lobjLoss.getAt(TotalLoss.I.SALVAGEVALUE) == null ? null :
				((BigDecimal)lobjLoss.getAt(TotalLoss.I.SALVAGEVALUE)).doubleValue());
		lobjResult.salvageBuyer = (String)lobjLoss.getAt(TotalLoss.I.SALVAGEBUYER);

		lobjResult.permissions = BigBangPermissionServiceImpl.sGetProcessPermissions(lobjProcess.getKey());

		return lobjResult;
	}

	public static TotalLossData sParseClient(TotalLossFile pobjSource)
	{
		TotalLossData lobjResult;

		lobjResult = new TotalLossData();

		lobjResult.mid = (pobjSource.id == null ? null : UUID.fromString(pobjSource.id));
		lobjResult.mstrReference = pobjSource.reference;
		lobjResult.midSubCasualty = UUID.fromString(pobjSource.subCasualtyId);
		lobjResult.midProcess = (pobjSource.processId == null ? null : UUID.fromString(pobjSource.processId));
		lobjResult.mdblCapital = ( pobjSource.capital == null ? null :
				new BigDecimal(pobjSource.capital + "") );
		lobjResult.mdblDeductible = ( pobjSource.deductible == null ? null :
				new BigDecimal(pobjSource.deductible + "") );
		lobjResult.mdblSettlement = ( pobjSource.settlement == null ? null :
				new BigDecimal(pobjSource.settlement + "") );
		lobjResult.mdblsalvageValue = ( pobjSource.salvageValue == null ? null :
				new BigDecimal(pobjSource.salvageValue + "") );
		lobjResult.midSalvageType = ( pobjSource.salvageTypeId == null ? null :
				UUID.fromString(pobjSource.salvageTypeId) );
		lobjResult.mstrSalvageBuyer = pobjSource.salvageBuyer;

		return lobjResult;
	}

	public TotalLossFile getTotalLossFile(String id)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return sGetTotalLoss(UUID.fromString(id));
	}

	public TotalLossFile editTotalLossFile(TotalLossFile file)
		throws SessionExpiredException, BigBangException
	{
		TotalLoss lobjFile;
		ManageData lopMD;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjFile = TotalLoss.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(file.id));
		}
		catch (BigBangJewelException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopMD = new ManageData(lobjFile.GetProcessID());
		lopMD.mobjData = sParseClient(file);

		try
		{
			lopMD.Execute();
		}
		catch (JewelPetriException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetTotalLoss(lopMD.mobjData.mid);
	}

	public Conversation sendMessage(Conversation conversation)
		throws SessionExpiredException, BigBangException
	{
		TotalLoss lobjFile;
		Timestamp ldtAux, ldtLimit;
		Calendar ldtAux2;
		CreateConversation lopCC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( conversation.replylimit == null )
			ldtLimit = null;
		else
		{
			ldtAux = new Timestamp(new java.util.Date().getTime());
	    	ldtAux2 = Calendar.getInstance();
	    	ldtAux2.setTimeInMillis(ldtAux.getTime());
	    	ldtAux2.add(Calendar.DAY_OF_MONTH, conversation.replylimit);
	    	ldtLimit = new Timestamp(ldtAux2.getTimeInMillis());
		}

		try
		{
			lobjFile = TotalLoss.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(conversation.parentDataObjectId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCC = new CreateConversation(lobjFile.GetProcessID());
		lopCC.mobjData = new ConversationData();
		lopCC.mobjData.mid = null;
		lopCC.mobjData.mstrSubject = conversation.subject;
		lopCC.mobjData.midType = UUID.fromString(conversation.requestTypeId);
		lopCC.mobjData.midProcess = null;
		lopCC.mobjData.midStartDir = Constants.MsgDir_Outgoing;
		lopCC.mobjData.midPendingDir = ( conversation.replylimit == null ? null : Constants.MsgDir_Incoming );
		lopCC.mobjData.mdtDueDate = ldtLimit;

		lopCC.mobjData.marrMessages = new MessageData[1];
		lopCC.mobjData.marrMessages[0] = MessageBridge.clientToServer(conversation.messages[0], Constants.ObjID_SubCasualty,
				(UUID)lobjFile.getAt(TotalLoss.I.SUBCASUALTY), Constants.MsgDir_Outgoing, null);

		try
		{
			lopCC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return ConversationServiceImpl.sGetConversation(lopCC.mobjData.mid);
	}

	public Conversation receiveMessage(Conversation conversation)
		throws SessionExpiredException, BigBangException
	{
		TotalLoss lobjFile;
		Timestamp ldtAux, ldtLimit;
		Calendar ldtAux2;
		CreateConversation lopCC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( conversation.replylimit == null )
			ldtLimit = null;
		else
		{
			ldtAux = new Timestamp(new java.util.Date().getTime());
	    	ldtAux2 = Calendar.getInstance();
	    	ldtAux2.setTimeInMillis(ldtAux.getTime());
	    	ldtAux2.add(Calendar.DAY_OF_MONTH, conversation.replylimit);
	    	ldtLimit = new Timestamp(ldtAux2.getTimeInMillis());
		}

		try
		{
			lobjFile = TotalLoss.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(conversation.parentDataObjectId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage() + " 256 ", e);
		}

		lopCC = new CreateConversation(lobjFile.GetProcessID());
		lopCC.mobjData = new ConversationData();
		lopCC.mobjData.mid = null;
		lopCC.mobjData.mstrSubject = conversation.subject;
		lopCC.mobjData.midType = UUID.fromString(conversation.requestTypeId);
		lopCC.mobjData.midProcess = null;
		lopCC.mobjData.midStartDir = ( ConversationStub.Direction.OUTGOING.equals(conversation.startDir) ?
				Constants.MsgDir_Outgoing : Constants.MsgDir_Incoming ); // On NULL, default is INCOMING
		lopCC.mobjData.midPendingDir = ( conversation.replylimit == null ? null : ( ConversationStub.Direction.OUTGOING.equals(conversation.startDir) ?
				Constants.MsgDir_Incoming : Constants.MsgDir_Outgoing ) );
		lopCC.mobjData.mdtDueDate = ldtLimit;

		lopCC.mobjData.marrMessages = new MessageData[1];
		
		javax.mail.Message storedMessage = null;
		try {
			storedMessage = MailConnector.getStoredMessage(null, conversation.messages[0].emailId);
		} catch (Throwable e) {
			throw new BigBangException(e.getMessage() + " 277 ", e);
		}
		lopCC.mobjData.marrMessages[0] = MessageBridge.clientToServer(conversation.messages[0], Constants.ObjID_SubCasualty,
				(UUID)lobjFile.getAt(TotalLoss.I.SUBCASUALTY), lopCC.mobjData.midStartDir, storedMessage);

		try
		{
			lopCC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage() + " 288 ", e);
		}

		return ConversationServiceImpl.sGetConversation(lopCC.mobjData.mid);
	}

	public TotalLossFile closeProcess(String id)
		throws SessionExpiredException, BigBangException
	{
		TotalLoss lobjFile;
		CloseProcess lopCP;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjFile = TotalLoss.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(id));
		}
		catch (BigBangJewelException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCP = new CloseProcess(lobjFile.GetProcessID());

		try
		{
			lopCP.Execute();
		}
		catch (JewelPetriException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetTotalLoss(lobjFile.getKey());
	}

	protected UUID getObjectID()
	{
		return Constants.ObjID_TotalLossFile;
	}

	protected String[] getColumns()
	{
		return new String[] {"[:Reference]", "[:Process]", "[:Salvage Type]", "[:Salvage Type:Type]", "[:Process:Running]"};
	}

	protected void filterAgentUser(StringBuilder pstrBuffer, UUID pidMediator)
		throws BigBangException
	{
	}

	protected boolean buildFilter(StringBuilder pstrBuffer, SearchParameter pParam)
		throws BigBangException
	{
		TotalLossSearchParameter lParam;
		String lstrAux;
		IEntity lrefSubCasualties;
		IEntity lrefPolObjects;
		IEntity lrefSubPolObjects;

		if ( !(pParam instanceof TotalLossSearchParameter) )
			return false;
		lParam = (TotalLossSearchParameter)pParam;

		if ( !lParam.includeClosed )
		{
			pstrBuffer.append(" AND [:Process:Running] = 1");
		}

		if ( (lParam.freeText != null) && (lParam.freeText.trim().length() > 0) )
		{
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			pstrBuffer.append(" AND ([:Reference] LIKE N'%").append(lstrAux).append("%'")
					.append(" OR [:Salvage Type:Type] LIKE N'%").append(lstrAux).append("%'")
					.append(" OR [:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefSubCasualties = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubCasualty));
				pstrBuffer.append(lrefSubCasualties.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSubC] WHERE ([:Casualty:Client:Name] LIKE '%").append(lstrAux).append("%')))");
		}

		if ( lParam.insuredObject != null )
		{
			lstrAux = lParam.insuredObject.trim().replace("'", "''").replace(" ", "%");
			pstrBuffer.append(" AND ([:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefSubCasualties = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubCasualty));
				pstrBuffer.append(lrefSubCasualties.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSubCasualties] WHERE ([:Generic Object] LIKE '%").append(lstrAux).append("%'")
					.append(" OR [:Policy Object] IN (SELECT [PK] FROM (");
			try
			{
				lrefPolObjects = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PolicyObject));
				pstrBuffer.append(lrefPolObjects.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxPolObjects] WHERE [:Name] LIKE '%").append(lstrAux).append("%')")
					.append(" OR [:Sub Policy Object] IN (SELECT [PK] FROM (");
			try
			{
				lrefSubPolObjects = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicyObject));
				pstrBuffer.append(lrefSubPolObjects.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSubPolObjects] WHERE [:Name] LIKE '%").append(lstrAux).append("%'))))");
		}

		return true;
	}

	protected boolean buildSort(StringBuilder pstrBuffer, SortParameter pParam, SearchParameter[] parrParams)
		throws BigBangException
	{
		TotalLossSortParameter lParam;
		IEntity lrefSubCasualties;

		if ( !(pParam instanceof TotalLossSortParameter) )
			return false;
		lParam = (TotalLossSortParameter)pParam;

		if ( lParam.field == TotalLossSortParameter.SortableField.RELEVANCE )
		{
			if ( !buildRelevanceSort(pstrBuffer, parrParams) )
				return false;
		}

		if ( lParam.field == TotalLossSortParameter.SortableField.REFERENCE )
			pstrBuffer.append("[:Reference]");

		if ( lParam.field == TotalLossSortParameter.SortableField.CLIENT_NAME )
		{
			pstrBuffer.append("(SELECT [:Casualty:Client:Name] FROM (");
			try
			{
				lrefSubCasualties = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubCasualty));
				pstrBuffer.append(lrefSubCasualties.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSubC] WHERE [:Process] = [Aux].[:Process:Parent])");
		}
		
		if ( lParam.order == SortOrder.ASC )
			pstrBuffer.append(" ASC");

		if ( lParam.order == SortOrder.DESC )
			pstrBuffer.append(" DESC");

		return true;
	}

	protected SearchResult buildResult(UUID pid, Object[] parrValues)
	{
		IProcess lobjProcess;
		TotalLossFileStub lobjResult;
		SubCasualty lobjSub;
		Client lobjCli;
		String lstrObj;
		
		lobjProcess = null;
		lobjCli = null;
		lstrObj = null;
		try
		{
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), (UUID)parrValues[1]);
			lobjSub = (SubCasualty)lobjProcess.GetParent().GetData();
			lobjCli = lobjSub.GetCasualty().GetClient();
			lstrObj = lobjSub.GetObjectName();
		}
		catch (Throwable e)
		{
		}

		lobjResult = new TotalLossFileStub();

		lobjResult.id = pid.toString();
		lobjResult.reference = (String)parrValues[0];
		lobjResult.salvageTypeId = ((UUID)parrValues[2]).toString();
		lobjResult.salvageTypeLabel = (String)parrValues[3];
		lobjResult.inheritClientName = (lobjCli == null ? "(Erro a obter o nome do cliente.)" : lobjCli.getLabel());
		lobjResult.inheritObjectName = lstrObj;
		lobjResult.isRunning = ((Boolean)parrValues[4]);

		return lobjResult;
	}

	private boolean buildRelevanceSort(StringBuilder pstrBuffer, SearchParameter[] parrParams)
		throws BigBangException
	{
		TotalLossSearchParameter lParam;
		String lstrAux;
		IEntity lrefSubCasualties;
		boolean lbFound;
		int i;

		if ( (parrParams == null) || (parrParams.length == 0) )
			return false;

		lbFound = false;
		for ( i = 0; i < parrParams.length; i++ )
		{
			if ( !(parrParams[i] instanceof TotalLossSearchParameter) )
				continue;
			lParam = (TotalLossSearchParameter) parrParams[i];
			if ( (lParam.freeText == null) || (lParam.freeText.trim().length() == 0) )
				continue;
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			if ( lbFound )
				pstrBuffer.append(" + ");
			lbFound = true;
			pstrBuffer.append("CASE WHEN [:Reference] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-PATINDEX(N'%").append(lstrAux).append("%', [:Reference]) ELSE ")
					.append("CASE WHEN [:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefSubCasualties = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubCasualty));
				pstrBuffer.append(lrefSubCasualties.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSubC] WHERE ([:Casualty:Client:Name] LIKE '%").append(lstrAux).append("%')) THEN ")
					.append("-1000*PATINDEX(N'%").append(lstrAux).append("%', (SELECT [:Casualty:Client:Name] FROM (");
			try
			{
				pstrBuffer.append(lrefSubCasualties.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSubC2] WHERE [:Process] = [Aux].[:Process:Parent])) ELSE ");

			pstrBuffer.append("CASE WHEN [:Salvage Type:Type] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-1000000*PATINDEX(N'%").append(lstrAux).append("%', [:Salvage Type:Type]) ELSE ");
			
			pstrBuffer.append("0 END END END");
		}

		return lbFound;
	}
}
