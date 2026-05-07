package GR2272_AlejandroFernandoP5.src.models;

import java.util.Objects;

/**
 * Modelo inmutable de una persona con atributos basicos para la practica.
 */
public class Person {
    private final String name;
    private final int age;
    private final int weight;
    private final int height;
    private final boolean male;

    public Person(String name, int age, int weight, int height, boolean male) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.male = male;
    }

    /**
     * Devuelve el nombre de la persona.
     *
     * @return nombre.
     */
    public String getName() { return name; }

    /**
     * Devuelve la edad de la persona.
     *
     * @return edad.
     */
    public int getAge() { return age; }

    /**
     * Devuelve el peso de la persona.
     *
     * @return peso.
     */
    public int getWeight() { return weight; }

    /**
     * Devuelve la altura de la persona.
     *
     * @return altura.
     */
    public int getHeight() { return height; }

    /**
     * Indica si la persona es masculina.
     *
     * @return true si es hombre, false en caso contrario.
     */
    public boolean isMale() { return male; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Person other)) return false;
        return age == other.age && weight == other.weight && height == other.height &&
                male == other.male && Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, weight, height, male);
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + ", weight=" + weight + 
               ", height=" + height + ", gender=" + (male ? "MALE" : "FEMALE") + "}";
    }
}
