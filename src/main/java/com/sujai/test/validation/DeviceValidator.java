package com.sujai.test.validation;

import com.sujai.test.model.Device;
import com.sujai.test.model.DeviceState;
import com.sujai.test.service.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DeviceValidator {

    private static final Logger logger = LoggerFactory.getLogger(DeviceValidator.class);

    public void validateUpdate(Device existing, Device incoming) {
        logger.debug("Validating update for device id: {}, current state: {}", existing.getId(), existing.getState());
        switch (existing.getState()) {
            case DeviceState.IN_USE -> {
                if (incoming.getName() != null && !incoming.getName().equals(existing.getName())) {
                    logger.warn("Cannot update name of device id: {} because it is in use", existing.getId());
                    throw new DeviceService.DeviceUpdateException("Cannot update name of a device that is in use");
                }
                if (incoming.getBrand() != null && !incoming.getBrand().equals(existing.getBrand())) {
                    logger.warn("Cannot update brand of device id: {} because it is in use", existing.getId());
                    throw new DeviceService.DeviceUpdateException("Cannot update brand of a device that is in use");
                }
            }
            default -> {
            }
        }
    }

    public void validateDelete(Device device) {
        logger.debug("Validating delete for device id: {}, current state: {}", device.getId(), device.getState());
        switch (device.getState()) {
            case DeviceState.IN_USE -> {
                logger.warn("Cannot delete device id: {} because it is in use", device.getId());
                throw new DeviceService.DeviceDeleteException("Cannot delete a device that is in use");
            }
            default -> {
            }
        }
    }
}
