package si.fri.smrpo.kis.server.ejb.seed;

import io.bloco.faker.Faker;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.core.logic.exceptions.BusinessLogicTransactionException;
import si.fri.smrpo.kis.server.jpa.entities.Board;
import si.fri.smrpo.kis.server.jpa.entities.BoardPart;
import si.fri.smrpo.kis.server.jpa.entities.DevTeam;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;
import si.fri.smrpo.kis.server.jpa.entities.mtm.UserAccountMtmDevTeam;
import si.fri.smrpo.kis.server.jpa.enums.MemberType;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Startup
@Singleton
public class SeedService {

    private static final Faker FAKER = new Faker();

    private static final Integer USERS_NUMBER = 100;
    private static final Integer DEV_TEAMS_NUMBER = 10;
    private static final Integer DEV_TEAM_MEMBERS_NUMBER_MIN = 5;
    private static final Integer DEV_TEAM_MEMBERS_NUMBER_MAX = 15;
    private static final Integer BOARD_PARTS_NUMBER = 4;


    @EJB
    private DatabaseServiceLocal database;

    @PostConstruct
    public void init() {
        if(isDatabaseEmpty()){
            try {
                ArrayList<UserAccount> userAccounts = generateUserAccounts();
                ArrayList<DevTeam> devTeams = generateDevTeams(userAccounts);
                ArrayList<Board> boards = generateBoard(devTeams);


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

            Set<UserAccountMtmDevTeam> joinedMembers = new HashSet<>();
            ArrayList<UserAccount> potentialMembers = new ArrayList<>(userAccounts);
            int members = FAKER.number.between(DEV_TEAM_MEMBERS_NUMBER_MIN, DEV_TEAM_MEMBERS_NUMBER_MAX);
            for(int m=0; m<members; m++) {
                int index = FAKER.number.between(0, potentialMembers.size());

                UserAccount ua = potentialMembers.get(index);
                potentialMembers.remove(index);

                UserAccountMtmDevTeam uaMTMdt = new UserAccountMtmDevTeam();
                uaMTMdt.setUserAccount(ua);
                uaMTMdt.setDevTeam(dt);

                if(m == 0) {
                    uaMTMdt.setMemberType(MemberType.KAMBAN_MASTER);
                } else {
                    uaMTMdt.setMemberType(MemberType.DEVELOPER);
                }

                uaMTMdt = database.create(uaMTMdt);

                joinedMembers.add(uaMTMdt);
            }

            dt.setJoinedUsers(joinedMembers);
        }

        return devTeams;
    }

    private ArrayList<BoardPart> generateBoardParts(Board board) throws BusinessLogicTransactionException {
        ArrayList<BoardPart> boardParts = new ArrayList<>();

        for(int i=0; i<BOARD_PARTS_NUMBER; i++){
            BoardPart bp = new BoardPart();
            bp.setBoard(board);
            bp.setMaxWip(FAKER.number.between(5, 15));
            bp.setName(FAKER.app.name());

            bp = database.create(bp);

            if(Math.random() > 0.5){
                for(int j=0; j<2; j++){
                    BoardPart sbp = new BoardPart();
                    sbp.setBoard(board);
                    sbp.setMaxWip(FAKER.number.between(5, 15));
                    sbp.setName(FAKER.app.name());
                    sbp.setParent(bp);

                    sbp = database.create(sbp);

                    boardParts.add(sbp);
                }
            }
            boardParts.add(bp);
        }

        return boardParts;
    }

    private ArrayList<Board> generateBoard(ArrayList<DevTeam> devTeams) throws BusinessLogicTransactionException {
        ArrayList<Board> boards = new ArrayList<>();

        for(DevTeam dt : devTeams) {
            Board b = new Board();
            b.setDevTeam(dt);
            b.setName(FAKER.app.name());

            b = database.create(b);

            ArrayList<BoardPart> boardParts = generateBoardParts(b);
            b.setBoardParts(new HashSet<>(boardParts));

            boards.add(b);
        }

        return boards;
    }

}
