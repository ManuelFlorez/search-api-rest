package colpatria.searchapirest.domain.messagedata.gateway;

import reactor.core.publisher.Mono;

import java.io.File;

public interface GenerateFileGateway {
    Mono<File> generateFile(byte[] file);
}
