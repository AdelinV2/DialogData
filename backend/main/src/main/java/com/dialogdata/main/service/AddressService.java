package com.dialogdata.main.service;

import com.dialogdata.main.entity.Address;
import com.dialogdata.main.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public Address findById(Integer id) {
        return addressRepository.findById(id).orElse(null);
    }

    public Address create(Address address) {
        return addressRepository.save(address);
    }

    public Address update(Integer id, Address address) {

        Address existingAddress = addressRepository.findById(id).orElse(null);

        if (existingAddress == null) {
            return null;
        }

        address.setId(id);

        return addressRepository.save(address);
    }

    public boolean deleteById(Integer id) {

        Address address = addressRepository.findById(id).orElse(null);

        if (address == null) {
            return false;
        }

        addressRepository.delete(address);

        return true;
    }
}
