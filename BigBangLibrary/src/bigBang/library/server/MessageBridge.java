package bigBang.library.server;

import java.util.StringTokenizer;
import java.util.UUID;

import Jewel.Engine.Engine;
import bigBang.definitions.shared.IncomingMessage;
import bigBang.definitions.shared.OutgoingMessage;

import com.premiumminds.BigBang.Jewel.Data.DocumentData;
import com.premiumminds.BigBang.Jewel.Data.IncomingMessageData;
import com.premiumminds.BigBang.Jewel.Data.OutgoingMessageData;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;

public class MessageBridge
{
	public static OutgoingMessageData outgoingToServer(OutgoingMessage pobjMessage)
	{
		OutgoingMessageData lobjResult;
		StringTokenizer lstrTok;
		int i;

		lobjResult = new OutgoingMessageData();

		if ( pobjMessage.forwardUserIds == null )
			lobjResult.marrUsers = new UUID[] {Engine.getCurrentUser()};
		else
		{
			lobjResult.marrUsers = new UUID[pobjMessage.forwardUserIds.length + 1];
			lobjResult.marrUsers[0] = Engine.getCurrentUser();
			for ( i = 0; i < pobjMessage.forwardUserIds.length; i++ )
				lobjResult.marrUsers[i + 1] = UUID.fromString(pobjMessage.forwardUserIds[i]);
		}

		if ( pobjMessage.toContactInfoId == null )
			lobjResult.marrContactInfos = null;
		else
			lobjResult.marrContactInfos = new UUID[] {UUID.fromString(pobjMessage.toContactInfoId)};

		if ( pobjMessage.externalCCs == null )
			lobjResult.marrCCs = null;
		else
		{
			lstrTok = new StringTokenizer(pobjMessage.externalCCs, ",;");
			lobjResult.marrCCs = new String[lstrTok.countTokens()];
			for ( i = 0; i < lobjResult.marrCCs.length; i++ )
				lobjResult.marrCCs[i] = lstrTok.nextToken();
		}

		if ( pobjMessage.internalBCCs == null )
			lobjResult.marrBCCs = null;
		else
		{
			lstrTok = new StringTokenizer(pobjMessage.internalBCCs, ",;");
			lobjResult.marrBCCs = new String[lstrTok.countTokens()];
			for ( i = 0; i < lobjResult.marrBCCs.length; i++ )
				lobjResult.marrBCCs[i] = lstrTok.nextToken();
		}

		lobjResult.mstrSubject = pobjMessage.subject;
		lobjResult.mstrBody = pobjMessage.text;

		return lobjResult;
	}

	public static IncomingMessageData incomingToServer(IncomingMessage lobMessage, UUID pidDocOwnerType)
	{
		IncomingMessageData lobjResult;
		int i;

		lobjResult = new IncomingMessageData();

		lobjResult.mstrSubject = null;
		lobjResult.mstrBody = lobMessage.notes;
		lobjResult.mstrEmailID = lobMessage.emailId;

		if ( lobMessage.upgrades == null )
			lobjResult.mobjDocOps = null;
		else
		{
			lobjResult.mobjDocOps = new DocOps();
			lobjResult.mobjDocOps.marrModify = null;
			lobjResult.mobjDocOps.marrDelete = null;
			lobjResult.mobjDocOps.marrCreate = new DocumentData[lobMessage.upgrades.length];
			for ( i = 0; i < lobMessage.upgrades.length; i++ )
			{
				lobjResult.mobjDocOps.marrCreate[i] = new DocumentData();
				lobjResult.mobjDocOps.marrCreate[i].mstrName = lobMessage.upgrades[i].name;
				lobjResult.mobjDocOps.marrCreate[i].midOwnerType = pidDocOwnerType;
				lobjResult.mobjDocOps.marrCreate[i].midOwnerId = null;
				lobjResult.mobjDocOps.marrCreate[i].midDocType = UUID.fromString(lobMessage.upgrades[i].docTypeId);
				lobjResult.mobjDocOps.marrCreate[i].mstrText = null;
				lobjResult.mobjDocOps.marrCreate[i].mobjFile = FileServiceImpl.GetFileXferStorage().
						get(UUID.fromString(lobMessage.upgrades[i].storageId)).GetVarData();
				lobjResult.mobjDocOps.marrCreate[i].marrInfo = null;
				lobjResult.mobjDocOps.marrCreate[i].mobjPrevValues = null;
			}
		}

		return lobjResult;
	}
}
