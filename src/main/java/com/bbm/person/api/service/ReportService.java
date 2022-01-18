package com.bbm.person.api.service;

import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class ReportService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public byte[] gerarRelatorio(String nomeRelatorio, Map<String, Object> params, ServletContext context) throws Exception {
		
		//Obter conexao com a base de dados
		Connection connection = jdbcTemplate.getDataSource().getConnection();
		
		//Carregar o Caminho do file jasper
		String caminho = context.getRealPath("reports") + File.separator + nomeRelatorio + ".jasper";
		
		//Gerar relatorio com dados e impressao
		JasperPrint print = JasperFillManager.fillReport(caminho, params, connection);
		
		//Exporta para byte o pdf para realizar o download		
		byte[] retorno = JasperExportManager.exportReportToPdf(print);
		
		//Fecha a conexao
		connection.close();
		
		return retorno;
	}
}
