package colpatria.searchapirest.domain.messagedata;

import lombok.*;

import java.util.List;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Data {
    private final Integer count;
    private final String next;
    private final String previous;
    private final List<Result> results;
}
