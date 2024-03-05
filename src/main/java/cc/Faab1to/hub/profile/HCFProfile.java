package cc.invictusgames.hub.profile;

import cc.invictusgames.hub.profile.deathban.Deathban;
import lombok.Data;
import org.bson.Document;

@Data
public class HCFProfile {

    private int kills = 0;
    private int deaths = 0;

    private int lives = 0;
    private Deathban deathban = null;

    public HCFProfile(Document document) {
        Document statistic = (Document) document.get("statistic");
        kills = statistic.getInteger("kills");
        deaths = statistic.getInteger("deaths");
        lives = statistic.getInteger("lives");

        if (document.containsKey("deathban")) {
            Document deathbanDocument = document.get("deathban", Document.class);
            if (deathbanDocument != null)
                this.deathban = new Deathban(deathbanDocument);
        }
    }
}
