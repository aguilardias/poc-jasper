package br.rest;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@RestController
@RequestMapping("relatorio")
public class RelatorioRest {

	@GetMapping(value = "pessoas")
	public ResponseEntity<byte[]> pessoas() {
		try {
			Map<String, Object> params = new HashMap<>();
//			params.put("nome", "");
			// --
			InputStream employeeReportStream = getClass().getResourceAsStream("/relatorios/pessoas.jrxml");
			JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);

//			JRSaver.saveObject(jasperReport, "pessoas.jasper");

			Usuario usuario1 = new Usuario("user1", new Date());
			Usuario usuario2 = new Usuario("user2", new Date());

			Collection<Usuario> studentCollection = Arrays.asList(usuario1, usuario2);
			JRBeanCollectionDataSource studentDS = new JRBeanCollectionDataSource(studentCollection);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, studentDS);

//			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params);

			// return the PDF in bytes
			byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
			// --

			return ResponseEntity.ok()
					// Specify content type as PDF
					.header("Content-Type", "application/pdf; charset=UTF-8")
					// Tell browser to display PDF if it can
					.header("Content-Disposition", "inline; filename=\"pessoas.pdf\"").body(bytes);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
