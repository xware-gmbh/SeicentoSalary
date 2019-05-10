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
import com.vaadin.ui.TabSheet;
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
import ch.xwr.seicentobookit.dal.SalaryCalculationDAO;
import ch.xwr.seicentobookit.dal.SalaryCalculationLineDAO;
import ch.xwr.seicentobookit.entities.SalaryCalculation;
import ch.xwr.seicentobookit.entities.SalaryCalculationLine;
import ch.xwr.seicentobookit.entities.SalaryCalculationLine_;
import ch.xwr.seicentobookit.entities.SalaryCalculation_;

public class SalaryCalculationTabView extends XdevView {

	/**
	 * 
	 */
	public SalaryCalculationTabView() {
		super();
		this.initUI();
		
		//dummy (hight get lost)
		//this.tabSheet.setWidth(100, Unit.PERCENTAGE);
		//this.tabSheet.setHeight(-1, Unit.PIXELS);
		
		//State
		comboBox.addItems((Object[])LovState.State.values());
		
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
		FilterData[] fd = new FilterData[]{new FilterData("sleState", new FilterOperator.Is(), valState)};
				
		this.containerFilterComponent.setFilterData(fd);
	}

	/**
	 * Event handler delegate method for the {@link XdevButton}
	 * {@link #cmdSave}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdSave_buttonClick(Button.ClickEvent event) {
		Notification.show("Save clicked", Notification.Type.TRAY_NOTIFICATION);
		this.fieldGroup.save();
	
		RowObjectManager man = new RowObjectManager();
		man.updateObject(this.fieldGroup.getItemDataSource().getBean().getSleId(),
				this.fieldGroup.getItemDataSource().getBean().getClass().getSimpleName());
		
		reloadTableList();
	}


	/**
	 * Event handler delegate method for the {@link XdevTabSheet} {@link #tabSheet}.
	 *
	 * @see TabSheet.SelectedTabChangeListener#selectedTabChange(TabSheet.SelectedTabChangeEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void tabSheet_selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
	
	}

	private SalaryCalculation getNewBeanWithDefaults() {
		SalaryCalculation bean = new SalaryCalculation();
		
		bean.setSleState(LovState.State.active);
		
		return bean;
	}
	

	/**
	 * Event handler delegate method for the {@link XdevButton} {@link #cmdNew}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdNew_buttonClick(Button.ClickEvent event) {
		this.fieldGroup.setItemDataSource(getNewBeanWithDefaults());
		setROFields();
	}


	/**
	 * Event handler delegate method for the {@link XdevButton} {@link #cmdDelete}.
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
				SalaryCalculation bean = table.getSelectedItem().getBean();
				// Salary bean = this.fieldGroup.getItemDataSource().getBean();
				// Delete Record
				RowObjectManager man = new RowObjectManager();
				man.deleteObject(bean.getSleId(), bean.getClass().getSimpleName());
			
				// JPADAO x = new JPADAO<Salary, Long>(Salary.class); //generic??
				// x.remove(bean);
			
				SalaryCalculationDAO dao = new SalaryCalculationDAO();
				dao.remove(bean);
				table.getBeanContainerDataSource().refresh();
			
				table.select(table.getCurrentPageFirstItemId());
				Notification.show("Datensatz löschen", "Datensatz wurde gelöscht!", Notification.Type.TRAY_NOTIFICATION);
			}
			
		});		

	}


	/**
	 * Event handler delegate method for the {@link XdevButton} {@link #cmdReload}.
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
	 * Event handler delegate method for the {@link XdevButton} {@link #cmdInfo}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdInfo_buttonClick(Button.ClickEvent event) {
		SalaryCalculation bean = this.fieldGroup.getItemDataSource().getBean();
	
		Window win = new Window();
		win.setWidth("1020");
		win.setHeight("700");
		win.center();
		win.setModal(true);
	
		// UI.getCurrent().getSession().setAttribute(String.class,
		// bean.getClass().getSimpleName());
		win.setContent(new RowObjectView(bean.getSleId(), bean.getClass().getSimpleName()));
		this.getUI().addWindow(win);
	}


	private void popupCalculationLine() {
		final Window win = SalaryCalculationLinePopup.getPopupWindow();

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

	private void reloadTableLineList() {
		if (this.fieldGroup.getItemDataSource() == null) {
			return;
		}
		final SalaryCalculation bean = this.fieldGroup.getItemDataSource().getBean();

		final XdevBeanContainer<SalaryCalculationLine> myList = this.tableLine.getBeanContainerDataSource();
		//List li = new SalaryBvgBaseLineDAO().findByHeader(bean);
		
		myList.removeAll();
		myList.addAll(new SalaryCalculationLineDAO().findByHeader(bean));

		this.tableLine.refreshRowCache();
		this.tableLine.getBeanContainerDataSource().refresh();
		
	}

	private void reloadTableList() {
		final XdevBeanContainer<SalaryCalculation> myList = this.table.getBeanContainerDataSource();
		
		myList.removeAll();
		myList.addAll(new SalaryCalculationDAO().findAll());

		this.table.refreshRowCache();
		this.table.getBeanContainerDataSource().refresh();
		
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

	private boolean isNew() {
		if (this.fieldGroup.getItemDataSource() == null) {
			return true;
		}
		final SalaryCalculation bean = this.fieldGroup.getItemDataSource().getBean();
		if (bean.getSleId() < 1) {
			return true;
		}
		return false;
	}


	/**
	 * Event handler delegate method for the {@link XdevButton} {@link #cmdNewLine}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdNewLine_buttonClick(final Button.ClickEvent event) {
		if (this.table.getSelectedItem() == null) {
			// Notification.show("Zeile hinzufügen", "Rechnungskopf wurde noch nich
			// gespeichert",
			// Notification.Type.WARNING_MESSAGE);
			// return;
			this.cmdSave.click();
		}
	
		final Long beanId = null;
		final Long objId = this.fieldGroup.getItemDataSource().getBean().getSleId();
	
		UI.getCurrent().getSession().setAttribute("beanId", beanId);
		UI.getCurrent().getSession().setAttribute("objId", objId);
	
		popupCalculationLine();
	
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
	
		final Long beanId = this.tableLine.getSelectedItem().getBean().getSlxId();
		final Long objId = null;
	
		UI.getCurrent().getSession().setAttribute("beanId", beanId);
		UI.getCurrent().getSession().setAttribute("objId", objId);
	
		popupCalculationLine();
	
	}


	/**
	 * Event handler delegate method for the {@link XdevButton}
	 * {@link #cmdDeleteLine}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdDeleteLine_buttonClick(final Button.ClickEvent event) {
		if (tableLine.getSelectedItem() == null) {
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
				SalaryCalculationLine bean = tableLine.getSelectedItem().getBean();
				// Delete Record
				RowObjectManager man = new RowObjectManager();
				man.deleteObject(bean.getSlxId(), bean.getClass().getSimpleName());
	
				SalaryCalculationLineDAO dao = new SalaryCalculationLineDAO();
				dao.remove(bean);
				reloadTableLineList();
				// tableLine.getBeanContainerDataSource().refresh();
	
				tableLine.select(tableLine.getCurrentPageFirstItemId());
				Notification.show("Datensatz löschen", "Datensatz wurde gelöscht!", Notification.Type.TRAY_NOTIFICATION);
			}
	
		});
	
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
			final SalaryCalculationLine obj = (SalaryCalculationLine) event.getItemId();
			this.tableLine.select(obj); // reselect after double-click
	
			final Long beanId = obj.getSlxId();
			final Long objId = null;
	
			UI.getCurrent().getSession().setAttribute("beanId", beanId);
			UI.getCurrent().getSession().setAttribute("objId", objId);
	
			popupCalculationLine();
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
		this.gridLayout = new XdevGridLayout();
		this.tabSheet = new XdevTabSheet();
		this.panel = new XdevPanel();
		this.verticalLayout2 = new XdevVerticalLayout();
		this.form = new XdevGridLayout();
		this.lblSleName = new XdevLabel();
		this.txtSleName = new XdevTextField();
		this.lblSleDescription = new XdevLabel();
		this.txtSleDescription = new XdevTextField();
		this.lblSleState = new XdevLabel();
		this.comboBox = new XdevComboBox<>();
		this.fieldGroup = new XdevFieldGroup<>(SalaryCalculation.class);
		this.horizontalLayout2 = new XdevHorizontalLayout();
		this.cmdNewLine = new XdevButton();
		this.cmdEditLine = new XdevButton();
		this.cmdDeleteLine = new XdevButton();
		this.tableLine = new XdevTable<>();
		this.horizontalLayout = new XdevHorizontalLayout();
		this.cmdSave = new XdevButton();
		this.cmdReset = new XdevButton();
	
		this.horizontalSplitPanel.setStyleName("frozen");
		this.horizontalSplitPanel.setSplitPosition(30.0F, Unit.PERCENTAGE);
		this.verticalLayout.setStyleName("bar");
		this.verticalLayout.setMargin(new MarginInfo(false, false, true, false));
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
		this.cmdInfo
				.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/info_small.jpg"));
		this.cmdInfo.setStyleName("icon-only");
		this.table.setColumnReorderingAllowed(true);
		this.table.setColumnCollapsingAllowed(true);
		this.table.setContainerDataSource(SalaryCalculation.class, DAOs.get(SalaryCalculationDAO.class).findAll());
		this.table.setVisibleColumns(SalaryCalculation_.sleName.getName(), SalaryCalculation_.sleState.getName(),
				SalaryCalculation_.sleDescription.getName());
		this.table.setColumnHeader("sleName", "Name");
		this.table.setColumnHeader("sleState", "Status");
		this.table.setColumnHeader("sleDescription", "Beschreibung");
		this.table.setColumnCollapsed("sleDescription", true);
		this.gridLayout.setSpacing(false);
		this.gridLayout.setMargin(new MarginInfo(false));
		this.tabSheet.setStyleName("framed");
		this.panel.setTabIndex(0);
		this.verticalLayout2.setCaption("Tab");
		this.form.setMargin(new MarginInfo(true, true, false, true));
		this.lblSleName.setValue(StringResourceUtils.optLocalizeString("{$lblSleName.value}", this));
		this.txtSleName.setRequired(true);
		this.txtSleName.setMaxLength(50);
		this.lblSleDescription.setValue(StringResourceUtils.optLocalizeString("{$lblSleDescription.value}", this));
		this.lblSleState.setValue(StringResourceUtils.optLocalizeString("{$lblSleState.value}", this));
		this.fieldGroup.bind(this.txtSleName, SalaryCalculation_.sleName.getName());
		this.fieldGroup.bind(this.txtSleDescription, SalaryCalculation_.sleDescription.getName());
		this.fieldGroup.bind(this.comboBox, SalaryCalculation_.sleState.getName());
		this.horizontalLayout2.setSpacing(false);
		this.horizontalLayout2.setMargin(new MarginInfo(false));
		this.cmdNewLine.setIcon(FontAwesome.PLUS_CIRCLE);
		this.cmdNewLine.setCaption("Neu...");
		this.cmdEditLine.setIcon(FontAwesome.EDIT);
		this.cmdEditLine.setCaption("Edit...");
		this.cmdDeleteLine.setIcon(FontAwesome.ERASER);
		this.cmdDeleteLine.setCaption("Löschen");
		this.tableLine.setContainerDataSource(SalaryCalculationLine.class, false);
		this.tableLine.setVisibleColumns(SalaryCalculationLine_.slxValidFrom.getName(),
				SalaryCalculationLine_.slxFactorAhv.getName(), SalaryCalculationLine_.slxFactorAlv.getName(),
				SalaryCalculationLine_.slxFactorKtg.getName(), SalaryCalculationLine_.slxFactorFak.getName(),
				SalaryCalculationLine_.slxFactorAdmin.getName(), SalaryCalculationLine_.slxFactorSol.getName(),
				SalaryCalculationLine_.slxCoordinationAlv.getName(), SalaryCalculationLine_.slxSldLowerBoundry.getName(),
				SalaryCalculationLine_.slxSldUpperBoundry.getName());
		this.tableLine.setColumnHeader("slxValidFrom", "Gültig ab");
		this.tableLine.setConverter("slxValidFrom", ConverterBuilder.stringToDate().dateOnly().build());
		this.tableLine.setColumnHeader("slxFactorAhv", "Ahv");
		this.tableLine.setColumnHeader("slxFactorAlv", "Alv");
		this.tableLine.setColumnHeader("slxFactorKtg", "Ktg");
		this.tableLine.setColumnHeader("slxFactorFak", "Fak");
		this.tableLine.setColumnHeader("slxFactorAdmin", "Admin");
		this.tableLine.setColumnHeader("slxFactorSol", "SOL");
		this.tableLine.setColumnHeader("slxCoordinationAlv", "Koord.Lohn");
		this.tableLine.setColumnHeader("slxSldLowerBoundry", "SOL UGrenze");
		this.tableLine.setColumnHeader("slxSldUpperBoundry", "SOL OGrenze");
		this.horizontalLayout.setMargin(new MarginInfo(true, false, true, true));
		this.cmdSave.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/save1.png"));
		this.cmdSave.setCaption(StringResourceUtils.optLocalizeString("{$cmdSave.caption}", this));
		this.cmdSave.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		this.cmdReset.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/delete1.png"));
		this.cmdReset.setCaption(StringResourceUtils.optLocalizeString("{$cmdReset.caption}", this));
		this.cmdReset.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
	
		MasterDetail.connect(this.table, this.fieldGroup);
	
		this.containerFilterComponent.setContainer(this.table.getBeanContainerDataSource(), "sleName", "sleState");
		this.containerFilterComponent.setSearchableProperties("sleName");
	
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
		this.actionLayout.setComponentAlignment(this.cmdInfo, Alignment.MIDDLE_LEFT);
		final CustomComponent actionLayout_spacer = new CustomComponent();
		actionLayout_spacer.setSizeFull();
		this.actionLayout.addComponent(actionLayout_spacer);
		this.actionLayout.setExpandRatio(actionLayout_spacer, 1.0F);
		this.containerFilterComponent.setWidth(98, Unit.PERCENTAGE);
		this.containerFilterComponent.setHeight(-1, Unit.PIXELS);
		this.verticalLayout.addComponent(this.containerFilterComponent);
		this.verticalLayout.setComponentAlignment(this.containerFilterComponent, Alignment.MIDDLE_CENTER);
		this.actionLayout.setWidth(98, Unit.PERCENTAGE);
		this.actionLayout.setHeight(-1, Unit.PIXELS);
		this.verticalLayout.addComponent(this.actionLayout);
		this.verticalLayout.setComponentAlignment(this.actionLayout, Alignment.MIDDLE_LEFT);
		this.table.setWidth(98, Unit.PERCENTAGE);
		this.table.setHeight(100, Unit.PERCENTAGE);
		this.verticalLayout.addComponent(this.table);
		this.verticalLayout.setComponentAlignment(this.table, Alignment.MIDDLE_CENTER);
		this.verticalLayout.setExpandRatio(this.table, 100.0F);
		this.form.setColumns(2);
		this.form.setRows(4);
		this.lblSleName.setSizeUndefined();
		this.form.addComponent(this.lblSleName, 0, 0);
		this.txtSleName.setWidth(100, Unit.PERCENTAGE);
		this.txtSleName.setHeight(-1, Unit.PIXELS);
		this.form.addComponent(this.txtSleName, 1, 0);
		this.lblSleDescription.setSizeUndefined();
		this.form.addComponent(this.lblSleDescription, 0, 1);
		this.txtSleDescription.setWidth(100, Unit.PERCENTAGE);
		this.txtSleDescription.setHeight(-1, Unit.PIXELS);
		this.form.addComponent(this.txtSleDescription, 1, 1);
		this.lblSleState.setSizeUndefined();
		this.form.addComponent(this.lblSleState, 0, 2);
		this.comboBox.setSizeUndefined();
		this.form.addComponent(this.comboBox, 1, 2);
		this.form.setColumnExpandRatio(1, 20.0F);
		final CustomComponent form_vSpacer = new CustomComponent();
		form_vSpacer.setSizeFull();
		this.form.addComponent(form_vSpacer, 0, 3, 1, 3);
		this.form.setRowExpandRatio(3, 1.0F);
		this.cmdNewLine.setSizeUndefined();
		this.horizontalLayout2.addComponent(this.cmdNewLine);
		this.horizontalLayout2.setComponentAlignment(this.cmdNewLine, Alignment.MIDDLE_CENTER);
		this.cmdEditLine.setSizeUndefined();
		this.horizontalLayout2.addComponent(this.cmdEditLine);
		this.horizontalLayout2.setComponentAlignment(this.cmdEditLine, Alignment.MIDDLE_CENTER);
		this.cmdDeleteLine.setSizeUndefined();
		this.horizontalLayout2.addComponent(this.cmdDeleteLine);
		this.horizontalLayout2.setComponentAlignment(this.cmdDeleteLine, Alignment.MIDDLE_CENTER);
		final CustomComponent horizontalLayout2_spacer = new CustomComponent();
		horizontalLayout2_spacer.setSizeFull();
		this.horizontalLayout2.addComponent(horizontalLayout2_spacer);
		this.horizontalLayout2.setExpandRatio(horizontalLayout2_spacer, 1.0F);
		this.form.setSizeFull();
		this.verticalLayout2.addComponent(this.form);
		this.verticalLayout2.setExpandRatio(this.form, 10.0F);
		this.horizontalLayout2.setWidth(100, Unit.PERCENTAGE);
		this.horizontalLayout2.setHeight(-1, Unit.PIXELS);
		this.verticalLayout2.addComponent(this.horizontalLayout2);
		this.verticalLayout2.setComponentAlignment(this.horizontalLayout2, Alignment.MIDDLE_LEFT);
		this.tableLine.setSizeFull();
		this.verticalLayout2.addComponent(this.tableLine);
		this.verticalLayout2.setComponentAlignment(this.tableLine, Alignment.BOTTOM_CENTER);
		this.verticalLayout2.setExpandRatio(this.tableLine, 10.0F);
		this.verticalLayout2.setSizeFull();
		this.panel.setContent(this.verticalLayout2);
		this.panel.setSizeFull();
		this.tabSheet.addTab(this.panel, null, null);
		this.tabSheet.setSelectedTab(this.panel);
		this.cmdSave.setSizeUndefined();
		this.horizontalLayout.addComponent(this.cmdSave);
		this.horizontalLayout.setComponentAlignment(this.cmdSave, Alignment.MIDDLE_CENTER);
		this.cmdReset.setSizeUndefined();
		this.horizontalLayout.addComponent(this.cmdReset);
		this.horizontalLayout.setComponentAlignment(this.cmdReset, Alignment.MIDDLE_CENTER);
		this.gridLayout.setColumns(1);
		this.gridLayout.setRows(2);
		this.tabSheet.setSizeFull();
		this.gridLayout.addComponent(this.tabSheet, 0, 0);
		this.horizontalLayout.setSizeUndefined();
		this.gridLayout.addComponent(this.horizontalLayout, 0, 1);
		this.gridLayout.setComponentAlignment(this.horizontalLayout, Alignment.BOTTOM_CENTER);
		this.gridLayout.setColumnExpandRatio(0, 10.0F);
		this.gridLayout.setRowExpandRatio(0, 10.0F);
		this.verticalLayout.setSizeFull();
		this.horizontalSplitPanel.setFirstComponent(this.verticalLayout);
		this.gridLayout.setSizeFull();
		this.horizontalSplitPanel.setSecondComponent(this.gridLayout);
		this.horizontalSplitPanel.setSizeFull();
		this.setContent(this.horizontalSplitPanel);
		this.setSizeFull();
	
		this.cmdNew.addClickListener(event -> this.cmdNew_buttonClick(event));
		this.cmdDelete.addClickListener(event -> this.cmdDelete_buttonClick(event));
		this.cmdReload.addClickListener(event -> this.cmdReload_buttonClick(event));
		this.cmdInfo.addClickListener(event -> this.cmdInfo_buttonClick(event));
		this.table.addValueChangeListener(event -> this.table_valueChange(event));
		this.tabSheet.addSelectedTabChangeListener(event -> this.tabSheet_selectedTabChange(event));
		this.cmdNewLine.addClickListener(event -> this.cmdNewLine_buttonClick(event));
		this.cmdEditLine.addClickListener(event -> this.cmdEditLine_buttonClick(event));
		this.cmdDeleteLine.addClickListener(event -> this.cmdDeleteLine_buttonClick(event));
		this.tableLine.addItemClickListener(event -> this.tableLine_itemClick(event));
		this.cmdSave.addClickListener(event -> this.cmdSave_buttonClick(event));
		this.cmdReset.addClickListener(event -> this.cmdReset_buttonClick(event));
	} // </generated-code>


	// <generated-code name="variables">
	private XdevButton cmdNew, cmdDelete, cmdReload, cmdInfo, cmdNewLine, cmdEditLine, cmdDeleteLine, cmdSave, cmdReset;
	private XdevLabel lblSleName, lblSleDescription, lblSleState;
	private XdevFieldGroup<SalaryCalculation> fieldGroup;
	private XdevTable<SalaryCalculation> table;
	private XdevTable<SalaryCalculationLine> tableLine;
	private XdevTabSheet tabSheet;
	private XdevPanel panel;
	private XdevGridLayout gridLayout, form;
	private XdevHorizontalSplitPanel horizontalSplitPanel;
	private XdevContainerFilterComponent containerFilterComponent;
	private XdevHorizontalLayout actionLayout, horizontalLayout2, horizontalLayout;
	private XdevComboBox<?> comboBox;
	private XdevTextField txtSleName, txtSleDescription;
	private XdevVerticalLayout verticalLayout, verticalLayout2;
	// </generated-code>


}
