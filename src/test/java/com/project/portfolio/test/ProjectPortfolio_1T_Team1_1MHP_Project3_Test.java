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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class ProjectPortfolio_1T_Team1_1MHP_Project3_Test {
	XSSFWorkbook  workbook = null;
	XSSFSheet sheet = null;
	ExcelManager excelManager = null;
	ProjectPortfolio portfolio = null;
	BufferedInputStream bufferedInputStream  = null;
	

	@Before
	public void setUp() throws Exception {
		ClassLoader loader = Class.forName("com.project.portfolio.test.ProjectPortfolio_1T_Team1_1MHP_Project3_Test").getClassLoader();
		bufferedInputStream = (BufferedInputStream)loader.getResourceAsStream("com\\project\\portfolio\\resources\\016_1T_Team1_1MHP_Project3.xlsx");
//		File file = new File();
		 
		workbook = new XSSFWorkbook(bufferedInputStream);
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
					Iterator<TeamBucket> bucketIterator = teamIterator.next().getTeamBuckets().iterator();
					TestUtility.verifyResult(bucketIterator, project, works);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
