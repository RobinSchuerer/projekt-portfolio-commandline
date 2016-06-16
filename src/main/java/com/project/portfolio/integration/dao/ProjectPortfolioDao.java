/**
 * 
 */
package com.project.portfolio.integration.dao;

import com.project.portfolio.domain.ProjectPortfolio;

/**
 * @author rbollu
 * 
 * This interface for managing the project portfolio's data.
 */
public interface ProjectPortfolioDao {
	
	ProjectPortfolio getProjectPortfolio(long id);
	ProjectPortfolio save(ProjectPortfolio projectPortfolio);
	ProjectPortfolio deleteProjectPortfolio(long id);

}
