package ch.xwr.seicentobookit.ui.desktop;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.ss.formula.functions.T;

import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
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
import com.xdev.ui.XdevPopupDateField;
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
import com.xdev.ui.util.NestedProperty;
import com.xdev.util.ConverterBuilder;

import ch.xwr.seicentobookit.business.CalculateSalary;
import ch.xwr.seicentobookit.business.CalculationHelper;
import ch.xwr.seicentobookit.business.ConfirmDialog;
import ch.xwr.seicentobookit.business.JasperManager;
import ch.xwr.seicentobookit.business.LovState;
import ch.xwr.seicentobookit.business.RowObjectManager;
import ch.xwr.seicentobookit.business.model.SolAlvValues;
import ch.xwr.seicentobookit.dal.EmployeeDAO;
import ch.xwr.seicentobookit.dal.SalaryDAO;
import ch.xwr.seicentobookit.entities.Employee;
import ch.xwr.seicentobookit.entities.Employee_;
import ch.xwr.seicentobookit.entities.Salary;
import ch.xwr.seicentobookit.entities.SalaryCalculationLine;
import ch.xwr.seicentobookit.entities.Salary_;

public class SalaryTabView extends XdevView {
	CalculationHelper helper = null;

	/**
	 * 
	 */
	public SalaryTabView() {
		super();
		this.initUI();

		helper = new CalculationHelper();
		
		// dummy (hight get lost)
		//this.tabSheet.setWidth(100, Unit.PERCENTAGE);
		this.tabSheet.setHeight(-1, Unit.PIXELS);

		// Type
		comboBoxType.addItems((Object[]) Salary.SalaryType.values());
		// State
		comboBoxState.addItems((Object[]) LovState.State.values());

		// Sort it
		Object[] properties = { "slrDate", "employee" };
		boolean[] ordering = { false, true };
		table.sort(properties, ordering);

		// RO Fields
		setROFields();

		setDefaultFilter();
	}

	private void setDefaultFilter() {
		Calendar cal = Calendar.getInstance();
		int iyear = cal.get(Calendar.YEAR);

		Integer[] val = new Integer[] { iyear };
		FilterData[] fd = new FilterData[] { new FilterData("slrYear", new FilterOperator.Is(), val) };

		this.containerFilterComponent.setFilterData(fd);
	}

	private void setROFields() {
		if (isBooked()) {
			cmdSave.setEnabled(false);
			cmdDelete.setEnabled(false);
		} else {
			cmdSave.setEnabled(true);
			cmdDelete.setEnabled(true);
			
			boolean hasData = true;
			if (this.fieldGroup.getItemDataSource() == null) {
				hasData = false;
			}
			this.cmdSave.setEnabled(hasData);
			this.tabSheet.setEnabled(hasData);			
		}

		textFieldSalaryNet.setEnabled(false);
		textFieldBaseAlv.setEnabled(false);
		textFieldBaseBvg.setEnabled(false);
		textFieldBaseBvg.setEnabled(false);
		textFieldBaseSol.setEnabled(false);

		textFieldFactorAhv.setEnabled(false);
		textFieldFactorAlv.setEnabled(false);
		textFieldFactorFak.setEnabled(false);
		textFieldFactorSol.setEnabled(false);
		textFieldFactorAdmin.setEnabled(false);

		textAmtAdmin.setEnabled(false);
		textAmtAhv.setEnabled(false);
		textAmtAlv.setEnabled(false);
		textAmtBvg1.setEnabled(false);
		textAmtBvgEmp.setEnabled(false);
		textAmtFak.setEnabled(false);
		textAmtSol.setEnabled(false);

		popupDateFieldPayDate.setEnabled(false);
		
		
	}

	private boolean isBooked() {
		if (fieldGroup == null || fieldGroup.getItemDataSource() == null)
			return false;
		Salary bean = fieldGroup.getItemDataSource().getBean();
		if (bean != null && bean.getSlrPayDate() != null) {
			return true;
		}
		return false;
	}

	/**
	 * Event handler delegate method for the {@link XdevButton} {@link #cmdNew}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdNew_buttonClick(Button.ClickEvent event) {
		this.fieldGroup.setItemDataSource(getNewSalaryWithDefaults());

		setROFields();
	}

	private Salary getNewSalaryWithDefaults() {
		Salary sal = new Salary();

		sal.setSlrState(LovState.State.active);
		sal.setSlrType(Salary.SalaryType.Normal);
		sal.setSlrDate(new Date());

		sal.setSlrAmountSourceTax(0.);
		sal.setSlrBaseAlv(0.);
		sal.setSlrBaseBvg(0.);
		sal.setSlrBaseSol(0.);
		sal.setSlrBirthAddon(0.);

		return sal;
	}

	private Double getBaseBvg(Employee emp) {
		if (comboBoxType.getConvertedValue().equals(Salary.SalaryType.Bonus))
			return new Double(0);
		if (emp == null) return new Double(0);
		// return
		// computePercentage(newEmp.getSalarybvgbase().getSbesalarydefamt(),
		// 12., new BigDecimal(1));
		return computePercentage(emp.getSalaryBvgBase().getSbeSalarydefAmt(), 12.);
	}

	private Double computePercentage(Double input, double d1) {
		// Double output = input.divide(new Double(d1), RoundingMode.HALF_DOWN);
		try {
			Double output = input / d1;
			return swissCommercialRound(new BigDecimal(output));
			
		} catch (Exception e) {
			System.out.println("Could not calculate Dobule value");
		}
		return new Double(0.);
	}

	private Double swissCommercialRound(BigDecimal input) {
		double ans = java.lang.Math.round((input.doubleValue() / 0.05)) * 0.05;
		BigDecimal value = new BigDecimal(ans).setScale(2, RoundingMode.HALF_DOWN);

		return value.doubleValue();
	}

	private void setBaseSolAlv(Employee newEmp) {
		Salary sal = this.fieldGroup.getItemDataSource().getBean();
		SalaryCalculationLine clc = helper.getCalculationBase(sal, newEmp);		
    	SolAlvValues values = helper.getBaseSolAlvValues(sal, newEmp, clc);

    	if (newEmp.getEmpBaseSalary() == null) newEmp.setEmpBaseSalary(new Double(0.0));
		double currentBrutSalary = newEmp.getEmpBaseSalary();
		if ((double) textFieldSalaryBrut.getConvertedValue() != 0)
			currentBrutSalary = (double) textFieldSalaryBrut.getConvertedValue();
		//double totSalary = currentBrutSalary; // Start Value with current Base Salary

		// Solidaritätbeitrag unter/ober Limit 
		if (values.getTotalSalary() > clc.getSlxSldLowerBoundry()) {
			textFieldBaseSol.setConvertedValue(values.getSolValue());
		}

		// Koordinationslohn ALV
		double salaryBase = currentBrutSalary;
		textFieldBaseAlv.setConvertedValue(salaryBase);
		if (values.getTotalSalary() > clc.getSlxCoordinationAlv()) {
			textFieldBaseAlv.setConvertedValue(values.getBaseAlv());
		}

	}

	/**
	 * Event handler delegate method for the {@link XdevButton}
	 * {@link #cmdGenerate}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdGenerate_buttonClick(Button.ClickEvent event) {		
		Window win = SalaryCreatePopupView.getPopupWindow();

		// UI.getCurrent().getSession().setAttribute(String.class,
		// bean.getClass().getSimpleName());
		UI.getCurrent().getSession().setAttribute(String.class, "state");
		win.setContent(new SalaryCreatePopupView());
		win.addCloseListener(new CloseListener() {

			@Override
			public void windowClose(CloseEvent e) {
				// Dummy for reloading
				SalaryTabView.this.reloadMainTable();
			}

		});
		this.getUI().addWindow(win);

	}

	private void reloadMainTable() {
		table.removeAllItems();		
		this.table.getBeanContainerDataSource().addAll(new SalaryDAO().findAll());		
//		table.refreshRowCache();
//		table.getBeanContainerDataSource().refresh();
		table.sort();		
	}
	
	/**
	 * Event handler delegate method for the {@link XdevTable} {@link #table}.
	 *
	 * @see ItemClickEvent.ItemClickListener#itemClick(ItemClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void table_itemClick(ItemClickEvent event) {
		// System.out.println("hier");
		// fieldGroup.

	}

	/**
	 * Event handler delegate method for the {@link XdevTable} {@link #table}.
	 *
	 * @see Property.ValueChangeListener#valueChange(Property.ValueChangeEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void table_valueChange(Property.ValueChangeEvent event) {
		// System.out.println("value change table...");
		// fieldgroup is loaded
		setROFields();
	}

	/**
	 * Event handler delegate method for the {@link XdevButton}
	 * {@link #cmdReport}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdReport_buttonClick(Button.ClickEvent event) {
		Salary sal = null;
		if (table.getSelectedItem() != null) {
			sal = table.getSelectedItem().getBean();
		}

		JasperManager jsp = new JasperManager();
		if (sal != null) {
			jsp.addParameter("Param_DateFrom", sal.getSlrDate().toString());
			jsp.addParameter("Param_DateTo", sal.getSlrDate().toString());
			jsp.addParameter("EmployeeId", "" + sal.getEmployee().getEmpId());			
		}

		Page.getCurrent().open(jsp.getUri(JasperManager.SalaryReport1), "_blank");
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
				if (retval == null)
					retval = "cmdCancel";

				if (retval.equals("cmdOk"))
					doDelete();
			}

			private void doDelete() {
				Salary bean = table.getSelectedItem().getBean();
				// Delete Record
				RowObjectManager man = new RowObjectManager();
				man.deleteObject(bean.getSlrId(), bean.getClass().getSimpleName());

				SalaryDAO dao = new SalaryDAO();
				dao.remove(bean);
				table.removeItem(bean);
				table.getBeanContainerDataSource().refresh();

				table.select(table.getCurrentPageFirstItemId());
				Notification.show("Datensatz löschen", "Datensatz wurde gelöscht!",
						Notification.Type.TRAY_NOTIFICATION);

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
		this.reloadMainTable();
		
//		table.refreshRowCache();
//		table.getBeanContainerDataSource().refresh();
//		table.sort();
		
		final Salary bean = this.fieldGroup.getItemDataSource().getBean();
		if (bean != null) {
			this.table.select(bean);
		}
		

	}

	/**
	 * Event handler delegate method for the {@link XdevComboBox}
	 * {@link #comboBoxEmployee}.
	 *
	 * @see Property.ValueChangeListener#valueChange(Property.ValueChangeEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void comboBoxEmployee_valueChange(Property.ValueChangeEvent event) {
		if (!fieldGroup.isModified())
			return;
		// if (event.getProperty().)
		Employee emp = (Employee) event.getProperty().getValue();
		loadDataFromEmp(emp);
	}

	private void loadDataFromEmp(Employee emp) {
		Salary sal = this.fieldGroup.getItemDataSource().getBean();
		SalaryCalculationLine clc = helper.getCalculationBase(sal, emp);
		
		if (clc != null) {
			textFieldFactorAdmin.setConvertedValue(clc.getSlxFactorAdmin());
			textFieldFactorAhv.setConvertedValue(clc.getSlxFactorAhv());
			textFieldFactorAlv.setConvertedValue(clc.getSlxFactorAlv());
			textFieldFactorFak.setConvertedValue(clc.getSlxFactorFak());
			textFieldFactorSol.setConvertedValue(clc.getSlxFactorSol());			
		}

		textFieldBaseBvg.setConvertedValue(getBaseBvg(emp));

		setBaseSolAlv(emp);

		if (fieldGroup.getItemDataSource().getBean().getSlrId() < 1) {
			// only change when new record
			textFieldSalaryBrut.setConvertedValue(emp.getEmpBaseSalary());
			textFieldKidsAddon.setConvertedValue(emp.getEmpKidsAddition());
		}
	}

	private String getConvertedValue(Object obj, Converter<String, Object> cnv) {		
		Double val = null;
		try {
			val = Double.valueOf((String) obj);
		} catch (Exception e) {
			//value already converted
			return null;
		}
		
		Object ret = cnv.convertToPresentation(val, String.class, this.getLocale());
		return (String) ret;
	}

	private void calculateGui() {
		if (!fieldGroup.isModified())
			return;
		if (textFieldSalaryBrut.isEmpty())
			return;
		if (textFieldKidsAddon.isEmpty())
			return;
		if (textFieldAddonGeneral.isEmpty())
			return;

		try {
			fieldGroup.commit();

			CalculateSalary calc = new CalculateSalary(fieldGroup.getItemDataSource().getBean(),
					(Employee) comboBoxEmployee.getValue());
			
			try {
				calc.calculateSalary();				
			} catch (Exception e) {
				Notification.show("Fehler beim Berechnen des Lohns", e.getMessage(), Notification.Type.ERROR_MESSAGE);				
			}

			fieldGroup.setItemDataSource(calc.getDto());
			setROFields();

		} catch (CommitException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Event handler delegate method for the {@link XdevButton}
	 * {@link #cmdSave}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdSave_buttonClick(Button.ClickEvent event) {
		calculateGui();

		if (!AreFieldsValid()) {
			return;
		}

		try {
			this.fieldGroup.save();
			setROFields();

			RowObjectManager man = new RowObjectManager();
			man.updateObject(this.fieldGroup.getItemDataSource().getBean().getSlrId(),
					this.fieldGroup.getItemDataSource().getBean().getClass().getSimpleName());

			Notification.show("Save clicked", "Daten wurden gespeichert", Notification.Type.TRAY_NOTIFICATION);
			cmdReload_buttonClick(event);
			
		} catch (final Exception e) {
			Notification.show("Fehler beim Speichern", e.getMessage(), Notification.Type.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private boolean AreFieldsValid() {
		if (this.fieldGroup.isValid()) {
			return true;
		}
		AbstractField<T> fld = null;
		try {
			final Collection<?> flds = this.fieldGroup.getFields();
			for (final Iterator<?> iterator = flds.iterator(); iterator.hasNext();) {
				fld = (AbstractField<T>) iterator.next();
				if (!fld.isValid()) {
					fld.focus();
					fld.validate();
				}
			}

		} catch (final Exception e) {
			final Object prop = this.fieldGroup.getPropertyId(fld);
			Notification.show("Feld ist ungültig", prop.toString(), Notification.Type.ERROR_MESSAGE);
		}

		return false;
	}

	/**
	 * Event handler delegate method for the {@link XdevComboBox}
	 * {@link #comboBoxType}.
	 *
	 * @see Property.ValueChangeListener#valueChange(Property.ValueChangeEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void comboBoxType_valueChange(Property.ValueChangeEvent event) {
		if (!fieldGroup.isModified())
			return;

		//TODO mj
		textFieldBaseBvg.setConvertedValue(getBaseBvg(fieldGroup.getItemDataSource().getBean().getEmployee()));
		
		if (comboBoxType.getConvertedValue().equals(Salary.SalaryType.Bonus)) {
			textFieldKidsAddon.setConvertedValue(0.);
			textFieldKidsAddon.setEnabled(false);
		} else {
			textFieldKidsAddon.setEnabled(true);
		}

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
	
	private boolean isNew() {
		if (this.fieldGroup.getItemDataSource() == null) {
			return true;
		}
		final Salary bean = this.fieldGroup.getItemDataSource().getBean();
		if (bean.getSlrId() < 1) {
			return true;
		}
		return false;
	}
	

	/**
	 * Event handler delegate method for the {@link XdevButton}
	 * {@link #cmdInfo}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdInfo_buttonClick(Button.ClickEvent event) {
		Salary bean = this.fieldGroup.getItemDataSource().getBean();

		Window win = new Window();
		win.setWidth("1020");
		win.setHeight("700");
		win.center();
		win.setModal(true);

		// UI.getCurrent().getSession().setAttribute(String.class,
		// bean.getClass().getSimpleName());
		win.setContent(new RowObjectView(bean.getSlrId(), bean.getClass().getSimpleName()));
		this.getUI().addWindow(win);
	}


	/**
	 * Event handler delegate method for the {@link XdevTextField}
	 * {@link #textFieldSalaryBrut}.
	 *
	 * @see FieldEvents.BlurListener#blur(FieldEvents.BlurEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void textFieldSalaryBrut_blur(FieldEvents.BlurEvent event) {
		if (! textFieldSalaryBrut.isModified()) return;

		String obj = textFieldSalaryBrut.getValue();
		if (obj == null) textFieldSalaryBrut.setConvertedValue(0.);
		Converter<String, Object> cnv = textFieldSalaryBrut.getConverter();					
		String val = getConvertedValue(obj, cnv);
		if (val != null) {
			textFieldSalaryBrut.setValue((String)val);
		}
		
		setBaseSolAlv((Employee) comboBoxEmployee.getValue());
			
		calculateGui();		
	}

	/**
	 * Event handler delegate method for the {@link XdevTextField}
	 * {@link #textFieldAddonGeneral}.
	 *
	 * @see FieldEvents.BlurListener#blur(FieldEvents.BlurEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void textFieldAddonGeneral_blur(FieldEvents.BlurEvent event) {
		if (! textFieldAddonGeneral.isModified()) return;
		String obj = textFieldAddonGeneral.getValue();		
		Converter<String, Object> cnv = textFieldAddonGeneral.getConverter();					
		String val = getConvertedValue(obj, cnv);
		if (val != null) {
			textFieldAddonGeneral.setValue((String)val);
		}
		
		calculateGui();				
	}

	/**
	 * Event handler delegate method for the {@link XdevTextField}
	 * {@link #textFieldKidsAddon}.
	 *
	 * @see FieldEvents.BlurListener#blur(FieldEvents.BlurEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void textFieldKidsAddon_blur(FieldEvents.BlurEvent event) {
		if (! textFieldKidsAddon.isModified()) return;
		String obj = textFieldKidsAddon.getValue();		
		Converter<String, Object> cnv = textFieldKidsAddon.getConverter();					
		String val = getConvertedValue(obj, cnv);
		if (val != null) {
			textFieldKidsAddon.setValue((String)val);
		}
		
		calculateGui();				
	
	}

	/**
	 * Event handler delegate method for the {@link XdevTextField}
	 * {@link #textFieldSalaryBrut}.
	 *
	 * @see FieldEvents.TextChangeListener#textChange(FieldEvents.TextChangeEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void textFieldSalaryBrut_textChange(final FieldEvents.TextChangeEvent event) {
	
	}

	/**
	 * Event handler delegate method for the {@link XdevButton} {@link #cmdCalc}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdCalc_buttonClick(final Button.ClickEvent event) {
		Salary sal = this.fieldGroup.getItemDataSource().getBean();
		if (sal.getEmployee() == null) return;
		if (sal.getSlrDate() == null) return;
		
		loadDataFromEmp(sal.getEmployee());		
		calculateGui();
		
		if (!fieldGroup.isModified())
			return;

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
		this.cmdGenerate = new XdevButton();
		this.cmdReport = new XdevButton();
		this.cmdInfo = new XdevButton();
		this.table = new XdevTable<>();
		this.form = new XdevGridLayout();
		this.tabSheet = new XdevTabSheet();
		this.gridLayout = new XdevGridLayout();
		this.label = new XdevLabel();
		this.popupDateFieldSalary = new XdevPopupDateField();
		this.label6 = new XdevLabel();
		this.popupDateFieldPayDate = new XdevPopupDateField();
		this.label2 = new XdevLabel();
		this.comboBoxEmployee = new XdevComboBox<>();
		this.label3 = new XdevLabel();
		this.textField = new XdevTextField();
		this.label4 = new XdevLabel();
		this.textFieldSalaryBrut = new XdevTextField();
		this.label5 = new XdevLabel();
		this.textFieldSalaryNet = new XdevTextField();
		this.label25 = new XdevLabel();
		this.comboBoxType = new XdevComboBox<>();
		this.label26 = new XdevLabel();
		this.comboBoxState = new XdevComboBox<>();
		this.panel = new XdevPanel();
		this.horizontalLayout2 = new XdevHorizontalLayout();
		this.label7 = new XdevLabel();
		this.textFieldBaseAlv = new XdevTextField();
		this.label8 = new XdevLabel();
		this.textFieldBaseBvg = new XdevTextField();
		this.label9 = new XdevLabel();
		this.textFieldBaseSol = new XdevTextField();
		this.panelZuAb = new XdevPanel();
		this.gridLayout4 = new XdevGridLayout();
		this.label10 = new XdevLabel();
		this.textFieldKidsAddon = new XdevTextField();
		this.label17 = new XdevLabel();
		this.textField14 = new XdevTextField();
		this.label18 = new XdevLabel();
		this.textFieldAddonGeneral = new XdevTextField();
		this.gridLayout2 = new XdevGridLayout();
		this.label13 = new XdevLabel();
		this.textAmtBvg1 = new XdevTextField();
		this.label24 = new XdevLabel();
		this.textAmtBvgEmp = new XdevTextField();
		this.panelAhv = new XdevPanel();
		this.gridLayout5 = new XdevGridLayout();
		this.label11 = new XdevLabel();
		this.textAmtAhv = new XdevTextField();
		this.label12 = new XdevLabel();
		this.textAmtAlv = new XdevTextField();
		this.label15 = new XdevLabel();
		this.textAmtSol = new XdevTextField();
		this.label19 = new XdevLabel();
		this.label16 = new XdevLabel();
		this.textAmtAdmin = new XdevTextField();
		this.label14 = new XdevLabel();
		this.textAmtFak = new XdevTextField();
		this.panelFactor = new XdevPanel();
		this.gridLayout3 = new XdevGridLayout();
		this.label27 = new XdevLabel();
		this.textFieldFactorAhv = new XdevTextField();
		this.label20 = new XdevLabel();
		this.textFieldFactorAlv = new XdevTextField();
		this.label21 = new XdevLabel();
		this.textFieldFactorFak = new XdevTextField();
		this.label23 = new XdevLabel();
		this.textFieldFactorAdmin = new XdevTextField();
		this.label22 = new XdevLabel();
		this.textFieldFactorSol = new XdevTextField();
		this.horizontalLayout = new XdevHorizontalLayout();
		this.cmdSave = new XdevButton();
		this.cmdReset = new XdevButton();
		this.cmdCalc = new XdevButton();
		this.fieldGroup = new XdevFieldGroup<>(Salary.class);
	
		this.horizontalSplitPanel.setSplitPosition(40.0F, Unit.PERCENTAGE);
		this.verticalLayout.setMargin(new MarginInfo(false, false, true, false));
		this.containerFilterComponent.setDescription("Filter");
		this.containerFilterComponent.setCaptionAsHtml(true);
		this.actionLayout.setSpacing(false);
		this.actionLayout.setMargin(new MarginInfo(false));
		this.cmdNew.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/new1_16.png"));
		this.cmdNew.setDescription("Neuen Datensatz anlegen");
		this.cmdNew.setStyleName("icon-only");
		this.cmdDelete
				.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/delete3_16.png"));
		this.cmdDelete.setCaption("");
		this.cmdDelete.setDescription("Markierten Datensatz löschen");
		this.cmdDelete.setStyleName("icon-only");
		this.cmdReload.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/reload2.png"));
		this.cmdReload.setDescription("Liste neu laden");
		this.cmdReload.setStyleName("icon-only");
		this.cmdGenerate
				.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/copy1_16.png"));
		this.cmdGenerate.setCaption("...");
		this.cmdGenerate.setDescription("generiert neue Lohndatensätze");
		this.cmdGenerate.setStyleName("icon-only");
		this.cmdReport.setIcon(
				new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/Printer_black_18.png"));
		this.cmdReport.setDescription("Lohnblatt drucken");
		this.cmdReport.setStyleName("icon-only");
		this.cmdInfo
				.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/info_small.jpg"));
		this.cmdInfo.setStyleName("icon-only");
		this.table.setPageLength(14);
		this.table.setColumnCollapsingAllowed(true);
		this.table.setContainerDataSource(Salary.class, DAOs.get(SalaryDAO.class).findAll(),
				NestedProperty.of(Salary_.employee, Employee_.empLastName),
				NestedProperty.of(Salary_.employee, Employee_.empFirstName),
				NestedProperty.of(Salary_.employee, Employee_.empAddress),
				NestedProperty.of(Salary_.employee, Employee_.empZip),
				NestedProperty.of(Salary_.employee, Employee_.empPlace),
				NestedProperty.of(Salary_.employee, Employee_.empBirthday),
				NestedProperty.of(Salary_.employee, Employee_.empStartwork));
		this.table.setVisibleColumns(Salary_.slrDate.getName(),
				NestedProperty.path(Salary_.employee, Employee_.empLastName),
				NestedProperty.path(Salary_.employee, Employee_.empFirstName),
				NestedProperty.path(Salary_.employee, Employee_.empAddress),
				NestedProperty.path(Salary_.employee, Employee_.empZip),
				NestedProperty.path(Salary_.employee, Employee_.empPlace), Salary_.slrType.getName(),
				Salary_.slrBirthAddon.getName(), Salary_.slrKidsAdditon.getName(), Salary_.slrSalaryNet.getName(),
				Salary_.slrSalaryBase.getName(), NestedProperty.path(Salary_.employee, Employee_.empBirthday),
				NestedProperty.path(Salary_.employee, Employee_.empStartwork));
		this.table.setColumnHeader("slrDate", "Datum");
		this.table.setConverter("slrDate", ConverterBuilder.stringToDate().pattern("YYYY MMMM").build());
		this.table.setColumnHeader("employee.empLastName", "Name");
		this.table.setColumnHeader("employee.empFirstName", "Vorname");
		this.table.setColumnHeader("employee.empAddress", "Adresse");
		this.table.setColumnCollapsed("employee.empAddress", true);
		this.table.setColumnHeader("employee.empZip", "PLZ");
		this.table.setColumnCollapsed("employee.empZip", true);
		this.table.setColumnHeader("employee.empPlace", "Ort");
		this.table.setColumnCollapsed("employee.empPlace", true);
		this.table.setColumnHeader("slrType", "Lohnart");
		this.table.setColumnHeader("slrBirthAddon", "+/-");
		this.table.setConverter("slrBirthAddon", ConverterBuilder.stringToDouble().currency().build());
		this.table.setColumnCollapsed("slrBirthAddon", true);
		this.table.setColumnHeader("slrKidsAdditon", "Kinderzulage");
		this.table.setConverter("slrKidsAdditon", ConverterBuilder.stringToDouble().currency().build());
		this.table.setColumnCollapsed("slrKidsAdditon", true);
		this.table.setColumnHeader("slrSalaryNet", "Nettolohn");
		this.table.setConverter("slrSalaryNet", ConverterBuilder.stringToDouble().currency().build());
		this.table.setColumnCollapsed("slrSalaryNet", true);
		this.table.setColumnHeader("slrSalaryBase", "Bruttolohn");
		this.table.setColumnAlignment("slrSalaryBase", Table.Align.RIGHT);
		this.table.setConverter("slrSalaryBase",
				ConverterBuilder.stringToDouble().currency().minimumFractionDigits(2).build());
		this.table.setColumnHeader("employee.empBirthday", "Geburtstag");
		this.table.setConverter("employee.empBirthday", ConverterBuilder.stringToDate().dateOnly().build());
		this.table.setColumnCollapsed("employee.empBirthday", true);
		this.table.setColumnHeader("employee.empStartwork", "Anstellungsbeginn");
		this.table.setConverter("employee.empStartwork", ConverterBuilder.stringToDate().dateOnly().build());
		this.table.setColumnCollapsed("employee.empStartwork", true);
		this.form.setMargin(new MarginInfo(false, false, true, false));
		this.tabSheet.setStyleName("framed");
		this.label.setValue(StringResourceUtils.optLocalizeString("{$label.value}", this));
		this.label6.setValue(StringResourceUtils.optLocalizeString("{$label6.value}", this));
		this.label2.setValue(StringResourceUtils.optLocalizeString("{$label2.value}", this));
		this.comboBoxEmployee.setRequired(true);
		this.comboBoxEmployee.setItemCaptionFromAnnotation(false);
		this.comboBoxEmployee.setContainerDataSource(Employee.class, DAOs.get(EmployeeDAO.class).findAll());
		this.comboBoxEmployee.setItemCaptionPropertyId("caption");
		this.label3.setValue(StringResourceUtils.optLocalizeString("{$label3.value}", this));
		this.textField.setMaxLength(128);
		this.label4.setValue(StringResourceUtils.optLocalizeString("{$label4.value}", this));
		this.textFieldSalaryBrut.setConverter(ConverterBuilder.stringToDouble().currency().build());
		this.textFieldSalaryBrut.setRequired(true);
		this.label5.setValue(StringResourceUtils.optLocalizeString("{$label5.value}", this));
		this.textFieldSalaryNet.setConverter(ConverterBuilder.stringToDouble().currency().build());
		this.textFieldSalaryNet.setStyleName("borderless large dark");
		this.textFieldSalaryNet.setEnabled(false);
		this.textFieldSalaryNet.setReadOnly(true);
		this.label25.setValue(StringResourceUtils.optLocalizeString("{$label25.value}", this));
		this.comboBoxType.setNullSelectionAllowed(false);
		this.label26.setValue(StringResourceUtils.optLocalizeString("{$label26.value}", this));
		this.comboBoxState.setNullSelectionAllowed(false);
		this.panel.setCaption("Basisbeträge");
		this.panel.setTabIndex(0);
		this.horizontalLayout2.setMargin(new MarginInfo(false, false, true, false));
		this.label7.setValue(StringResourceUtils.optLocalizeString("{$label7.value}", this));
		this.textFieldBaseAlv.setConverter(ConverterBuilder.stringToDouble().currency().build());
		this.label8.setValue(StringResourceUtils.optLocalizeString("{$label8.value}", this));
		this.textFieldBaseBvg.setConverter(ConverterBuilder.stringToDouble().currency().build());
		this.label9.setValue(StringResourceUtils.optLocalizeString("{$label9.value}", this));
		this.textFieldBaseSol.setConverter(ConverterBuilder.stringToDouble().currency().build());
		this.panelZuAb.setCaption("Zu-/Abschläge");
		this.panelZuAb.setTabIndex(0);
		this.gridLayout4.setMargin(new MarginInfo(false, true, true, false));
		this.label10.setValue(StringResourceUtils.optLocalizeString("{$label10.value}", this));
		this.textFieldKidsAddon.setConverter(ConverterBuilder.stringToDouble().currency().build());
		this.label17.setValue(StringResourceUtils.optLocalizeString("{$label17.value}", this));
		this.textField14.setConverter(ConverterBuilder.stringToDouble().currency().build());
		this.label18.setValue(StringResourceUtils.optLocalizeString("{$label18.value}", this));
		this.textFieldAddonGeneral.setConverter(ConverterBuilder.stringToDouble().currency().build());
		this.label13.setValue(StringResourceUtils.optLocalizeString("{$label13.value}", this));
		this.textAmtBvg1.setConverter(ConverterBuilder.stringToDouble().currency().build());
		this.label24.setValue(StringResourceUtils.optLocalizeString("{$label24.value}", this));
		this.textAmtBvgEmp.setConverter(ConverterBuilder.stringToDouble().currency().build());
		this.panelAhv.setCaption("Beträge Ausgleichskasse");
		this.gridLayout5.setMargin(new MarginInfo(false, true, true, false));
		this.label11.setValue(StringResourceUtils.optLocalizeString("{$label11.value}", this));
		this.textAmtAhv.setConverter(ConverterBuilder.stringToDouble().currency().build());
		this.label12.setValue(StringResourceUtils.optLocalizeString("{$label12.value}", this));
		this.textAmtAlv.setConverter(ConverterBuilder.stringToDouble().currency().build());
		this.label15.setValue(StringResourceUtils.optLocalizeString("{$label15.value}", this));
		this.textAmtSol.setConverter(ConverterBuilder.stringToDouble().currency().build());
		this.label19.setStyleName("colored bold");
		this.label19.setValue("Arbeitgeber");
		this.label16.setValue(StringResourceUtils.optLocalizeString("{$label16.value}", this));
		this.textAmtAdmin.setConverter(ConverterBuilder.stringToDouble().currency().build());
		this.label14.setValue(StringResourceUtils.optLocalizeString("{$label14.value}", this));
		this.textAmtFak.setConverter(ConverterBuilder.stringToDouble().currency().build());
		this.panelFactor.setCaption("Ansätze %");
		this.gridLayout3.setMargin(new MarginInfo(false, true, true, false));
		this.label27.setValue(StringResourceUtils.optLocalizeString("{$label19.value}", this));
		this.textFieldFactorAhv.setColumns(5);
		this.label20.setValue(StringResourceUtils.optLocalizeString("{$label20.value}", this));
		this.textFieldFactorAlv.setColumns(5);
		this.label21.setValue(StringResourceUtils.optLocalizeString("{$label21.value}", this));
		this.textFieldFactorFak.setColumns(5);
		this.label23.setValue(StringResourceUtils.optLocalizeString("{$label23.value}", this));
		this.textFieldFactorAdmin.setColumns(5);
		this.label22.setValue(StringResourceUtils.optLocalizeString("{$label22.value}", this));
		this.textFieldFactorSol.setColumns(5);
		this.horizontalLayout.setMargin(new MarginInfo(false, true, false, true));
		this.cmdSave.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/save1.png"));
		this.cmdSave.setCaption(StringResourceUtils.optLocalizeString("{$cmdSave.caption}", this));
		this.cmdSave.setDescription("Datensatz speichern");
		this.cmdReset.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/delete1.png"));
		this.cmdReset.setCaption(StringResourceUtils.optLocalizeString("{$cmdReset.caption}", this));
		this.cmdReset.setDescription("macht die letzten Änderungen rückgängig");
		this.cmdCalc.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/work1.png"));
		this.cmdCalc.setCaption("Neu berechnen");
		this.cmdCalc.setDescription("macht die letzten Änderungen rückgängig");
		this.fieldGroup.bind(this.popupDateFieldSalary, Salary_.slrDate.getName());
		this.fieldGroup.bind(this.comboBoxEmployee, Salary_.employee.getName());
		this.fieldGroup.bind(this.textField, Salary_.slrRemark.getName());
		this.fieldGroup.bind(this.textFieldSalaryBrut, Salary_.slrSalaryBase.getName());
		this.fieldGroup.bind(this.textFieldSalaryNet, Salary_.slrSalaryNet.getName());
		this.fieldGroup.bind(this.popupDateFieldPayDate, Salary_.slrPayDate.getName());
		this.fieldGroup.bind(this.comboBoxType, Salary_.slrType.getName());
		this.fieldGroup.bind(this.comboBoxState, Salary_.slrState.getName());
		this.fieldGroup.bind(this.textFieldKidsAddon, Salary_.slrKidsAdditon.getName());
		this.fieldGroup.bind(this.textField14, Salary_.slrAmountSourceTax.getName());
		this.fieldGroup.bind(this.textFieldBaseAlv, Salary_.slrBaseAlv.getName());
		this.fieldGroup.bind(this.textFieldBaseBvg, Salary_.slrBaseBvg.getName());
		this.fieldGroup.bind(this.textFieldBaseSol, Salary_.slrBaseSol.getName());
		this.fieldGroup.bind(this.textFieldAddonGeneral, Salary_.slrBirthAddon.getName());
		this.fieldGroup.bind(this.textAmtAhv, Salary_.slrAmountAhv.getName());
		this.fieldGroup.bind(this.textAmtAlv, Salary_.slrAmountAlv.getName());
		this.fieldGroup.bind(this.textAmtBvg1, Salary_.slrAmountBvg.getName());
		this.fieldGroup.bind(this.textAmtFak, Salary_.slrAmountFak.getName());
		this.fieldGroup.bind(this.textAmtSol, Salary_.slrAmountSol.getName());
		this.fieldGroup.bind(this.textAmtAdmin, Salary_.slrAmountAdminfees.getName());
		this.fieldGroup.bind(this.textFieldFactorAhv, Salary_.slrFactorAhv.getName());
		this.fieldGroup.bind(this.textFieldFactorAlv, Salary_.slrFactorAlv.getName());
		this.fieldGroup.bind(this.textFieldFactorFak, Salary_.slrFactorFak.getName());
		this.fieldGroup.bind(this.textFieldFactorSol, Salary_.slrFactorSol.getName());
		this.fieldGroup.bind(this.textFieldFactorAdmin, Salary_.slrFactorAdmin.getName());
		this.fieldGroup.bind(this.textAmtBvgEmp, Salary_.slrAmountBvgEmp.getName());
	
		MasterDetail.connect(this.table, this.fieldGroup);
	
		this.containerFilterComponent.setContainer(this.table.getBeanContainerDataSource(), "slrYear", "slrDate", "slrType",
				"slrSalaryBase", "slrPayDate", "slrState", "employee");
		this.containerFilterComponent.setSearchableProperties("employee.empLastName", "employee.empFirstName",
				"employee.empCostAccount", "slrRemark");
	
		this.cmdNew.setSizeUndefined();
		this.actionLayout.addComponent(this.cmdNew);
		this.actionLayout.setComponentAlignment(this.cmdNew, Alignment.TOP_RIGHT);
		this.cmdDelete.setSizeUndefined();
		this.actionLayout.addComponent(this.cmdDelete);
		this.actionLayout.setComponentAlignment(this.cmdDelete, Alignment.TOP_RIGHT);
		this.cmdReload.setSizeUndefined();
		this.actionLayout.addComponent(this.cmdReload);
		this.actionLayout.setComponentAlignment(this.cmdReload, Alignment.MIDDLE_CENTER);
		this.cmdGenerate.setSizeUndefined();
		this.actionLayout.addComponent(this.cmdGenerate);
		this.actionLayout.setComponentAlignment(this.cmdGenerate, Alignment.TOP_RIGHT);
		this.cmdReport.setSizeUndefined();
		this.actionLayout.addComponent(this.cmdReport);
		this.actionLayout.setComponentAlignment(this.cmdReport, Alignment.MIDDLE_CENTER);
		this.cmdInfo.setSizeUndefined();
		this.actionLayout.addComponent(this.cmdInfo);
		this.actionLayout.setComponentAlignment(this.cmdInfo, Alignment.MIDDLE_LEFT);
		final CustomComponent actionLayout_spacer = new CustomComponent();
		actionLayout_spacer.setSizeFull();
		this.actionLayout.addComponent(actionLayout_spacer);
		this.actionLayout.setExpandRatio(actionLayout_spacer, 1.0F);
		this.containerFilterComponent.setWidth(100, Unit.PERCENTAGE);
		this.containerFilterComponent.setHeight(-1, Unit.PIXELS);
		this.verticalLayout.addComponent(this.containerFilterComponent);
		this.verticalLayout.setComponentAlignment(this.containerFilterComponent, Alignment.TOP_RIGHT);
		this.actionLayout.setWidth(98, Unit.PERCENTAGE);
		this.actionLayout.setHeight(-1, Unit.PIXELS);
		this.verticalLayout.addComponent(this.actionLayout);
		this.verticalLayout.setComponentAlignment(this.actionLayout, Alignment.TOP_CENTER);
		this.table.setWidth(98, Unit.PERCENTAGE);
		this.table.setHeight(100, Unit.PERCENTAGE);
		this.verticalLayout.addComponent(this.table);
		this.verticalLayout.setComponentAlignment(this.table, Alignment.MIDDLE_CENTER);
		this.verticalLayout.setExpandRatio(this.table, 100.0F);
		this.label7.setSizeUndefined();
		this.horizontalLayout2.addComponent(this.label7);
		this.textFieldBaseAlv.setWidth(100, Unit.PERCENTAGE);
		this.textFieldBaseAlv.setHeight(-1, Unit.PIXELS);
		this.horizontalLayout2.addComponent(this.textFieldBaseAlv);
		this.horizontalLayout2.setExpandRatio(this.textFieldBaseAlv, 100.0F);
		this.label8.setSizeUndefined();
		this.horizontalLayout2.addComponent(this.label8);
		this.textFieldBaseBvg.setWidth(100, Unit.PERCENTAGE);
		this.textFieldBaseBvg.setHeight(-1, Unit.PIXELS);
		this.horizontalLayout2.addComponent(this.textFieldBaseBvg);
		this.horizontalLayout2.setExpandRatio(this.textFieldBaseBvg, 100.0F);
		this.label9.setSizeUndefined();
		this.horizontalLayout2.addComponent(this.label9);
		this.textFieldBaseSol.setWidth(100, Unit.PERCENTAGE);
		this.textFieldBaseSol.setHeight(-1, Unit.PIXELS);
		this.horizontalLayout2.addComponent(this.textFieldBaseSol);
		this.horizontalLayout2.setExpandRatio(this.textFieldBaseSol, 100.0F);
		this.horizontalLayout2.setWidth(100, Unit.PERCENTAGE);
		this.horizontalLayout2.setHeight(-1, Unit.PIXELS);
		this.panel.setContent(this.horizontalLayout2);
		this.gridLayout4.setColumns(4);
		this.gridLayout4.setRows(3);
		this.label10.setSizeUndefined();
		this.gridLayout4.addComponent(this.label10, 0, 0);
		this.textFieldKidsAddon.setSizeUndefined();
		this.gridLayout4.addComponent(this.textFieldKidsAddon, 1, 0);
		this.label17.setSizeUndefined();
		this.gridLayout4.addComponent(this.label17, 2, 0);
		this.textField14.setSizeUndefined();
		this.gridLayout4.addComponent(this.textField14, 3, 0);
		this.label18.setSizeUndefined();
		this.gridLayout4.addComponent(this.label18, 0, 1);
		this.textFieldAddonGeneral.setSizeUndefined();
		this.gridLayout4.addComponent(this.textFieldAddonGeneral, 1, 1);
		this.gridLayout4.setColumnExpandRatio(1, 10.0F);
		this.gridLayout4.setColumnExpandRatio(3, 10.0F);
		final CustomComponent gridLayout4_vSpacer = new CustomComponent();
		gridLayout4_vSpacer.setSizeFull();
		this.gridLayout4.addComponent(gridLayout4_vSpacer, 0, 2, 3, 2);
		this.gridLayout4.setRowExpandRatio(2, 1.0F);
		this.gridLayout4.setSizeFull();
		this.panelZuAb.setContent(this.gridLayout4);
		this.gridLayout.setColumns(4);
		this.gridLayout.setRows(8);
		this.label.setSizeUndefined();
		this.gridLayout.addComponent(this.label, 0, 0);
		this.popupDateFieldSalary.setSizeUndefined();
		this.gridLayout.addComponent(this.popupDateFieldSalary, 1, 0);
		this.label6.setSizeUndefined();
		this.gridLayout.addComponent(this.label6, 2, 0);
		this.popupDateFieldPayDate.setSizeUndefined();
		this.gridLayout.addComponent(this.popupDateFieldPayDate, 3, 0);
		this.label2.setSizeUndefined();
		this.gridLayout.addComponent(this.label2, 0, 1);
		this.comboBoxEmployee.setWidth(100, Unit.PERCENTAGE);
		this.comboBoxEmployee.setHeight(-1, Unit.PIXELS);
		this.gridLayout.addComponent(this.comboBoxEmployee, 1, 1);
		this.label3.setSizeUndefined();
		this.gridLayout.addComponent(this.label3, 0, 2);
		this.textField.setWidth(100, Unit.PERCENTAGE);
		this.textField.setHeight(-1, Unit.PIXELS);
		this.gridLayout.addComponent(this.textField, 1, 2, 2, 2);
		this.label4.setSizeUndefined();
		this.gridLayout.addComponent(this.label4, 0, 3);
		this.textFieldSalaryBrut.setWidth(100, Unit.PERCENTAGE);
		this.textFieldSalaryBrut.setHeight(-1, Unit.PIXELS);
		this.gridLayout.addComponent(this.textFieldSalaryBrut, 1, 3);
		this.label5.setSizeUndefined();
		this.gridLayout.addComponent(this.label5, 2, 3);
		this.textFieldSalaryNet.setWidth(100, Unit.PERCENTAGE);
		this.textFieldSalaryNet.setHeight(-1, Unit.PIXELS);
		this.gridLayout.addComponent(this.textFieldSalaryNet, 3, 3);
		this.label25.setSizeUndefined();
		this.gridLayout.addComponent(this.label25, 0, 4);
		this.comboBoxType.setSizeUndefined();
		this.gridLayout.addComponent(this.comboBoxType, 1, 4);
		this.label26.setSizeUndefined();
		this.gridLayout.addComponent(this.label26, 2, 4);
		this.comboBoxState.setSizeUndefined();
		this.gridLayout.addComponent(this.comboBoxState, 3, 4);
		this.panel.setWidth(100, Unit.PERCENTAGE);
		this.panel.setHeight(-1, Unit.PIXELS);
		this.gridLayout.addComponent(this.panel, 0, 5, 3, 5);
		this.panelZuAb.setWidth(100, Unit.PERCENTAGE);
		this.panelZuAb.setHeight(-1, Unit.PIXELS);
		this.gridLayout.addComponent(this.panelZuAb, 0, 6, 3, 6);
		this.gridLayout.setColumnExpandRatio(1, 30.0F);
		this.gridLayout.setColumnExpandRatio(3, 50.0F);
		final CustomComponent gridLayout_vSpacer = new CustomComponent();
		gridLayout_vSpacer.setSizeFull();
		this.gridLayout.addComponent(gridLayout_vSpacer, 0, 7, 3, 7);
		this.gridLayout.setRowExpandRatio(7, 1.0F);
		this.gridLayout5.setColumns(2);
		this.gridLayout5.setRows(7);
		this.label11.setSizeUndefined();
		this.gridLayout5.addComponent(this.label11, 0, 0);
		this.textAmtAhv.setSizeUndefined();
		this.gridLayout5.addComponent(this.textAmtAhv, 1, 0);
		this.label12.setSizeUndefined();
		this.gridLayout5.addComponent(this.label12, 0, 1);
		this.textAmtAlv.setSizeUndefined();
		this.gridLayout5.addComponent(this.textAmtAlv, 1, 1);
		this.label15.setSizeUndefined();
		this.gridLayout5.addComponent(this.label15, 0, 2);
		this.textAmtSol.setSizeUndefined();
		this.gridLayout5.addComponent(this.textAmtSol, 1, 2);
		this.label19.setSizeUndefined();
		this.gridLayout5.addComponent(this.label19, 0, 3);
		this.label16.setSizeUndefined();
		this.gridLayout5.addComponent(this.label16, 0, 4);
		this.textAmtAdmin.setSizeUndefined();
		this.gridLayout5.addComponent(this.textAmtAdmin, 1, 4);
		this.label14.setSizeUndefined();
		this.gridLayout5.addComponent(this.label14, 0, 5);
		this.textAmtFak.setSizeUndefined();
		this.gridLayout5.addComponent(this.textAmtFak, 1, 5);
		this.gridLayout5.setColumnExpandRatio(1, 5.0F);
		final CustomComponent gridLayout5_vSpacer = new CustomComponent();
		gridLayout5_vSpacer.setSizeFull();
		this.gridLayout5.addComponent(gridLayout5_vSpacer, 0, 6, 1, 6);
		this.gridLayout5.setRowExpandRatio(6, 1.0F);
		this.gridLayout5.setSizeUndefined();
		this.panelAhv.setContent(this.gridLayout5);
		this.gridLayout3.setColumns(3);
		this.gridLayout3.setRows(6);
		this.label27.setSizeUndefined();
		this.gridLayout3.addComponent(this.label27, 0, 0);
		this.textFieldFactorAhv.setSizeUndefined();
		this.gridLayout3.addComponent(this.textFieldFactorAhv, 1, 0);
		this.label20.setSizeUndefined();
		this.gridLayout3.addComponent(this.label20, 0, 1);
		this.textFieldFactorAlv.setSizeUndefined();
		this.gridLayout3.addComponent(this.textFieldFactorAlv, 1, 1);
		this.label21.setSizeUndefined();
		this.gridLayout3.addComponent(this.label21, 0, 2);
		this.textFieldFactorFak.setSizeUndefined();
		this.gridLayout3.addComponent(this.textFieldFactorFak, 1, 2);
		this.label23.setSizeUndefined();
		this.gridLayout3.addComponent(this.label23, 0, 3);
		this.textFieldFactorAdmin.setSizeUndefined();
		this.gridLayout3.addComponent(this.textFieldFactorAdmin, 1, 3);
		this.label22.setSizeUndefined();
		this.gridLayout3.addComponent(this.label22, 0, 4);
		this.textFieldFactorSol.setSizeUndefined();
		this.gridLayout3.addComponent(this.textFieldFactorSol, 1, 4);
		final CustomComponent gridLayout3_hSpacer = new CustomComponent();
		gridLayout3_hSpacer.setSizeFull();
		this.gridLayout3.addComponent(gridLayout3_hSpacer, 2, 0, 2, 4);
		this.gridLayout3.setColumnExpandRatio(2, 1.0F);
		final CustomComponent gridLayout3_vSpacer = new CustomComponent();
		gridLayout3_vSpacer.setSizeFull();
		this.gridLayout3.addComponent(gridLayout3_vSpacer, 0, 5, 1, 5);
		this.gridLayout3.setRowExpandRatio(5, 1.0F);
		this.gridLayout3.setSizeUndefined();
		this.panelFactor.setContent(this.gridLayout3);
		this.gridLayout2.setColumns(3);
		this.gridLayout2.setRows(4);
		this.label13.setSizeUndefined();
		this.gridLayout2.addComponent(this.label13, 0, 0);
		this.textAmtBvg1.setSizeUndefined();
		this.gridLayout2.addComponent(this.textAmtBvg1, 1, 0);
		this.label24.setSizeUndefined();
		this.gridLayout2.addComponent(this.label24, 0, 1);
		this.textAmtBvgEmp.setSizeUndefined();
		this.gridLayout2.addComponent(this.textAmtBvgEmp, 1, 1);
		this.panelAhv.setSizeUndefined();
		this.gridLayout2.addComponent(this.panelAhv, 0, 2, 1, 2);
		this.panelFactor.setSizeUndefined();
		this.gridLayout2.addComponent(this.panelFactor, 2, 2);
		this.gridLayout2.setColumnExpandRatio(1, 10.0F);
		this.gridLayout2.setColumnExpandRatio(2, 20.0F);
		final CustomComponent gridLayout2_vSpacer = new CustomComponent();
		gridLayout2_vSpacer.setSizeFull();
		this.gridLayout2.addComponent(gridLayout2_vSpacer, 0, 3, 2, 3);
		this.gridLayout2.setRowExpandRatio(3, 1.0F);
		this.gridLayout.setSizeFull();
		this.tabSheet.addTab(this.gridLayout, StringResourceUtils.optLocalizeString("{$gridLayout.caption}", this), null);
		this.gridLayout2.setSizeFull();
		this.tabSheet.addTab(this.gridLayout2, StringResourceUtils.optLocalizeString("{$gridLayout2.caption}", this), null);
		this.tabSheet.setSelectedTab(this.gridLayout);
		this.cmdSave.setSizeUndefined();
		this.horizontalLayout.addComponent(this.cmdSave);
		this.horizontalLayout.setComponentAlignment(this.cmdSave, Alignment.MIDDLE_CENTER);
		this.cmdReset.setSizeUndefined();
		this.horizontalLayout.addComponent(this.cmdReset);
		this.horizontalLayout.setComponentAlignment(this.cmdReset, Alignment.MIDDLE_RIGHT);
		this.cmdCalc.setSizeUndefined();
		this.horizontalLayout.addComponent(this.cmdCalc);
		this.horizontalLayout.setComponentAlignment(this.cmdCalc, Alignment.MIDDLE_RIGHT);
		this.form.setColumns(1);
		this.form.setRows(2);
		this.tabSheet.setSizeFull();
		this.form.addComponent(this.tabSheet, 0, 0);
		this.horizontalLayout.setSizeUndefined();
		this.form.addComponent(this.horizontalLayout, 0, 1);
		this.form.setComponentAlignment(this.horizontalLayout, Alignment.MIDDLE_CENTER);
		this.form.setColumnExpandRatio(0, 100.0F);
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
		this.cmdGenerate.addClickListener(event -> this.cmdGenerate_buttonClick(event));
		this.cmdReport.addClickListener(event -> this.cmdReport_buttonClick(event));
		this.cmdInfo.addClickListener(event -> this.cmdInfo_buttonClick(event));
		this.table.addItemClickListener(event -> this.table_itemClick(event));
		this.table.addValueChangeListener(event -> this.table_valueChange(event));
		this.comboBoxEmployee.addValueChangeListener(event -> this.comboBoxEmployee_valueChange(event));
		this.textFieldSalaryBrut.addBlurListener(event -> this.textFieldSalaryBrut_blur(event));
		this.textFieldSalaryBrut.addTextChangeListener(event -> this.textFieldSalaryBrut_textChange(event));
		this.comboBoxType.addValueChangeListener(event -> this.comboBoxType_valueChange(event));
		this.textFieldKidsAddon.addBlurListener(event -> this.textFieldKidsAddon_blur(event));
		this.textFieldAddonGeneral.addBlurListener(event -> this.textFieldAddonGeneral_blur(event));
		this.cmdSave.addClickListener(event -> this.cmdSave_buttonClick(event));
		this.cmdReset.addClickListener(event -> this.cmdReset_buttonClick(event));
		this.cmdCalc.addClickListener(event -> this.cmdCalc_buttonClick(event));
	} // </generated-code>

	// <generated-code name="variables">
	private XdevButton cmdNew, cmdDelete, cmdReload, cmdGenerate, cmdReport, cmdInfo, cmdSave, cmdReset, cmdCalc;
	private XdevLabel label, label6, label2, label3, label4, label5, label25, label26, label7, label8, label9, label10,
			label17, label18, label13, label24, label11, label12, label15, label19, label16, label14, label27, label20,
			label21, label23, label22;
	private XdevTabSheet tabSheet;
	private XdevPanel panel, panelZuAb, panelAhv, panelFactor;
	private XdevGridLayout form, gridLayout, gridLayout4, gridLayout2, gridLayout5, gridLayout3;
	private XdevHorizontalSplitPanel horizontalSplitPanel;
	private XdevComboBox<CustomComponent> comboBoxType, comboBoxState;
	private XdevContainerFilterComponent containerFilterComponent;
	private XdevHorizontalLayout actionLayout, horizontalLayout2, horizontalLayout;
	private XdevComboBox<Employee> comboBoxEmployee;
	private XdevPopupDateField popupDateFieldSalary, popupDateFieldPayDate;
	private XdevFieldGroup<Salary> fieldGroup;
	private XdevTable<Salary> table;
	private XdevTextField textField, textFieldSalaryBrut, textFieldSalaryNet, textFieldBaseAlv, textFieldBaseBvg,
			textFieldBaseSol, textFieldKidsAddon, textField14, textFieldAddonGeneral, textAmtBvg1, textAmtBvgEmp,
			textAmtAhv, textAmtAlv, textAmtSol, textAmtAdmin, textAmtFak, textFieldFactorAhv, textFieldFactorAlv,
			textFieldFactorFak, textFieldFactorAdmin, textFieldFactorSol;
	private XdevVerticalLayout verticalLayout;
	// </generated-code>

}
