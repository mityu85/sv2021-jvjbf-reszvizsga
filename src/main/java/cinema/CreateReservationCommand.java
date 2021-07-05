package cinema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReservationCommand {

    private int reservationNumber;
}
