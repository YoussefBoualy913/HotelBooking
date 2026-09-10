package service;
import exception.ReservationNotFoundException;
import exception.RoomNotFoundException;
import exception.RoomUnavailableException;
import model.Reservation;
import model.ReservationStatus;
import model.Room;
import model.User;
import repository.ReservationRepository;
import repository.RoomRepository;
import util.InputUtils;
import util.Roomutils;
import util.ValidationUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthServiceReservationService {
    private InputUtils inputUtils;
    private Roomutils roomutils;
    private ReservationRepository reservationRepository;
    private int reservationNumber = 1;
    public AuthServiceReservationService(ReservationRepository reservationRepository) {
        inputUtils = new InputUtils();
        roomutils = new Roomutils(reservationRepository);
        this.reservationRepository = reservationRepository;
    }
  public void createReservation(User user, RoomRepository roomRepository){

      String roomnumber = inputUtils.readString("Room nomber:");
      if (!ValidationUtils.isNotEmpty(roomnumber)) {
          throw new IllegalArgumentException("Room nomber cannot be empty");
      }
    System.out.println(roomnumber);
      Room room = roomRepository.findByRoomNumber(roomnumber)
              .orElseThrow(() -> new RoomNotFoundException(
                      "Room number does not exist"
              ));

      LocalDate checkIn;
      LocalDate checkOut;

      while (true){

       checkIn = inputUtils.readDate("Check-in (YYYY-MM-DD): ");
       checkOut = inputUtils.readDate("Check-out (YYYY-MM-DD): ");

       if (!ValidationUtils.isValidDateRange(checkIn, checkOut)) {
              System.out.println("Check-in must be before check-out.");
              continue;
          }
      boolean isRoomAvailable = roomutils.isRoomAvailable(roomnumber, checkIn, checkOut);

      if(!isRoomAvailable){
          System.out.println(
                  "The room is not available for the selected period."
          );
          continue;
      }

      break;
      }

      int numberOfGuests;

      while (true) {
          numberOfGuests = inputUtils.readInt("Number of guests: ");

          if (numberOfGuests < 1) {
              System.out.println("Number of guests cannot be less than 1");
              continue;
          }

          if (numberOfGuests > room.getCapacity()) {
              System.out.println(
                      "Number of guests cannot be greater than room capacity"
              );
              continue;
          }

          break;
      }

      String reservationCode =
              String.format("RES-%d-%04d", checkIn.getYear(), reservationNumber++);

      long numberOfNights =
              ChronoUnit.DAYS.between(checkIn, checkOut);

      BigDecimal totalPrice =
              room.getPricePerNight()
                      .multiply(BigDecimal.valueOf(numberOfNights));

      Reservation reservation = new Reservation(user.getId(),reservationCode,roomnumber,checkIn,checkOut,numberOfGuests,numberOfNights,totalPrice);
      reservationRepository.save(reservation);

    }

    public List<Reservation> getMyReservations(AuthService authService) {
        UUID userId = authService.getCurrentUser().getId();
        return reservationRepository.findByUserId(userId)
                .stream()
                .filter(reservation -> reservation.getStatus() != ReservationStatus.CANCELLED)
                .toList();
    }
    public void cancelReservation() {
       String reservationCode = inputUtils.readString("Reservation code: ");

        Reservation reservation = reservationRepository
                .findByCode(reservationCode)
                .orElseThrow(() ->
                        new ReservationNotFoundException("Reservation not found"));

        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new IllegalArgumentException(
                    "Only confirmed reservations can be cancelled."
            );
        }

        reservation.setStatus(ReservationStatus.CANCELLED);

        reservationRepository.save(reservation);
    }

}