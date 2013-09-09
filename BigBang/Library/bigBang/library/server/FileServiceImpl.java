package bigBang.library.server;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Engine.SysObjects.ObjectBase;
import bigBang.definitions.shared.TipifiedListItem;
import bigBang.library.interfaces.FileService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Objects.FileProcesser;

public class FileServiceImpl
	extends EngineImplementor
	implements FileService
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public static Map<UUID, FileXfer> GetFileXferStorage()
	{
		ConcurrentHashMap<UUID, FileXfer> larrAux;

        if (getSession() == null)
            return null;

        larrAux = (ConcurrentHashMap<UUID, FileXfer>)getSession().getAttribute("MADDS_FileXfer_Storage");
        if (larrAux == null)
        {
        	larrAux = new ConcurrentHashMap<UUID, FileXfer>();
            getSession().setAttribute("MADDS_FileXfer_Storage", larrAux);
        }

        return larrAux;
	}

    protected void service(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException
	{
    	String r;

    	if ( req.getMethod().equals("POST") && ServletFileUpload.isMultipartContent(req) )
    	{
			setSession(req.getSession());
			try
			{
				overridePost(req, resp);
			}
			catch (BigBangException e)
			{
				r = "!Exception thrown: " + e.getMessage();
				resp.setContentLength(r.length());
				resp.setContentType("text/html");
				resp.setStatus(HttpServletResponse.SC_OK);
				resp.getWriter().print(r);
			}
			finally
			{
				clearSession();
			}
    	}
    	else
    		super.service(req, resp);
	}

    private void overridePost(HttpServletRequest request, HttpServletResponse response)
    	throws IOException, BigBangException
    {
    	DiskFileItemFactory factory;
    	ServletFileUpload upload;
    	List<?> items;
    	FileItem item;
        FileXfer lbuffer;
        String r;
        UUID lidKey;

        factory = new DiskFileItemFactory();
        factory.setSizeThreshold(102400);
        factory.setFileCleaningTracker(FileCleanerCleanup.getFileCleaningTracker(request.getSession().getServletContext()));
    	upload = new ServletFileUpload(factory);

    	try
    	{
        	items = upload.parseRequest(request);
		}
    	catch (FileUploadException e)
    	{
    		throw new BigBangException(e.getMessage(), e);
		}
    	if ( items.size() != 1 )
        	throw new BigBangException("Unexpected number of fields in form.");
    	item = (FileItem)items.get(0);
        if (item.isFormField())
        	throw new BigBangException("Unexpected non-file field in form.");

        r = item.getFieldName();
        if ( r.equals("none") )
        	lidKey = UUID.randomUUID();
        else
        	lidKey = UUID.fromString(r);
        lbuffer = new FileXfer((int)item.getSize(), item.getContentType(), item.getName(), item.getInputStream());
        GetFileXferStorage().put(lidKey, lbuffer);
        r = lidKey.toString() + "!" + item.getName();

        response.setContentLength(r.length());
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print(r);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    	throws IOException, ServletException
    {
    	String lstrRef;
    	FileXfer lbuffer;

		lstrRef = req.getParameter("fileref");
		if ( lstrRef == null )
		{
			super.doGet(req, resp);
			return;
		}

		if ( Engine.getCurrentUser() == null )
			return;

		lbuffer = GetFileXferStorage().get(UUID.fromString(lstrRef));
		if ( lbuffer == null )
			return;

		resp.setContentType(lbuffer.getContentType());
//		resp.addHeader("Content-Disposition", "attachment; filename=\"" + lbuffer.getFileName() + "\"");
		resp.addHeader("Content-Disposition", "inline; filename=\"" + lbuffer.getFileName() + "\"");
		resp.flushBuffer();
		resp.getOutputStream().write(lbuffer.getData());
    }

	public TipifiedListItem[] getListItemsFilter(String listId, String filterId)
		throws SessionExpiredException, BigBangException
	{
		ArrayList<TipifiedListItem> larrAux;
		Entity lrefFormats;
        MasterDB ldb;
        ResultSet lrsFormats;
		ObjectBase lobjFormat;
		TipifiedListItem lobjAux;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( !ObjectGUIDs.O_FileSpec.equals(UUID.fromString(listId)) )
			throw new BigBangException("Erro: Lista inválida para o espaço de trabalho.");

		larrAux = new ArrayList<TipifiedListItem>();

		try
		{
			lrefFormats = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_FileSpec));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

        try
        {
	        lrsFormats = lrefFormats.SelectByMembers(ldb, new int[] {0}, new java.lang.Object[] {filterId}, new int[] {0});
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
	        while (lrsFormats.next())
	        {
	        	lobjFormat = Engine.GetWorkInstance(lrefFormats.getKey(), lrsFormats);
	        	lobjAux = new TipifiedListItem();
	        	lobjAux.id = lobjFormat.getKey().toString();
	        	lobjAux.value = lobjFormat.getLabel();
	        	larrAux.add(lobjAux);
	        }
        }
        catch (Throwable e)
        {
			try { lrsFormats.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
        	throw new BigBangException(e.getMessage(), e);
        }

        try
        {
        	lrsFormats.close();
        }
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return larrAux.toArray(new TipifiedListItem[larrAux.size()]);
	}

	public void process(String formatId, String storageId)
		 throws SessionExpiredException, BigBangException
	{
		UUID lidFormat;
		FileProcesser lobjProc;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lidFormat = UUID.fromString(formatId);

		try
		{
			lobjProc = FileProcesser.GetProcesserForFormat(lidFormat);
		}
		catch (Throwable e)
		{
			try { Discard(storageId); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		if ( lobjProc == null )
			throw new BigBangException("Erro: Não existe processador para esse formato.");

		try
		{
			lobjProc.Process(GetFileXferStorage().get(UUID.fromString(storageId)), lidFormat);
		}
		catch (BigBangJewelException e)
		{
			try { Discard(storageId); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		GetFileXferStorage().remove(storageId);
	}

	public void Discard(String pstrID)
		throws SessionExpiredException, BigBangException 
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			GetFileXferStorage().remove(UUID.fromString(pstrID));
		}
		catch(Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	public void Discard(String[] parrIDs)
		throws SessionExpiredException, BigBangException
	{
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( parrIDs == null )
			return;

		for ( i = 0; i < parrIDs.length; i++ )
			Discard(parrIDs[i]);
	}
}
