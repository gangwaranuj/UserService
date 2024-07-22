package com.ms.userservice.services.impl;


import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ms.userservice.entities.Hotel;
import com.ms.userservice.entities.Rating;
import com.ms.userservice.entities.User;
import com.ms.userservice.exceptions.ResourceNotFoundException;
import com.ms.userservice.external.services.IHotelService;
import com.ms.userservice.repository.UserRepository;
import com.ms.userservice.services.IUserService;

@Service
public class UserServiceImpl implements IUserService{


	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private IHotelService hotelService;

	@Override
	public User save(User user) {
		String randomUserId = UUID.randomUUID().toString();
		user.setUserId(randomUserId);
		return userRepo.save(user);
	}

	@Override
	public List<User> getAllUser() {
		return userRepo.findAll();
	}

	@Override
	public User getUserById(String id) {
		// TODO Auto-generated method stub
		User user =  userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("User with given id is not found on server"));
		Rating[] ratings =restTemplate.getForObject("http://RATINGSERVICE/ratings/users/"+user.getUserId(), Rating[].class);
		List<Rating> ratingList = Arrays.asList(ratings).stream().map((rating)->{
			/**
			 * Using with resttemplate
			 */
				    	ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTELSERVICE/hotel/"+rating.getHotelId(), Hotel.class);
					    Hotel hotel = forEntity.getBody();

			//**************************************

			//************using fin-client *******888
//			Hotel hotel = hotelService.getHotelById(rating.getHotelId());
			rating.setHotel(hotel);
			return rating;
		}).collect(Collectors.toList());

		user.setRatings(ratingList);
		return user;

	}

}
