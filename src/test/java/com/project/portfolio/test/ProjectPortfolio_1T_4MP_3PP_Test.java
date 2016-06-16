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

public class ProjectPortfolio_1T_4MP_3PP_Test {

	XSSFWorkbook  workbook = null;
	XSSFSheet sheet = null;
	ExcelManager excelManager = null;
	ProjectPortfolio portfolio = null;
	BufferedInputStream bufferedInputStream  = null;
	
	@Before
	public void setUp() throws Exception {
		java.nio.file.Path resPath = java.nio.file.Paths.get( getClass().getResource("/013 Many-teams-many-projects_team1extracted (4).xlsx").toURI());

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
	public void test1T4MP3PP() {
		assertNotNull(workbook);
		
		List<ProjectTeamWork> works = new ArrayList<ProjectTeamWork>();
		
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2), "Project1", "Team1", new LocalDate(2016, 12, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2), "Project1", "Team1", new LocalDate(2016, 11, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2), "Project1", "Team1", new LocalDate(2016, 10, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2), "Project1", "Team1", new LocalDate(2016, 9, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(1.3333333333333335), "Project1", "Team1", new LocalDate(2016, 8, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(1.866666666666667), "Project1", "Team1", new LocalDate(2016, 7, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2.666666666666667), "Project1", "Team1", new LocalDate(2016, 6, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2.666666666666667), "Project1", "Team1", new LocalDate(2016, 5, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2.666666666666667), "Project1", "Team1", new LocalDate(2016, 4, 1)));
		
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2), "Project2", "Team1", new LocalDate(2016, 10, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2), "Project2", "Team1", new LocalDate(2016, 9, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(1.3333333333333335), "Project2", "Team1", new LocalDate(2016, 8, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(1.866666666666667), "Project2", "Team1", new LocalDate(2016, 7, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2.666666666666667), "Project2", "Team1", new LocalDate(2016, 6, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2.666666666666667), "Project2", "Team1", new LocalDate(2016, 5, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2.666666666666667), "Project2", "Team1", new LocalDate(2016, 4, 1)));
		
		works.add(new ProjectTeamWork(BigDecimal.valueOf(8), "Project3", "Team1", new LocalDate(2017, 8, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(7), "Project3", "Team1", new LocalDate(2017, 7, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project3", "Team1", new LocalDate(2017, 6, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project3", "Team1", new LocalDate(2017, 5, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project3", "Team1", new LocalDate(2017, 4, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project3", "Team1", new LocalDate(2017, 3, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project3", "Team1", new LocalDate(2017, 2, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project3", "Team1", new LocalDate(2017, 1, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project3", "Team1", new LocalDate(2016, 12, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project3", "Team1", new LocalDate(2016, 11, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project3", "Team1", new LocalDate(2016, 10, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project3", "Team1", new LocalDate(2016, 9, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project3", "Team1", new LocalDate(2016, 8, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project3", "Team1", new LocalDate(2016, 7, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project3", "Team1", new LocalDate(2016, 6, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project3", "Team1", new LocalDate(2016, 5, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project3", "Team1", new LocalDate(2016, 4, 1)));
		
		works.add(new ProjectTeamWork(BigDecimal.valueOf(8), "Project4", "Team1", new LocalDate(2017, 12, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2), "Project4", "Team1", new LocalDate(2017, 11, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project4", "Team1", new LocalDate(2017, 10, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project4", "Team1", new LocalDate(2017, 9, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project4", "Team1", new LocalDate(2017, 8, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project4", "Team1", new LocalDate(2017, 7, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project4", "Team1", new LocalDate(2017, 6, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project4", "Team1", new LocalDate(2017, 5, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project4", "Team1", new LocalDate(2017, 4, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project4", "Team1", new LocalDate(2017, 3, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project4", "Team1", new LocalDate(2017, 2, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project4", "Team1", new LocalDate(2017, 1, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project4", "Team1", new LocalDate(2016, 12, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project4", "Team1", new LocalDate(2016, 11, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project4", "Team1", new LocalDate(2016, 10, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project4", "Team1", new LocalDate(2016, 9, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project4", "Team1", new LocalDate(2016, 8, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project4", "Team1", new LocalDate(2016, 7, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project4", "Team1", new LocalDate(2016, 6, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project4", "Team1", new LocalDate(2016, 5, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project4", "Team1", new LocalDate(2016, 4, 1)));
		
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2.5), "Project5", "Team1", new LocalDate(2016, 12, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2.5), "Project5", "Team1", new LocalDate(2016, 11, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2.5), "Project5", "Team1", new LocalDate(2016, 10, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2.5), "Project5", "Team1", new LocalDate(2016, 9, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(1.666666666666667), "Project5", "Team1", new LocalDate(2016, 8, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2.3333333333333335), "Project5", "Team1", new LocalDate(2016, 7, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(3.333333333333334), "Project5", "Team1", new LocalDate(2016, 6, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(3.333333333333334), "Project5", "Team1", new LocalDate(2016, 5, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(3.333333333333334), "Project5", "Team1", new LocalDate(2016, 4, 1)));
		
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2.5), "Project6", "Team1", new LocalDate(2016, 12, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2.5), "Project6", "Team1", new LocalDate(2016, 11, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2.5), "Project6", "Team1", new LocalDate(2016, 10, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2.5), "Project6", "Team1", new LocalDate(2016, 9, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project6", "Team1", new LocalDate(2016, 8, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project6", "Team1", new LocalDate(2016, 7, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project6", "Team1", new LocalDate(2016, 6, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project6", "Team1", new LocalDate(2016, 5, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project6", "Team1", new LocalDate(2016, 4, 1)));
		
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2.5), "Project6", "Team1", new LocalDate(2016, 12, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(2.5), "Project6", "Team1", new LocalDate(2016, 11, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project6", "Team1", new LocalDate(2016, 10, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project6", "Team1", new LocalDate(2016, 9, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project6", "Team1", new LocalDate(2016, 8, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project6", "Team1", new LocalDate(2016, 7, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project6", "Team1", new LocalDate(2016, 6, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project6", "Team1", new LocalDate(2016, 5, 1)));
		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project6", "Team1", new LocalDate(2016, 4, 1)));


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
				if(project.getProjectType().getProjectTypeCode().equals("SP")) {
					assertNotNull(project.getPriority());
				}
				// Perform Output Testing.
				// Verify the result for the Projects...
				Iterator<Team> teamIterator = portfolio.getTeams().iterator();
				while(teamIterator.hasNext()) {
					Team team = teamIterator.next();
					Iterator<TeamBucket> bucketIterator = team.getTeamBuckets().iterator();
					TestUtility.verifyResult(bucketIterator, project, works);
					if(team.getName().equalsIgnoreCase("Team1")){
						assertEquals(44.6, team.getOverflowWork().doubleValue(), 0);	
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
