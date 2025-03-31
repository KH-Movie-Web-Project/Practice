package kh.gangnam.movie.Model.OpenApiDAO;

import jakarta.persistence.*;
import kh.gangnam.movie.Model.OpenApiDTO.BoxOfficeResult;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
public class BoxOfficeResultDAO {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long dailyId;

    private String boxofficeType;
    private String showRange;

    @OneToMany(mappedBy = "boxOfficeResult", cascade = CascadeType.ALL)
    private List<DailyBoxOfficeDAO> dailyBoxOfficeList;

    public static BoxOfficeResultDAO fromDTO(BoxOfficeResult dto) {
        BoxOfficeResultDAO dao = new BoxOfficeResultDAO();
        dao.boxofficeType = dto.getBoxofficeType();
        dao.showRange = dto.getShowRange();
        return dao;
    }
}
