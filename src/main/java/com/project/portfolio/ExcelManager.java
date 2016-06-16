package com.project.portfolio;

import com.project.portfolio.domain.*;
import com.project.portfolio.exceptions.WrongFileFormatException;
import com.project.portfolio.utility.ProjectColor;
import com.project.portfolio.utility.TeamColor;
import de.lv1871.projektportfolio.domain.ProjektPortfolio;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDate;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

import static com.project.portfolio.utility.PortfolioConstants.*;

public class ExcelManager {

    public ProjektPortfolio read(XSSFWorkbook workbook) throws Exception {
        XSSFSheet sheet = workbook.getSheetAt(0);

        String portfolioName = getPortfolioName(sheet);

        return ProjektPortfolio.newBuilder().withName(portfolioName).build();

    }

    private String getPortfolioName(XSSFSheet sheet) {
        Iterator<Row> rowIterator = sheet.rowIterator();
        while(rowIterator.hasNext()){
            Row zeile = rowIterator.next();
            // erste Zelle der Zeile soll Portfolio sein
            Cell ersteZelle = zeile.getCell(0);
            if(ersteZelle.getCellType() == Cell.CELL_TYPE_STRING && "portfolio".equals(ersteZelle.getStringCellValue())){
                return zeile.getCell(2).getStringCellValue();
            }
        }

        throw new AssertionError("Portfolio Name konnte nicht gefunden werden");
    }


    public ProjectPortfolio readPortfolio(XSSFWorkbook workbook) throws Exception {



		boolean isInputRead = false;
		ProjectPortfolio portfolio = new ProjectPortfolio();
		Set<Restriction> restrictions = portfolio.getRestrictions();  
		SortedSet<Team> teams = portfolio.getTeams();
		LocalDate[] monthsForEstimation= portfolio.getMonthsForEstimation();
		Team team = null;
		TeamCapacity teamCapacity = null;
		LocalDate executionMonth= null;
		Row row = null;
		String[] colors = null;
		XSSFSheet sheet = workbook.getSheetAt(0);
		
		
		//Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = sheet.iterator();
		
		while (rowIterator.hasNext()) {
			 
			if(isInputRead) {
				break;	
			}
		row = rowIterator.next();	
			
			//Get iterator to all cells of current row
			Iterator<Cell> cellIterator = row.cellIterator();	
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				
					String cellValue = cell.getStringCellValue();
					if(cellValue != null && cellValue.startsWith(DEADLINE)) {
						cellValue = cellValue.substring(0,8).trim();
					}
					
					switch(cellValue.toLowerCase()) {
					case "portfolio": 
						portfolio.setName(cellIterator.next().getStringCellValue()); 
						break;
					case "restriction must-have":
						Restriction mhRestriction  = new Restriction();
						mhRestriction.setProjectType(new ProjectType(MHP));
						mhRestriction.setTeamCapacityAllocation((Double)(cellIterator.next().getNumericCellValue() * 100));
						restrictions.add(mhRestriction);	
						break;
						
					case "restriction product":
						Restriction pRestriction  = new Restriction();
						pRestriction.setProjectType(new ProjectType(PP));
						pRestriction.setTeamCapacityAllocation((Double)(cellIterator.next().getNumericCellValue() * 100));
						restrictions.add(pRestriction);
						break;
						
					case "earliest exec month":
						Date startingMonth = cellIterator.next().getDateCellValue();
						executionMonth = new LocalDate(startingMonth);
						break;
						
					case "teams":
						for (int i=0; i< row.getPhysicalNumberOfCells()-1; i++) {
							team = new Team();
							team.setOrder(i);
							team.setName(cellIterator.next().getStringCellValue());
							team.setColor(TeamColor.getTeamColor(i));
							teams.add(team);
						}
						if(teams.size() == 0) {
							throw new WrongFileFormatException("Wrong Excel File Format...");
						}
						break;
						
					case "month": 
						for(int i=0; i<24; i++) {
							row = rowIterator.next();
							Iterator<Cell> capacityCellsIterator = row.cellIterator();
								Date month =  capacityCellsIterator.next().getDateCellValue();
								monthsForEstimation[i] = new LocalDate(month);
								Iterator<Team> teamIterator = teams.iterator();
								while (teamIterator.hasNext()) {
									Team t = teamIterator.next();
                                    System.out.println(t.getName());
										teamCapacity = new TeamCapacity();
										LocalDate currentMonth = new LocalDate(month);
										if(executionMonth == null) {
											executionMonth = new LocalDate(currentMonth);
										}
										teamCapacity.setMonth(currentMonth);
                                    teamCapacity.setManDays(BigDecimal.valueOf(capacityCellsIterator.next().getNumericCellValue()));

										t.getTeamCapacities().add(teamCapacity);
										TeamBucket tBucket = new TeamBucket();
										tBucket.setBucketMonth(new LocalDate(month));
										tBucket.setTeamCapacity(teamCapacity);
										t.addTeamBucket(tBucket);
										tBucket.setTeamName(t.getName());
										
							}
						}
						break;
						
					case "projects":
						int projectsCount = row.getPhysicalNumberOfCells()-1;
						for(int j=0; j < projectsCount; j++) {
							Project p = new Project();
							p.setOrder(j);
							p.setColor(ProjectColor.getProjectColor(j));
							p.setExecutionMonth(executionMonth);
							p.setName(cellIterator.next().getStringCellValue());
							portfolio.addProject(p);
						}
						break;
						
					case "type":
						int index = 0;
						SortedSet<Project> projects = portfolio.getProjects();
						Iterator<Project> projectIterator = projects.iterator();
						while (projectIterator.hasNext()) {
							Project p = projectIterator.next();
							if(colors == null) {
								colors = new String[projects.size()];
							}
							String pType =  cellIterator.next().getStringCellValue();
							p.setProjectType(new ProjectType(getProjectTypeCode(pType), pType));
							index++;
							
						}
						break;
						
					case "priority":
						SortedSet<Project> projectsForPriority = portfolio.getProjects();
						Iterator<Project> projectIterator1 = projectsForPriority.iterator();
						while (projectIterator1.hasNext()) {
							projectIterator1.next().setPriority((int)cellIterator.next().getNumericCellValue());
						}
						break;
						
					case "deadline":
						SortedSet<Project> projectsForDeadline = portfolio.getProjects();
						Iterator<Project> projectIterator2 = projectsForDeadline.iterator();
						while (projectIterator2.hasNext()) {
							Cell dateCell = cellIterator.next();
							if(dateCell.getCellType() == 0) {
								projectIterator2.next().setDeadlineMonth(new LocalDate(dateCell.getDateCellValue()));	
							} else {
								projectIterator2.next().setDeadlineMonth(null);
							}
							
						} 
						break;
						
					case "effort":
						SortedSet<Project> projectsForEffort = portfolio.getProjects();
						if(teams.size() == 0) {
							throw new WrongFileFormatException("Wrong Excel File Format...");
						}
						setUpProjectTeamEffort(projectsForEffort,teams, cellIterator);
						for (int i=0; i< teams.size()-1; i++) {
							row = rowIterator.next();
							Iterator<Cell> effotCellIterator = row.cellIterator();  
							setUpProjectTeamEffort(projectsForEffort, teams, effotCellIterator);
						}
						isInputRead = true;
						break;
						
					default:
			                System.out.println("Invalid Value.");
			                break;
					}
				}
			}
		
		return portfolio;
	}
	public String getProjectCode(String name){
		if (name.equalsIgnoreCase(MUST_HAVE)) {
			return MHP;
		} else {
			return PP;
		}
	}
	
	public Team getTeam(String name, Set<Team> teams) {
		Team team = null;
		Iterator<Team>  teamIterator = teams.iterator();
		while (teamIterator.hasNext()) {
			 team = teamIterator.next();
			if (team.getName().equalsIgnoreCase(name)) {
				return team;
			}
		}
		return team;
	}
	
	public void setUpProjectTeamEffort(Set<Project> projects,SortedSet<Team> teams, Iterator<Cell> cellIterator) {
		String teamName = cellIterator.next().getStringCellValue();
		Iterator<Project> projectIterator = projects.iterator();
		while (projectIterator.hasNext()) {
			TeamEffort teamEffort = new TeamEffort();
			Team team = getTeam(teamName, teams);
			teamEffort.setTeamEffortForProject((double)cellIterator.next().getNumericCellValue());
			team.addTeamEffort(teamEffort);
			Project p =projectIterator.next();
			p.addTeamEffort(teamEffort);
			p.addTeam(team);
			team.addProject(p);
		}
	}
	

	public ProjectPortfolio processProjectPortfolio(XSSFWorkbook workbook) throws Exception {
		ProjectPortfolio projectPortfolio = readPortfolio(workbook);
		SortedSet<Project> projects = projectPortfolio.getProjects();
		Set<Team> teams = projectPortfolio.getTeams();
		Set<Restriction> restrictions = projectPortfolio.getRestrictions();
		Restriction mhpRestriction = getMHPRestriction(restrictions);
		Restriction ppRestriction = getPPRestriction(restrictions);
		Map<String,SortedSet<Project>> projectTypes = getProjectsTypes(projects);
		SortedSet<Project> mhProjects = projectTypes.get(MHP);
		SortedSet<Project> pProjects  = projectTypes.get(PP);
		SortedSet<Project> sProjects  = projectTypes.get(SP);
		
		// start with Must-Have & Product projects...
		
		SortedSet<Project> mhANDpProjects = new TreeSet<Project>();
		mhANDpProjects.addAll(mhProjects);
		mhANDpProjects.addAll(pProjects);
		
		// Get the latest deadline month.
		LocalDate latestDeadline = getLatestDeadline(mhANDpProjects);
		
		if(mhANDpProjects != null && mhANDpProjects.size() != 0) {
			Iterator<Team> teamIterator = teams.iterator();
			while (teamIterator.hasNext()) {
				BigDecimal accumulatedTeamWork = new BigDecimal(0.0) ;
				Team team = teamIterator.next();
				List<Project> teamEngagedProjects = team.getTeamEngagedProjects();
				SortedSet<TeamCapacity> teamCapacities = team.getTeamCapacities();
				SortedSet<TeamBucket> teamBuckets = team.getTeamBuckets();
				
				
				
				// Iterate over all the team capacities starting from the latest deadline month to starting month..
				
				TeamCapacity tailTCapacity = getTeamMonthCapacity(teamCapacities, latestDeadline);
				SortedSet<TeamCapacity> tcSortedSet = teamCapacities.headSet(tailTCapacity);
				SortedSet<TeamCapacity> engagedTeamCapacitySet = new TreeSet<TeamCapacity>();
				engagedTeamCapacitySet.addAll(tcSortedSet);
				engagedTeamCapacitySet.add(tailTCapacity);
				TeamCapacity[] engagedTCapacities = new TeamCapacity[engagedTeamCapacitySet.size()];
				engagedTCapacities = engagedTeamCapacitySet.toArray(engagedTCapacities);
				for(int i=engagedTCapacities.length-1; i>=0 ; i--) {
					double sharedCapacityForProject = 0.0d;
					List<Project> monthProjects = null;
					// Get the projects on that month (may be deadline)...
					
					monthProjects = getMonthlyProjects(engagedTCapacities[i].getMonth(), mhANDpProjects, teamEngagedProjects, team);
					if(monthProjects.size() == 0) continue;
					
					// share the team capacity for the projects team working on this month...
					
					
					// Get the TeamBucket for that month.
					TeamBucket tBucket = getTeamMonthBucket(teamBuckets, engagedTCapacities[i].getMonth());
					
					// Get the Team Capacity for that month.
					TeamCapacity tCapacity = tBucket.getTeamCapacity();
					
					int projectsCountForMonth = monthProjects.size();
					tCapacity.setNoOfunUsedCapacitySharedProjects(projectsCountForMonth);
					
					// share the team capacity for each project on this month.
					
					if(projectsCountForMonth > 1) {
						// Assign team work for each project based on restriction applied.
						Iterator<Project> pIterator = monthProjects.iterator();
						while(pIterator.hasNext()) {
							Project p = pIterator.next();
								sharedCapacityForProject = tCapacity.getManDays().doubleValue() / projectsCountForMonth;	
							if(p.getProjectType().getProjectTypeCode().equals(MHP)) {
								Double teamWorkForProject = sharedCapacityForProject * mhpRestriction.getTeamCapacityAllocation() /100;
								ProjectTeamWork ptWork = new ProjectTeamWork();
								addTeamWorkToBucket(teamWorkForProject, ptWork, p, tBucket, team,accumulatedTeamWork, monthProjects, sharedCapacityForProject, tCapacity, mhANDpProjects);
							}
							
							if(p.getProjectType().getProjectTypeCode().equals(PP)) {
								Double teamWorkForProject = sharedCapacityForProject* ppRestriction.getTeamCapacityAllocation() /100;
								ProjectTeamWork ptWork = new ProjectTeamWork();
								addTeamWorkToBucket(teamWorkForProject, ptWork, p, tBucket, team, accumulatedTeamWork, monthProjects, sharedCapacityForProject, tCapacity, mhANDpProjects);
							}
						}
					} else {
						Iterator<Project> pIterator = monthProjects.iterator();
						while(pIterator.hasNext()) {
							Project p = pIterator.next();
							if(p.getProjectType().getProjectTypeCode().equals(MHP)) {
								sharedCapacityForProject = tCapacity.getManDays().doubleValue();
								double teamWorkForProject = tCapacity.getManDays().doubleValue() * mhpRestriction.getTeamCapacityAllocation() /100;
								ProjectTeamWork ptWork = new ProjectTeamWork();
								addTeamWorkToBucket(teamWorkForProject, ptWork, p, tBucket, team, accumulatedTeamWork, monthProjects, 0, tCapacity,mhANDpProjects);
							}
							if(p.getProjectType().getProjectTypeCode().equals(PP)) {
								double teamWorkForProject = tCapacity.getManDays().doubleValue() * ppRestriction.getTeamCapacityAllocation() /100;
								ProjectTeamWork ptWork = new ProjectTeamWork();
								addTeamWorkToBucket(teamWorkForProject, ptWork, p, tBucket, team, accumulatedTeamWork, monthProjects, 0, tCapacity, mhANDpProjects);
							}
						}
					}
				} // for loop
				
				List<ProjectTeamWork> ptworks = team.getMissingTeamCapacity().getProjectTeamWorks();
				if(ptworks.size() > 0) {
					Double overflowWork = 0.00d;
					Iterator<ProjectTeamWork> ptIterator  = ptworks.iterator();
					while(ptIterator.hasNext()) {
						overflowWork = overflowWork + ptIterator.next().getEffort().doubleValue();
					}
					team.setOverflowWork(new BigDecimal(overflowWork.toString(), new MathContext(4)));
				}
				
			} // team iterator
		}
		
		// Get the Prioritized Strategic projects...
		SortedSet<Project> prioritizedProjects = getPrioritizedStrategicProjects(sProjects);
		Iterator<Project> prioritizedProjectIterator =  prioritizedProjects.iterator();
		while(prioritizedProjectIterator.hasNext()) {
			Project p = prioritizedProjectIterator.next();
			SortedSet<Team> projectTeams = p.getTeams();
			Iterator<Team> projectTeamIterator = projectTeams.iterator();
			while(projectTeamIterator.hasNext()) {
				Team pTeam = projectTeamIterator.next();
				pTeam.setCompletedProjectWork(false);
				BigDecimal accumulatedTeamWork = new BigDecimal(0) ;
				Iterator<TeamCapacity> capacityIterator = pTeam.getTeamCapacities().iterator();
					while (capacityIterator.hasNext()) {
						TeamCapacity tCapacity = capacityIterator.next();
						TeamBucket tBucket = getTeamMonthBucket(pTeam.getTeamBuckets(),tCapacity.getMonth());
						if(pTeam.hasCompletedProjectWork() == false) {
							addTeamWorkToBucketForSProject(p, tBucket, tCapacity,pTeam, accumulatedTeamWork, prioritizedProjects);
						} else break;
					}	
			}
		}
		
		
		projectPortfolio.stringPortfolio();
		
		// Write algorithm output into Excel workbook.
//		writeToWorkBook(projectPortfolio);
		
		
		// Set the teams with team buckets back to ProjectPortfolio
		return projectPortfolio;
	}
	
	public void addTeamWorkToBucketForSProject(Project p, TeamBucket tBucket, TeamCapacity tCapacity,
			Team pTeam, BigDecimal accumulatedTeamWork, SortedSet<Project> prioritizedProjects) throws Exception {
		// Get the TeamEffort for this project...
		TeamEffort te = getProjectTeamEffort(p, pTeam);
		
		// Get the accumulated work by this team to thsi project...
		SortedSet<TeamBucket>  filledBuckets = getFilledBuckets(pTeam.getTeamBuckets());
		
		// get accumulated team work for this project by this team.
		accumulatedTeamWork = getAccumulatedTeamWork(filledBuckets, p, pTeam);
		BigDecimal projectTeamEffort = BigDecimal.valueOf(te.getTeamEffortForProject());
		BigDecimal outstandingTeamWorkForProject = projectTeamEffort.subtract(accumulatedTeamWork);
		BigDecimal teamWorkforProjectInMonth = tBucket.getOutstandingTeamWork();
		ProjectTeamWork ptWork = new ProjectTeamWork();
		if(teamWorkforProjectInMonth.doubleValue() == 0) {
			if(!isTeamCapactiyFullyAllocated(tBucket)){
				teamWorkforProjectInMonth = tBucket.getTeamCapacity().getManDays();
			}
		}
		
		// Get the accumulated team work for a Team Bucket..
		if(outstandingTeamWorkForProject.doubleValue() > teamWorkforProjectInMonth.doubleValue()) {
			 ptWork.setEffort(teamWorkforProjectInMonth);
			 ptWork.setProject(p);
				p.addProjectTeamWork(ptWork);
				ptWork.setProjectName(p.getName());
				ptWork.setTeamName(pTeam.getName());
				ptWork.setMonth(tBucket.getBucketMonth());
				tBucket.addProjectTeamWork(ptWork);
				tBucket.setOutstandingTeamWork(BigDecimal.valueOf(0));	
		} else {
			tBucket.setOutstandingTeamWork(teamWorkforProjectInMonth.subtract(outstandingTeamWorkForProject));
			 ptWork.setEffort(outstandingTeamWorkForProject);
			 ptWork.setProject(p);
				p.addProjectTeamWork(ptWork);
				ptWork.setProjectName(p.getName());
				ptWork.setTeamName(pTeam.getName());
				ptWork.setMonth(tBucket.getBucketMonth());
				tBucket.addProjectTeamWork(ptWork);
				
				// Compute the Dead line Month...
				filledBuckets = getFilledBuckets(pTeam.getTeamBuckets());
				accumulatedTeamWork = getAccumulatedTeamWork(filledBuckets, p, pTeam);
				outstandingTeamWorkForProject = projectTeamEffort.subtract(accumulatedTeamWork);
				if(outstandingTeamWorkForProject.doubleValue() == 0) {
					p.setDeadlineMonth(tBucket.getBucketMonth());
					pTeam.setCompletedProjectWork(true);
				}
		}
	}
	
	public TeamEffort getProjectTeamEffort(Project p, Team team) {
		Iterator<TeamEffort> teamEffortIterator = p.getTeamEfforts().iterator();
		while(teamEffortIterator.hasNext()) {
			TeamEffort tEffort = 	teamEffortIterator.next();
			if(tEffort.getTeam().getName().equals(team.getName())) {
				return tEffort;
			}
		}
		return null;
	}
	
	public BigDecimal getAccumulatedTeamWorkForBucket (TeamBucket bucket) {
		double work = 0;
		Iterator<ProjectTeamWork> ptwIterator = bucket.getProjectTeamWorks().iterator();
		while(ptwIterator.hasNext()) {
			ProjectTeamWork ptWork = ptwIterator.next();
			work = work + ptWork.getEffort().doubleValue();
//			accumulatedTeamWork.add(ptWork.getEffort());
		}
		return BigDecimal.valueOf(work);
	}
	
	
	public BigDecimal getAccumulatedTeamWork(SortedSet<TeamBucket> filledBuckets,Project p, Team pTeam) {
		BigDecimal accumulatedTeamWork = new BigDecimal(0.0);
		Iterator<TeamBucket> tbIterator = filledBuckets.iterator();
		while (tbIterator.hasNext()) {
			TeamBucket tb = tbIterator.next();
			ProjectTeamWork currentProjectTWork = getProjectTeamWork(tb,p,pTeam);
			if(currentProjectTWork != null) {
				accumulatedTeamWork = accumulatedTeamWork.add(currentProjectTWork.getEffort());
			}
		}
		return accumulatedTeamWork;
	}
	
	public Map<String, SortedSet<Project>> getProjectsTypes(SortedSet<Project> teamProjects) {
		Map<String, SortedSet<Project>> projectTypes = new Hashtable<String, SortedSet<Project>>(0);
		SortedSet<Project> mProjects = new TreeSet<Project>(), pProjects = new  TreeSet<Project>(Project.priorityComparator), sProjects =  new TreeSet<Project>(Project.priorityComparator);
		Iterator<Project> projectsIterator = teamProjects.iterator();
		while (projectsIterator.hasNext()) {
			Project project = projectsIterator.next();
			if(project.getProjectType().getProjectTypeCode().equals(MHP)) {
				mProjects.add(project);
			} else if(project.getProjectType().getProjectTypeCode().equals(PP)) {
				pProjects.add(project);
			} else if(project.getProjectType().getProjectTypeCode().equals(SP)) {
				sProjects.add(project);
			}
		}
		projectTypes.put(MHP, mProjects);
		projectTypes.put(PP, pProjects);
		projectTypes.put(SP, sProjects);
		
		return projectTypes;
	}
	
	public TeamCapacity getTeamMonthCapacity(SortedSet<TeamCapacity> teamCapacities, LocalDate deadlineMonth) {
		Iterator<TeamCapacity> tcIterator = teamCapacities.iterator();
		TeamCapacity monthTeamtCapacity = null;
		while (tcIterator.hasNext()) {
			TeamCapacity tc = tcIterator.next();
			if((tc.getMonth().getMonthOfYear() ==  deadlineMonth.getMonthOfYear()) && (tc.getMonth().getYear() ==  deadlineMonth.getYear())) {
				monthTeamtCapacity = tc; 
				break;
			}
		}
		return monthTeamtCapacity;
	}
	
	public TeamBucket getTeamMonthBucket(SortedSet<TeamBucket> teamBuckets, LocalDate deadlineMonth) {
		Iterator<TeamBucket> tbIterator = teamBuckets.iterator();
		TeamBucket teamMonthtBucket= null;
		while (tbIterator.hasNext()) {
			TeamBucket tb = tbIterator.next();
			/*if((tb.getBucketMonth().getMonthOfYear() ==  deadlineMonth.getMonthOfYear()) && (tb.getBucketMonth().getYear() ==  deadlineMonth.getYear())) {
				teamMonthtBucket = tb; 
			}*/
			if(tb.getBucketMonth().equals(deadlineMonth)){
				teamMonthtBucket = tb;
				break;
			}
		}
		return teamMonthtBucket;
	}
	
	public int findProjectsWithSameDeadline(Set<Project> mhProjects, Set<Project> pProjects, LocalDate deadlineMonth) {
		int count = 0;
		Iterator<Project> mhIterator = mhProjects.iterator();
		while (mhIterator.hasNext()) {
			Project mhp = mhIterator.next(); 
			if((mhp.getDeadlineMonth().getMonthOfYear() == deadlineMonth.getMonthOfYear()) && (mhp.getDeadlineMonth().getYear() ==  deadlineMonth.getYear())) {
				count++;
			}
		}
		Iterator<Project> ppIterator = pProjects.iterator();
		while (ppIterator.hasNext()) {
			Project pp = ppIterator.next(); 
			if((pp.getDeadlineMonth().getMonthOfYear() == deadlineMonth.getMonthOfYear()) && (pp.getDeadlineMonth().getYear() ==  deadlineMonth.getYear())) {
				count++;
			}
		}
		return count;
		
	}
	
	public Restriction getMHPRestriction(Set<Restriction> restrictions) {
		Restriction mhpRestriction = null;
		Iterator<Restriction> rtIterator = restrictions.iterator();
		while(rtIterator.hasNext()) {
			Restriction rt = rtIterator.next();
			if(rt.getProjectType().getProjectTypeCode().equals(MHP)) {
				mhpRestriction = rt;
			}
		}
		return mhpRestriction;
	}
	
	public Restriction getPPRestriction(Set<Restriction> restrictions) {
		Restriction ppRestriction = null;
		Iterator<Restriction> rtIterator = restrictions.iterator();
		while(rtIterator.hasNext()) {
			Restriction rt = rtIterator.next();
			if(rt.getProjectType().getProjectTypeCode().equals(PP)) {
				ppRestriction = rt;
			}
		}
		return ppRestriction;
	}
	
	public Restriction getRestriction(Set<Restriction> restrictions, ProjectType ptype) {
		Restriction Restriction = null;
		Iterator<Restriction> rtIterator = restrictions.iterator();
		while(rtIterator.hasNext()) {
			Restriction rt = rtIterator.next();
			if(rt.getProjectType().getProjectTypeCode().equals(ptype.getProjectTypeCode())) {
				Restriction = rt;
			}
		}
		return Restriction;
	}
	
	public LocalDate getLatestDeadline(SortedSet<Project> mhANDpProjects){
		LocalDate lastestDeadlineMonth = null;
        List<LocalDate> deadLinesList = new ArrayList<>();
		LocalDate[] deadlines = new LocalDate[mhANDpProjects.size()];
		int index = 0;
		Iterator<Project> projectsIterator = mhANDpProjects.iterator();
		while(projectsIterator.hasNext()) {
            LocalDate deadlineMonth = projectsIterator.next().getDeadlineMonth();

            if(deadlineMonth != null) {
                deadLinesList.add(deadlineMonth);
                deadlines[index] = deadlineMonth;
            }
			index++;
		}

        Collections.sort(deadLinesList,(o1, o2) -> {

            System.out.println("d1 " +o1.toString("MMM YY"));
            System.out.println("d2 " +o2.toString("MMM YY"));
            if(o1.getYear() == o2.getYear() && o1.getMonthOfYear() == o2.getMonthOfYear()){
                return 0;
            }
            return o1.compareTo(o2);
        });
        return deadLinesList.get(deadLinesList.size() - 1);
	}
	
	public List<Project> getMonthlyProjects(LocalDate month, SortedSet<Project> mhANDpProjects, List<Project> teamEngagedProjects, Team team){
		SortedSet<Project> sortedMHANDPProjects = new TreeSet<Project>(Project.priorityComparator);
		SortedSet<Project> mhProjects = new TreeSet<Project>();
		SortedSet<Project> pProjects = new TreeSet<Project>();
		Iterator<Project> iterator = mhANDpProjects.iterator();
		boolean isTeamWorkDoneForThisProject = false;
		while(iterator.hasNext()) {
			Project project = iterator.next();
			for (Map.Entry<String, Boolean> entry : team.getDoneWithProject().entrySet()) {
				if(entry.getKey().equals(project.getName()) && entry.getValue() == true){
					isTeamWorkDoneForThisProject = true;
					teamEngagedProjects.remove(project);
					break;
				} else {
					isTeamWorkDoneForThisProject = false;
				}
			}
			if(!isTeamWorkDoneForThisProject) {
				if(project.getDeadlineMonth() != null && project.getDeadlineMonth().equals(month)){

                    String projectTypeCode = project.getProjectType().getProjectTypeCode();

                    if(projectTypeCode == "MHP") mhProjects.add(project);
					if(projectTypeCode == "PP") pProjects.add(project);
				}
			}
		}
		if(mhProjects.size() > 0) teamEngagedProjects.addAll(mhProjects);
		if(pProjects.size() > 0) teamEngagedProjects.addAll(pProjects);
		
		sortedMHANDPProjects.addAll(teamEngagedProjects);
		Project[] projectArray = new Project[sortedMHANDPProjects.size()];
		teamEngagedProjects = Arrays.asList(sortedMHANDPProjects.toArray(projectArray)); 
		return teamEngagedProjects;
	}
	
	public void addTeamWorkToBucket(double teamWorkForProject, ProjectTeamWork ptWork, Project p, TeamBucket tBucket, Team team, BigDecimal accumulatedTeamWork, List<Project> monthProjects,
			double sharedCapacityForProject, TeamCapacity tCapacity, SortedSet<Project> mhANDpProjects) throws Exception{
		TeamEffort te = null;
		double actualEffort = calculateTeamWorkOnCapacity(teamWorkForProject, p, team, tBucket, accumulatedTeamWork,monthProjects, sharedCapacityForProject, tCapacity);

        System.out.println("aufwand:" +actualEffort);
        ptWork.setEffort(BigDecimal.valueOf(actualEffort));
		ptWork.setProject(p);
		ptWork.setProjectName(p.getName());
		ptWork.setTeamName(team.getName());
		ptWork.setMonth(tBucket.getBucketMonth());
		p.addProjectTeamWork(ptWork);
		tBucket.addProjectTeamWork(ptWork);
		// Set bucket outstanding team work for the next projects assignments...
		if(tBucket.getProjectTeamWorks().size() == monthProjects.size()) {
			tBucket.setOutstandingTeamWork(tBucket.getTeamCapacity().getManDays().subtract(getAccumulatedTeamWorkForBucket(tBucket)));		
		}
		
		// overflow capacity
		if(tBucket.getBucketMonth().equals(p.getExecutionMonth())) {
			te = getProjectTeamEffort(p, team);
			BigDecimal projectTeamEffort = BigDecimal.valueOf(te.getTeamEffortForProject());
			TeamBucket bucket = team.getMissingTeamCapacity();
			 ProjectTeamWork missingPTW = new ProjectTeamWork();
			 missingPTW.setProjectName(p.getName());
			 missingPTW.setEffort(projectTeamEffort.subtract(getAccumulatedTeamWork(getFilledBuckets(team.getTeamBuckets()), p, team)));
			 missingPTW.setProject(p);
			 bucket.addProjectTeamWork(missingPTW);
		}
		
		// Check if oustanding work for team is zero, if yes, remove this project from the team working list.
		te = getProjectTeamEffort(p, team);
		SortedSet<TeamBucket>  filledBuckets = getFilledBuckets(team.getTeamBuckets());
		// get accumulate team work for that project by this team.
		accumulatedTeamWork = getAccumulatedTeamWork(filledBuckets, p, team);
		BigDecimal projectTeamEffort = BigDecimal.valueOf(te.getTeamEffortForProject());
		BigDecimal outstandingTeamWorkForProject = projectTeamEffort.subtract(accumulatedTeamWork);
		if(outstandingTeamWorkForProject.doubleValue() == 0) 	{
			team.getDoneWithProject().put(p.getName(), true);
			team.setDoneWithProject(team.getDoneWithProject());
		}
	}
	
	public String getProjectTypeCode(String pType){
		if(pType.equalsIgnoreCase(MUST_HAVE)) {
			return MHP;	
		}
		if(pType.equalsIgnoreCase(PRODUCT)) {
			return PP;
		}
		if(pType.equalsIgnoreCase(STRATEGIC)) {
			return SP;	
		}
		return null;
	}
	
	public double calculateTeamWorkOnCapacity(double teamWorkforProjectInMonth, Project p, Team team, TeamBucket tBucket, BigDecimal accumulatedTeamWork, List<Project> monthProjects,
			double sharedCapacityForProject, TeamCapacity tCapacity) throws Exception{
		TeamEffort te = getProjectTeamEffort(p, team);
		SortedSet<TeamBucket>  filledBuckets = getFilledBuckets(team.getTeamBuckets());
		// get accumulate team work for that project by this team.
		accumulatedTeamWork = getAccumulatedTeamWork(filledBuckets, p, team);
		BigDecimal projectTeamEffort = BigDecimal.valueOf(te.getTeamEffortForProject());
		BigDecimal outstandingTeamWorkForProject = projectTeamEffort.subtract(accumulatedTeamWork);
		if(outstandingTeamWorkForProject.doubleValue() == 0) 	{
			tCapacity.setNoOfunUsedCapacitySharedProjects(tCapacity.getNoOfunUsedCapacitySharedProjects() - 1);
			tCapacity.setUnUsedSharedCapacity(tCapacity.getUnUsedSharedCapacity() + getUnUsedCapacity(outstandingTeamWorkForProject, sharedCapacityForProject,p));
			return 0;
		}
		if (outstandingTeamWorkForProject.doubleValue() > teamWorkforProjectInMonth) {
			if(p.getProjectType().getProjectTypeCode() == "MHP") {
				double maxAllocatedCapacity = (getRestriction(p.getProjectPortfolio().getRestrictions(), p.getProjectType()).getTeamCapacityAllocation() / 100) * tCapacity.getManDays().doubleValue(); 
				if((tBucket.getProjectTeamWorks().size() + 1) == monthProjects.size() && outstandingTeamWorkForProject.doubleValue() < maxAllocatedCapacity) {
					return outstandingTeamWorkForProject.doubleValue();
				} 	
			}
			
			if(tCapacity.getUnUsedSharedCapacity() != 0) {
				teamWorkforProjectInMonth = teamWorkforProjectInMonth + (tCapacity.getUnUsedSharedCapacity() /tCapacity.getNoOfunUsedCapacitySharedProjects());
				tCapacity.setNoOfunUsedCapacitySharedProjects(tCapacity.getNoOfunUsedCapacitySharedProjects() - 1);
				if(tCapacity.getNoOfunUsedCapacitySharedProjects() != 0) {
					tCapacity.setUnUsedSharedCapacity(tCapacity.getUnUsedSharedCapacity() - (tCapacity.getUnUsedSharedCapacity() /tCapacity.getNoOfunUsedCapacitySharedProjects()));
				}
				return teamWorkforProjectInMonth;
			}
			return teamWorkforProjectInMonth;
		} else {
			tCapacity.setNoOfunUsedCapacitySharedProjects(tCapacity.getNoOfunUsedCapacitySharedProjects() - 1);
			tCapacity.setUnUsedSharedCapacity(tCapacity.getUnUsedSharedCapacity() + getUnUsedCapacity(outstandingTeamWorkForProject, sharedCapacityForProject,p));
			return outstandingTeamWorkForProject.doubleValue(); 
		}
	}
	
	public SortedSet<TeamBucket> getFilledBuckets(SortedSet<TeamBucket> buckets) {
		Iterator<TeamBucket> iterator = buckets.iterator();
		SortedSet<TeamBucket> filledBuckets = new TreeSet<TeamBucket>();
		while(iterator.hasNext()) {
			TeamBucket tBucket = iterator.next();
			if(tBucket.getProjectTeamWorks().size() > 0){
				filledBuckets.add(tBucket);
			}
		}
		return filledBuckets;
		
	}
	
	public ProjectTeamWork getProjectTeamWork(TeamBucket bucket, Project p, Team team) {
		ProjectTeamWork ptw = null;
		
			Iterator<ProjectTeamWork> ptwIterator = bucket.getProjectTeamWorks().iterator();
			while(ptwIterator.hasNext()) {
				ProjectTeamWork ptWork = ptwIterator.next();
				if(ptWork.getProject().getName().equals(p.getName()) && ptWork.getTeamBucket().getTeam().getName().equals(team.getName())) {
					ptw = ptWork; 
					break;
				}
			}
		
		return ptw;
	}
	public SortedSet<Project> getPrioritizedStrategicProjects(SortedSet<Project> sProjects) {
		SortedSet<Project> sortedSPs = new TreeSet<Project>(Project.priorityComparator);
		sortedSPs.addAll(sProjects);
		return sortedSPs; 
	}
	
	public double getUnUsedCapacity(BigDecimal outstandingTeamWorkForProject, double sharedCapacityForProject, Project p) {
		double unUsedCapacity = 0.0d;
		Iterator<Restriction> restrictionIterator = p.getProjectPortfolio().getRestrictions().iterator();
		while(restrictionIterator.hasNext()) {
			Restriction restriction = restrictionIterator.next();
			if(restriction.getProjectType().getProjectTypeCode().equalsIgnoreCase(p.getProjectType().getProjectTypeCode())) {
				double percentageCapacityUsed = (outstandingTeamWorkForProject.doubleValue() * 100) / sharedCapacityForProject;		
				unUsedCapacity = sharedCapacityForProject * (restriction.getTeamCapacityAllocation().doubleValue() - percentageCapacityUsed) / 100;
				break;
			}
		}
		return unUsedCapacity;
	}
	
	public boolean isTeamCapactiyFullyAllocated(TeamBucket tBucket) {
		
		double accumulatedWork = 0.0d;
		if(tBucket.getProjectTeamWorks().size() == 0) return false;
		Iterator<ProjectTeamWork> iterator = tBucket.getProjectTeamWorks().iterator();
		while(iterator.hasNext()) {
			ProjectTeamWork ptWork = iterator.next();
			accumulatedWork = accumulatedWork + ptWork.getEffort().doubleValue();
		}
		return (accumulatedWork == tBucket.getTeamCapacity().getManDays().doubleValue()) ? true : false;
	}
	
	public boolean writeToWorkBook(ProjectPortfolio projectPortfolio, XSSFWorkbook portflioWorkBook)  {
		boolean writeToExcel = false;
		FileOutputStream fileOut = null;
		BufferedInputStream bIn = null;
		XSSFSheet inputSheet = null;
		Row row = null;
		XSSFRow newRow = null;
		Cell cell = null;
		String cellValue = null;
		int rowNumber = 0;
		LocalDate[] monthsForEstimation= projectPortfolio.getMonthsForEstimation();
		Iterator<Team> teamIterator = null;
		Iterator<Project> projectIterator = null;
		boolean isFileUnlocked = false;
		try {
			inputSheet = portflioWorkBook.getSheetAt(0);
			Iterator<Row> rowIterator = inputSheet.iterator();
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				cell = row.getCell(0);
				if(cell != null) {
					if(cell.getCellType() ==  0){
						cellValue = cell.getDateCellValue().toString();
					} else cellValue = cell.getStringCellValue();
				} else continue;
				cellValue = cellValue.toLowerCase();
				if(!cellValue.equals("effort")) {
					continue;
				} else {
					rowNumber =  row.getRowNum();
					newRow = inputSheet.createRow(rowNumber + projectPortfolio.getTeams().size() + 1);
					newRow.createCell(0).setCellValue("OUTPUT");
					// Place the Projects...
					newRow = inputSheet.createRow(newRow.getRowNum() + 1);
					newRow.createCell(0).setCellValue("");
					projectIterator = projectPortfolio.getProjects().iterator();
					int columnIndex = 1;
					while(projectIterator.hasNext()) {
						Project p = projectIterator.next();
						newRow.createCell(columnIndex).setCellValue(p.getName());;
						columnIndex = columnIndex +  projectPortfolio.getTeams().size();
					}
					
					newRow = inputSheet.createRow(newRow.getRowNum() + 1);
					newRow.createCell(0).setCellValue("month");
					projectIterator = projectPortfolio.getProjects().iterator();
					columnIndex = 1;
					while(projectIterator.hasNext()) {
						Project project = projectIterator.next();
						teamIterator = projectPortfolio.getTeams().iterator();
						while(teamIterator.hasNext()) {
							Team team = teamIterator.next();
							newRow.createCell(columnIndex).setCellValue(team.getName());;
							columnIndex++; 
						}
					}
					
					// Add Team Work..
					for(int i = 0; i < monthsForEstimation.length; i++) {
						newRow = inputSheet.createRow(newRow.getRowNum() + 1);
						newRow.createCell(0).setCellValue(monthsForEstimation[i].toString());
						projectIterator = projectPortfolio.getProjects().iterator();
						columnIndex = 1; 
						while(projectIterator.hasNext()) {
							Project project = projectIterator.next();
							teamIterator = projectPortfolio.getTeams().iterator();
							while(teamIterator.hasNext()) {
								Team team = teamIterator.next();
								TeamBucket tBucket = getTeamMonthBucket(team.getTeamBuckets(), monthsForEstimation[i]);
								ProjectTeamWork ptWork = getProjectTeamWork(tBucket, project, team);
								if(ptWork != null) {
									newRow.createCell(columnIndex).setCellValue(ptWork.getEffort().doubleValue());
								}
								columnIndex++;
							}
						}
					}
					
					// Add Overflow...
					newRow = inputSheet.createRow(newRow.getRowNum() + 1);
					newRow.createCell(0).setCellValue("Overflow");
					teamIterator = projectPortfolio.getTeams().iterator();
					 columnIndex = 1;
					while(teamIterator.hasNext()) {
						Team team = teamIterator.next();
						newRow.createCell(columnIndex).setCellValue(team.getName());
						columnIndex++;
					}
					newRow = inputSheet.createRow(newRow.getRowNum() + 1);
					newRow.createCell(0).setCellValue("");
					teamIterator = projectPortfolio.getTeams().iterator();
					columnIndex = 1;
					while(teamIterator.hasNext()) {
						Team team = teamIterator.next();
						newRow.createCell(columnIndex).setCellValue(team.getOverflowWork().doubleValue());
						columnIndex++;
					}
					newRow = inputSheet.createRow(newRow.getRowNum() + 1);
					
					// Add Projects DeadLines...
					newRow = inputSheet.createRow(newRow.getRowNum() + 1);
					newRow.createCell(0).setCellValue("projects");
					projectIterator = projectPortfolio.getProjects().iterator();
					columnIndex = 1;
					while(projectIterator.hasNext()) {
						Project project = projectIterator.next();
						newRow.createCell(columnIndex).setCellValue(project.getName());
						columnIndex++;
					}
					newRow = inputSheet.createRow(newRow.getRowNum() + 1);
					newRow.createCell(0).setCellValue("deadline (last month)");
					projectIterator = projectPortfolio.getProjects().iterator();
					columnIndex = 1;
					while(projectIterator.hasNext()) {
						Project project = projectIterator.next();
						newRow.createCell(columnIndex).setCellValue(project.getDeadlineMonth().toString());
						columnIndex++;
					}
					
					break;
				}
			}
			
			String userHomeDir = System.getProperty("user.home");
			String fileName = userHomeDir + "\\Desktop\\workbook.xls";
			File existingFile = new File(fileName);
			
			try {
			org.apache.commons.io.FileUtils.touch(existingFile);
			isFileUnlocked = true;
			
			} catch(IOException ioe) {
				isFileUnlocked = false;
			}
			if(isFileUnlocked) {
				fileOut = new FileOutputStream(existingFile);
				portflioWorkBook.write(fileOut);
			    fileOut.close();
			}
			writeToExcel = true;
		} catch(Exception e) {
			writeToExcel = false;
			e.printStackTrace();
		}
		return writeToExcel;
	}

}
