package colpatria.searchapirest.domain.common;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface ObjectMapperDomain {
    default <T, E> List<E> mapear(List<T> list, Function<T, E> transform) {
        return list.stream().map(transform).collect(Collectors.toList());
    }
}
