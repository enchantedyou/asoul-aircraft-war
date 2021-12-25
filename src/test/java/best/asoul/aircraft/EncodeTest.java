package best.asoul.aircraft;

import best.asoul.aircraft.entity.ResourceType;
import best.asoul.aircraft.handler.resource.ResourceEncoder;

public class EncodeTest {

	public static void main(String[] args) {
		ResourceEncoder.encode("C:\\Users\\DELL\\Desktop\\AsoulAircraftWarResource\\sound",
				ResourceType.SOUND);
		ResourceEncoder.encode("C:\\Users\\DELL\\Desktop\\AsoulAircraftWarResource\\animation",
				ResourceType.ANIMATION);
		ResourceEncoder.encode("C:\\Users\\DELL\\Desktop\\AsoulAircraftWarResource\\image",
				ResourceType.IMAGE);
	}
}
