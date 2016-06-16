package com.project.portfolio.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;


/**
 * The persistent class for the team_bucket database table.
 * 
 */
@Entity
@Table(name="team_bucket")
@NamedQuery(name="TeamBucket.findAll", query="SELECT t FROM TeamBucket t")
public class TeamBucket implements  Comparable<TeamBucket>, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="team_bucket_id")
	private int teamBucketId;

	@Column(name="bucket_month")
	private LocalDate bucketMonth;

	@Column(name="bucket_type")
	private int bucketType;

	//bi-directional many-to-one association to ProjectTeamWork
	@OneToMany(mappedBy="teamBucket", fetch=FetchType.EAGER)
	@JsonManagedReference
	private List<ProjectTeamWork> projectTeamWorks = new ArrayList<ProjectTeamWork>(0);

	//bi-directional many-to-one association to Team
	@ManyToOne
	@JoinColumn(name="bucket_team_id")
	@JsonBackReference
	private Team team;

	//uni-directional one-to-one association to TeamCapacity
	@OneToOne
	@JoinColumn(name="team_capacity_id")
	private TeamCapacity teamCapacity;
	
	private BigDecimal outstandingTeamWork = new BigDecimal(0);
	
	private String teamName;

	public TeamBucket() {
	}

	public int getTeamBucketId() {
		return this.teamBucketId;
	}

	public void setTeamBucketId(int teamBucketId) {
		this.teamBucketId = teamBucketId;
	}

	public LocalDate getBucketMonth() {
		return this.bucketMonth;
	}

	public void setBucketMonth(LocalDate bucketMonth) {
		this.bucketMonth = bucketMonth;
	}

	public int getBucketType() {
		return this.bucketType;
	}

	public void setBucketType(int bucketType) {
		this.bucketType = bucketType;
	}

	public List<ProjectTeamWork> getProjectTeamWorks() {
		return this.projectTeamWorks;
	}

	public void setProjectTeamWorks(List<ProjectTeamWork> projectTeamWorks) {
		this.projectTeamWorks = projectTeamWorks;
	}

	public ProjectTeamWork addProjectTeamWork(ProjectTeamWork projectTeamWork) {
		getProjectTeamWorks().add(projectTeamWork);
		projectTeamWork.setTeamBucket(this);

		return projectTeamWork;
	}

	public ProjectTeamWork removeProjectTeamWork(ProjectTeamWork projectTeamWork) {
		getProjectTeamWorks().remove(projectTeamWork);
		projectTeamWork.setTeamBucket(null);

		return projectTeamWork;
	}

	public Team getTeam() {
		return this.team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public TeamCapacity getTeamCapacity() {
		return this.teamCapacity;
	}

	public void setTeamCapacity(TeamCapacity teamCapacity) {
		this.teamCapacity = teamCapacity;
	}
	
	public BigDecimal getOutstandingTeamWork() {
		return outstandingTeamWork;
	}

	public void setOutstandingTeamWork(BigDecimal outstandingTeamWork) {
		this.outstandingTeamWork = outstandingTeamWork;
	}
	
	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	
	@Override
	public int compareTo(TeamBucket o) {
		if(this.getBucketMonth().isAfter(o.getBucketMonth())) {
			return 1;
		} else if(this.getBucketMonth().isBefore(o.getBucketMonth())) {
			return -1;
		} else {
			return 0;
		}
	}

}