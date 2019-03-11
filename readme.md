JasperReportsViewResolver 
JasperReportsPdfView

https://stackoverflow.com/questions/27532446/how-to-use-jasperreports-with-spring-mvc

# compilar jrxml
InputStream employeeReportStream = getClass().getResourceAsStream("/relatorios/pessoas.jrxml");
JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);
JRSaver.saveObject(jasperReport, "pessoas.jasper");

# data source
JRBeanCollectionDataSource studentDS = new JRBeanCollectionDataSource(studentCollection);
JasperFillManager.fillReport(jasperReport, parameters, studentDS);

<!-- https://mvnrepository.com/artifact/net.sf.jasperreports/jasperreports -->
<dependency>
    <groupId>net.sf.jasperreports</groupId>
    <artifactId>jasperreports</artifactId>
    <version>6.7.1</version>
</dependency>

#IDE
Jaspersoft Studio 6.6.0, 2018-May-30
Utilizar versão 6.5.1, verão 6.6.0, apresenta um bug (https://community.jaspersoft.com/jaspersoft-studio/issues/10956)

# plugin compilar jasperReport
https://mvnrepository.com/artifact/com.alexnederlof/jasperreports-plugin
https://github.com/alexnederlof/Jasper-report-maven-plugin

# TODO
ok-compilar 'jrxml' com maven
ok-exportar para stream/rest
ok-testar JRBeanCollectionDataSource
ok-testar parametros
ok-exportar para arquivo
ok-modelagem classes
ok-exportar arquivo, escolher se deve fazer download ou abrir arquivo
ok-exportar para xls/csv
ok-subreports

