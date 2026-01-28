package com.library.seatmanager.controller;

import com.library.seatmanager.dto.DashboardResponse;
import com.library.seatmanager.entity.Student;
import com.library.seatmanager.repository.SeatRepository;
import com.library.seatmanager.repository.StudentRepository;
import com.library.seatmanager.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class DashboardController {

    @Autowired
    private DashboardService service;

    @Autowired
    private SeatRepository seatRepo;

    @Autowired
    private StudentRepository studentRepo;

    @GetMapping("/dashboards")
    public DashboardResponse getDashboard() {

        int totalSeats = (int) seatRepo.count();
//        int filledSeats = (int) seatRepo.countByOccupiedTrue();
        long filledSeats =
                studentRepo.countByStudentTypeAndActiveTrue(Student.StudentType.FULL_DAY);
        long vacantSeats = totalSeats - filledSeats;

        long halfDayCount =
                studentRepo.countByStudentTypeAndActiveTrue(Student.StudentType.HALF_DAY);

        return new DashboardResponse(
                totalSeats,
                filledSeats,
                vacantSeats,
                halfDayCount
        );
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        return service.getDashboardStats();
    }


}

