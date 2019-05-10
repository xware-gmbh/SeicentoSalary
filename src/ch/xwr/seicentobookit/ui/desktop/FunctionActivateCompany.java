
package ch.xwr.seicentobookit.ui.desktop;

import java.util.Iterator;
import java.util.List;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.ClientConnector;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.xdev.ui.XdevButton;
import com.xdev.ui.XdevHorizontalLayout;
import com.xdev.ui.entitycomponent.table.XdevTable;

import ch.xwr.seicentobookit.business.LovState;
import ch.xwr.seicentobookit.dal.CompanyDAO;
import ch.xwr.seicentobookit.entities.Company;

public class FunctionActivateCompany extends XdevHorizontalLayout {

	public static class Generator implements ColumnGenerator {
		@Override
		public Object generateCell(Table table, Object itemId, Object columnId) {

			return new FunctionActivateCompany(table, itemId, columnId);
		}
	}

	private final Table customizedTable;
	private final Object itemId;
	private final Object columnId;

	private FunctionActivateCompany(Table customizedTable, Object itemId, Object columnId) {
		super();

		this.customizedTable = customizedTable;
		this.itemId = itemId;
		this.columnId = columnId;

		this.initUI();
	}

	public Table getTable() {
		return customizedTable;
	}

	public Object getItemId() {
		return itemId;
	}

	public Object getColumnId() {
		return columnId;
	}

	@SuppressWarnings("unchecked")
	public Company getBean() {
		return ((XdevTable<Company>) getTable()).getBeanContainerDataSource().getItem(getItemId()).getBean();
	}

	/**
	 * Event handler delegate method for the {@link XdevButton} {@link #button}.
	 *
	 * @see ClickListener#buttonClick(ClickEvent)
	 * @eventHandlerDelegate
	 */
	@SuppressWarnings("unchecked")
	private void button_buttonClick(ClickEvent event) {
		selectItem();
		Notification.show("Clicked in line, id = " + getItemId());
		
		Company bean = getBean();
		if (bean.getCmpState().equals(LovState.State.active)) {
			Notification.show("Konfiguration ist bereits aktiv");			
		}
		
		//Delete Record
		CompanyDAO cmpDao = new CompanyDAO();		
		List<Company> slrs = cmpDao.findAll();

		for (Iterator<Company> iterator = slrs.iterator(); iterator.hasNext();) {
			Company cmp = iterator.next();
			cmp.setCmpState(LovState.State.inactive);
			if (cmp.getCmpId() == (long) getItemId()) {
				cmp.setCmpState(LovState.State.active);
			}
			cmpDao.save(cmp);
		}

		//refresh
		((XdevTable<Company>) getTable()).getBeanContainerDataSource().refresh(); 		
	}

	/**
	 * Event handler delegate method for the {@link XdevHorizontalLayout}.
	 *
	 * @see LayoutClickListener#layoutClick(LayoutClickEvent)
	 * @eventHandlerDelegate
	 */
	private void this_layoutClick(LayoutClickEvent event) {
		selectItem();
	}

	private void selectItem() {
		getTable().select(getItemId());
	}

	/**
	 * Event handler delegate method for the {@link XdevButton}
	 * {@link #button}.
	 *
	 * @see ClientConnector.AttachListener#attach(ClientConnector.AttachEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void button_attach(ClientConnector.AttachEvent event) {		
		if (getBean().getCmpState().equals(LovState.State.active)) {
			//System.out.println("aktiv....");
			button.setVisible(false);
			//button.setEnabled(false);
		}
	}

	/*
	 * WARNING: Do NOT edit!<br>The content of this method is always regenerated
	 * by the UI designer.
	 */
	// <generated-code name="initUI">
	private void initUI() {
		this.button = new XdevButton();
	
		this.setSpacing(false);
		this.setMargin(new MarginInfo(false));
		this.button.setCaption("Aktivieren");
	
		this.button.setSizeUndefined();
		this.addComponent(this.button);
		this.setComponentAlignment(this.button, Alignment.MIDDLE_CENTER);
		this.setSizeUndefined();
	
		this.addLayoutClickListener(event -> this.this_layoutClick(event));
		button.addClickListener(event -> this.button_buttonClick(event));
		button.addAttachListener(event -> this.button_attach(event));
	} // </generated-code>

	// <generated-code name="variables">
	private XdevButton button; // </generated-code>

}
