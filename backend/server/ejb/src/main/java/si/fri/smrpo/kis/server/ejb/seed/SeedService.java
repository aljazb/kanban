package si.fri.smrpo.kis.server.ejb.seed;

import io.bloco.faker.Faker;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.jpa.entities.*;
import si.fri.smrpo.kis.server.jpa.entities.mtm.UserAccountMtmDevTeam;
import si.fri.smrpo.kis.server.jpa.enums.MemberType;
import si.fri.smrpo.kis.server.jpa.enums.RequestStatus;
import si.fri.smrpo.kis.server.jpa.enums.RequestType;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.*;
import java.util.stream.Collectors;

import static si.fri.smrpo.kis.server.ejb.managers.base.AuthManager.*;

@Startup
@Singleton
public class SeedService {

    private static final Faker FAKER = new Faker();

    private static final String TEST_USER_ID = "b0ee9324-e4bc-40a3-8068-d4f7042dae60";

    private static final Integer USERS_NUMBER = 100;
    private static final Integer DEV_TEAMS_NUMBER = 10;
    private static final Integer DEV_TEAM_MEMBERS_NUMBER_MIN = 7;
    private static final Integer DEV_TEAM_MEMBERS_NUMBER_MAX = 15;
    private static final Integer BOARD_PARTS_NUMBER = 4;
    private static final Integer CARD_NUMBER_MIN = 3;
    private static final Integer CARD_NUMBER_MAX = 20;


    @EJB
    private DatabaseServiceLocal database;

    private UserAccount root;
    private DevTeam rootDevTeam;

    private ArrayList<UserAccount> userAccounts = new ArrayList<>();
    private ArrayList<DevTeam> devTeams = new ArrayList<>();
    private HashMap<UUID, ArrayList<UserAccountMtmDevTeam>> devTeamMembers = new HashMap<>();
    private HashMap<UUID, Board> devTeamBoard = new HashMap<>();
    private HashMap<UUID, ArrayList<BoardPart>> boardParts = new HashMap<>();
    private ArrayList<Project> projects = new ArrayList<>();
    private HashMap<UUID, BoardLane> projectBoardLane = new HashMap<>();



    @PostConstruct
    public void init() {
        if(isDatabaseEmpty()){
            try {
                generateUserAccounts();
                generateDevTeams();
                generateBoard();
                generateBoardParts();
                generateProjects();
                generateCards();
                generateRequests();
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isDatabaseEmpty(){
        List<UserAccount> userAccounts = database.getEntityManager()
                .createNamedQuery("user-account.get-all", UserAccount.class)
                .setMaxResults(1).getResultList();

        return userAccounts.isEmpty();
    }

    private void generateUserAccounts() throws DatabaseException {
        for(int i=0; i<USERS_NUMBER; i++){
            UserAccount ua = new UserAccount();
            ua.setUsername(FAKER.app.author());
            ua.setEmail(FAKER.internet.email());
            ua.setFirstName(FAKER.name.firstName());
            ua.setLastName(FAKER.name.lastName());
            ua.setInRoleDeveloper(true);
            ua.setInRoleKanbanMaster(false);
            ua.setInRoleAdministrator(false);
            ua.setInRoleProductOwner(false);

            if(i == 0){
                ua.setId(UUID.fromString(TEST_USER_ID));
                ua.setInRoleKanbanMaster(true);
                ua.setInRoleAdministrator(true);
                ua.setInRoleProductOwner(true);
                root = ua;
            }

            ua = database.create(ua);
            userAccounts.add(ua);
        }
    }

    private void generateDevTeams() throws DatabaseException {
        for(int i=0; i<DEV_TEAMS_NUMBER; i++){
            DevTeam dt = new DevTeam();
            dt.setName(FAKER.company.name());

            dt = database.create(dt);

            devTeams.add(dt);

            ArrayList<UserAccountMtmDevTeam> dtMembers = new ArrayList<>();
            ArrayList<UserAccount> potentialMembers = new ArrayList<>(userAccounts);
            int members = FAKER.number.between(DEV_TEAM_MEMBERS_NUMBER_MIN, DEV_TEAM_MEMBERS_NUMBER_MAX);
            for(int m=0; m<members; m++) {
                int index = FAKER.number.between(0, potentialMembers.size());

                if(i == 0 && m == 0){
                    index = 0;
                    rootDevTeam = dt;
                }

                UserAccount ua = potentialMembers.get(index);
                potentialMembers.remove(index);

                UserAccountMtmDevTeam uaMTMdt = new UserAccountMtmDevTeam();
                uaMTMdt.setUserAccount(ua);
                uaMTMdt.setDevTeam(dt);

                if(m == 0) {
                    ua.setInRoleKanbanMaster(true);
                    uaMTMdt.setMemberType(MemberType.DEVELOPER_AND_KANBAN_MASTER);
                } else if(m == 1) {
                    ua.setInRoleProductOwner(true);
                    uaMTMdt.setMemberType(MemberType.PRODUCT_OWNER);
                } else {
                    ua.setInRoleDeveloper(true);
                    uaMTMdt.setMemberType(MemberType.DEVELOPER);
                }

                uaMTMdt = database.create(uaMTMdt);

                dtMembers.add(uaMTMdt);
            }

            devTeamMembers.put(dt.getId(), dtMembers);
        }
    }

    private void generateBoard() throws DatabaseException {
        for(DevTeam dt : devTeams) {
            Board b = new Board();
            b.setDevTeam(dt);
            b.setName(FAKER.app.name());
            b.setHighestPriority(0);
            b.setStartDev(1);
            b.setEndDev(2);
            b.setAcceptenceTesting(3);

            b = database.create(b);

            devTeamBoard.put(dt.getId(), b);
        }
    }

    private void generateBoardParts() throws DatabaseException {
        for(Board board : devTeamBoard.values()){
            ArrayList<BoardPart> leafParts = new ArrayList<>();

            for(int i=0; i<BOARD_PARTS_NUMBER; i++){
                BoardPart bp = new BoardPart();
                bp.setOrderIndex(i);
                bp.setBoard(board);
                bp.setMaxWip(FAKER.number.between(5, 15));
                bp.setName(FAKER.app.name());
                bp.setLeaf(true);

                bp = database.create(bp);

                if(Math.random() > 0.5){
                    bp.setLeaf(false);
                    database.update(bp);
                    for(int j=0; j<2; j++){
                        BoardPart sbp = new BoardPart();
                        sbp.setOrderIndex(j);
                        sbp.setBoard(board);
                        sbp.setMaxWip(FAKER.number.between(5, 15));
                        sbp.setName(FAKER.app.name());
                        sbp.setParent(bp);
                        sbp.setLeaf(true);

                        sbp = database.create(sbp);

                        leafParts.add(sbp);
                    }
                } else {
                    leafParts.add(bp);
                }
            }

            boardParts.put(board.getId(), leafParts);
        }
    }

    private void generateProjects() throws DatabaseException {
        for(DevTeam dt : devTeams) {

            int projectNum = FAKER.number.between(1,2);
            for(int i=0; i<projectNum; i++){
                UserAccountMtmDevTeam uaMTMdt = devTeamMembers.get(dt.getId()).stream()
                        .filter(e -> e.getMemberType() == MemberType.KANBAN_MASTER ||
                                e.getMemberType() == MemberType.DEVELOPER_AND_KANBAN_MASTER).findFirst().get();

                Project p = new Project();
                p.setName(FAKER.app.name());
                p.setProductBuyer(FAKER.company.name());
                p.setDescription(FAKER.lorem.characters(150));
                p.setStartDate(FAKER.date.forward(1));
                p.setEndDate(FAKER.date.forward(FAKER.number.between(40, 100)));

                p.setOwner(uaMTMdt.getUserAccount());
                p.setDevTeam(dt);

                p = database.create(p);

                projects.add(p);
                Board b = devTeamBoard.get(dt.getId());

                BoardLane bl = new BoardLane();
                bl.setBoard(b);
                bl.setProject(p);
                bl.setName(FAKER.app.name());

                bl = database.create(bl);

                projectBoardLane.put(p.getId(), bl);
            }
        }
    }

    private void generateCards() throws DatabaseException {
        for(Project p : projects) {

            BoardLane bl = projectBoardLane.get(p.getId());
            ArrayList<BoardPart> bpLeaves = boardParts.get(bl.getBoard().getId());

            int cardsNumber = FAKER.number.between(CARD_NUMBER_MIN, CARD_NUMBER_MAX);
            for(int i=0; i<cardsNumber; i++){
                BoardPart bp = bpLeaves.get(FAKER.number.between(0, bpLeaves.size()-1));

                Card c = new Card();

                c.setBoardLane(bl);
                c.setBoardPart(bp);
                c.setProject(p);

                c.setName(FAKER.app.name());
                c.setDescription(FAKER.lorem.characters(50));
                c.setWorkload(FAKER.number.between(1, 10));

                c = database.create(c);

                int tasks = FAKER.number.between(1,5);
                for(int t=0; t<tasks; t++){
                    SubTask st = new SubTask();
                    st.setName(FAKER.app.name());
                    st.setDescription(FAKER.lorem.characters(20));
                    st.setWorkingHours(FAKER.number.between(5, 40));
                    st.setCard(c);

                    database.create(st);
                }
            }

        }
    }

    private void generateRequests() throws DatabaseException {
        List<UserAccount> members = devTeamMembers.get(rootDevTeam.getId())
                .stream()
                .map(UserAccountMtmDevTeam::getUserAccount)
                .collect(Collectors.toList());

        List<UserAccount> potentialInvites = new ArrayList<>(userAccounts);
        potentialInvites.removeAll(members);

        for(int i=0; i<5; i++) {
            UserAccount reciever = potentialInvites.get(FAKER.number.between(0, potentialInvites.size()-1));
            potentialInvites.remove(reciever);

            Request request = new Request();
            request.setRequestType(RequestType.DEV_TEAM_INVITE);
            request.setContext("Invite to dev team " + rootDevTeam.getName());
            request.setRequestStatus(RequestStatus.PENDING);
            request.setSender(root);
            request.setReceiver(reciever);
            request.setReferenceId(rootDevTeam.getId());

            database.create(request);
        }

        DevTeam dt = devTeams.get(0);
        for (int i = 0; i < devTeams.size(); i++) {
            dt = devTeams.get(i);
            if (dt != rootDevTeam) {
                break;
            }
        }
        UserAccount ua = userAccounts.get(0);
        for (int i = 0; i < userAccounts.size(); i++) {
            ua = userAccounts.get(i);
            if (ua != root) {
                break;
            }
        }
        Request requestToRoot = new Request();
        requestToRoot.setRequestType(RequestType.DEV_TEAM_INVITE);
        requestToRoot.setContext("Invite to dev team " + dt.getName());
        requestToRoot.setRequestStatus(RequestStatus.PENDING);
        requestToRoot.setSender(ua);
        requestToRoot.setReceiver(root);
        requestToRoot.setReferenceId(dt.getId());

        database.create(requestToRoot);

        Request request = new Request();
        request.setRequestType(RequestType.DEV_TEAM_KANBAN_MASTER_PROMOTION);
        request.setContext("Promotion to kanban master for dev team " + rootDevTeam.getName());
        request.setRequestStatus(RequestStatus.PENDING);
        request.setSender(root);
        request.setReceiver(members.get(2));
        request.setReferenceId(rootDevTeam.getId());

        database.create(request);
    }

}
