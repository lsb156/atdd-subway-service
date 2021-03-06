package nextstep.subway.favorite.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-07
 */
public class FavoriteResponse {

    private Long id;

    private StationResponse source;

    private StationResponse target;

    private FavoriteResponse(Long id, StationResponse source,
            StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(long id, Station source, Station target) {
        return new FavoriteResponse(id,
                StationResponse.of(source),
                StationResponse.of(target));
    }

    public Long getId() {
        return id;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }
}
