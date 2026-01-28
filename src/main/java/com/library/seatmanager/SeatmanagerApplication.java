package com.library.seatmanager;

import com.library.seatmanager.entity.Admin;
import com.library.seatmanager.entity.Library;
import com.library.seatmanager.entity.Seat;
import com.library.seatmanager.repository.AdminRepository;
import com.library.seatmanager.repository.LibraryRepository;
import com.library.seatmanager.repository.SeatRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SeatmanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeatmanagerApplication.class, args);
	}

	// 🔽 PLACE THIS HERE
	@Bean
	CommandLineRunner initSeats(SeatRepository repo) {
		return args -> {
			if (repo.count() == 0) {
				for (int i = 1; i <= 65; i++) {
					Seat seat = new Seat();
					seat.setSeatNumber(i);
					seat.setOccupied(false);
					repo.save(seat);
				}
			}
		};
	}

	@Bean
	CommandLineRunner initAdmin(AdminRepository adminRepo,
								LibraryRepository libraryRepo,
								PasswordEncoder encoder) {

		return args -> {

			if (adminRepo.count() == 0) {
				Admin admin = new Admin();
				admin.setName("Library admin");
				admin.setPhone("9999999999");
				admin.setPassword(encoder.encode("admin@123"));
				adminRepo.save(admin);
			}

			if (libraryRepo.count() == 0) {
				Library lib = new Library();
				lib.setLibraryName("Success Library");
				lib.setTotalSeats(65);
				libraryRepo.save(lib);
			}
		};
	}

}
