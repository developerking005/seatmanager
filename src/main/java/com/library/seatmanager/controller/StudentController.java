package com.library.seatmanager.controller;

import com.library.seatmanager.dto.StudentTableResponse;
import com.library.seatmanager.dto.StudentUpdateRequest;
import com.library.seatmanager.entity.Seat;
import com.library.seatmanager.entity.Student;
import com.library.seatmanager.repository.SeatRepository;
import com.library.seatmanager.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/student")
@CrossOrigin
public class StudentController {

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private SeatRepository seatRepo;

    @GetMapping("/seat/{seatNumber}")
    public ResponseEntity<Student> getStudentBySeat(@PathVariable int seatNumber) {

        return studentRepo
                .findBySeat_SeatNumberAndActiveTrue(seatNumber)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Active student not found"));
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateStudent(
            @PathVariable Long id,
            @RequestBody StudentUpdateRequest req) {

        Student student = studentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (req.getName() != null) {
            student.setName(req.getName());
        }

        if (req.getPhone() != null) {
            student.setPhone(req.getPhone());
        }

        if (req.getSeatNumber() != null) {
            student.setSeatNumber(req.getSeatNumber());
        }

//        if (student.getSeatNumber()!=(req.getSeatNumber())) {
//
//            // 1️⃣ Free old seat
//            Optional<Seat> oldSeat = seatRepo.findBySeatNumber(student.getSeatNumber());
//            oldSeat.get().setOccupied(false);
//            oldSeat.setStudent(null);
//            seatRepo.save(oldSeat.get());
//
//            // 2️⃣ Assign new seat
//            Optional<Seat> newSeat = seatRepo.findBySeatNumber(req.getSeatNumber());
//            newSeat.get().setOccupied(true);
//            newSeat.setStudent(student);
//            seatRepo.save(newSeat.get());
//
//            // 3️⃣ Update student
//            student.setSeatNumber(req.getSeatNumber());
//        }

        if (req.getEndDate() != null) {
            student.setEndDate(req.getEndDate());
        }
        if (req.getExpireDate() != null) {
            System.out.println("request ExpiredDate  : " + req.getExpireDate());
            student.setExpiryDate(req.getExpireDate());

        }

        studentRepo.save(student);

        System.out.println("Student s : " + student.getExpiryDate());
        return ResponseEntity.ok("Student updated");
    }



    @GetMapping
    public List<StudentTableResponse> getAllActiveStudents() {

        return studentRepo.findByActiveTrue()
                .stream()
                .map(s -> new StudentTableResponse(
                        s.getId(),
                        s.getAmountPaid(),
                        s.getExpiryDate(),
                        s.getEndDate(),
                        s.getStartDate(),
                        s.getSeat().getSeatNumber(),
                        s.getPhone(),
                        s.getName()
                ))
                .toList();
    }

    @PostMapping("/{id}/vacate")
    public void vacateStudent(@PathVariable Long id) {

        Student student = studentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setActive(false);
        studentRepo.save(student);
    }

//  filter the student by name , phone, seat
@GetMapping("/search")
public List<StudentTableResponse> searchStudents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Integer seat
    ) {
        List<Student> students;

        if (seat != null) {
            students = studentRepo.findBySeatNumberAndActiveTrue(seat)
                    .map(List::of)
                    .orElse(List.of());
        } else if (name != null && !name.isBlank()) {
            students = studentRepo.findByNameContainingIgnoreCaseAndActiveTrue(name);
        } else if (phone != null && !phone.isBlank()) {
            students = studentRepo.findByPhoneContainingAndActiveTrue(phone);
        } else {
            students = studentRepo.findByActiveTrue();
        }

        return students.stream()
                .map(s -> StudentTableResponse.from(s))
                .toList();
    }

// Subscription expired Students
    @GetMapping("/expiring-soon")
    public List<StudentTableResponse> expiringSoon() {
        LocalDate today = LocalDate.now();
        LocalDate limit = today.plusDays(2);

        return studentRepo
                .findByActiveTrueAndExpiryDateBetween(today, limit)
                .stream()
                .map(StudentTableResponse::from)
                .toList();
    }

    @GetMapping("/expired")
    public List<StudentTableResponse> expiredStudents() {
        LocalDate today = LocalDate.now();

        return studentRepo
                .findByActiveTrueAndExpiryDateBefore(today)
                .stream()
                .map(StudentTableResponse::from)
                .toList();
    }

}
