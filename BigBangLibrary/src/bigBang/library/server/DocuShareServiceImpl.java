package bigBang.library.server;

import java.util.ArrayList;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.FileXfer;
import bigBang.library.interfaces.DocuShareService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.DocuShareItem;
import bigBang.library.shared.SessionExpiredException;

import com.xerox.docushare.DSContentElement;
import com.xerox.docushare.DSFactory;
import com.xerox.docushare.DSHandle;
import com.xerox.docushare.DSObject;
import com.xerox.docushare.DSObjectIterator;
import com.xerox.docushare.DSServer;
import com.xerox.docushare.DSSession;
import com.xerox.docushare.object.DSCollection;
import com.xerox.docushare.object.DSDocument;

public class DocuShareServiceImpl
	extends EngineImplementor
	implements DocuShareService
{
	private static final long serialVersionUID = 1L;

	private static DSServer GetServer()
		throws BigBangException
	{
		DSServer lrefResult;

        if (getSession() == null)
            return null;

        lrefResult = (DSServer)getSession().getAttribute("BigBang_DocuShare_Server");
        if (lrefResult == null)
        {
        	try
        	{
				lrefResult = DSFactory.createServer("192.168.0.3", 1099);
			}
        	catch (Throwable e)
        	{
        		throw new BigBangException(e.getMessage(), e);
			}
            getSession().setAttribute("BigBang_DocuShare_Server", lrefResult);
        }

        return lrefResult;
	}

	private static DSSession GetSession()
		throws BigBangException
	{
		DSSession lrefResult;

        if (getSession() == null)
            return null;

        lrefResult = (DSSession)getSession().getAttribute("BigBang_DocuShare_Session");
        if (lrefResult == null)
        {
        	try
        	{
				lrefResult = GetServer().createSession("DocuShare", "scanner", "scanner");
			}
        	catch (BigBangException e)
        	{
        		throw e;
			}
        	catch (Throwable e)
        	{
        		throw new BigBangException(e.getMessage(), e);
			}
            getSession().setAttribute("BigBang_DocuShare_Session", lrefResult);
        }

        return lrefResult;
	}

	public static void LogOff()
	{
		HttpSession lrefHttpSession;
		DSSession lrefSession;
		DSServer lrefServer;

        if (getSession() == null)
            return;

        lrefHttpSession = getSession();
        if ( lrefHttpSession == null )
        	return;

        lrefSession = (DSSession)lrefHttpSession.getAttribute("BigBang_DocuShare_Session");
        if (lrefSession != null)
        {
	        lrefHttpSession.removeAttribute("BigBang_DocuShare_Session");
	        try { lrefSession.close(); } catch (Throwable e) {}
        }

        lrefServer = (DSServer)lrefHttpSession.getAttribute("BigBang_DocuShare_Server");
        if (lrefServer != null)
        {
	        lrefHttpSession.removeAttribute("BigBang_DocuShare_Server");
	        try { lrefServer.close(); } catch (Throwable e) {}
		}
	}

	public DocuShareItem[] getSubFolders(String pstrFolder)
		throws SessionExpiredException, BigBangException
	{
		DSSession lrefSession;
		DSHandle lhFolder;
		DSCollection lobjFolder;
		DSObjectIterator i;
		DSObject lobjAux;
		ArrayList<DocuShareItem> larrResult;
		DocuShareItem lobjTmp;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lrefSession = GetSession();
		if ( lrefSession == null )
			return null;

		larrResult = new ArrayList<DocuShareItem>();

		if ( pstrFolder == null )
			lhFolder = new DSHandle("Collection-59066");
		else
			lhFolder = new DSHandle(pstrFolder);

		try
		{
			lobjFolder = (DSCollection)lrefSession.getObject(lhFolder);
			i = lobjFolder.children(null);
			while ( i.hasNext() )
			{
				lobjAux = i.nextObject();
				if ( lobjAux instanceof DSCollection )
				{
					lobjTmp = new DocuShareItem();
					lobjTmp.directory = true;
					lobjTmp.handle = lobjAux.getHandle().toString();
					lobjTmp.desc = lobjAux.getTitle();
					larrResult.add(lobjTmp);
				}
			}
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return larrResult.toArray(new DocuShareItem[larrResult.size()]);
	}

	public DocuShareItem[] getItems(String pstrFolder)
		throws SessionExpiredException, BigBangException
	{
		DSSession lrefSession;
		DSHandle lhFolder;
		DSCollection lobjFolder;
		DSObjectIterator i;
		DSObject lobjAux;
		ArrayList<DocuShareItem> larrResult;
		DocuShareItem lobjTmp;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lrefSession = GetSession();
		if ( lrefSession == null )
			return null;

		larrResult = new ArrayList<DocuShareItem>();

		if ( pstrFolder == null )
			lhFolder = new DSHandle("Collection-59066");
		else
			lhFolder = new DSHandle(pstrFolder);

		try
		{
			lobjFolder = (DSCollection)lrefSession.getObject(lhFolder);
			i = lobjFolder.children(null);
			while ( i.hasNext() )
			{
				lobjAux = i.nextObject();
				if ( !(lobjAux instanceof DSCollection) )
				{
					lobjTmp = new DocuShareItem();
					lobjTmp.directory = false;
					lobjTmp.handle = lobjAux.getHandle().toString();
					lobjTmp.desc = lobjAux.getTitle();
					larrResult.add(lobjTmp);
				}
			}
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return larrResult.toArray(new DocuShareItem[larrResult.size()]);
	}

	public String getItem(String pstrItem)
		throws SessionExpiredException, BigBangException
	{
		DSSession lrefSession;
		DSDocument lobjAux;
		DSContentElement[] larrAux;
		FileXfer lobjFile;
		UUID lidKey;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lrefSession = GetSession();
		if ( lrefSession == null )
			return null;

		try
		{
			lobjAux = (DSDocument)lrefSession.getObject(new DSHandle(pstrItem));
			larrAux = lobjAux.getContentElements();
			larrAux[0].open();
			lobjFile = new FileXfer(lobjAux.getSize(), lobjAux.getContentType(), lobjAux.getOriginalFileName(), larrAux[0]);
			larrAux[0].close();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lidKey = UUID.randomUUID();
		FileServiceImpl.GetFileXferStorage().put(lidKey, lobjFile);

		return lidKey.toString();
	}
}
