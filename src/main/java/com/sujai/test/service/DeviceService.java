package com.sujai.test.service;

import com.sujai.test.dto.DeviceDto;
import com.sujai.test.model.Device;
import com.sujai.test.model.DeviceState;
import com.sujai.test.repository.DeviceRepository;
import com.sujai.test.validation.DeviceValidator;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class DeviceService {

    private static final Logger logger = LoggerFactory.getLogger(DeviceService.class);

    private final DeviceRepository deviceRepository;
    private final DeviceValidator deviceValidator;

    public DeviceService(DeviceRepository deviceRepository, DeviceValidator deviceValidator) {
        this.deviceRepository = deviceRepository;
        this.deviceValidator = deviceValidator;
    }

    public DeviceDto createDevice(Device device) {
        logger.info("Creating new device with name: {}, brand: {}", device.getName(), device.getBrand());
        Device saved = deviceRepository.save(device);
        logger.info("Device created successfully with id: {}", saved.getId());
        return DeviceDto.from(saved);
    }

    public Page<DeviceDto> getAllDevices(Pageable pageable) {
        logger.debug("Fetching all devices with pagination: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        return deviceRepository.findAll(pageable).map(DeviceDto::from);
    }

    public DeviceDto getDeviceById(Long id) {
        logger.debug("Fetching device by id: {}", id);
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found with id: " + id));
        return DeviceDto.from(device);
    }

    public Page<DeviceDto> getDevicesByBrand(String brand, Pageable pageable) {
        logger.debug("Fetching devices by brand: {}, pagination: page={}, size={}", brand, pageable.getPageNumber(), pageable.getPageSize());
        return deviceRepository.findByBrand(brand, pageable).map(DeviceDto::from);
    }

    public Page<DeviceDto> getDevicesByState(DeviceState state, Pageable pageable) {
        logger.debug("Fetching devices by state: {}, pagination: page={}, size={}", state, pageable.getPageNumber(), pageable.getPageSize());
        return deviceRepository.findByState(state, pageable).map(DeviceDto::from);
    }

    public List<String> getAllDistinctBrands() {
        logger.debug("Fetching all distinct brands");
        return deviceRepository.findDistinctBrands();
    }

    public DeviceDto updateDevice(Long id, Device deviceDetails) {
        logger.info("Updating device with id: {}", id);
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found with id: " + id));

        deviceValidator.validateUpdate(device, deviceDetails);

        if (StringUtils.hasText(deviceDetails.getName())) {
            device.setName(deviceDetails.getName());
        }

        if (StringUtils.hasText(deviceDetails.getBrand())) {
            device.setBrand(deviceDetails.getBrand());
        }

        if (deviceDetails.getState() != null) {
            device.setState(deviceDetails.getState());
        }

        Device saved = deviceRepository.save(device);
        logger.info("Device updated successfully with id: {}", saved.getId());
        return DeviceDto.from(saved);
    }

    public void deleteDevice(Long id) {
        logger.info("Deleting device with id: {}", id);
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found with id: " + id));
        deviceValidator.validateDelete(device);
        deviceRepository.delete(device);
        logger.info("Device deleted successfully with id: {}", id);
    }

    public static class DeviceNotFoundException extends RuntimeException {
        public DeviceNotFoundException(String message) {
            super(message);
        }
    }

    public static class DeviceUpdateException extends RuntimeException {
        public DeviceUpdateException(String message) {
            super(message);
        }
    }

    public static class DeviceDeleteException extends RuntimeException {
        public DeviceDeleteException(String message) {
            super(message);
        }
    }
}
