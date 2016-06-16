package com.project.portfolio.domain;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;


/**
 * The persistent class for the team_effort database table.
 * 
 */
@Entity
@Table(name="team_effort")
@NamedQuery(name="TeamEffort.findAll", query="SELECT t FROM TeamEffort t")
public class TeamEffort implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="team_effort_id")
	private int teamEffortId;

	@Column(name="team_effort")
	private double teamEffortForProject;

	//bi-directional many-to-one association to Project
	@ManyToOne
	@JoinColumn(name="project_id")
	@JsonBackReference
	private Project project;

	//bi-directional many-to-one association to Team
	@ManyToOne
	@JoinColumn(name="team_with_id")
	@JsonBackReference
	private Team team;

	public TeamEffort() {
	}

	public int getTeamEffortId() {
		return this.teamEffortId;
	}

	public void setTeamEffortId(int teamEffortId) {
		this.teamEffortId = teamEffortId;
	}

	public double getTeamEffortForProject() {
		return this.teamEffortForProject;
	}

	public void setTeamEffortForProject(double teamEffortForProject) {
		this.teamEffortForProject = teamEffortForProject;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Team getTeam() {
		return this.team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

}