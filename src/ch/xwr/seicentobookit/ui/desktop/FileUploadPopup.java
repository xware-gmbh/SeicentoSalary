package ch.xwr.seicentobookit.ui.desktop;

import com.vaadin.ui.CustomComponent;
import com.xdev.ui.XdevGridLayout;
import com.xdev.ui.XdevUpload;
import com.xdev.ui.XdevView;

public class FileUploadPopup extends XdevView {

	/**
	 * 
	 */
	public FileUploadPopup() {
		super();
		this.initUI();
	}

	/*
	 * WARNING: Do NOT edit!<br>The content of this method is always regenerated
	 * by the UI designer.
	 */
	// <generated-code name="initUI">
	private void initUI() {
		this.gridLayout = new XdevGridLayout();
		this.upload = new XdevUpload();
	
		this.upload.setCaption("Datei");
	
		this.gridLayout.setColumns(2);
		this.gridLayout.setRows(2);
		this.upload.setWidth(421, Unit.PIXELS);
		this.upload.setHeight(72, Unit.PIXELS);
		this.gridLayout.addComponent(this.upload, 0, 0);
		CustomComponent gridLayout_hSpacer = new CustomComponent();
		gridLayout_hSpacer.setSizeFull();
		this.gridLayout.addComponent(gridLayout_hSpacer, 1, 0, 1, 0);
		this.gridLayout.setColumnExpandRatio(1, 1.0F);
		CustomComponent gridLayout_vSpacer = new CustomComponent();
		gridLayout_vSpacer.setSizeFull();
		this.gridLayout.addComponent(gridLayout_vSpacer, 0, 1, 0, 1);
		this.gridLayout.setRowExpandRatio(1, 1.0F);
		this.gridLayout.setSizeFull();
		this.setContent(this.gridLayout);
		this.setSizeFull();
	} // </generated-code>

	// <generated-code name="variables">
	private XdevUpload upload;
	private XdevGridLayout gridLayout; // </generated-code>


}
