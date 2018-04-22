package si.fri.smrpo.kis.server.ejb.source.interfaces;

import si.fri.smrpo.kis.core.rest.source.interfaces.CrudSourceImpl;
import si.fri.smrpo.kis.server.jpa.entities.Board;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import java.util.UUID;

public interface BoardSourceLocal extends CrudSourceImpl<Board, UUID>, AuthImpl {

}
