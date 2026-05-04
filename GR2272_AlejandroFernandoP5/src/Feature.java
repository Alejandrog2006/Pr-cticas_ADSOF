package GR2272_AlejandroFernandoP5.src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Feature<T extends Comparable<? super T>> extends ArrayList<T>{
	
	public T min() {
		return isEmpty() ? null : Collections.min(this);
	}

	public T max() {
		return isEmpty() ? null : Collections.max(this);
	}

	public Map<T, Integer> distribution() {
		Map<T, Integer> frequencies = new LinkedHashMap<>();
		for (T value : this) {
			frequencies.merge(value, 1, Integer::sum); // si no hay valor agrega 1, sino le suma 1 a la frecuencia
		}
		return frequencies;
	}
}
