package m2u.eyelink.aibot;

public interface IConstants {
	
	interface Type {
		interface Message {
			String TEXT = "text";
			String PHOTO = "photo";
		}
		interface Keyboard {
			String TEXT = "text";
			String BUTTONS = "buttons";
		}
	}
	
	interface CHARSET {
		String DFLT_CHARSET = "utf-8";
	}
	
}
