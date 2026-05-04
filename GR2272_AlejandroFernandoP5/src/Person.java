package GR2272_AlejandroFernandoP5.src;

import java.util.Objects;

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

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getWeight() {
        return weight;
    }

    public int getHeight() {
        return height;
    }

    public boolean isMale() {
        return male;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Person other)) {
            return false;
        }
        return age == other.age
                && weight == other.weight
                && height == other.height
                && male == other.male
                && Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, weight, height, male);
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + ", weight=" + weight + ", height=" + height + ", gender=" + (male ? "MALE" : "FEMALE") + "}";
    }
}