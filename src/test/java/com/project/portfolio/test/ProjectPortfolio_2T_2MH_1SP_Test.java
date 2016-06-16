package com.project.portfolio.test;

import com.project.portfolio.ExcelManager;
import com.project.portfolio.TestUtility;
import com.project.portfolio.domain.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ProjectPortfolio_2T_2MH_1SP_Test {

	XSSFWorkbook  workbook = null;
	XSSFSheet sheet = null;
	ExcelManager excelManager = null;
	ProjectPortfolio portfolio = null;
	BufferedInputStream bufferedInputStream  = null;
	
	@Before
	public void setUp() throws Exception {
		java.nio.file.Path resPath = java.nio.file.Paths.get( getClass().getResource("/009 Two-teams-two-mh-one-strategic-project.xlsx").toURI());

		workbook = new XSSFWorkbook(Files.newInputStream(resPath));
		excelManager = new ExcelManager();
	}

	@After
	public void tearDown() throws Exception {
		if(workbook != null) {
			workbook.close();
		}
	}
	
	@Test
	public void test1T1MP() {
		assertNotNull(workbook);
		
		List<ProjectTeamWork> works = new ArrayList<ProjectTeamWork>();
		works.add(new ProjectTeamWork(BigDecimal.valueOf(8), "Project1", "Team1", new LocalDate(2016, 12, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(8), "Project1", "Team1", new LocalDate(2016, 11, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(4), "Project1", "Team1", new LocalDate(2016, 10, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(4), "Project1", "Team1", new LocalDate(2016, 9, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2), "Project1", "Team1", new LocalDate(2016, 8, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2.8), "Project1", "Team1", new LocalDate(2016, 7, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(4), "Project1", "Team1", new LocalDate(2016, 6, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(4), "Project1", "Team1", new LocalDate(2016, 5, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(1.2), "Project1", "Team1", new LocalDate(2016, 4, 1)));
		
		works.add(new ProjectTeamWork(BigDecimal.valueOf(4), "Project2", "Team1", new LocalDate(2016, 10, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(4), "Project2", "Team1", new LocalDate(2016, 9, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2), "Project2", "Team1", new LocalDate(2016, 8, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2.8), "Project2", "Team1", new LocalDate(2016, 7, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(4), "Project2", "Team1", new LocalDate(2016, 6, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(4), "Project2", "Team1", new LocalDate(2016, 5, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(4.2), "Project2", "Team1", new LocalDate(2016, 4, 1)));
		
		works.add(new ProjectTeamWork(BigDecimal.valueOf(1), "Project3", "Team1", new LocalDate(2017, 03, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(10), "Project3", "Team1", new LocalDate(2017, 02, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(10), "Project3", "Team1", new LocalDate(2017, 01, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2), "Project3", "Team1", new LocalDate(2016, 12, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2), "Project3", "Team1", new LocalDate(2016, 11, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2), "Project3", "Team1", new LocalDate(2016, 10, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2), "Project3", "Team1", new LocalDate(2016, 9, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(1), "Project3", "Team1", new LocalDate(2016, 8, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(1.4), "Project3", "Team1", new LocalDate(2016, 7, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2), "Project3", "Team1", new LocalDate(2016, 6, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2), "Project3", "Team1", new LocalDate(2016, 5, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(4.6), "Project3", "Team1", new LocalDate(2016, 4, 1)));
		
		works.add(new ProjectTeamWork(BigDecimal.valueOf(6.4), "Project1", "Team2", new LocalDate(2016, 12, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(6.4), "Project1", "Team2", new LocalDate(2016, 11, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(3.2), "Project1", "Team2", new LocalDate(2016, 10, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project1", "Team2", new LocalDate(2016, 9, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(1.2), "Project1", "Team2", new LocalDate(2016, 8, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2.8), "Project1", "Team2", new LocalDate(2016, 7, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project1", "Team2", new LocalDate(2016, 6, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project1", "Team2", new LocalDate(2016, 5, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project1", "Team2", new LocalDate(2016, 4, 1)));
		
		works.add(new ProjectTeamWork(BigDecimal.valueOf(3.2), "Project2", "Team2", new LocalDate(2016, 10, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project2", "Team2", new LocalDate(2016, 9, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(1.2), "Project2", "Team2", new LocalDate(2016, 8, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(3.6), "Project2", "Team2", new LocalDate(2016, 7, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(6.4), "Project2", "Team2", new LocalDate(2016, 6, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(5.6), "Project2", "Team2", new LocalDate(2016, 5, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project2", "Team2", new LocalDate(2016, 4, 1)));
		
		works.add(new ProjectTeamWork(BigDecimal.valueOf(5), "Project3", "Team2", new LocalDate(2017, 8, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(8), "Project3", "Team2", new LocalDate(2017, 7, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(8), "Project3", "Team2", new LocalDate(2017, 6, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(8), "Project3", "Team2", new LocalDate(2017, 5, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(8), "Project3", "Team2", new LocalDate(2017, 4, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(8), "Project3", "Team2", new LocalDate(2017, 3, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(8), "Project3", "Team2", new LocalDate(2017, 2, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(8), "Project3", "Team2", new LocalDate(2017, 1, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(1.6), "Project3", "Team2", new LocalDate(2016, 12, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(1.6), "Project3", "Team2", new LocalDate(2016, 11, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(1.6), "Project3", "Team2", new LocalDate(2016, 10, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project3", "Team2", new LocalDate(2016, 9, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0.6), "Project3", "Team2", new LocalDate(2016, 8, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(1.6), "Project3", "Team2", new LocalDate(2016, 7, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(1.6), "Project3", "Team2", new LocalDate(2016, 6, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2.4), "Project3", "Team2", new LocalDate(2016, 5, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(8), "Project3", "Team2", new LocalDate(2016, 4, 1)));


		
		try {
			portfolio = excelManager.readPortfolio(workbook);
			assertNotNull(portfolio);
			portfolio = excelManager.processProjectPortfolio(workbook);
			Iterator<Project> projectIterator = portfolio.getProjects().iterator();
			while(projectIterator.hasNext()) {
				Project project = projectIterator.next();
				assertNotNull(project.getExecutionMonth());
				assertNotNull(project.getDeadlineMonth());
				assertNotNull(project.getTeamEfforts());
				if(project.getProjectType().getProjectTypeCode().equals("SP") && project.getName().equalsIgnoreCase("Project3")) {
					assertNotNull(project.getPriority());
					assertEquals("2017-08-01", project.getDeadlineMonth().toString());
				}
				// Perform Output Testing.
				// Verify the result for the Projects...
				Iterator<Team> teamIterator = portfolio.getTeams().iterator();
				while(teamIterator.hasNext()) {
					Iterator<TeamBucket> bucketIterator = teamIterator.next().getTeamBuckets().iterator();
					TestUtility.verifyResult(bucketIterator, project, works);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
