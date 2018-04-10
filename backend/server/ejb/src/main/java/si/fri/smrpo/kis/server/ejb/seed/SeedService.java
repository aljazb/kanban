package si.fri.smrpo.kis.server.ejb.seed;

import io.bloco.faker.Faker;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.jpa.entities.*;
import si.fri.smrpo.kis.server.jpa.entities.Membership;
import si.fri.smrpo.kis.server.jpa.enums.MemberType;
import si.fri.smrpo.kis.server.jpa.enums.RequestStatus;
import si.fri.smrpo.kis.server.jpa.enums.RequestType;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.*;
import java.util.stream.Collectors;

@Startup
@Singleton
public class SeedService {

    private static final Faker FAKER = new Faker();

    private static final String TEST_USER_ID = "b0ee9324-e4bc-40a3-8068-d4f7042dae60";
    private static final String ADMIN_USER_ID = "29b3a75e-0c4f-4aa7-a505-5290af252abe";
    private static final String DEVELOPER_USER_ID = "c8bca3a6-b49e-45fe-bd14-af82472933e9";
    private static final String KANBAN_MASTER_USER_ID = "944848a6-be17-404e-8ecc-a9247a094702";
    private static final String PRODUCT_OWNER_USER_ID = "37f2b970-63ee-4c36-bafb-2447d3a405ad";

    private UserAccount testAccount;
    private UserAccount adminAccount;
    private UserAccount developerAccount;
    private UserAccount kanbanMasterAccount;
    private UserAccount productOwnerAccount;

    private static final Integer USERS_NUMBER = 15;
    private static final Integer DEV_TEAMS_NUMBER = 5;
    private static final Integer DEV_TEAM_MEMBERS_NUMBER_MIN = 3;
    private static final Integer DEV_TEAM_MEMBERS_NUMBER_MAX = 10;
    private static final Integer BOARD_PARTS_NUMBER = 4;
    private static final Integer CARD_NUMBER_MIN = 3;
    private static final Integer CARD_NUMBER_MAX = 20;


    @EJB
    private DatabaseServiceLocal database;

    private DevTeam testDevTeam;

    private ArrayList<UserAccount> userAccounts = new ArrayList<>();
    private ArrayList<DevTeam> devTeams = new ArrayList<>();
    private HashMap<UUID, ArrayList<Membership>> devTeamMembers = new HashMap<>();
    private HashMap<UUID, Board> userBoard = new HashMap<>();
    private HashMap<UUID, ArrayList<BoardPart>> boardParts = new HashMap<>();
    private ArrayList<Project> projects = new ArrayList<>();



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
            ua.setUsername(FAKER.name.firstName());
            ua.setEmail(FAKER.internet.email());
            ua.setFirstName(FAKER.name.firstName());
            ua.setLastName(FAKER.name.lastName());
            ua.setInRoleDeveloper(true);
            ua.setInRoleKanbanMaster(false);
            ua.setInRoleAdministrator(false);
            ua.setInRoleProductOwner(false);

            if(i == 0){
                ua.setId(UUID.fromString(TEST_USER_ID));
                ua.setEmail("test@test.com");
                ua.setInRoleKanbanMaster(true);
                ua.setInRoleAdministrator(true);
                ua.setInRoleProductOwner(true);
                ua.setInRoleDeveloper(true);
                testAccount = ua;
            } else if (i == 1) {
                ua.setId(UUID.fromString(ADMIN_USER_ID));
                ua.setEmail("admin@admin.com");
                ua.setInRoleKanbanMaster(false);
                ua.setInRoleAdministrator(true);
                ua.setInRoleProductOwner(false);
                ua.setInRoleDeveloper(false);
                adminAccount = ua;
            } else if (i == 2) {
                ua.setId(UUID.fromString(DEVELOPER_USER_ID));
                ua.setEmail("developer@developer.com");
                ua.setInRoleKanbanMaster(false);
                ua.setInRoleAdministrator(false);
                ua.setInRoleProductOwner(false);
                ua.setInRoleDeveloper(true);
                developerAccount = ua;
            } else if (i == 3) {
                ua.setId(UUID.fromString(KANBAN_MASTER_USER_ID));
                ua.setEmail("kanban@master.com");
                ua.setInRoleKanbanMaster(true);
                ua.setInRoleAdministrator(false);
                ua.setInRoleProductOwner(false);
                ua.setInRoleDeveloper(true);
                kanbanMasterAccount = ua;
            } else if (i == 4) {
                ua.setId(UUID.fromString(PRODUCT_OWNER_USER_ID));
                ua.setEmail("product@owner.com");
                ua.setInRoleKanbanMaster(false);
                ua.setInRoleAdministrator(false);
                ua.setInRoleProductOwner(true);
                ua.setInRoleDeveloper(false);
                productOwnerAccount = ua;
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

            ArrayList<Membership> dtMembers = new ArrayList<>();
            ArrayList<UserAccount> potentialMembers = new ArrayList<>(userAccounts);
            int members = FAKER.number.between(DEV_TEAM_MEMBERS_NUMBER_MIN, DEV_TEAM_MEMBERS_NUMBER_MAX);
            for(int m=0; m<members; m++) {
                int index = FAKER.number.between(0, potentialMembers.size());

                if(i == 0 && m == 0){
                    index = 0;
                    testDevTeam = dt;
                    testDevTeam.setName("Test Dev Team");
                    database.update(testDevTeam);
                }

                UserAccount ua = potentialMembers.get(index);
                potentialMembers.remove(index);

                Membership uaMTMdt = new Membership();
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
        for(UserAccount ua : userAccounts) {
            Board b = new Board();
            b.setOwner(ua);
            b.setName(FAKER.app.name());
            b.setHighestPriority(0);
            b.setStartDev(1);
            b.setEndDev(2);
            b.setAcceptenceTesting(3);

            b = database.create(b);

            userBoard.put(ua.getId(), b);
        }
    }

    private void generateBoardParts() throws DatabaseException {
        for(Board board : userBoard.values()){
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
                Membership uaMTMdt = devTeamMembers.get(dt.getId()).stream()
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
                Board b = userBoard.get(dt.getId());

            }
        }
    }

    private void generateCards() throws DatabaseException {
        for(Project p : projects) {

            ArrayList<Membership> members = devTeamMembers.get(p.getDevTeam().getId());

            Membership m = members.stream().filter(e ->
                    e.getMemberType() == MemberType.DEVELOPER_AND_KANBAN_MASTER ||
                    e.getMemberType() == MemberType.KANBAN_MASTER).findFirst().orElse(null);

            Board b = userBoard.get(m.getUserAccount().getId());
            ArrayList<BoardPart> bpLeaves = boardParts.get(b.getId());

            int cardsNumber = FAKER.number.between(CARD_NUMBER_MIN, CARD_NUMBER_MAX);
            for(int i=0; i<cardsNumber; i++){
                BoardPart bp = bpLeaves.get(FAKER.number.between(0, bpLeaves.size()-1));

                Card c = new Card();

                c.setProject(p);
                c.setBoardPart(bp);
                c.setProject(p);
                c.setOrderIndex(i);

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

        Request request = new Request();
        request.setRequestType(RequestType.KANBAN_MASTER_INVITE);
        request.setContext("Invite to become kanban master for dev team " + testDevTeam.getName());
        request.setRequestStatus(RequestStatus.PENDING);
        request.setSender(testAccount);
        request.setReceiver(kanbanMasterAccount);
        request.setReferenceId(testDevTeam.getId());

        database.create(request);
    }

}
