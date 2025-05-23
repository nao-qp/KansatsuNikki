package plant.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	// 画像表示URLを物理ディレクトリに読み替えるためのクラス
	// /uploads/・・・でアクセスすると、物理ディレクトリ/uploads・・・に自動で読み替える(該当箇所に実装は不要)
	
	@Value("${app.upload-static-dir}")
    private String uploadStaticDir;			// (例: /Users/hosoyanaomi/00gitProjectWork/Kansatsunikki/)
			// 例: C:\00gitProjectWork\Kansatusnikki\
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadStaticDir + "uploads/")
                .setCachePeriod(0) // キャッシュ無効（開発中）
                .resourceChain(false); // キャッシュチェーンも無効化
    }
}
