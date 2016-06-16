/**
 * 
 */
package com.project.portfolio.integration.service;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.project.portfolio.domain.ProjectPortfolio;

/**
 * @author rbollu
 * 
 * This Interface for managing the Project Portfolios.
 */
public interface ProjectPortfolioService {
	
	ProjectPortfolio getProjectPortfolio(long id);
	ProjectPortfolio save(ProjectPortfolio projectPortfolio);
	ProjectPortfolio deleteProjectPortfolio(long id);
	ProjectPortfolio processPortfolio(XSSFWorkbook workbook) throws Exception;
	public boolean writeToWorkBook(ProjectPortfolio projectPortfolio, XSSFWorkbook portflioWorkBook);
	
	

}
