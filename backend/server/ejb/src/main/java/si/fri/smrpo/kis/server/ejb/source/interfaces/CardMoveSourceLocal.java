package si.fri.smrpo.kis.server.ejb.source.interfaces;

import si.fri.smrpo.kis.core.rest.source.interfaces.GetSourceImpl;
import si.fri.smrpo.kis.server.jpa.entities.CardMove;

import java.util.UUID;

public interface CardMoveSourceLocal extends GetSourceImpl<CardMove, UUID>, AuthImpl {

    CardMove create(CardMove card) throws Exception;

}
