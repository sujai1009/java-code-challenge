package com.sujai.test.validation;

import com.sujai.test.model.Device;
import com.sujai.test.model.DeviceState;
import com.sujai.test.service.DeviceService;
import org.springframework.stereotype.Component;

@Component
public class DeviceValidator {

    public void validateUpdate(Device existing, Device incoming) {
        switch (existing.getState()) {
            case DeviceState.IN_USE -> {
                if (incoming.getName() != null && !incoming.getName().equals(existing.getName())) {
                    throw new DeviceService.DeviceUpdateException("Cannot update name of a device that is in use");
                }
                if (incoming.getBrand() != null && !incoming.getBrand().equals(existing.getBrand())) {
                    throw new DeviceService.DeviceUpdateException("Cannot update brand of a device that is in use");
                }
            }
            default -> {
            }
        }
    }

    public void validateDelete(Device device) {
        switch (device.getState()) {
            case DeviceState.IN_USE -> throw new DeviceService.DeviceDeleteException("Cannot delete a device that is in use");
            default -> {
            }
        }
    }
}
