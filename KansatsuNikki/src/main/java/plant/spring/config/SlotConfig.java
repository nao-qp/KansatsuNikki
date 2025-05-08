package plant.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SlotConfig {
	@Value("${app.slot.max-count}")
    private int maxSlotCount;

    public int getMaxSlotCount() {
        return maxSlotCount;
    }
}
