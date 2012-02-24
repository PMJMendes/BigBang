package bigBang.library.server;

import java.util.StringTokenizer;
import java.util.UUID;

import Jewel.Engine.Engine;
import bigBang.definitions.shared.OutgoingMessage;

import com.premiumminds.BigBang.Jewel.Data.OutgoingMessageData;

public class OutgoingHeaderBridge
{
	public static OutgoingMessageData toServer(OutgoingMessage pobjMessage)
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
}
