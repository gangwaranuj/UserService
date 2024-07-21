package com.ms.userservice.external.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ms.userservice.entities.Hotel;

@FeignClient(name="HOTELSERVICE")
public interface IHotelService {
	
	@GetMapping("/hotel/{id}")
	Hotel getHotelById(@PathVariable String id);

}
