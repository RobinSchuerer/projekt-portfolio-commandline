package com.project.portfolio.domain;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the restriction database table.
 * 
 */
@Entity
@NamedQuery(name="Restriction.findAll", query="SELECT r FROM Restriction r")
public class Restriction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="restriction_id")
	private int restrictionId;

	@Column(name="is_required")
	private Object isRequired;

	@Column(name="team_capacity_allocation")
	private Double teamCapacityAllocation;

	//bi-directional many-to-one association to ProjectPortfolio
	@ManyToOne
	@JoinColumn(name="project_portfolio_id")
	private ProjectPortfolio projectPortfolio;

	//uni-directional many-to-one association to ProjectType
	@ManyToOne
	@JoinColumn(name="project_type_code")
	private ProjectType projectType;

	public Restriction() {
	}

	public int getRestrictionId() {
		return this.restrictionId;
	}

	public void setRestrictionId(int restrictionId) {
		this.restrictionId = restrictionId;
	}

	public Object getIsRequired() {
		return this.isRequired;
	}

	public void setIsRequired(Object isRequired) {
		this.isRequired = isRequired;
	}

	public Double getTeamCapacityAllocation() {
		return this.teamCapacityAllocation;
	}

	public void setTeamCapacityAllocation(Double teamCapacityAllocation) {
		this.teamCapacityAllocation = teamCapacityAllocation;
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

}