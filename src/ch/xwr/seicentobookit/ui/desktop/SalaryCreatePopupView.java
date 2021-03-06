package ch.xwr.seicentobookit.ui.desktop;

import java.util.Date;

import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.xdev.ui.XdevAbsoluteLayout;
import com.xdev.ui.XdevButton;
import com.xdev.ui.XdevCheckBox;
import com.xdev.ui.XdevLabel;
import com.xdev.ui.XdevPopupDateField;
import com.xdev.ui.XdevView;

import ch.xwr.seicentobookit.business.CreateSalary;

public class SalaryCreatePopupView extends XdevView {

	/**
	 * 
	 */
	public SalaryCreatePopupView() {
		super();
		this.initUI();

		popupDateField.setValue(new Date());

		// String ret = UI.getCurrent().getSession().getAttribute(String.class);
	}

	/**
	 * Event handler delegate method for the {@link XdevButton}
	 * {@link #btnCreate}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void btnCreate_buttonClick(Button.ClickEvent event) {
		Date dat = (Date) popupDateField.getConvertedValue();

		if (dat != null) {
			boolean cpLastValue = cbxLastSalary.getValue().booleanValue();
			CreateSalary cs = new CreateSalary(dat);

			try {
				cs.createRecords(cpLastValue);
				Notification.show("Löhne für Mitarbeiter erstellt per " + dat.toString(),
						Notification.Type.TRAY_NOTIFICATION);
			} catch (Exception e) {
				Notification.show("Achtung bei der Generierung sind Probleme aufgetreten. Bitte kontrollieren.",
						Notification.Type.WARNING_MESSAGE);

			}

			UI.getCurrent().getSession().setAttribute(String.class, "state");

			((Window) this.getParent()).close();
		}
	}
	
	public static Window getPopupWindow() {
		final Window win = new Window();
		win.setWidth("560");
		win.setHeight("250");
		win.center();
		win.setModal(true);
		win.setContent(new SalaryCreatePopupView());
		
		return win;
	}
	

	/**
	 * Event handler delegate method for the {@link XdevButton}
	 * {@link #btnCancel}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void btnCancel_buttonClick(Button.ClickEvent event) {
		((Window) this.getParent()).close();
	}

	/*
	 * WARNING: Do NOT edit!<br>The content of this method is always regenerated by
	 * the UI designer.
	 */
	// <generated-code name="initUI">
	private void initUI() {
		this.absoluteLayout = new XdevAbsoluteLayout();
		this.label = new XdevLabel();
		this.popupDateField = new XdevPopupDateField();
		this.btnCreate = new XdevButton();
		this.btnCancel = new XdevButton();
		this.label2 = new XdevLabel();
		this.label3 = new XdevLabel();
		this.cbxLastSalary = new XdevCheckBox();
	
		this.label.setValue("Datum neue Lohnzahlung");
		this.popupDateField.setRequired(true);
		this.btnCreate.setCaption("Erstellen");
		this.btnCancel.setCaption("Abbrechen");
		this.label2.setStyleName("bold");
		this.label2.setValue("Lohndaten generieren für aktive Mitarbeiter");
		this.label3.setValue("Bruttolohn von letztem Lohn übernehmen");
		this.cbxLastSalary.setValue(true);
	
		this.absoluteLayout.addComponent(this.label, "left:10px; top:68px");
		this.absoluteLayout.addComponent(this.popupDateField, "left:356px; top:60px");
		this.absoluteLayout.addComponent(this.btnCreate, "left:87px; top:141px");
		this.absoluteLayout.addComponent(this.btnCancel, "left:212px; top:141px");
		this.label2.setWidth(359, Unit.PIXELS);
		this.label2.setHeight(21, Unit.PIXELS);
		this.absoluteLayout.addComponent(this.label2, "left:10px; top:20px");
		this.absoluteLayout.addComponent(this.label3, "left:10px; top:99px");
		this.absoluteLayout.addComponent(this.cbxLastSalary, "left:358px; top:104px");
		this.absoluteLayout.setSizeFull();
		this.setContent(this.absoluteLayout);
		this.setSizeFull();
	
		this.btnCreate.addClickListener(event -> this.btnCreate_buttonClick(event));
		this.btnCancel.addClickListener(event -> this.btnCancel_buttonClick(event));
	} // </generated-code>

	// <generated-code name="variables">
	private XdevLabel label, label2, label3;
	private XdevButton btnCreate, btnCancel;
	private XdevAbsoluteLayout absoluteLayout;
	private XdevPopupDateField popupDateField;
	private XdevCheckBox cbxLastSalary;
	// </generated-code>

}
