package com.backend.dream.controller;

import com.backend.dream.repository.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class ReportController {
  @Autowired
  private Report report;

  private static final int orderStatus = 4;

  @GetMapping("/report")
  public String getReport(Model model) {
    getStatistic(model);

    return "/admin/home/report";
  }

  @GetMapping("/getByDate")
  public String getRevenue(Model model, @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
    getStatistic(model);

    List<Object[]> getRevenueByDateAndStatus = report.getTotalRevenueByDateAndStatus(orderStatus,startDate,endDate);
    model.addAttribute("getRevenueByDateAndStatus", getRevenueByDateAndStatus);

    return "/admin/home/report";
  }


  private void getStatistic(Model model){
    Double revenue = report.getRevenue(orderStatus);
    model.addAttribute("revenue",revenue);

    Double totalOrder = report.getTotalOrder(orderStatus);
    model.addAttribute("totalOrder",totalOrder);

    Integer totalProductHasSold = report.totalProductHasSold(orderStatus);
    model.addAttribute("totalProductHasSold",totalProductHasSold);

    Integer totalAccount = report.totalAccount();
    model.addAttribute("totalAccount",totalAccount);

    List<Object[]> countProductHasSold = report.countProductSold(orderStatus);
    model.addAttribute("countProductHasSold",countProductHasSold);

    List<Object[]> getProductHasSoldByCategory = report.getProductHasSoldByCategory(orderStatus);
    model.addAttribute("productHasSoldByCategory",getProductHasSoldByCategory);

    List<Object[]> getAmountPaidByAccount = report.getAmountPaidByAccount(orderStatus);
    model.addAttribute("getAmountPaidByAccount", getAmountPaidByAccount);

    List<Object[]> dailyRevenue = report.getDailyRevenue(orderStatus);
    model.addAttribute("dailyRevenue", dailyRevenue);

    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_MONTH,-7);

    Calendar calendarLastMonth = Calendar.getInstance();
    calendarLastMonth.add(Calendar.DAY_OF_MONTH,-30);

    Date startDate = calendar.getTime();
    Date startDateLastMonth = calendarLastMonth.getTime();

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    String formattedStartDate = dateFormat.format(startDate);
    String formattedStartDateLastDate = dateFormat.format(startDateLastMonth);

    Double getRevenueWeekAndStatus = report.getTotalRevenueLastWeekAndStatus(orderStatus, startDate);
    model.addAttribute("weeklyRevenue", getRevenueWeekAndStatus);

    Double getRevenueMonthAndStatus = report.getTotalRevenueLastMonthAndStatus(orderStatus, startDateLastMonth);
    model.addAttribute("monthlyRevenue", getRevenueMonthAndStatus);

  }

}
