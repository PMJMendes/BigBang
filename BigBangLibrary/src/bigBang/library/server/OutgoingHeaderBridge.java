package bigBang.library.server;

import java.util.StringTokenizer;
import java.util.UUID;

import Jewel.Engine.Engine;
import bigBang.definitions.shared.OutgoingHeaders;

import com.premiumminds.BigBang.Jewel.Data.OutgoingHeaderData;

public class OutgoingHeaderBridge
{
	public static OutgoingHeaderData toServer(OutgoingHeaders pobjHeaders)
	{
		OutgoingHeaderData lobjResult;
		StringTokenizer lstrTok;
		int i;

		lobjResult = new OutgoingHeaderData();

		if ( pobjHeaders.forwardUserIds == null )
			lobjResult.marrUsers = new UUID[] {Engine.getCurrentUser()};
		else
		{
			lobjResult.marrUsers = new UUID[pobjHeaders.forwardUserIds.length + 1];
			lobjResult.marrUsers[0] = Engine.getCurrentUser();
			for ( i = 0; i < pobjHeaders.forwardUserIds.length; i++ )
				lobjResult.marrUsers[i + 1] = UUID.fromString(pobjHeaders.forwardUserIds[i]);
		}

		if ( pobjHeaders.toContactInfoId == null )
			lobjResult.marrContactInfos = null;
		else
			lobjResult.marrContactInfos = new UUID[] {UUID.fromString(pobjHeaders.toContactInfoId)};

		if ( pobjHeaders.externalCCs == null )
			lobjResult.marrCCs = null;
		else
		{
			lstrTok = new StringTokenizer(pobjHeaders.externalCCs, ",;");
			lobjResult.marrCCs = new String[lstrTok.countTokens()];
			for ( i = 0; i < lobjResult.marrCCs.length; i++ )
				lobjResult.marrCCs[i] = lstrTok.nextToken();
		}

		if ( pobjHeaders.internalBCCs == null )
			lobjResult.marrBCCs = null;
		else
		{
			lstrTok = new StringTokenizer(pobjHeaders.internalBCCs, ",;");
			lobjResult.marrBCCs = new String[lstrTok.countTokens()];
			for ( i = 0; i < lobjResult.marrBCCs.length; i++ )
				lobjResult.marrBCCs[i] = lstrTok.nextToken();
		}

		return lobjResult;
	}
}
