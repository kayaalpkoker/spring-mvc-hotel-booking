package edu.sabanciuniv.hotelbookingapp.service;

import edu.sabanciuniv.hotelbookingapp.model.Address;
import edu.sabanciuniv.hotelbookingapp.model.dto.AddressDTO;

public interface AddressService {

    Address saveAddress(AddressDTO addressDTO);

    AddressDTO findAddressById(Long id);

    Address updateAddress(AddressDTO addressDTO);

    void deleteAddress(Long id);

    Address mapAddressDtoToAddress(AddressDTO dto);

    AddressDTO mapAddressToAddressDto(Address address);


}
