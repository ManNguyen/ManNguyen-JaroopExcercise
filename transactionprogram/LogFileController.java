package transactionprogram;

import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;
import static javax.xml.bind.DatatypeConverter.parseFloat;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 *  LogFileCOntroller use JSoup library to access the log.html 
 */
public class LogFileController {

    private final Document LogFile;
    private final String FilePath;
    private final Elements TransactionsBody;

    //Constructor use the path of the log.html file and parse it to Jsoup Document
    //Throws IOException when path not found or transactions table not found
    public LogFileController(String filePath) throws IOException {
        File file = new File(filePath);
        FilePath = filePath;
        LogFile = Jsoup.parse(file, "UTF-8", "");
        TransactionsBody = LogFile.select("table[id=transactions]").select("tbody");
        if (!TransactionsBody.hasText()){
            throw new IOException("Log file do not contain transactions table");
        }
    }

    // Traverse to the body of the table with id = transactions then append new row with contain value 
    public void Append(float value) {
        TransactionsBody.append(String.format("<tr><td>%.2f</td></tr>", value));
    }

    //Loop through rows in transactions body and sum of the balance
    public float GetBalance() {
        Elements rows = TransactionsBody.select("tr");
        float balance = 0;
        for (int i = 0; i < rows.size(); i++) {
            String val = rows.get(i).select("td").text();
            balance += parseFloat(val);
        }
        return balance;
    }

    //Save the transaction back to the FilePath, throws IOException when could not find FilePath 
    public void Save() throws IOException {
        try (PrintWriter writer = new PrintWriter(FilePath, "UTF-8")) {
            writer.print(LogFile.outerHtml());
        }
    }

}
