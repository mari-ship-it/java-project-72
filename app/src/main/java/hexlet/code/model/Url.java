package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Url {

    private Long id;

    @ToString.Include
    private String name;
    private String createdAt;

    public Url(String name) {
        this.name = name;
    }
}
