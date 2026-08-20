package com.sujai.test.repository;

import com.sujai.test.model.Device;
import com.sujai.test.model.DeviceState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    Page<Device> findByBrand(String brand, Pageable pageable);

    Page<Device> findByState(DeviceState state, Pageable pageable);

    List<Device> findByBrandAndState(String brand, DeviceState state);

    @Query("SELECT DISTINCT d.brand FROM Device d")
    List<String> findDistinctBrands();
}
