package val.hor.simulator.service;

import org.springframework.stereotype.Service;
import val.hor.simulator.entity.setting.EmailSettingBag;
import val.hor.simulator.entity.setting.GeneralSettingBag;
import val.hor.simulator.entity.setting.Setting;
import val.hor.simulator.entity.setting.SettingCategory;
import val.hor.simulator.repository.SettingsRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class SettingService {

    private final SettingsRepository settingsRepository;

    public SettingService(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }


    public List<Setting> listAllSettings(){
       return settingsRepository.findAll();
    }

    public GeneralSettingBag getGeneralSettingBag() {
        List<Setting> settings = new ArrayList<>();

        List<Setting> generalSetting = settingsRepository.findByCategory(SettingCategory.GENERAL);
        settings.addAll(generalSetting);
        return new GeneralSettingBag(settings);
    }

    public List<Setting> getGeneralSettings() {
       return settingsRepository.findByCategory(SettingCategory.GENERAL);
    }

    public void saveAll(Iterable<Setting> settings){
        settingsRepository.saveAll(settings);
    }


    public List<Setting> getMailServerSettings(){
        return settingsRepository.findByCategory(SettingCategory.MAIL_SERVER);
    }

    public List<Setting> getMailTemplatesSettings(){
        return settingsRepository.findByCategory(SettingCategory.MAIL_TEMPLATES);
    }

    public EmailSettingBag getEmailSettings(){
        List<Setting> settings = getMailServerSettings();
        settings.addAll(getMailTemplatesSettings());

        return new EmailSettingBag(settings);
    }









}
