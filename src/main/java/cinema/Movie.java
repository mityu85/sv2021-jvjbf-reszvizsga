package cinema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

    private long id;
    private String title;
    private LocalDateTime date;
    private int maxReservation;
    private int freeSpaces = maxReservation;

    public void createReservation(int numb) {
        if (freeSpaces - numb <= 0 || numb > maxReservation) {
            throw new IllegalStateException();
        } else {
            freeSpaces -= numb;
        }
    }
}
