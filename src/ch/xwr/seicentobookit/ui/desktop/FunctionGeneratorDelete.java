
package ch.xwr.seicentobookit.ui.desktop;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.xdev.res.ApplicationResource;
import com.xdev.ui.XdevButton;
import com.xdev.ui.XdevHorizontalLayout;
import com.xdev.ui.entitycomponent.table.XdevTable;

import ch.xwr.seicentobookit.business.RowObjectManager;
import ch.xwr.seicentobookit.dal.BankDAO;
import ch.xwr.seicentobookit.entities.Bank;

public class FunctionGeneratorDelete extends XdevHorizontalLayout {

	public static class Generator implements ColumnGenerator {
		@Override
		public Object generateCell(Table table, Object itemId, Object columnId) {

			return new FunctionGeneratorDelete(table, itemId, columnId);
		}
	}

	private final Table customizedTable;
	private final Object itemId;
	private final Object columnId;

	private FunctionGeneratorDelete(Table customizedTable, Object itemId, Object columnId) {
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
	public Bank getBean() {
		return ((XdevTable<Bank>) getTable()).getBeanContainerDataSource().getItem(getItemId()).getBean();
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
		
		//Delete Record
		RowObjectManager man = new RowObjectManager();
		man.deleteObject(getBean().getBnkId(), getBean().getClass().getSimpleName());
		
		//Delete Record
		BankDAO dao = new BankDAO();
		dao.remove(getBean());
		//refresh
		((XdevTable<Bank>) getTable()).getBeanContainerDataSource().refresh(); 		
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

	/*
	 * WARNING: Do NOT edit!<br>The content of this method is always regenerated
	 * by the UI designer.
	 */
	// <generated-code name="initUI">
	private void initUI() {
		this.button = new XdevButton();
	
		this.setSpacing(false);
		this.setMargin(new MarginInfo(false));
		this.button
				.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/delete32.png"));
	
		this.button.setSizeUndefined();
		this.addComponent(this.button);
		this.setComponentAlignment(this.button, Alignment.MIDDLE_CENTER);
		this.setSizeUndefined();
	
		this.addLayoutClickListener(event -> this.this_layoutClick(event));
		button.addClickListener(event -> this.button_buttonClick(event));
	} // </generated-code>

	// <generated-code name="variables">
	private XdevButton button; // </generated-code>

}
