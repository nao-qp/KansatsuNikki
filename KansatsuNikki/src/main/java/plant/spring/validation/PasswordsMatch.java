package plant.spring.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.TYPE })  // このアノテーションはクラスに適用されることを示します。
@Retention(RetentionPolicy.RUNTIME)  // アノテーションが実行時に利用できることを示します。
@Constraint(validatedBy = PasswordsMatchValidator.class)  // どのバリデータで検証するか指定します。
public @interface PasswordsMatch {
	String message() default "パスワードが一致しません";  // エラーメッセージのデフォルト値
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
