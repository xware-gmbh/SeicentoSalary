
package ch.xwr.seicentobookit.ui.desktop;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.server.ClientConnector;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Window;
import com.xdev.res.ApplicationResource;
import com.xdev.security.authentication.ui.XdevAuthenticationNavigator;
import com.xdev.server.aa.openid.auth.AzureUser;
import com.xdev.server.aa.openid.helper.DiscoveryHelper;
import com.xdev.ui.XdevHorizontalLayout;
import com.xdev.ui.XdevImage;
import com.xdev.ui.XdevLabel;
import com.xdev.ui.XdevMenuBar;
import com.xdev.ui.XdevMenuBar.XdevMenuItem;
import com.xdev.ui.XdevTabSheet;
import com.xdev.ui.XdevUI;
import com.xdev.ui.XdevVerticalLayout;
import com.xdev.ui.XdevView;

import ch.xwr.seicentobookit.business.helper.AzureHelper;
import ch.xwr.seicentobookit.dal.CompanyDAO;
import ch.xwr.seicentobookit.entities.Company;

@Push
@Theme("reindeer")
public class DesktopUI extends XdevUI {
	/** Logger initialized */
	private static final Logger LOG = LoggerFactory.getLogger(DesktopUI.class);
	
	private AzureUser currentUser;
	
	public DesktopUI() {
		super();
		
		try {			
			DiscoveryHelper.performDiscovery(VaadinServlet.getCurrent().getServletContext());
		} catch (final Throwable e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(VaadinRequest request) {
		setLocale();
		this.initUI();
		checkLocalDevEnv();
		setCallBackUri();
		loadMyData();		
	}

	//only for local testing with preview
	private void checkLocalDevEnv() {
		if (isLocalDevEnv()) {
			LOG.info("Local DEV Environment.... enable Menues");

			//Eclipse Preview (does not have Path in Jetty)
			enableMenu(true);

			//this.currentUser = new AzureUser(null);

			this.menuBarRight.setVisible(false);
			this.menuBarRight.setEnabled(false);

		}
	}

	private boolean isLocalDevEnv() {
		final URI loc = this.getPage().getLocation();
		final String path = loc.getPath();

		if (path == null || path.length() < 3)
		 {
			return true;  //Jetty
		}
		if ((loc.getPort() >=8080 && loc.getPort() < 8090) && loc.getHost().equals("localhost")) {  //local tomcat
			return true;
		}

		return false;
	}

	//will be consumed in AuthView
	private void setCallBackUri() {
		final AzureHelper hlp = new AzureHelper();
		hlp.setCallBackUri(this.getPage().getLocation());

	}
	
	
	private void enableMenu(boolean state) {
		menuBar.setVisible(state);
		menuBar.setEnabled(state);
		
		menuBarRight.setVisible(state);
		menuBarRight.setEnabled(state);		
	}

	public void loggedIn(boolean lgin, AzureUser user) {
		currentUser = user;
		if (lgin) {
			LOG.info("User logged in " + user.name());			
			menuItemUser.setCaption(currentUser.name());
			setLocale();
		} else {			
			//getSession().close();   //leads to session expired
			LOG.info("User logged out");			
			menuItemUser.setCaption("");			
		}
		
		enableMenu(lgin);
	}
	
	public AzureUser getUser() {
		return currentUser;
	}
	
	//auf Azure App Service ist diese auf en-US
	private void setLocale() {
		Locale.setDefault(new Locale("de", "CH"));
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Zurich"));
	}

	private void loadMyData() {
		LOG.info("Load company Data...");
		
		CompanyDAO dao = new CompanyDAO();
		Company cmp = dao.getActiveConfig();
		
		lblEnvironment.setValue(dao.getDbNameNativeSQL());
		lblCompany.setValue(cmp.getCmpName());
	}

	/**
	 * Event handler delegate method for the
	 * {@link XdevMenuBar.XdevMenuItem} {@link #menuItem4}.
	 *
	 * @see MenuBar.Command#menuSelected(MenuBar.MenuItem)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void menuItem4_menuSelected(MenuBar.MenuItem selectedItem) {
		loadTab(SalaryTabView.class, selectedItem.getText());
		
		
//		Iterator<Component> itr = tabSheet.iterator();
//		while (itr.hasNext()){
//			Component cmp = itr.next();
//			if (cmp.getClass() == SalaryTabView.class){
//				tabSheet.setSelectedTab(cmp);				
//				//break
//				return;
//			}			
//			if (cmp.getClass() == MainView.class){
//				//tabSheet.removeComponent(cmp);
//			}			
//		}
//		
//		SalaryTabView win = new SalaryTabView();
//		TabSheet.Tab tab = tabSheet.addTab(win);
//		tab.setDescription("Mein Tab");
//		tab.setCaption(selectedItem.getText());
//		tab.setClosable(true);
//		tabSheet.setSelectedTab(win);	
	}

	private void loadTab(Class<?> myClass, String desc){		
		Iterator<Component> itr = tabSheet.iterator();
		while (itr.hasNext()){
			Component cmp = itr.next();
			if (cmp.getClass() == myClass){
				tabSheet.setSelectedTab(cmp);				
				//break
				return;
			}			
			if (cmp.getClass() == MainView.class){
				//tabSheet.removeComponent(cmp);
				cmp.setVisible(false);
			}			
			if (cmp.getClass() == AuthView.class){
				//tabSheet.removeComponent(cmp);
				cmp.setVisible(false);
			}			
		}
		
		Constructor<?> cons;
		try {
			cons = myClass.getConstructor();
			XdevView viw = (XdevView) cons.newInstance();
			TabSheet.Tab tab = tabSheet.addTab(viw);
			tab.setDescription("Mein Tab");
			tab.setCaption(desc);
			tab.setClosable(true);
			tabSheet.setSelectedTab(viw);	
		} catch (Exception e) {
			e.printStackTrace();
		}

		//remove dummy tab (would be 1st position)
		Component cmp = tabSheet.iterator().next();
		if (cmp.getClass() == MainView.class){
			tabSheet.removeComponent(cmp);
		}
		
//		Tab cmp = tabSheet.getTab(0);
//		if (cmp.getClass() == MainView.class){
//			tabSheet.removeTab(cmp);
//		}			
		
//		TabSheet.Tab tab = tabSheet.addTab(viw);
//		//tab.setDescription("Mein Tab");
//		tab.setCaption(desc);
//		tab.setClosable(true);
//		tabSheet.setSelectedTab(viw);	
		
	}
	
	/**
	 * Event handler delegate method for the
	 * {@link XdevMenuBar.XdevMenuItem} {@link #menuItem5}.
	 *
	 * @see MenuBar.Command#menuSelected(MenuBar.MenuItem)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void menuItem5_menuSelected(MenuBar.MenuItem selectedItem) {
		loadTab(BankTabView.class, selectedItem.getText());
		
//		Iterator<Component> itr = tabSheet.iterator();
//		while (itr.hasNext()){
//			Component cmp = itr.next();
//			if (cmp.getClass() == BankTabView.class){
//				tabSheet.setSelectedTab(cmp);				
//				//break
//				return;
//			}			
//			if (cmp.getClass() == MainView.class){
//				//tabSheet.removeComponent(cmp);
//				cmp.setVisible(false);
//			}			
//		}
//		
//		BankTabView win = new BankTabView();
//		TabSheet.Tab tab = tabSheet.addTab(win);
//		tab.setDescription("Mein Tab");
//		tab.setCaption(selectedItem.getText());
//		tab.setClosable(true);
//		tabSheet.setSelectedTab(win);	
	
	}


	/**
	 * Event handler delegate method for the {@link XdevVerticalLayout}
	 * {@link #verticalLayout}.
	 *
	 * @see HasComponents.ComponentAttachListener#componentAttachedToContainer(HasComponents.ComponentAttachEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void verticalLayout_componentAttachedToContainer(HasComponents.ComponentAttachEvent event) {
		System.out.println("Event Attach 1....");
//		Company cmp = new Company();
//		CompanyDAO dao = new CompanyDAO();
//		// cmp = dao.find((long) 2);
//		cmp = dao.getActiveConfig();
//	
//		// Set Value
//		lblEnvironment.setCaption(cmp.getCmpRemark());
//		// try {
//		// startOperation();
//		// Query query = session.createQuery("from company where
//		// cmpstate=1");
//		// objects = query.list();
//		// tx.commit();
//		// } catch (HibernateException e) {
//	
	}

	/**
	 * Event handler delegate method for the {@link XdevUI}.
	 *
	 * @see ClientConnector.AttachListener#attach(ClientConnector.AttachEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void this_attach(ClientConnector.AttachEvent event) {
		System.out.println("Event Attach 2....");
//		Company cmp = new Company();
//		CompanyDAO dao = new CompanyDAO();
//		// cmp = dao.find((long) 2);
//		cmp = dao.getActiveConfig();
//	
//		// Set Value
//		lblEnvironment.setCaption(cmp.getCmpRemark());
//		// try {
//		// startOperation();
//		// Query query = session.createQuery("from company where
//		// cmpstate=1");
//		// objects = query.list();
//		// tx.commit();
//		// } catch (HibernateException e) {
	}

	/**
	 * Event handler delegate method for the {@link XdevUI}.
	 *
	 * @see Component.Listener#componentEvent(Component.Event)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void this_componentEvent(Component.Event event) {
	
	}

	/**
	 * Event handler delegate method for the {@link XdevUI}.
	 *
	 * @see HasComponents.ComponentAttachListener#componentAttachedToContainer(HasComponents.ComponentAttachEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void this_componentAttachedToContainer(HasComponents.ComponentAttachEvent event) {
		System.out.println("Event Attach 3....");
//		Company cmp = new Company();
//		CompanyDAO dao = new CompanyDAO();
//		// cmp = dao.find((long) 2);
//		cmp = dao.getActiveConfig();
//	
//		// Set Value
//		lblEnvironment.setCaption(cmp.getCmpRemark());
//		// try {
//		// startOperation();
//		// Query query = session.createQuery("from company where
//		// cmpstate=1");
//		// objects = query.list();
//		// tx.commit();
//		// } catch (HibernateException e) {
	
	}

	/**
	 * Event handler delegate method for the
	 * {@link XdevMenuBar.XdevMenuItem} {@link #menuItem10}.
	 *
	 * @see MenuBar.Command#menuSelected(MenuBar.MenuItem)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void menuItem10_menuSelected(MenuBar.MenuItem selectedItem) {
		loadTab(CompanyTabView.class, selectedItem.getText());
	}

	/**
	 * Event handler delegate method for the
	 * {@link XdevMenuBar.XdevMenuItem} {@link #menuItem7}.
	 *
	 * @see MenuBar.Command#menuSelected(MenuBar.MenuItem)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void menuItem7_menuSelected(MenuBar.MenuItem selectedItem) {
		loadTab(EmployeeTabView.class, selectedItem.getText());	
	}

	/**
	 * Event handler delegate method for the
	 * {@link XdevMenuBar.XdevMenuItem} {@link #menuItem8}.
	 *
	 * @see MenuBar.Command#menuSelected(MenuBar.MenuItem)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void menuItem8_menuSelected(MenuBar.MenuItem selectedItem) {
		loadTab(SalaryCalculationTabView.class, selectedItem.getText());		
	}

	/**
	 * Event handler delegate method for the
	 * {@link XdevMenuBar.XdevMenuItem} {@link #menuItem9}.
	 *
	 * @see MenuBar.Command#menuSelected(MenuBar.MenuItem)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void menuItem9_menuSelected(MenuBar.MenuItem selectedItem) {
		loadTab(SalaryBvgBaseTabView.class, selectedItem.getText());			
	}

	/**
	 * Event handler delegate method for the
	 * {@link XdevMenuBar.XdevMenuItem} {@link #menuItem11}.
	 *
	 * @see MenuBar.Command#menuSelected(MenuBar.MenuItem)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void menuItem11_menuSelected(MenuBar.MenuItem selectedItem) {
		loadTab(RowObjectTabView.class, selectedItem.getText());				
	}

	/**
	 * Event handler delegate method for the
	 * {@link XdevMenuBar.XdevMenuItem} {@link #menuItemLogout}.
	 *
	 * @see MenuBar.Command#menuSelected(MenuBar.MenuItem)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void menuItemLogout_menuSelected(MenuBar.MenuItem selectedItem) {
		tabSheet.removeAllComponents();
		loggedIn(false, null);
		navigator.navigateTo("");
	}

	/**
	 * Event handler delegate method for the {@link XdevMenuBar.XdevMenuItem}
	 * {@link #menuItemUsrInfo}.
	 *
	 * @see MenuBar.Command#menuSelected(MenuBar.MenuItem)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void menuItemUsrInfo_menuSelected(MenuBar.MenuItem selectedItem) {
		Window win = new Window();
		win.setWidth("1020");
		win.setHeight("700");
		win.center();
		win.setModal(true);
	
		// UI.getCurrent().getSession().setAttribute(String.class,
		// bean.getClass().getSimpleName());
		win.setContent(new UserInfoPopup());
		this.getUI().addWindow(win);	
	}

	/*
	 * WARNING: Do NOT edit!<br>The content of this method is always regenerated by
	 * the UI designer.
	 */
	// <generated-code name="initUI">
	private void initUI() {
		this.verticalLayout = new XdevVerticalLayout();
		this.horizontalLayoutTitle = new XdevHorizontalLayout();
		this.imagePrd = new XdevImage();
		this.verticalLayout2 = new XdevVerticalLayout();
		this.lblCompany = new XdevLabel();
		this.lblEnvironment = new XdevLabel();
		this.horizontalLayoutMenue = new XdevHorizontalLayout();
		this.menuBar = new XdevMenuBar();
		this.menuItem = this.menuBar.addItem("Daten", null);
		this.menuItem4 = this.menuItem.addItem("Lohn", null);
		this.menuItem8 = this.menuItem.addItem("Lohnnebenkosten", null);
		this.menuItem9 = this.menuItem.addItem("BVG Kategorien", null);
		this.menuItem6 = this.menuItem.addItem("Hilfstabellen", null);
		this.menuItem7 = this.menuItem6.addItem("Mitarbeiter", null);
		this.menuItem5 = this.menuItem6.addItem("Bank", null);
		this.menuOptions = this.menuBar.addItem("Optionen", null);
		this.menuItem10 = this.menuOptions.addItem("Firma", null);
		this.menuItem11 = this.menuOptions.addItem("Objektstamm", null);
		this.menuBarRight = new XdevMenuBar();
		this.menuItemUser = this.menuBarRight.addItem("Benutzer", null);
		this.menuItemUsrInfo = this.menuItemUser.addItem("Info", null);
		this.menuItemLogout = this.menuItemUser.addItem("Logout", null);
		this.layoutsTab = new XdevVerticalLayout();
		this.tabSheet = new XdevTabSheet();
		this.navigator = new XdevAuthenticationNavigator(this, this.tabSheet);
	
		this.setIcon(FontAwesome.BANK);
		this.verticalLayout.setSpacing(false);
		this.verticalLayout.setMargin(new MarginInfo(false));
		this.horizontalLayoutTitle.setStyleName("bar FontAwesome ThemeIcons");
		this.horizontalLayoutTitle.setSpacing(false);
		this.horizontalLayoutTitle.setMargin(new MarginInfo(false, true, false, false));
		this.imagePrd.setSource(
				new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/seicento_salary.png"));
		this.verticalLayout2.setEnabled(false);
		this.verticalLayout2.setSpacing(false);
		this.verticalLayout2.setMargin(new MarginInfo(false, false, false, true));
		this.lblCompany.setValue("Firma");
		this.lblEnvironment.setStyleName("small");
		this.lblEnvironment.setValue("Josef Muri");
		this.horizontalLayoutMenue.setSpacing(false);
		this.horizontalLayoutMenue.setMargin(new MarginInfo(false));
		this.menuBar.setEnabled(false);
		this.menuBar.setVisible(false);
		this.menuBarRight.setEnabled(false);
		this.menuBarRight.setVisible(false);
		this.menuItemUser
				.setIcon(new ApplicationResource(this.getClass(), "WebContent/WEB-INF/resources/images/User_black_18.png"));
		this.layoutsTab.setStyleName("small");
		this.layoutsTab.setMargin(new MarginInfo(false));
		this.tabSheet.setStyleName("framed");
		this.navigator.setRedirectViewName("home");
		this.navigator.addView("", AuthView.class);
		this.navigator.addView("home", MainView.class);
	
		this.lblCompany.setSizeUndefined();
		this.verticalLayout2.addComponent(this.lblCompany);
		this.verticalLayout2.setComponentAlignment(this.lblCompany, Alignment.BOTTOM_RIGHT);
		this.lblEnvironment.setSizeUndefined();
		this.verticalLayout2.addComponent(this.lblEnvironment);
		this.verticalLayout2.setComponentAlignment(this.lblEnvironment, Alignment.BOTTOM_RIGHT);
		this.imagePrd.setSizeUndefined();
		this.horizontalLayoutTitle.addComponent(this.imagePrd);
		this.horizontalLayoutTitle.setComponentAlignment(this.imagePrd, Alignment.BOTTOM_LEFT);
		this.verticalLayout2.setSizeUndefined();
		this.horizontalLayoutTitle.addComponent(this.verticalLayout2);
		this.horizontalLayoutTitle.setComponentAlignment(this.verticalLayout2, Alignment.MIDDLE_RIGHT);
		this.horizontalLayoutTitle.setExpandRatio(this.verticalLayout2, 10.0F);
		this.menuBar.setWidth(100, Unit.PERCENTAGE);
		this.menuBar.setHeight(-1, Unit.PIXELS);
		this.horizontalLayoutMenue.addComponent(this.menuBar);
		this.horizontalLayoutMenue.setComponentAlignment(this.menuBar, Alignment.MIDDLE_CENTER);
		this.horizontalLayoutMenue.setExpandRatio(this.menuBar, 100.0F);
		this.menuBarRight.setWidth(100, Unit.PERCENTAGE);
		this.menuBarRight.setHeight(-1, Unit.PIXELS);
		this.horizontalLayoutMenue.addComponent(this.menuBarRight);
		this.horizontalLayoutMenue.setComponentAlignment(this.menuBarRight, Alignment.MIDDLE_RIGHT);
		this.horizontalLayoutMenue.setExpandRatio(this.menuBarRight, 10.0F);
		this.tabSheet.setSizeFull();
		this.layoutsTab.addComponent(this.tabSheet);
		this.layoutsTab.setComponentAlignment(this.tabSheet, Alignment.MIDDLE_LEFT);
		this.layoutsTab.setExpandRatio(this.tabSheet, 100.0F);
		this.horizontalLayoutTitle.setWidth(100, Unit.PERCENTAGE);
		this.horizontalLayoutTitle.setHeight(-1, Unit.PIXELS);
		this.verticalLayout.addComponent(this.horizontalLayoutTitle);
		this.horizontalLayoutMenue.setWidth(100, Unit.PERCENTAGE);
		this.horizontalLayoutMenue.setHeight(-1, Unit.PIXELS);
		this.verticalLayout.addComponent(this.horizontalLayoutMenue);
		this.verticalLayout.setComponentAlignment(this.horizontalLayoutMenue, Alignment.TOP_CENTER);
		this.layoutsTab.setWidth(100, Unit.PERCENTAGE);
		this.layoutsTab.setHeight(99, Unit.PERCENTAGE);
		this.verticalLayout.addComponent(this.layoutsTab);
		this.verticalLayout.setComponentAlignment(this.layoutsTab, Alignment.MIDDLE_CENTER);
		this.verticalLayout.setExpandRatio(this.layoutsTab, 10.0F);
		this.verticalLayout.setSizeFull();
		this.setContent(this.verticalLayout);
		this.setSizeFull();
	
		this.addAttachListener(event -> this.this_attach(event));
		this.addComponentAttachListener(event -> this.this_componentAttachedToContainer(event));
		this.addListener((Component.Listener) event -> this.this_componentEvent(event));
		this.verticalLayout.addComponentAttachListener(event -> this.verticalLayout_componentAttachedToContainer(event));
		this.menuItem4.setCommand(selectedItem -> this.menuItem4_menuSelected(selectedItem));
		this.menuItem8.setCommand(selectedItem -> this.menuItem8_menuSelected(selectedItem));
		this.menuItem9.setCommand(selectedItem -> this.menuItem9_menuSelected(selectedItem));
		this.menuItem7.setCommand(selectedItem -> this.menuItem7_menuSelected(selectedItem));
		this.menuItem5.setCommand(selectedItem -> this.menuItem5_menuSelected(selectedItem));
		this.menuItem10.setCommand(selectedItem -> this.menuItem10_menuSelected(selectedItem));
		this.menuItem11.setCommand(selectedItem -> this.menuItem11_menuSelected(selectedItem));
		this.menuItemUsrInfo.setCommand(selectedItem -> this.menuItemUsrInfo_menuSelected(selectedItem));
		this.menuItemLogout.setCommand(selectedItem -> this.menuItemLogout_menuSelected(selectedItem));
	} // </generated-code>

	// <generated-code name="variables">
	private XdevLabel lblCompany, lblEnvironment;
	private XdevMenuBar menuBar, menuBarRight;
	private XdevHorizontalLayout horizontalLayoutTitle, horizontalLayoutMenue;
	private XdevImage imagePrd;
	private XdevMenuItem menuItem, menuItem4, menuItem8, menuItem9, menuItem6, menuItem7, menuItem5, menuOptions,
			menuItem10, menuItem11, menuItemUser, menuItemUsrInfo, menuItemLogout;
	private XdevTabSheet tabSheet;
	private XdevVerticalLayout verticalLayout, verticalLayout2, layoutsTab;
	private XdevAuthenticationNavigator navigator;
	// </generated-code>
	
}