package com.project.portfolio.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;


/**
 * The persistent class for the team database table.
 * 
 */
@Entity
@NamedQuery(name="Team.findAll", query="SELECT t FROM Team t")
public class Team implements Comparable<Team>, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="team_id")
	private int teamId;

	private String color;

	private String name;
	
	private int order;

	//bi-directional many-to-one association to ProjectPortfolio
	@ManyToOne
	@JoinColumn(name="team_portfolio_id")
	private ProjectPortfolio projectPortfolio;

	//bi-directional many-to-one association to TeamBucket
	@OneToMany(mappedBy="team", fetch=FetchType.EAGER)
	@JsonManagedReference
	private SortedSet<TeamBucket> teamBuckets = new TreeSet<TeamBucket>();

	//bi-directional many-to-one association to TeamCapacity
	@OneToMany(mappedBy="team", fetch=FetchType.EAGER)
	@JsonManagedReference
	private SortedSet<TeamCapacity> teamCapacities = new TreeSet<TeamCapacity>();

	//bi-directional many-to-one association to TeamEffort
	@OneToMany(mappedBy="team", fetch=FetchType.EAGER)
	@JsonManagedReference
	private Set<TeamEffort> teamEfforts = new HashSet<TeamEffort>(0);
	
	
	@ManyToMany(mappedBy="teams")
	@JsonManagedReference
	private SortedSet<Project> projects = new TreeSet<Project>();

	private boolean completedProjectWork = false;
	
	private TeamBucket missingTeamCapacity = new TeamBucket();
	
	private BigDecimal overflowWork = new BigDecimal("0.0");
	
	private int projectCount = 0;
	
	List<Project> teamEngagedProjects = new ArrayList<Project>();
	
	private Map<String, Boolean> doneWithProject = new HashMap<String, Boolean>();
	
	public Map<String, Boolean> getDoneWithProject() {
		return doneWithProject;
	}

	public void setDoneWithProject(Map<String, Boolean> doneWithProject) {
		this.doneWithProject = doneWithProject;
	}

	public boolean addProject(Project e) {
		this.projectCount ++;
		return projects.add(e);
	}

	public boolean removeProejc(Object o) {
		return projects.remove(o);
	}

	public SortedSet<Project> getProjects() {
		return projects;
	}

	public void setProjects(SortedSet<Project> projects) {
		this.projects = projects;
	}

	public Team() {
	}

	public int getTeamId() {
		return this.teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ProjectPortfolio getProjectPortfolio() {
		return this.projectPortfolio;
	}

	public void setProjectPortfolio(ProjectPortfolio projectPortfolio) {
		this.projectPortfolio = projectPortfolio;
	}

	public SortedSet<TeamBucket> getTeamBuckets() {
		return this.teamBuckets;
	}

	public void setTeamBuckets(SortedSet<TeamBucket> teamBuckets) {
		this.teamBuckets = teamBuckets;
	}

	public TeamBucket addTeamBucket(TeamBucket teamBucket) {
		getTeamBuckets().add(teamBucket);
		teamBucket.setTeam(this);

		return teamBucket;
	}

	public TeamBucket removeTeamBucket(TeamBucket teamBucket) {
		getTeamBuckets().remove(teamBucket);
		teamBucket.setTeam(null);

		return teamBucket;
	}

	public SortedSet<TeamCapacity> getTeamCapacities() {
		return this.teamCapacities;
	}

	public void setTeamCapacities(SortedSet<TeamCapacity> teamCapacities) {
		this.teamCapacities = teamCapacities;
	}

	public TeamCapacity addTeamCapacity(TeamCapacity teamCapacity) {
		getTeamCapacities().add(teamCapacity);
		teamCapacity.setTeam(this);

		return teamCapacity;
	}

	public TeamCapacity removeTeamCapacity(TeamCapacity teamCapacity) {
		getTeamCapacities().remove(teamCapacity);
		teamCapacity.setTeam(null);

		return teamCapacity;
	}

	
	public TeamEffort addTeamEffort(TeamEffort teamEffort) {
		getTeamEfforts().add(teamEffort);
		teamEffort.setTeam(this);
		return teamEffort;
	}

	public TeamEffort removeTeamEffort(TeamEffort teamEffort) {
		getTeamEfforts().remove(teamEffort);
		teamEffort.setTeam(null);
		return teamEffort;
	}

	public Set<TeamEffort> getTeamEfforts() {
		return this.teamEfforts;
	}

	public void setTeamEfforts(Set<TeamEffort> teamEfforts) {
		this.teamEfforts = teamEfforts;
	}

	public TeamEffort addTeamEfforts(TeamEffort teamEffort) {
		getTeamEfforts().add(teamEffort);
		teamEffort.setTeam(this);

		return teamEffort;
	}

	public TeamEffort removeTeamEfforts(TeamEffort teamEfforts) {
		getTeamEfforts().remove(teamEfforts);
		teamEfforts.setTeam(null);

		return teamEfforts;
	}
	
	public boolean hasCompletedProjectWork() {
		return completedProjectWork;
	}

	public void setCompletedProjectWork(boolean completedProjectWork) {
		this.completedProjectWork = completedProjectWork;
	}
	
	public TeamBucket getMissingTeamCapacity() {
		return missingTeamCapacity;
	}

	public void setMissingTeamCapacity(TeamBucket missingTeamCapacity) {
		this.missingTeamCapacity = missingTeamCapacity;
	}
	
	public BigDecimal getOverflowWork() {
		return overflowWork;
	}

	public void setOverflowWork(BigDecimal overflowWork) {
		this.overflowWork = overflowWork;
	}

	@Override
	public int compareTo(Team o) {
		return this.getOrder() - o.getOrder();
	}
	
	public int getOrder() {
		return this.order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	public int getProjectCount() {
		return projectCount;
	}

	public void setProjectCount(int projectCount) {
		this.projectCount = projectCount;
	}
	
	public List<Project> getTeamEngagedProjects() {
		return teamEngagedProjects;
	}

	public void setTeamEngagedProjects(List<Project> teamEngagedProjects) {
		this.teamEngagedProjects = teamEngagedProjects;
	}
	
	public Project addTeamEngagedProject(Project project) {
		this.getTeamEngagedProjects().add(project);
		return project;
	}

	public Project removeTeamEngagedProject(Project project) {
		this.getTeamEngagedProjects().remove(project);
		return project;
	}

	

}