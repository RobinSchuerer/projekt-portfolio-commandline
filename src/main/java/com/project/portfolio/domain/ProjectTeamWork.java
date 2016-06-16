package com.project.portfolio.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;


/**
 * The persistent class for the project_team_work database table.
 * 
 */
@Entity
@Table(name="project_team_work")
@NamedQuery(name="ProjectTeamWork.findAll", query="SELECT p FROM ProjectTeamWork p")
public class ProjectTeamWork implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="project_team_work_id")
	private int projectTeamWorkId;

	private BigDecimal effort = new BigDecimal("0.0");

	//uni-directional many-to-one association to Project
	@ManyToOne
	@JoinColumn(name="team_work_project_id")
	@JsonBackReference
	private Project project;
	
	private String projectName;
	
	private String teamName;
	
	private LocalDate month;
	
	public ProjectTeamWork(BigDecimal effort, String projectName,
			String teamName, LocalDate month) {
		super();
		this.effort = effort;
		this.projectName = projectName;
		this.teamName = teamName;
		this.month = month;
	}
	
	//bi-directional many-to-one association to TeamBucket
	@ManyToOne
	@JoinColumn(name="team_bucket_id")
	@JsonBackReference
	private TeamBucket teamBucket;

	public ProjectTeamWork() {
	}

	public int getProjectTeamWorkId() {
		return this.projectTeamWorkId;
	}

	public void setProjectTeamWorkId(int projectTeamWorkId) {
		this.projectTeamWorkId = projectTeamWorkId;
	}

	public BigDecimal getEffort() {
		return this.effort;
	}

	public void setEffort(BigDecimal effort) {
		this.effort = effort;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public TeamBucket getTeamBucket() {
		return this.teamBucket;
	}

	public void setTeamBucket(TeamBucket teamBucket) {
		this.teamBucket = teamBucket;
	}
	
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	
	public LocalDate getMonth() {
		return month;
	}

	public void setMonth(LocalDate month) {
		this.month = month;
	}



}