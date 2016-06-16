package com.project.portfolio.domain;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;


/**
 * The persistent class for the project database table.
 * 
 */
@Entity
@NamedQuery(name="Project.findAll", query="SELECT p FROM Project p")
public class Project implements Comparable<Project>, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="project_id")
	private int projectId;

	private String color;

	
	@Column(name="deadline_month")
	private LocalDate deadlineMonth;

	@Column(name="execution_month")
	private LocalDate executionMonth;

	private String name;

	private int priority;
	
	private int order;

	//bi-directional many-to-one association to ProjectPortfolio
	@ManyToOne
	@JoinColumn(name="portfolio_id")
	@JsonBackReference
	private ProjectPortfolio projectPortfolio;

	//uni-directional many-to-one association to ProjectType
	@ManyToOne
	@JoinColumn(name="project_type_cd")
	private ProjectType projectType;

	//bi-directional many-to-one association to TeamEffort
	@OneToMany(mappedBy="project", fetch=FetchType.EAGER)
	@JsonManagedReference
	private Set<TeamEffort> teamEfforts = new HashSet<TeamEffort>(0);

	//bi-directional many-to-one association to ProjectTeamWork
	@OneToMany(mappedBy="project", fetch=FetchType.EAGER)
	@JsonManagedReference
	private Set<ProjectTeamWork> projectTeamWorks = new HashSet<ProjectTeamWork>(0);
	
	@ManyToMany
	  @JoinTable(
	      name="project_team",
	      joinColumns=@JoinColumn(name="portfolio_project_id", referencedColumnName="project_id"),
	      inverseJoinColumns=@JoinColumn(name="portfolio_team_id", referencedColumnName="team_id"))
	@JsonBackReference
	  private SortedSet<Team> teams = new TreeSet<Team>();
	
	private boolean projectStarted;
	
	private Map<String, Boolean> teamCompletedWork = new HashMap<String, Boolean>();

	public boolean isProjectStarted() {
		return this.projectStarted;
	}

	public void setProjectStarted(boolean projectStarted) {
		this.projectStarted = projectStarted;
	}

	public Project() {
	}

	public int getProjectId() {
		return this.projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public LocalDate getDeadlineMonth() {
		return this.deadlineMonth;
	}

	public void setDeadlineMonth(LocalDate deadlineMonth) {
		this.deadlineMonth = deadlineMonth;
	}

	public LocalDate getExecutionMonth() {
		return this.executionMonth;
	}

	public void setExecutionMonth(LocalDate executionMonth) {
		this.executionMonth = executionMonth;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPriority() {
		return this.priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public ProjectPortfolio getProjectPortfolio() {
		return this.projectPortfolio;
	}

	public void setProjectPortfolio(ProjectPortfolio projectPortfolio) {
		this.projectPortfolio = projectPortfolio;
	}

	public ProjectType getProjectType() {
		return this.projectType;
	}

	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}

	public Set<TeamEffort> getTeamEfforts() {
		return this.teamEfforts;
	}

	public void setTeamEfforts(Set<TeamEffort> teamEfforts) {
		this.teamEfforts = teamEfforts;
	}

	public TeamEffort addTeamEffort(TeamEffort teamEffort) {
		getTeamEfforts().add(teamEffort);
		teamEffort.setProject(this);

		return teamEffort;
	}

	public TeamEffort removeTeamEffort(TeamEffort teamEffort) {
		getTeamEfforts().remove(teamEffort);
		teamEffort.setProject(null);

		return teamEffort;
	}

	public Set<ProjectTeamWork> getProjectTeamWorks() {
		return this.projectTeamWorks;
	}

	public void setProjectTeamWorks(Set<ProjectTeamWork> projectTeamWorks) {
		this.projectTeamWorks = projectTeamWorks;
	}

	public ProjectTeamWork addProjectTeamWork(ProjectTeamWork projectTeamWork) {
		getProjectTeamWorks().add(projectTeamWork);
		projectTeamWork.setProject(this);

		return projectTeamWork;
	}

	public ProjectTeamWork removeProjectTeamWork(ProjectTeamWork projectTeamWork) {
		getProjectTeamWorks().remove(projectTeamWork);
		projectTeamWork.setProject(null);

		return projectTeamWork;
	}
	
	public SortedSet<Team> getTeams() {
		return teams;
	}

	public void setTeams(SortedSet<Team> teams) {
		this.teams = teams;
	}
	
	public boolean addTeam(Team e) {
		return teams.add(e);
	}

	public boolean removeTeam(Team o) {
		return teams.remove(o);
	}
	
	public Map<String, Boolean> getTeamCompletedWork() {
		return teamCompletedWork;
	}

	public void setTeamCompletedWork(Map<String, Boolean> teamCompletedWork) {
		this.teamCompletedWork = teamCompletedWork;
	}

	@Override
	public int compareTo(Project o) {
//		return this.getName().compareTo(o.getName());
		return this.getOrder() - o.getOrder();
		/*if(this.getPriority() > (o.getPriority())) {
			return 1;
		} else if(this.getPriority() < (o.getPriority())) {
			return -1;
		} else {
			return 0;
		}*/
	}
	
	
	
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	// Comparator for Priorities..
	public static  Comparator<Project> priorityComparator = new Comparator<Project>() {
		@Override
		public int compare(Project p1, Project p2) {
			int priorityOrder = 0;
			priorityOrder = p1.getPriority()-p2.getPriority();
			 if(priorityOrder == 0) return 1;
			 else return priorityOrder;
		}
	};
	

}