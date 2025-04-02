package kh.gangnam.movie.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import kh.gangnam.movie.Model.OpenApiDTO.BoxOfficeResponse;
import kh.gangnam.movie.Model.OpenApiDTO.GenresDTO;
import kh.gangnam.movie.Service.MovieService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/api")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // TODO JSON 직접 변환 방법 ObjectMapper 이용
    @GetMapping("/open/object-mapper/daily-box-office/{day}")
    public JsonNode daily(@PathVariable(value = "day") String day) throws JsonProcessingException {
        return movieService.getBoxOfficeData1(day);
    }

    @GetMapping("/open/responseentity/daily-box-office/{day}")
    public BoxOfficeResponse daily2(@PathVariable(value = "day") String day) throws JsonProcessingException {

        return movieService.getBoxOfficeData2(day);
    }

    @GetMapping("/save/{day}")
    public void save(@PathVariable(value = "day") String day) throws JsonProcessingException {
        movieService.save(day);
    }
    // TODO 헤더 넣는 방법
    @GetMapping("/head/test")
    public GenresDTO testHead(){

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.themoviedb.org/3/genre/movie/list?language=ko";
        // TODO 서비스 로직 부분으로 넘길 것
        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        headers.set("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxYTgwOWNhNjk1ZGM1MjNhNWVhZGRhMDE1NTlmNTMxNSIsIm5iZiI6MTc0MzQwODAxOS4wMTcsInN1YiI6IjY3ZWE0YjkzN2E3NTI5NGI3NmM2ZDEwNCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.e_2ALQjMwsQEaEAvzUTgRfKdYLsPJcMB9yleCP87Csc");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<GenresDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                GenresDTO.class
        );
        return response.getBody();
    }

}
