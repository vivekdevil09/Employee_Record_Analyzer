import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class EmployeeRecord {
    private String name;
    private String position;
    private Date startTime;
    private Date endTime;

    public EmployeeRecord(String name, String position, Date startTime, Date endTime) {
        this.name = name;
        this.position = position;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }
}

public class EmployeeAnalyzer {
    public static void main(String[] args) {
        // Replace with the path to your text file
        String filePath = "F:/Assignment_Timecard.txt";

        List<EmployeeRecord> employeeRecords = new ArrayList<>();

        // Read and parse the text file
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String name = parts[0].trim();
                    String position = parts[1].trim();
                    Date startTime = dateFormat.parse(parts[2].trim());
                    Date endTime = dateFormat.parse(parts[3].trim());
                    employeeRecords.add(new EmployeeRecord(name, position, startTime, endTime));
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return;
        }

        // Analyze the employee records
        for (int i = 0; i < employeeRecords.size(); i++) {
            EmployeeRecord currentRecord = employeeRecords.get(i);
            boolean consecutiveDays = true;
            boolean lessThan10Hours = false;
            boolean moreThan14Hours = false;

            for (int j = i + 1; j < employeeRecords.size() && j < i + 7; j++) {
                EmployeeRecord nextRecord = employeeRecords.get(j);

                // Check consecutive days
                long diffInMillies = Math.abs(nextRecord.getStartTime().getTime() - currentRecord.getEndTime().getTime());
                long hoursBetweenShifts = diffInMillies / (60 * 60 * 1000);

                if (hoursBetweenShifts >= 24) {
                    consecutiveDays = false;
                    break;
                }

                // Check less than 10 hours between shifts
                if (hoursBetweenShifts > 1 && hoursBetweenShifts < 10) {
                    lessThan10Hours = true;
                }

                // Check more than 14 hours in a single shift
                long shiftDuration = (nextRecord.getEndTime().getTime() - currentRecord.getStartTime().getTime()) / (60 * 60 * 1000);
                if (shiftDuration > 14) {
                    moreThan14Hours = true;
                }
            }

            if (consecutiveDays) {
                System.out.println(currentRecord.getName() + " worked for 7 consecutive days as " + currentRecord.getPosition());
            }
            if (lessThan10Hours) {
                System.out.println(currentRecord.getName() + " has less than 10 hours between shifts as " + currentRecord.getPosition());
            }
            if (moreThan14Hours) {
                System.out.println(currentRecord.getName() + " worked for more than 14 hours in a single shift as " + currentRecord.getPosition());
            }
        }
    }
}
