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

    private static final Integer BOARD_PARTS_NUMBER = 4;
    private static final Integer CARD_NUMBER_MIN = 3;
    private static final Integer CARD_NUMBER_MAX = 10;


    @EJB
    private DatabaseServiceLocal database;

    private DevTeam testDevTeam;
    private Project testProject;
    private Board testBoard;
    private ArrayList<BoardPart> testBoardPartLeafs;


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

    private UserAccount genUserAccount() {
        UserAccount ua = new UserAccount();
        ua.setUsername(FAKER.name.firstName());
        ua.setEmail(FAKER.internet.email());
        ua.setFirstName(FAKER.name.firstName());
        ua.setLastName(FAKER.name.lastName());
        ua.setInRoleDeveloper(false);
        ua.setInRoleKanbanMaster(false);
        ua.setInRoleAdministrator(false);
        ua.setInRoleProductOwner(false);
        return ua;
    }

    private void generateUserAccounts() throws DatabaseException {

        testAccount = genUserAccount();
        testAccount.setId(UUID.fromString(TEST_USER_ID));
        testAccount.setEmail("test@test.com");
        testAccount.setInRoleKanbanMaster(true);
        testAccount.setInRoleAdministrator(true);
        testAccount.setInRoleProductOwner(true);
        testAccount.setInRoleDeveloper(true);
        testAccount = database.create(testAccount);

        adminAccount = genUserAccount();
        adminAccount.setId(UUID.fromString(ADMIN_USER_ID));
        adminAccount.setEmail("admin@admin.com");
        adminAccount.setInRoleAdministrator(true);
        adminAccount = database.create(adminAccount);

        developerAccount = genUserAccount();
        developerAccount.setId(UUID.fromString(DEVELOPER_USER_ID));
        developerAccount.setEmail("developer@developer.com");
        developerAccount.setInRoleDeveloper(true);
        developerAccount = database.create(developerAccount);

        kanbanMasterAccount = genUserAccount();
        kanbanMasterAccount.setId(UUID.fromString(KANBAN_MASTER_USER_ID));
        kanbanMasterAccount.setEmail("kanban@master.com");
        kanbanMasterAccount.setInRoleKanbanMaster(true);
        kanbanMasterAccount = database.create(kanbanMasterAccount);

        productOwnerAccount = genUserAccount();
        productOwnerAccount.setId(UUID.fromString(PRODUCT_OWNER_USER_ID));
        productOwnerAccount.setEmail("product@owner.com");
        productOwnerAccount.setInRoleProductOwner(true);
        productOwnerAccount = database.create(productOwnerAccount);
    }

    private void generateDevTeams() throws DatabaseException {

        testDevTeam = new DevTeam();
        testDevTeam.setName(FAKER.company.name());
        testDevTeam = database.create(testDevTeam);

        Membership m = new Membership();
        m.setUserAccount(kanbanMasterAccount);
        m.setDevTeam(testDevTeam);
        m.setMemberType(MemberType.KANBAN_MASTER);
        database.create(m);

        m = new Membership();
        m.setUserAccount(productOwnerAccount);
        m.setDevTeam(testDevTeam);
        m.setMemberType(MemberType.PRODUCT_OWNER);
        database.create(m);

        m = new Membership();
        m.setUserAccount(developerAccount);
        m.setDevTeam(testDevTeam);
        m.setMemberType(MemberType.DEVELOPER);
        database.create(m);
    }

    private void generateBoard() throws DatabaseException {

        testBoard = new Board();
        testBoard.setOwner(kanbanMasterAccount);
        testBoard.setName(FAKER.app.name());
        testBoard = database.create(testBoard);

    }

    private void generateBoardParts() throws DatabaseException {
        testBoardPartLeafs = new ArrayList<>();

        for(int i=0; i<4; i++){
            BoardPart bp = new BoardPart();
            bp.setOrderIndex(i);
            bp.setBoard(testBoard);
            bp.setMaxWip(FAKER.number.between(5, 15));
            bp.setName(FAKER.app.name());
            bp.setLeaf(true);

            bp = database.create(bp);

            switch (i) {
                case 0: testBoard.setHighestPriority(bp.getId()); break;
                case 1: testBoard.setStartDev(bp.getId()); break;
                case 2: testBoard.setEndDev(bp.getId()); break;
                case 3: testBoard.setAcceptanceTesting(bp.getId()); break;
            }
            testBoard = database.update(testBoard);

            if(Math.random() > 0.5) {

                bp.setLeaf(false);
                database.update(bp);

                for(int j=0; j<2; j++){
                    BoardPart sbp = new BoardPart();
                    sbp.setOrderIndex(j);
                    sbp.setBoard(testBoard);
                    sbp.setMaxWip(FAKER.number.between(5, 15));
                    sbp.setName(FAKER.app.name());
                    sbp.setParent(bp);
                    sbp.setLeaf(true);

                    sbp = database.create(sbp);

                    testBoardPartLeafs.add(sbp);
                }
            } else {
                testBoardPartLeafs.add(bp);
            }
        }
    }

    private void generateProjects() throws DatabaseException {

        testProject = new Project();
        testProject.setCode(FAKER.app.name().toLowerCase().replaceAll(" ", "-"));
        testProject.setName(FAKER.app.name());
        testProject.setProductBuyer(FAKER.company.name());
        testProject.setDescription(FAKER.lorem.characters(150));
        testProject.setStartDate(FAKER.date.forward(1));
        testProject.setEndDate(FAKER.date.forward(FAKER.number.between(40, 100)));

        testProject.setOwner(kanbanMasterAccount);
        testProject.setDevTeam(testDevTeam);

        testProject.setBoard(testBoard);

        testProject = database.create(testProject);

    }

    private void generateCards() throws DatabaseException {

        for(int i=0; i<5; i++) {
            BoardPart bp = testBoardPartLeafs.get(FAKER.number.between(0, testBoardPartLeafs.size()));

            Card c = new Card();

            c.setProject(testProject);
            c.setBoardPart(bp);
            c.setOrderIndex(i);

            c.setName(FAKER.app.name());
            c.setDescription(FAKER.lorem.characters(50));
            c.setWorkload(FAKER.number.between(1, 10));
            c.setColor("#9cec9c");

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

    private void generateRequests() throws DatabaseException {

        Request request = new Request();
        request.setRequestType(RequestType.KANBAN_MASTER_INVITE);
        request.setContext("Invite to become kanban master for dev team " + testDevTeam.getName());
        request.setRequestStatus(RequestStatus.PENDING);
        request.setSender(kanbanMasterAccount);
        request.setReceiver(testAccount);
        request.setReferenceId(testDevTeam.getId());

        database.create(request);
    }

}
