package com.imooc.pan.server.common.stream.event.search;
import lombok.*;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;


@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class UserSearchEvent implements Serializable {
    private static final long serialVersionUID = 4307512337571187139L;
    private String keyword;

    private Long userId;

    public UserSearchEvent(String keyword, Long userId) {
        this.keyword = keyword;
        this.userId = userId;
    }


}
