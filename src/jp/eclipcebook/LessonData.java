package jp.eclipcebook;

public class LessonData {
	public static String getLessonData(int number) {
		
		String answer = null;
		
		switch(number) {
		case 1:
			answer = "左腕を上げる\n左腕を下げる\n右腕を上げる\n右腕を下げる";
			break;
		case 2:
			answer = "左腕を上げる 右腕を上げる\n左腕を下げる\n右腕を下げる\n左腕を上げる\n右腕を上げる\n左腕を下げる 右腕を下げる\nジャンプする";
			break;
		case 3:
			answer = "左腕を上げる\n左腕を下げる\n右腕を上げる\n右腕を下げる";
			break;
		case 4:
			answer = "左腕を上げる\n左腕を下げる\n右腕を上げる\n右腕を下げる";
			break;
		default:
			answer = "null";
			break;
		}
		return answer;
	}
}
