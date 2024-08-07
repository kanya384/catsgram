package ru.yandex.practicum.catsgram.controller;

import exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;
import java.util.Optional;


@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public Collection<Post> findAll(@RequestParam(defaultValue = "0", required = false) int from, @RequestParam(defaultValue = "10", required = false) int size, @RequestParam(defaultValue = "desc", required = false) String sort) {
        return postService.findAll(from, size, sort);
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long id) {
        Optional<Post> postOpt = postService.findPostById(id);
        if (postOpt.isEmpty()) {
            throw new NotFoundException(String.format("нет поста с заданным id = %d", id));
        }

        return postOpt.get();
    }

    @PostMapping
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }

    @PutMapping
    public Post update(@RequestBody Post newPost) {
        return postService.update(newPost);
    }
}
