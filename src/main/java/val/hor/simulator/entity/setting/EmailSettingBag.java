package val.hor.simulator.entity.setting;

import java.util.List;

public class EmailSettingBag extends SettingBag{
    public EmailSettingBag(List<Setting> settingList) {
        super(settingList);
    }
    public String getHost(){
        return super.getValue("MAIL_HOST");
    }
    public String getUsername(){
        return  super.getValue("MAIL_USERNAME");
    }
    public int getPort(){return  Integer.parseInt(super.getValue("MAIL_PORT"));}

    public String getPassword(){return  super.getValue("MAIL_PASSWORD");}
    public String getSmtpAuth(){
        return  super.getValue("SMTP_AUTH");
    }
    public String getSenderName(){
        return  super.getValue("MAIL_SENDER_NAME");
    }
    public String getSmtpSecured(){
        return  super.getValue("SMTP_SECURED");
    }
    public String getFromAddress(){
        return  super.getValue("MAIL_FROM");
    }

    public String getStudentVerifySubject(){
        return  super.getValue("USER_VERIFY_SUBJECT");
    }
    public String getStudentVerifyContent(){
        return  super.getValue("USER_VERIFY_CONTENT");
    }
}
