package val.hor.simulator.controller.exporter;

import jakarta.servlet.http.HttpServletResponse;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import val.hor.simulator.entity.User;

import java.io.IOException;
import java.util.List;


public class UserCsvExporter extends  AbstractExporter{

    public void export(List<User> userList, HttpServletResponse response) throws IOException {

        super.setResponseHeader(response,"text/csv",".csv","users_");
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"User ID", "Email", "First Name", "Last Name", "Roles", "Enabled"};
        String[] fieldMapping = {"id", "email", "firstName", "lastName", "roles", "enabled"};
        csvWriter.writeHeader(csvHeader);
        for(User user: userList){
            csvWriter.write(user, fieldMapping);
        }
        csvWriter.close();
    }
}
