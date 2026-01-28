package com.library.seatmanager.controller;

import com.library.seatmanager.dto.HalfDayStudentResponse;
import com.library.seatmanager.dto.StudentCreateRequest;
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
import java.time.LocalDateTime;
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
//
//        if (req.getSeatNumber() != null) {
//            student.setSeatNumber(req.getSeatNumber());
//        }

        if (student.getSeatNumber() != req.getSeatNumber()) {

            System.out.println("Old Seat no : " + student.getSeatNumber() + " and new Seat no : " +req.getSeatNumber() );

            try {
                // 1️⃣ Free old seat
                Seat oldSeat = seatRepo.findBySeatNumber(student.getSeatNumber())
                        .orElseThrow(() -> new RuntimeException("Old seat not found"));
                oldSeat.setOccupied(false);
                seatRepo.save(oldSeat);

                // 2️⃣ Assign new seat
                Seat newSeat = seatRepo.findBySeatNumber(req.getSeatNumber())
                        .orElseThrow(() -> new RuntimeException("New seat not found"));


                if (newSeat.isOccupied()) {
                    throw new RuntimeException("Seat already occupied");
                }

                newSeat.setOccupied(true);
                seatRepo.save(newSeat);
                student.setSeat(newSeat);
                student.setSeatNumber(newSeat.getSeatNumber());
            }
            catch (Exception e){
                if (req.getSeatNumber() != null) {
                    student.setSeatNumber(req.getSeatNumber());
                }
            }
        }



        if (req.getEndDate() != null) {
            student.setEndDate(req.getEndDate());
        }
        if (req.getExpireDate() != null) {
            System.out.println("request ExpiredDate  : " + req.getExpireDate());
            student.setExpiryDate(req.getExpireDate());

        }

        studentRepo.save(student);

        System.out.println("Student s : " + student.getExpiryDate());
        System.out.println("Student new Seat no : " + student.getSeatNumber());
        return ResponseEntity.ok("Student updated");
    }



    @GetMapping
    public List<StudentTableResponse> getAllActiveStudents() {
        return studentRepo
                .findByActiveTrueAndStudentType(Student.StudentType.FULL_DAY)
                .stream()
                .map(StudentTableResponse::from)
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

        List<Student> list =
                studentRepo.findByActiveTrueAndExpiryDateBetween(today, limit);
        System.out.println("EXPIRING SOON COUNT = " + list.size());
        return list.stream().map(StudentTableResponse::from).toList();
    }

    @GetMapping("/expired")
    public List<StudentTableResponse> expiredStudents() {
        LocalDate today = LocalDate.now();

        List<Student> list =
                studentRepo.findByActiveTrueAndExpiryDateBefore(today);

        System.out.println("EXPIRED COUNT = " + list.size());
        return list.stream().map(StudentTableResponse::from).toList();
    }


    @PostMapping("/create")
    public ResponseEntity<String> createStudent(
            @RequestBody StudentCreateRequest req
    ) {
        Student student = new Student();

        student.setName(req.getName());
        student.setPhone(req.getPhone());
        student.setAmountPaid(req.getAmount());

        student.setBookingDate(LocalDate.now());
        student.setStartDate(LocalDateTime.now());
        student.setExpiryDate(LocalDate.now().plusDays(30));
        student.setActive(true);

        // 🔥 CORE LOGIC
        if (req.getStudentType() == Student.StudentType.FULL_DAY) {

            if (req.getSeatNumber() == null) {
                throw new RuntimeException("Seat is required for full day student");
            }

            Seat seat = seatRepo.findBySeatNumber(req.getSeatNumber())
                    .orElseThrow(() -> new RuntimeException("Seat not found"));

            if (seat.isOccupied()) {
                throw new RuntimeException("Seat already occupied");
            }

            seat.setOccupied(true);
            seatRepo.save(seat);

            student.setSeat(seat);
            student.setSeatNumber(seat.getSeatNumber());
            student.setStudentType(Student.StudentType.FULL_DAY);
            student.setHalfDaySlot(null);
        }

        if (req.getStudentType() == Student.StudentType.HALF_DAY) {

            if (req.getHalfDaySlot() == null) {
                throw new RuntimeException("Half day slot is required");
            }

            // ❌ No seat
            student.setSeat(null);
            student.setSeatNumber(0);

            student.setStudentType(Student.StudentType.HALF_DAY);
            student.setHalfDaySlot(req.getHalfDaySlot());
        }

        studentRepo.save(student);

        return ResponseEntity.ok("Student created successfully");
    }

    @GetMapping("/halfday")
    public List<HalfDayStudentResponse> getHalfDayStudents() {

        List<Student> list =
                studentRepo.findByStudentTypeAndActiveTrue(
                        Student.StudentType.HALF_DAY
                );

        System.out.println("HALF DAY STUDENTS = " + list.size());

        return list.stream()
                .map(HalfDayStudentResponse::from)
                .toList();
    }
}
