package com.sesac.alltaxi.service;

@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;

    public Driver saveTaxiDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    public Optional<Driver> getTaxiDriverById(Long id) {
        return driverRepository.findById(id);
    }
}
