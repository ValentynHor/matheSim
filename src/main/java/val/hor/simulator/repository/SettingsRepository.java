package val.hor.simulator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import val.hor.simulator.entity.setting.Setting;
import val.hor.simulator.entity.setting.SettingCategory;

import java.util.List;

public interface SettingsRepository extends JpaRepository<Setting,Integer> {

    public List<Setting> findByCategory (SettingCategory category);

    @Query("SELECT s FROM Setting s WHERE s.category = ?1 OR s.category = ?2")
    public List<Setting> findByTwoCategory (SettingCategory catOne, SettingCategory catTwo);
}
