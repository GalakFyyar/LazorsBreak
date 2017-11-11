import java.util.Arrays;
import java.util.HashSet;

class CombGen{
	private static boolean[] finArr;
	private static HashSet<boolean[]> combs = new HashSet<boolean[]>();
	
	static HashSet<boolean[]> generate(int aLen, byte numberOfElements){
		boolean[] arr = new boolean[aLen];
		for(byte i = 0; i < numberOfElements; i++){//Set up true cells in init array
			arr[i] = true;
		}
		
		finArr = new boolean[aLen];
		for(byte i = 0; i < numberOfElements; i++){//Set up true cells in final array
			finArr[aLen - 1 - i] = true;
		}
		
		combs.add(arr);
		while(hasNext(arr)){
			arr = next(arr);
			combs.add(arr);
		}
		
		return combs;
	}
	
	private static boolean hasNext(boolean[] arr){
		return !Arrays.equals(finArr, arr);
	}

	private static boolean[] next(boolean[] arr){
		int len = arr.length;
		boolean[] oarr = new boolean[len];
		System.arraycopy(arr, 0, oarr, 0, len);
		
		int tail = -1;
		int shift = -1;
		if(arr[len - 1]){
			tail = 1;
			for(int i = len - 2; i > -1; i--){
				if(arr[i])
					tail++;
				else
					break;
			}
			
			for(int i = len - 1 - tail; i > -1; i--){
				if(arr[i]){
					shift = i;
					break;
				}
			}
			
			swap(oarr, shift, shift + 1);
			
			for(int i = shift + 2, j = 1; i < shift + 2 + tail; i++, j++)
				swap(oarr, i, len - j);
			
			return oarr;
		}
		
		for(int i = len - 1; i > -1; i--){
			if(arr[i]){
				swap(oarr, i, i + 1);
				break;
			}
		}
		return oarr;
	}
	
	private static void swap(boolean[] arr, int i1, int i2){
		if(i1 == i2){
			return;
		}
		if(!arr[i1] && !arr[i2]){
			return;
		}
		if(arr[i1] && arr[i2]){
			return;
		}
		arr[i1] = !arr[i1];
		arr[i2] = !arr[i2];
	}
	
	@SuppressWarnings("unused")
	static void printToString(boolean[] arr){
		StringBuilder str = new StringBuilder("]");
		for(int i = arr.length - 1; i > -1; i--){
			if(arr[i])
				str.insert(0, " 1");
			else
				str.insert(0, " 0");
		}
		System.out.println("[" + str.substring(1));
	}
}