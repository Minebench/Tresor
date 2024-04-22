package de.minebench.tresor.services.npc.skin;

public class NPCSkinData {

    private final String texture;
    private final String signature;

    public NPCSkinData(String texture, String signature) {
        this.texture = texture;
        this.signature = signature;
    }

    public String getTexture() {
        return texture;
    }

    public String getSignature() {
        return signature;
    }
}
