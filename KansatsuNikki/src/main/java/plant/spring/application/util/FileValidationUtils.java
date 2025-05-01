package plant.spring.application.util;

import org.springframework.web.multipart.MultipartFile;

//ファイルチェックstaticメソッドのクラス
public class FileValidationUtils {
	// インスタンス化禁止（コンストラクタをprivateにする）
    private FileValidationUtils() {}

    /**
     * ファイルのMIMEタイプ検証
     * @param file
     * ファイルのnull,空チェック、MINEタイプのチェックを行う。
     * MINEタイプは"image/jpeg","image/png"のみ許可。
     */
    public static void validateImageContentType(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("ファイルが選択されていません");
        }

        String contentType = file.getContentType();
        if (!(contentType != null && (contentType.equals("image/jpeg") || contentType.equals("image/png")))) {
        									// jpgのMINEタイプはimage/jpeg
            throw new IllegalArgumentException("許可されていないコンテンツタイプです");
        }
    }
}
