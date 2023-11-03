package util;

import java.util.Objects;

public class Actor implements Comparable<Actor> {

    private final String name;

    private final String surname;

    public Actor(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    @Override
    public String toString() {
        return name + " " + surname;
    }

    @Override
    public int compareTo(Actor otherActor) {
        return toString().compareTo(otherActor.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return name.equals(actor.name) &&
                surname.equals(actor.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname);
    }
}
