package edu.sabanciuniv.hotelbookingapp.service.impl;

import edu.sabanciuniv.hotelbookingapp.model.Address;
import edu.sabanciuniv.hotelbookingapp.model.dto.AddressDTO;
import edu.sabanciuniv.hotelbookingapp.repository.AddressRepository;
import edu.sabanciuniv.hotelbookingapp.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    public Address saveAddress(AddressDTO addressDTO) {
        log.info("Attempting to save a new address: {}", addressDTO.toString());
        Address address = mapAddressDtoToAddress(addressDTO);

        Address savedAddress = addressRepository.save(address);
        log.info("Successfully saved new address with ID: {}", address.getId());
        return savedAddress;
    }

    private Address mapAddressDtoToAddress(AddressDTO dto) {
        return Address.builder()
                .addressLine(dto.getAddressLine())
                .city(dto.getCity())
                .country(dto.getCountry())
                .build();
    }

    private AddressDTO mapAddressToAddressDto(Address address) {
        return AddressDTO.builder()
                .id(address.getId())
                .addressLine(address.getAddressLine())
                .city(address.getCity())
                .country(address.getCountry())
                .build();
    }

}
