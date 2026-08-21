package com.sujai.test.validation;

import com.sujai.test.model.Device;
import com.sujai.test.model.DeviceState;
import com.sujai.test.service.DeviceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class DeviceValidatorTest {

    @Test
    void shouldThrowWhenUpdatingNameOfInUseDevice() {
        DeviceValidator validator = new DeviceValidator();
        Device existing = new Device("iPhone 15", "Apple", DeviceState.IN_USE);
        existing.setId(1L);
        Device incoming = new Device("iPhone 15 Pro", "Apple", DeviceState.IN_USE);

        assertThatThrownBy(() -> validator.validateUpdate(existing, incoming))
                .isInstanceOf(DeviceService.DeviceUpdateException.class)
                .hasMessageContaining("Cannot update name of a device that is in use");
    }

    @Test
    void shouldThrowWhenUpdatingBrandOfInUseDevice() {
        DeviceValidator validator = new DeviceValidator();
        Device existing = new Device("iPhone 15", "Apple", DeviceState.IN_USE);
        existing.setId(1L);
        Device incoming = new Device("iPhone 15", "Samsung", DeviceState.IN_USE);

        assertThatThrownBy(() -> validator.validateUpdate(existing, incoming))
                .isInstanceOf(DeviceService.DeviceUpdateException.class)
                .hasMessageContaining("Cannot update brand of a device that is in use");
    }

    @Test
    void shouldAllowUpdateOfAvailableDevice() {
        DeviceValidator validator = new DeviceValidator();
        Device existing = new Device("iPhone 15", "Apple", DeviceState.AVAILABLE);
        existing.setId(1L);
        Device incoming = new Device("iPhone 15 Pro", "Apple", DeviceState.IN_USE);

        // Should not throw
        validator.validateUpdate(existing, incoming);
    }

    @Test
    void shouldAllowUpdateOfInactiveDevice() {
        DeviceValidator validator = new DeviceValidator();
        Device existing = new Device("iPhone 15", "Apple", DeviceState.INACTIVE);
        existing.setId(1L);
        Device incoming = new Device("iPhone 15 Pro", "Apple", DeviceState.AVAILABLE);

        // Should not throw
        validator.validateUpdate(existing, incoming);
    }

    @Test
    void shouldThrowWhenDeletingInUseDevice() {
        DeviceValidator validator = new DeviceValidator();
        Device device = new Device("iPhone 15", "Apple", DeviceState.IN_USE);
        device.setId(1L);

        assertThatThrownBy(() -> validator.validateDelete(device))
                .isInstanceOf(DeviceService.DeviceDeleteException.class)
                .hasMessageContaining("Cannot delete a device that is in use");
    }

    @Test
    void shouldAllowDeletionOfAvailableDevice() {
        DeviceValidator validator = new DeviceValidator();
        Device device = new Device("iPhone 15", "Apple", DeviceState.AVAILABLE);
        device.setId(1L);

        // Should not throw
        validator.validateDelete(device);
    }

    @Test
    void shouldAllowDeletionOfInactiveDevice() {
        DeviceValidator validator = new DeviceValidator();
        Device device = new Device("iPhone 15", "Apple", DeviceState.INACTIVE);
        device.setId(1L);

        // Should not throw
        validator.validateDelete(device);
    }
}
