package co.com.crediya.loan.model.valueobjects;

import co.com.crediya.loan.model.exception.InvalidLoanRequestDataException;

import java.util.regex.Pattern;

public class Email {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final String value;

    private Email(String value) {
        this.value = value;
    }

    public static Email of(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidLoanRequestDataException("email cannot be null or empty");
        }

        if (!EMAIL_PATTERN.matcher(value.trim()).matches()) {
            throw new InvalidLoanRequestDataException("email must have a valid format");
        }

        return new Email(value.trim());
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Email email = (Email) obj;
        return value.equals(email.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
