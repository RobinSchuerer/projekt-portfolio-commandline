package de.lv1871.projektportfolio.test;

class TestUtility {

//	public static List<TeamKapazitaet> readAssertations(final XSSFWorkbook workbook){
//		XSSFSheet sheet1 = workbook.getSheetAt(0);
//
//		Iterator<Row> rowIterator = sheet1.rowIterator();
//
//		int rowIndexOfOutput = 0;
//
//		while(rowIterator.hasNext()){
//			Row row = rowIterator.next();
//			Iterator<Cell> cellIterator = row.cellIterator();
//			while (cellIterator.hasNext()) {
//				Cell cell = cellIterator.next();
//				if (cell.getCellType() == CELL_TYPE_STRING &&  "OUTPUT".equals(cell.getStringCellValue())){
//					rowIndexOfOutput = cell.getRowIndex();
//				}
//			}
//		}
//
//		// Projekte
//		List<String> projekte = new ArrayList<>();
//		Iterator<Cell> projektCells = sheet1.getRow(rowIndexOfOutput + 1).cellIterator();
//		while (projektCells.hasNext()) {
//			Cell cell = projektCells.next();
//			if (cell.getCellType() == CELL_TYPE_STRING ){
//				projekte.add(cell.getStringCellValue());
//			}
//		}
//
//		// Teams
//		List<String> teams = new ArrayList<>();
//		Iterator<Cell> teamCells = sheet1.getRow(rowIndexOfOutput + 2).cellIterator();
//		while (teamCells.hasNext()) {
//			Cell cell = teamCells.next();
//			if (cell.getCellType() == CELL_TYPE_STRING && !cell.getStringCellValue().equals("month") ){
//				teams.add(cell.getStringCellValue());
//			}
//		}
//
//		Map<Integer,Date> datesUndIndexMap = new LinkedHashMap<>();
//		Iterator<Row> rowIterator2 = sheet1.rowIterator();
//		while(rowIterator2.hasNext()) {
//			Row row = rowIterator2.next();
//			if(row.getRowNum() < rowIndexOfOutput + 3){
//				continue;
//			}
//
//			Cell dateCell = row.getCell(0);
//
//			if(dateCell.getCellType() == CELL_TYPE_BLANK || dateCell.getCellType() == CELL_TYPE_STRING){
//				break;
//			}
//
//			Date dateString = dateCell.getDateCellValue();
//			datesUndIndexMap.put(row.getRowNum(), dateString);
//		}
//
//
//
//		List<TeamKapazitaet> works = new ArrayList<>();
//
//		// baue Kombinationen
//		for (int i = 0; i < teams.size(); i++) {
//			String team = teams.get(i);
//			String projekt = projekte.get(i);
//			for (Integer index :datesUndIndexMap.keySet()) {
//				Date date = datesUndIndexMap.get(index);
//				XSSFCell cell = sheet1.getRow(index).getCell(i + 1);
//				double numericCellValue = cell.getNumericCellValue();
//
////				TeamKapazitaet.newBuilder().withTeam()
////				works.add(new ProjectTeamWork(BigDecimal.valueOf(numericCellValue), projekt, team, new LocalDate(date)));
//
//			}
//		}
//		return works;
//	}

}
