package com.project.portfolio.integration.dao.jpa;

import javax.inject.Inject;

import org.springframework.stereotype.Repository;

import com.project.portfolio.domain.ProjectPortfolio;
import com.project.portfolio.integration.dao.ProjectPortfolioDao;

/**
 * @author rbollu
 * 
 * This service provides the implementation of how project portfolio's data being managed.
 */

@Repository
public class ProjectPortfolioJpaDao implements ProjectPortfolioDao {
	
	
	@Override
	public ProjectPortfolio getProjectPortfolio(long id) {
		// TODO Auto-generated method stub
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

}
