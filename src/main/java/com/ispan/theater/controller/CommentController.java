package com.ispan.theater.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.theater.domain.Comment;
import com.ispan.theater.domain.Movie;
import com.ispan.theater.repository.CommentRepository;
import com.ispan.theater.repository.MovieRepository;
import com.ispan.theater.service.CommentService;
import com.ispan.theater.service.MovieService;
import com.ispan.theater.service.UserService;
import com.ispan.theater.util.JsonWebTokenUtility;



@CrossOrigin
@RestController

@RequestMapping("comment")
public class CommentController {
	
    private Map<Integer, Set<Integer>> likes = new HashMap<>(); // 存儲每個評論的點讚用戶
    private Map<Integer, Set<Integer>> less = new HashMap<>(); // 存儲每個評論噓用戶


	@Autowired
	private MovieRepository movr;
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private CommentService commentService;
	@Autowired
	private UserService userService;
	@Autowired
	private MovieService movieService;
	@Autowired
	private JsonWebTokenUtility jwtu;
	@PostMapping
	public ResponseEntity<String> save(@RequestBody Comment comment ,@RequestParam String token,@RequestParam Integer movieid) {
		
		if (token != null) {
	        // 解碼TOKEN
	        String authToken = jwtu.validateToken(token);
	        if (authToken != null) {
	        	// 解碼TOKEN
	            JSONObject obj = new JSONObject(authToken);
	            Integer userId = obj.getInt("userid");


	            	//輸入USERID
					comment.getUserId().setId(userId);
					comment.getMovieId().setId(movieid);//設置電影ID
					comment.setCreatetime(LocalDateTime.now());
					// Call service class method to save the comment
					commentService.insertComment(comment);

	        } else {
	            // 錯誤的TOKEN
			        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("token錯誤");

	        }
	    } 
	    return ResponseEntity.ok("發布成功");

	    }

	
	@GetMapping
	public Map<String, Object> list(
	        @RequestParam Movie movieId,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {
	    
	    Map<String, Object> map = new HashMap<>();
	    
	    // Create a Pageable object
	    Pageable pageable = PageRequest.of(page, size, Sort.by("commentId").ascending());
	    
	    // Fetch paginated root comments (comments with pid == null)
	    Page<Comment> rootCommentPage = commentRepository.findAllByMovieIdAndPidIsNull(movieId, pageable);
	    List<Comment> rootComments = rootCommentPage.getContent();
	    
	    // Fetch all comments for the movie
	    List<Comment> allComments = commentRepository.findAllByMovieId(movieId);
	    
	    // Calculate average rate
	    map.put("rate", BigDecimal.ZERO);
	    List<Comment> commentList = allComments.stream()
	                                            .filter(comment -> comment.getRate() != null)
	                                            .collect(Collectors.toList());
	    commentList.stream()
	               .map(Comment::getRate)
	               .reduce(BigDecimal::add)
	               .ifPresent(res -> map.put("rate", res.divide(BigDecimal.valueOf(commentList.size()), 1, RoundingMode.HALF_UP)));
	    
	    // Build comment hierarchy
	    for (Comment rootComment : rootComments) {
	        rootComment.setChildren(allComments.stream()
	                                           .filter(comment -> rootComment.getCommentId().equals(comment.getPid()))
	                                           .collect(Collectors.toList()));
	    }
	    
	    // Add paginated root comments and pagination info to the map
	    map.put("comments", rootComments);
	    map.put("currentPage", rootCommentPage.getNumber());
	    map.put("totalPages", rootCommentPage.getTotalPages());
	    map.put("totalItems", rootCommentPage.getTotalElements());
	    
	    return map;
	}
	@GetMapping("/rate")
	public Map<String, Object> findRate(@RequestParam Integer movieId) {
	    Map<String, Object> map = new HashMap<>();

	    if (movieId == null) {
	        map.put("error", "movieId cannot be null");
	        return map;
	    }

	    List<Comment> comments = commentRepository.findCommentsByMovieIdByRate(movieId);
	    List<Comment> commentList = comments.stream()
	            .filter(comment -> comment.getRate() != null)
	            .collect(Collectors.toList());

	    BigDecimal totalRate = commentList.stream()
	            .map(Comment::getRate)
	            .reduce(BigDecimal.ZERO, BigDecimal::add);

	    if (!commentList.isEmpty()) {
	        BigDecimal averageRate = totalRate.divide(BigDecimal.valueOf(commentList.size()), 1, RoundingMode.HALF_UP);
	        map.put("rate", averageRate);
	    } else {
	        map.put("rate", BigDecimal.ZERO);
	    }

	    // 按評分排序的評論已經來自於查詢，處理樹狀結構
	    List<Comment> rootComments = comments.stream()
	            .filter(comment -> comment.getPid() == null)
	            .collect(Collectors.toList());

	    for (Comment rootComment : rootComments) {
	        List<Comment> children = comments.stream()
	                .filter(comment -> rootComment.getCommentId().equals(comment.getPid()))
	                .collect(Collectors.toList());
	        rootComment.setChildren(children);
	    }

	    map.put("comments", rootComments);
	    return map;
	}

	
	@GetMapping("/new")
	public Map<String, Object> findTime(@RequestParam Integer movieId) {
	    Map<String, Object> map = new HashMap<>();

	    if (movieId == null) {
	        map.put("error", "movieId cannot be null");
	        return map;
	    }

	    List<Comment> comments = commentRepository.findCommentsByMovieIdByTime(movieId);
	    List<Comment> commentList = comments.stream()
	            .filter(comment -> comment.getRate() != null)
	            .collect(Collectors.toList());

	    BigDecimal totalRate = commentList.stream()
	            .map(Comment::getRate)
	            .reduce(BigDecimal.ZERO, BigDecimal::add);

	    if (!commentList.isEmpty()) {
	        BigDecimal averageRate = totalRate.divide(BigDecimal.valueOf(commentList.size()), 1, RoundingMode.HALF_UP);
	        map.put("rate", averageRate);
	    } else {
	        map.put("rate", BigDecimal.ZERO);
	    }

	    // 這裡按評分排序的評論已經來自於查詢，所以我們只需要處理樹狀結構
	    List<Comment> rootComments = comments.stream()
	            .filter(comment -> comment.getPid() == null)
	            .collect(Collectors.toList());

	    for (Comment rootComment : rootComments) {
	        List<Comment> children = comments.stream()
	                .filter(comment -> rootComment.getCommentId().equals(comment.getPid()))
	                .collect(Collectors.toList());
	        rootComment.setChildren(children);
	    }

	    map.put("comments", rootComments);
	    return map;
	}

	
	@DeleteMapping("/{commentId}")
	public ResponseEntity<String> delete(@PathVariable Integer commentId, @RequestParam String token) {
	    if (token == null) {
	        return ResponseEntity.badRequest().body("Token遺失");
	    }


	    String authToken = jwtu.validateToken(token);
	    if (authToken == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("沒有token");
	    }


	    // Decode TOKEN to get user ID
	    JSONObject obj = new JSONObject(authToken);
	    Integer userId = obj.getInt("userid");


	    // Check if the comment exists and belongs to the user
	    Comment existingComment = commentService.findCommentById(commentId);
	    if (existingComment == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("沒有評論");
	    }

	    if (existingComment.getUserId() == null || !existingComment.getUserId().getId().equals(userId)) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("該評論不屬於該用戶");
	    }

	    // Delete the comment
	    commentRepository.deleteById(commentId);
	    return ResponseEntity.ok("珊除成功");
	}

	@PutMapping("/{commentId}")
	public ResponseEntity<String> update(@PathVariable Integer commentId, @RequestBody Comment updatedComment, @RequestParam String token) {
	    if (token != null) {
	        // 解碼TOKEN
	        String authToken = jwtu.validateToken(token);
	        if (authToken != null) {
	            // 解碼TOKEN
	            JSONObject obj = new JSONObject(authToken);
	            Integer userId = obj.getInt("userid");


	            // 檢查該評論是否存在並且屬於該使用者
	            Comment existingComment = commentService.findCommentById(commentId);
	            if (existingComment != null && existingComment.getUserId() != null && existingComment.getUserId().getId().equals(userId)) {
	                // 更新評論內容
	                existingComment.setContent(updatedComment.getContent());
	                existingComment.setRate(updatedComment.getRate());
	                commentRepository.save(existingComment);
	                return ResponseEntity.ok("Comment deleted successfully");
	            } else {
	            	return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token.");
	            }
	        } else {
	            // 錯誤的TOKEN
	        }
	    } else {
	        // 遺失
	    }
    	return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token.");
	}
	
//	找全部評論
	@GetMapping("/path")
	public Map<String, Object> getAllComments(
	        @ModelAttribute Comment commentId,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "20") int size) {
	    
	    Map<String, Object> map = new HashMap<>();
	    
	    //建立page物件
	    Pageable pageable = PageRequest.of(page, size, Sort.by("movieId").ascending());
	    
	    // Assuming your repository has a method that accepts a Comment object and a Pageable object
	    Page<Comment> commentPage = commentRepository.findAll(pageable);
	    List<Comment> comments = commentPage.getContent();
	    
	    // Adding additional pagination information
	    map.put("comments", comments);
	    map.put("currentPage", commentPage.getNumber());
	    map.put("totalItems", commentPage.getTotalElements());
	    map.put("totalPages", commentPage.getTotalPages());
	    
	    return map;
	}
	
	
	@GetMapping("/backstage/movie/findAll")
	public ResponseEntity<?> findMovie(
	        @RequestParam Map<String, String> requestParams,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size
	) {
	    JSONObject jsonObject = new JSONObject(requestParams);
	    JSONObject response = new JSONObject();
	    
        Page<Movie> result = movieService.findMulti1(jsonObject);
	    List<Movie> movies = result.getContent();
	    long count = result.getTotalElements();
	    JSONArray array = new JSONArray();
	    if (!movies.isEmpty()) {
	        for (Movie m : movies) {
	            JSONObject movie = movieService.movieToJson(m);
	            array.put(movie);
	        }
	        response.put("movies", array);
	        response.put("count", count);
	        return ResponseEntity.ok().body(response);
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}
	@DeleteMapping("/delete/{commentId}")
	public ResponseEntity<String>delete(@PathVariable Integer commentId){
		if(commentId!=null&&commentId!=0) {	    
			commentRepository.deleteById(commentId);
		    return ResponseEntity.ok("刪除成功");
			
		}
		return ResponseEntity.notFound().build();
	}
	


	@GetMapping("/search")
	public Map<String, Object> searchComments(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentPage = commentService.searchComments(keyword, pageable);
        List<Comment> comments = commentPage.getContent();
        
        Map<String, Object> response = new HashMap<>();
        response.put("comments", comments);
        response.put("currentPage", commentPage.getNumber());
        response.put("totalItems", commentPage.getTotalElements());
        response.put("totalPages", commentPage.getTotalPages());
        
        return response;
    }
	@GetMapping("/search/email")
	public Map<String, Object> searchEmail(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentPage = commentService.searchByName(keyword, pageable);
        List<Comment> comments = commentPage.getContent();
        
        Map<String, Object> response = new HashMap<>();
        response.put("comments", comments);
        response.put("currentPage", commentPage.getNumber());
        response.put("totalItems", commentPage.getTotalElements());
        response.put("totalPages", commentPage.getTotalPages());
        
        return response;
    }
	@GetMapping("/search/movie")
	public Map<String, Object> searchByMovie(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentPage = commentService.searchByMovie(keyword, pageable);
        List<Comment> comments = commentPage.getContent();
        
        Map<String, Object> response = new HashMap<>();
        response.put("comments", comments);
        response.put("currentPage", commentPage.getNumber());
        response.put("totalItems", commentPage.getTotalElements());
        response.put("totalPages", commentPage.getTotalPages());
        
        return response;
    }
	@PutMapping("/good/{commentId}")
    public ResponseEntity<String> good(@PathVariable Integer commentId, @RequestParam String token) {
        if (token != null) {
            // 解碼TOKEN
            String authToken = jwtu.validateToken(token);
            if (authToken != null) {
                // 解碼TOKEN
                JSONObject obj = new JSONObject(authToken);
                Integer userId = obj.getInt("userid");


                // 確認commentId存在
                Optional<Comment> existingCommentOpt = commentRepository.findById(commentId);
                if (!existingCommentOpt.isPresent()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment not found");
                }

                Comment existingComment = existingCommentOpt.get();

                // 根據用戶點讚狀態更新點讚數
                Set<Integer> userLikes = likes.getOrDefault(commentId, new HashSet<>());
                if (userLikes.contains(userId)) {
                    existingComment.setGood(existingComment.getGood() - 1); // 取消點讚
                    userLikes.remove(userId);
                } else {
                    existingComment.setGood(existingComment.getGood() + 1); // 增加點讚數
                    userLikes.add(userId);
                }

                likes.put(commentId, userLikes);

                // 保存更新後的評論
                commentRepository.save(existingComment);

                return ResponseEntity.ok("Comment updated successfully");
            } else {
                // 錯誤的TOKEN
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token失效");
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token is missing");
        }
    }
	@PutMapping("/useless/{commentId}")
    public ResponseEntity<String> useless(@PathVariable Integer commentId, @RequestParam String token) {
        if (token != null) {
            // 解碼TOKEN
            String authToken = jwtu.validateToken(token);
            if (authToken != null) {
                // 解碼TOKEN
                JSONObject obj = new JSONObject(authToken);
                Integer userId = obj.getInt("userid");


                // 確認commentId存在
                Optional<Comment> existingCommentOpt = commentRepository.findById(commentId);
                if (!existingCommentOpt.isPresent()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment not found");
                }

                Comment existingComment = existingCommentOpt.get();

                // 根據用戶點讚狀態更新點讚數
                Set<Integer> userless = less.getOrDefault(commentId, new HashSet<>());
                if (userless.contains(userId)) {
                    existingComment.setUseless(existingComment.getUseless() - 1); // 取消點讚
                    userless.remove(userId);
                } else {
                    existingComment.setUseless(existingComment.getUseless() + 1); // 增加點讚數
                    userless.add(userId);
                }

                less.put(commentId, userless);

                // 保存更新後的評論
                commentRepository.save(existingComment);

                return ResponseEntity.ok("Comment updated successfully");
            } else {
                // 錯誤的TOKEN
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token失效");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is missing");
        }
    }
	@PutMapping("/update/{commentId}")
	public ResponseEntity<String> updateback(@PathVariable Integer commentId, @RequestBody Comment updatedComment) {
	   if(updatedComment!=null) { 
		updatedComment.setContent(updatedComment.getContent());
		updatedComment.setRate(updatedComment.getRate());
	                commentRepository.save(updatedComment);
	                return ResponseEntity.ok("Comment deleted successfully");
	   }else {
		   return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("修改失敗");
	   }
	            	
	}

}
	
	


