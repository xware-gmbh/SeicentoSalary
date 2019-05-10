package ch.xwr.seicentobookit.ui.desktop;

import com.vaadin.data.Property;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.xdev.dal.DAOs;
import com.xdev.res.ApplicationResource;
import com.xdev.res.StringResourceUtils;
import com.xdev.ui.XdevButton;
import com.xdev.ui.XdevFieldGroup;
import com.xdev.ui.XdevGridLayout;
import com.xdev.ui.XdevHorizontalLayout;
import com.xdev.ui.XdevHorizontalSplitPanel;
import com.xdev.ui.XdevLabel;
import com.xdev.ui.XdevPanel;
import com.xdev.ui.XdevTabSheet;
import com.xdev.ui.XdevTextField;
import com.xdev.ui.XdevVerticalLayout;
import com.xdev.ui.XdevView;
import com.xdev.ui.entitycomponent.combobox.XdevComboBox;
import com.xdev.ui.entitycomponent.table.XdevTable;
import com.xdev.ui.filter.FilterData;
import com.xdev.ui.filter.FilterOperator;
import com.xdev.ui.filter.XdevContainerFilterComponent;
import com.xdev.ui.masterdetail.MasterDetail;

import ch.xwr.seicentobookit.business.ConfirmDialog;
import ch.xwr.seicentobookit.business.LovState;
import ch.xwr.seicentobookit.business.RowObjectManager;
import ch.xwr.seicentobookit.dal.BankDAO;
import ch.xwr.seicentobookit.entities.Bank;
import ch.xwr.seicentobookit.entities.Bank_;
import ch.xwr.seicentobookit.entities.Employee;
import ch.xwr.seicentobookit.entities.Employee_;

public class BankTabView extends XdevView {

	/**
	 * 
	 */
	public BankTabView() {
		super();
		this.initUI();
		
		//Type
		comboBox.addItems((Object[])LovState.State.values());
		
		setDefaultFilter();
		setROFields();
	}

	private void setROFields() {
		//this.textAge.setEnabled(false);
		
		boolean hasData = true;
		if (this.fieldGroup.getItemDataSource() == null) {
			hasData = false;
		}
		this.cmdSave.setEnabled(hasData);
		this.tabSheet.setEnabled(hasData);
		
	}
	private void setDefaultFilter() {
		LovState.State[] valState = new LovState.State[]{LovState.State.active};
		FilterData[] fd = new FilterData[]{new FilterData("bnkState", new FilterOperator.Is(), valState)};
				
		this.containerFilterComponent.setFilterData(fd);
	}

	private boolean isNew() {
		if (this.fieldGroup.getItemDataSource() == null) {
			return true;
		}
		final Bank bean = this.fieldGroup.getItemDataSource().getBean();
		if (bean.getBnkId() < 1) {
			return true;
		}
		return false;
	}

	/**
	 * Event handler delegate method for the {@link XdevButton}
	 * {@link #cmdSave}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdSave_buttonClick(Button.ClickEvent event) {
		this.fieldGroup.save();
		
		RowObjectManager man = new RowObjectManager();
		man.updateObject(this.fieldGroup.getItemDataSource().getBean().getBnkId(), this.fieldGroup.getItemDataSource().getBean().getClass().getSimpleName());
		
		Notification.show("Save clicked", "Daten wurden gespeichert", Notification.Type.TRAY_NOTIFICATION);
	}


	/**
	 * Event handler delegate method for the {@link XdevTable}
	 * {@link #table}.
	 *
	 * @see Property.ValueChangeListener#valueChange(Property.ValueChangeEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void table_valueChange(Property.ValueChangeEvent event) {
//		tableRefEmployee.getBeanContainerDataSource().addContainerFilter(new Compare.Equal("bank", fieldGroup.getItemDataSource().getBean()));
//		tableRefEmployee.setAutoQueryData(true);
//		tableRefEmployee.getBeanContainerDataSource().refresh();
		setROFields();
	}

	/**
	 * Event handler delegate method for the {@link XdevButton}
	 * {@link #cmdInfo}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdInfo_buttonClick(Button.ClickEvent event) {
		Bank bean = this.fieldGroup.getItemDataSource().getBean();
		
		Window win = new Window();
		win.setWidth("1020");
		win.setHeight("700");		
		win.center();
		win.setModal(true);
		
		//UI.getCurrent().getSession().setAttribute(String.class, bean.getClass().getSimpleName());
		win.setContent(new RowObjectView(bean.getBnkId(), bean.getClass().getSimpleName()));
		this.getUI().addWindow(win);		
	}



	/**
	 * Event handler delegate method for the {@link XdevTable}
	 * {@link #tableRefEmployee}.
	 *
	 * @see Property.ValueChangeListener#valueChange(Property.ValueChangeEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void tableRefEmployee_valueChange(Property.ValueChangeEvent event) {
	
	}

	/**
	 * Event handler delegate method for the {@link XdevButton}
	 * {@link #cmdNew}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdNew_buttonClick(Button.ClickEvent event) {
		this.fieldGroup.setItemDataSource(getNewBeanWithDefaults());
	
		setROFields();
	}

	private Bank getNewBeanWithDefaults() {
		Bank bean = new Bank();
		
		bean.setBnkState(LovState.State.active);
		
		return bean;
	}

	/**
	 * Event handler delegate method for the {@link XdevButton}
	 * {@link #cmdDelete}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdDelete_buttonClick(Button.ClickEvent event) {
		if (table.getSelectedItem() == null) {
			Notification.show("Datensatz löschen", "Es wurde keine Zeile selektiert in der Tabelle",
					Notification.Type.WARNING_MESSAGE);
			return;
		}
		
		ConfirmDialog.show(getUI(), "Datensatz löschen", "Wirklich löschen?", new CloseListener() {
			@Override
			public void windowClose(CloseEvent e) {
				String retval = UI.getCurrent().getSession().getAttribute(String.class);
				if (retval == null) retval = "cmdCancel";
				
				if (retval.equals("cmdOk")) doDelete();
			}

			private void doDelete() {
				Bank bean = table.getSelectedItem().getBean();
				// Salary bean = this.fieldGroup.getItemDataSource().getBean();
				// Delete Record
				RowObjectManager man = new RowObjectManager();
				man.deleteObject(bean.getBnkId(), bean.getClass().getSimpleName());
			
				// JPADAO x = new JPADAO<Salary, Long>(Salary.class); //generic??
				// x.remove(bean);
			
				BankDAO dao = new BankDAO();
				dao.remove(bean);
				table.getBeanContainerDataSource().refresh();
			
				table.select(table.getCurrentPageFirstItemId());
				Notification.show("Datensatz löschen", "Datensatz wurde gelöscht!", Notification.Type.TRAY_NOTIFICATION);				
			}
			
		});		
	}

	/**
	 * Event handler delegate method for the {@link XdevButton}
	 * {@link #cmdReload}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdReload_buttonClick(Button.ClickEvent event) {
		table.refreshRowCache();
		table.getBeanContainerDataSource().refresh();
		table.sort();
	
	}

	/**
	 * Event handler delegate method for the {@link XdevButton}
	 * {@link #cmdReset}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdReset_buttonClick(Button.ClickEvent event) {
		if (isNew()) {
			cmdNew_buttonClick(event);
		} else {
			this.fieldGroup.discard();
		}
		setROFields();
	}

	/*
	 * WARNING: Do NOT edit!<br>The content of this method is always regenerated by
	 * the UI designer.
	 */
	// <generated-code name="initUI">
	private void initUI() {
		this.horizontalSplitPanel = new XdevHorizontalSplitPanel();
		this.verticalLayout = new XdevVerticalLayout();
		this.containerFilterComponent = new XdevContainerFilterComponent();
		this.actionLayout = new XdevHorizontalLayout();
		this.cmdNew = new XdevButton();
		this.cmdDelete = new XdevButton();
		this.cmdReload = new XdevButton();
		this.cmdInfo = new XdevButton();
		this.table = new XdevTable<>();
		this.form = new XdevGridLayout();
		this.tabSheet = new XdevTabSheet();
		this.gridLayout = new XdevGridLayout();
		this.label = new XdevLabel();
		this.textField = new XdevTextField();
		this.label2 = new XdevLabel();
		this.textField2 = new XdevTextField();
		this.label3 = new XdevLabel();
		this.textField3 = new XdevTextField();
		this.label4 = new XdevLabel();
		this.textField4 = new XdevTextField();
		this.label5 = new XdevLabel();
		this.textField5 = new XdevTextField();
		this.label6 = new XdevLabel();
		this.textField6 = new XdevTextField();
		this.label7 = new XdevLabel();
		this.comboBox = new XdevComboBox<>();
		this.panel = new XdevPanel();
		this.tableRefEmployee = new XdevTable<>();
		this.horizontalLayout = new XdevHorizontalLayout();
		this.cmdSave = new XdevButton();
		this.cmdReset = new XdevButton();
		this.fieldGroup = new XdevFieldGroup<>(Bank.class);
	
		this.horizontalSplitPanel.setSplitPosition(33.0F, Unit.PERCENTAGE);
		this.verticalLayout.setMargin(new MarginInfo(false));
		this.actionLayout.setSpacing(false);
		this.actionLayout.setMargin(new MarginInfo(false));
		this.cmdNew.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/new1_16.png"));
		this.cmdNew.setDescription("Neuen Datensatz anlegen");
		this.cmdNew.setStyleName("icon-only");
		this.cmdDelete
				.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/delete3_16.png"));
		this.cmdDelete.setCaption("");
		this.cmdDelete.setDescription("Markierte Zeile löschen");
		this.cmdDelete.setStyleName("icon-only");
		this.cmdReload.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/reload2.png"));
		this.cmdReload.setDescription("Liste neu laden");
		this.cmdReload.setStyleName("icon-only");
		this.cmdInfo
				.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/info_small.jpg"));
		this.cmdInfo.setStyleName("icon-only");
		this.table.setColumnReorderingAllowed(true);
		this.table.setColumnCollapsingAllowed(true);
		this.table.setContainerDataSource(Bank.class, DAOs.get(BankDAO.class).findAll());
		this.table.setVisibleColumns(Bank_.bnkName.getName(), Bank_.bnkZip.getName(), Bank_.bnkPlace.getName(),
				Bank_.bnkState.getName(), Bank_.bnkSwift.getName(), Bank_.bnkClearing.getName());
		this.table.setColumnHeader("bnkName", "Name");
		this.table.setColumnHeader("bnkZip", "Plz");
		this.table.setColumnHeader("bnkPlace", "Ort");
		this.table.setColumnHeader("bnkState", "Status");
		this.table.setColumnHeader("bnkSwift", "Swift");
		this.table.setColumnCollapsed("bnkSwift", true);
		this.table.setColumnHeader("bnkClearing", "Clearing");
		this.table.setColumnCollapsed("bnkClearing", true);
		this.form.setMargin(new MarginInfo(false));
		this.tabSheet.setStyleName("framed");
		this.label.setValue(StringResourceUtils.optLocalizeString("{$label.value}", this));
		this.textField.setRequired(true);
		this.textField.setMaxLength(50);
		this.label2.setValue(StringResourceUtils.optLocalizeString("{$label2.value}", this));
		this.textField2.setMaxLength(50);
		this.label3.setValue(StringResourceUtils.optLocalizeString("{$label3.value}", this));
		this.textField3.setMaxLength(30);
		this.label4.setValue(StringResourceUtils.optLocalizeString("{$label4.value}", this));
		this.textField4.setMaxLength(50);
		this.label5.setValue(StringResourceUtils.optLocalizeString("{$label5.value}", this));
		this.textField5.setMaxLength(50);
		this.label6.setValue(StringResourceUtils.optLocalizeString("{$label6.value}", this));
		this.textField6.setMaxLength(50);
		this.label7.setValue(StringResourceUtils.optLocalizeString("{$label7.value}", this));
		this.comboBox.setNullSelectionAllowed(false);
		this.panel.setCaption("Referenzen");
		this.tableRefEmployee.setContainerDataSource(Employee.class, false);
		this.tableRefEmployee.setVisibleColumns(Employee_.empNumber.getName(), Employee_.empLastName.getName(),
				Employee_.empFirstName.getName(), Employee_.empZip.getName(), Employee_.empPlace.getName());
		this.tableRefEmployee.setColumnHeader("empNumber", "Nummer");
		this.tableRefEmployee.setColumnHeader("empLastName", "Name");
		this.tableRefEmployee.setColumnHeader("empFirstName", "Vorname");
		this.tableRefEmployee.setColumnHeader("empZip", "PLZ");
		this.tableRefEmployee.setColumnHeader("empPlace", "Ort");
		this.horizontalLayout.setMargin(new MarginInfo(false, true, false, true));
		this.cmdSave.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/save1.png"));
		this.cmdSave.setCaption(StringResourceUtils.optLocalizeString("{$cmdSave.caption}", this));
		this.cmdSave.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		this.cmdReset.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/delete1.png"));
		this.cmdReset.setCaption(StringResourceUtils.optLocalizeString("{$cmdReset.caption}", this));
		this.cmdReset.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
		this.fieldGroup.bind(this.textField, Bank_.bnkName.getName());
		this.fieldGroup.bind(this.textField2, Bank_.bnkAddress.getName());
		this.fieldGroup.bind(this.textField3, Bank_.bnkZip.getName());
		this.fieldGroup.bind(this.textField4, Bank_.bnkPlace.getName());
		this.fieldGroup.bind(this.textField5, Bank_.bnkSwift.getName());
		this.fieldGroup.bind(this.textField6, Bank_.bnkClearing.getName());
		this.fieldGroup.bind(this.comboBox, Bank_.bnkState.getName());
	
		MasterDetail.connect(this.table, this.fieldGroup);
		MasterDetail.connect(this.table, this.tableRefEmployee);
	
		this.containerFilterComponent.setContainer(this.table.getBeanContainerDataSource(), "bnkName", "bnkState", "bnkZip",
				"bnkSwift", "bnkClearing");
		this.containerFilterComponent.setSearchableProperties("bnkName", "bnkPlace", "bnkSwift");
	
		this.cmdNew.setSizeUndefined();
		this.actionLayout.addComponent(this.cmdNew);
		this.actionLayout.setComponentAlignment(this.cmdNew, Alignment.TOP_RIGHT);
		this.cmdDelete.setSizeUndefined();
		this.actionLayout.addComponent(this.cmdDelete);
		this.actionLayout.setComponentAlignment(this.cmdDelete, Alignment.TOP_RIGHT);
		this.cmdReload.setSizeUndefined();
		this.actionLayout.addComponent(this.cmdReload);
		this.actionLayout.setComponentAlignment(this.cmdReload, Alignment.MIDDLE_CENTER);
		this.cmdInfo.setSizeUndefined();
		this.actionLayout.addComponent(this.cmdInfo);
		this.actionLayout.setComponentAlignment(this.cmdInfo, Alignment.MIDDLE_CENTER);
		final CustomComponent actionLayout_spacer = new CustomComponent();
		actionLayout_spacer.setSizeFull();
		this.actionLayout.addComponent(actionLayout_spacer);
		this.actionLayout.setExpandRatio(actionLayout_spacer, 1.0F);
		this.containerFilterComponent.setWidth(100, Unit.PERCENTAGE);
		this.containerFilterComponent.setHeight(-1, Unit.PIXELS);
		this.verticalLayout.addComponent(this.containerFilterComponent);
		this.verticalLayout.setComponentAlignment(this.containerFilterComponent, Alignment.TOP_CENTER);
		this.actionLayout.setWidth(100, Unit.PERCENTAGE);
		this.actionLayout.setHeight(-1, Unit.PIXELS);
		this.verticalLayout.addComponent(this.actionLayout);
		this.verticalLayout.setComponentAlignment(this.actionLayout, Alignment.MIDDLE_CENTER);
		this.table.setSizeFull();
		this.verticalLayout.addComponent(this.table);
		this.verticalLayout.setComponentAlignment(this.table, Alignment.MIDDLE_CENTER);
		this.verticalLayout.setExpandRatio(this.table, 10.0F);
		this.tableRefEmployee.setSizeFull();
		this.panel.setContent(this.tableRefEmployee);
		this.gridLayout.setColumns(3);
		this.gridLayout.setRows(8);
		this.label.setSizeUndefined();
		this.gridLayout.addComponent(this.label, 0, 0);
		this.textField.setWidth(100, Unit.PERCENTAGE);
		this.textField.setHeight(-1, Unit.PIXELS);
		this.gridLayout.addComponent(this.textField, 2, 0);
		this.label2.setSizeUndefined();
		this.gridLayout.addComponent(this.label2, 0, 1);
		this.textField2.setWidth(100, Unit.PERCENTAGE);
		this.textField2.setHeight(-1, Unit.PIXELS);
		this.gridLayout.addComponent(this.textField2, 2, 1);
		this.label3.setSizeUndefined();
		this.gridLayout.addComponent(this.label3, 0, 2);
		this.textField3.setSizeUndefined();
		this.gridLayout.addComponent(this.textField3, 2, 2);
		this.label4.setSizeUndefined();
		this.gridLayout.addComponent(this.label4, 0, 3);
		this.textField4.setSizeUndefined();
		this.gridLayout.addComponent(this.textField4, 2, 3);
		this.label5.setSizeUndefined();
		this.gridLayout.addComponent(this.label5, 0, 4);
		this.textField5.setSizeUndefined();
		this.gridLayout.addComponent(this.textField5, 2, 4);
		this.label6.setSizeUndefined();
		this.gridLayout.addComponent(this.label6, 0, 5);
		this.textField6.setSizeUndefined();
		this.gridLayout.addComponent(this.textField6, 2, 5);
		this.label7.setSizeUndefined();
		this.gridLayout.addComponent(this.label7, 0, 6);
		this.comboBox.setSizeUndefined();
		this.gridLayout.addComponent(this.comboBox, 2, 6);
		this.panel.setSizeFull();
		this.gridLayout.addComponent(this.panel, 1, 7, 2, 7);
		this.gridLayout.setColumnExpandRatio(0, 10.0F);
		this.gridLayout.setColumnExpandRatio(2, 100.0F);
		this.gridLayout.setRowExpandRatio(7, 2.0F);
		this.gridLayout.setSizeFull();
		this.tabSheet.addTab(this.gridLayout, "Daten", null);
		this.tabSheet.setSelectedTab(this.gridLayout);
		this.cmdSave.setSizeUndefined();
		this.horizontalLayout.addComponent(this.cmdSave);
		this.horizontalLayout.setComponentAlignment(this.cmdSave, Alignment.MIDDLE_CENTER);
		this.cmdReset.setSizeUndefined();
		this.horizontalLayout.addComponent(this.cmdReset);
		this.horizontalLayout.setComponentAlignment(this.cmdReset, Alignment.MIDDLE_RIGHT);
		this.form.setColumns(2);
		this.form.setRows(2);
		this.tabSheet.setSizeFull();
		this.form.addComponent(this.tabSheet, 1, 0);
		this.horizontalLayout.setSizeUndefined();
		this.form.addComponent(this.horizontalLayout, 0, 1, 1, 1);
		this.form.setComponentAlignment(this.horizontalLayout, Alignment.MIDDLE_CENTER);
		this.form.setColumnExpandRatio(1, 100.0F);
		this.form.setRowExpandRatio(0, 100.0F);
		this.verticalLayout.setSizeFull();
		this.horizontalSplitPanel.setFirstComponent(this.verticalLayout);
		this.form.setSizeFull();
		this.horizontalSplitPanel.setSecondComponent(this.form);
		this.horizontalSplitPanel.setSizeFull();
		this.setContent(this.horizontalSplitPanel);
		this.setSizeFull();
	
		this.cmdNew.addClickListener(event -> this.cmdNew_buttonClick(event));
		this.cmdDelete.addClickListener(event -> this.cmdDelete_buttonClick(event));
		this.cmdReload.addClickListener(event -> this.cmdReload_buttonClick(event));
		this.cmdInfo.addClickListener(event -> this.cmdInfo_buttonClick(event));
		this.table.addValueChangeListener(event -> this.table_valueChange(event));
		this.tableRefEmployee.addValueChangeListener(event -> this.tableRefEmployee_valueChange(event));
		this.cmdSave.addClickListener(event -> this.cmdSave_buttonClick(event));
		this.cmdReset.addClickListener(event -> this.cmdReset_buttonClick(event));
	} // </generated-code>

	// <generated-code name="variables">
	private XdevButton cmdNew, cmdDelete, cmdReload, cmdSave, cmdReset, cmdInfo;
	private XdevLabel label, label2, label3, label4, label5, label6, label7;
	private XdevTable<Employee> tableRefEmployee;
	private XdevTabSheet tabSheet;
	private XdevPanel panel;
	private XdevGridLayout form, gridLayout;
	private XdevHorizontalSplitPanel horizontalSplitPanel;
	private XdevComboBox<CustomComponent> comboBox;
	private XdevContainerFilterComponent containerFilterComponent;
	private XdevHorizontalLayout actionLayout, horizontalLayout;
	private XdevTextField textField, textField2, textField3, textField4, textField5, textField6;
	private XdevVerticalLayout verticalLayout;
	private XdevTable<Bank> table;
	private XdevFieldGroup<Bank> fieldGroup; // </generated-code>


}
