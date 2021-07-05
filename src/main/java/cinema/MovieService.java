package cinema;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private AtomicLong idGenerator = new AtomicLong();
    private ModelMapper modelMapper;

    private List<Movie> movies = Collections.synchronizedList(new ArrayList<>());

    public MovieService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public List<MovieDTO> getMovies(Optional<String> title) {
        List<MovieDTO> filtered = movies.stream()
                .filter(e -> title.isEmpty() || e.getTitle().equalsIgnoreCase(title.get()))
                .map(e -> modelMapper.map(e, MovieDTO.class))
                .collect(Collectors.toList());
        return filtered;
    }

    public MovieDTO getMovieById(long id) {
        return modelMapper.map(movies.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Movie is not found: " + id)), MovieDTO.class);
    }

    public MovieDTO createMovie(CreateMovieCommand command) {
        Movie movie = new Movie(idGenerator.incrementAndGet(),
                command.getTitle(),
                command.getDate(),
                command.getMaxReservation(),
                command.getMaxReservation());
        movies.add(movie);
        return modelMapper.map(movie, MovieDTO.class);
    }

    public MovieDTO createReservation(long id, CreateReservationCommand command) {
        Movie movie = movies.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Movie is not found: " + id));
        movie.createReservation(command.getReservationNumber());
        return modelMapper.map(movie, MovieDTO.class);
    }

    public MovieDTO updateDate(long id, UpdateDateCommand command) {
        Movie movie = movies.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Movie is not found: " + id));
        movie.setDate(command.getDate());
        return modelMapper.map(movie, MovieDTO.class);
    }

    public void deleteAllMovies() {
        idGenerator = new AtomicLong();
        movies.clear();
    }
}
