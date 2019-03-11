package br.rest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

@RestController
@RequestMapping("relatorio")
public class RelatorioRest {

	@GetMapping(value = "pessoas")
	public ResponseEntity<byte[]> pessoas() {
		try {
			// caminho jasper
			String caminhoJasper = "/relatorios/compilados/pessoas.jasper";

			String nomeArquivoExportado = "pessoas.pdf";
			String tipoArquivo = "application/pdf";
//			String nomeArquivoExportado = "pessoas.xls";
//			String tipoArquivo = "application/octet-stream";
			
//			String tipoExportacao = "attachment";
			String tipoExportacao = "inline";

			// parâmetros
			Map<String, Object> params = new HashMap<>();
			params.put("Parameter1", "teste 11");
			
			// data source
			Usuario usuario1 = new Usuario("user1", new Date(),null);
			Usuario usuario2 = new Usuario("user2", new Date(),null);
			List<Usuario> dados = Arrays.asList(usuario1, usuario2);
			
			byte[] bytes = gerarRelatorioPdfParaStream(caminhoJasper, params, dados);
//			byte[] bytes = gerarRelatorioXlsParaStream(caminhoJasper, params, dados);
//			gerarRelatorioParaArquivo(caminhoJasper, params, dados);
			
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_TYPE, tipoArquivo+"; charset=UTF-8");
			headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("%s; filename=\"%s\"", tipoExportacao, nomeArquivoExportado));
			
			return ResponseEntity.ok()
					.headers(headers)
					.body(bytes);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	@GetMapping(value = "subrelatorio")
	public ResponseEntity<byte[]> subrelatorio() {
		try {
			// caminho jasper
			String caminhoJasper = "/relatorios/compilados/subrelatorio.jasper";

			String nomeArquivoExportado = "subrelatorio.pdf";
			String tipoArquivo = "application/pdf";
			
			String tipoExportacao = "inline";
			
			// parâmetros
			Map<String, Object> params = new HashMap<>();
			params.put("subreportDir", "relatorios/compilados/");

			// data source
			Usuario usuario1 = new Usuario("user1", new Date(),Arrays.asList("f1","f2"));
			Usuario usuario2 = new Usuario("user2", new Date(),Arrays.asList("f1"));
			List<Usuario> dados = Arrays.asList(usuario1, usuario2);
			
			byte[] bytes = gerarRelatorioPdfParaStream(caminhoJasper, params, dados);
			
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_TYPE, tipoArquivo+"; charset=UTF-8");
			headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("%s; filename=\"%s\"", tipoExportacao, nomeArquivoExportado));
			
			return ResponseEntity.ok()
					.headers(headers)
					.body(bytes);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private byte[] gerarRelatorioXlsParaStream(String caminhoJasper,
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

	private byte[] gerarRelatorioPdfParaStream(String caminhoJasper, Map<String, Object> params, Collection<?> dados) throws JRException {

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
	
	private void gerarRelatorioPdfParaArquivo(String caminnhoJasper, Map<String, Object> params, Collection<?> dados) throws JRException {
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
