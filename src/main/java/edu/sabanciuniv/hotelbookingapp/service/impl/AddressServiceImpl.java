package edu.sabanciuniv.hotelbookingapp.service.impl;

import edu.sabanciuniv.hotelbookingapp.model.Address;
import edu.sabanciuniv.hotelbookingapp.model.dto.AddressDTO;
import edu.sabanciuniv.hotelbookingapp.repository.AddressRepository;
import edu.sabanciuniv.hotelbookingapp.service.AddressService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    @Override
    public AddressDTO findAddressById(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));

        return mapAddressToAddressDto(address);
    }

    @Override
    public Address updateAddress(AddressDTO addressDTO) {
        log.info("Attempting to update address with ID: {}", addressDTO.getId());
        Address existingAddress = addressRepository.findById(addressDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));

        setFormattedDataToAddress(existingAddress, addressDTO);

        Address updatedAddress = addressRepository.save(existingAddress);
        log.info("Successfully updated address with ID: {}", existingAddress.getId());
        return updatedAddress;
    }

    @Override
    public void deleteAddress(Long id) {
        log.info("Attempting to delete address with ID: {}", id);
        if (!addressRepository.existsById(id)) {
            log.error("Failed to delete address. Address with ID: {} not found", id);
            throw new EntityNotFoundException("Address not found");
        }
        addressRepository.deleteById(id);
        log.info("Successfully deleted address with ID: {}", id);
    }

    @Override
    public Address mapAddressDtoToAddress(AddressDTO dto) {
        return Address.builder()
                .addressLine(formatText(dto.getAddressLine()))
                .city(formatText(dto.getCity()))
                .country(formatText(dto.getCountry()))
                .build();
    }

    @Override
    public AddressDTO mapAddressToAddressDto(Address address) {
        return AddressDTO.builder()
                .id(address.getId())
                .addressLine(address.getAddressLine())
                .city(address.getCity())
                .country(address.getCountry())
                .build();
    }

    private String formatText(String text) {
        return StringUtils.capitalize(text.trim());
    }

    private void setFormattedDataToAddress(Address address, AddressDTO addressDTO) {
        address.setAddressLine(formatText(addressDTO.getAddressLine()));
        address.setCity(formatText(addressDTO.getCity()));
        address.setCountry(formatText(addressDTO.getCountry()));
    }
}
