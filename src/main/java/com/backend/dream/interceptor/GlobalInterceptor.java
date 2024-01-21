package com.backend.dream.interceptor;

import com.backend.dream.dto.NotificationDTO;
import com.backend.dream.entity.Account;
import com.backend.dream.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Component
public class GlobalInterceptor implements HandlerInterceptor {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AccountService accountService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private NotificationService notificationService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        request.setAttribute("categories", categoryService.findAll());
        String remoteUser = request.getRemoteUser();
        Long id_account = accountService.findIDByUsername(remoteUser);
        String fullname = accountService.findFullNameByUsername(remoteUser);
        String avatar = accountService.getImageByUserName(remoteUser);
        List<NotificationDTO> adminNotifications = notificationService.getAdminNotifications();
        List<NotificationDTO> notifications = notificationService.getNotificationsByAccountId(id_account);
        if (modelAndView != null) {
            if (remoteUser != null && request.isUserInRole("ADMIN")) {
                modelAndView.addObject("isAuthenticated", true);
                modelAndView.addObject("isAdminOrStaff", true);
                modelAndView.addObject("isAdmin", true);
                modelAndView.addObject("fullname", fullname);
                modelAndView.addObject("username", remoteUser);
                modelAndView.addObject("id_account", id_account);
                modelAndView.addObject("avatar", avatar);
                modelAndView.addObject("notifications", notifications);
                modelAndView.addObject("adminNotifications", adminNotifications);
            } else if (remoteUser != null &&  request.isUserInRole("STAFF")) {
                modelAndView.addObject("isAuthenticated", true);
                modelAndView.addObject("isAdminOrStaff", true);
                modelAndView.addObject("fullname", fullname);
                modelAndView.addObject("username", remoteUser);
                modelAndView.addObject("id_account", id_account);
                modelAndView.addObject("avatar", avatar);
                modelAndView.addObject("notifications", notifications);
                modelAndView.addObject("adminNotifications", adminNotifications);
            } else if (remoteUser != null) {
                modelAndView.addObject("username", remoteUser);
                modelAndView.addObject("fullname", fullname);
                modelAndView.addObject("id_account", id_account);
                modelAndView.addObject("avatar", avatar);
                modelAndView.addObject("isAuthenticated", true);
                modelAndView.addObject("notifications", notifications);
                modelAndView.addObject("adminNotifications", adminNotifications);
            } else {
                modelAndView.addObject("isAuthenticated", false);
                modelAndView.addObject("isAdminOrStaff", false);
            }
        }
    }
}
