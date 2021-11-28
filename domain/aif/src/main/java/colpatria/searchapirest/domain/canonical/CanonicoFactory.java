package colpatria.searchapirest.domain.canonical;

import colpatria.searchapirest.domain.canonico.Header;
import colpatria.searchapirest.domain.canonico.Canonico;

import java.util.Date;

public interface CanonicoFactory {
    default <T> Canonico createGenericCanonico(T data, String uuid, String appId) {
        return Canonico.builder()
                .header(Header.builder()
                        .applicationId(appId)
                        .hostname("host-scheduler")
                        .transactionDate(new Date())
                        .transactionId(uuid)
                        .user("user-scheduler")
                        .build())
                .data(data)
                .build();
    }
}
