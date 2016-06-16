package com.project.portfolio.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;


/**
 * The persistent class for the team_capacity database table.
 * 
 */
@Entity
@Table(name="team_capacity")
@NamedQuery(name="TeamCapacity.findAll", query="SELECT t FROM TeamCapacity t")
public class TeamCapacity implements Comparable<TeamCapacity>, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="team_capacity_id")
	private int teamCapacityId;

	@Column(name="man_days")
	private BigDecimal manDays = new BigDecimal(0);
	
	private LocalDate month;

	//bi-directional many-to-one association to Team
	@ManyToOne
	@JoinColumn(name="team_id")
	@JsonBackReference
	private Team team;
	
	private double unUsedSharedCapacity;
	
	private int noOfunUsedCapacitySharedProjects;

	public TeamCapacity() {
	}

	public int getTeamCapacityId() {
		return this.teamCapacityId;
	}

	public void setTeamCapacityId(int teamCapacityId) {
		this.teamCapacityId = teamCapacityId;
	}

	public LocalDate getMonth() {
		return this.month;
	}

	public void setMonth(LocalDate month) {
		this.month = month;
	}

	public Team getTeam() {
		return this.team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}
	
	public double getUnUsedSharedCapacity() {
		return unUsedSharedCapacity;
	}

	public void setUnUsedSharedCapacity(double unUsedSharedCapacity) {
		this.unUsedSharedCapacity = unUsedSharedCapacity;
	}
	
	public int getNoOfunUsedCapacitySharedProjects() {
		return noOfunUsedCapacitySharedProjects;
	}

	public void setNoOfunUsedCapacitySharedProjects(
			int noOfunUsedCapacitySharedProjects) {
		this.noOfunUsedCapacitySharedProjects = noOfunUsedCapacitySharedProjects;
	}
	
	public BigDecimal getManDays() {
		return manDays;
	}

	public void setManDays(BigDecimal manDays) {
		this.manDays = manDays;
	}
	
	@Override
	public int compareTo(TeamCapacity o) {
		if(this.getMonth().isAfter(o.getMonth())) {
			return 1;
		} else if(this.getMonth().isBefore(o.getMonth())) {
			return -1;
		} else {
			return 0;
		}
	}

}