package val.hor.simulator.controller.exporter;

import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import val.hor.simulator.entity.*;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class ExamPdfExporter extends AbstractExporter{


    public void exportResult(Exam exam, User userTakingExam, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "application/pdf", ".pdf", "exam_" + exam.getId() + "_");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        User userWhoCreated = exam.getWhoCreated();
        createHeader(document, exam, userWhoCreated);

        createUserInfo(document, userTakingExam);

        List<Task> taskList = exam.getTasks();

        int i = 0;
        for (Task task : taskList) {
            createTaskInfo(document, task, i + 1);
            createResultTable(document, task.getResults());
            document.add(new Chunk(Chunk.NEWLINE));
            i++;
        }
        document.close();
    }



    private void createHeader(Document document, Exam exam, User user) throws DocumentException {
        Paragraph testNr = new Paragraph("TEST " + exam.getId(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
        testNr.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(testNr);
        document.add(new Chunk(Chunk.NEWLINE));

        Paragraph userInfo = new Paragraph("erstellt von ", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.ITALIC));
        userInfo.add(new Chunk(user.getFullName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8)));
        userInfo.setAlignment(Element.ALIGN_RIGHT);
        document.add(userInfo);
        document.add(new Chunk(Chunk.NEWLINE));


    }

    private void createUserInfo(Document document, User user) throws DocumentException {
        Paragraph userInfo = new Paragraph("gemacht von ", FontFactory.getFont(FontFactory.HELVETICA, 14, Font.ITALIC));
        userInfo.add(new Chunk(user.getFullName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
        userInfo.setAlignment(Element.ALIGN_LEFT);
        document.add(userInfo);
        document.add(new Chunk(Chunk.NEWLINE));
    }

    private void createTaskInfo(Document document, Task task, int index) throws DocumentException {
        Paragraph taskInfo = new Paragraph("Aufgabe " + index, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
        taskInfo.add(new Chunk(Chunk.NEWLINE));
        taskInfo.add(new Chunk(task.getName(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
        taskInfo.add(new Chunk(Chunk.NEWLINE));
        taskInfo.add(new Chunk(task.getMission(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
        taskInfo.add(new Chunk(Chunk.NEWLINE));
        taskInfo.add(new Chunk("Aufgabestellung:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
        taskInfo.add(new Chunk(Chunk.NEWLINE));
        taskInfo.add(new Chunk(task.getQuestion(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
        taskInfo.add(new Chunk(Chunk.NEWLINE));
        taskInfo.setIndentationLeft(25);
        document.add(taskInfo);

        Paragraph variant = new Paragraph(task.getVariant(), FontFactory.getFont(FontFactory.HELVETICA, 10));
        variant.setIndentationLeft(30);
        document.add(variant);
        document.add(new Chunk(Chunk.NEWLINE));
    }

    private void createResultTable(Document document, List<Result> resultList) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(60);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);

        float[] columnWidths = {550f, 80f};
        table.setWidths(columnWidths);

        for (Result result : resultList) {
            PdfPCell cell1 = createCell(result.getName(), 10, Color.BLACK, Element.ALIGN_LEFT);
            table.addCell(cell1);


            String checkmark;
            if (result.getIsChecked()) {
                checkmark = "< X >";
            } else {
                checkmark = "<   >";
            }
            Color cellColor = result.getIsChecked() == result.getIsRight() ? Color.GREEN : Color.RED;

            PdfPCell cell2 = createCell(checkmark, 10, cellColor, Element.ALIGN_CENTER);
            table.addCell(cell2);
        }

        document.add(table);
    }

    private void createAnswerTable(Document document, List<Answer> answerList) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(60);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);

        float[] columnWidths = {550f, 80f};
        table.setWidths(columnWidths);

        for (Answer answer : answerList) {
            PdfPCell cell1 = createCell(answer.getName(), 10, Color.BLACK, Element.ALIGN_LEFT);
            table.addCell(cell1);
            PdfPCell cell2 = createCell("<    >", 10, Color.BLACK, Element.ALIGN_CENTER);
            table.addCell(cell2);
        }

        document.add(table);
    }

    private PdfPCell createCell(String text, float fontSize, Color color, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, fontSize, color)));
        cell.setBorderColor(Color.BLACK);
        cell.setPadding(5);
        cell.setHorizontalAlignment(alignment);
        return cell;
    }

}
