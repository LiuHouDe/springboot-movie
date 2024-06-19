package com.ispan.theater.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.ispan.theater.domain.Comment;
import com.ispan.theater.repository.CommentRepository;
@Service
public class CommentService {
	@Autowired
	private CommentRepository commentRepository;
	
	public Comment findCommentById(Integer commentId) {
		Optional<Comment> optional = commentRepository.findById(commentId);
		if(optional.isEmpty()) {
			return null;
		}
		
		Comment comment = optional.get();
		
		return comment;
	}
	public Comment insertComment(Comment comment) {
		return commentRepository.save(comment);
	}

	public Page<Comment> findNewComment() {
		Pageable pgb = PageRequest.of(0, 1,Direction.DESC,"createTime");
		Page<Comment> page = commentRepository.findAll(pgb);
		 return page;
	}
	 public Page<Comment> searchComments(String keyword, Pageable pageable) {
	        return commentRepository.searchByKeyword(keyword, pageable);
	    }
	 public Page<Comment> searchByName(String keyword, Pageable pageable) {
	        return commentRepository.searchByName(keyword, pageable);
	    }
	 public Page<Comment> searchByMovie(String keyword, Pageable pageable) {
	        return commentRepository.searchByMovie(keyword, pageable);
	    }
}
