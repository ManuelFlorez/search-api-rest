package colpatria.searchapirest.api;

import colpatria.searchapirest.api.dto.Anexo.AnexoDTO;
import colpatria.searchapirest.api.dto.Anexo.ResultAnexoDTO;
import colpatria.searchapirest.api.mapper.AnexoMapper;
import colpatria.searchapirest.api.mapper.DataMapper;
import colpatria.searchapirest.domain.messagedata.Anexo;
import colpatria.searchapirest.domain.messagedata.Data;
import colpatria.searchapirest.domain.messagedata.ResultAnexo;
import colpatria.searchapirest.domain.messagedata.gateway.CommunicationApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static reactor.core.publisher.Mono.just;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Log
@Service
public class ApiAdapter implements CommunicationApi {

    //@Value("${search.api.url.pqr}")
    private final String url; // = "https://6184369cac0b850017489cb4.mockapi.io/api/v1/pqr";

    //@Value("${search.api.url.anexos}")
    private final String urlAnexos; //= "https://6184369cac0b850017489cb4.mockapi.io/api/v1/anexos";

    //@Value("${time.out.duration.api.rest}")
    private final Integer timeOut;

    @Autowired
    public ApiAdapter(@Value("${search.api.url.pqr}") String url, @Value("${search.api.url.anexos}") String urlAnexos,
                      @Value("${time.out.duration.api.rest}") Integer timeOut, WebClient webClient, DataMapper mapper,
                      AnexoMapper anexoMapper) {
        this.url = url;
        this.urlAnexos = urlAnexos;
        this.timeOut = timeOut;

        this.webClient = webClient;
        this.mapper = mapper;
        this.mapperAnexo = anexoMapper;
    }

    private final WebClient webClient;

    private final DataMapper mapper;

    private final AnexoMapper mapperAnexo;

    @Value("${path.file}")
    private String path;

    @Override
    public Mono<Data> searchApi() {
        return webClient
                .get()
                .uri(url)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(timeOut))
                .onErrorResume(error -> Mono.error(new RuntimeException(error.getMessage())))
                .flatMap(mapper::map);
    }

    @Override
    public Mono<Anexo> SearchAnexos(List<String> ids) {
        return webClient
                .get()
                .uri(urlAnexos)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(timeOut))
                .onErrorResume(error -> Mono.error(new RuntimeException(error.getMessage())))
                .flatMap(mapperAnexo::map)
                .flatMap(anexoDTO -> validate(anexoDTO, ids))
                .flatMap(mapperAnexo::map);
    }

    @Override
    public Mono<String> downloadFile(ResultAnexo resultAnexo) {

        Flux<DataBuffer> dataBuffer = webClient
                .get()
                .uri(resultAnexo.getFile())
                .retrieve()
                .bodyToFlux(DataBuffer.class);

        String absolutePath = path + "\\" + resultAnexo.getId() + "." + resultAnexo.getType();
        Path destination = Paths.get(absolutePath);

        return DataBufferUtils.write(dataBuffer, destination, StandardOpenOption.CREATE).share()
                .thenReturn(absolutePath);
    }


    private Mono<AnexoDTO> validate(AnexoDTO anexoDTO, List<String> ids) {
        return just(anexoDTO.toBuilder()
                        .results( filterResultAnexoDTO(anexoDTO.getResults(), ids) )
                .build());
    }

    private List<ResultAnexoDTO> filterResultAnexoDTO(List<ResultAnexoDTO> resultAnexoDTOS, List<String> ids) {
        return resultAnexoDTOS.stream()
                .filter(resultAnexoDTO -> findIdList(ids, resultAnexoDTO.getId()) )
                .collect(Collectors.toList());
    }

    private boolean findIdList(List<String> ids, String id) {
        List<String> auxList = ids.stream().filter(auxId -> auxId.equals(id)).collect(Collectors.toList());
        return !auxList.isEmpty() && auxList.get(0).equals(id);
    }

}
