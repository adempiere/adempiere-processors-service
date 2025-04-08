package org.spin.processor.server;

import org.compiere.jr.report.JRViewerProvider;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

public class ServerReportProvider implements JRViewerProvider {

	@Override
	public void openViewer(JasperPrint jasperPrint, String title) throws JRException {
		//	Nothing here
	}

}
