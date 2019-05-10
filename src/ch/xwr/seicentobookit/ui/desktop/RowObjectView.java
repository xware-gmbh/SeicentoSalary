package ch.xwr.seicentobookit.ui.desktop;

import com.vaadin.data.util.filter.Compare;
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
import com.xdev.ui.XdevLabel;
import com.xdev.ui.XdevPanel;
import com.xdev.ui.XdevPopupDateField;
import com.xdev.ui.XdevTabSheet;
import com.xdev.ui.XdevTextField;
import com.xdev.ui.XdevVerticalLayout;
import com.xdev.ui.XdevVerticalSplitPanel;
import com.xdev.ui.XdevView;
import com.xdev.ui.entitycomponent.combobox.XdevComboBox;
import com.xdev.ui.entitycomponent.table.XdevTable;
import com.xdev.ui.util.NestedProperty;

import ch.xwr.seicentobookit.business.ConfirmDialog;
import ch.xwr.seicentobookit.business.RowObjectManager;
import ch.xwr.seicentobookit.dal.DatabaseVersionDAO;
import ch.xwr.seicentobookit.dal.RowEntityDAO;
import ch.xwr.seicentobookit.dal.RowFileDAO;
import ch.xwr.seicentobookit.dal.RowObjectDAO;
import ch.xwr.seicentobookit.dal.RowTextDAO;
import ch.xwr.seicentobookit.entities.DatabaseVersion;
import ch.xwr.seicentobookit.entities.DatabaseVersion_;
import ch.xwr.seicentobookit.entities.Language_;
import ch.xwr.seicentobookit.entities.RowEntity;
import ch.xwr.seicentobookit.entities.RowEntity_;
import ch.xwr.seicentobookit.entities.RowFile;
import ch.xwr.seicentobookit.entities.RowFile_;
import ch.xwr.seicentobookit.entities.RowObject;
import ch.xwr.seicentobookit.entities.RowObject_;
import ch.xwr.seicentobookit.entities.RowText;
import ch.xwr.seicentobookit.entities.RowText_;

public class RowObjectView extends XdevView {

	/**
	 * 
	 */
	public RowObjectView() {
		super();
		this.initUI();		
	}

	/**
	 * 
	 */
	public RowObjectView(long rowid, String entName) {
		super();
		this.initUI();
			
		loadUI(rowid, entName);
	}
	
	private void loadUI(long rowid, String entName) {		
		RowObjectDAO objDao = new RowObjectDAO();
		RowObject obj = objDao.getObjectBase(entName, rowid);
		
		if (obj == null) {
			Notification.show("Objektstamm", "Kein Record gefunden!", Notification.Type.WARNING_MESSAGE);
		}
		
		this.fieldGroup.setItemDataSource(obj);
		
		//2nd ObjeText
		tableText.getBeanContainerDataSource().addContainerFilter(new Compare.Equal("rowObject", obj));
		tableText.getBeanContainerDataSource().refresh();
		
		tableRowFile.getBeanContainerDataSource().addContainerFilter(new Compare.Equal("rowObject", obj));
		tableRowFile.getBeanContainerDataSource().refresh();		
	}

	/**
	 * Event handler delegate method for the {@link XdevButton}
	 * {@link #cmdReset}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdReset_buttonClick(Button.ClickEvent event) {
		((Window) this.getParent()).close();
	}

	private void popupRowText() {
		Window win = new Window();
		win.setWidth("670");
		win.setHeight("400");		
		win.center();
		win.setModal(true);		
		win.setContent(new RowTextPopup());
		
		win.addCloseListener(new CloseListener() {			
			@Override
			public void windowClose(CloseEvent e) {
				//Dummy for reloading
				tableText.refreshRowCache();
				tableText.sort();
				tableText.getBeanContainerDataSource().refresh();				
			}
		});
		this.getUI().addWindow(win);
	}

	private void popupRowFile() {
		Window win = new Window();
		win.setWidth("670");
		win.setHeight("550");		
		win.center();
		win.setModal(true);		
		win.setContent(new RowFilePopup());
		
		win.addCloseListener(new CloseListener() {			
			@Override
			public void windowClose(CloseEvent e) {
				//Dummy for reloading
				tableRowFile.refreshRowCache();
				tableRowFile.sort();
				tableRowFile.getBeanContainerDataSource().refresh();				
			}
		});
		this.getUI().addWindow(win);
	}
	
	/**
	 * Event handler delegate method for the {@link XdevButton}
	 * {@link #cmdNewText}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdNewText_buttonClick(Button.ClickEvent event) {
		Long beanId = null;
		Long objId = fieldGroup.getItemDataSource().getBean().getObjId();
		
		UI.getCurrent().getSession().setAttribute("beanId", beanId);
		UI.getCurrent().getSession().setAttribute("objId", objId);
	
		popupRowText();		
	}

	/**
	 * Event handler delegate method for the {@link XdevButton}
	 * {@link #cmdUpdateText}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdUpdateText_buttonClick(Button.ClickEvent event) {
		if (tableText.getSelectedItem() == null) return;
		
		Long beanId = tableText.getSelectedItem().getBean().getTxtId();
		Long objId = null;
		
		UI.getCurrent().getSession().setAttribute("beanId", beanId);
		UI.getCurrent().getSession().setAttribute("objId", objId);
	
		popupRowText();		
	
	}

	/**
	 * Event handler delegate method for the {@link XdevButton}
	 * {@link #cmdDeleteText}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdDeleteText_buttonClick(Button.ClickEvent event) {
		if (tableText.getSelectedItem() == null) {
			Notification.show("Datensatz löschen", "Es wurde keine Zeile selektiert in der Tabelle", Notification.Type.WARNING_MESSAGE);			
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
				RowText bean = tableText.getSelectedItem().getBean();
				//Delete Record
				RowObjectManager man = new RowObjectManager();
				man.deleteObject(bean.getTxtId(), bean.getClass().getSimpleName());

				RowTextDAO dao = new RowTextDAO();
				dao.remove(bean);
				tableText.getBeanContainerDataSource().refresh();
				
				if (!tableText.isEmpty() ) {
					//tableText.select(tableText.getCurrentPageFirstItemId());
				}
				Notification.show("Datensatz löschen", "Datensatz wurde gelöscht!", Notification.Type.TRAY_NOTIFICATION);			
			}
			
		});		
		
	}

	/**
	 * Event handler delegate method for the {@link XdevButton}
	 * {@link #cmdNewFile}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdNewFile_buttonClick(Button.ClickEvent event) {
		Long beanId = null;
		Long objId = fieldGroup.getItemDataSource().getBean().getObjId();
		
		UI.getCurrent().getSession().setAttribute("beanId", beanId);
		UI.getCurrent().getSession().setAttribute("objId", objId);
	
		popupRowFile();			
	}

	/**
	 * Event handler delegate method for the {@link XdevButton}
	 * {@link #cmdUpdateFile}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdUpdateFile_buttonClick(Button.ClickEvent event) {
		if (tableRowFile.getSelectedItem() == null) return;
		
		Long beanId = tableRowFile.getSelectedItem().getBean().getRimId();		
		Long objId = null;
		
		UI.getCurrent().getSession().setAttribute("beanId", beanId);
		UI.getCurrent().getSession().setAttribute("objId", objId);
	
		popupRowFile();				
	}

	/**
	 * Event handler delegate method for the {@link XdevButton}
	 * {@link #cmdDeleteFile}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdDeleteFile_buttonClick(Button.ClickEvent event) {
		XdevTable<RowFile> tab = tableRowFile;
		if (tab.getSelectedItem() == null) {
			Notification.show("Datensatz löschen", "Es wurde keine Zeile selektiert in der Tabelle", Notification.Type.WARNING_MESSAGE);			
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
				RowFile bean = tab.getSelectedItem().getBean();
				//Delete Record
				RowObjectManager man = new RowObjectManager();
				man.deleteObject(bean.getRimId(), bean.getClass().getSimpleName());

				RowFileDAO dao = new RowFileDAO();
				dao.remove(bean);
				tab.getBeanContainerDataSource().refresh();
				
				//if (!tab.isEmpty()) tab.select(tab.getCurrentPageFirstItemId());
				Notification.show("Datensatz löschen", "Datensatz wurde gelöscht!", Notification.Type.TRAY_NOTIFICATION);							
			}
			
		});		
				
	}

	/*
	 * WARNING: Do NOT edit!<br>The content of this method is always regenerated by
	 * the UI designer.
	 */
	// <generated-code name="initUI">
	private void initUI() {
		this.verticalSplitPanel = new XdevVerticalSplitPanel();
		this.panel = new XdevPanel();
		this.form = new XdevGridLayout();
		this.label12 = new XdevLabel();
		this.textFieldId = new XdevTextField();
		this.label6 = new XdevLabel();
		this.popupDateField = new XdevPopupDateField();
		this.label10 = new XdevLabel();
		this.popupDateField3 = new XdevPopupDateField();
		this.label2 = new XdevLabel();
		this.comboBox2 = new XdevComboBox<>();
		this.label7 = new XdevLabel();
		this.textField4 = new XdevTextField();
		this.label11 = new XdevLabel();
		this.textField6 = new XdevTextField();
		this.label3 = new XdevLabel();
		this.textField = new XdevTextField();
		this.label8 = new XdevLabel();
		this.popupDateField2 = new XdevPopupDateField();
		this.label = new XdevLabel();
		this.comboBox = new XdevComboBox<>();
		this.label4 = new XdevLabel();
		this.textField2 = new XdevTextField();
		this.label9 = new XdevLabel();
		this.textField5 = new XdevTextField();
		this.label5 = new XdevLabel();
		this.textField3 = new XdevTextField();
		this.horizontalLayout = new XdevHorizontalLayout();
		this.cmdReset = new XdevButton();
		this.fieldGroup = new XdevFieldGroup<>(RowObject.class);
		this.tabSheet = new XdevTabSheet();
		this.verticalLayoutText = new XdevVerticalLayout();
		this.horizontalLayout2 = new XdevHorizontalLayout();
		this.cmdNewText = new XdevButton();
		this.cmdDeleteText = new XdevButton();
		this.cmdUpdateText = new XdevButton();
		this.tableText = new XdevTable<>();
		this.verticalLayoutFile = new XdevVerticalLayout();
		this.actionLayout = new XdevHorizontalLayout();
		this.cmdNewFile = new XdevButton();
		this.cmdDeleteFile = new XdevButton();
		this.cmdUpdateFile = new XdevButton();
		this.tableRowFile = new XdevTable<>();
	
		this.setCaption("Objektstamm");
		this.verticalSplitPanel.setSplitPosition(33.0F, Unit.PERCENTAGE);
		this.form.setMargin(new MarginInfo(true, true, false, true));
		this.label12.setValue(StringResourceUtils.optLocalizeString("{$label12.value}", this));
		this.label6.setValue(StringResourceUtils.optLocalizeString("{$label6.value}", this));
		this.label10.setValue(StringResourceUtils.optLocalizeString("{$label10.value}", this));
		this.label2.setValue(StringResourceUtils.optLocalizeString("{$label2.value}", this));
		this.comboBox2.setContainerDataSource(RowEntity.class, DAOs.get(RowEntityDAO.class).findAll());
		this.comboBox2.setItemCaptionPropertyId(RowEntity_.entName.getName());
		this.label7.setValue(StringResourceUtils.optLocalizeString("{$label7.value}", this));
		this.label11.setValue(StringResourceUtils.optLocalizeString("{$label11.value}", this));
		this.label3.setValue(StringResourceUtils.optLocalizeString("{$label3.value}", this));
		this.label8.setValue(StringResourceUtils.optLocalizeString("{$label8.value}", this));
		this.label.setValue(StringResourceUtils.optLocalizeString("{$label.value}", this));
		this.comboBox.setContainerDataSource(DatabaseVersion.class, DAOs.get(DatabaseVersionDAO.class).findAll());
		this.comboBox.setItemCaptionPropertyId(DatabaseVersion_.dbvMicro.getName());
		this.label4.setValue(StringResourceUtils.optLocalizeString("{$label4.value}", this));
		this.label9.setValue(StringResourceUtils.optLocalizeString("{$label9.value}", this));
		this.label5.setValue(StringResourceUtils.optLocalizeString("{$label5.value}", this));
		this.horizontalLayout.setMargin(new MarginInfo(false));
		this.cmdReset.setCaption(StringResourceUtils.optLocalizeString("{$cmdReset.caption}", this));
		this.fieldGroup.setReadOnly(true);
		this.fieldGroup.bind(this.comboBox, RowObject_.databaseVersion.getName());
		this.fieldGroup.bind(this.comboBox2, RowObject_.rowEntity.getName());
		this.fieldGroup.bind(this.textField, RowObject_.objRowId.getName());
		this.fieldGroup.bind(this.textField2, RowObject_.objChngcnt.getName());
		this.fieldGroup.bind(this.textField3, RowObject_.objState.getName());
		this.fieldGroup.bind(this.popupDateField, RowObject_.objAdded.getName());
		this.fieldGroup.bind(this.textField4, RowObject_.objAddedBy.getName());
		this.fieldGroup.bind(this.popupDateField2, RowObject_.objChanged.getName());
		this.fieldGroup.bind(this.textField5, RowObject_.objChangedBy.getName());
		this.fieldGroup.bind(this.popupDateField3, RowObject_.objDeleted.getName());
		this.fieldGroup.bind(this.textFieldId, RowObject_.objId.getName());
		this.fieldGroup.bind(this.textField6, RowObject_.objDeletedBy.getName());
		this.tabSheet.setStyleName("framed");
		this.horizontalLayout2.setSpacing(false);
		this.horizontalLayout2.setMargin(new MarginInfo(false, true, false, false));
		this.cmdNewText
				.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/new1_16.png"));
		this.cmdNewText.setCaption("New");
		this.cmdNewText.setStyleName("icon-only");
		this.cmdDeleteText
				.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/delete3_16.png"));
		this.cmdDeleteText.setCaption("Delete");
		this.cmdDeleteText.setStyleName("icon-only");
		this.cmdUpdateText
				.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/edit1.png"));
		this.cmdUpdateText.setCaption("Update");
		this.cmdUpdateText.setStyleName("icon-only");
		this.tableText.setContainerDataSource(RowText.class, NestedProperty.of(RowText_.language, Language_.lngName));
		this.tableText.setVisibleColumns(RowText_.txtNumber.getName(), RowText_.txtFreetext.getName(),
				NestedProperty.path(RowText_.language, Language_.lngName), RowText_.txtState.getName());
		this.tableText.setColumnHeader("txtNumber", "Nummer");
		this.tableText.setColumnWidth("txtNumber", 33);
		this.tableText.setColumnHeader("txtFreetext", "Text");
		this.tableText.setColumnHeader("language.lngName", "Sprache");
		this.tableText.setColumnHeader("txtState", "Status");
		this.actionLayout.setSpacing(false);
		this.actionLayout.setMargin(new MarginInfo(false, true, false, false));
		this.cmdNewFile
				.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/new1_16.png"));
		this.cmdNewFile.setCaption("New");
		this.cmdNewFile.setStyleName("icon-only");
		this.cmdDeleteFile
				.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/delete3_16.png"));
		this.cmdDeleteFile.setCaption("Delete");
		this.cmdDeleteFile.setStyleName("icon-only");
		this.cmdUpdateFile
				.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/edit1.png"));
		this.cmdUpdateFile.setCaption("Bearbeiten");
		this.cmdUpdateFile.setStyleName("icon-only");
		this.tableRowFile.setContainerDataSource(RowFile.class);
		this.tableRowFile.addGeneratedColumn("generated", new FunctionUpDownloadRowFile.Generator());
		this.tableRowFile.setVisibleColumns(RowFile_.rimNumber.getName(), RowFile_.rimName.getName(),
				RowFile_.rimType.getName(), RowFile_.rimSize.getName(), RowFile_.rimState.getName(), "generated");
		this.tableRowFile.setColumnHeader("rimNumber", "Nummer");
		this.tableRowFile.setColumnAlignment("rimNumber", Table.Align.RIGHT);
		this.tableRowFile.setColumnWidth("rimNumber", 33);
		this.tableRowFile.setColumnHeader("rimName", "Name");
		this.tableRowFile.setColumnHeader("rimType", "Typ");
		this.tableRowFile.setColumnHeader("rimSize", "Grösse");
		this.tableRowFile.setColumnHeader("rimState", "Status");
		this.tableRowFile.setColumnHeader("generated", "...");
	
		this.cmdReset.setSizeUndefined();
		this.horizontalLayout.addComponent(this.cmdReset);
		this.horizontalLayout.setComponentAlignment(this.cmdReset, Alignment.MIDDLE_LEFT);
		final CustomComponent horizontalLayout_spacer = new CustomComponent();
		horizontalLayout_spacer.setSizeFull();
		this.horizontalLayout.addComponent(horizontalLayout_spacer);
		this.horizontalLayout.setExpandRatio(horizontalLayout_spacer, 1.0F);
		this.form.setColumns(6);
		this.form.setRows(6);
		this.label12.setSizeUndefined();
		this.form.addComponent(this.label12, 0, 0);
		this.textFieldId.setWidth(100, Unit.PERCENTAGE);
		this.textFieldId.setHeight(-1, Unit.PIXELS);
		this.form.addComponent(this.textFieldId, 1, 0);
		this.label6.setSizeUndefined();
		this.form.addComponent(this.label6, 2, 0);
		this.popupDateField.setWidth(100, Unit.PERCENTAGE);
		this.popupDateField.setHeight(-1, Unit.PIXELS);
		this.form.addComponent(this.popupDateField, 3, 0);
		this.label10.setSizeUndefined();
		this.form.addComponent(this.label10, 4, 0);
		this.popupDateField3.setWidth(100, Unit.PERCENTAGE);
		this.popupDateField3.setHeight(-1, Unit.PIXELS);
		this.form.addComponent(this.popupDateField3, 5, 0);
		this.label2.setSizeUndefined();
		this.form.addComponent(this.label2, 0, 1);
		this.comboBox2.setWidth(100, Unit.PERCENTAGE);
		this.comboBox2.setHeight(-1, Unit.PIXELS);
		this.form.addComponent(this.comboBox2, 1, 1);
		this.label7.setSizeUndefined();
		this.form.addComponent(this.label7, 2, 1);
		this.textField4.setWidth(100, Unit.PERCENTAGE);
		this.textField4.setHeight(-1, Unit.PIXELS);
		this.form.addComponent(this.textField4, 3, 1);
		this.label11.setSizeUndefined();
		this.form.addComponent(this.label11, 4, 1);
		this.textField6.setWidth(100, Unit.PERCENTAGE);
		this.textField6.setHeight(-1, Unit.PIXELS);
		this.form.addComponent(this.textField6, 5, 1);
		this.label3.setSizeUndefined();
		this.form.addComponent(this.label3, 0, 2);
		this.textField.setWidth(100, Unit.PERCENTAGE);
		this.textField.setHeight(-1, Unit.PIXELS);
		this.form.addComponent(this.textField, 1, 2);
		this.label8.setSizeUndefined();
		this.form.addComponent(this.label8, 2, 2);
		this.popupDateField2.setWidth(100, Unit.PERCENTAGE);
		this.popupDateField2.setHeight(-1, Unit.PIXELS);
		this.form.addComponent(this.popupDateField2, 3, 2);
		this.label.setSizeUndefined();
		this.form.addComponent(this.label, 4, 2);
		this.comboBox.setWidth(100, Unit.PERCENTAGE);
		this.comboBox.setHeight(-1, Unit.PIXELS);
		this.form.addComponent(this.comboBox, 5, 2);
		this.label4.setSizeUndefined();
		this.form.addComponent(this.label4, 0, 3);
		this.textField2.setWidth(100, Unit.PERCENTAGE);
		this.textField2.setHeight(-1, Unit.PIXELS);
		this.form.addComponent(this.textField2, 1, 3);
		this.label9.setSizeUndefined();
		this.form.addComponent(this.label9, 2, 3);
		this.textField5.setWidth(100, Unit.PERCENTAGE);
		this.textField5.setHeight(-1, Unit.PIXELS);
		this.form.addComponent(this.textField5, 3, 3);
		this.label5.setSizeUndefined();
		this.form.addComponent(this.label5, 4, 3);
		this.textField3.setWidth(100, Unit.PERCENTAGE);
		this.textField3.setHeight(-1, Unit.PIXELS);
		this.form.addComponent(this.textField3, 5, 3);
		this.horizontalLayout.setWidth(100, Unit.PERCENTAGE);
		this.horizontalLayout.setHeight(-1, Unit.PIXELS);
		this.form.addComponent(this.horizontalLayout, 0, 4, 5, 4);
		this.form.setColumnExpandRatio(1, 100.0F);
		this.form.setColumnExpandRatio(3, 100.0F);
		this.form.setColumnExpandRatio(5, 100.0F);
		final CustomComponent form_vSpacer = new CustomComponent();
		form_vSpacer.setSizeFull();
		this.form.addComponent(form_vSpacer, 0, 5, 5, 5);
		this.form.setRowExpandRatio(5, 1.0F);
		this.form.setSizeFull();
		this.panel.setContent(this.form);
		this.cmdNewText.setSizeUndefined();
		this.horizontalLayout2.addComponent(this.cmdNewText);
		this.horizontalLayout2.setComponentAlignment(this.cmdNewText, Alignment.MIDDLE_CENTER);
		this.cmdDeleteText.setSizeUndefined();
		this.horizontalLayout2.addComponent(this.cmdDeleteText);
		this.horizontalLayout2.setComponentAlignment(this.cmdDeleteText, Alignment.MIDDLE_CENTER);
		this.cmdUpdateText.setSizeUndefined();
		this.horizontalLayout2.addComponent(this.cmdUpdateText);
		this.horizontalLayout2.setComponentAlignment(this.cmdUpdateText, Alignment.MIDDLE_CENTER);
		final CustomComponent horizontalLayout2_spacer = new CustomComponent();
		horizontalLayout2_spacer.setSizeFull();
		this.horizontalLayout2.addComponent(horizontalLayout2_spacer);
		this.horizontalLayout2.setExpandRatio(horizontalLayout2_spacer, 1.0F);
		this.horizontalLayout2.setWidth(100, Unit.PERCENTAGE);
		this.horizontalLayout2.setHeight(-1, Unit.PIXELS);
		this.verticalLayoutText.addComponent(this.horizontalLayout2);
		this.verticalLayoutText.setComponentAlignment(this.horizontalLayout2, Alignment.MIDDLE_CENTER);
		this.tableText.setSizeFull();
		this.verticalLayoutText.addComponent(this.tableText);
		this.verticalLayoutText.setComponentAlignment(this.tableText, Alignment.MIDDLE_CENTER);
		this.verticalLayoutText.setExpandRatio(this.tableText, 100.0F);
		this.cmdNewFile.setSizeUndefined();
		this.actionLayout.addComponent(this.cmdNewFile);
		this.actionLayout.setComponentAlignment(this.cmdNewFile, Alignment.MIDDLE_LEFT);
		this.cmdDeleteFile.setSizeUndefined();
		this.actionLayout.addComponent(this.cmdDeleteFile);
		this.actionLayout.setComponentAlignment(this.cmdDeleteFile, Alignment.MIDDLE_CENTER);
		this.cmdUpdateFile.setSizeUndefined();
		this.actionLayout.addComponent(this.cmdUpdateFile);
		this.actionLayout.setComponentAlignment(this.cmdUpdateFile, Alignment.MIDDLE_CENTER);
		this.actionLayout.setSizeUndefined();
		this.verticalLayoutFile.addComponent(this.actionLayout);
		this.verticalLayoutFile.setComponentAlignment(this.actionLayout, Alignment.MIDDLE_LEFT);
		this.tableRowFile.setSizeFull();
		this.verticalLayoutFile.addComponent(this.tableRowFile);
		this.verticalLayoutFile.setComponentAlignment(this.tableRowFile, Alignment.MIDDLE_CENTER);
		this.verticalLayoutFile.setExpandRatio(this.tableRowFile, 100.0F);
		this.verticalLayoutText.setSizeFull();
		this.tabSheet.addTab(this.verticalLayoutText, "Text", null);
		this.verticalLayoutFile.setSizeFull();
		this.tabSheet.addTab(this.verticalLayoutFile, "Dateien", null);
		this.tabSheet.setSelectedTab(this.verticalLayoutFile);
		this.panel.setSizeFull();
		this.verticalSplitPanel.setFirstComponent(this.panel);
		this.tabSheet.setSizeFull();
		this.verticalSplitPanel.setSecondComponent(this.tabSheet);
		this.verticalSplitPanel.setSizeFull();
		this.setContent(this.verticalSplitPanel);
		this.setSizeFull();
	
		this.cmdReset.addClickListener(event -> this.cmdReset_buttonClick(event));
		this.cmdNewText.addClickListener(event -> this.cmdNewText_buttonClick(event));
		this.cmdDeleteText.addClickListener(event -> this.cmdDeleteText_buttonClick(event));
		this.cmdUpdateText.addClickListener(event -> this.cmdUpdateText_buttonClick(event));
		this.cmdNewFile.addClickListener(event -> this.cmdNewFile_buttonClick(event));
		this.cmdDeleteFile.addClickListener(event -> this.cmdDeleteFile_buttonClick(event));
		this.cmdUpdateFile.addClickListener(event -> this.cmdUpdateFile_buttonClick(event));
	} // </generated-code>

	// <generated-code name="variables">
	private XdevLabel label12, label6, label10, label2, label7, label11, label3, label8, label, label4, label9,
			label5;
	private XdevComboBox<DatabaseVersion> comboBox;
	private XdevButton cmdReset, cmdNewText, cmdUpdateText, cmdDeleteText, cmdNewFile, cmdUpdateFile, cmdDeleteFile;
	private XdevComboBox<RowEntity> comboBox2;
	private XdevPanel panel;
	private XdevTabSheet tabSheet;
	private XdevGridLayout form;
	private XdevTable<RowFile> tableRowFile;
	private XdevVerticalSplitPanel verticalSplitPanel;
	private XdevHorizontalLayout horizontalLayout, horizontalLayout2, actionLayout;
	private XdevPopupDateField popupDateField, popupDateField3, popupDateField2;
	private XdevFieldGroup<RowObject> fieldGroup;
	private XdevTextField textFieldId, textField4, textField6, textField, textField2, textField5, textField3;
	private XdevVerticalLayout verticalLayoutText, verticalLayoutFile;
	private XdevTable<RowText> tableText; // </generated-code>


}
