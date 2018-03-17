package si.fri.smrpo.kis.app.server.ejb.seed;

import io.bloco.faker.Faker;
import si.fri.smrpo.kis.app.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.core.businessLogic.exceptions.BusinessLogicTransactionException;
import si.fri.smrpo.kis.core.jpa.entities.DevTeam;
import si.fri.smrpo.kis.core.jpa.entities.UserAccount;
import si.fri.smrpo.kis.core.jpa.entities.mtm.UserAccountMtmDevTeam;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.ArrayList;
import java.util.List;

@Startup
@Singleton
public class SeedService {

    @EJB
    private DatabaseServiceLocal database;

    @PostConstruct
    public void init() {
        Faker faker = new Faker();

        if(isDatabaseEmpty()){
            try {
                userAccounts(faker, 100);
            } catch (BusinessLogicTransactionException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isDatabaseEmpty(){
        List<UserAccount> userAccounts = database.getStream(UserAccount.class).limit(1).toList();
        return userAccounts.isEmpty();
    }

    private ArrayList<UserAccount> userAccounts(Faker faker, int number) throws BusinessLogicTransactionException {
        ArrayList<UserAccount> userAccounts = new ArrayList<>();

        for(int i=0; i<number; i++){
            UserAccount ua = new UserAccount();
            ua.setEmail(faker.internet.email());
            ua.setFirstName(faker.name.firstName());
            ua.setLastName(faker.name.lastName());

            ua = database.create(ua, null, null);
            userAccounts.add(ua);
        }

        return userAccounts;
    }

    private ArrayList<DevTeam> devTeams(Faker faker, int number, ArrayList<UserAccount> userAccounts) throws BusinessLogicTransactionException {
        ArrayList<DevTeam> devTeams = new ArrayList<>();

        for(int i=0; i<number; i++){
            DevTeam dt = new DevTeam();
            dt.setName(faker.company.name());

            //dt = database.create(dt);

            devTeams.add(dt);

            ArrayList<UserAccount> potentialMembers = new ArrayList<>(userAccounts);
            int members = faker.number.between(5, 10);
            for(int m=0; m<members; m++) {
                int index = faker.number.between(0, potentialMembers.size());
                UserAccount ua = potentialMembers.get(index);
                UserAccountMtmDevTeam uaMTMdt = new UserAccountMtmDevTeam();
                uaMTMdt.setUserAccount(ua);
                uaMTMdt.setDevTeam(dt);

                //database.create(uaMTMdt);
            }
        }

        return devTeams;
    }

}
