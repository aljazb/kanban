package si.fri.smrpo.kis.server.ejb.source.interfaces;

import si.fri.smrpo.kis.core.rest.source.interfaces.CrudSourceImpl;
import si.fri.smrpo.kis.server.jpa.entities.CardMove;

import java.util.UUID;

public interface CardMoveSourceLocal extends CrudSourceImpl<CardMove, UUID>, AuthImpl {

}
