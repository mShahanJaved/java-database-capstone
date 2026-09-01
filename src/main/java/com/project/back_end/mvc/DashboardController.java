package com.project.back_end.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin/adminDashboard";
    }

    @GetMapping("/doctor/dashboard")
    public String doctorDashboard() {
        return "doctor/doctorDashboard";
    }
}
