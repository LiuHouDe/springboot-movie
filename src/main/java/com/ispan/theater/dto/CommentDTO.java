package com.ispan.theater.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import jakarta.persistence.Transient;

public class CommentDTO {
    private Integer commentId;
	private String content;
	private BigDecimal rate;
	private Integer pid;
	private String target;
	private LocalDateTime createtime;
	private Integer useful;
	private Integer useless;
	private Integer good;

    private Integer userId;
    private String email;
    private String userPhoto;
    private String userLastname;
    private Integer movieId;
    private String categoryCode;
    private String description;
    private String director;
    private byte[] image;
    private Date modifyDate;
    private String name;
    private String nameEng;
    private String ratedCode;
    
    private List<CommentDTO> children;

    // Constructor with all fields
    public CommentDTO(Integer commentId, String content, BigDecimal rate, Integer pid, String target,
                      LocalDateTime createtime, Integer useful, Integer useless, Integer good, Integer userId,
                      String email, String userPhoto, String userLastname, Integer movieId, String categoryCode,
                      String description, String director, byte[] image, Date modifyDate, String name,
                      String nameEng, String ratedCode) {
        this.setCommentId(commentId);
        this.setContent(content);
        this.setRate(rate);
        this.setPid(pid);
        this.setTarget(target);
        this.setCreatetime(createtime);
        this.setUseful(useful);
        this.setUseless(useless);
        this.setGood(good);
        this.setUserId(userId);
        this.setEmail(email);
        this.setUserPhoto(userPhoto);
        this.setUserLastname(userLastname);
        this.setMovieId(movieId);
        this.setCategoryCode(categoryCode);
        this.setDescription(description);
        this.setDirector(director);
        this.setImage(image);
        this.setModifyDate(modifyDate);
        this.setName(name);
        this.setNameEng(nameEng);
        this.setRatedCode(ratedCode);
    }

    // Getters and setters
    // ... existing getters and setters

    @Transient
    public List<CommentDTO> getChildren() {
        return children;
    }

    public void setChildren(List<CommentDTO> children) {
        this.children = children;
    }

	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public LocalDateTime getCreatetime() {
		return createtime;
	}

	public void setCreatetime(LocalDateTime createtime) {
		this.createtime = createtime;
	}

	public Integer getUseful() {
		return useful;
	}

	public void setUseful(Integer useful) {
		this.useful = useful;
	}

	public Integer getUseless() {
		return useless;
	}

	public void setUseless(Integer useless) {
		this.useless = useless;
	}

	public Integer getGood() {
		return good;
	}

	public void setGood(Integer good) {
		this.good = good;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}

	public String getUserLastname() {
		return userLastname;
	}

	public void setUserLastname(String userLastname) {
		this.userLastname = userLastname;
	}

	public Integer getMovieId() {
		return movieId;
	}

	public void setMovieId(Integer movieId) {
		this.movieId = movieId;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameEng() {
		return nameEng;
	}

	public void setNameEng(String nameEng) {
		this.nameEng = nameEng;
	}

	public String getRatedCode() {
		return ratedCode;
	}

	public void setRatedCode(String ratedCode) {
		this.ratedCode = ratedCode;
	}
}
