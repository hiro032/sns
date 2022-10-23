package com.hiro.sns.controller;

import com.hiro.sns.controller.request.PostCreateRequest;
import com.hiro.sns.controller.request.PostModifyRequest;
import com.hiro.sns.controller.response.PostResponse;
import com.hiro.sns.controller.response.Response;
import com.hiro.sns.model.Post;
import com.hiro.sns.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	@PostMapping
	public Response<PostResponse> create(@RequestBody PostCreateRequest request, Authentication authentication) {
		Post post = postService.create(request.getTitle(), request.getBody(), authentication.getName());

		return Response.success(PostResponse.fromPost(post));
	}

	@PutMapping("/{postId}")
	public Response<PostResponse> modify(@PathVariable Integer postId, @RequestBody PostModifyRequest request, Authentication authentication) {
		Post post = postService.modify(request.getTitle(), request.getBody(), authentication.getName(), postId);

		return Response.success(PostResponse.fromPost(post));
	}
}
