package com.project.portfolio.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonManagedReference;


/**
 * The persistent class for the project_portfolio database table.
 * 
 */
@Entity
@Table(name="project_portfolio")
@NamedQuery(name="ProjectPortfolio.findAll", query="SELECT p FROM ProjectPortfolio p")
public class ProjectPortfolio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="project_portfolio_id")
	private int projectPortfolioId;

	private String name;

	//bi-directional many-to-one association to Project
	@OneToMany(mappedBy="projectPortfolio", fetch=FetchType.EAGER)
	@JsonManagedReference
	private SortedSet<Project> projects = new TreeSet<Project>();

	//bi-directional many-to-one association to Restriction
	@OneToMany(mappedBy="projectPortfolio", fetch=FetchType.EAGER)
	private Set<Restriction> restrictions = new HashSet<Restriction>(0);

	//bi-directional many-to-one association to Team
	@OneToMany(mappedBy="projectPortfolio", fetch=FetchType.EAGER)
	private SortedSet<Team> teams = new TreeSet<Team>();
	
	private LocalDate[] monthsForEstimation = new LocalDate[24];  

	public ProjectPortfolio() {
	}

	public int getProjectPortfolioId() {
		return this.projectPortfolioId;
	}

	public void setProjectPortfolioId(int projectPortfolioId) {
		this.projectPortfolioId = projectPortfolioId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SortedSet<Project> getProjects() {
		return this.projects;
	}

	public void setProjects(SortedSet<Project> projects) {
		this.projects = projects;
	}

	public Project addProject(Project project) {
		getProjects().add(project);
		project.setProjectPortfolio(this);

		return project;
	}

	public Project removeProject(Project project) {
		getProjects().remove(project);
		project.setProjectPortfolio(null);

		return project;
	}

	public Set<Restriction> getRestrictions() {
		return this.restrictions;
	}

	public void setRestrictions(Set<Restriction> restrictions) {
		this.restrictions = restrictions;
	}

	public Restriction addRestriction(Restriction restriction) {
		getRestrictions().add(restriction);
		restriction.setProjectPortfolio(this);

		return restriction;
	}

	public Restriction removeRestriction(Restriction restriction) {
		getRestrictions().remove(restriction);
		restriction.setProjectPortfolio(null);

		return restriction;
	}

	public SortedSet<Team> getTeams() {
		return this.teams;
	}

	public void setTeams(SortedSet<Team> teams) {
		this.teams = teams;
	}

	public Team addTeam(Team team) {
		getTeams().add(team);
		team.setProjectPortfolio(this);

		return team;
	}

	public Team removeTeam(Team team) {
		getTeams().remove(team);
		team.setProjectPortfolio(null);

		return team;
	}
	
	public LocalDate[] getMonthsForEstimation() {
		return monthsForEstimation;
	}

	public void setMonthsForEstimation(LocalDate[] monthsForEstimation) {
		this.monthsForEstimation = monthsForEstimation;
	}
	
	public void stringPortfolio() {
		 String data = "";
			 Iterator<Team> it2 = teams.iterator();
			 while(it2.hasNext()) {
				 Team team =  it2.next();
				 System.out.print(team.getName()+"\t");
				 SortedSet<Project> tPrjts = team.getProjects();
				 Iterator<Project> tpIT = tPrjts.iterator();
				 
				 while(tpIT.hasNext()) {
					 Project project = tpIT.next();
					 System.out.print("\t"+project.getName());
					 
				 }
				 System.out.println("");
				
//					 Project project = tpIT2.next();
//					 System.out.print(project.getName()+"\t");
					 Iterator<TeamBucket> teamBuckets = team.getTeamBuckets().iterator();
					 System.out.println("");
					 while(teamBuckets.hasNext()) {
						 TeamBucket tb = teamBuckets.next();
						 List<ProjectTeamWork> teamWorks = tb.getProjectTeamWorks();
						 if(teamWorks.size() > 0) {
							 System.out.print(tb.getBucketMonth() + "\t");
							 for(ProjectTeamWork teamWit : teamWorks) {
								 Iterator<Project> tpIT2 = tPrjts.iterator();
								 while(tpIT2.hasNext()) {
									 if(teamWit.getProjectName().equals(tpIT2.next().getName())) {
										 System.out.print(teamWit.getEffort() + "\t\t");
									 }
								 }
							 }
							 System.out.println("");
							 /*Iterator<ProjectTeamWork> teamWit = teamWorks.iterator();
							 while(teamWit.hasNext()){
								 System.out.print(teamWit.next().getEffort() + "\t\t");
								 
							 }*/
//							 System.out.println("");
						 } else {
							 break;
						 }
					 }
				 System.out.println("");
			 }
	}

}