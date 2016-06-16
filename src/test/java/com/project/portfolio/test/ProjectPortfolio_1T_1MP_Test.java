package com.project.portfolio.test;

import com.project.portfolio.ExcelManager;
import com.project.portfolio.TestUtility;
import com.project.portfolio.domain.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class ProjectPortfolio_1T_1MP_Test {

	XSSFWorkbook  workbook = null;
	XSSFSheet sheet = null;
	ExcelManager excelManager = null;
	ProjectPortfolio portfolio = null;
	BufferedInputStream bufferedInputStream  = null;
	

	@Before
	public void setUp() throws Exception {
		java.nio.file.Path resPath = java.nio.file.Paths.get( getClass().getResource("/001 One-team-one-mhproject_v3.xlsx").toURI());

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
	public void test1T1MP() throws Exception {
		assertNotNull(workbook);

		List<ProjectTeamWork> works = TestUtility.readAssertations(this.workbook);
//		works.add(new ProjectTeamWork(BigDecimal.valueOf(8), "Project1", "Team1", new LocalDate(2016, 12, 1)));
//		works.add(new ProjectTeamWork(BigDecimal.valueOf(8), "Project1", "Team1", new LocalDate(2016, 11, 1)));
//		works.add(new ProjectTeamWork(BigDecimal.valueOf(8), "Project1", "Team1", new LocalDate(2016, 10, 1)));
//		works.add(new ProjectTeamWork(BigDecimal.valueOf(8), "Project1", "Team1", new LocalDate(2016, 9, 1)));
//		works.add(new ProjectTeamWork(BigDecimal.valueOf(4), "Project1", "Team1", new LocalDate(2016, 8, 1)));
//		works.add(new ProjectTeamWork(BigDecimal.valueOf(5.6), "Project1", "Team1", new LocalDate(2016, 7, 1)));
//		works.add(new ProjectTeamWork(BigDecimal.valueOf(8), "Project1", "Team1", new LocalDate(2016, 6, 1)));
//		works.add(new ProjectTeamWork(BigDecimal.valueOf(5.4), "Project1", "Team1", new LocalDate(2016, 5, 1)));
//		works.add(new ProjectTeamWork(BigDecimal.valueOf(0), "Project1", "Team1", new LocalDate(2016, 4, 1)));
		
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
					Iterator<TeamBucket> bucketIterator = teamIterator.next().getTeamBuckets().iterator();
					TestUtility.verifyResult(bucketIterator, project, works);
				}
			}

		
	}

}
