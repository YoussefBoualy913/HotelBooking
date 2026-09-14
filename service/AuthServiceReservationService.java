package service;

import exception.ReservationNotFoundException;
import exception.RoomNotFoundException;
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
import java.util.UUID;

public class AuthServiceReservationService {
    private InputUtils inputUtils;
    private Roomutils roomutils;
    private ReservationRepository reservationRepository;
    private int reservationNumber = 5;

    public AuthServiceReservationService(ReservationRepository reservationRepository) {
        inputUtils = new InputUtils();
        roomutils = new Roomutils(reservationRepository);
        this.reservationRepository = reservationRepository;
    }

    public void createReservation(User user, RoomRepository roomRepository) {

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

        while (true) {

            checkIn = inputUtils.readDate("Check-in (YYYY-MM-DD): ");
            checkOut = inputUtils.readDate("Check-out (YYYY-MM-DD): ");

            if (!ValidationUtils.isValidDateRange(checkIn, checkOut)) {
                System.out.println("Check-in must be before check-out.");
                continue;
            }
            boolean isRoomAvailable = roomutils.isRoomAvailable(roomnumber, checkIn, checkOut,null);

            if (!isRoomAvailable) {
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

        Reservation reservation = new Reservation(user.getId(), reservationCode, roomnumber, checkIn, checkOut, numberOfGuests, numberOfNights, totalPrice);
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

    public void updateReservation(

            AuthService  authService,
            RoomRepository roomRepository
    ) throws ReservationNotFoundException {
      String  reservationCode = inputUtils.readString("Reservation code: ");
        UUID userId =  authService.getCurrentUser().getId();

        Reservation reservation = reservationRepository
                .findByCode(reservationCode)
                .orElseThrow(() ->
                        new ReservationNotFoundException(
                                "Reservation not found"
                        ));

        Room room= roomRepository.findByRoomNumber(reservation.getRoomNumber())
                .orElseThrow(() ->
                        new ReservationNotFoundException("Room not found"));

        if (!reservation.getUserId().equals(userId)) {
            throw new IllegalArgumentException(
                    "You cannot update this reservation."
            );
        }


        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new IllegalArgumentException(
                    "Only confirmed reservations can be updated."
            );
        }

        LocalDate  checkIn;
        LocalDate  checkOut;
        while (true) {

            checkIn = inputUtils.readDate("Check-in (YYYY-MM-DD): ");
            checkOut = inputUtils.readDate("Check-out (YYYY-MM-DD): ");

            if (!ValidationUtils.isValidDateRange(checkIn, checkOut)) {
                System.out.println("Check-in must be before check-out.");
                continue;
            }
            boolean isRoomAvailable = roomutils.isRoomAvailable(reservation.getRoomNumber(), checkIn, checkOut,reservation.getId());

            if (!isRoomAvailable) {
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


        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);

        BigDecimal totalPrice = room.getPricePerNight()
                .multiply(BigDecimal.valueOf(nights));


        reservation.setCheckIn(checkIn);
        reservation.setCheckOut(checkOut);
        reservation.setNumberOfGuests(numberOfGuests);
        reservation.setNumberOfNights(nights);
        reservation.setTotalPrice(totalPrice);


        reservationRepository.save(reservation);
    }

  public void reservationDetailes(AuthService authService) {

       String reservationCode =  inputUtils.readString("Reservation code: ");
      User user =  authService.getCurrentUser();

      Reservation reservation = reservationRepository
              .findByCode(reservationCode)
              .orElseThrow(() ->
                      new ReservationNotFoundException(
                              "Reservation not found"
                      ));

      if (!reservation.getUserId().equals(user.getId()) || reservation.getStatus() != ReservationStatus.CONFIRMED) {
          throw new IllegalArgumentException(
                  "You cannot show this reservation."
          );
      }

      System.out.println();
      System.out.println("⫷ RESERVATION DETAILS ⫸\n" +
              "* Code:" +reservation.getReservationCode()+"\n"+
              "* User:" +user.getFullName()+"\n"+
              "* Room:" +reservation.getRoomNumber()+"\n"+
              "* Check-in:" +reservation.getCheckIn()+"\n"+
              "* Check-out:" +reservation.getCheckOut()+"\n"+
              "* Guests:" +reservation.getNumberOfGuests()+"\n"+
              "* Nights:" +reservation.getNumberOfNights()+"\n"+
              "* Total:" +reservation.getTotalPrice()+"\n"+
              "* Status:" +reservation.getStatus()+"\n"+
              "* Reserver at:"+reservation.getCreatedAt()+"\n"+
              "");


  }

    public static void updateExpiredReservations(ReservationRepository reservationRepository) {
        List<Reservation> reservations = reservationRepository.findAll();
        LocalDate today = LocalDate.now();

        for (Reservation reservation : reservations) {
            if (reservation.getStatus() == ReservationStatus.CONFIRMED
                    && !today.isBefore(reservation.getCheckOut())) {

                reservation.setStatus(ReservationStatus.COMPLETED);
            }
        }
    }

}