package com.sujai.test.concurrency;

import com.sujai.test.dto.DeviceDto;
import com.sujai.test.model.Device;
import com.sujai.test.model.DeviceState;
import com.sujai.test.service.DeviceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class DeviceUpdateConcurencyTest {

    @Autowired
    private DeviceService deviceService;

    private DeviceDto setupTestDevice() {
        Device device = new Device("Test Device", "Apple", DeviceState.AVAILABLE);
        return deviceService.createDevice(device);
    }

    @Test
    void testConcurrentUpdatesToSameDevice() throws Exception {
        DeviceDto device = setupTestDevice();
        Long deviceId = device.getId();

        int numberOfThreads = 5;
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(numberOfThreads);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < numberOfThreads; i++) {
            final int threadId = i;
            futures.add(service.submit(() -> {
                try {
                    startLatch.await(); // Wait for the green light
                    Device updateDetails = new Device("Updated Name " + threadId, "Apple", DeviceState.IN_USE);
                    deviceService.updateDevice(deviceId, updateDetails);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failureCount.incrementAndGet();
                } finally {
                    finishLatch.countDown();
                }
            }));
        }

        startLatch.countDown(); // Release all threads at once
        finishLatch.await();    // Wait for all threads to finish

        // With optimistic locking, only one thread should succeed
        // The others should fail with OptimisticLockingFailureException
        assertThat(successCount.get()).isGreaterThan(0);
        assertThat(failureCount.get()).isGreaterThan(0);
        assertThat(successCount.get() + failureCount.get()).isEqualTo(numberOfThreads);

        service.shutdown();
    }

    @Test
    void testConcurrentReads() throws Exception {
        DeviceDto device = setupTestDevice();
        Long deviceId = device.getId();

        int numberOfThreads = 10;
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(numberOfThreads);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        for (int i = 0; i < numberOfThreads; i++) {
            service.submit(() -> {
                try {
                    startLatch.await();
                    DeviceDto found = deviceService.getDeviceById(deviceId);
                    assertThat(found).isNotNull();
                    assertThat(found.getId()).isEqualTo(deviceId);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failureCount.incrementAndGet();
                } finally {
                    finishLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        finishLatch.await();

        // All reads should succeed
        assertThat(successCount.get()).isEqualTo(numberOfThreads);
        assertThat(failureCount.get()).isEqualTo(0);

        service.shutdown();
    }

    @Test
    void testConcurrentCreates() throws Exception {
        int numberOfThreads = 5;
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(numberOfThreads);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        for (int i = 0; i < numberOfThreads; i++) {
            final int threadId = i;
            service.submit(() -> {
                try {
                    startLatch.await();
                    Device newDevice = new Device("Concurrent Device " + threadId, "Brand" + threadId, DeviceState.AVAILABLE);
                    deviceService.createDevice(newDevice);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failureCount.incrementAndGet();
                } finally {
                    finishLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        finishLatch.await();

        // All creates should succeed
        assertThat(successCount.get()).isEqualTo(numberOfThreads);
        assertThat(failureCount.get()).isEqualTo(0);

        service.shutdown();
    }

    @Test
    void testConcurrentDeletesOfDifferentDevices() throws Exception {
        // First create some devices to delete
        List<DeviceDto> devicesToDelete = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Device device = new Device("Device to Delete " + i, "DeleteBrand", DeviceState.AVAILABLE);
            DeviceDto saved = deviceService.createDevice(device);
            devicesToDelete.add(saved);
        }

        int numberOfThreads = 5;
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(numberOfThreads);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        for (int i = 0; i < numberOfThreads; i++) {
            final int index = i;
            service.submit(() -> {
                try {
                    startLatch.await();
                    DeviceDto device = devicesToDelete.get(index);
                    deviceService.deleteDevice(device.getId());
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failureCount.incrementAndGet();
                } finally {
                    finishLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        finishLatch.await();

        // All deletes should succeed
        assertThat(successCount.get()).isEqualTo(numberOfThreads);
        assertThat(failureCount.get()).isEqualTo(0);

        service.shutdown();
    }

    @Test
    void testConcurrentMixedOperations() throws Exception {
        DeviceDto device = setupTestDevice();
        Long deviceId = device.getId();

        int numberOfThreads = 10;
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(numberOfThreads);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        for (int i = 0; i < numberOfThreads; i++) {
            final int threadId = i;
            service.submit(() -> {
                try {
                    startLatch.await();
                    switch (threadId % 3) {
                        case 0 -> {
                            // Read
                            deviceService.getDeviceById(deviceId);
                        }
                        case 1 -> {
                            // Create
                            Device newDevice = new Device("Mixed Op " + threadId, "MixedBrand", DeviceState.AVAILABLE);
                            deviceService.createDevice(newDevice);
                        }
                        case 2 -> {
                            // Update
                            Device updateDetails = new Device("Mixed Update " + threadId, "Apple", DeviceState.IN_USE);
                            deviceService.updateDevice(deviceId, updateDetails);
                        }
                    }
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failureCount.incrementAndGet();
                } finally {
                    finishLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        finishLatch.await();

        // Most operations should succeed, some updates may fail due to optimistic locking
        assertThat(successCount.get()).isGreaterThan(0);
        assertThat(successCount.get() + failureCount.get()).isEqualTo(numberOfThreads);

        service.shutdown();
    }
}
