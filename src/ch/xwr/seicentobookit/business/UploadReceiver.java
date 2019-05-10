package ch.xwr.seicentobookit.business;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

import ch.xwr.seicentobookit.dal.RowFileDAO;
import ch.xwr.seicentobookit.entities.RowFile;

public class UploadReceiver implements Receiver, SucceededListener  {
	public Long rowId;
	
	RowFile rowfile = null;
	ByteArrayOutputStream baos = null;

	
	//Constructor
	public UploadReceiver(Long itemId) {
		rowId = itemId;
		
		RowFileDAO dao = new RowFileDAO();
		rowfile = dao.find(rowId);
		
	}
	
	public UploadReceiver(RowFile rfile) {
		rowfile = rfile;
		
	}

	/**
	 * Bean	
	 * @return
	 */
	public RowFile getBean() {
		return rowfile;
	}
	
	public OutputStream receiveUpload(String filename, String mimeType) {		
	    System.out.println("________________ UPLOAD Receiver x");
	    if(mimeType.length() > 55)
	        mimeType = mimeType.substring(0,55);
	    	    
		rowfile.setRimName(filename);
		rowfile.setRimMimetype(mimeType);
		
		baos = new ByteArrayOutputStream();
		//DataOutputStream w = new DataOutputStream(baos);
		
		return baos; 
	}
	
	@Override
    public void uploadSucceeded(SucceededEvent event) {
	    System.out.println("________________ UPLOAD SUCCEEDED x");
	    
		rowfile.setRimSize(baos.size() + " Bytes");
		rowfile.setRimFile(baos.toByteArray());
    }

}
