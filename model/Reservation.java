import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Reservation {

    private UUID id;
    private String reservationCode;
    private UUID userId;
    private String roomNumber;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int numberOfGuests;
    private long numberOfNights;
    private BigDecimal totalPrice;
    private ReservationStatus status;
    private LocalDateTime createdAt;

    public Reservation(
            UUID userId,
            String roomNumber,
            LocalDate checkIn,
            LocalDate checkOut,
            int numberOfGuests,
            long numberOfNights,
            BigDecimal totalPrice
    ) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.roomNumber = roomNumber;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.numberOfGuests = numberOfGuests;
        this.numberOfNights = numberOfNights;
        this.totalPrice = totalPrice;
        this.status = ReservationStatus.CONFIRMED;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getReservationCode() {
        return reservationCode;
    }

    public void setReservationCode(String reservationCode) {
        this.reservationCode = reservationCode;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public long getNumberOfNights() {
        return numberOfNights;
    }

    public void setNumberOfNights(long numberOfNights) {
        this.numberOfNights = numberOfNights;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}