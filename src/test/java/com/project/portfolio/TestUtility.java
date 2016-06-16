package com.project.portfolio;

import com.project.portfolio.domain.Project;
import com.project.portfolio.domain.ProjectTeamWork;
import com.project.portfolio.domain.TeamBucket;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.util.*;

import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BLANK;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;
import static org.junit.Assert.assertEquals;

public class TestUtility {

	public static ProjectTeamWork getTeamWorkOnProject(ProjectTeamWork work, List<ProjectTeamWork> works){
		ProjectTeamWork tProjectWork = null;
		for(ProjectTeamWork ptWork : works) {
			if(ptWork.getMonth().equals(work.getMonth()) && ptWork.getProjectName().equals(work.getProjectName()) && ptWork.getTeamName().equals(work.getTeamName())) {
				tProjectWork = ptWork;
				break;
			}
		}
		return tProjectWork;
	}
	
	public static void verifyResult(Iterator<TeamBucket> bucketIterator, Project project, List<ProjectTeamWork> works){
		while(bucketIterator.hasNext()) {
			List<ProjectTeamWork> teamProjectWorks = bucketIterator.next().getProjectTeamWorks();
			for(ProjectTeamWork teamWork: teamProjectWorks) {
				if(teamWork.getProjectName().equals(project.getName())) {
					ProjectTeamWork actualProjectWork = getTeamWorkOnProject(teamWork, works);
					if(actualProjectWork != null) {
						assertEquals(teamWork.getEffort().doubleValue(), getTeamWorkOnProject(teamWork, works).getEffort().doubleValue(),0);	
					}
					
				}
			}
		}
	}

	public static List<ProjectTeamWork> readAssertations(final XSSFWorkbook workbook){
		XSSFSheet sheet1 = workbook.getSheetAt(0);

		Iterator<Row> rowIterator = sheet1.rowIterator();

		int rowIndexOfOutput = 0;

		while(rowIterator.hasNext()){
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				if (cell.getCellType() == CELL_TYPE_STRING &&  "OUTPUT".equals(cell.getStringCellValue())){
					rowIndexOfOutput = cell.getRowIndex();
				}
			}
		}

		// Projekte
		List<String> projekte = new ArrayList<>();
		Iterator<Cell> projektCells = sheet1.getRow(rowIndexOfOutput + 1).cellIterator();
		while (projektCells.hasNext()) {
			Cell cell = projektCells.next();
			if (cell.getCellType() == CELL_TYPE_STRING ){
				projekte.add(cell.getStringCellValue());
			}
		}

		// Teams
		List<String> teams = new ArrayList<>();
		Iterator<Cell> teamCells = sheet1.getRow(rowIndexOfOutput + 2).cellIterator();
		while (teamCells.hasNext()) {
			Cell cell = teamCells.next();
			if (cell.getCellType() == CELL_TYPE_STRING && !cell.getStringCellValue().equals("month") ){
				teams.add(cell.getStringCellValue());
			}
		}

		Map<Integer,Date> datesUndIndexMap = new LinkedHashMap<>();
		Iterator<Row> rowIterator2 = sheet1.rowIterator();
		while(rowIterator2.hasNext()) {
			Row row = rowIterator2.next();
			if(row.getRowNum() < rowIndexOfOutput + 3){
				continue;
			}

			Cell dateCell = row.getCell(0);

			if(dateCell.getCellType() == CELL_TYPE_BLANK || dateCell.getCellType() == CELL_TYPE_STRING){
				break;
			}

			Date dateString = dateCell.getDateCellValue();
			datesUndIndexMap.put(row.getRowNum(), dateString);
		}



		List<ProjectTeamWork> works = new ArrayList<ProjectTeamWork>();

		// baue Kombinationen
		for (int i = 0; i < teams.size(); i++) {
			String team = teams.get(i);
			String projekt = projekte.get(i);
			for (Integer index :datesUndIndexMap.keySet()) {
				Date date = datesUndIndexMap.get(index);
				XSSFCell cell = sheet1.getRow(index).getCell(i + 1);
				double numericCellValue = cell.getNumericCellValue();

				works.add(new ProjectTeamWork(BigDecimal.valueOf(numericCellValue), projekt, team, new LocalDate(date)));

			}
		}
		return works;
	}

}
