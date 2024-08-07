package ru.yandex.practicum.catsgram.model;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class Post implements Comparable<Post> {
    private Long id;
    private long authorId;
    private String description;
    private Instant postDate;

    @Override
    public int compareTo(Post o) {
        return getPostDate().compareTo(o.postDate);
    }
}
