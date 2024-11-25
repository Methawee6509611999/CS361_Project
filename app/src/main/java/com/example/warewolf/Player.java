package com.example.warewolf;
import android.os.Parcel;
import android.os.Parcelable;

public class Player implements Parcelable{
    private String name;
    private String role; // E.g., "Villager", "Werewolf", "Seer", etc.
    private boolean isAlive;

    public Player(String name, String role) {
        this.name = name;
        this.role = role;
        this.isAlive = true; // Initially alive
    }

    // Parcelable constructor
    protected Player(Parcel in) {
        name = in.readString();  // Reading from the Parcel
        role = in.readString();
        isAlive = in.readByte() != 0; // Read boolean (stored as a byte)
    }

    // Method to write data into the Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);  // Writing to the Parcel
        dest.writeString(role);
        dest.writeByte((byte) (isAlive ? 1 : 0)); // Write boolean as a byte
    }


    // Describe any special contents (not needed in most cases)
    @Override
    public int describeContents() {
        return 0;
    }

    // Creator to help reconstruct the object from the Parcel
    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);  // Reconstruct the Player object
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];  // Create an array of Player objects
        }
    };

    public void Die() {isAlive = false;}

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
