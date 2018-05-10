package si.fri.smrpo.kis.server.ejb.source.interfaces;

import si.fri.smrpo.kis.core.rest.source.interfaces.GetSourceImpl;
import si.fri.smrpo.kis.server.jpa.entities.CardMove;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import javax.xml.registry.infomodel.User;
import java.util.UUID;

public interface CardMoveSourceLocal extends GetSourceImpl<CardMove, UUID, UserAccount> {

    CardMove create(CardMove card, UserAccount authUser) throws Exception;

}
