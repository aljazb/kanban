package si.fri.smrpo.kis.server.ejb.source.interfaces;

import si.fri.smrpo.kis.core.rest.source.interfaces.CrudSourceImpl;
import si.fri.smrpo.kis.server.jpa.entities.Card;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import java.util.UUID;

public interface CardSourceLocal extends CrudSourceImpl<Card, UUID>, AuthImpl {

}