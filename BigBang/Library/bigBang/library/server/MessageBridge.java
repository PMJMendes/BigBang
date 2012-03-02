package bigBang.library.server;

import java.sql.ResultSet;
import java.util.StringTokenizer;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.Interfaces.IEntity;
import bigBang.definitions.shared.IncomingMessage;
import bigBang.definitions.shared.OutgoingMessage;
import bigBang.library.shared.BigBangException;

import com.premiumminds.BigBang.Jewel.Data.DocumentData;
import com.premiumminds.BigBang.Jewel.Data.IncomingMessageData;
import com.premiumminds.BigBang.Jewel.Data.OutgoingMessageData;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;

public class MessageBridge
{
	public static OutgoingMessageData outgoingToServer(OutgoingMessage pobjMessage)
		throws BigBangException
	{
		OutgoingMessageData lobjResult;
		MasterDB ldb;
		IEntity lrefUsers;
		ResultSet lrs;
		StringTokenizer lstrTok;
		int i;

		lobjResult = new OutgoingMessageData();

		if ( pobjMessage.forwardUserFullNames == null )
			lobjResult.marrUsers = new UUID[] {Engine.getCurrentUser()};
		else
		{
			try
			{
				lrefUsers = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
						Jewel.Engine.Constants.ObjectGUIDs.O_User));
				ldb = new MasterDB();
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			lobjResult.marrUsers = new UUID[pobjMessage.forwardUserFullNames.length + 1];
			lobjResult.marrUsers[0] = Engine.getCurrentUser();

			for ( i = 0; i < pobjMessage.forwardUserFullNames.length; i++ )
			{
				try
				{
					lrs = lrefUsers.SelectByMembers(ldb, new int[] {0}, new java.lang.Object[] {pobjMessage.forwardUserFullNames[i]},
							new int[] {1});
				}
				catch (Throwable e)
				{
					try { ldb.Disconnect(); } catch (Throwable e1) {}
					throw new BigBangException(e.getMessage(), e);
				}

				try
				{
					if ( lrs.next() )
						lobjResult.marrUsers[i + 1] = User.GetInstance(Engine.getCurrentNameSpace(), lrs).getKey();
					else
						throw new BigBangException("Erro: Utilizador não existente.");
				}
				catch (BigBangException e)
				{
					try { lrs.close(); } catch (Throwable e1) {}
					try { ldb.Disconnect(); } catch (Throwable e1) {}
					throw new BigBangException(e.getMessage(), e);
				}
				catch (Throwable e)
				{
					try { lrs.close(); } catch (Throwable e1) {}
					try { ldb.Disconnect(); } catch (Throwable e1) {}
					throw new BigBangException(e.getMessage(), e);
				}

				try
				{
					lrs.close();
				}
				catch (Throwable e)
				{
					try { ldb.Disconnect(); } catch (Throwable e1) {}
					throw new BigBangException(e.getMessage(), e);
				}
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
