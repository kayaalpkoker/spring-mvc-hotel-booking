package edu.sabanciuniv.hotelbookingapp.service;

import edu.sabanciuniv.hotelbookingapp.model.HotelManager;
import edu.sabanciuniv.hotelbookingapp.model.User;

public interface HotelManagerService {

    HotelManager findByUser(User user);

}
