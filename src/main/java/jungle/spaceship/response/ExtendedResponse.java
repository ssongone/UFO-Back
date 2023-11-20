package jungle.spaceship.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class ExtendedResponse<T> extends BasicResponse {
    private final T data;

    public ExtendedResponse(T data, int code, String message) {
        super(code, message);
        this.data = data;
    }

}
