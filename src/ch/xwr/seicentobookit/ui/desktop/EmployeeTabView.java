package ch.xwr.seicentobookit.ui.desktop;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.xdev.dal.DAOs;
import com.xdev.reports.tableexport.ui.TableExportPopup;
import com.xdev.res.ApplicationResource;
import com.xdev.res.StringResourceUtils;
import com.xdev.ui.XdevButton;
import com.xdev.ui.XdevCheckBox;
import com.xdev.ui.XdevFieldGroup;
import com.xdev.ui.XdevGridLayout;
import com.xdev.ui.XdevHorizontalLayout;
import com.xdev.ui.XdevHorizontalSplitPanel;
import com.xdev.ui.XdevLabel;
import com.xdev.ui.XdevPanel;
import com.xdev.ui.XdevPopupDateField;
import com.xdev.ui.XdevTabSheet;
import com.xdev.ui.XdevTextField;
import com.xdev.ui.XdevVerticalLayout;
import com.xdev.ui.XdevView;
import com.xdev.ui.entitycomponent.XdevBeanContainer;
import com.xdev.ui.entitycomponent.combobox.XdevComboBox;
import com.xdev.ui.entitycomponent.table.XdevTable;
import com.xdev.ui.filter.FilterData;
import com.xdev.ui.filter.FilterOperator;
import com.xdev.ui.filter.XdevContainerFilterComponent;
import com.xdev.ui.masterdetail.MasterDetail;
import com.xdev.util.ConverterBuilder;

import ch.xwr.seicentobookit.business.ConfirmDialog;
import ch.xwr.seicentobookit.business.LovState;
import ch.xwr.seicentobookit.business.RowObjectManager;
import ch.xwr.seicentobookit.dal.BankDAO;
import ch.xwr.seicentobookit.dal.EmployeeDAO;
import ch.xwr.seicentobookit.dal.SalaryBvgBaseDAO;
import ch.xwr.seicentobookit.dal.SalaryCalculationDAO;
import ch.xwr.seicentobookit.dal.WorkRoleDAO;
import ch.xwr.seicentobookit.entities.Bank;
import ch.xwr.seicentobookit.entities.Bank_;
import ch.xwr.seicentobookit.entities.Employee;
import ch.xwr.seicentobookit.entities.Employee_;
import ch.xwr.seicentobookit.entities.SalaryBvgBase;
import ch.xwr.seicentobookit.entities.SalaryBvgBase_;
import ch.xwr.seicentobookit.entities.SalaryCalculation;
import ch.xwr.seicentobookit.entities.SalaryCalculation_;
import ch.xwr.seicentobookit.entities.WorkRole;
import ch.xwr.seicentobookit.entities.WorkRole_;

public class EmployeeTabView extends XdevView {

	/**
	 * 
	 */
	public EmployeeTabView() {
		super();
		this.initUI();
		
		//dummy (hight get lost)
		this.tabSheet.setWidth(100, Unit.PERCENTAGE);
		this.tabSheet.setHeight(-1, Unit.PIXELS);
		
		//Type		
		comboBoxCivilState.addItems((Object[])Employee.CivilState.values());		
		//State
		comboBoxState.addItems((Object[])LovState.State.values());
		
		//Filtering
		setDefaultFilter();
		setROFields();
		
	}
	
	private void setROFields() {
		this.textAge.setEnabled(false);
		
		boolean hasData = true;
		if (this.fieldGroup.getItemDataSource() == null) {
			hasData = false;
		}
		this.cmdSave.setEnabled(hasData);
		this.tabSheet.setEnabled(hasData);
		
	}
	
	private void setDefaultFilter() {
		//Equal filter = new Equal("empLastName", "Muri");
//		Equal filter = new Equal("empState", LovState.State.active);
//		this.containerFilterComponent.getContainer().addContainerFilter(filter);
		
//		String[] val = new String[]{"Muri"};
//		FilterData[] fd = new FilterData[]{new FilterData("empLastName", new FilterOperator.Equals(), val)};
		
		LovState.State[] valState = new LovState.State[]{LovState.State.active};
		FilterData[] fd = new FilterData[]{new FilterData("empState", new FilterOperator.Is(), valState)};
				
		this.containerFilterComponent.setFilterData(fd);
	}

	private Employee getNewBeanWithDefaults() {
		Employee bean = new Employee();
		
		bean.setEmpState(LovState.State.active);
		bean.setEmpStartwork(new Date());
		bean.setEmpCivilState(Employee.CivilState.Ledig);
		bean.setEmpKidsAddition(0.);
		bean.setEmpBaseSalary(new Double(3000.));
		
		List<WorkRole> wrklst =  new WorkRoleDAO().findAll();
		if (! wrklst.isEmpty()) {
			bean.setWorkRole(wrklst.get(0));
		}
		List<SalaryBvgBase> bvglst = new SalaryBvgBaseDAO().findAll();
		if (! bvglst.isEmpty()) {
			bean.setSalaryBvgBase(bvglst.get(0));
		}
		List<SalaryCalculation> callst = new SalaryCalculationDAO().findAll();
		if (! callst.isEmpty()) {
			bean.setSalaryCalculation(callst.get(0));
		}
		
		return bean;
	}
	
	private boolean isNew() {
		if (this.fieldGroup.getItemDataSource() == null) {
			return true;
		}
		final Employee bean = this.fieldGroup.getItemDataSource().getBean();
		if (bean.getEmpId() < 1) {
			return true;
		}
		return false;
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
				Employee bean = table.getSelectedItem().getBean();
				// Salary bean = this.fieldGroup.getItemDataSource().getBean();
				// Delete Record
				RowObjectManager man = new RowObjectManager();
				man.deleteObject(bean.getEmpId(), bean.getClass().getSimpleName());
			
				// JPADAO x = new JPADAO<Salary, Long>(Salary.class); //generic??
				// x.remove(bean);
			
				EmployeeDAO dao = new EmployeeDAO();
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
	
	private void reloadTableList() {
		//final Order ord = this.fieldGroup.getItemDataSource().getBean();

		final XdevBeanContainer<Employee> myList = this.table.getBeanContainerDataSource();
		myList.removeAll();
		myList.addAll(new EmployeeDAO().findAll());

		this.table.refreshRowCache();
		this.table.getBeanContainerDataSource().refresh();
		this.table.sort();
	}
	

	/**
	 * Event handler delegate method for the {@link XdevButton}
	 * {@link #cmdInfo}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdInfo_buttonClick(Button.ClickEvent event) {
		Employee bean = this.fieldGroup.getItemDataSource().getBean();
	
		Window win = new Window();
		win.setWidth("1020");
		win.setHeight("700");
		win.center();
		win.setModal(true);
	
		// UI.getCurrent().getSession().setAttribute(String.class,
		// bean.getClass().getSimpleName());
		win.setContent(new RowObjectView(bean.getEmpId(), bean.getClass().getSimpleName()));
		this.getUI().addWindow(win);
	}

	/**
	 * Event handler delegate method for the {@link XdevButton}
	 * {@link #cmdSave}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdSave_buttonClick(Button.ClickEvent event) {
	
		if (!AreFieldsValid()) {
			return;
		}
		
		try {			
			this.fieldGroup.save();
			
			RowObjectManager man = new RowObjectManager();
			man.updateObject(this.fieldGroup.getItemDataSource().getBean().getEmpId(),
					this.fieldGroup.getItemDataSource().getBean().getClass().getSimpleName());
		
			Notification.show("Save clicked", "Daten wurden gespeichert", Notification.Type.TRAY_NOTIFICATION);
						
		} catch (Exception e) {
			Notification.show("Fehler beim Speichern", e.getMessage(), Notification.Type.ERROR_MESSAGE);
			e.printStackTrace();			
		}
		reloadTableList();
		setROFields();
	}

	private boolean AreFieldsValid() {
		if (this.fieldGroup.isValid()) {
			return true;
		}
		AbstractField<T> fld =  null;
		try {
			Collection<?> flds = this.fieldGroup.getFields();
			for (Iterator<?> iterator = flds.iterator(); iterator.hasNext();) {
				fld = (AbstractField<T>) iterator.next();
				if (!fld.isValid()) {
					fld.focus();
					fld.validate();					
				}				
			}
			
		} catch (Exception e) {
			Object prop = this.fieldGroup.getPropertyId(fld);
			Notification.show("Feld ist ungültig", prop.toString(), Notification.Type.ERROR_MESSAGE);
		}
		
		return false;
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

	/**
	 * Event handler delegate method for the {@link XdevButton} {@link #cmdExport}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdExport_buttonClick(Button.ClickEvent event) {
		TableExportPopup.show(this.table);
		
//		Resource exportToResource = Report.New().jrxml("WebContent/WEB-INF/resources/reports/Employee_A4.jrxml")
//				.dataSource(com.xdev.dal.DAOs.get(ch.xwr.seicentobookit.dal.EmployeeDAO.class).findAll())
//				.exportToResource(ExportType.PDF);
//		BrowserFrame browserframe = new BrowserFrame();
//		browserframe.setSource(exportToResource);		
	    //.parameter("creator", "CK").parameter("created", new Date()).exportToResource(ExportType.PDF);		
	}

	/**
	 * Event handler delegate method for the {@link XdevTable} {@link #table}.
	 *
	 * @see Property.ValueChangeListener#valueChange(Property.ValueChangeEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void table_valueChange(final Property.ValueChangeEvent event) {
		if (this.fieldGroup.getItemDataSource() == null) {
			return;
		}

//		final Order bean2 = this.fieldGroup.getItemDataSource().getBean();
//		this.table.select(bean2);

		setROFields();	
	}

	/*
	 * WARNING: Do NOT edit!<br>The content of this method is always regenerated by
	 * the UI designer.
	 */
	// <generated-code name="initUI">
	private void initUI() {
		this.panel = new XdevPanel();
		this.horizontalSplitPanel = new XdevHorizontalSplitPanel();
		this.verticalLayoutLeft = new XdevVerticalLayout();
		this.containerFilterComponent = new XdevContainerFilterComponent();
		this.actionLayout = new XdevHorizontalLayout();
		this.cmdNew = new XdevButton();
		this.cmdDelete = new XdevButton();
		this.cmdReload = new XdevButton();
		this.cmdInfo = new XdevButton();
		this.cmdExport = new XdevButton();
		this.table = new XdevTable<>();
		this.verticalLayoutRight = new XdevVerticalLayout();
		this.gridLayout = new XdevGridLayout();
		this.tabSheet = new XdevTabSheet();
		this.gridLayout6 = new XdevGridLayout();
		this.label5 = new XdevLabel();
		this.textField = new XdevTextField();
		this.label7 = new XdevLabel();
		this.textField3 = new XdevTextField();
		this.label6 = new XdevLabel();
		this.textField2 = new XdevTextField();
		this.label8 = new XdevLabel();
		this.textField4 = new XdevTextField();
		this.label10 = new XdevLabel();
		this.textField5 = new XdevTextField();
		this.label31 = new XdevLabel();
		this.textField6 = new XdevTextField();
		this.label12 = new XdevLabel();
		this.popupDateField = new XdevPopupDateField();
		this.label13 = new XdevLabel();
		this.popupDateField2 = new XdevPopupDateField();
		this.label15 = new XdevLabel();
		this.popupDateField3 = new XdevPopupDateField();
		this.label = new XdevLabel();
		this.textAge = new XdevTextField();
		this.label4 = new XdevLabel();
		this.comboBox4 = new XdevComboBox<>();
		this.label16 = new XdevLabel();
		this.comboBoxCivilState = new XdevComboBox<>();
		this.label9 = new XdevLabel();
		this.comboBoxState = new XdevComboBox<>();
		this.gridLayout2 = new XdevGridLayout();
		this.label11 = new XdevLabel();
		this.textField7 = new XdevTextField();
		this.label14 = new XdevLabel();
		this.textField8 = new XdevTextField();
		this.label17 = new XdevLabel();
		this.textField10 = new XdevTextField();
		this.label18 = new XdevLabel();
		this.textField11 = new XdevTextField();
		this.label20 = new XdevLabel();
		this.textField12 = new XdevTextField();
		this.label2 = new XdevLabel();
		this.comboBox2 = new XdevComboBox<>();
		this.label3 = new XdevLabel();
		this.comboBox3 = new XdevComboBox<>();
		this.label19 = new XdevLabel();
		this.checkBox = new XdevCheckBox();
		this.panel3 = new XdevPanel();
		this.gridLayout4 = new XdevGridLayout();
		this.label28 = new XdevLabel();
		this.comboBox5 = new XdevComboBox<>();
		this.label29 = new XdevLabel();
		this.textField20 = new XdevTextField();
		this.label30 = new XdevLabel();
		this.textField22 = new XdevTextField();
		this.panel2 = new XdevPanel();
		this.gridLayout5 = new XdevGridLayout();
		this.label23 = new XdevLabel();
		this.textField15 = new XdevTextField();
		this.label24 = new XdevLabel();
		this.textField16 = new XdevTextField();
		this.label25 = new XdevLabel();
		this.textField17 = new XdevTextField();
		this.label26 = new XdevLabel();
		this.textField18 = new XdevTextField();
		this.label27 = new XdevLabel();
		this.textField19 = new XdevTextField();
		this.fieldGroup = new XdevFieldGroup<>(Employee.class);
		this.horizontalLayout = new XdevHorizontalLayout();
		this.cmdSave = new XdevButton();
		this.cmdReset = new XdevButton();
	
		this.horizontalSplitPanel.setSplitPosition(35.0F, Unit.PERCENTAGE);
		this.verticalLayoutLeft.setMargin(new MarginInfo(false, false, true, false));
		this.actionLayout.setSpacing(false);
		this.actionLayout.setMargin(new MarginInfo(false));
		this.cmdNew.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/new1_16.png"));
		this.cmdNew.setDescription("Neuen Datensatz hinzufügen");
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
		this.cmdExport
				.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/export16.png"));
		this.cmdExport.setStyleName("icon-only");
		this.table.setColumnReorderingAllowed(true);
		this.table.setColumnCollapsingAllowed(true);
		this.table.setContainerDataSource(Employee.class, DAOs.get(EmployeeDAO.class).findAll());
		this.table.setVisibleColumns(Employee_.empNumber.getName(), Employee_.empFirstName.getName(),
				Employee_.empLastName.getName(), Employee_.empZip.getName(), Employee_.empPlace.getName(),
				Employee_.empState.getName(), Employee_.empAddress.getName(), Employee_.empStartwork.getName(),
				Employee_.empEndWork.getName(), Employee_.empBirthday.getName(), Employee_.empAhvnbr.getName(),
				Employee_.empCostAccount.getName(), Employee_.empBankIban.getName(), Employee_.empFrmRemark.getName());
		this.table.setColumnHeader("empNumber", "Nummer");
		this.table.setConverter("empNumber", ConverterBuilder.stringToDouble().groupingUsed(false).build());
		this.table.setColumnHeader("empFirstName", "Vorname");
		this.table.setColumnHeader("empLastName", "Name");
		this.table.setColumnHeader("empZip", "PLZ");
		this.table.setColumnHeader("empPlace", "Ort");
		this.table.setColumnHeader("empState", "Status");
		this.table.setColumnHeader("empAddress", "Adresse");
		this.table.setColumnCollapsed("empAddress", true);
		this.table.setColumnHeader("empStartwork", "Eintritt");
		this.table.setColumnCollapsed("empStartwork", true);
		this.table.setColumnHeader("empEndWork", "Austritt");
		this.table.setColumnCollapsed("empEndWork", true);
		this.table.setColumnHeader("empBirthday", "Geburtstag");
		this.table.setColumnCollapsed("empBirthday", true);
		this.table.setColumnHeader("empAhvnbr", "Ahv Nr");
		this.table.setColumnCollapsed("empAhvnbr", true);
		this.table.setColumnHeader("empCostAccount", "Kst");
		this.table.setColumnCollapsed("empCostAccount", true);
		this.table.setColumnHeader("empBankIban", "Iban");
		this.table.setColumnCollapsed("empBankIban", true);
		this.table.setColumnCollapsed("empFrmRemark", true);
		this.verticalLayoutRight.setMargin(new MarginInfo(false));
		this.gridLayout.setMargin(new MarginInfo(false, true, true, false));
		this.tabSheet.setStyleName("framed");
		this.gridLayout6.setMargin(new MarginInfo(true, false, false, true));
		this.label5.setValue(StringResourceUtils.optLocalizeString("{$label5.value}", this));
		this.label7.setValue(StringResourceUtils.optLocalizeString("{$label7.value}", this));
		this.textField3.setDescription("Nachname des Mitarbeiters");
		this.textField3.setRequired(true);
		this.label6.setValue(StringResourceUtils.optLocalizeString("{$label6.value}", this));
		this.label8.setValue(StringResourceUtils.optLocalizeString("{$label8.value}", this));
		this.label10.setValue("PLZ");
		this.label31.setValue(StringResourceUtils.optLocalizeString("{$label10.value}", this));
		this.label12.setValue(StringResourceUtils.optLocalizeString("{$label12.value}", this));
		this.popupDateField.setRequired(true);
		this.label13.setValue(StringResourceUtils.optLocalizeString("{$label13.value}", this));
		this.label15.setValue(StringResourceUtils.optLocalizeString("{$label15.value}", this));
		this.popupDateField3.setRequired(true);
		this.label.setValue("Alter");
		this.textAge.setEnabled(false);
		this.label4.setValue(StringResourceUtils.optLocalizeString("{$label4.value}", this));
		this.comboBox4.setRequired(true);
		this.comboBox4.setContainerDataSource(WorkRole.class, DAOs.get(WorkRoleDAO.class).findAll());
		this.comboBox4.setItemCaptionPropertyId(WorkRole_.wrrName.getName());
		this.label16.setValue(StringResourceUtils.optLocalizeString("{$label16.value}", this));
		this.label9.setValue(StringResourceUtils.optLocalizeString("{$label9.value}", this));
		this.gridLayout2.setMargin(new MarginInfo(true, false, false, true));
		this.label11.setValue(StringResourceUtils.optLocalizeString("{$label11.value}", this));
		this.label14.setValue(StringResourceUtils.optLocalizeString("{$label14.value}", this));
		this.textField8.setRequired(true);
		this.label17.setValue(StringResourceUtils.optLocalizeString("{$label17.value}", this));
		this.label18.setValue(StringResourceUtils.optLocalizeString("{$label18.value}", this));
		this.textField11.setConverter(ConverterBuilder.stringToDouble().currency().build());
		this.label20.setValue(StringResourceUtils.optLocalizeString("{$label20.value}", this));
		this.textField12.setConverter(ConverterBuilder.stringToDouble().currency().build());
		this.label2.setValue(StringResourceUtils.optLocalizeString("{$label2.value}", this));
		this.comboBox2.setRequired(true);
		this.comboBox2.setContainerDataSource(SalaryBvgBase.class, DAOs.get(SalaryBvgBaseDAO.class).findAll());
		this.comboBox2.setItemCaptionPropertyId(SalaryBvgBase_.sbeName.getName());
		this.label3.setValue(StringResourceUtils.optLocalizeString("{$label3.value}", this));
		this.comboBox3.setRequired(true);
		this.comboBox3.setContainerDataSource(SalaryCalculation.class, DAOs.get(SalaryCalculationDAO.class).findAll());
		this.comboBox3.setItemCaptionPropertyId(SalaryCalculation_.sleName.getName());
		this.label19.setValue(StringResourceUtils.optLocalizeString("{$label19.value}", this));
		this.checkBox.setCaption("");
		this.panel3.setCaption("Bank");
		this.panel3.setTabIndex(0);
		this.gridLayout4.setCaption("");
		this.gridLayout4.setMargin(new MarginInfo(true, true, false, true));
		this.label28.setValue(StringResourceUtils.optLocalizeString("{$label28.value}", this));
		this.comboBox5.setRequired(true);
		this.comboBox5.setContainerDataSource(Bank.class, DAOs.get(BankDAO.class).findAll());
		this.comboBox5.setItemCaptionPropertyId(Bank_.bnkName.getName());
		this.label29.setValue(StringResourceUtils.optLocalizeString("{$label29.value}", this));
		this.label30.setValue(StringResourceUtils.optLocalizeString("{$label30.value}", this));
		this.panel2.setCaption("Lohnausweis");
		this.panel2.setTabIndex(0);
		this.gridLayout5.setCaption("");
		this.label23.setValue(StringResourceUtils.optLocalizeString("{$label23.value}", this));
		this.label24.setValue(StringResourceUtils.optLocalizeString("{$label24.value}", this));
		this.textField16.setConverter(ConverterBuilder.stringToDouble().currency().build());
		this.label25.setValue(StringResourceUtils.optLocalizeString("{$label25.value}", this));
		this.textField17.setConverter(ConverterBuilder.stringToDouble().currency().build());
		this.label26.setValue(StringResourceUtils.optLocalizeString("{$label26.value}", this));
		this.textField18.setConverter(ConverterBuilder.stringToDouble().currency().build());
		this.label27.setValue(StringResourceUtils.optLocalizeString("{$label27.value}", this));
		this.fieldGroup.bind(this.comboBox2, Employee_.salaryBvgBase.getName());
		this.fieldGroup.bind(this.comboBox3, Employee_.salaryCalculation.getName());
		this.fieldGroup.bind(this.comboBox4, Employee_.workRole.getName());
		this.fieldGroup.bind(this.textField, Employee_.empNumber.getName());
		this.fieldGroup.bind(this.textField2, Employee_.empFirstName.getName());
		this.fieldGroup.bind(this.textField3, Employee_.empLastName.getName());
		this.fieldGroup.bind(this.textField4, Employee_.empAddress.getName());
		this.fieldGroup.bind(this.textField5, Employee_.empZip.getName());
		this.fieldGroup.bind(this.textField6, Employee_.empPlace.getName());
		this.fieldGroup.bind(this.textField7, Employee_.empAhvnbr.getName());
		this.fieldGroup.bind(this.popupDateField, Employee_.empStartwork.getName());
		this.fieldGroup.bind(this.popupDateField2, Employee_.empEndWork.getName());
		this.fieldGroup.bind(this.textField8, Employee_.empCostAccount.getName());
		this.fieldGroup.bind(this.popupDateField3, Employee_.empBirthday.getName());
		this.fieldGroup.bind(this.textAge, "age");
		this.fieldGroup.bind(this.textField10, Employee_.empNbrOfKids.getName());
		this.fieldGroup.bind(this.textField11, Employee_.empKidsAddition.getName());
		this.fieldGroup.bind(this.checkBox, Employee_.empSourceTax.getName());
		this.fieldGroup.bind(this.textField12, Employee_.empBaseSalary.getName());
		this.fieldGroup.bind(this.textField20, Employee_.empBankAccount.getName());
		this.fieldGroup.bind(this.textField22, Employee_.empBankIban.getName());
		this.fieldGroup.bind(this.textField15, Employee_.empAhvnbrold.getName());
		this.fieldGroup.bind(this.textField16, Employee_.empAmtFrmRep.getName());
		this.fieldGroup.bind(this.textField17, Employee_.empAmtFrmCar.getName());
		this.fieldGroup.bind(this.textField18, Employee_.empAmtFrmJourney.getName());
		this.fieldGroup.bind(this.textField19, Employee_.empFrmRemark.getName());
		this.fieldGroup.bind(this.comboBox5, Employee_.bank.getName());
		this.fieldGroup.bind(this.comboBoxState, Employee_.empState.getName());
		this.fieldGroup.bind(this.comboBoxCivilState, Employee_.empCivilState.getName());
		this.horizontalLayout.setMargin(new MarginInfo(false, true, false, true));
		this.cmdSave.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/save1.png"));
		this.cmdSave.setCaption("Speichern");
		this.cmdSave.setDescription("Datensatz speichern");
		this.cmdReset.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/delete1.png"));
		this.cmdReset.setCaption("Zurücksetzen");
		this.cmdReset.setDescription("macht die letzten Änderungen rückgängig");
	
		MasterDetail.connect(this.table, this.fieldGroup);
	
		this.containerFilterComponent.setContainer(this.table.getBeanContainerDataSource(), "empNumber", "empFirstName",
				"empLastName", "empZip", "empPlace", "empState", "empAhvnbr", "workRole", "empCivilState");
		this.containerFilterComponent.setSearchableProperties("empFirstName", "empLastName", "empZip", "empPlace");
	
		this.cmdNew.setSizeUndefined();
		this.actionLayout.addComponent(this.cmdNew);
		this.actionLayout.setComponentAlignment(this.cmdNew, Alignment.MIDDLE_LEFT);
		this.cmdDelete.setSizeUndefined();
		this.actionLayout.addComponent(this.cmdDelete);
		this.actionLayout.setComponentAlignment(this.cmdDelete, Alignment.MIDDLE_LEFT);
		this.cmdReload.setSizeUndefined();
		this.actionLayout.addComponent(this.cmdReload);
		this.actionLayout.setComponentAlignment(this.cmdReload, Alignment.MIDDLE_LEFT);
		this.cmdInfo.setSizeUndefined();
		this.actionLayout.addComponent(this.cmdInfo);
		this.actionLayout.setComponentAlignment(this.cmdInfo, Alignment.MIDDLE_LEFT);
		this.cmdExport.setSizeUndefined();
		this.actionLayout.addComponent(this.cmdExport);
		this.actionLayout.setComponentAlignment(this.cmdExport, Alignment.MIDDLE_LEFT);
		final CustomComponent actionLayout_spacer = new CustomComponent();
		actionLayout_spacer.setSizeFull();
		this.actionLayout.addComponent(actionLayout_spacer);
		this.actionLayout.setExpandRatio(actionLayout_spacer, 1.0F);
		this.containerFilterComponent.setWidth(100, Unit.PERCENTAGE);
		this.containerFilterComponent.setHeight(-1, Unit.PIXELS);
		this.verticalLayoutLeft.addComponent(this.containerFilterComponent);
		this.verticalLayoutLeft.setComponentAlignment(this.containerFilterComponent, Alignment.MIDDLE_LEFT);
		this.actionLayout.setWidth(100, Unit.PERCENTAGE);
		this.actionLayout.setHeight(-1, Unit.PIXELS);
		this.verticalLayoutLeft.addComponent(this.actionLayout);
		this.verticalLayoutLeft.setComponentAlignment(this.actionLayout, Alignment.MIDDLE_LEFT);
		this.table.setSizeFull();
		this.verticalLayoutLeft.addComponent(this.table);
		this.verticalLayoutLeft.setComponentAlignment(this.table, Alignment.MIDDLE_CENTER);
		this.verticalLayoutLeft.setExpandRatio(this.table, 10.0F);
		this.gridLayout6.setColumns(4);
		this.gridLayout6.setRows(9);
		this.label5.setSizeUndefined();
		this.gridLayout6.addComponent(this.label5, 0, 0);
		this.textField.setSizeUndefined();
		this.gridLayout6.addComponent(this.textField, 1, 0);
		this.label7.setSizeUndefined();
		this.gridLayout6.addComponent(this.label7, 0, 1);
		this.textField3.setWidth(100, Unit.PERCENTAGE);
		this.textField3.setHeight(-1, Unit.PIXELS);
		this.gridLayout6.addComponent(this.textField3, 1, 1);
		this.label6.setSizeUndefined();
		this.gridLayout6.addComponent(this.label6, 2, 1);
		this.textField2.setWidth(288, Unit.PIXELS);
		this.textField2.setHeight(-1, Unit.PIXELS);
		this.gridLayout6.addComponent(this.textField2, 3, 1);
		this.label8.setSizeUndefined();
		this.gridLayout6.addComponent(this.label8, 0, 2);
		this.textField4.setWidth(100, Unit.PERCENTAGE);
		this.textField4.setHeight(-1, Unit.PIXELS);
		this.gridLayout6.addComponent(this.textField4, 1, 2);
		this.label10.setSizeUndefined();
		this.gridLayout6.addComponent(this.label10, 0, 3);
		this.textField5.setWidth(100, Unit.PERCENTAGE);
		this.textField5.setHeight(-1, Unit.PIXELS);
		this.gridLayout6.addComponent(this.textField5, 1, 3);
		this.label31.setSizeUndefined();
		this.gridLayout6.addComponent(this.label31, 2, 3);
		this.textField6.setWidth(288, Unit.PIXELS);
		this.textField6.setHeight(-1, Unit.PIXELS);
		this.gridLayout6.addComponent(this.textField6, 3, 3);
		this.label12.setSizeUndefined();
		this.gridLayout6.addComponent(this.label12, 0, 4);
		this.popupDateField.setSizeUndefined();
		this.gridLayout6.addComponent(this.popupDateField, 1, 4);
		this.label13.setSizeUndefined();
		this.gridLayout6.addComponent(this.label13, 2, 4);
		this.popupDateField2.setSizeUndefined();
		this.gridLayout6.addComponent(this.popupDateField2, 3, 4);
		this.label15.setSizeUndefined();
		this.gridLayout6.addComponent(this.label15, 0, 5);
		this.popupDateField3.setSizeUndefined();
		this.gridLayout6.addComponent(this.popupDateField3, 1, 5);
		this.label.setSizeUndefined();
		this.gridLayout6.addComponent(this.label, 2, 5);
		this.textAge.setSizeUndefined();
		this.gridLayout6.addComponent(this.textAge, 3, 5);
		this.label4.setSizeUndefined();
		this.gridLayout6.addComponent(this.label4, 0, 6);
		this.comboBox4.setWidth(100, Unit.PERCENTAGE);
		this.comboBox4.setHeight(-1, Unit.PIXELS);
		this.gridLayout6.addComponent(this.comboBox4, 1, 6);
		this.label16.setSizeUndefined();
		this.gridLayout6.addComponent(this.label16, 2, 6);
		this.comboBoxCivilState.setSizeUndefined();
		this.gridLayout6.addComponent(this.comboBoxCivilState, 3, 6);
		this.label9.setSizeUndefined();
		this.gridLayout6.addComponent(this.label9, 0, 7);
		this.comboBoxState.setSizeUndefined();
		this.gridLayout6.addComponent(this.comboBoxState, 1, 7);
		this.gridLayout6.setColumnExpandRatio(1, 10.0F);
		this.gridLayout6.setColumnExpandRatio(3, 10.0F);
		final CustomComponent gridLayout6_vSpacer = new CustomComponent();
		gridLayout6_vSpacer.setSizeFull();
		this.gridLayout6.addComponent(gridLayout6_vSpacer, 0, 8, 3, 8);
		this.gridLayout6.setRowExpandRatio(8, 1.0F);
		this.gridLayout4.setColumns(2);
		this.gridLayout4.setRows(4);
		this.label28.setWidth(92, Unit.PIXELS);
		this.label28.setHeight(-1, Unit.PIXELS);
		this.gridLayout4.addComponent(this.label28, 0, 0);
		this.comboBox5.setWidth(100, Unit.PERCENTAGE);
		this.comboBox5.setHeight(-1, Unit.PIXELS);
		this.gridLayout4.addComponent(this.comboBox5, 1, 0);
		this.label29.setWidth(92, Unit.PIXELS);
		this.label29.setHeight(-1, Unit.PIXELS);
		this.gridLayout4.addComponent(this.label29, 0, 1);
		this.textField20.setWidth(100, Unit.PERCENTAGE);
		this.textField20.setHeight(-1, Unit.PIXELS);
		this.gridLayout4.addComponent(this.textField20, 1, 1);
		this.label30.setWidth(92, Unit.PIXELS);
		this.label30.setHeight(-1, Unit.PIXELS);
		this.gridLayout4.addComponent(this.label30, 0, 2);
		this.textField22.setWidth(100, Unit.PERCENTAGE);
		this.textField22.setHeight(-1, Unit.PIXELS);
		this.gridLayout4.addComponent(this.textField22, 1, 2);
		this.gridLayout4.setColumnExpandRatio(1, 10.0F);
		final CustomComponent gridLayout4_vSpacer = new CustomComponent();
		gridLayout4_vSpacer.setSizeFull();
		this.gridLayout4.addComponent(gridLayout4_vSpacer, 0, 3, 1, 3);
		this.gridLayout4.setRowExpandRatio(3, 1.0F);
		this.gridLayout4.setSizeFull();
		this.panel3.setContent(this.gridLayout4);
		this.gridLayout5.setColumns(2);
		this.gridLayout5.setRows(6);
		this.label23.setWidth(108, Unit.PIXELS);
		this.label23.setHeight(-1, Unit.PIXELS);
		this.gridLayout5.addComponent(this.label23, 0, 0);
		this.textField15.setWidth(100, Unit.PERCENTAGE);
		this.textField15.setHeight(-1, Unit.PIXELS);
		this.gridLayout5.addComponent(this.textField15, 1, 0);
		this.label24.setSizeUndefined();
		this.gridLayout5.addComponent(this.label24, 0, 1);
		this.textField16.setWidth(100, Unit.PERCENTAGE);
		this.textField16.setHeight(-1, Unit.PIXELS);
		this.gridLayout5.addComponent(this.textField16, 1, 1);
		this.label25.setSizeUndefined();
		this.gridLayout5.addComponent(this.label25, 0, 2);
		this.textField17.setSizeUndefined();
		this.gridLayout5.addComponent(this.textField17, 1, 2);
		this.label26.setSizeUndefined();
		this.gridLayout5.addComponent(this.label26, 0, 3);
		this.textField18.setSizeUndefined();
		this.gridLayout5.addComponent(this.textField18, 1, 3);
		this.label27.setSizeUndefined();
		this.gridLayout5.addComponent(this.label27, 0, 4);
		this.textField19.setWidth(100, Unit.PERCENTAGE);
		this.textField19.setHeight(-1, Unit.PIXELS);
		this.gridLayout5.addComponent(this.textField19, 1, 4);
		this.gridLayout5.setColumnExpandRatio(1, 10.0F);
		final CustomComponent gridLayout5_vSpacer = new CustomComponent();
		gridLayout5_vSpacer.setSizeFull();
		this.gridLayout5.addComponent(gridLayout5_vSpacer, 0, 5, 1, 5);
		this.gridLayout5.setRowExpandRatio(5, 1.0F);
		this.gridLayout5.setSizeFull();
		this.panel2.setContent(this.gridLayout5);
		this.gridLayout2.setColumns(4);
		this.gridLayout2.setRows(6);
		this.label11.setSizeUndefined();
		this.gridLayout2.addComponent(this.label11, 0, 0);
		this.textField7.setWidth(100, Unit.PERCENTAGE);
		this.textField7.setHeight(-1, Unit.PIXELS);
		this.gridLayout2.addComponent(this.textField7, 1, 0);
		this.label14.setSizeUndefined();
		this.gridLayout2.addComponent(this.label14, 2, 0);
		this.textField8.setWidth(100, Unit.PERCENTAGE);
		this.textField8.setHeight(-1, Unit.PIXELS);
		this.gridLayout2.addComponent(this.textField8, 3, 0);
		this.label17.setSizeUndefined();
		this.gridLayout2.addComponent(this.label17, 0, 1);
		this.textField10.setSizeUndefined();
		this.gridLayout2.addComponent(this.textField10, 1, 1);
		this.label18.setSizeUndefined();
		this.gridLayout2.addComponent(this.label18, 2, 1);
		this.textField11.setWidth(100, Unit.PERCENTAGE);
		this.textField11.setHeight(-1, Unit.PIXELS);
		this.gridLayout2.addComponent(this.textField11, 3, 1);
		this.label20.setSizeUndefined();
		this.gridLayout2.addComponent(this.label20, 0, 2);
		this.textField12.setSizeUndefined();
		this.gridLayout2.addComponent(this.textField12, 1, 2);
		this.label2.setSizeUndefined();
		this.gridLayout2.addComponent(this.label2, 0, 3);
		this.comboBox2.setWidth(100, Unit.PERCENTAGE);
		this.comboBox2.setHeight(-1, Unit.PIXELS);
		this.gridLayout2.addComponent(this.comboBox2, 1, 3);
		this.label3.setSizeUndefined();
		this.gridLayout2.addComponent(this.label3, 0, 4);
		this.comboBox3.setWidth(100, Unit.PERCENTAGE);
		this.comboBox3.setHeight(-1, Unit.PIXELS);
		this.gridLayout2.addComponent(this.comboBox3, 1, 4);
		this.label19.setSizeUndefined();
		this.gridLayout2.addComponent(this.label19, 2, 4);
		this.checkBox.setWidth(100, Unit.PERCENTAGE);
		this.checkBox.setHeight(-1, Unit.PIXELS);
		this.gridLayout2.addComponent(this.checkBox, 3, 4);
		this.panel3.setSizeFull();
		this.gridLayout2.addComponent(this.panel3, 0, 5, 1, 5);
		this.panel2.setSizeFull();
		this.gridLayout2.addComponent(this.panel2, 2, 5, 3, 5);
		this.gridLayout2.setColumnExpandRatio(1, 10.0F);
		this.gridLayout2.setColumnExpandRatio(2, 10.0F);
		this.gridLayout2.setColumnExpandRatio(3, 10.0F);
		this.gridLayout2.setRowExpandRatio(5, 10.0F);
		this.gridLayout6.setSizeFull();
		this.tabSheet.addTab(this.gridLayout6, StringResourceUtils.optLocalizeString("{$gridLayout.caption}", this), null);
		this.gridLayout2.setSizeFull();
		this.tabSheet.addTab(this.gridLayout2, StringResourceUtils.optLocalizeString("{$gridLayout2.caption}", this), null);
		this.tabSheet.setSelectedTab(this.gridLayout6);
		this.gridLayout.setColumns(1);
		this.gridLayout.setRows(1);
		this.tabSheet.setSizeFull();
		this.gridLayout.addComponent(this.tabSheet, 0, 0);
		this.gridLayout.setColumnExpandRatio(0, 100.0F);
		this.gridLayout.setRowExpandRatio(0, 100.0F);
		this.cmdSave.setSizeUndefined();
		this.horizontalLayout.addComponent(this.cmdSave);
		this.horizontalLayout.setComponentAlignment(this.cmdSave, Alignment.MIDDLE_CENTER);
		this.cmdReset.setSizeUndefined();
		this.horizontalLayout.addComponent(this.cmdReset);
		this.horizontalLayout.setComponentAlignment(this.cmdReset, Alignment.MIDDLE_RIGHT);
		this.gridLayout.setSizeFull();
		this.verticalLayoutRight.addComponent(this.gridLayout);
		this.verticalLayoutRight.setExpandRatio(this.gridLayout, 100.0F);
		this.horizontalLayout.setSizeUndefined();
		this.verticalLayoutRight.addComponent(this.horizontalLayout);
		this.verticalLayoutRight.setComponentAlignment(this.horizontalLayout, Alignment.MIDDLE_CENTER);
		this.verticalLayoutLeft.setSizeFull();
		this.horizontalSplitPanel.setFirstComponent(this.verticalLayoutLeft);
		this.verticalLayoutRight.setSizeFull();
		this.horizontalSplitPanel.setSecondComponent(this.verticalLayoutRight);
		this.horizontalSplitPanel.setSizeFull();
		this.panel.setContent(this.horizontalSplitPanel);
		this.panel.setSizeFull();
		this.setContent(this.panel);
		this.setSizeFull();
	
		this.cmdNew.addClickListener(event -> this.cmdNew_buttonClick(event));
		this.cmdDelete.addClickListener(event -> this.cmdDelete_buttonClick(event));
		this.cmdReload.addClickListener(event -> this.cmdReload_buttonClick(event));
		this.cmdInfo.addClickListener(event -> this.cmdInfo_buttonClick(event));
		this.cmdExport.addClickListener(event -> this.cmdExport_buttonClick(event));
		this.table.addValueChangeListener(event -> this.table_valueChange(event));
		this.cmdSave.addClickListener(event -> this.cmdSave_buttonClick(event));
		this.cmdReset.addClickListener(event -> this.cmdReset_buttonClick(event));
	} // </generated-code>

	// <generated-code name="variables">
	private XdevButton cmdNew, cmdDelete, cmdReload, cmdInfo, cmdExport, cmdSave, cmdReset;
	private XdevLabel label5, label7, label6, label8, label10, label31, label12, label13, label15, label, label4, label16,
			label9, label11, label14, label17, label18, label20, label2, label3, label19, label28, label29, label30,
			label23, label24, label25, label26, label27;
	private XdevTable<Employee> table;
	private XdevComboBox<Bank> comboBox5;
	private XdevPanel panel, panel3, panel2;
	private XdevTabSheet tabSheet;
	private XdevGridLayout gridLayout, gridLayout6, gridLayout2, gridLayout4, gridLayout5;
	private XdevHorizontalSplitPanel horizontalSplitPanel;
	private XdevComboBox<CustomComponent> comboBoxCivilState, comboBoxState;
	private XdevContainerFilterComponent containerFilterComponent;
	private XdevHorizontalLayout actionLayout, horizontalLayout;
	private XdevComboBox<SalaryCalculation> comboBox3;
	private XdevPopupDateField popupDateField, popupDateField2, popupDateField3;
	private XdevComboBox<WorkRole> comboBox4;
	private XdevCheckBox checkBox;
	private XdevTextField textField, textField3, textField2, textField4, textField5, textField6, textAge, textField7,
			textField8, textField10, textField11, textField12, textField20, textField22, textField15, textField16,
			textField17, textField18, textField19;
	private XdevVerticalLayout verticalLayoutLeft, verticalLayoutRight;
	private XdevComboBox<SalaryBvgBase> comboBox2;
	private XdevFieldGroup<Employee> fieldGroup;
	// </generated-code>


}
