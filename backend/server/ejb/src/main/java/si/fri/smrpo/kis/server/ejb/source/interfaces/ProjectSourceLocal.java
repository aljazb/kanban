package si.fri.smrpo.kis.server.ejb.source.interfaces;

import si.fri.smrpo.kis.core.rest.source.interfaces.CrudSourceImpl;
import si.fri.smrpo.kis.server.jpa.entities.Project;

import java.util.UUID;

public interface ProjectSourceLocal extends CrudSourceImpl<Project, UUID>, AuthImpl {


}
