package com.project.portfolio.domain;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the project_team database table.
 * 
 */
@Entity
@Table(name="project_team")
@NamedQuery(name="ProjectTeam.findAll", query="SELECT p FROM ProjectTeam p")
public class ProjectTeam implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="project_to_team_id")
	private int projectToTeamId;

	@Column(name="portfolio_project_id")
	private int portfolioProjectId;

	@Column(name="portfolio_team_id")
	private int portfolioTeamId;
	
		public ProjectTeam() {
	}

	public int getProjectToTeamId() {
		return this.projectToTeamId;
	}

	public void setProjectToTeamId(int projectToTeamId) {
		this.projectToTeamId = projectToTeamId;
	}

	public int getPortfolioProjectId() {
		return this.portfolioProjectId;
	}

	public void setPortfolioProjectId(int portfolioProjectId) {
		this.portfolioProjectId = portfolioProjectId;
	}

	public int getPortfolioTeamId() {
		return this.portfolioTeamId;
	}

	public void setPortfolioTeamId(int portfolioTeamId) {
		this.portfolioTeamId = portfolioTeamId;
	}
}