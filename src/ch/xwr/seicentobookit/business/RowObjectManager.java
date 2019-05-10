package ch.xwr.seicentobookit.business;

import java.util.Date;

import com.vaadin.server.VaadinSession;
import com.xdev.persistence.PersistenceUtils;
import com.xdev.security.authorization.Subject;
import com.xdev.server.aa.openid.auth.AzureUser;

import ch.xwr.seicentobookit.dal.RowEntityDAO;
import ch.xwr.seicentobookit.dal.RowObjectDAO;
import ch.xwr.seicentobookit.entities.RowEntity;
import ch.xwr.seicentobookit.entities.RowObject;

public class RowObjectManager {
	private String username = null;
	
	public void updateObject(Long id, String name) {
		RowEntityDAO entDao = new RowEntityDAO();
		RowEntity ent = entDao.findEntity(name);
		if (ent.getEntHasrowobject() == false) return; //no Objectroot
				
		RowObject obj = getRowObject(name, id, ent);
		obj.setObjChanged(new Date());
		obj.setObjChangedBy(getUserName());
		obj.setObjChngcnt(obj.getObjChngcnt() + 1);
		
		PersistenceUtils.getEntityManager(RowObject.class).persist(obj);		
	}

	private RowObject getRowObject(String name, Long id, RowEntity ent) {
		RowObjectDAO objDao = new RowObjectDAO();
		RowObject obj = objDao.getObjectBase(name, id);
		
		if (obj == null) {
			obj = new RowObject();
			obj.setObjAdded(new Date());
			obj.setObjAddedBy(getUserName());
			obj.setObjRowId(id);
			obj.setObjChngcnt((long) 0);
			obj.setObjState(LovState.State.active);
			obj.setRowEntity(ent);
			obj.setDatabaseVersion(null);
		}
		if (obj.getObjChngcnt() == null) obj.setObjChngcnt((long) 0);
		
		return obj;
	}

	public void deleteObject(Long id, String name) {
		RowEntityDAO entDao = new RowEntityDAO();
		RowEntity ent = entDao.findEntity(name);
		if (ent.getEntHasrowobject() == false) return; //no Objectroot
		
		RowObject obj = getRowObject(name, id, ent);
		obj.setObjDeleted(new Date());
		obj.setObjDeletedBy(getUserName());
		obj.setObjState(LovState.State.locked);
		obj.setObjChngcnt(obj.getObjChngcnt() + 1);
		
		PersistenceUtils.getEntityManager(RowObject.class).persist(obj);		
	}

	private String getUserName() {
		//if (username != null) return username;
		username = "undefined";
		
		final Subject sub = VaadinSession.getCurrent().getAttribute(Subject.class);
		if (sub != null && sub instanceof AzureUser)
		{
			AzureUser user = (AzureUser) sub;
			username = user.name();
		}

		return  username;	
	}
	
}
