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
import bigBang.definitions.shared.MedicalFile;
import bigBang.definitions.shared.MedicalFileStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.server.BigBangPermissionServiceImpl;
import bigBang.library.server.ConversationServiceImpl;
import bigBang.library.server.MessageBridge;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.casualtyModule.interfaces.MedicalFileService;
import bigBang.module.casualtyModule.shared.MedicalFileSearchParameter;
import bigBang.module.casualtyModule.shared.MedicalFileSortParameter;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ConversationData;
import com.premiumminds.BigBang.Jewel.Data.MedicalAppointmentData;
import com.premiumminds.BigBang.Jewel.Data.MedicalDetailData;
import com.premiumminds.BigBang.Jewel.Data.MedicalFileData;
import com.premiumminds.BigBang.Jewel.Data.MessageData;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.MedicalAppointment;
import com.premiumminds.BigBang.Jewel.Objects.MedicalDetail;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualty;
import com.premiumminds.BigBang.Jewel.Operations.MedicalFile.CloseProcess;
import com.premiumminds.BigBang.Jewel.Operations.MedicalFile.CreateConversation;
import com.premiumminds.BigBang.Jewel.Operations.MedicalFile.ManageData;
import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;

public class MedicalFileServiceImpl
	extends SearchServiceBase
	implements MedicalFileService
{
	private static final long serialVersionUID = 1L;

	public static MedicalFile.MedicalDetail sGetDetail(MedicalDetail pobjDetail)
		throws BigBangException
	{
		ObjectBase lobjType;
		MedicalFile.MedicalDetail lobjResult;

		try
		{
			lobjType = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_DisabilityType),
					(UUID)pobjDetail.getAt(MedicalDetail.I.DISABILITYTYPE));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new MedicalFile.MedicalDetail();
		lobjResult.id = pobjDetail.getKey().toString();
		lobjResult.disabilityTypeId = lobjType.getKey().toString();
		lobjResult.disabilityTypeLabel = lobjType.getLabel();
		lobjResult.startDate = (pobjDetail.getAt(MedicalDetail.I.STARTDATE) == null ? null :
				((Timestamp)pobjDetail.getAt(MedicalDetail.I.STARTDATE)).toString().substring(0, 10) );
		lobjResult.place = (String)pobjDetail.getAt(MedicalDetail.I.PLACE);
		lobjResult.percentDisability = (Integer)pobjDetail.getAt(MedicalDetail.I.PERCENT);
		lobjResult.endDate = (pobjDetail.getAt(MedicalDetail.I.ENDDATE) == null ? null :
				((Timestamp)pobjDetail.getAt(MedicalDetail.I.ENDDATE)).toString().substring(0, 10) );
		lobjResult.benefits = (pobjDetail.getAt(MedicalDetail.I.BENEFITS) == null ? null :
				((BigDecimal)pobjDetail.getAt(MedicalDetail.I.BENEFITS)).doubleValue());

		return lobjResult;
	}

	public static MedicalFile.Appointment sGetAppointment(MedicalAppointment pobjAppt)
	{
		MedicalFile.Appointment lobjResult;

		lobjResult = new MedicalFile.Appointment();
		lobjResult.id = pobjAppt.getKey().toString();
		lobjResult.label = (String)pobjAppt.getAt(MedicalAppointment.I.LABEL);
		lobjResult.date = ((Timestamp)pobjAppt.getAt(MedicalAppointment.I.DATE)).toString().substring(0, 10);

		return lobjResult;
	}

	public static MedicalFile sGetMedicalFile(UUID pidFile)
		throws BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.MedicalFile lobjFile;
		MedicalDetail[] larrDetails;
		MedicalAppointment[] larrAppts;
		SubCasualty lobjSubC;
		Client lobjCli;
		String lstrObj;
		IProcess lobjProcess;
		MedicalFile lobjResult;
		int i;

		try
		{
			lobjFile = com.premiumminds.BigBang.Jewel.Objects.MedicalFile.GetInstance(Engine.getCurrentNameSpace(), pidFile);
			larrDetails = lobjFile.GetCurrentDetails();
			larrAppts = lobjFile.GetCurrentAppts();
			lobjSubC = SubCasualty.GetInstance(Engine.getCurrentNameSpace(),
					(UUID)lobjFile.getAt(com.premiumminds.BigBang.Jewel.Objects.MedicalFile.I.SUBCASUALTY));
			lobjCli = lobjSubC.GetCasualty().GetClient();
			lstrObj = lobjSubC.GetObjectName();
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjFile.GetProcessID());
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new MedicalFile();
		lobjResult.id = lobjFile.getKey().toString();
		lobjResult.processId = lobjProcess.getKey().toString();
		lobjResult.reference = lobjFile.getLabel();
		lobjResult.nextDate = (lobjFile.getAt(com.premiumminds.BigBang.Jewel.Objects.MedicalFile.I.NEXTDATE) == null ? null :
			((Timestamp)lobjFile.getAt(com.premiumminds.BigBang.Jewel.Objects.MedicalFile.I.NEXTDATE)).toString().substring(0, 10) );
		lobjResult.notes = (String)lobjFile.getAt(com.premiumminds.BigBang.Jewel.Objects.MedicalFile.I.NOTES);
		lobjResult.inheritClientName = lobjCli.getLabel();
		lobjResult.inheritObjectName = lstrObj;
		lobjResult.isRunning = lobjProcess.IsRunning();
		lobjResult.subCasualtyId = lobjSubC.getKey().toString();
		lobjResult.subCasualtyNumber = lobjSubC.getLabel();

		if ( larrDetails == null )
			lobjResult.details = null;
		else
		{
			lobjResult.details = new MedicalFile.MedicalDetail[larrDetails.length];
			for ( i = 0; i < larrDetails.length; i++ )
				lobjResult.details[i] = sGetDetail(larrDetails[i]);
		}

		if ( larrAppts == null )
			lobjResult.appointments = null;
		else
		{
			lobjResult.appointments = new MedicalFile.Appointment[larrAppts.length];
			for ( i = 0; i < larrAppts.length; i++ )
				lobjResult.appointments[i] = sGetAppointment(larrAppts[i]);
		}

		lobjResult.permissions = BigBangPermissionServiceImpl.sGetProcessPermissions(lobjProcess.getKey());

		return lobjResult;
	}

	public static MedicalDetailData sParseDetail(MedicalFile.MedicalDetail pobjSource, UUID pidFile)
	{
		MedicalDetailData lobjResult;

		if ( (pobjSource.id == null) && pobjSource.deleted )
			lobjResult = null;
		else
		{
			lobjResult = new MedicalDetailData();
			lobjResult.mid = ( pobjSource.id == null ? null : UUID.fromString(pobjSource.id) );
			if ( pobjSource.deleted )
				lobjResult.mbDeleted = true;
			else
			{
				lobjResult.mbDeleted = false;
				lobjResult.mbNew = (pobjSource.id == null);
				lobjResult.midFile = pidFile;
				lobjResult.midDisabilityType = ( pobjSource.disabilityTypeId == null ? null :
						UUID.fromString(pobjSource.disabilityTypeId) );
				lobjResult.mdtStartDate = ( pobjSource.startDate == null ? null :
						Timestamp.valueOf(pobjSource.startDate + " 00:00:00.0") );
				lobjResult.mstrPlace = pobjSource.place;
				lobjResult.mlngPercent = pobjSource.percentDisability;
				lobjResult.mdtEndDate = ( pobjSource.endDate == null ? null :
						Timestamp.valueOf(pobjSource.endDate + " 00:00:00.0") );
				lobjResult.mdblBenefits = ( pobjSource.benefits == null ? null :
						new BigDecimal(pobjSource.benefits + "") );
			}
		}

		return lobjResult;
	}

	public static MedicalAppointmentData sParseAppointment(MedicalFile.Appointment pobjSource, UUID pidFile)
	{
		MedicalAppointmentData lobjResult;

		if ( (pobjSource.id == null) && pobjSource.deleted )
			lobjResult = null;
		else
		{
			lobjResult = new MedicalAppointmentData();
			lobjResult.mid = ( pobjSource.id == null ? null : UUID.fromString(pobjSource.id) );
			if ( pobjSource.deleted )
				lobjResult.mbDeleted = true;
			else
			{
				lobjResult.mbDeleted = false;
				lobjResult.mbNew = (pobjSource.id == null);
				lobjResult.midFile = pidFile;
				lobjResult.mstrLabel = pobjSource.label;
				lobjResult.mdtDate = Timestamp.valueOf(pobjSource.date + " 00:00:00.0");
			}
		}

		return lobjResult;
	}

	public static MedicalFileData sParseClient(MedicalFile pobjSource)
	{
		MedicalFileData lobjResult;
		int i;

		lobjResult = new MedicalFileData();

		lobjResult.mid = (pobjSource.id == null ? null : UUID.fromString(pobjSource.id));
		lobjResult.mstrReference = pobjSource.reference;
		lobjResult.midSubCasualty = UUID.fromString(pobjSource.subCasualtyId);
		lobjResult.midProcess = ( pobjSource.processId == null ? null : UUID.fromString(pobjSource.processId) );
		lobjResult.mdtNextDate = ( pobjSource.nextDate == null ? null : Timestamp.valueOf(pobjSource.nextDate + " 00:00:00.0") );
		lobjResult.mstrNotes = pobjSource.notes;

		if ( pobjSource.details == null )
			lobjResult.marrDetails = null;
		else
		{
			lobjResult.marrDetails = new MedicalDetailData[pobjSource.details.length];
			for ( i = 0; i < pobjSource.details.length ; i++ )
				lobjResult.marrDetails[i] = sParseDetail(pobjSource.details[i], lobjResult.mid);
		}

		if ( pobjSource.appointments == null )
			lobjResult.marrAppts = null;
		else
		{
			lobjResult.marrAppts = new MedicalAppointmentData[pobjSource.appointments.length];
			for ( i = 0; i < pobjSource.appointments.length ; i++ )
				lobjResult.marrAppts[i] = sParseAppointment(pobjSource.appointments[i], lobjResult.mid);
		}

		return lobjResult;
	}

	public MedicalFile getMedicalFile(String id)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return sGetMedicalFile(UUID.fromString(id));
	}

	public MedicalFile editMedicalFile(MedicalFile medicalFile)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.MedicalFile lobjFile;
		ManageData lopMD;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjFile = com.premiumminds.BigBang.Jewel.Objects.MedicalFile.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(medicalFile.id));
		}
		catch (BigBangJewelException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopMD = new ManageData(lobjFile.GetProcessID());
		lopMD.mobjData = sParseClient(medicalFile);

		try
		{
			lopMD.Execute();
		}
		catch (JewelPetriException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetMedicalFile(lopMD.mobjData.mid);
	}

	public Conversation sendMessage(Conversation conversation)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.MedicalFile lobjFile;
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
			lobjFile = com.premiumminds.BigBang.Jewel.Objects.MedicalFile.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(conversation.parentDataObjectId));
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
				(UUID)lobjFile.getAt(com.premiumminds.BigBang.Jewel.Objects.MedicalFile.I.SUBCASUALTY),
				Constants.MsgDir_Outgoing, null);

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
		com.premiumminds.BigBang.Jewel.Objects.MedicalFile lobjFile;
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
			lobjFile = com.premiumminds.BigBang.Jewel.Objects.MedicalFile.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(conversation.parentDataObjectId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage() + " 389 ", e);
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
			storedMessage = MailConnector.getStoredMessage(null);
		} catch (Throwable e) {
			throw new BigBangException(e.getMessage() + " 410 ", e);
		}
		lopCC.mobjData.marrMessages[0] = MessageBridge.clientToServer(conversation.messages[0], Constants.ObjID_SubCasualty,
				(UUID)lobjFile.getAt(com.premiumminds.BigBang.Jewel.Objects.MedicalFile.I.SUBCASUALTY),
				lopCC.mobjData.midStartDir, storedMessage);

		try
		{
			lopCC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage() + " 422 ", e);
		}

		return ConversationServiceImpl.sGetConversation(lopCC.mobjData.mid);
	}

	public MedicalFile closeProcess(String id)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.MedicalFile lobjFile;
		CloseProcess lopCP;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjFile = com.premiumminds.BigBang.Jewel.Objects.MedicalFile.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(id));
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

		return sGetMedicalFile(lobjFile.getKey());
	}

	protected UUID getObjectID()
	{
		return Constants.ObjID_MedicalFile;
	}

	protected String[] getColumns()
	{
		return new String[] {"[:Reference]", "[:Process]", "[:Next Date]", "[:Process:Running]"};
	}

	protected void filterAgentUser(StringBuilder pstrBuffer, UUID pidMediator)
		throws BigBangException
	{
	}

	protected boolean buildFilter(StringBuilder pstrBuffer, SearchParameter pParam)
		throws BigBangException
	{
		MedicalFileSearchParameter lParam;
		String lstrAux;
		IEntity lrefSubCasualties;
		IEntity lrefPolObjects;
		IEntity lrefSubPolObjects;

		if ( !(pParam instanceof MedicalFileSearchParameter) )
			return false;
		lParam = (MedicalFileSearchParameter)pParam;

		if ( !lParam.includeClosed )
		{
			pstrBuffer.append(" AND [:Process:Running] = 1");
		}

		if ( (lParam.freeText != null) && (lParam.freeText.trim().length() > 0) )
		{
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			pstrBuffer.append(" AND ([:Reference] LIKE N'%").append(lstrAux).append("%'")
					.append(" OR (LEFT(CONVERT(NVARCHAR, [:Next Date], 120), 10) LIKE N'%").append(lstrAux).append("%')")
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
		MedicalFileSortParameter lParam;

		if ( !(pParam instanceof MedicalFileSortParameter) )
			return false;
		lParam = (MedicalFileSortParameter)pParam;

		if ( lParam.field == MedicalFileSortParameter.SortableField.RELEVANCE )
		{
			if ( !buildRelevanceSort(pstrBuffer, parrParams) )
				return false;
		}

		if ( lParam.field == MedicalFileSortParameter.SortableField.REFERENCE )
			pstrBuffer.append("[:Reference]");

		if ( lParam.field == MedicalFileSortParameter.SortableField.NEXT_DATE )
			pstrBuffer.append("[:Next Date]");

		if ( lParam.order == SortOrder.ASC )
			pstrBuffer.append(" ASC");

		if ( lParam.order == SortOrder.DESC )
			pstrBuffer.append(" DESC");

		return true;
	}

	protected SearchResult buildResult(UUID pid, Object[] parrValues)
	{
		IProcess lobjProcess;
		MedicalFileStub lobjResult;
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

		lobjResult = new MedicalFileStub();

		lobjResult.id = pid.toString();
		lobjResult.reference = (String)parrValues[0];
		lobjResult.nextDate = ( parrValues[2] == null ? null : ((Timestamp)parrValues[2]).toString().substring(0, 10) );
		lobjResult.inheritClientName = (lobjCli == null ? "(Erro a obter o nome do cliente.)" : lobjCli.getLabel());
		lobjResult.inheritObjectName = lstrObj;
		lobjResult.isRunning = ((Boolean)parrValues[3]);

		return lobjResult;
	}

	private boolean buildRelevanceSort(StringBuilder pstrBuffer, SearchParameter[] parrParams)
		throws BigBangException
	{
		MedicalFileSearchParameter lParam;
		String lstrAux;
		IEntity lrefSubCasualties;
		boolean lbFound;
		int i;

		if ( (parrParams == null) || (parrParams.length == 0) )
			return false;

		lbFound = false;
		for ( i = 0; i < parrParams.length; i++ )
		{
			if ( !(parrParams[i] instanceof MedicalFileSearchParameter) )
				continue;
			lParam = (MedicalFileSearchParameter) parrParams[i];
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
			
			pstrBuffer.append("CASE WHEN (LEFT(CONVERT(NVARCHAR, [:Next Date], 120), 10) LIKE N'%")
					.append(lstrAux).append("%') THEN ")
					.append("-1000000*PATINDEX(N'%").append(lstrAux)
					.append("%', LEFT(CONVERT(NVARCHAR, [:Next Date], 120), 10)) ELSE ");
			
			pstrBuffer.append("0 END END END");
		}

		return lbFound;
	}
}
