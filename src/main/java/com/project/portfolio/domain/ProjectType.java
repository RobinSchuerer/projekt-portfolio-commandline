package com.project.portfolio.domain;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the project_type database table.
 * 
 */
@Entity
@Table(name="project_type")
@NamedQuery(name="ProjectType.findAll", query="SELECT p FROM ProjectType p")
public class ProjectType implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="project_type_code")
	private String projectTypeCode;

	private String description;

	private String name;

	public ProjectType() {
	}
	
	public ProjectType(String projectTypeCode, String name) {
		super();
		this.projectTypeCode = projectTypeCode;
		this.name = name;
	}
	
	public ProjectType(String projectTypeCode) {
		super();
		this.projectTypeCode = projectTypeCode;
	}

	public String getProjectTypeCode() {
		return this.projectTypeCode;
	}

	public void setProjectTypeCode(String projectTypeCode) {
		this.projectTypeCode = projectTypeCode;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}