package hiep.com.springboot.validation.validator;

import hiep.com.springboot.models.entities.UserEntity;
import hiep.com.springboot.repository.UserRepository;
import hiep.com.springboot.validation.annotation.UniqueUserName;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class UniqueUserNameValidator implements ConstraintValidator<UniqueUserName, String> {
    @Autowired
    private UserRepository userRepository;

    private String message;

    @Override
    public void initialize(UniqueUserName uniqueUserName) {
        message = uniqueUserName.message();
    }

    @Override
    public boolean isValid(String userName, ConstraintValidatorContext context) {
        if (userName == null)
            return false;

        Optional<UserEntity> userOtp = userRepository.findByUsername(userName);

        if (userOtp.isEmpty()) {
            return true;
        }

        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        return false;
    }
}