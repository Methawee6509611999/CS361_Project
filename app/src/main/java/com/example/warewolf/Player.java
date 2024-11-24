package com.example.warewolf;

public class Player {
    private String name;
    private String role; // E.g., "Villager", "Werewolf", "Seer", etc.
    private boolean isAlive;

    public Player(String name, String role) {
        this.name = name;
        this.role = role;
        this.isAlive = true; // Initially alive
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role= role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAlive() {
        return isAlive;
    }
}
