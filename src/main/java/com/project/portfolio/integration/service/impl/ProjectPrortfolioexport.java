package com.project.portfolio.integration.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedSet;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.project.portfolio.domain.Project;
import com.project.portfolio.domain.ProjectPortfolio;
import com.project.portfolio.domain.ProjectTeamWork;
import com.project.portfolio.domain.Restriction;
import com.project.portfolio.domain.Team;
import com.project.portfolio.domain.TeamBucket;

public class ProjectPrortfolioexport
{
//  public boolean createBook(ProjectPortfolio pc)
//  {
//    HSSFWorkbook workbook = new HSSFWorkbook();
//    HSSFSheet sheet = workbook.createSheet("Sample sheet");
//    HSSFPalette palette = workbook.getCustomPalette();
//    ArrayList<String> highestbucket = getHighestBucketTeam(pc.getTeams());
//    
//
//
//    Row row = sheet.createRow(0);
//    
//    int cellnum = 0;
//    Cell cellmain = row.createCell(cellnum++);
//    cellmain.setCellValue("");
//    Cell cellmain2 = row.createCell(cellnum++);
//    cellmain2.setCellValue("");
//    for (Iterator iterator = highestbucket.iterator(); iterator.hasNext();)
//    {
//      String dateyear = (String)iterator.next();
//      
//      Cell cell = row.createCell(cellnum++);
//      
//      cell.setCellValue(dateyear);
//    }
//    Cell Overflow = row.createCell(cellnum++);
//    Overflow.setCellValue("Overflow");
//    
//
//
//
//
//
//
//
//
//
//
//
//
//
//    double musthaverectriction = 0.0D;
//    Iterator<Restriction> items2 = pc.getRestrictions().iterator();
//    while (items2.hasNext())
//    {
//      Restriction resc = (Restriction)items2.next();
//      if (resc.getProjectType().getProjectTypeCode() == "MHP") {
//        musthaverectriction = resc.getTeamCapacityAllocation().doubleValue();
//      }
//    }
//    HashMap<String, String> teamcolor = new HashMap();
//    ArrayList<Object> projectTeamWorks = new ArrayList();
//    Iterator peojectiterator;
//    for (Iterator iterator = pc.getTeams().iterator(); iterator.hasNext(); peojectiterator.hasNext())
//    {
//      Team teamval = (Team)iterator.next();
//      
//      peojectiterator = pc.getProjects().iterator(); continue;
//      Project projectentity = (Project)peojectiterator.next();
//      
//      ArrayList<Object> teamProjectWorks = new ArrayList();
//      ArrayList<ArrayList<String>> teamsProjectsWorks = new ArrayList();
//      int countcell = 0;
//      for (Iterator teambucket = teamval.getTeamBuckets().iterator(); teambucket.hasNext();)
//      {
//        countcell++;
//        TeamBucket bucketentity = (TeamBucket)teambucket.next();
//        ArrayList<String> arlsit = new ArrayList();
//        int team_100 = 0;
//        int project_count = 0;
//        int project_musthave = 0;
//        int project_product = 0;
//        boolean isprojectAvailable = false;
//        for (Iterator bucketproject = bucketentity.getProjectTeamWorks().iterator(); bucketproject.hasNext();)
//        {
//          boolean available = false;
//          ProjectTeamWork projecteamwork = (ProjectTeamWork)bucketproject.next();
//          if (projecteamwork.getEffort().intValue() != 0)
//          {
//            team_100 = (int)(team_100 + Math.round(projecteamwork.getEffort().doubleValue() * 100.0D / bucketentity.getTeamCapacity().getManDays()));
//            Iterator pcprodcut = pc.getProjects().iterator();
//            while (pcprodcut.hasNext())
//            {
//              Project projectentity2 = (Project)pcprodcut.next();
//              if (projectentity2.getName() == projecteamwork.getProjectName()) {
//                if (projectentity2.getProjectType().getProjectTypeCode() == "MHP") {
//                  project_musthave++;
//                } else {
//                  project_product++;
//                }
//              }
//            }
//            project_count++;
//          }
//          System.out.println(projecteamwork.getEffort().intValue() + ">>>>>>" + "projecteamwork.getProjectName() >>" + projecteamwork.getProjectName() + "<<<<" + projectentity.getName());
//          if (projecteamwork.getProjectName() == projectentity.getName())
//          {
//            available = true;
//            isprojectAvailable = true;
//          }
//          if (available)
//          {
//            System.out.println("projecteamwork.getEffort().intValue() >>" + projecteamwork.getEffort().intValue());
//            if (projecteamwork.getEffort().intValue() != 0)
//            {
//              arlsit.add(Math.round(projecteamwork.getEffort().doubleValue() * 100.0D / bucketentity.getTeamCapacity().getManDays()) + " %");
//              arlsit.add(projectentity.getName());
//              arlsit.add(bucketentity.getTeamName());
//              arlsit.add(projectentity.getColor());
//              arlsit.add(teamval.getColor());
//            }
//            else
//            {
//              arlsit.add("0 %");
//              arlsit.add(projectentity.getName());
//              arlsit.add(bucketentity.getTeamName());
//              arlsit.add("");
//              arlsit.add(teamval.getColor());
//            }
//            teamsProjectsWorks.add(arlsit);
//          }
//        }
//        if ((team_100 != 100) && (team_100 > 0) && (isprojectAvailable)) {
//          if (project_count != 0)
//          {
//            ArrayList<String> arlistdup = new ArrayList();
//            int index = teamsProjectsWorks.size() - 1;
//            arlistdup = (ArrayList)teamsProjectsWorks.get(index);
//            String prevVal = (String)arlistdup.get(0);
//            System.out.println(">>>>>>" + (String)arlistdup.get(0));
//            if (!((String)arlistdup.get(0)).equals("0 %"))
//            {
//              arlistdup.remove(0);
//              if (projectentity.getProjectType().getProjectTypeCode() == "MHP")
//              {
//                if (team_100 != musthaverectriction)
//                {
//                  if (project_musthave != project_count)
//                  {
//                    double rextrictionval = 100 - 100 / project_count;
//                    arlistdup.add(0, rextrictionval / project_musthave + " %");
//                  }
//                  else
//                  {
//                    arlistdup.add(0, 100 / project_count + " %");
//                  }
//                }
//                else {
//                  arlistdup.add(0, prevVal);
//                }
//              }
//              else {
//                arlistdup.add(0, 100 / project_count + " %");
//              }
//              teamsProjectsWorks.remove(index);
//              teamsProjectsWorks.add(index, arlistdup);
//            }
//          }
//        }
//        System.out.println("------------------zzzz-----------------------------");
//      }
//      teamProjectWorks.add(teamval.getName() + "-" + projectentity.getName() + '-' + teamval.getOverflowWork());
//      teamProjectWorks.add(projectentity.getName());
//      teamProjectWorks.add(teamsProjectsWorks);
//      teamcolor.put(teamval.getName() + "-" + projectentity.getName() + '-' + teamval.getOverflowWork(), teamval.getColor());
//      projectTeamWorks.add(teamProjectWorks);
//    }
//    int rownum = 1;
//    for (Iterator teamProjectWork = projectTeamWorks.iterator(); teamProjectWork.hasNext();)
//    {
//      ArrayList<Object> object = (ArrayList)teamProjectWork.next();
//      System.out.println("object <<<>>>" + object.get(0));
//      cellnum = 0;
//      Row rowz = sheet.createRow(rownum++);
//      HSSFCellStyle style2 = workbook.createCellStyle();
//      System.out.println("teamval.getColor2() >>" + (String)teamcolor.get((String)object.get(0)));
//      int[] rgb1 = getRGB(((String)teamcolor.get((String)object.get(0))).substring(1));
//      
//
//      HSSFColor myColor2 = palette.findSimilarColor(rgb1[0], rgb1[1], rgb1[2]);
//      short palIndex2 = myColor2.getIndex();
//      style2.setFillForegroundColor(palIndex2);
//      style2.setFillPattern((short)1);
//      
//
//      Cell cellTeam = rowz.createCell(cellnum++);
//      cellTeam.setCellValue(((String)object.get(0)).split("-")[0]);
//      cellTeam.setCellStyle(style2);
//      Cell cellProject = rowz.createCell(cellnum++);
//      cellProject.setCellValue((String)object.get(1));
//      
//      ArrayList<Object> columnitems = (ArrayList)object.get(2);
//      int columncount = 1;
//      for (Iterator iterator = columnitems.iterator(); iterator.hasNext();)
//      {
//        ArrayList<String> object2 = (ArrayList)iterator.next();
//        Cell cellvalues = rowz.createCell(cellnum++);
//        if ((object2.size() > 1) && (!((String)object2.get(3)).toString().equals("")))
//        {
//          HSSFCellStyle style = workbook.createCellStyle();
//          int[] rgb = getRGB(((String)object2.get(3)).substring(1));
//          HSSFColor myColor = palette.findSimilarColor(rgb[0], rgb[1], rgb[2]);
//          short palIndex = myColor.getIndex();
//          style.setFillForegroundColor(palIndex);
//          style.setFillPattern((short)1);
//          cellvalues.setCellStyle(style);
//        }
//        System.out.println(columncount++ + ">>object2.get(0) >>" + (String)object2.get(0));
//        cellvalues.setCellValue((String)object2.get(0));
//      }
//      for (int x = columnitems.size(); x < highestbucket.size(); x++)
//      {
//        Cell cellvalues = rowz.createCell(cellnum++);
//        cellvalues.setCellValue("0 %");
//      }
//      Cell cellvaluesoverflow = rowz.createCell(cellnum++);
//      cellvaluesoverflow.setCellValue(((String)object.get(0)).split("-")[2] + " MD");
//      System.out.println(highestbucket.size() + " >>> " + columnitems.size());
//    }
//    Row rowz = sheet.createRow(rownum++);
//    rowz = sheet.createRow(rownum++);
//    rowz = sheet.createRow(rownum++);
//    
//    cellnum = 0;
//    Cell projectName = rowz.createCell(cellnum++);
//    projectName.setCellValue("Project Name");
//    
//    Cell projectColor = rowz.createCell(cellnum++);
//    projectColor.setCellValue("Project Color");
//    
//    Cell projectNamePtype = rowz.createCell(cellnum++);
//    projectNamePtype.setCellValue("Project Type");
//    
//    Cell projectNameExe = rowz.createCell(cellnum++);
//    projectNameExe.setCellValue("Execution Month");
//    
//    Cell projectNamedead = rowz.createCell(cellnum++);
//    projectNamedead.setCellValue("Deadline Month");
//    
//    Cell projectName_over = rowz.createCell(cellnum++);
//    projectName_over.setCellValue("Causing overflow in teams");
//    for (Iterator iterator = pc.getProjects().iterator(); iterator.hasNext();)
//    {
//      Project Projectentity = (Project)iterator.next();
//      cellnum = 0;
//      rowz = sheet.createRow(rownum++);
//      
//      Cell projectloop = rowz.createCell(cellnum++);
//      projectloop.setCellValue(Projectentity.getName());
//      
//      Cell projectloop_1 = rowz.createCell(cellnum++);
//      HSSFCellStyle style = workbook.createCellStyle();
//      int[] rgb = getRGB(Projectentity.getColor().substring(1));
//      HSSFColor myColor = palette.findSimilarColor(rgb[0], rgb[1], rgb[2]);
//      short palIndex = myColor.getIndex();
//      style.setFillForegroundColor(palIndex);
//      style.setFillPattern((short)1);
//      projectloop_1.setCellStyle(style);
//      projectloop_1.setCellValue("");
//      
//
//      Cell projectloop_3 = rowz.createCell(cellnum++);
//      projectloop_3.setCellValue(Projectentity.getProjectType().getName());
//      
//      Cell projectloop_4 = rowz.createCell(cellnum++);
//      
//      SimpleDateFormat sdf = new SimpleDateFormat("MMM YY");
//      String[] teammonth = Projectentity.getExecutionMonth().toString().split("-");
//      Calendar calendar = new GregorianCalendar(Integer.parseInt(teammonth[0]), Integer.parseInt(teammonth[1]), Integer.parseInt(teammonth[2]), 0, 0, 0);
//      projectloop_4.setCellValue(sdf.format(calendar.getTime()));
//      
//      Cell projectloop_5 = rowz.createCell(cellnum++);
//      String[] teammonthdl = Projectentity.getExecutionMonth().toString().split("-");
//      Calendar calendardl = new GregorianCalendar(Integer.parseInt(teammonthdl[0]), Integer.parseInt(teammonthdl[1]), Integer.parseInt(teammonthdl[2]), 0, 0, 0);
//      projectloop_5.setCellValue(sdf.format(calendardl.getTime()));
//      
//
//      String overfloweffort = "";
//      Iterator iterator3;
//      for (Iterator iterator2 = pc.getTeams().iterator(); iterator2.hasNext(); iterator3.hasNext())
//      {
//        Team object = (Team)iterator2.next();
//        
//        iterator3 = object.getProjects().iterator(); continue;
//        Project teamproject = (Project)iterator3.next();
//        if (teamproject.getName() == Projectentity.getName()) {
//          overfloweffort = overfloweffort + teamproject.getName() + " (" + object.getOverflowWork() + ") ,";
//        }
//      }
//      Cell projectloop_6 = rowz.createCell(cellnum++);
//      projectloop_6.setCellValue(overfloweffort.substring(0, overfloweffort.length() - 1));
//    }
//    try
//    {
//      ClassLoader loader = ProjectPrortfolioexport.class.getClassLoader();
//      String path = loader.getResource("com/project/portfolio/integration/service/impl/ProjectPrortfolioexport.class").toString();
//      System.out.println("path >>" + path);
//      String path2 = path.substring(path.indexOf("/") + 1, path.length());
//      System.out.println("path2 >>" + path2);
//      path = path2.substring(0, path2.indexOf("/"));
//      System.out.println("path >>" + path);
//      FileOutputStream out = 
//        new FileOutputStream(new File(path + "\\uploads\\new.xls"));
//      workbook.write(out);
//      out.close();
//      System.out.println("Excel written successfully..");
//    }
//    catch (FileNotFoundException e)
//    {
//      e.printStackTrace();
//    }
//    catch (IOException e)
//    {
//      e.printStackTrace();
//    }
//    return true;
//  }
//  
//  String TeamName = "";
//  
//  private ArrayList<String> getHighestBucketTeam(SortedSet<Team> teams)
//  {
//    ArrayList<String> arlist = new ArrayList();
//    int countval = 0;
//    Team teambuckethighest = null;
//    for (Iterator iterator = teams.iterator(); iterator.hasNext();)
//    {
//      Team team = (Team)iterator.next();
//      if (team.getTeamBuckets().size() > countval)
//      {
//        countval = team.getTeamBuckets().size();
//        this.TeamName = team.getName();
//        teambuckethighest = team;
//      }
//    }
//    for (Iterator iterator = teambuckethighest.getTeamBuckets().iterator(); iterator.hasNext();)
//    {
//      TeamBucket teamz = (TeamBucket)iterator.next();
//      SimpleDateFormat sdf = new SimpleDateFormat("MMM YY");
//      String[] teammonth = teamz.getBucketMonth().toString().split("-");
//      Calendar calendar = new GregorianCalendar(Integer.parseInt(teammonth[0]), Integer.parseInt(teammonth[1]) - 1, Integer.parseInt(teammonth[2]), 0, 0, 0);
//      
//      System.out.println("teamz.getBucketMonth().toString() >>" + teamz.getBucketMonth().toString() + ">>>>>" + sdf.format(calendar.getTime()));
//      arlist.add(sdf.format(calendar.getTime()));
//    }
//    return arlist;
//  }
//  
//  public int[] getRGB(String rgb)
//  {
//    int[] ret = new int[3];
//    for (int i = 0; i < 3; i++) {
//      ret[i] = hexToInt(rgb.charAt(i * 2), rgb.charAt(i * 2 + 1));
//    }
//    return ret;
//  }
//  
//  public int hexToInt(char a, char b)
//  {
//    int x = a < 'A' ? a - '0' : a - '7';
//    int y = b < 'A' ? b - '0' : b - '7';
//    return x * 16 + y;
//  }
}
