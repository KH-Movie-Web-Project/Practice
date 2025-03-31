package kh.gangnam.movie.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kh.gangnam.movie.Model.OpenApiDAO.BoxOfficeResultDAO;
import kh.gangnam.movie.Model.OpenApiDAO.DailyBoxOfficeDAO;
import kh.gangnam.movie.Model.OpenApiDTO.BoxOfficeResponse;
import kh.gangnam.movie.Model.OpenApiDTO.DailyBoxOffice;
import kh.gangnam.movie.Repository.BoxOfficeResultDAORepository;
import kh.gangnam.movie.Repository.DailyBoxOfficeDAORepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final BoxOfficeResultDAORepository boxOfficeResultDAORepository;
    private final DailyBoxOfficeDAORepository dailyBoxOfficeDAORepository;

    public MovieService(RestTemplate restTemplate, ObjectMapper objectMapper, BoxOfficeResultDAORepository boxOfficeResultDAORepository, DailyBoxOfficeDAORepository dailyBoxOfficeDAORepository) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.boxOfficeResultDAORepository = boxOfficeResultDAORepository;
        this.dailyBoxOfficeDAORepository = dailyBoxOfficeDAORepository;
    }

    @Value("${openapi.api-key}")
    private String apikey;
    @Value("${openapi.daily}")
    private String url;

    // TODO 받은 데이터 그대로 반환하기 Open API 요청 -> 바로 반환
    public JsonNode getBoxOfficeData1(String day) throws JsonProcessingException {
        String URL = url +"?key=" + apikey + "&targetDt=" + day;
        String data = restTemplate.getForObject(URL, String.class);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(data);
    }


    // TODO ResponseEntity 사용 -> 미리 정의해둔 파일로 변환
    public BoxOfficeResponse getBoxOfficeData2(String day) throws JsonProcessingException {
        String URL = url +"?key=" + apikey + "&targetDt=" + day;

        String response = restTemplate.getForObject(URL, String.class);
        return objectMapper.readValue(response, BoxOfficeResponse.class);
    }

    // TODO 데이터 받아와서 저장
    public void save(String day) throws JsonProcessingException {
        String URL = url +"?key=" + apikey + "&targetDt=" + day;

        String response = restTemplate.getForObject(URL, String.class);
        BoxOfficeResponse result = objectMapper.readValue(response, BoxOfficeResponse.class);

        BoxOfficeResultDAO boxOfficeResultDAO = BoxOfficeResultDAO.fromDTO(result.getBoxOfficeResult());
        List<DailyBoxOfficeDAO> dailyBoxOfficeDAOList = new ArrayList<>();

        for (DailyBoxOffice dto : result.getBoxOfficeResult().getDailyBoxOfficeList()) {
            DailyBoxOfficeDAO dailyBoxOfficeDAO = DailyBoxOfficeDAO.fromDTO(dto);
            dailyBoxOfficeDAO.setBoxOfficeResult(boxOfficeResultDAO);
            dailyBoxOfficeDAOList.add(dailyBoxOfficeDAO);
        }

        boxOfficeResultDAO.setDailyBoxOfficeList(dailyBoxOfficeDAOList);

        boxOfficeResultDAORepository.save(boxOfficeResultDAO);
        System.out.println("저장 성공");
    }
}
