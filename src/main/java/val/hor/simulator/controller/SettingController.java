package val.hor.simulator.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import val.hor.simulator.entity.setting.GeneralSettingBag;
import val.hor.simulator.entity.setting.Setting;
import val.hor.simulator.service.SettingService;

import java.util.List;

@Controller
public class SettingController {

    private final SettingService settingService;

    public SettingController(SettingService settingService) {
        this.settingService = settingService;
    }

   @GetMapping("/settings")
    public String listAll(Model model){
        List<Setting> settingList = settingService.listAllSettings();

        for(Setting setting : settingList){
            model.addAttribute(setting.getKey(),setting.getValue());
        }
        return "settings/settings";
    }

    @PostMapping("/settings/save_general")
    public String saveGeneralSettings(HttpServletRequest request, RedirectAttributes redirectAttributes) {

        GeneralSettingBag settingBag = settingService.getGeneralSettingBag();

        updateSettingValuesFromForm(request, settingBag.list());

        redirectAttributes.addFlashAttribute("message", "General settings have been saved.");
        return "redirect:/settings";
    }

    @PostMapping("/settings/save_mail_server")
    public String saveMailServerSettings(HttpServletRequest request, RedirectAttributes redirectAttributes){

        List<Setting> mailServerSettings = settingService.getMailServerSettings();

        updateSettingValuesFromForm(request,mailServerSettings);

        redirectAttributes.addFlashAttribute("message","Mail Server settings have been saved.");
        return "redirect:/settings";
    }

    @PostMapping("/settings/save_mail_templates")
    public String saveMailTemplatesSettings(HttpServletRequest request, RedirectAttributes redirectAttributes){

        List<Setting> mailServerSettings = settingService.getMailTemplatesSettings();

        updateSettingValuesFromForm(request,mailServerSettings);

        redirectAttributes.addFlashAttribute("message","Mail Templates settings have been saved.");
        return "redirect:/settings";
    }

    private void updateSettingValuesFromForm(HttpServletRequest request, List<Setting> settingsList){
        for(Setting setting : settingsList){
            String value = request.getParameter(setting.getKey());
            if(value != null){
                setting.setValue(value);
            }
        }
        settingService.saveAll(settingsList);
    }


}
