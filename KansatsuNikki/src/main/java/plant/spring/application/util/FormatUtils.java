package plant.spring.application.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

// フォーマットメソッドクラス
public class FormatUtils {

	/**
	 * LocalDate型を"yyyy年MM月dd日 (E)"にフォーマットする
	 * @param date
	 * @return String "yyyy年MM月dd日 (E)"
	 */
	public static String formatToJapaneseDateString(LocalDate date) {
		if (date == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 (E)", Locale.JAPANESE);
        return date.format(formatter);
	}
}
