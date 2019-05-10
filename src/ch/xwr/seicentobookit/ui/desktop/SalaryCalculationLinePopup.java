package ch.xwr.seicentobookit.ui.desktop;

import java.util.Collection;
import java.util.Iterator;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.xdev.ui.XdevButton;
import com.xdev.ui.XdevFieldGroup;
import com.xdev.ui.XdevGridLayout;
import com.xdev.ui.XdevHorizontalLayout;
import com.xdev.ui.XdevLabel;
import com.xdev.ui.XdevPopupDateField;
import com.xdev.ui.XdevTextField;
import com.xdev.ui.XdevView;

import ch.xwr.seicentobookit.business.LovState;
import ch.xwr.seicentobookit.business.RowObjectManager;
import ch.xwr.seicentobookit.dal.SalaryCalculationDAO;
import ch.xwr.seicentobookit.dal.SalaryCalculationLineDAO;
import ch.xwr.seicentobookit.entities.SalaryCalculation;
import ch.xwr.seicentobookit.entities.SalaryCalculationLine;
import ch.xwr.seicentobookit.entities.SalaryCalculationLine_;

public class SalaryCalculationLinePopup extends XdevView {

	/**
	 * 
	 */
	public SalaryCalculationLinePopup() {
		super();
		this.initUI();
		
		this.comboBoxState.addItems((Object[]) LovState.State.values());
		// this.comboBoxWorktype.addItems((Object[])LovState.WorkType.values());

		// get Parameter
		final Long beanId = (Long) UI.getCurrent().getSession().getAttribute("beanId");
		final Long objId = (Long) UI.getCurrent().getSession().getAttribute("objId");
		SalaryCalculationLine bean = null;
		SalaryCalculation obj = null;

		if (beanId == null) {
			// new
			final SalaryCalculationDAO objDao = new SalaryCalculationDAO();
			obj = objDao.find(objId);

			bean = new SalaryCalculationLine();
			bean.setSalaryCalculation(obj);
			bean.setSlxState(LovState.State.active);

		} else {
			final SalaryCalculationLineDAO dao = new SalaryCalculationLineDAO();
			bean = dao.find(beanId.longValue());
		}

		setBeanGui(bean);
		//setROFields();
		
	}

	private void setBeanGui(SalaryCalculationLine bean) {
		// set Bean + Fields
		this.fieldGroup.setItemDataSource(bean);
				
	}

	public static Window getPopupWindow() {
		final Window win = new Window();
		win.setWidth("900");
		win.setHeight("480");
		win.center();
		win.setModal(true);
		win.setContent(new SalaryCalculationLinePopup());

		return win;
	}
	
	/**
	 * Event handler delegate method for the {@link XdevButton} {@link #cmdClose}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdClose_buttonClick(final Button.ClickEvent event) {
		UI.getCurrent().getSession().setAttribute(String.class, "cmdCancel");
		this.fieldGroup.discard();
		((Window) this.getParent()).close();	
	}

	/**
	 * Event handler delegate method for the {@link XdevButton} {@link #cmdSave}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdSave_buttonClick(final Button.ClickEvent event) {
		UI.getCurrent().getSession().setAttribute(String.class, "cmdSave");

		try {
			if (!this.fieldGroup.isValid()){
				final Collection<Field<?>> c2 = this.fieldGroup.getFields();
				for (final Iterator<Field<?>> iterator = c2.iterator(); iterator.hasNext();) {
					final Field<?> object = iterator.next();
					final String name = (String) this.fieldGroup.getPropertyId(object);
					try {
						object.validate();
					} catch (final InvalidValueException e) {
						Notification.show("Ungültiger Wert in Feld " + name, "Message" + e.getMessage(), Notification.Type.ERROR_MESSAGE);
						e.printStackTrace();
					} catch (final Exception e) {
						Notification.show("Fehler beim Speichern " + object.getCaption(), e.getMessage(), Notification.Type.ERROR_MESSAGE);
					}
				}

				return;
			}
			this.fieldGroup.save();

			final RowObjectManager man = new RowObjectManager();
			man.updateObject(this.fieldGroup.getItemDataSource().getBean().getSlxId(),
					this.fieldGroup.getItemDataSource().getBean().getClass().getSimpleName());

			((Window) this.getParent()).close();
			Notification.show("Save clicked", "Daten wurden gespeichert", Notification.Type.TRAY_NOTIFICATION);

		} catch (final Exception e) {
			Notification.show("Fehler beim Speichern", e.getMessage(), Notification.Type.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	/*
	 * WARNING: Do NOT edit!<br>The content of this method is always regenerated by
	 * the UI designer.
	 */
	// <generated-code name="initUI">
	private void initUI() {
		this.form = new XdevGridLayout();
		this.lblSlxValidFrom = new XdevLabel();
		this.dateSlxValidFrom = new XdevPopupDateField();
		this.lblSlxFactorAhv = new XdevLabel();
		this.txtSlxFactorAhv = new XdevTextField();
		this.lblSlxCoordinationAlv = new XdevLabel();
		this.txtSlxCoordinationAlv = new XdevTextField();
		this.lblSlxFactorAlv = new XdevLabel();
		this.txtSlxFactorAlv = new XdevTextField();
		this.lblSlxFactorKtg = new XdevLabel();
		this.txtSlxFactorKtg = new XdevTextField();
		this.lblSlxSldUpperBoundry = new XdevLabel();
		this.txtSlxSldUpperBoundry = new XdevTextField();
		this.lblSlxFactorFak = new XdevLabel();
		this.txtSlxFactorFak = new XdevTextField();
		this.lblSlxSldLowerBoundry = new XdevLabel();
		this.txtSlxSldLowerBoundry = new XdevTextField();
		this.lblSlxFactorAdmin = new XdevLabel();
		this.txtSlxFactorAdmin = new XdevTextField();
		this.lblSlxFactorSol = new XdevLabel();
		this.txtSlxFactorSol = new XdevTextField();
		this.lblSlxState = new XdevLabel();
		this.comboBoxState = new ComboBox();
		this.horizontalLayout = new XdevHorizontalLayout();
		this.cmdSave = new XdevButton();
		this.cmdClose = new XdevButton();
		this.fieldGroup = new XdevFieldGroup<>(SalaryCalculationLine.class);
	
		this.lblSlxValidFrom.setValue("Gültigab");
		this.dateSlxValidFrom.setTabIndex(1);
		this.lblSlxFactorAhv.setValue("AHV Beitrag");
		this.txtSlxFactorAhv.setTabIndex(2);
		this.lblSlxCoordinationAlv.setValue("Koordinationslohn ALV");
		this.txtSlxCoordinationAlv.setTabIndex(8);
		this.lblSlxFactorAlv.setValue("ALV Beitrag");
		this.txtSlxFactorAlv.setTabIndex(3);
		this.lblSlxFactorKtg.setValue("KTG Beitrag");
		this.txtSlxFactorKtg.setTabIndex(4);
		this.lblSlxSldUpperBoundry.setValue("Obergrenze SOL");
		this.txtSlxSldUpperBoundry.setTabIndex(9);
		this.lblSlxFactorFak.setValue("FAK Beitrag");
		this.txtSlxFactorFak.setTabIndex(5);
		this.lblSlxSldLowerBoundry.setValue("Untergrenze SOL");
		this.txtSlxSldLowerBoundry.setTabIndex(10);
		this.lblSlxFactorAdmin.setValue("Verwaltungskosten");
		this.txtSlxFactorAdmin.setTabIndex(6);
		this.lblSlxFactorSol.setValue("Solidaritätsbeitrag");
		this.txtSlxFactorSol.setTabIndex(7);
		this.lblSlxState.setValue("Status");
		this.comboBoxState.setTabIndex(11);
		this.horizontalLayout.setMargin(new MarginInfo(false, false, false, true));
		this.cmdSave.setIcon(FontAwesome.SAVE);
		this.cmdSave.setCaption("Speichern");
		this.cmdSave.setTabIndex(13);
		this.cmdSave.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		this.cmdClose.setIcon(FontAwesome.CLOSE);
		this.cmdClose.setCaption("Schliessen");
		this.cmdClose.setTabIndex(12);
		this.cmdClose.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
		this.fieldGroup.bind(this.dateSlxValidFrom, SalaryCalculationLine_.slxValidFrom.getName());
		this.fieldGroup.bind(this.txtSlxFactorAhv, SalaryCalculationLine_.slxFactorAhv.getName());
		this.fieldGroup.bind(this.txtSlxFactorAlv, SalaryCalculationLine_.slxFactorAlv.getName());
		this.fieldGroup.bind(this.txtSlxFactorKtg, SalaryCalculationLine_.slxFactorKtg.getName());
		this.fieldGroup.bind(this.txtSlxFactorFak, SalaryCalculationLine_.slxFactorFak.getName());
		this.fieldGroup.bind(this.txtSlxFactorAdmin, SalaryCalculationLine_.slxFactorAdmin.getName());
		this.fieldGroup.bind(this.txtSlxFactorSol, SalaryCalculationLine_.slxFactorSol.getName());
		this.fieldGroup.bind(this.txtSlxCoordinationAlv, SalaryCalculationLine_.slxCoordinationAlv.getName());
		this.fieldGroup.bind(this.txtSlxSldLowerBoundry, SalaryCalculationLine_.slxSldLowerBoundry.getName());
		this.fieldGroup.bind(this.txtSlxSldUpperBoundry, SalaryCalculationLine_.slxSldUpperBoundry.getName());
		this.fieldGroup.bind(this.comboBoxState, SalaryCalculationLine_.slxState.getName());
	
		this.cmdSave.setSizeUndefined();
		this.horizontalLayout.addComponent(this.cmdSave);
		this.horizontalLayout.setComponentAlignment(this.cmdSave, Alignment.MIDDLE_RIGHT);
		this.cmdClose.setSizeUndefined();
		this.horizontalLayout.addComponent(this.cmdClose);
		this.horizontalLayout.setComponentAlignment(this.cmdClose, Alignment.MIDDLE_RIGHT);
		final CustomComponent horizontalLayout_spacer = new CustomComponent();
		horizontalLayout_spacer.setSizeFull();
		this.horizontalLayout.addComponent(horizontalLayout_spacer);
		this.horizontalLayout.setExpandRatio(horizontalLayout_spacer, 1.0F);
		this.form.setColumns(4);
		this.form.setRows(10);
		this.lblSlxValidFrom.setSizeUndefined();
		this.form.addComponent(this.lblSlxValidFrom, 0, 0);
		this.dateSlxValidFrom.setSizeUndefined();
		this.form.addComponent(this.dateSlxValidFrom, 1, 0);
		this.lblSlxFactorAhv.setSizeUndefined();
		this.form.addComponent(this.lblSlxFactorAhv, 0, 1);
		this.txtSlxFactorAhv.setSizeUndefined();
		this.form.addComponent(this.txtSlxFactorAhv, 1, 1);
		this.lblSlxCoordinationAlv.setSizeUndefined();
		this.form.addComponent(this.lblSlxCoordinationAlv, 2, 1);
		this.txtSlxCoordinationAlv.setSizeUndefined();
		this.form.addComponent(this.txtSlxCoordinationAlv, 3, 1);
		this.lblSlxFactorAlv.setSizeUndefined();
		this.form.addComponent(this.lblSlxFactorAlv, 0, 2);
		this.txtSlxFactorAlv.setSizeUndefined();
		this.form.addComponent(this.txtSlxFactorAlv, 1, 2);
		this.lblSlxFactorKtg.setSizeUndefined();
		this.form.addComponent(this.lblSlxFactorKtg, 0, 3);
		this.txtSlxFactorKtg.setSizeUndefined();
		this.form.addComponent(this.txtSlxFactorKtg, 1, 3);
		this.lblSlxSldUpperBoundry.setSizeUndefined();
		this.form.addComponent(this.lblSlxSldUpperBoundry, 2, 3);
		this.txtSlxSldUpperBoundry.setSizeUndefined();
		this.form.addComponent(this.txtSlxSldUpperBoundry, 3, 3);
		this.lblSlxFactorFak.setSizeUndefined();
		this.form.addComponent(this.lblSlxFactorFak, 0, 4);
		this.txtSlxFactorFak.setSizeUndefined();
		this.form.addComponent(this.txtSlxFactorFak, 1, 4);
		this.lblSlxSldLowerBoundry.setSizeUndefined();
		this.form.addComponent(this.lblSlxSldLowerBoundry, 2, 4);
		this.txtSlxSldLowerBoundry.setSizeUndefined();
		this.form.addComponent(this.txtSlxSldLowerBoundry, 3, 4);
		this.lblSlxFactorAdmin.setSizeUndefined();
		this.form.addComponent(this.lblSlxFactorAdmin, 0, 5);
		this.txtSlxFactorAdmin.setSizeUndefined();
		this.form.addComponent(this.txtSlxFactorAdmin, 1, 5);
		this.lblSlxFactorSol.setSizeUndefined();
		this.form.addComponent(this.lblSlxFactorSol, 0, 6);
		this.txtSlxFactorSol.setSizeUndefined();
		this.form.addComponent(this.txtSlxFactorSol, 1, 6);
		this.lblSlxState.setSizeUndefined();
		this.form.addComponent(this.lblSlxState, 2, 7);
		this.comboBoxState.setSizeUndefined();
		this.form.addComponent(this.comboBoxState, 3, 7);
		this.horizontalLayout.setWidth(100, Unit.PERCENTAGE);
		this.horizontalLayout.setHeight(-1, Unit.PIXELS);
		this.form.addComponent(this.horizontalLayout, 1, 8);
		this.form.setComponentAlignment(this.horizontalLayout, Alignment.TOP_RIGHT);
		this.form.setColumnExpandRatio(1, 100.0F);
		this.form.setColumnExpandRatio(3, 100.0F);
		final CustomComponent form_vSpacer = new CustomComponent();
		form_vSpacer.setSizeFull();
		this.form.addComponent(form_vSpacer, 0, 9, 3, 9);
		this.form.setRowExpandRatio(9, 1.0F);
		this.form.setSizeFull();
		this.setContent(this.form);
		this.setSizeFull();
	
		this.cmdSave.addClickListener(event -> this.cmdSave_buttonClick(event));
		this.cmdClose.addClickListener(event -> this.cmdClose_buttonClick(event));
	} // </generated-code>

	// <generated-code name="variables">
	private XdevLabel lblSlxValidFrom, lblSlxFactorAhv, lblSlxCoordinationAlv, lblSlxFactorAlv, lblSlxFactorKtg,
			lblSlxSldUpperBoundry, lblSlxFactorFak, lblSlxSldLowerBoundry, lblSlxFactorAdmin, lblSlxFactorSol, lblSlxState;
	private XdevButton cmdClose, cmdSave;
	private XdevHorizontalLayout horizontalLayout;
	private XdevPopupDateField dateSlxValidFrom;
	private ComboBox comboBoxState;
	private XdevFieldGroup<SalaryCalculationLine> fieldGroup;
	private XdevGridLayout form;
	private XdevTextField txtSlxFactorAhv, txtSlxCoordinationAlv, txtSlxFactorAlv, txtSlxFactorKtg, txtSlxSldUpperBoundry,
			txtSlxFactorFak, txtSlxSldLowerBoundry, txtSlxFactorAdmin, txtSlxFactorSol;
	// </generated-code>

}