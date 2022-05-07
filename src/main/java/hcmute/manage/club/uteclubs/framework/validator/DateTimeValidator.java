package hcmute.manage.club.uteclubs.framework.validator;

import hcmute.manage.club.uteclubs.framework.common.CommonConstant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class DateTimeValidator implements ConstraintValidator<DateTimeConstraint, String> {

  @Override
  public void initialize(DateTimeConstraint constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String input, ConstraintValidatorContext constraintValidatorContext) {
    if (StringUtils.isEmpty(input)) {
      return true;
    }
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(
        CommonConstant.DATE_TIME_PATTERN);
    try {
      LocalDateTime dateTime = LocalDateTime.parse(input, dateTimeFormatter);
      String dateTimeInString = dateTime.format(dateTimeFormatter);
      return input.equals(dateTimeInString);
    } catch (Exception ex) {
      return false;
    }
  }
}
