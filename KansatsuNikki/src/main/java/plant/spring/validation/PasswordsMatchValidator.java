package plant.spring.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import plant.spring.form.SignupForm;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, SignupForm> {

    @Override
    public void initialize(PasswordsMatch constraintAnnotation) {
        // 初期化が必要な場合に使用
    }

    @Override
    public boolean isValid(SignupForm form, ConstraintValidatorContext context) {

        if (form == null || form.getPass() == null || form.getConfirmPass() == null) {
            return true; // Null チェックは外部で行う
        }

        // パスワードと確認用パスワードが一致するかをチェック
        return form.getPass().equals(form.getConfirmPass());
    }
}
