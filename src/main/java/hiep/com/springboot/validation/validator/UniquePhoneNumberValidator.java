package hiep.com.springboot.validation.validator;

import hiep.com.springboot.models.entities.UserEntity;
import hiep.com.springboot.repository.UserRepository;
import hiep.com.springboot.validation.annotation.UniquePhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class UniquePhoneNumberValidator implements ConstraintValidator<UniquePhoneNumber, String> {
    @Autowired
    private UserRepository userRepository;

    private String message;

    @Override
    public void initialize(UniquePhoneNumber uniquePhoneNumber) {
        message = uniquePhoneNumber.message();
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null)
            return false;

        Optional<UserEntity> userOtp = userRepository.findByPhoneNumber(phoneNumber);

        if (userOtp.isEmpty()) {
            return true;
        }

        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        return false;
    }
}
