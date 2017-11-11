@SuppressWarnings({"unused", "SpellCheckingInspection"})
class Test{
	static void test(int pos, boolean cwis){
		boolean fwd;
		int clzr;
		
		int senario = pos | (cwis ? 1 : 0) << 2;
		System.out.println(senario);
		switch(senario){
			case 0:
				clzr = 0;
				fwd = false;
				break;
			case 1:
				clzr = 0;
				fwd = false;
				break;
			case 2:
				clzr = 0;
				fwd = false;
				break;
			case 3:
				clzr = 0;
				fwd = false;
				break;
			case 4:
				clzr = 0;
				fwd = false;
				break;
			case 5:
				clzr = 0;
				fwd = false;
				break;
			case 6:
				clzr = 0;
				fwd = false;
				break;
			case 7:
				clzr = 0;
				fwd = false;
				break;
		}
	}
}
