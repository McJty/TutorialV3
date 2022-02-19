package com.example.tutorialv3.manasystem.client;

/**
 * Class holding the data for mana client-side
 */
public class ClientManaData {

    private static int playerMana;
    private static int chunkMana;

    public static void set(int playerMana, int chunkMana) {
        ClientManaData.playerMana = playerMana;
        ClientManaData.chunkMana = chunkMana;
    }

    public static int getPlayerMana() {
        return playerMana;
    }

    public static int getChunkMana() {
        return chunkMana;
    }
}
