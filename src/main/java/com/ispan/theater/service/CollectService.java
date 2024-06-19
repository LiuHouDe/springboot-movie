package com.ispan.theater.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ispan.theater.domain.Collect;
import com.ispan.theater.domain.User;
import com.ispan.theater.repository.CollectRepository;
import com.ispan.theater.repository.MovieActRepository;
import com.ispan.theater.repository.UserRepository;

import jakarta.servlet.http.HttpSession;


@Service
public class CollectService {
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	MovieActRepository movieActRepository;
	@Autowired
	CollectRepository collectRepository;
	
	public List<Collect> showAllCollect (HttpSession session,Integer id){
		 Optional<User> userOptional =userRepository.findById((Integer)session.getAttribute("loginUserId"));
		 User user =userOptional.get();
		 return collectRepository.findByUserId(user.getId());
		}
	
	
	
	
}
