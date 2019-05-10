package ch.xwr.seicentobookit.ui.desktop;

import com.vaadin.data.Property;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
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
import ch.xwr.seicentobookit.dal.SalaryBvgBaseDAO;
import ch.xwr.seicentobookit.dal.SalaryBvgBaseLineDAO;
import ch.xwr.seicentobookit.entities.Employee;
import ch.xwr.seicentobookit.entities.Employee_;
import ch.xwr.seicentobookit.entities.SalaryBvgBase;
import ch.xwr.seicentobookit.entities.SalaryBvgBaseLine;
import ch.xwr.seicentobookit.entities.SalaryBvgBaseLine_;
import ch.xwr.seicentobookit.entities.SalaryBvgBase_;

public class SalaryBvgBaseTabView extends XdevView {

	/**
	 * 
	 */
	public SalaryBvgBaseTabView() {
		super();
		this.initUI();
		
		//State
		comboBox.addItems((Object[])LovState.State.values());
		
		//Filtering
		setDefaultFilter();
		setROFields();

	}
	
	private void setROFields() {
		
		boolean hasData = true;
		if (this.fieldGroup.getItemDataSource() == null) {
			hasData = false;
		}
		this.cmdSave.setEnabled(hasData);
		this.tabSheet.setEnabled(hasData);
		
	}


	private void setDefaultFilter() {
		LovState.State[] valState = new LovState.State[]{LovState.State.active};
		FilterData[] fd = new FilterData[]{new FilterData("sbeState", new FilterOperator.Is(), valState)};
				
		this.containerFilterComponent.setFilterData(fd);
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

	private SalaryBvgBase getNewBeanWithDefaults() {
		SalaryBvgBase bean = new SalaryBvgBase();
		
		bean.setSbeState(LovState.State.active);
		
		return bean;
	}
	
	private boolean isNew() {
		if (this.fieldGroup.getItemDataSource() == null) {
			return true;
		}
		final SalaryBvgBase bean = this.fieldGroup.getItemDataSource().getBean();
		if (bean.getSbeId() < 1) {
			return true;
		}
		return false;
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
				SalaryBvgBase bean = table.getSelectedItem().getBean();
				// Salary bean = this.fieldGroup.getItemDataSource().getBean();
				// Delete Record
				RowObjectManager man = new RowObjectManager();
				man.deleteObject(bean.getSbeId(), bean.getClass().getSimpleName());
			
				// JPADAO x = new JPADAO<Salary, Long>(Salary.class); //generic??
				// x.remove(bean);
			
				SalaryBvgBaseDAO dao = new SalaryBvgBaseDAO();
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
//		table.refreshRowCache();
//		table.getBeanContainerDataSource().refresh();
//		table.sort();
		
		reloadTableList();	
	}

	/**
	 * Event handler delegate method for the {@link XdevButton}
	 * {@link #cmdInfo}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdInfo_buttonClick(Button.ClickEvent event) {
		SalaryBvgBase bean = this.fieldGroup.getItemDataSource().getBean();
	
		Window win = new Window();
		win.setWidth("1020");
		win.setHeight("700");
		win.center();
		win.setModal(true);
	
		// UI.getCurrent().getSession().setAttribute(String.class,
		// bean.getClass().getSimpleName());
		win.setContent(new RowObjectView(bean.getSbeId(), bean.getClass().getSimpleName()));
		this.getUI().addWindow(win);
	}

	private void reloadTableList() {
		final XdevBeanContainer<SalaryBvgBase> myList = this.table.getBeanContainerDataSource();
		myList.removeAll();
		myList.addAll(new SalaryBvgBaseDAO().findAll());

		this.table.refreshRowCache();
		this.table.getBeanContainerDataSource().refresh();
		this.table.sort();		
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

		reloadTableLineList();
		setROFields();
	}

	private void reloadTableLineList() {
		if (this.fieldGroup.getItemDataSource() == null) {
			return;
		}
		final SalaryBvgBase bean = this.fieldGroup.getItemDataSource().getBean();

		final XdevBeanContainer<SalaryBvgBaseLine> myList = this.tableLine.getBeanContainerDataSource();
		//List li = new SalaryBvgBaseLineDAO().findByHeader(bean);
		
		myList.removeAll();
		myList.addAll(new SalaryBvgBaseLineDAO().findByHeader(bean));

		this.tableLine.refreshRowCache();
		this.tableLine.getBeanContainerDataSource().refresh();
		
	}

	/**
	 * Event handler delegate method for the {@link XdevButton} {@link #cmdNewLine}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdNewLine_buttonClick(final Button.ClickEvent event) {
		if (this.table.getSelectedItem() == null) {
//			Notification.show("Zeile hinzufügen", "Rechnungskopf wurde noch nich gespeichert",
//					Notification.Type.WARNING_MESSAGE);
//			return;
			this.cmdSave.click();
		}

		final Long beanId = null;
		final Long objId = this.fieldGroup.getItemDataSource().getBean().getSbeId();

		UI.getCurrent().getSession().setAttribute("beanId", beanId);
		UI.getCurrent().getSession().setAttribute("objId", objId);
		
		popupBvgLine();
	
	}

	private void popupBvgLine() {
		final Window win = SalaryBvgBaseLinePopup.getPopupWindow();

		win.addCloseListener(new CloseListener() {
			@Override
			public void windowClose(final CloseEvent e) {
				String retval = UI.getCurrent().getSession().getAttribute(String.class);
				if (retval == null) {
					retval = "cmdCancel";
				}
				if (retval.equals("cmdSave")) {
					reloadTableLineList();

					//calculateHeader();
					//saveHeader(false);
				}

			}
		});
		this.getUI().addWindow(win);
	}

	/**
	 * Event handler delegate method for the {@link XdevButton} {@link #cmdSave}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdSave_buttonClick(final Button.ClickEvent event) {
		this.fieldGroup.save();
		
		RowObjectManager man = new RowObjectManager();
		man.updateObject(this.fieldGroup.getItemDataSource().getBean().getSbeId(), this.fieldGroup.getItemDataSource().getBean().getClass().getSimpleName());
		
		Notification.show("Save clicked", "Daten wurden gespeichert", Notification.Type.TRAY_NOTIFICATION);
		reloadTableList();	
	}

	/**
	 * Event handler delegate method for the {@link XdevButton} {@link #cmdReset}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdReset_buttonClick(final Button.ClickEvent event) {
		if (isNew()) {
			cmdNew_buttonClick(event);
		} else {
			this.fieldGroup.discard();
		}
		setROFields();
	}

	/**
	 * Event handler delegate method for the {@link XdevButton}
	 * {@link #cmdEditLine}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdEditLine_buttonClick(final Button.ClickEvent event) {
		if (this.tableLine.getSelectedItem() == null) {
			return;
		}
	
		final Long beanId = this.tableLine.getSelectedItem().getBean().getSbxId();
		final Long objId = null;
	
		UI.getCurrent().getSession().setAttribute("beanId", beanId);
		UI.getCurrent().getSession().setAttribute("objId", objId);
	
		popupBvgLine();
	
	}

	/**
	 * Event handler delegate method for the {@link XdevButton}
	 * {@link #cmdDeleteLine}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdDeleteLine_buttonClick(final Button.ClickEvent event) {
		if (this.tableLine.getSelectedItem() == null) {
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
				SalaryBvgBaseLine bean = tableLine.getSelectedItem().getBean();
				// Delete Record
				RowObjectManager man = new RowObjectManager();
				man.deleteObject(bean.getSbxId(), bean.getClass().getSimpleName());
	
				SalaryBvgBaseLineDAO dao = new SalaryBvgBaseLineDAO();
				dao.remove(bean);
				reloadTableLineList();
	
				tableLine.select(tableLine.getCurrentPageFirstItemId());
				Notification.show("Datensatz löschen", "Datensatz wurde gelöscht!", Notification.Type.TRAY_NOTIFICATION);
			}
	
		});
	
	}

	/**
	 * Event handler delegate method for the {@link XdevTable} {@link #tableLine}.
	 *
	 * @see Property.ValueChangeListener#valueChange(Property.ValueChangeEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void tableLine_valueChange(final Property.ValueChangeEvent event) {
	
	}

	/**
	 * Event handler delegate method for the {@link XdevTable} {@link #tableLine}.
	 *
	 * @see ItemClickEvent.ItemClickListener#itemClick(ItemClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void tableLine_itemClick(final ItemClickEvent event) {
		if (event.isDoubleClick()) {
			// Notification.show("Event Triggered ", Notification.Type.TRAY_NOTIFICATION);
			final SalaryBvgBaseLine obj = (SalaryBvgBaseLine) event.getItemId();
			this.tableLine.select(obj); // reselect after double-click
	
			final Long beanId = obj.getSbxId();
			final Long objId = null;
	
			UI.getCurrent().getSession().setAttribute("beanId", beanId);
			UI.getCurrent().getSession().setAttribute("objId", objId);
	
			popupBvgLine();
		}
	
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
		this.data = new XdevPanel();
		this.verticalLayout3 = new XdevVerticalLayout();
		this.gridLayout2 = new XdevGridLayout();
		this.label = new XdevLabel();
		this.textField = new XdevTextField();
		this.label2 = new XdevLabel();
		this.textField2 = new XdevTextField();
		this.label3 = new XdevLabel();
		this.textField3 = new XdevTextField();
		this.label12 = new XdevLabel();
		this.comboBox = new XdevComboBox<>();
		this.fieldGroup = new XdevFieldGroup<>(SalaryBvgBase.class);
		this.horizontalLayoutLine2 = new XdevHorizontalLayout();
		this.cmdNewLine = new XdevButton();
		this.cmdEditLine = new XdevButton();
		this.cmdDeleteLine = new XdevButton();
		this.tableLine = new XdevTable<>();
		this.relation = new XdevPanel();
		this.verticalLayout2 = new XdevVerticalLayout();
		this.table2 = new XdevTable<>();
		this.horizontalLayout = new XdevHorizontalLayout();
		this.cmdSave = new XdevButton();
		this.cmdReset = new XdevButton();
	
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
		this.table.setColumnCollapsingAllowed(true);
		this.table.setContainerDataSource(SalaryBvgBase.class, DAOs.get(SalaryBvgBaseDAO.class).findAll());
		this.table.setVisibleColumns(SalaryBvgBase_.sbeName.getName(), SalaryBvgBase_.sbeState.getName(),
				SalaryBvgBase_.sbeCoordinationAmt.getName(), SalaryBvgBase_.sbeSalarydefAmt.getName());
		this.table.setColumnHeader("sbeName", "Name");
		this.table.setColumnHeader("sbeState", "Status");
		this.table.setColumnHeader("sbeCoordinationAmt", "Koordinationsbetrag");
		this.table.setColumnCollapsed("sbeCoordinationAmt", true);
		this.table.setColumnHeader("sbeSalarydefAmt", "Basislohn");
		this.table.setColumnCollapsed("sbeSalarydefAmt", true);
		this.form.setMargin(new MarginInfo(false, true, false, false));
		this.tabSheet.setStyleName("framed");
		this.data.setTabIndex(0);
		this.data.setScrollTop(1);
		this.verticalLayout3.setCaption("Tab");
		this.verticalLayout3.setMargin(new MarginInfo(false, true, true, true));
		this.gridLayout2.setCaption("");
		this.label.setValue(StringResourceUtils.optLocalizeString("{$label.value}", this));
		this.textField.setRequired(true);
		this.textField.setMaxLength(50);
		this.label2.setValue(StringResourceUtils.optLocalizeString("{$label2.value}", this));
		this.textField2.setConverter(ConverterBuilder.stringToDouble().currency().build());
		this.label3.setValue(StringResourceUtils.optLocalizeString("{$label3.value}", this));
		this.textField3.setConverter(ConverterBuilder.stringToDouble().currency().build());
		this.label12.setValue(StringResourceUtils.optLocalizeString("{$label12.value}", this));
		this.fieldGroup.bind(this.textField, SalaryBvgBase_.sbeName.getName());
		this.fieldGroup.bind(this.textField2, SalaryBvgBase_.sbeCoordinationAmt.getName());
		this.fieldGroup.bind(this.textField3, SalaryBvgBase_.sbeSalarydefAmt.getName());
		this.fieldGroup.bind(this.comboBox, SalaryBvgBase_.sbeState.getName());
		this.horizontalLayoutLine2.setSpacing(false);
		this.horizontalLayoutLine2.setMargin(new MarginInfo(false, true, false, false));
		this.cmdNewLine.setIcon(FontAwesome.PLUS_CIRCLE);
		this.cmdNewLine.setCaption("New...");
		this.cmdEditLine.setIcon(FontAwesome.EDIT);
		this.cmdEditLine.setCaption("Edit...");
		this.cmdDeleteLine.setIcon(FontAwesome.ERASER);
		this.cmdDeleteLine.setCaption("Löschen");
		this.tableLine.setColumnCollapsingAllowed(true);
		this.tableLine.setContainerDataSource(SalaryBvgBaseLine.class, false);
		this.tableLine.setVisibleColumns(SalaryBvgBaseLine_.sbxValidFrom.getName(),
				SalaryBvgBaseLine_.sbxAgeStartYear.getName(), SalaryBvgBaseLine_.sbxCompany.getName(),
				SalaryBvgBaseLine_.sbxWorker.getName(), SalaryBvgBaseLine_.sbxState.getName());
		this.tableLine.setColumnHeader("sbxValidFrom", "Gültig ab");
		this.tableLine.setConverter("sbxValidFrom", ConverterBuilder.stringToDate().dateOnly().build());
		this.tableLine.setColumnHeader("sbxAgeStartYear", "Alter (JJ) ab");
		this.tableLine.setColumnHeader("sbxCompany", "Arbeitgeber");
		this.tableLine.setColumnAlignment("sbxCompany", Table.Align.RIGHT);
		this.tableLine.setConverter("sbxCompany", ConverterBuilder.stringToDouble().minimumFractionDigits(3).build());
		this.tableLine.setColumnHeader("sbxWorker", "Arbeitnehmer");
		this.tableLine.setColumnAlignment("sbxWorker", Table.Align.RIGHT);
		this.tableLine.setConverter("sbxWorker", ConverterBuilder.stringToDouble().minimumFractionDigits(3).build());
		this.tableLine.setColumnHeader("sbxState", "Status");
		this.relation.setTabIndex(0);
		this.verticalLayout2.setCaption("");
		this.table2.setContainerDataSource(Employee.class, false);
		this.table2.setVisibleColumns(Employee_.empNumber.getName(), Employee_.empLastName.getName(),
				Employee_.empFirstName.getName(), Employee_.empAddress.getName(), Employee_.empZip.getName(),
				Employee_.empPlace.getName(), Employee_.empState.getName());
		this.table2.setColumnHeader("empNumber", "Nummer");
		this.table2.setColumnHeader("empLastName", "Name");
		this.table2.setColumnHeader("empFirstName", "Vorname");
		this.table2.setColumnHeader("empAddress", "Adresse");
		this.table2.setColumnHeader("empZip", "PLZ");
		this.table2.setColumnHeader("empPlace", "Ort");
		this.table2.setColumnHeader("empState", "Status");
		this.horizontalLayout.setMargin(new MarginInfo(false, false, false, true));
		this.cmdSave.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/save1.png"));
		this.cmdSave.setCaption(StringResourceUtils.optLocalizeString("{$cmdSave.caption}", this));
		this.cmdSave.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		this.cmdReset.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/delete1.png"));
		this.cmdReset.setCaption(StringResourceUtils.optLocalizeString("{$cmdReset.caption}", this));
		this.cmdReset.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
	
		MasterDetail.connect(this.table, this.fieldGroup);
		MasterDetail.connect(this.table, this.table2);
	
		this.containerFilterComponent.setContainer(this.table.getBeanContainerDataSource(), "sbeName", "sbeState",
				"sbeCoordinationAmt", "sbeSalarydefAmt");
		this.containerFilterComponent.setSearchableProperties("sbeName");
	
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
		this.verticalLayout.setExpandRatio(this.table, 100.0F);
		this.gridLayout2.setColumns(2);
		this.gridLayout2.setRows(5);
		this.label.setSizeUndefined();
		this.gridLayout2.addComponent(this.label, 0, 0);
		this.textField.setWidth(100, Unit.PERCENTAGE);
		this.textField.setHeight(-1, Unit.PIXELS);
		this.gridLayout2.addComponent(this.textField, 1, 0);
		this.label2.setSizeUndefined();
		this.gridLayout2.addComponent(this.label2, 0, 1);
		this.textField2.setSizeUndefined();
		this.gridLayout2.addComponent(this.textField2, 1, 1);
		this.label3.setSizeUndefined();
		this.gridLayout2.addComponent(this.label3, 0, 2);
		this.textField3.setSizeUndefined();
		this.gridLayout2.addComponent(this.textField3, 1, 2);
		this.label12.setSizeUndefined();
		this.gridLayout2.addComponent(this.label12, 0, 3);
		this.comboBox.setSizeUndefined();
		this.gridLayout2.addComponent(this.comboBox, 1, 3);
		this.gridLayout2.setColumnExpandRatio(1, 10.0F);
		final CustomComponent gridLayout2_vSpacer = new CustomComponent();
		gridLayout2_vSpacer.setSizeFull();
		this.gridLayout2.addComponent(gridLayout2_vSpacer, 0, 4, 1, 4);
		this.gridLayout2.setRowExpandRatio(4, 1.0F);
		this.cmdNewLine.setSizeUndefined();
		this.horizontalLayoutLine2.addComponent(this.cmdNewLine);
		this.horizontalLayoutLine2.setComponentAlignment(this.cmdNewLine, Alignment.MIDDLE_CENTER);
		this.cmdEditLine.setSizeUndefined();
		this.horizontalLayoutLine2.addComponent(this.cmdEditLine);
		this.horizontalLayoutLine2.setComponentAlignment(this.cmdEditLine, Alignment.MIDDLE_CENTER);
		this.cmdDeleteLine.setSizeUndefined();
		this.horizontalLayoutLine2.addComponent(this.cmdDeleteLine);
		this.horizontalLayoutLine2.setComponentAlignment(this.cmdDeleteLine, Alignment.MIDDLE_CENTER);
		final CustomComponent horizontalLayoutLine2_spacer = new CustomComponent();
		horizontalLayoutLine2_spacer.setSizeFull();
		this.horizontalLayoutLine2.addComponent(horizontalLayoutLine2_spacer);
		this.horizontalLayoutLine2.setExpandRatio(horizontalLayoutLine2_spacer, 1.0F);
		this.gridLayout2.setWidth(100, Unit.PERCENTAGE);
		this.gridLayout2.setHeight(-1, Unit.PIXELS);
		this.verticalLayout3.addComponent(this.gridLayout2);
		this.verticalLayout3.setComponentAlignment(this.gridLayout2, Alignment.MIDDLE_CENTER);
		this.horizontalLayoutLine2.setWidth(100, Unit.PERCENTAGE);
		this.horizontalLayoutLine2.setHeight(-1, Unit.PIXELS);
		this.verticalLayout3.addComponent(this.horizontalLayoutLine2);
		this.tableLine.setSizeFull();
		this.verticalLayout3.addComponent(this.tableLine);
		this.verticalLayout3.setComponentAlignment(this.tableLine, Alignment.MIDDLE_CENTER);
		this.verticalLayout3.setExpandRatio(this.tableLine, 100.0F);
		this.verticalLayout3.setSizeFull();
		this.data.setContent(this.verticalLayout3);
		this.table2.setSizeFull();
		this.verticalLayout2.addComponent(this.table2);
		this.verticalLayout2.setComponentAlignment(this.table2, Alignment.MIDDLE_CENTER);
		this.verticalLayout2.setExpandRatio(this.table2, 100.0F);
		this.verticalLayout2.setSizeFull();
		this.relation.setContent(this.verticalLayout2);
		this.data.setSizeFull();
		this.tabSheet.addTab(this.data, "Details", null);
		this.relation.setSizeFull();
		this.tabSheet.addTab(this.relation, "Referenzen", null);
		this.tabSheet.setSelectedTab(this.data);
		this.cmdSave.setSizeUndefined();
		this.horizontalLayout.addComponent(this.cmdSave);
		this.horizontalLayout.setComponentAlignment(this.cmdSave, Alignment.MIDDLE_CENTER);
		this.cmdReset.setSizeUndefined();
		this.horizontalLayout.addComponent(this.cmdReset);
		this.horizontalLayout.setComponentAlignment(this.cmdReset, Alignment.MIDDLE_CENTER);
		this.form.setColumns(1);
		this.form.setRows(2);
		this.tabSheet.setSizeFull();
		this.form.addComponent(this.tabSheet, 0, 0);
		this.horizontalLayout.setSizeUndefined();
		this.form.addComponent(this.horizontalLayout, 0, 1);
		this.form.setComponentAlignment(this.horizontalLayout, Alignment.BOTTOM_CENTER);
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
		this.table.addValueChangeListener(event -> this.table_valueChange(event));
		this.cmdNewLine.addClickListener(event -> this.cmdNewLine_buttonClick(event));
		this.cmdEditLine.addClickListener(event -> this.cmdEditLine_buttonClick(event));
		this.cmdDeleteLine.addClickListener(event -> this.cmdDeleteLine_buttonClick(event));
		this.tableLine.addValueChangeListener(event -> this.tableLine_valueChange(event));
		this.tableLine.addItemClickListener(event -> this.tableLine_itemClick(event));
		this.cmdSave.addClickListener(event -> this.cmdSave_buttonClick(event));
		this.cmdReset.addClickListener(event -> this.cmdReset_buttonClick(event));
	} // </generated-code>

	// <generated-code name="variables">
	private XdevButton cmdNew, cmdDelete, cmdReload, cmdInfo, cmdNewLine, cmdEditLine, cmdDeleteLine, cmdSave, cmdReset;
	private XdevLabel label, label2, label3, label12;
	private XdevFieldGroup<SalaryBvgBase> fieldGroup;
	private XdevTable<Employee> table2;
	private XdevTabSheet tabSheet;
	private XdevPanel data, relation;
	private XdevGridLayout form, gridLayout2;
	private XdevHorizontalSplitPanel horizontalSplitPanel;
	private XdevComboBox<CustomComponent> comboBox;
	private XdevContainerFilterComponent containerFilterComponent;
	private XdevTable<SalaryBvgBase> table;
	private XdevHorizontalLayout actionLayout, horizontalLayoutLine2, horizontalLayout;
	private XdevTable<SalaryBvgBaseLine> tableLine;
	private XdevTextField textField, textField2, textField3;
	private XdevVerticalLayout verticalLayout, verticalLayout3, verticalLayout2;
	// </generated-code>


}
