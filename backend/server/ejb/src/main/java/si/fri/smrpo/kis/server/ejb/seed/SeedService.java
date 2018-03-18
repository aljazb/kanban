package si.fri.smrpo.kis.server.ejb.seed;

import io.bloco.faker.Faker;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.core.logic.exceptions.BusinessLogicTransactionException;
import si.fri.smrpo.kis.server.jpa.entities.DevTeam;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;
import si.fri.smrpo.kis.server.jpa.entities.mtm.UserAccountMtmDevTeam;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.ArrayList;
import java.util.List;

@Startup
@Singleton
public class SeedService {

    private static final Faker FAKER = new Faker();

    private static final Integer USERS_NUMBER = 100;
    private static final Integer DEV_TEAMS_NUMBER = 10;
    private static final Integer DEV_TEAM_MEMBERS_NUMBER_MIN = 5;
    private static final Integer DEV_TEAM_MEMBERS_NUMBER_MAX = 15;



    @EJB
    private DatabaseServiceLocal database;

    @PostConstruct
    public void init() {
        if(isDatabaseEmpty()){
            try {
                ArrayList<UserAccount> userAccounts = generateUserAccounts();
                //ArrayList<DevTeam> devTeams = generateDevTeams(userAccounts);

            } catch (BusinessLogicTransactionException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isDatabaseEmpty(){
        List<UserAccount> userAccounts = database.getStream(UserAccount.class).limit(1).toList();
        return userAccounts.isEmpty();
    }

    private ArrayList<UserAccount> generateUserAccounts() throws BusinessLogicTransactionException {
        ArrayList<UserAccount> userAccounts = new ArrayList<>();

        for(int i=0; i<USERS_NUMBER; i++){
            UserAccount ua = new UserAccount();
            ua.setEmail(FAKER.internet.email());
            ua.setFirstName(FAKER.name.firstName());
            ua.setLastName(FAKER.name.lastName());

            ua = database.create(ua);
            userAccounts.add(ua);
        }

        return userAccounts;
    }

    private ArrayList<DevTeam> generateDevTeams(ArrayList<UserAccount> userAccounts) throws BusinessLogicTransactionException {
        ArrayList<DevTeam> devTeams = new ArrayList<>();

        for(int i=0; i<DEV_TEAMS_NUMBER; i++){
            DevTeam dt = new DevTeam();
            dt.setName(FAKER.company.name());

            dt = database.create(dt);

            devTeams.add(dt);

            ArrayList<UserAccount> potentialMembers = new ArrayList<>(userAccounts);
            int members = FAKER.number.between(DEV_TEAM_MEMBERS_NUMBER_MIN, DEV_TEAM_MEMBERS_NUMBER_MAX);
            for(int m=0; m<members; m++) {
                int index = FAKER.number.between(0, potentialMembers.size());
                UserAccount ua = potentialMembers.get(index);
                UserAccountMtmDevTeam uaMTMdt = new UserAccountMtmDevTeam();
                uaMTMdt.setUserAccount(ua);
                uaMTMdt.setDevTeam(dt);

                database.create(uaMTMdt);
            }
        }

        return devTeams;
    }

}
