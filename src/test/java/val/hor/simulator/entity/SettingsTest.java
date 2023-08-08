package val.hor.simulator.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import val.hor.simulator.entity.setting.Setting;
import val.hor.simulator.entity.setting.SettingCategory;
import val.hor.simulator.repository.SettingsRepository;

import java.util.ArrayList;
import java.util.List;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class SettingsTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private SettingsRepository settingsRepo;

    @Test
    public void createGeneralSettings(){

        Setting siteName = new Setting("SITE_NAME", "*******", SettingCategory.GENERAL);
        Setting copyright = new Setting("COPYRIGHT", "*******.", SettingCategory.GENERAL);

        //settingsRepo.saveAll(List.of(siteName,copyright));
    }

    @Test
    public void createMailServerSettings() {

        List <Setting> settings = new ArrayList<>();
        settings.add(new Setting("MAIL_HOST","smtp.gmail.com",SettingCategory.MAIL_SERVER));
        settings.add(new Setting("MAIL_PORT","587",SettingCategory.MAIL_SERVER));
        settings.add(new Setting("MAIL_USERNAME","**********",SettingCategory.MAIL_SERVER));
        settings.add(new Setting("MAIL_PASSWORD","**********",SettingCategory.MAIL_SERVER));
        settings.add(new Setting("SMTP_AUTH","true",SettingCategory.MAIL_SERVER));
        settings.add(new Setting("SMTP_SECURED","true",SettingCategory.MAIL_SERVER));
        settings.add(new Setting("MAIL_FROM","**********",SettingCategory.MAIL_SERVER));
        settings.add(new Setting("MAIL_SENDER_NAME","*********",SettingCategory.MAIL_SERVER));
        settings.add(new Setting("USER_VERIFY_SUBJECT","Please verify your registration to continue",SettingCategory.MAIL_TEMPLATES));
        settings.add(new Setting("USER_VERIFY_CONTENT","Dear [[name]], please click the verification link below to verify your registration:\n" +
                "[[link]]\n" +
                "Thank you,\n" +
                "The MatheSim Team",SettingCategory.MAIL_TEMPLATES));

        //settingsRepo.saveAll(settings);

    }











}
