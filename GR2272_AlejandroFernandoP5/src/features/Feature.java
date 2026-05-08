package features;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Lista de valores comparables que representa una feature extraida de un dataset.
 * Permite calcular estadisticos basicos como minimo, maximo y distribucion.
 *
 * @param <T> tipo de los valores almacenados en la feature.
 */
public class Feature<T extends Comparable<? super T>> extends ArrayList<T>{
	
	/**
	 * Devuelve el valor minimo de la feature.
	 *
	 * @return el minimo, o null si la feature esta vacia.
	 */
	public T min() {
		return isEmpty() ? null : Collections.min(this);
	}

	/**
	 * Devuelve el valor maximo de la feature.
	 *
	 * @return el maximo, o null si la feature esta vacia.
	 */
	public T max() {
		return isEmpty() ? null : Collections.max(this);
	}

	/**
	 * Calcula la distribucion de frecuencias de los valores almacenados.
	 *
	 * @return un mapa con cada valor y su frecuencia.
	 */
	public Map<T, Integer> distribution() {
		Map<T, Integer> frequencies = new LinkedHashMap<>();
		for (T value : this) {
			frequencies.merge(value, 1, Integer::sum); // si no hay valor agrega 1, sino le suma 1 a la frecuencia
		}
		return frequencies;
	}
}
