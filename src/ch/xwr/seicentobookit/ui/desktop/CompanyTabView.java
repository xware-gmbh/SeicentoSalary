package ch.xwr.seicentobookit.ui.desktop;

import com.vaadin.data.Property;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
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
import com.xdev.ui.XdevPasswordField;
import com.xdev.ui.XdevTabSheet;
import com.xdev.ui.XdevTextField;
import com.xdev.ui.XdevVerticalLayout;
import com.xdev.ui.XdevView;
import com.xdev.ui.entitycomponent.table.XdevTable;
import com.xdev.ui.filter.XdevContainerFilterComponent;
import com.xdev.ui.masterdetail.MasterDetail;

import ch.xwr.seicentobookit.business.ConfirmDialog;
import ch.xwr.seicentobookit.business.RowObjectManager;
import ch.xwr.seicentobookit.dal.CompanyDAO;
import ch.xwr.seicentobookit.entities.Company;
import ch.xwr.seicentobookit.entities.Company_;

public class CompanyTabView extends XdevView {

	/**
	 * 
	 */
	public CompanyTabView() {
		super();
		this.initUI();
		
		//dummy (hight get lost)
		this.tabSheet.setWidth(100, Unit.PERCENTAGE);
		this.tabSheet.setHeight(-1, Unit.PIXELS);
		
		//Sort it
		Object [] properties={"cmpState","cmpName"};
		boolean [] ordering={false, true};
		table.sort(properties, ordering);
		
		table.select(table.getCurrentPageFirstItemId());
		
		setROFields();
	}

	private void setROFields() {
		textFieldStatus.setEnabled(false);
	}

	/**
	 * Event handler delegate method for the {@link XdevButton}
	 * {@link #cmdAdminReset}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdAdminReset_buttonClick(Button.ClickEvent event) {
		Window win = new Window();
		win.setWidth("450");
		win.setHeight("220");		
		win.center();
		win.setModal(true);
		
		//UI.getCurrent().getSession().setAttribute(String.class, bean.getClass().getSimpleName());
		UI.getCurrent().getSession().setAttribute(String.class, "state");
		win.setContent(new ResetPayDatePopup());
//		win.addCloseListener(new CloseListener() {
//			
//			@Override
//			public void windowClose(CloseEvent e) {
//				//Dummy for reloading
//				table.refreshRowCache();
//				table.sort();
//			}
//		});
		this.getUI().addWindow(win);		
	
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
				Company bean = table.getSelectedItem().getBean();
				// Delete Record
				RowObjectManager man = new RowObjectManager();
				man.deleteObject(bean.getCmpId(), bean.getClass().getSimpleName());
			
				CompanyDAO dao = new CompanyDAO();
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
	 * {@link #cmdInfo}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdInfo_buttonClick(Button.ClickEvent event) {
		Company bean = this.fieldGroup.getItemDataSource().getBean();
	
		Window win = new Window();
		win.setWidth("1020");
		win.setHeight("700");
		win.center();
		win.setModal(true);
	
		// UI.getCurrent().getSession().setAttribute(String.class,
		// bean.getClass().getSimpleName());
		win.setContent(new RowObjectView(bean.getCmpId(), bean.getClass().getSimpleName()));
		this.getUI().addWindow(win);
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
	}

	private Company getNewBeanWithDefaults() {
		Company bean = new Company();
		
		//bean.setBnkState(LovState.State.active);
		
		return bean;
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
		setROFields();
		
		RowObjectManager man = new RowObjectManager();
		man.updateObject(this.fieldGroup.getItemDataSource().getBean().getCmpId(), this.fieldGroup.getItemDataSource().getBean().getClass().getSimpleName());
		
		Notification.show("Save clicked", "Daten wurden gespeichert", Notification.Type.TRAY_NOTIFICATION);
	
	}

	/**
	 * Event handler delegate method for the {@link XdevButton}
	 * {@link #cmdReset}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdReset_buttonClick(Button.ClickEvent event) {
		this.fieldGroup.discard();		
	}

	/**
	 * Event handler delegate method for the {@link XdevTable} {@link #table}.
	 *
	 * @see Property.ValueChangeListener#valueChange(Property.ValueChangeEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void table_valueChange(Property.ValueChangeEvent event) {
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
		this.gridLayout4 = new XdevGridLayout();
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
		this.textFieldStatus = new XdevTextField();
		this.gridLayout2 = new XdevGridLayout();
		this.label8 = new XdevLabel();
		this.textField8 = new XdevTextField();
		this.label9 = new XdevLabel();
		this.textField9 = new XdevTextField();
		this.label10 = new XdevLabel();
		this.textField10 = new XdevTextField();
		this.label11 = new XdevLabel();
		this.textField11 = new XdevTextField();
		this.label12 = new XdevLabel();
		this.textField12 = new XdevTextField();
		this.label13 = new XdevLabel();
		this.textField13 = new XdevTextField();
		this.gridLayout3 = new XdevGridLayout();
		this.label14 = new XdevLabel();
		this.textField14 = new XdevTextField();
		this.label15 = new XdevLabel();
		this.textField15 = new XdevTextField();
		this.label16 = new XdevLabel();
		this.textField16 = new XdevTextField();
		this.label17 = new XdevLabel();
		this.passwordField = new XdevPasswordField();
		this.label18 = new XdevLabel();
		this.textField18 = new XdevTextField();
		this.label19 = new XdevLabel();
		this.textField19 = new XdevTextField();
		this.label20 = new XdevLabel();
		this.textField20 = new XdevTextField();
		this.label21 = new XdevLabel();
		this.textField21 = new XdevTextField();
		this.horizontalLayout = new XdevHorizontalLayout();
		this.cmdSave = new XdevButton();
		this.cmdReset = new XdevButton();
		this.cmdAdminReset = new XdevButton();
		this.fieldGroup = new XdevFieldGroup<>(Company.class);
	
		this.horizontalSplitPanel.setSplitPosition(40.0F, Unit.PERCENTAGE);
		this.verticalLayout.setMargin(new MarginInfo(false));
		this.actionLayout.setSpacing(false);
		this.actionLayout.setMargin(new MarginInfo(false, true, false, false));
		this.cmdNew.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/new1_16.png"));
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
		this.table.addShortcutListener(new AbstractField.FocusShortcut(this.table, ShortcutAction.KeyCode.ENTER));
		this.table.setContainerDataSource(Company.class, DAOs.get(CompanyDAO.class).findAll());
		this.table.addGeneratedColumn("generated", new FunctionActivateCompany.Generator());
		this.table.setVisibleColumns(Company_.cmpName.getName(), Company_.cmpAddress.getName(), Company_.cmpZip.getName(),
				Company_.cmpPlace.getName(), Company_.cmpState.getName(), "generated");
		this.table.setColumnHeader("cmpName", "Name");
		this.table.setColumnHeader("cmpAddress", "Adresse");
		this.table.setColumnHeader("cmpZip", "PLZ");
		this.table.setColumnHeader("cmpPlace", "Ort");
		this.table.setColumnHeader("cmpState", "Status");
		this.table.setColumnHeader("generated", "...");
		this.tabSheet.setStyleName("framed");
		this.label.setValue(StringResourceUtils.optLocalizeString("{$label.value}", this));
		this.label2.setValue(StringResourceUtils.optLocalizeString("{$label2.value}", this));
		this.label3.setValue(StringResourceUtils.optLocalizeString("{$label3.value}", this));
		this.label4.setValue(StringResourceUtils.optLocalizeString("{$label4.value}", this));
		this.label5.setValue(StringResourceUtils.optLocalizeString("{$label5.value}", this));
		this.label6.setValue(StringResourceUtils.optLocalizeString("{$label6.value}", this));
		this.label7.setValue(StringResourceUtils.optLocalizeString("{$label7.value}", this));
		this.label8.setValue(StringResourceUtils.optLocalizeString("{$label8.value}", this));
		this.label9.setValue(StringResourceUtils.optLocalizeString("{$label9.value}", this));
		this.label10.setValue(StringResourceUtils.optLocalizeString("{$label10.value}", this));
		this.label11.setValue(StringResourceUtils.optLocalizeString("{$label11.value}", this));
		this.label12.setValue(StringResourceUtils.optLocalizeString("{$label12.value}", this));
		this.label13.setValue(StringResourceUtils.optLocalizeString("{$label13.value}", this));
		this.label14.setValue(StringResourceUtils.optLocalizeString("{$label14.value}", this));
		this.label15.setValue(StringResourceUtils.optLocalizeString("{$label15.value}", this));
		this.label16.setValue(StringResourceUtils.optLocalizeString("{$label16.value}", this));
		this.label17.setValue(StringResourceUtils.optLocalizeString("{$label17.value}", this));
		this.label18.setValue(StringResourceUtils.optLocalizeString("{$label18.value}", this));
		this.label19.setValue(StringResourceUtils.optLocalizeString("{$label19.value}", this));
		this.label20.setValue(StringResourceUtils.optLocalizeString("{$label20.value}", this));
		this.label21.setValue(StringResourceUtils.optLocalizeString("{$label21.value}", this));
		this.horizontalLayout.setMargin(new MarginInfo(false));
		this.cmdSave.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/save1.png"));
		this.cmdSave.setCaption(StringResourceUtils.optLocalizeString("{$cmdSave.caption}", this));
		this.cmdReset.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/delete1.png"));
		this.cmdReset.setCaption(StringResourceUtils.optLocalizeString("{$cmdReset.caption}", this));
		this.cmdAdminReset
				.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/work1.png"));
		this.cmdAdminReset.setCaption("Admin");
		this.fieldGroup.bind(this.textField, Company_.cmpName.getName());
		this.fieldGroup.bind(this.textField2, Company_.cmpAddress.getName());
		this.fieldGroup.bind(this.textField3, Company_.cmpZip.getName());
		this.fieldGroup.bind(this.textField4, Company_.cmpPlace.getName());
		this.fieldGroup.bind(this.textField5, Company_.cmpVatcode.getName());
		this.fieldGroup.bind(this.textField6, Company_.cmpCurrency.getName());
		this.fieldGroup.bind(this.textFieldStatus, Company_.cmpState.getName());
		this.fieldGroup.bind(this.textField8, Company_.cmpUid.getName());
		this.fieldGroup.bind(this.textField9, Company_.cmpPhone.getName());
		this.fieldGroup.bind(this.textField10, Company_.cmpMail.getName());
		this.fieldGroup.bind(this.textField11, Company_.cmpComm1.getName());
		this.fieldGroup.bind(this.textField12, Company_.cmpBusiness.getName());
		this.fieldGroup.bind(this.textField13, Company_.cmpRemark.getName());
		this.fieldGroup.bind(this.textField14, Company_.cmpJapsperServer.getName());
		this.fieldGroup.bind(this.textField15, Company_.cmpJasperUri.getName());
		this.fieldGroup.bind(this.textField16, Company_.cmpReportUsr.getName());
		this.fieldGroup.bind(this.passwordField, Company_.cmpReportPwd.getName());
		this.fieldGroup.bind(this.textField18, Company_.cmpAhvnumber.getName());
		this.fieldGroup.bind(this.textField19, Company_.cmpNameBookKepper.getName());
		this.fieldGroup.bind(this.textField20, Company_.cmpSalaryDay.getName());
		this.fieldGroup.bind(this.textField21, Company_.cmpLastEmpNbr.getName());
	
		MasterDetail.connect(this.table, this.fieldGroup);
	
		this.containerFilterComponent.setContainer(this.table.getBeanContainerDataSource(), "cmpName", "cmpAddress",
				"cmpZip", "cmpPlace", "cmpState");
		this.containerFilterComponent.setSearchableProperties("cmpName", "cmpAddress", "cmpPlace");
	
		this.cmdNew.setSizeUndefined();
		this.actionLayout.addComponent(this.cmdNew);
		this.actionLayout.setComponentAlignment(this.cmdNew, Alignment.MIDDLE_LEFT);
		this.cmdDelete.setSizeUndefined();
		this.actionLayout.addComponent(this.cmdDelete);
		this.actionLayout.setComponentAlignment(this.cmdDelete, Alignment.TOP_RIGHT);
		this.cmdReload.setSizeUndefined();
		this.actionLayout.addComponent(this.cmdReload);
		this.actionLayout.setComponentAlignment(this.cmdReload, Alignment.MIDDLE_CENTER);
		this.cmdInfo.setSizeUndefined();
		this.actionLayout.addComponent(this.cmdInfo);
		this.actionLayout.setComponentAlignment(this.cmdInfo, Alignment.MIDDLE_CENTER);
		this.containerFilterComponent.setWidth(100, Unit.PERCENTAGE);
		this.containerFilterComponent.setHeight(-1, Unit.PIXELS);
		this.verticalLayout.addComponent(this.containerFilterComponent);
		this.verticalLayout.setComponentAlignment(this.containerFilterComponent, Alignment.MIDDLE_CENTER);
		this.actionLayout.setSizeUndefined();
		this.verticalLayout.addComponent(this.actionLayout);
		this.verticalLayout.setComponentAlignment(this.actionLayout, Alignment.MIDDLE_LEFT);
		this.table.setSizeFull();
		this.verticalLayout.addComponent(this.table);
		this.verticalLayout.setComponentAlignment(this.table, Alignment.MIDDLE_CENTER);
		this.verticalLayout.setExpandRatio(this.table, 90.0F);
		this.gridLayout4.setColumns(2);
		this.gridLayout4.setRows(7);
		this.label.setSizeUndefined();
		this.gridLayout4.addComponent(this.label, 0, 0);
		this.textField.setWidth(100, Unit.PERCENTAGE);
		this.textField.setHeight(-1, Unit.PIXELS);
		this.gridLayout4.addComponent(this.textField, 1, 0);
		this.label2.setSizeUndefined();
		this.gridLayout4.addComponent(this.label2, 0, 1);
		this.textField2.setWidth(100, Unit.PERCENTAGE);
		this.textField2.setHeight(-1, Unit.PIXELS);
		this.gridLayout4.addComponent(this.textField2, 1, 1);
		this.label3.setSizeUndefined();
		this.gridLayout4.addComponent(this.label3, 0, 2);
		this.textField3.setWidth(100, Unit.PERCENTAGE);
		this.textField3.setHeight(-1, Unit.PIXELS);
		this.gridLayout4.addComponent(this.textField3, 1, 2);
		this.label4.setSizeUndefined();
		this.gridLayout4.addComponent(this.label4, 0, 3);
		this.textField4.setWidth(100, Unit.PERCENTAGE);
		this.textField4.setHeight(-1, Unit.PIXELS);
		this.gridLayout4.addComponent(this.textField4, 1, 3);
		this.label5.setSizeUndefined();
		this.gridLayout4.addComponent(this.label5, 0, 4);
		this.textField5.setWidth(100, Unit.PERCENTAGE);
		this.textField5.setHeight(-1, Unit.PIXELS);
		this.gridLayout4.addComponent(this.textField5, 1, 4);
		this.label6.setSizeUndefined();
		this.gridLayout4.addComponent(this.label6, 0, 5);
		this.textField6.setWidth(100, Unit.PERCENTAGE);
		this.textField6.setHeight(-1, Unit.PIXELS);
		this.gridLayout4.addComponent(this.textField6, 1, 5);
		this.label7.setSizeUndefined();
		this.gridLayout4.addComponent(this.label7, 0, 6);
		this.textFieldStatus.setWidth(100, Unit.PERCENTAGE);
		this.textFieldStatus.setHeight(-1, Unit.PIXELS);
		this.gridLayout4.addComponent(this.textFieldStatus, 1, 6);
		this.gridLayout4.setColumnExpandRatio(1, 100.0F);
		this.gridLayout2.setColumns(2);
		this.gridLayout2.setRows(6);
		this.label8.setSizeUndefined();
		this.gridLayout2.addComponent(this.label8, 0, 0);
		this.textField8.setWidth(100, Unit.PERCENTAGE);
		this.textField8.setHeight(-1, Unit.PIXELS);
		this.gridLayout2.addComponent(this.textField8, 1, 0);
		this.label9.setSizeUndefined();
		this.gridLayout2.addComponent(this.label9, 0, 1);
		this.textField9.setWidth(100, Unit.PERCENTAGE);
		this.textField9.setHeight(-1, Unit.PIXELS);
		this.gridLayout2.addComponent(this.textField9, 1, 1);
		this.label10.setSizeUndefined();
		this.gridLayout2.addComponent(this.label10, 0, 2);
		this.textField10.setWidth(100, Unit.PERCENTAGE);
		this.textField10.setHeight(-1, Unit.PIXELS);
		this.gridLayout2.addComponent(this.textField10, 1, 2);
		this.label11.setSizeUndefined();
		this.gridLayout2.addComponent(this.label11, 0, 3);
		this.textField11.setWidth(100, Unit.PERCENTAGE);
		this.textField11.setHeight(-1, Unit.PIXELS);
		this.gridLayout2.addComponent(this.textField11, 1, 3);
		this.label12.setSizeUndefined();
		this.gridLayout2.addComponent(this.label12, 0, 4);
		this.textField12.setWidth(100, Unit.PERCENTAGE);
		this.textField12.setHeight(-1, Unit.PIXELS);
		this.gridLayout2.addComponent(this.textField12, 1, 4);
		this.label13.setSizeUndefined();
		this.gridLayout2.addComponent(this.label13, 0, 5);
		this.textField13.setWidth(100, Unit.PERCENTAGE);
		this.textField13.setHeight(-1, Unit.PIXELS);
		this.gridLayout2.addComponent(this.textField13, 1, 5);
		this.gridLayout2.setColumnExpandRatio(1, 100.0F);
		this.gridLayout3.setColumns(2);
		this.gridLayout3.setRows(8);
		this.label14.setSizeUndefined();
		this.gridLayout3.addComponent(this.label14, 0, 0);
		this.textField14.setWidth(100, Unit.PERCENTAGE);
		this.textField14.setHeight(-1, Unit.PIXELS);
		this.gridLayout3.addComponent(this.textField14, 1, 0);
		this.label15.setSizeUndefined();
		this.gridLayout3.addComponent(this.label15, 0, 1);
		this.textField15.setWidth(100, Unit.PERCENTAGE);
		this.textField15.setHeight(-1, Unit.PIXELS);
		this.gridLayout3.addComponent(this.textField15, 1, 1);
		this.label16.setSizeUndefined();
		this.gridLayout3.addComponent(this.label16, 0, 2);
		this.textField16.setWidth(100, Unit.PERCENTAGE);
		this.textField16.setHeight(-1, Unit.PIXELS);
		this.gridLayout3.addComponent(this.textField16, 1, 2);
		this.label17.setSizeUndefined();
		this.gridLayout3.addComponent(this.label17, 0, 3);
		this.passwordField.setWidth(100, Unit.PERCENTAGE);
		this.passwordField.setHeight(-1, Unit.PIXELS);
		this.gridLayout3.addComponent(this.passwordField, 1, 3);
		this.label18.setSizeUndefined();
		this.gridLayout3.addComponent(this.label18, 0, 4);
		this.textField18.setWidth(100, Unit.PERCENTAGE);
		this.textField18.setHeight(-1, Unit.PIXELS);
		this.gridLayout3.addComponent(this.textField18, 1, 4);
		this.label19.setSizeUndefined();
		this.gridLayout3.addComponent(this.label19, 0, 5);
		this.textField19.setWidth(100, Unit.PERCENTAGE);
		this.textField19.setHeight(-1, Unit.PIXELS);
		this.gridLayout3.addComponent(this.textField19, 1, 5);
		this.label20.setSizeUndefined();
		this.gridLayout3.addComponent(this.label20, 0, 6);
		this.textField20.setWidth(100, Unit.PERCENTAGE);
		this.textField20.setHeight(-1, Unit.PIXELS);
		this.gridLayout3.addComponent(this.textField20, 1, 6);
		this.label21.setSizeUndefined();
		this.gridLayout3.addComponent(this.label21, 0, 7);
		this.textField21.setWidth(100, Unit.PERCENTAGE);
		this.textField21.setHeight(-1, Unit.PIXELS);
		this.gridLayout3.addComponent(this.textField21, 1, 7);
		this.gridLayout3.setColumnExpandRatio(1, 100.0F);
		this.gridLayout4.setSizeFull();
		this.tabSheet.addTab(this.gridLayout4, StringResourceUtils.optLocalizeString("{$gridLayout.caption}", this), null);
		this.gridLayout2.setSizeFull();
		this.tabSheet.addTab(this.gridLayout2, StringResourceUtils.optLocalizeString("{$gridLayout2.caption}", this), null);
		this.gridLayout3.setSizeFull();
		this.tabSheet.addTab(this.gridLayout3, StringResourceUtils.optLocalizeString("{$gridLayout3.caption}", this), null);
		this.tabSheet.setSelectedTab(this.gridLayout4);
		this.cmdSave.setSizeUndefined();
		this.horizontalLayout.addComponent(this.cmdSave);
		this.horizontalLayout.setComponentAlignment(this.cmdSave, Alignment.MIDDLE_LEFT);
		this.cmdReset.setSizeUndefined();
		this.horizontalLayout.addComponent(this.cmdReset);
		this.horizontalLayout.setComponentAlignment(this.cmdReset, Alignment.MIDDLE_LEFT);
		this.cmdAdminReset.setSizeUndefined();
		this.horizontalLayout.addComponent(this.cmdAdminReset);
		this.form.setColumns(1);
		this.form.setRows(2);
		this.tabSheet.setWidth(100, Unit.PERCENTAGE);
		this.tabSheet.setHeight(-1, Unit.PIXELS);
		this.form.addComponent(this.tabSheet, 0, 0);
		this.horizontalLayout.setSizeUndefined();
		this.form.addComponent(this.horizontalLayout, 0, 1);
		this.form.setComponentAlignment(this.horizontalLayout, Alignment.TOP_CENTER);
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
		this.cmdInfo.addClickListener(event -> this.cmdInfo_buttonClick(event));
		this.table.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				CompanyTabView.this.table_valueChange(event);
			}
		});
		this.cmdSave.addClickListener(event -> this.cmdSave_buttonClick(event));
		this.cmdReset.addClickListener(event -> this.cmdReset_buttonClick(event));
		this.cmdAdminReset.addClickListener(event -> this.cmdAdminReset_buttonClick(event));
	} // </generated-code>

	// <generated-code name="variables">
	private XdevButton cmdNew, cmdDelete, cmdReload, cmdInfo, cmdSave, cmdReset, cmdAdminReset;
	private XdevLabel label, label2, label3, label4, label5, label6, label7, label8, label9, label10, label11, label12,
			label13, label14, label15, label16, label17, label18, label19, label20, label21;
	private XdevFieldGroup<Company> fieldGroup;
	private XdevHorizontalLayout actionLayout, horizontalLayout;
	private XdevPasswordField passwordField;
	private XdevTabSheet tabSheet;
	private XdevTable<Company> table;
	private XdevGridLayout form, gridLayout4, gridLayout2, gridLayout3;
	private XdevTextField textField, textField2, textField3, textField4, textField5, textField6, textFieldStatus,
			textField8, textField9, textField10, textField11, textField12, textField13, textField14, textField15,
			textField16, textField18, textField19, textField20, textField21;
	private XdevVerticalLayout verticalLayout;
	private XdevHorizontalSplitPanel horizontalSplitPanel;
	private XdevContainerFilterComponent containerFilterComponent;
	// </generated-code>


}
