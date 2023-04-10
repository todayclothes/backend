package com.seungah.todayclothes.repository;

        import com.seungah.todayclothes.entity.HourlyWeather;
        import com.seungah.todayclothes.entity.Region;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.stereotype.Repository;

        import java.time.LocalDateTime;

@Repository
public interface HourlyWeatherRepository extends JpaRepository<HourlyWeather, Long> {
    HourlyWeather findByDateAndRegion(LocalDateTime date, Region region);
}
