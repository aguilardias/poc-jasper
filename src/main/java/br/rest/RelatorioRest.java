package br.rest;

import java.io.InputStream;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRSaver;

@RestController
@RequestMapping("relatorio")
public class RelatorioRest {

	@GetMapping(value = "pessoas")
	public ResponseEntity<String> pessoas() {
		try {
			InputStream employeeReportStream = getClass().getResourceAsStream("/relatorios/pessoas.jrxml");
			JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);

			JRSaver.saveObject(jasperReport, "pessoas.jasper");

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ResponseEntity.ok("ok");

	}

}
