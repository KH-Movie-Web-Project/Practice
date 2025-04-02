package kh.gangnam.movie.Model.OpenApiDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class GenresDTO {
    private List<Genres> genres;
}
