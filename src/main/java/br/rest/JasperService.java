package br.rest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@Component
public class JasperService {

	public byte[] gerarRelatorioPdfParaStream(String caminhoJasper, Map<String, Object> params, Collection<?> dados) throws JRException {

		JasperPrint jasperPrint = gerarRelatorio(caminhoJasper, params, dados);
		
//		return JasperExportManager.exportReportToPdf(jasperPrint);
		JRPdfExporter exporter = new JRPdfExporter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
		 
		SimplePdfReportConfiguration reportConfig
		  = new SimplePdfReportConfiguration();
		reportConfig.setSizePageToContent(true);
		reportConfig.setForceLineBreakPolicy(false);
		 
		SimplePdfExporterConfiguration exportConfig
		  = new SimplePdfExporterConfiguration();
		exportConfig.setMetadataAuthor("SERPRO");
		exportConfig.setEncrypted(true);
		//exportConfig.setAllowedPermissionsHint("PRINTING");
		 
		//exporter.setConfiguration(reportConfig);
		exporter.setConfiguration(exportConfig);
		
		// exporta o relatório para 'baos'
		exporter.exportReport();
		
		return baos.toByteArray();
	}
	
	public byte[] gerarRelatorioXlsParaStream(String caminhoJasper,
			Map<String, Object> params, List<Usuario> dados) throws JRException {
		
		JasperPrint jasperPrint = gerarRelatorio(caminhoJasper, params, dados);
		
		JRXlsxExporter exporter = new JRXlsxExporter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
		
		SimpleXlsxReportConfiguration reportConfig
		  = new SimpleXlsxReportConfiguration();
		reportConfig.setSheetNames(new String[] { "Employee Data" });
		 
		exporter.setConfiguration(reportConfig);
		exporter.exportReport();
		
		return baos.toByteArray();
	}
	
	public void gerarRelatorioPdfParaArquivo(String caminnhoJasper, Map<String, Object> params, Collection<?> dados) throws JRException {
		JasperPrint jasperPrint = gerarRelatorio(caminnhoJasper, params, dados);
		
		// exportar para arquivo
		JasperExportManager.exportReportToPdfFile(jasperPrint, "pessoas.pdf");
	}

	private JasperPrint gerarRelatorio(String caminhoJasper, Map<String, Object> params, Collection<?> dados) throws JRException {
		InputStream jasperStream = getClass().getResourceAsStream(caminhoJasper);
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dados);
		return JasperFillManager.fillReport(jasperReport, params, dataSource);
		
	}
}
