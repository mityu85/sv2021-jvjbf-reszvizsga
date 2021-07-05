package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cinema")
public class MovieController {

    private MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public List<MovieDTO> getMovies(@Valid @RequestParam Optional<String> title) {
        return movieService.getMovies(title);
    }

    @GetMapping("/{id}")
    public MovieDTO getMovieById(@PathVariable("id") long id) {
        return movieService.getMovieById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieDTO createMovie(@Valid @RequestBody CreateMovieCommand command) {
        return movieService.createMovie(command);
    }

    @PostMapping("/{id}/reserve")
    public MovieDTO createReservation(@PathVariable("id") long id, @RequestBody CreateReservationCommand command) {
        return movieService.createReservation(id, command);
    }

    @PutMapping("/{id}")
    public MovieDTO updateMovie(@PathVariable("id") long id, @RequestBody UpdateDateCommand command) {
        return movieService.updateDate(id, command);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllMovies() {
        movieService.deleteAllMovies();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Problem> handleNotFound(IllegalArgumentException e) {
        Problem problem =
                Problem.builder()
                        .withType(URI.create("cinema/not-found"))
                        .withTitle("Not found")
                        .withStatus(Status.NOT_FOUND)
                        .withDetail(e.getMessage())
                        .build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Problem> handleNotFound(IllegalStateException e) {
        Problem problem =
                Problem.builder()
                        .withType(URI.create("cinema/bad-reservation"))
                        .withTitle("Validation error")
                        .withStatus(Status.BAD_REQUEST)
                        .withDetail(e.getMessage())
                        .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }
}
