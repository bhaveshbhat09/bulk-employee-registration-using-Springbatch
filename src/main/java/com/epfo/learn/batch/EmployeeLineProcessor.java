package com.epfo.learn.batch;

import com.epfo.learn.validation.AadhaarValidator;
import com.epfo.learn.validation.PanValidator;
import com.epfo.learn.validation.PassportValidator;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.batch.item.ItemProcessor;

public class EmployeeLineProcessor implements ItemProcessor<RawLine, LineResult> {

    private static final String DELIMITER = "#~#";
    private static final int EXPECTED_FIELDS = 6;
    private static final Pattern MOBILE = Pattern.compile("[6-9][0-9]{9}");

    

    @Override
    public LineResult process(RawLine raw) {
        int n = raw.getLineNumber();
        List<LineError> errors = new ArrayList<>();
        String text = raw.getText();

        if (text == null || text.trim().isEmpty()) {
            errors.add(new LineError(n, "line", "Line is empty"));
              return new LineResult(n, text, errors);
        }

        String[] f = text.split(Pattern.quote(DELIMITER), -1);
        if (f.length != EXPECTED_FIELDS) {
            errors.add(new LineError(n, "line",
                    "Expected " + EXPECTED_FIELDS + " fields but found " + f.length));
               return new LineResult(n, text, errors);
        }

        EmployeeRecord r = new EmployeeRecord();
        r.setName(f[0].trim());
        r.setMobile(f[1].trim());
        r.setAadhaar(f[2].trim());
        r.setPan(f[3].trim());
        r.setPassport(f[4].trim());
        r.setDob(f[5].trim());

        add(errors, n, "name", validateName(r.getName()));
        add(errors, n, "mobile", validateMobile(r.getMobile()));
        add(errors, n, "aadhaar", AadhaarValidator.validate(r.getAadhaar()));
        add(errors, n, "pan", PanValidator.validate(r.getPan()));
        add(errors, n, "passport", PassportValidator.validate(r.getPassport()));
        add(errors, n, "dob", validateDob(r.getDob()));

            return errors.isEmpty() ? null : new LineResult(n, text, errors);
    }

    private void add(List<LineError> errors, int line, String field, String reason) {
        if (reason != null) {
            errors.add(new LineError(line, field, reason));
        }
    }

    private String validateName(String name) {
        if (name.isEmpty()) return "Name is required";
        if (name.length() > 100) return "Name is too long (max 100 characters)";
        return null;
    }

    private String validateMobile(String mobile) {
        if (!MOBILE.matcher(mobile).matches()) {
            return "Mobile must be 10 digits starting with 6, 7, 8 or 9";
        }
        return null;
    }

    private String validateDob(String dob) {
        if (dob.isEmpty()) return "Date of birth is required";
        try {
            LocalDate d = LocalDate.parse(dob);
            if (d.isAfter(LocalDate.now())) return "Date of birth cannot be in the future";
            return null;
        } catch (DateTimeParseException e) {
            return "Date of birth must be a real date in yyyy-MM-dd format";
        }
    }
}