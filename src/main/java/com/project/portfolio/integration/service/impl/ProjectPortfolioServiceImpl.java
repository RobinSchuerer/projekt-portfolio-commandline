package com.project.portfolio.integration.service.impl;

import javax.inject.Inject;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.project.portfolio.ExcelManager;
import com.project.portfolio.domain.ProjectPortfolio;
import com.project.portfolio.integration.dao.ProjectPortfolioDao;
import com.project.portfolio.integration.service.ProjectPortfolioService;

/**
 * @author rbollu
 *
 * This service provides the implementation of how Project Portfolio's are managed.
 */
@Service
public class ProjectPortfolioServiceImpl implements ProjectPortfolioService {
	
	@Inject ProjectPortfolioDao portfolioDao;
	
	@Override
	public ProjectPortfolio getProjectPortfolio(long id) {
		
		return null;
	}

	@Override
	public ProjectPortfolio save(ProjectPortfolio projectPortfolio) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProjectPortfolio deleteProjectPortfolio(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProjectPortfolio processPortfolio(XSSFWorkbook portflioWorkBook) throws Exception {
		ProjectPortfolio portfolio = new ExcelManager().processProjectPortfolio(portflioWorkBook);
		return portfolio;
	}

	@Override
	public boolean writeToWorkBook(ProjectPortfolio projectPortfolio,
			XSSFWorkbook portflioWorkBook) {
		return  new ExcelManager().writeToWorkBook(projectPortfolio, portflioWorkBook);
	}

}
