package com.example.moviereservationsystem.controller;

import com.example.moviereservationsystem.model.Movie;
import com.example.moviereservationsystem.model.ShowTime;
import com.example.moviereservationsystem.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @PostMapping
    public Movie addMovie (@RequestBody Movie movie) {
        return this.movieService.addMovie (movie);
    }

    @PutMapping("/{id}")
    public Movie updateMovie (@PathVariable Long id, @RequestBody Movie updatedMovie) {
        return this.movieService.updateMovie (id, updatedMovie);
    }

    @DeleteMapping("/{id}")
    public void deleteMovie (@PathVariable Long id) {
        this.movieService.deleteMovie (id);
    }

    @GetMapping
    public List <Movie> getAllMovies () {
        return this.movieService.getAllMovies ();
    }

    @GetMapping("/{id}")
    public Optional <Movie> getMovieById (@PathVariable Long id) {
        return this.movieService.getMovieById (id);
    }

    @GetMapping("/genre/{genre}")
    public List <Movie> getMoviesByGenre (@PathVariable String genre) {
        return this.movieService.getMoviesByGenre (genre);
    }

    @PostMapping("/{movieId}/showtimes")
    public ShowTime addShowTime (@PathVariable Long movieId, @RequestBody ShowTime showTime) {
        return this.movieService.addShowTime (movieId, showTime);
    }

    @GetMapping("/{movieId}/showtimes")
    public List <ShowTime> getShowTimesByMovie (@PathVariable Long movieId) {
        return this.movieService.getShowTimesByMovie (movieId);
    }

    @GetMapping("/showtimes/date")
    public List <ShowTime> getShowTimesByDate (@RequestParam String date) {
        return this.movieService.getShowTimesByDate (LocalDate.parse (date));
    }
}